package ua.pb.task.manager.service.session;

import org.springframework.stereotype.Repository;

/**
 * Created by Mednikov on 25.03.2016.
 */
@Repository
public class CacheStorage<T> implements Storage {

//https://github.com/google/guava/wiki/CachesExplained
    @Override
    public void storeObject(String key, Object object) {

    }

    @Override
    public void storeObject(String key, Object object, long storeTime) {

    }

    @Override
    public String storeObject(Object object) {
        return null;
    }

    @Override
    public String storeObject(Object object, long storeTime) {
        return null;
    }

    @Override
    public Object getObject(String key) {
        return null;
    }

    @Override
    public void deleteObject(String key) {

    }
}
