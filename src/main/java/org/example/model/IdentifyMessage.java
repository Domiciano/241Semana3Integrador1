package org.example.model;

public class IdentifyMessage {
    private String type;
    private String itsme;


    public IdentifyMessage() {
    }

    public IdentifyMessage(String itsme, String type) {
        this.itsme = itsme;
        this.type = type;
    }

    public String getItsme() {
        return itsme;
    }

    public void setItsme(String itsme) {
        this.itsme = itsme;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
