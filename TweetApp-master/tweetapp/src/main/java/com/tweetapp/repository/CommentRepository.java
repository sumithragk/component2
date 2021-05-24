package com.tweetapp.repository;

import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tweetapp.model.Comment;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
	@DeleteQuery
	void deleteByPostId(String postId);
}