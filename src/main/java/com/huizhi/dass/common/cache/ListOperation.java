package com.huizhi.dass.common.cache;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;


public class ListOperation<K, V> {
	
	private RedisTemplate<K, V> redisTemplate;
	

	public ListOperation(RedisTemplate<K, V> redisTemplate) {
		this.redisTemplate = redisTemplate;
	}

	/**
	 * 压栈
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long push(K key, V value) {
		return redisTemplate.opsForList().leftPush(key, value);
	}

	/**
	 * 出栈
	 * 
	 * @param key
	 * @return
	 */
	public V pop(K key) {
		return (V) redisTemplate.opsForList().leftPop(key);
	}

	/**
	 * 入队
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	public Long in(K key, V value) {
		return redisTemplate.opsForList().rightPush(key, value);
	}

	/**
	 * 出队
	 * 
	 * @param key
	 * @return
	 */
	public String out(K key) {
		return (String) redisTemplate.opsForList().leftPop(key);
	}

	/**
	 * 栈/队列长
	 * 
	 * @param key
	 * @return
	 */
	public Long length(K key) {
		return redisTemplate.opsForList().size(key);
	}

	/**
	 * 范围检索
	 * 
	 * @param key
	 * @param start
	 * @param end
	 * @return
	 */
	public List<V> range(K key, int start, int end) {
		return redisTemplate.opsForList().range(key, start, end);
	}

	/**
	 * 移除
	 * 
	 * @param key
	 * @param i
	 * @param value
	 */
	public void remove(K key, long i, String value) {
		redisTemplate.opsForList().remove(key, i, value);
	}

	/**
	 * 检索
	 * 
	 * @param key
	 * @param index
	 * @return
	 */
	public V index(K key, long index) {
		return (V) redisTemplate.opsForList().index(key, index);
	}

	/**
	 * 置值
	 * 
	 * @param key
	 * @param index
	 * @param value
	 */
	public void set(K key, long index, V value) {
		redisTemplate.opsForList().set(key, index, value);
	}

	/**
	 * 裁剪
	 * 
	 * @param key
	 * @param start
	 * @param end
	 */
	public void trim(K key, long start, int end) {
		redisTemplate.opsForList().trim(key, start, end);
	}
}

