package com.tweetapp.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.tweetapp.model.User;

public interface UserRepository extends MongoRepository<User, String>{

	User findByUsername(String username);

	List<User> findByUsernameLike(String username);

}