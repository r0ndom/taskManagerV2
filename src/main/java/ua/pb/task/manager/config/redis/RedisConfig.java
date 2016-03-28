//package ua.pb.task.manager.config.redis;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.PropertySource;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//import redis.clients.jedis.JedisPoolConfig;
//
///**
// * Created by Mike on 3/27/2016.
// */
//@Configuration
//@PropertySource("classpath:redis.properties")
//public class RedisConfig {
//
//    @Value("${redis.pool.maxTotal}")
//    private Integer maxTotal;
//
//    @Value("${redis.pool.maxIdle}")
//    private Integer maxIdle;
//
//    @Value("${redis.pool.minIdle}")
//    private Integer minIdle;
//
//    @Bean
//    public JedisSentinelConnectionFactory jedisSentinelConnectionFactory() {
//        return new JedisSentinelConnectionFactory();
//    }
//
//    @Bean
//    public JedisPoolConfig poolConfig() {
//        JedisPoolConfig poolConfig = new JedisPoolConfig();
//        poolConfig.setMaxTotal(maxTotal);
//        poolConfig.setMaxIdle(maxIdle);
//        poolConfig.setMinIdle(minIdle);
//        return poolConfig;
//    }
//
//    @Bean
//    public JedisConnectionFactory jedisConnectionFactory() {
//        return jedisSentinelConnectionFactory().getJedisConnectionFactory();
//    }
//
//    @Bean
//    public RedisTemplate redisTemplate() {
//        RedisTemplate redisTemplate = new RedisTemplate();
//        redisTemplate.setConnectionFactory(jedisConnectionFactory());
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        return redisTemplate;
//    }
//}
