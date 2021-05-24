package com.tweetapp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.tweetapp.constants.ServiceConstants;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = ServiceConstants.USER_COLLECTION_TABLE)
public class User {
	@Id
	String username;
	@Indexed(unique = true)
	String email;
	@ApiModelProperty(hidden = true)
	String password;
	String firstName;
	String lastName;
	String contactNumber;
	String gender;
}