package ua.pb.task.manager.model;

/**
 * Created by Mednikov on 30.03.2016.
 */
public class EmailMessage {
    private String to;
    private String subject;
    private String text;

    public EmailMessage(String to, String subject, String text) {
        this.to = to;
        this.subject = subject;
        this.text = text;
    }

    public String getTo() {
        return to;
    }

    public String getSubject() {
        return subject;
    }

    public String getText() {
        return text;
    }
}
