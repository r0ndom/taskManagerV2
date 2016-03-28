package ua.pb.task.manager.repository.session;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import ua.pb.task.manager.util.KeyGenerator;

/**
 * Created by Mednikov on 25.03.2016.
 */
@Repository
public class SessionStorage<T> implements Storage<T> {

    private static Logger LOG = LoggerFactory.getLogger(SessionStorage.class);

    @Autowired
    private CacheStorage<T> cacheStorage;
//    @Autowired
//    private RedisStorage redisStorage;

    @Override
    public void storeObject(String key, T object) {
        cacheStorage.storeObject(key, object);
    }

    @Override
    public void storeObject(String key, T object, long storeTime) {
        cacheStorage.storeObject(key, object, storeTime);
    }

    @Override
    public String storeObject(T object) {
        return cacheStorage.storeObject(object);
    }

    @Override
    public String storeObject(T object, long storeTime) {
        return cacheStorage.storeObject(object, storeTime);
    }

    @Override
    public T getObject(String key) {
        return cacheStorage.getObject(key);
    }

    @Override
    public void deleteObject(String key) {
        cacheStorage.deleteObject(key);
    }

}
