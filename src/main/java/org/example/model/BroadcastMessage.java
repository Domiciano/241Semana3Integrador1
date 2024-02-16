package org.example.model;

public class BroadcastMessage {

    private String type; //broadcast, direct, autoid
    private String author;
    private String message;

    public BroadcastMessage() {
    }

    public BroadcastMessage(String type, String author, String message) {
        this.type = type;
        this.author = author;
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
