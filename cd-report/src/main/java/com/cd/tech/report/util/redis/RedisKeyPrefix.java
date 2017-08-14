package com.cd.tech.report.util.redis;

/**
 * Enumeration of Redis key prefix, will be appended to the key when adding object to Redis
 *  or getting object from Redis.
 * @author laiss
 *
 */
public enum RedisKeyPrefix {
	// following are the component names
	COMMON ("CM"),
	USER ("USR"),
	SELLER ("SLR"),
	WEBSITE ("WBS"),
	ORDER ("ORD"),
	MANAGEMENT ("MGT"),
	SEARCH ("SRH"),
	OTHER ("OTH"),
	SALES ("SAL");
	
	private final String prefix;
	   
	private RedisKeyPrefix(String prefix) {
	     this.prefix = prefix;
	}
	
	public String getKeyPrefix() {
	     return prefix;
	}
}
