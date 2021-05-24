package com.tweetapp.service;

import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.tweetapp.exception.PostNotAvailableException;
import com.tweetapp.exception.UserNotFoundException;
import com.tweetapp.model.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.tweetapp.constants.ServiceConstants;
import com.tweetapp.exception.UserAlreadyExistException;
import com.tweetapp.model.User;
import com.tweetapp.repository.UserRepository;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public class UserService {

	
	@Autowired 
	private UserRepository userRepository;
	
	public Map<String, String> authenticate(String authHeader) {
		Map<String, String> jwt = new HashMap<String, String>();
		String user = getUser(authHeader);
		String token = generateJwt(user);
		User loggedInUser = userRepository.findByUsername(user);
		jwt.put(ServiceConstants.TOKEN, token);
		jwt.put(ServiceConstants.USER, loggedInUser.getUsername());
		return jwt;
	}

	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	private String getUser(String authHeader) {
		String encodedCredentials = authHeader.split(" ")[1];
		byte[] decodedCredentials = Base64.getDecoder().decode(encodedCredentials);
		String user = new String(decodedCredentials).split(":")[0];
		return user;
	}

	private String generateJwt(String user) {
		JwtBuilder builder = Jwts.builder();
		builder.setSubject(user); // Set the token issue time as current time
		builder.setIssuedAt(new Date()); // Set the token expiry as 20minutes from now
		builder.setExpiration(new Date((new Date()).getTime() + 1200000));
		builder.signWith(SignatureAlgorithm.HS256, ServiceConstants.JWT_SECRET_KEY);
		String token = builder.compact();
		return token;
	}

	public void saveUser(User user) throws UserAlreadyExistException {
		
		try {
			User existedUser = userRepository.findByUsername(user.getUsername());
			if (existedUser != null) {
				throw new UserAlreadyExistException();
			} else {
				user.setPassword(passwordEncoder().encode(user.getPassword()));
				userRepository.save(user);
			}
		} catch (Exception e) {
			throw new UserAlreadyExistException();
		}
	}

	public List<User> getAllUsers() {
		List<User> users = userRepository.findAll();
		if( !users.isEmpty() ) {
			return users;
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"users not found" );
		}
	}

	public List<User> searchByUsername(String username) throws UsernameNotFoundException {
		List<User> users = userRepository.findByUsernameLike(username);
		if( !users.isEmpty() ) {
			return users;
		} else {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND,
					"username not matched with existing users" );
		}
	}

	public void updatePassword(User user, String userName) throws UserNotFoundException {
		User existedUser = userRepository.findById(userName).get();
		if (existedUser != null) {
			existedUser.setUsername(user.getUsername());
			existedUser.setPassword(passwordEncoder().encode(user.getPassword()));
			existedUser.setEmail(user.getEmail());
			userRepository.save(existedUser);
		} else
			throw new UserNotFoundException();
	}

}