package ua.pb.task.manager.service.session;

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

    @Value("${auth.default.store.time}")
    private Long defaultStoreTime;
    @Autowired
    private KeyGenerator generator;

    @Override
    public void storeObject(String key, T object) {
        if (LOG.isDebugEnabled())
            LOG.info("Store object {} by specified key {}", object, key);
        //template.opsForValue().set(key, object, getDefaultStoreTime(), TimeUnit.SECONDS);
    }

    @Override
    public void storeObject(String key, T object, long storeTime) {
        if (LOG.isDebugEnabled())
            LOG.debug("Store object {} by specified key {} with store time {}", object, key, storeTime);
        //template.opsForValue().set(key, object, storeTime, TimeUnit.SECONDS);
    }

    @Override
    public String storeObject(T object) {
        String key = generator.getRandomKey();
        if (LOG.isDebugEnabled())
            LOG.debug("Store object {} by key {}", object, key);
        //template.opsForValue().set(key, object, getDefaultStoreTime(), TimeUnit.SECONDS);
        return key;
    }

    @Override
    public String storeObject(T object, long storeTime) {
        String key = generator.getRandomKey();
        if (LOG.isDebugEnabled())
            LOG.debug("Store object {} by key {} with store time {}", object, key, storeTime);
        //template.opsForValue().set(key, object, storeTime, TimeUnit.SECONDS);
        return key;
    }

    @Override
    public T getObject(String key) {
        //T object = template.opsForValue().get(key);
        LOG.debug("Received object {} by key {}", object, key);
        return object;
    }

    @Override
    public void deleteObject(String key) {
        LOG.info("Delete object by key {}", key);
        //template.delete(key);
    }

}
