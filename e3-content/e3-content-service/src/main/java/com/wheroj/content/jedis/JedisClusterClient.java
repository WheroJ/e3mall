package com.wheroj.content.jedis;

import redis.clients.jedis.JedisCluster;

public class JedisClusterClient implements JedisClient {
	
	private JedisCluster jedisCluster;
	
	public JedisClusterClient(JedisCluster jedisCluster) {
		this.jedisCluster = jedisCluster;
	}
	
	@Override
	public void set(String key, String value) {
		jedisCluster.set(key, value);
	}

	@Override
	public void hset(String key, final String field, String value) {
		jedisCluster.hset(key, field, value.toString());
	}

	@Override
	public void lpush(String key, String... values) {
		jedisCluster.lpush(key, values);
	}

	@Override
	public Long del(String key) {
		return jedisCluster.del(key);
	}

	@Override
	public Long del(String key, String field) {
		return jedisCluster.hdel(key, field);
	}

	@Override
	public String lpop(String key) {
		return jedisCluster.lpop(key);
	}

	@Override
	public String rpop(String key) {
		return jedisCluster.rpop(key);
	}

	@Override
	public String get(String key) {
		return jedisCluster.get(key);
	}

	@Override
	public String hget(String key, String field) {
		return jedisCluster.hget(key, field);
	}
}
