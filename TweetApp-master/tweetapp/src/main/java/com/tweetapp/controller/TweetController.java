package com.tweetapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tweetapp.exception.PostNotAvailableException;
import com.tweetapp.exception.UserNotFoundException;
import com.tweetapp.model.Post;
import com.tweetapp.model.Comment;
import com.tweetapp.model.PostDestinationModel;
import com.tweetapp.service.TweetService;

@RestController
@RequestMapping("/api/v1.0/tweets")
public class TweetController {

	@Autowired
	TweetService postService;

	@GetMapping("/all")
	public List<PostDestinationModel> getTweets() {
		return postService.getAllTweets();
	}

	@GetMapping("/{username}")
	public List<PostDestinationModel> getTweetsByUser(@PathVariable String username) {
		return postService.getUserTweets(username);
	}

	@PostMapping("/{username}/add")
	public void addPost(@RequestBody Post newPost, @PathVariable String username) throws UserNotFoundException {
		postService.addPost(newPost, username);
	}

	@GetMapping("/tweetBy/{tweetId}")
	public Post getPost(@PathVariable String tweetId) throws PostNotAvailableException {
		return postService.getPost(tweetId);
	}
	
	@PutMapping("/update/{tweetId}")
	public void updatePost(@RequestBody Post post, @PathVariable String tweetId) throws PostNotAvailableException {
		postService.updatedPost(post, tweetId);
	}

	@DeleteMapping("/delete/{tweetId}")
	public void deletePost(@PathVariable String tweetId) throws PostNotAvailableException {
		System.err.println("delete method calling ");
		postService.deletePost(tweetId);
	}

	@PostMapping("/{username}/comment/{tweetId}")
	public void replyPost(@RequestBody Comment comment, @PathVariable String username, @PathVariable String tweetId)
			throws PostNotAvailableException {
		System.err.println(comment);
		postService.addCommentToPost(comment, username, tweetId);
	}
	
	@PutMapping("/like/{id}")
	public void likePost(@PathVariable String id) throws PostNotAvailableException{
		postService.addLikeToPost(id);
	}
}