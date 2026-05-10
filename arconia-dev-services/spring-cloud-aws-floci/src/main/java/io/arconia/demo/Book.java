package io.arconia.demo;

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;

@DynamoDbBean
public class Book {

	private Long id;
	private String title;

	public Book() {
	}

	public Book(Long id, String title) {
		this.id = id;
		this.title = title;
	}

	@DynamoDbPartitionKey
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}
