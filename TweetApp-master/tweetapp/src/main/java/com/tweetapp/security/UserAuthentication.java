package com.tweetapp.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tweetapp.model.User;
import com.tweetapp.repository.UserRepository;

@Service
public class UserAuthentication implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	public UserAuthentication(UserRepository repository) {
		this.userRepository = repository;
	}
	
	@Transactional
	@Override
	public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(name);
		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		} else {
			return new AppUser(user);
		}
	}
}