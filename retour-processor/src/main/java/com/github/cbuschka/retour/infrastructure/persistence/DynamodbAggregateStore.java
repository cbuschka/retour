package com.github.cbuschka.retour.infrastructure.persistence;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.GetItemSpec;
import com.amazonaws.services.dynamodbv2.document.spec.PutItemSpec;
import com.amazonaws.services.dynamodbv2.model.ConditionalCheckFailedException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public abstract class DynamodbAggregateStore<T>
{
	private static final int INITIAL_VERSION = 0;
	private static final String DATA_COLUMN_NAME = "Data";
	private static final String VERSION_COLUMN_NAME = "Version";

	private ObjectMapper objectMapper = new ObjectMapper();

	private AmazonDynamoDB dbClient = AmazonDynamoDBClientBuilder.standard()
			.withRegion(Regions.EU_CENTRAL_1)
			.build();

	private String keyColumnName;
	private String tableName;
	private Class<T> dataType;

	protected DynamodbAggregateStore(String tableName, String keyColumnName, Class<T> dataType)
	{
		this.keyColumnName = keyColumnName;
		this.tableName = tableName;
		this.dataType = dataType;
	}

	public AggregateRoot<T> create(String key)
	{
		T data = fromJson("{}");
		return new AggregateRoot<>(key, INITIAL_VERSION, data);
	}

	public Optional<AggregateRoot<T>> findByKey(String key)
	{
		DynamoDB dynamoDB = new DynamoDB(dbClient);
		Table table = dynamoDB.getTable(this.tableName);
		Item item = table.getItem(new GetItemSpec()
				.withPrimaryKey(this.keyColumnName, key));
		if (item == null)
		{
			return Optional.empty();
		}

		AggregateRoot<T> root = buildAggregate(key, item);
		return Optional.of(root);
	}

	public void store(AggregateRoot<T> root) throws ConcurrentModification
	{
		String json = toJson(root.getData());

		DynamoDB dynamoDB = new DynamoDB(dbClient);
		Table table = dynamoDB.getTable(this.tableName);

		Map<String, Object> attributeValues = new HashMap<>();
		attributeValues.put(":expectedVersion", root.getVersion());

		try
		{
			table.putItem(new PutItemSpec()
					.withItem(new Item()
							.withPrimaryKey(this.keyColumnName, root.getKey())
							.withInt(VERSION_COLUMN_NAME, root.getVersion() + 1)
							.withJSON(DATA_COLUMN_NAME, json))
					.withConditionExpression(root.getVersion() == INITIAL_VERSION ?
							"attribute_not_exists(" + VERSION_COLUMN_NAME + ")" :
							"(" + VERSION_COLUMN_NAME + " = :expectedVersion)")
					.withValueMap(attributeValues));
			root.setVersion(root.getVersion() + 1);
		}
		catch (ConditionalCheckFailedException ex)
		{
			throw new ConcurrentModification(this.tableName + " with key " + keyColumnName + "=" + root.getKey() + " has been modified concurrently.");
		}
	}

	private AggregateRoot<T> buildAggregate(String key, Item item)
	{
		String json = item.getJSON(DATA_COLUMN_NAME);
		T data = fromJson(json != null ? json : "{}");
		int version = item.getInt(VERSION_COLUMN_NAME);
		return new AggregateRoot<>(key, version, data);
	}

	private String toJson(T data)
	{
		try
		{
			return this.objectMapper.writeValueAsString(data);
		}
		catch (IOException ex)
		{
			throw new RuntimeException(ex);
		}
	}

	private T fromJson(String json)
	{
		try
		{
			return this.objectMapper.readerFor(this.dataType).readValue(json);
		}
		catch (IOException ex)
		{
			throw new RuntimeException(ex);
		}
	}
}
