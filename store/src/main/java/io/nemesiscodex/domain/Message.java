package io.nemesiscodex.domain;

import org.springframework.data.annotation.Id;

import java.time.Instant;


public class Message {
    @Id
    private String id;
    private String message;
    private Instant time;

    public Message(String message, Instant time) {
        this.message = message;
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public Message setId(String id) {
        this.id = id;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Message setMessage(String message) {
        this.message = message;
        return this;
    }

    public Instant getTime() {
        return time;
    }

    public Message setTime(Instant time) {
        this.time = time;
        return this;
    }
}
