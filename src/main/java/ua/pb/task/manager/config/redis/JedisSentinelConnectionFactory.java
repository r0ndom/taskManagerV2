package ua.pb.task.manager.config.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import static com.google.common.base.Preconditions.checkArgument;

public class JedisSentinelConnectionFactory {
    public static final int NEED_MIN_COUNT_SENTINEL = 2;
    public static final int NEED_COUNT_REDIS = 1;

    public static final Logger LOG = LoggerFactory.getLogger(JedisSentinelConnectionFactory.class);
    private RedisSentinelConfiguration redisSentinelConfiguration;
    private JedisConnectionFactory jedisConnectionFactory;

    @Autowired
    private JedisPoolConfig poolConfig;

    @Value("${redis.sentinel.masterName}")
    private String masterName;

    @Value("${redis.host}")
    private String[] host;

    @Value("${redis.port}")
    private String[] port;

    @Value("${redis.pass}")
    private String password;

    @Value("${redis.sentinel}")
    private Boolean sentinel;

    @Value("${redis.pool}")
    private Boolean pool;

    @PostConstruct
    public void getInstance() {
        LOG.info("Initialize jedis instance configuration with masterName:{} and sentinels host: {}, port: {}", masterName, host, port);
        LOG.info("Config state - sentinel: {}, pool: {}", sentinel, pool);
        LOG.info("Config count - host: {}, port: {}, needMinCount: {}", host.length, port.length, NEED_MIN_COUNT_SENTINEL);
        checkArgument(!(sentinel&&!pool), "If sentinel turn on, pool must be turn on too");
        checkArgument(host.length == port.length, "Invalid settings for redis connection(number of hosts is not equal to the number of ports)");
        switchSentinel();
        jedisConnectionFactory.setHostName(host[0]);
        jedisConnectionFactory.setPort(Integer.parseInt(port[0]));
        jedisConnectionFactory.setPassword(password);
        switchPool();
    }

    private void switchSentinel() {
        if (sentinel) {
            checkArgument(host.length >= NEED_MIN_COUNT_SENTINEL, "Invalid settings for redis connection(incorrect number of sentinel)");
            redisSentinelConfiguration = new RedisSentinelConfiguration().master(masterName);
            for (int i = 0; i < host.length; i++) {
                redisSentinelConfiguration.sentinel(host[i], Integer.valueOf(port[i]));
            }
            jedisConnectionFactory = new JedisConnectionFactory(redisSentinelConfiguration);
        } else {
            checkArgument(host.length == NEED_COUNT_REDIS, "Invalid settings for redis connection(expected single host and port address)");
            jedisConnectionFactory = new JedisConnectionFactory();
        }
    }

    private void switchPool() {
        jedisConnectionFactory.setUsePool(pool);
        if (pool) {
            jedisConnectionFactory.setPoolConfig(poolConfig);
        }
    }

    @PreDestroy
    public void destroy() {
        if (jedisConnectionFactory != null)
            jedisConnectionFactory.destroy();
    }

    public JedisConnectionFactory getJedisConnectionFactory() {
        return jedisConnectionFactory;
    }

    public RedisSentinelConfiguration getRedisSentinelConfiguration() {
        return redisSentinelConfiguration;
    }
}