package com.github.cbuschka.retour.domain.order_store;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.cbuschka.retour.domain.retour_store.RetourAlreadyProcessed;
import com.github.cbuschka.retour.util.Dates;
import com.github.cbuschka.retour.util.Logger;
import com.sun.org.apache.xpath.internal.operations.Or;

public class OrderDao
{
	private static final String TABLE_NAME = "Order";

	private AmazonDynamoDB dbClient = AmazonDynamoDBClientBuilder.standard()
			.withRegion(Regions.EU_CENTRAL_1)
			.build();

	public void storeOrder(OrderRecord orderRecord)
	{
		DynamoDB dynamoDB = new DynamoDB(dbClient);
		Table orderTable = dynamoDB.getTable(TABLE_NAME);
		orderTable.putItem(new PutItemSpec()
				.withItem(new Item()
						.withPrimaryKey("OrderNo", orderRecord.orderNo)));
	}

	public OrderRecord findOrder(String orderNo) throws OrderNotFound
	{
		DynamoDB dynamoDB = new DynamoDB(dbClient);
		Table orderTable = dynamoDB.getTable(TABLE_NAME);
		Item orderItem = orderTable.getItem(new GetItemSpec()
				.withPrimaryKey("OrderNo", orderNo));
		if (orderItem == null)
		{
			throw new OrderNotFound("Order " + orderNo + " not found.");
		}
		OrderRecord orderRecord = new OrderRecord();
		orderRecord.orderNo = orderNo;
		return orderRecord;
	}
}
