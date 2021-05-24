package com.tweetapp.model;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.tweetapp.constants.ServiceConstants;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Document(collection = ServiceConstants.COMMENT_COLLECTION_TABLE)
public class Comment {
	@Id
	String commentId;
	String postId;
	String author;
	String commentMessage;
	Date dateOfComment;
}