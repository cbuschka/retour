package com.github.cbuschka.retour.domain.order_store;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY,
		property = "type")
@JsonSubTypes({
		@JsonSubTypes.Type(value = RetourCreatedEvent.class, name = "RetourCreated"),
		@JsonSubTypes.Type(value = RetourReceivedEvent.class, name = "RetourReceived")}
)
public abstract class OrderEvent
{
	@JsonProperty
	public abstract String getType();

	protected OrderEvent()
	{
	}
}
