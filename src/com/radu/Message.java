package com.radu;

import java.io.Serializable;

public class Message implements Serializable {
    private String sender;
    private String recipient;
    private String message;

    public Message(String sender, String recipient, String message) {
        this.sender = sender;
        this.recipient = recipient;
        this.message = message;
    }

    @Override
    public String toString() {
        return sender + ": " + message;
    }

    public String getSender() {
        return sender;
    }

    public String getRecipient() {
        return recipient;
    }
}
