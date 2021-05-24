package com.tweetapp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.tweetapp.model.Post;

@Repository
public interface PostRepository extends MongoRepository<Post, String>{

}
