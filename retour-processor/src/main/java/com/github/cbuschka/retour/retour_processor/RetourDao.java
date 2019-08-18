package com.github.cbuschka.retour.retour_processor;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.github.cbuschka.retour.util.Dates;
import com.github.cbuschka.retour.util.Logger;

public class RetourDao {

	private static Logger logger = Logger.get();

	private static final String TABLE_NAME = "Retour";

	private AmazonDynamoDB dbClient = AmazonDynamoDBClientBuilder.standard()
			.withRegion(Regions.EU_CENTRAL_1)
			.build();

	public void createRetour(String retourNo) throws RetourAlreadyProcessed {
		DynamoDB dynamoDB = new DynamoDB(dbClient);
		Table retourTable = dynamoDB.getTable(TABLE_NAME);
		try {
			Item item = new Item()
					.withPrimaryKey("RetourNo", retourNo);
			PutItemRequest putItemRequest = new PutItemRequest();
			putItemRequest.setTableName(TABLE_NAME);
			retourTable.putItem(new PutItemSpec()
					.withItem(item)
					.withConditionExpression("attribute_not_exists(ProcessedAt)"));

			logger.log("Retour " + retourNo + " created.");
		} catch (ConditionalCheckFailedException ex) {
			throw new RetourAlreadyProcessed("Retour " + retourNo + " already processed.");
		}
	}

	public void markRetourProcessed(String retourNo) throws RetourAlreadyProcessed {
		DynamoDB dynamoDB = new DynamoDB(dbClient);
		Table retourTable = dynamoDB.getTable(TABLE_NAME);
		try {
			String now = Dates.nowUTCAsIsoString();
			Item item = new Item()
					.withPrimaryKey("RetourNo", retourNo)
					.with("ProcessedAt", now);
			PutItemRequest putItemRequest = new PutItemRequest();
			putItemRequest.setTableName(TABLE_NAME);
			retourTable.putItem(new PutItemSpec()
					.withItem(item)
					.withConditionExpression("attribute_not_exists(ProcessedAt)"));

			logger.log("Retour " + retourNo + " marked as processed.");
		} catch (ConditionalCheckFailedException ex) {
			throw new RetourAlreadyProcessed("Retour " + retourNo + " already processed.");
		}
	}
}
