package ua.pb.task.manager.model;

import java.util.Date;

/**
 * Created by Mednikov on 10.02.2016.
 */
public class Comment {
    private Long id;
    private String taskId;
    private String ldap;
    private String text;
    private Date date;

    public Comment() {
    }

    public Comment(Long id, String taskId, String ldap, String text, Date date) {
        this.id = id;
        this.taskId = taskId;
        this.ldap = ldap;
        this.text = text;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getLdap() {
        return ldap;
    }

    public void setLdap(String ldap) {
        this.ldap = ldap;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
