package com.github.cbuschka.retour.infrastructure.persistence;

public class AggregateRoot<T>
{
	private String key;

	private int version;

	private T data;

	public AggregateRoot(String key, int version, T data)
	{
		this.key = key;
		this.version = version;
		this.data = data;
	}

	public T getData() {
		return this.data;
	}

	public void setData(T data)
	{
		this.data = data;
	}

	public void setVersion(int version)
	{
		this.version = version;
	}

	public int getVersion()
	{
		return version;
	}

	public String getKey()
	{
		return key;
	}
}
