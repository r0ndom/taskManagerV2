package ua.pb.task.manager.model.filter;


public class TaskSearchFilter {
    private String status;
    private String executor;
    private String author;

    public TaskSearchFilter() {
    }

    public TaskSearchFilter(String status, String executor, String author) {
        this.status = status;
        this.executor = executor;
        this.author = author;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
