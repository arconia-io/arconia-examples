package io.arconia.demo;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.BillingMode;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ResourceInUseException;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.BucketAlreadyOwnedByYouException;

@Component
class AwsResourceInitializer {

	private final DynamoDbClient dynamoDbClient;
	private final S3Client s3Client;

	AwsResourceInitializer(DynamoDbClient dynamoDbClient, S3Client s3Client) {
		this.dynamoDbClient = dynamoDbClient;
		this.s3Client = s3Client;
	}

	@EventListener(ApplicationReadyEvent.class)
	void createResources() {
		createBookTable();
		createScrollsBucket();
	}

	private void createBookTable() {
		try {
			dynamoDbClient.createTable(r -> r
				.tableName("book")
				.keySchema(KeySchemaElement.builder()
					.attributeName("id")
					.keyType(KeyType.HASH)
					.build())
				.attributeDefinitions(AttributeDefinition.builder()
					.attributeName("id")
					.attributeType(ScalarAttributeType.N)
					.build())
				.billingMode(BillingMode.PAY_PER_REQUEST)
			);
		} catch (ResourceInUseException ignored) {
		}
	}

	private void createScrollsBucket() {
		try {
			s3Client.createBucket(r -> r.bucket(ScrollController.BUCKET));
		} catch (BucketAlreadyOwnedByYouException ignored) {
		}
	}

}
