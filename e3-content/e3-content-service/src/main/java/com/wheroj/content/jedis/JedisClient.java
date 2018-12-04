package com.wheroj.content.jedis;

public interface JedisClient {
	public void set(String key, String value);
	
	public String get(String key);
	
	public void hset(String key, final String field, String value);
	
	public String hget(String key, final String field);
	
	public void lpush(String key, String ...values);
	
	public Long del(String key);
	
	public Long del(String key, String field);
	
	public String lpop(String key);
	
	public String rpop(String key);
}
