package ua.pb.task.manager.repository.session;


import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import ua.pb.task.manager.util.KeyGenerator;

import java.util.concurrent.TimeUnit;

/**
 * Created by Mednikov on 25.03.2016.
 */
//TODO need to replace this impl to ehcache and understand how to use with spring
@Repository
public class CacheStorage<T> implements Storage<T> {

    @Autowired
    private KeyGenerator generator;

    @Value("${auth.default.storeTime}")
    private Long defaultStoreTime;

    private Cache<Object, Object> storage;

    public CacheStorage() {
        storage = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterAccess(456, TimeUnit.SECONDS)
                .build();
    }

    @Override
    public void storeObject(String key, T object) {
        storage.put(key, object);
    }

    @Override
    public void storeObject(String key, T object, long storeTime) {
        storage.put(key, object);
    }

    @Override
    public String storeObject(T object) {
        String key = generator.getRandomKey();
        storage.put(key, object);
        return key;
    }

    @Override
    public String storeObject(T object, long storeTime) {
        String key = generator.getRandomKey();
        storage.put(key, object);
        return key;
    }

    @Override
    public T getObject(String key) {
        return (T) storage.getIfPresent(key);
    }

    @Override
    public void deleteObject(String key) {
        storage.invalidate(key);
    }
}
