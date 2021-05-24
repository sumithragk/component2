package com.tweetapp.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.tweetapp.constants.ServiceConstants;
import com.tweetapp.exception.PostNotAvailableException;
import com.tweetapp.exception.UserNotFoundException;
import com.tweetapp.model.Post;
import com.tweetapp.model.Comment;
import com.tweetapp.model.PostDestinationModel;
import com.tweetapp.repository.CommentRepository;
import com.tweetapp.repository.PostRepository;
import com.tweetapp.repository.UserRepository;
import com.tweetapp.util.DateUtil;

@Service
public class TweetService {

	@Autowired
	public MongoTemplate template;
	@Autowired
	public PostRepository postrepository;
	@Autowired
	public UserRepository userRepository;
	@Autowired
	public CommentRepository commentRepository;

	public void addPost(Post newPost, String author) throws UserNotFoundException {
		if (userRepository.findByUsername(author) != null) {
			newPost.setId(DateUtil.getCurrentTime());
			newPost.setAuthor(author);
			newPost.setDateOfPost(new Date(System.currentTimeMillis()));
			newPost.setLikesCount(0L);
			postrepository.save(newPost);
		} else
			throw new UserNotFoundException();
	}

	public void updatedPost(Post post, String postId) throws PostNotAvailableException {
		Post existedPost = postrepository.findById(postId).get();
		if (existedPost != null) {
			existedPost.setPostMessage(post.getPostMessage());
			existedPost.setHasTag(post.getHasTag());
			existedPost.setDateOfPost(new Date(System.currentTimeMillis()));
			postrepository.save(existedPost);
		} else
			throw new PostNotAvailableException();
	}

	public void deletePost(String postId) throws PostNotAvailableException {
		if (postrepository.findById(postId) != null) {
			postrepository.deleteById(postId);
			commentRepository.deleteByPostId(postId);
		} else
			throw new PostNotAvailableException();
	}

	public void addCommentToPost(Comment comment, String username, String id) throws PostNotAvailableException {
		if (postrepository.findById(id) != null) {
			comment.setCommentId(DateUtil.getCurrentTime());
			comment.setPostId(id);
			comment.setAuthor(username);
			comment.setDateOfComment(new Date(System.currentTimeMillis()));
			commentRepository.save(comment);
		} else
			throw new PostNotAvailableException();
	}

	public List<PostDestinationModel> getAllTweets() {
		LookupOperation lookup = LookupOperation.newLookup().from(ServiceConstants.COMMENT_COLLECTION_TABLE)
				.localField(ServiceConstants.LOCAL_FIELD).foreignField(ServiceConstants.FOREIGN_FIELD)
				.as(ServiceConstants.COMMENTS);
		Aggregation aggregation = Aggregation.newAggregation(lookup,
				Aggregation.sort(Sort.Direction.DESC, ServiceConstants.DATE_OF_POST));
		List<PostDestinationModel> posts = template
				.aggregate(aggregation, ServiceConstants.POST_COLLECTION_TABLE, PostDestinationModel.class)
				.getMappedResults();
		if (!posts.isEmpty()) {
			return posts;
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no posts available...!");
		}
	}

	public List<PostDestinationModel> getUserTweets(String username) {
		LookupOperation lookup = LookupOperation.newLookup().from(ServiceConstants.COMMENT_COLLECTION_TABLE)
				.localField(ServiceConstants.LOCAL_FIELD).foreignField(ServiceConstants.FOREIGN_FIELD)
				.as(ServiceConstants.COMMENTS);
		Aggregation aggregation = Aggregation.newAggregation(Aggregation.match(Criteria.where("author").is(username)),
				lookup, Aggregation.sort(Sort.Direction.DESC, ServiceConstants.DATE_OF_POST));
		List<PostDestinationModel> userPosts = template
				.aggregate(aggregation, ServiceConstants.POST_COLLECTION_TABLE, PostDestinationModel.class)
				.getMappedResults();
		if (!userPosts.isEmpty()) {
			return userPosts;
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "no posts available...!");
		}

	}
	
	public void addLikeToPost(String id) throws PostNotAvailableException {
		Post existedPost = postrepository.findById(id).get();
		if (existedPost != null) {
			Long currentLike = existedPost.getLikesCount();
			currentLike++;
			existedPost.setLikesCount(currentLike);
			postrepository.save(existedPost);
		} else
			throw new PostNotAvailableException();
	}

	public Post getPost(String postId) throws PostNotAvailableException {
		Post post = postrepository.findById(postId).get();
		if (post != null)
			return post;
		else
			throw new PostNotAvailableException();
	}
}