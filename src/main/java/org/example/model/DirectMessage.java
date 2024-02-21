package org.example.model;

public class DirectMessage extends BroadcastMessage{

    //para quien
    private String to;

    public DirectMessage(String type, String author, String message, String to) {
        super(type, author, message);
        this.to = to;
    }

    public DirectMessage(){}

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
