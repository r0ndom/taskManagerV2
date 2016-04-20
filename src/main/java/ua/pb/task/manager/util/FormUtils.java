package ua.pb.task.manager.util;

import org.activiti.engine.form.FormProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.pb.task.manager.form.enums.Enumerable;
import ua.pb.task.manager.form.enums.EnumerableFactory;
import ua.pb.task.manager.model.FormFieldData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by stas on 21.01.16.
 */
@Component
public class FormUtils {

    @Autowired
    private EnumerableFactory factory;

    public List<FormFieldData> convertTo(List<FormProperty> formPropertyList){
        List<FormFieldData> formFieldDataList = new ArrayList<>();
        for(FormProperty formProperty: formPropertyList){
            Enumerable enumerable = factory.buildEnum(formProperty);
            FormFieldData formFieldData = new FormFieldData();
            formFieldData.setId(formProperty.getId());
            formFieldData.setName(formProperty.getName());
            formFieldData.setRequired(formProperty.isRequired());
            formFieldData.setReadable(formProperty.isReadable());
            formFieldData.setWritable(formProperty.isWritable());
            formFieldData.setType(formProperty.getType().getName());
            formFieldData.setValue(formProperty.getValue());
            formFieldData.setSelectValues(enumerable.getValues());
            formFieldDataList.add(formFieldData);
        }
        return formFieldDataList;
    }

}
