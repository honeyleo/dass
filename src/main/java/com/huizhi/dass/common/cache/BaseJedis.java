package com.huizhi.dass.common.cache;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public abstract class BaseJedis<T> {
	
	private JedisPool pool = RedisPool.getPool();
	
	public T get(MyRedisCallback<T> callback) {
		Jedis jedis = pool.getResource();
		T obj = callback.doInRedis(jedis);
		pool.returnResource(jedis);
		
		return obj;
	}

}
