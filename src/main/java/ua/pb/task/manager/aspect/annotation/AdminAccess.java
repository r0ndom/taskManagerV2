package ua.pb.task.manager.aspect.annotation;

import ua.pb.task.manager.model.Role;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by Mednikov on 29.03.2016.
 */
//TODO need to add miltiple role support
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(value = RetentionPolicy.RUNTIME)
public @interface AdminAccess {

    Role accessRole() default Role.ROLE_CONTROL;

}