package ua.pb.task.manager.model;

/**
 * Created by Mike on 3/10/2016.
 */
public class TokenInfo {

    private String code;
    private String error;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public boolean isTokenValid() {
        if (error != null) {
            throw new IllegalStateException(error);
        }
        return true;
    }
}
