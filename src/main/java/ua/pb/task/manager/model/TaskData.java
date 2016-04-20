package ua.pb.task.manager.model;

import java.util.Map;

/**
 * Created by Mike on 12/31/2015.
 */
public class TaskData {

    private String id;
    private String activitiDynamicId;
    Map<String, String> params;

    public TaskData(Map<String, String> params) {
        this.params = params;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TaskData(){

    }

    public String getName() {
        return params.get("name");
    }

    public String getExecutor() {
        return params.get("executor");
    }

    public String getAuthor() {
        return params.get("author");
    }

    public Long getExpectedTime() {
        String expectedTimeStr = params.get("expectedTime");
        return (expectedTimeStr != null && !expectedTimeStr.equals(""))
                ? Long.parseLong(expectedTimeStr) : null;
    }

    public String getDescription() {
        return params.get("description");
    }

    public String getActivitiDynamicId() {
        return activitiDynamicId;
    }

    public void setActivitiDynamicId(String activitiDynamicId) {
        this.activitiDynamicId = activitiDynamicId;
    }

    public Map<String, String> getParams() {
        return params;
    }
}

