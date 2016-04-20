package ua.pb.task.manager.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mednikov on 05.01.2016.
 */
public class FormData {
    private String id;
    private Map<String, String> map;

    public FormData() {
    }

    public FormData(String id, Map<String, String> map) {
        this.id = id;
        this.map = map;
    }

    public FormData(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, String> getMap() {
        return map;
    }

    public void setMap(Map<String, String> map) {
        this.map = map;
    }

    public FormData put(String variableName, String value) {
        if(map == null){
            map = new HashMap<>();
        }
        map.put(variableName, value);
        return this;
    }
}
