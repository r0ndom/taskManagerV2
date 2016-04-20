package ua.pb.task.manager.model;

/**
 * Created by Mike on 3/2/2016.
 */
public class TaskDto {
    private String id;
    private String activitiDynamicId;
    private String key;
    private String value;

    public TaskDto(String id, String activitiDynamicId, String key, String value) {
        this.id = id;
        this.activitiDynamicId = activitiDynamicId;
        this.key = key;
        this.value = value;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActivitiDynamicId() {
        return activitiDynamicId;
    }

    public void setActivitiDynamicId(String activitiDynamicId) {
        this.activitiDynamicId = activitiDynamicId;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
