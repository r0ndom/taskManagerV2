package ua.pb.task.manager.form;

import org.activiti.engine.ActivitiIllegalArgumentException;
import org.activiti.engine.form.AbstractFormType;

import java.util.Map;


public class UsersFormType extends AbstractFormType {

    private static final long serialVersionUID = 1L;
    public static final String NAME = "users";

    protected Map<String, String> values;

    public String getName() {
        return NAME;
    }

    @Override
    public Object getInformation(String key) {
        if (key.equals("values")) {
            return values;
        }
        return null;
    }

    @Override
    public Object convertFormValueToModelValue(String propertyValue) {
        validateValue(propertyValue);
        return propertyValue;
    }

    @Override
    public String convertModelValueToFormValue(Object modelValue) {
        if (modelValue != null) {
            if (!(modelValue instanceof String)) {
                throw new ActivitiIllegalArgumentException("Model value should be a String");
            }
            validateValue((String) modelValue);
        }
        return (String) modelValue;
    }

    protected void validateValue(String value) {
        if (value != null) {
            if (values != null && !values.containsKey(value)) {
                throw new ActivitiIllegalArgumentException("Invalid value for enum form property: " + value);
            }
        }
    }

}
