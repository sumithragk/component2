package com.tweetapp.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.tweetapp.constants.ServiceConstants;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Document(collection = ServiceConstants.POST_COLLECTION_TABLE)
public class Post {
	@Id
	String id;
	String author;
	String postMessage;
	String hasTag;
	Date dateOfPost;
	Long likesCount;
}