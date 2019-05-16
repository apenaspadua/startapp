package com.treinamento.mdomingos.startapp.model;

public class Chat {

    private String sender;
    private String receiver;
    private String message;
    private boolean iseen;

    public Chat(){}

    public Chat(String sender, String receiver, String message, boolean iseen) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.iseen = iseen;

    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean getIseen() {
        return iseen;
    }

    public void setIseen(boolean iseen) {
        this.iseen = iseen;
    }
}
