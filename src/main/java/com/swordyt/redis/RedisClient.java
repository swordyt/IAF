package com.swordyt.redis;

import org.apache.log4j.Logger;

import redis.clients.jedis.Jedis;

public class RedisClient {
	private static final Logger log = Logger.getLogger(RedisClient.class);
	private Jedis redis;
	private static RedisClient client;

	private Jedis getRedis() {
		return redis;
	}

	protected RedisClient(String server, int port, String password) {
		redis = new Jedis(server, port);
		redis.auth(password);

	}

	public static Jedis getInstance(String server, String password) {
		return getInstance(server, 6379, password);
	}

	public static Jedis getInstance(String server, int port, String password) {
		log.info("redisServer:" + server + ",port:" + port + ",auth:" + password);
		if (client == null) {
			RedisClient testClient = new RedisClient(server, port, password);
			if (testClient.login()) {
				log.info("redis login success.");
				client = testClient;
			}
		}
		return client == null ? null : client.getRedis();
	}

	/**
	 * 验证连接Redis是否成功
	 */
	private boolean login() {
		return redis.isConnected();
	}
}
