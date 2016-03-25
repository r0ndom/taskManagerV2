package ua.pb.task.manager.form;

import org.activiti.engine.form.AbstractFormType;

public class TextAreaFormType extends AbstractFormType {

    public static final String TYPE_NAME = "textarea";

    @Override
    public String getName() {
        return TYPE_NAME;
    }

    @Override
    public Object convertFormValueToModelValue(String propertyValue) {
        return propertyValue;
    }

    @Override
    public String convertModelValueToFormValue(Object modelValue) {
        if (modelValue == null) {
            return null;
        }
        return modelValue.toString();
    }

}
