package ua.pb.task.manager.form.enums;


import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.FormType;

import java.util.Map;

/**
 * Created by Mednikov on 22.02.2016.
 */
public class DefaultEnum implements Enumerable {


    private FormProperty formProperty;

    public DefaultEnum(FormProperty formProperty) {
        this.formProperty = formProperty;
    }

    @Override
    public Map<String, String> getValues() {
        FormType type = formProperty.getType();
        return (Map<String,String>)type.getInformation("values");
    }
}
