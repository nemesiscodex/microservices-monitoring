package io.nemesiscodex.web.rest.vm;

public class Result {
    private String message;
    private boolean success;
    private String jobKey;

    public Result(String message, boolean success, String jobKey) {
        this.message = message;
        this.success = success;
        this.jobKey = jobKey;
    }

    public String getJobKey() {
        return jobKey;
    }

    public Result setJobKey(String jobKey) {
        this.jobKey = jobKey;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}

