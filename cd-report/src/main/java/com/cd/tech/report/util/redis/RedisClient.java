package com.cd.tech.report.util.redis;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.cd.tech.report.util.PropertiesUtil;
import org.apache.log4j.Logger;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;


import redis.clients.jedis.JedisPoolConfig;

/**
 * Redis client for all operations to Redis server. TODO:: Improve storage
 * effeciency for Double, Float, Integer, Long and Short??
 * 
 * @author laiss
 *
 */
public class RedisClient {

	private static Logger log = Logger.getLogger(RedisClient.class);

	private RedisTemplate<String, Object> redisTemplate;
	
	public RedisClient(){
		initRedisTemplate();
	}

	/**
	 * Get <code>RedisTemplate</code> for more operations.
	 * @return
	 */
	public void initRedisTemplate() {
		if (redisTemplate == null) {
			redisTemplate = new RedisTemplate<String, Object>();
			redisTemplate.setConnectionFactory(redisConnectionFactory());
			redisTemplate.setKeySerializer(new StringRedisSerializer());
			redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
			
			redisTemplate.afterPropertiesSet();
		}
	}

	public RedisConnectionFactory redisConnectionFactory() {
		JedisConnectionFactory cf = new JedisConnectionFactory();
		cf.setPoolConfig(getJedisPoolConfig());
		cf.setHostName(PropertiesUtil.getStringValue("redis.host"));
		cf.setPort(PropertiesUtil.getIntValue("redis.port"));
		cf.setPassword(PropertiesUtil.getStringValue("redis.pass"));
		cf.afterPropertiesSet();
		return cf;
	}

	public JedisPoolConfig getJedisPoolConfig(){
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxIdle(PropertiesUtil.getIntValue("redis.max_idle")); 
		poolConfig.setMaxTotal(PropertiesUtil.getIntValue("redis.max_active")); 
		poolConfig.setMaxWaitMillis(PropertiesUtil.getIntValue("redis.max_wait"));
		poolConfig.setTestOnBorrow(false); 
		
		return poolConfig;
	}
	
	/**
	 * Put an object to redis server.
	 * 
	 * @param keyPrefix
	 *            Module name of the cached object in order to avoid
	 *            duplication. see <code>RedisKeyPrefix</code>.
	 * @param key
	 *            The key in redis to be cached
	 * @param value
	 *            The value to be cached
	 * @param validPeriod
	 *            Time of validity by Second.
	 */
	public void set(RedisKeyPrefix keyPrefix, String key, Object value, int validPeriod) {
		String redisKey = buildKey(keyPrefix, key);
		ValueOperations<String, Object> opt = redisTemplate.opsForValue();
		opt.set(redisKey, value, validPeriod, TimeUnit.SECONDS);
	}

	public void set(RedisKeyPrefix keyPrefix, String key, Object value) {
		String redisKey = buildKey(keyPrefix, key);
		ValueOperations<String, Object> opt = redisTemplate.opsForValue();
		opt.set(redisKey, value);
	}

	/**
	 * Get an object from redis server.
	 * 
	 * @param keyPrefix
	 *            Module name of the cached object in order to avoid
	 *            duplication. see <code>RedisKeyPrefix</code>.
	 * @param key
	 *            The key in redis to be cached
	 * @return The object to be cached, Null will be returned if the cached
	 *         object is expired or never cached.
	 */
	public Object get(RedisKeyPrefix keyPrefix, String key) {
		String redisKey = buildKey(keyPrefix, key);
		ValueOperations<String, Object> opt = redisTemplate.opsForValue();
		try {
			return opt.get(redisKey);
		} catch (SerializationException se) {
			log.warn("Error happened when getting data from Redis!", se);
			return null;
		}
	}

	public void delete(RedisKeyPrefix keyPrefix, String key) {
		String redisKey = buildKey(keyPrefix, key);
		redisTemplate.delete(redisKey);
	}

	public boolean exists(RedisKeyPrefix keyPrefix, String key) {
		return (this.get(keyPrefix, key) != null);
	}

	/**
	 * 计数用的方法。以给定的Key在redis中的值与输入的incr值相加，并存入redis；若给定的Key在redis中不存在，
	 * 则直接保存incr的值到redis中。 此方法将重新设置给定Key在redis中的过期时间。
	 * 
	 * @param keyPrefix
	 * @param key
	 * @param incr
	 *            新增长的值，可以为负数；
	 * @param validPeriod
	 *            修改值以后redis中对应key的过期时间，单位是秒。
	 * @return 返回累加之后的值。
	 */
	public synchronized long incrCounter(RedisKeyPrefix keyPrefix, String key, long incr, int validPeriod) {
		Object obj = this.get(keyPrefix, key);
		long currNumber = 0;
		if (obj != null && (obj instanceof Integer || obj instanceof Long)) {
			currNumber = ((Number) obj).longValue();
		}
		long newNumber = currNumber + incr;
		this.set(keyPrefix, key, newNumber, validPeriod);
		return newNumber;
	}

	/**
	 * 获取计数器的值。与get()方法类似，只是返回值类型不同。
	 * 
	 * @param keyPrefix
	 * @param key
	 * @return
	 */
	public long getCounter(RedisKeyPrefix keyPrefix, String key) {
		Object obj = this.get(keyPrefix, key);
		long currNumber = 0;
		if (obj != null && (obj instanceof Integer || obj instanceof Long)) {
			currNumber = ((Number) obj).longValue();
		}
		return currNumber;
	}

	public void listAdd(RedisKeyPrefix keyPrefix, String key, Object value) {
		ListOperations<String, Object> opt = redisTemplate.opsForList();
		String redisKey = buildKey(keyPrefix, key);
		opt.rightPush(redisKey, value);
	}

	public void listAdd(RedisKeyPrefix keyPrefix, String key, Object value, long validPeriod) {
		this.listAdd(keyPrefix, key, value);
		setExpiredTime(keyPrefix, key, validPeriod);
	}

	public Object listGet(RedisKeyPrefix keyPrefix, String key, long index) {
		ListOperations<String, Object> opt = redisTemplate.opsForList();
		String redisKey = buildKey(keyPrefix, key);
		return opt.index(redisKey, index);
	}

	public List<Object> listGetAll(RedisKeyPrefix keyPrefix, String key) {
		ListOperations<String, Object> opt = redisTemplate.opsForList();
		String redisKey = buildKey(keyPrefix, key);
		long size = opt.size(redisKey);
		return opt.range(redisKey, 0, size);
	}

	/**
	 * Remove all the elements which equals the given value.
	 * 
	 * @param keyPrefix
	 * @param key
	 * @param value
	 *            the value will be deleted.
	 */
	public void listDelete(RedisKeyPrefix keyPrefix, String key, Object value) {
		ListOperations<String, Object> opt = redisTemplate.opsForList();
		String redisKey = buildKey(keyPrefix, key);
		opt.remove(redisKey, 0, value); // remove all the elements which equals
										// the given value
	}

	public void listSet(RedisKeyPrefix keyPrefix, String key, long index, Object value) {
		ListOperations<String, Object> opt = redisTemplate.opsForList();
		String redisKey = buildKey(keyPrefix, key);
		opt.set(redisKey, index, value);
	}

	public void listSet(RedisKeyPrefix keyPrefix, String key, long index, Object value, long validPeriod) {
		this.listSet(keyPrefix, key, index, value);
		setExpiredTime(keyPrefix, key, validPeriod);
	}

	public long listSize(RedisKeyPrefix keyPrefix, String key) {
		ListOperations<String, Object> opt = redisTemplate.opsForList();
		String redisKey = buildKey(keyPrefix, key);
		return opt.size(redisKey);
	}

	public void hashPut(RedisKeyPrefix keyPrefix, String key, String hashKey, Object hashValue) {
		HashOperations<String, String, Object> opt = redisTemplate.opsForHash();
		String redisKey = buildKey(keyPrefix, key);
		opt.put(redisKey, hashKey, hashValue);
	}

	public void hashPut(RedisKeyPrefix keyPrefix, String key, String hashKey, Object hashValue, long validPeriod) {
		this.hashPut(keyPrefix, key, hashKey, hashValue);
		setExpiredTime(keyPrefix, key, validPeriod);
	}

	public Object hashGet(RedisKeyPrefix keyPrefix, String key, String hashKey) {
		HashOperations<String, String, Object> opt = redisTemplate.opsForHash();
		String redisKey = buildKey(keyPrefix, key);
		return opt.get(redisKey, hashKey);
	}

	public void hashDelete(RedisKeyPrefix keyPrefix, String key, String hashKey) {
		HashOperations<String, String, Object> opt = redisTemplate.opsForHash();
		String redisKey = buildKey(keyPrefix, key);
		opt.delete(redisKey, hashKey);
	}

	public void hashPutAll(RedisKeyPrefix keyPrefix, String key, Map<String, Object> elements) {
		HashOperations<String, String, Object> opt = redisTemplate.opsForHash();
		String redisKey = buildKey(keyPrefix, key);
		opt.putAll(redisKey, elements);
	}

	public void hashPutAll(RedisKeyPrefix keyPrefix, String key, Map<String, Object> elements, long validPeriod) {
		this.hashPutAll(keyPrefix, key, elements);
		setExpiredTime(keyPrefix, key, validPeriod);
	}

	/**
	 * Check if the specified key exists in a <code>HashMap</code>.
	 * 
	 * @param keyPrefix
	 * @param key
	 * @param hashKey
	 * @return
	 */
	public boolean hashCheckKey(RedisKeyPrefix keyPrefix, String key, String hashKey) {
		HashOperations<String, String, Object> opt = redisTemplate.opsForHash();
		String redisKey = buildKey(keyPrefix, key);
		return opt.hasKey(redisKey, hashKey);
	}

	/**
	 * Return all keys in the <code>HashMap</code>.
	 * 
	 * @param keyPrefix
	 * @param key
	 * @return
	 */
	public Set<String> hashGetKeys(RedisKeyPrefix keyPrefix, String key) {
		HashOperations<String, String, Object> opt = redisTemplate.opsForHash();
		String redisKey = buildKey(keyPrefix, key);
		return opt.keys(redisKey);
	}

	/**
	 * Return all elements in the <code>HashMap</code>.
	 * 
	 * @param keyPrefix
	 * @param key
	 * @return
	 */
	public Map<String, Object> hashGetEntries(RedisKeyPrefix keyPrefix, String key) {
		HashOperations<String, String, Object> opt = redisTemplate.opsForHash();
		String redisKey = buildKey(keyPrefix, key);
		return opt.entries(redisKey);
	}

	public long hashSize(RedisKeyPrefix keyPrefix, String key) {
		HashOperations<String, String, Object> opt = redisTemplate.opsForHash();
		String redisKey = buildKey(keyPrefix, key);
		return opt.size(redisKey);
	}

	/**
	 * 原子累加操作。注意：通过此方法设的值不能通过hashGet和hashGetEntries来获取！TODO:: Improve it??
	 * 
	 * @param keyPrefix
	 * @param key
	 * @param hashKey
	 * @param incr
	 *            累加计数，可以为负数。
	 * @return
	 */
	public long hashIncrement(RedisKeyPrefix keyPrefix, String key, String hashKey, long incr) {
		HashOperations<String, String, Object> opt = redisTemplate.opsForHash();
		String redisKey = buildKey(keyPrefix, key);
		return opt.increment(redisKey, hashKey, incr);
	}

	/**
	 * 原子累加操作。注意：通过此方法设的值不能通过hashGet和hashGetEntries来获取！
	 * 
	 * @param keyPrefix
	 * @param key
	 * @param hashKey
	 * @param incr
	 *            累加计数，可以为负数。
	 * @return
	 */
	public double hashIncrement(RedisKeyPrefix keyPrefix, String key, String hashKey, double incr) {
		HashOperations<String, String, Object> opt = redisTemplate.opsForHash();
		String redisKey = buildKey(keyPrefix, key);
		return opt.increment(redisKey, hashKey, incr);
	}

	/**
	 * Build the key in redis.
	 * 
	 * @param keyPrefix
	 * @param key
	 * @return
	 */
	protected String buildKey(RedisKeyPrefix keyPrefix, String key) {
		return keyPrefix.getKeyPrefix() + ":" + key;
	}

	/**
	 * Set the expired time for given redis key.
	 * 
	 * @param keyPrefix
	 * @param key
	 * @param validPeriod
	 *            expired time with second.
	 */
	protected void setExpiredTime(RedisKeyPrefix keyPrefix, String key, long validPeriod) {
		String redisKey = buildKey(keyPrefix, key);
		redisTemplate.expire(redisKey, validPeriod, TimeUnit.SECONDS);
	}

	/**
	 * getObjects:方法描述 根据keys值做批量查询
	 * 
	 * @author -
	 * @param redisKeys
	 * @return 如果输入的key对应的缓存不存在的话 返回值是 null 必要时请做非空过滤
	 * @since JDK 1.7
	 */
	public Object getObjects(Collection<String> redisKeys) {

		ValueOperations<String, Object> opt = redisTemplate.opsForValue();
		try {
			return opt.multiGet(redisKeys);
		} catch (SerializationException se) {
			log.warn("Error happened when getting data from Redis!", se);
			return null;
		}
	}

	/**
	 * delete:方法描述 服务端调用的删除缓存的方法 直接根据输入的key做缓存删除
	 * 
	 * @author -
	 * @param key
	 *            缓存的key
	 * @since JDK 1.7
	 */
	public void delete(String key) {
		redisTemplate.delete(key);
	}

	/**
	 * get:方法描述 不加前缀的key
	 * @author -
	 * @param key
	 * @return
	 * @since JDK 1.7
	 */
	public Object get(String key) {
		ValueOperations<String, Object> opt = redisTemplate.opsForValue();
		try {
			return opt.get(key);
		} catch (SerializationException se) {
			log.warn("Error happened when getting data from Redis!", se);
			return null;
		}
	}
	
	/**
	 * @Description: set:方法描述 不加前缀的key
	 * @author hour 
	 * @param key
	 * @param value
	 * @param validPeriod
	 */
	public void set(String key, Object value, int validPeriod) {
		ValueOperations<String, Object> opt = redisTemplate.opsForValue();
		opt.set(key, value, validPeriod, TimeUnit.SECONDS);
	}
	
	public static void main(String[] args) {
		//CHEM_RUN_FLAG
		RedisClient redisClient = new RedisClient();
		//redisClient.set("CHEM_RUN_FLAG", "8888", 24*60*60);//生存周期24小时
		log.info(redisClient.get("CHEM_RUN_FLAG"));
	}
}
