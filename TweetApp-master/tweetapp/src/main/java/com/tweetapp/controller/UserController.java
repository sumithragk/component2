package com.tweetapp.controller;

import java.util.List;
import java.util.Map;

import com.tweetapp.exception.PostNotAvailableException;
import com.tweetapp.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.tweetapp.exception.UserAlreadyExistException;
import com.tweetapp.exception.UserNotFoundException;
import com.tweetapp.model.User;
import com.tweetapp.service.UserService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/v1.0/tweets")
@Slf4j
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public void signUp(@RequestBody User user) throws UserAlreadyExistException{
		System.err.println(user);
		userService.saveUser(user);
	}

	@GetMapping("/login")
	public Map<String, String> login(@RequestHeader("Authorization") String authHeader) {
		log.info(authHeader);
		return userService.authenticate(authHeader);
	}
	
	@GetMapping("/users")
	public List<User> getUsers(){
		return userService.getAllUsers();
	}
	
	@GetMapping("/user/search/{username}")
	public List<User> searchByUsername(@PathVariable String username) throws UserNotFoundException{
		return userService.searchByUsername(username);
	}

	@PutMapping("/updatePassword/{userName}")
	public void updatePassword(@RequestBody User user, @PathVariable String userName) throws UserNotFoundException {
		userService.updatePassword(user, userName);
	}
	
}