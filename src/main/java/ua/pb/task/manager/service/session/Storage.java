package ua.pb.task.manager.service.session;


public interface Storage<T> {
    /**
     * Store object by key
     * @param key
     * @param object
     */
    void storeObject(String key, T object);

    /**
     * Store object by key until the storeTime comes
     * @param key
     * @param object
     * @param storeTime
     */
    void storeObject(String key, T object, long storeTime);

    /**
     * Store object and return stored key
     * @param object
     * @return key
     */
    String storeObject(T object);

    /**
     * Store object until storeTime and return stored key
     * @param object
     * @param storeTime
     * @return key
     */
    String storeObject(T object, long storeTime);

    /**
     * Get object by key
     * @param key
     * @return T instance or null if object is expired by storeTime
     */
    T getObject(String key);

    void deleteObject(String key);
}