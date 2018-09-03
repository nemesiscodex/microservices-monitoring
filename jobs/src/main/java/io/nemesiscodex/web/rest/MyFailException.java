package io.nemesiscodex.web.rest;

public class MyFailException extends RuntimeException {
    public MyFailException(String message) {
        super(message);
    }
}
