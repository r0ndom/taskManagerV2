package ua.pb.task.manager.model;

import java.util.Map;

/**
 * Created by stas on 21.01.16.
 */
public class FormFieldData {
    private String id;
    private String name;
    private String type;
    private boolean isRequired;
    private boolean isReadable;
    private boolean isWritable;
    private String value;
    private Map<String, String> selectValues;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public boolean isRequired() {
        return isRequired;
    }

    public void setRequired(boolean required) {
        isRequired = required;
    }

    public boolean isReadable() {
        return isReadable;
    }

    public void setReadable(boolean readable) {
        isReadable = readable;
    }

    public boolean isWritable() {
        return isWritable;
    }

    public void setWritable(boolean writable) {
        isWritable = writable;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Map<String, String> getSelectValues() {
        return selectValues;
    }

    public void setSelectValues(Map<String, String> selectValues) {
        this.selectValues = selectValues;
    }
}
