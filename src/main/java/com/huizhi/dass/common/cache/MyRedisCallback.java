package com.huizhi.dass.common.cache;

import redis.clients.jedis.Jedis;

public interface MyRedisCallback<T> {
	
	public T doInRedis(Jedis jedis);

}
