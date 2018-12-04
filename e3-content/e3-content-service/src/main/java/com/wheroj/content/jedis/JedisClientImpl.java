package com.wheroj.content.jedis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class JedisClientImpl implements JedisClient{
	
	private Jedis jedis;
	
	public JedisClientImpl(JedisPool jedisPool) {
		jedis = jedisPool.getResource();
	}

	@Override
	public void set(String key, String value) {
		jedis.set(key, value);
	}

	@Override
	public String get(String key) {
		return jedis.get(key);
	}

	@Override
	public void hset(String key, String field, String value) {
		jedis.hset(key, field, value);
	}

	@Override
	public String hget(String key, String field) {
		return jedis.hget(key, field);
	}

	@Override
	public void lpush(String key, String... values) {
		jedis.lpush(key, values);		
	}

	@Override
	public Long del(String key) {
		return jedis.del(key);
	}

	@Override
	public Long del(String key, String field) {
		return jedis.hdel(key, field);
	}

	@Override
	public String lpop(String key) {
		return jedis.lpop(key);
	}

	@Override
	public String rpop(String key) {
		return jedis.rpop(key);
	}

}
