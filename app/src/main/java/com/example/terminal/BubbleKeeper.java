package com.example.terminal;

public class BubbleKeeper {

    private String message;
    private String date;
    private String sender;

    public BubbleKeeper(){}

    public BubbleKeeper(String message, String date, String sender) {
        this.message = message;
        this.date = date;
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String getSender() {
        return sender;
    }
}
