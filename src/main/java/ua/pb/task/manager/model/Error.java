package ua.pb.task.manager.model;

/**
 * Created by Mednikov on 29.03.2016.
 */
public class Error {
    private String ref;
    private String type;
    private String message;

    public Error(String ref, String type, String message) {
        this.ref = ref;
        this.type = type;
        this.message = message;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Error{" +
                "ref='" + ref + '\'' +
                ", type='" + type + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
