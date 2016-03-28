//package ua.pb.task.manager.repository.session;
//
//import org.apache.commons.codec.binary.Base64;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.ValueOperations;
//import org.springframework.stereotype.Repository;
//
//import javax.annotation.Resource;
//import java.security.SecureRandom;
//import java.util.concurrent.TimeUnit;
//
//
//@Repository
//public class RedisStorage<T> implements Storage<T> {
//    private static Logger LOG = LoggerFactory.getLogger(RedisStorage.class);
//    //default store object for about 12 hours
//    public static final long MAX_STORE_TIME = 18000;
//    @Autowired
//    private RedisTemplate<String, T> template;
//    @Value("${auth.default.storeTime}")
//    private Long defaultStoreTime;
//    private SecureRandom sessionGenerator;
//
//    @Resource(name = "redisTemplate")
//    private ValueOperations<String, T> valueOps;
//
//    @Override
//    public void storeObject(String key, T object) {
//        if (LOG.isDebugEnabled())
//            LOG.info("Store object {} by specified key {}", object, key);
//        template.opsForValue().set(key, object, getDefaultStoreTime(), TimeUnit.SECONDS);
//    }
//
//    @Override
//    public void storeObject(String key, T object, long storeTime) {
//        if (LOG.isDebugEnabled())
//            LOG.debug("Store object {} by specified key {} with store time {}", object, key, storeTime);
//        template.opsForValue().set(key, object, storeTime, TimeUnit.SECONDS);
//    }
//
//    @Override
//    public String storeObject(T object) {
//        String key = getRandomKey();
//        if (LOG.isDebugEnabled())
//            LOG.debug("Store object {} by key {}", object, key);
//        template.opsForValue().set(key, object, getDefaultStoreTime(), TimeUnit.SECONDS);
//        return key;
//    }
//
//    @Override
//    public String storeObject(T object, long storeTime) {
//        String key = getRandomKey();
//        if (LOG.isDebugEnabled())
//            LOG.debug("Store object {} by key {} with store time {}", object, key, storeTime);
//        template.opsForValue().set(key, object, storeTime, TimeUnit.SECONDS);
//        return key;
//    }
//
//    @Override
//    public T getObject(String key) {
//        T object = template.opsForValue().get(key);
//        LOG.debug("Received object {} by key {}", object, key);
//        return object;
//    }
//
//    public void deleteObject(String key) {
//        LOG.info("Delete object by key {}", key);
//        template.delete(key);
//    }
//
//    private String getRandomKey() {
//        byte[] buff = new byte[32];
//        try {
//            if (sessionGenerator == null) {
//                sessionGenerator = SecureRandom.getInstance("SHA1PRNG");
//                sessionGenerator.generateSeed(2);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            sessionGenerator = new SecureRandom();
//        }
//
//        sessionGenerator.nextBytes(buff);
//        return Base64.encodeBase64URLSafeString(buff);
//    }
//
//    /**
//     * Safe ttl for redis sentinel cluster. Cluster is about 1gb.
//     *
//     * @return default store time
//     */
//    private Long getDefaultStoreTime() {
//        if (defaultStoreTime <= 0) {
//            defaultStoreTime = MAX_STORE_TIME;
//        }
//        return defaultStoreTime;
//    }
//}