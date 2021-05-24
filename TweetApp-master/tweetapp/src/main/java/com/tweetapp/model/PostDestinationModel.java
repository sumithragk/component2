package com.tweetapp.model;

import java.util.Date;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostDestinationModel {	
		
		String id;
		String author;
		String postMessage;
		String hasTag;
		Date dateOfPost;
		Long likesCount;
		List<Comment> comments;	
	
}