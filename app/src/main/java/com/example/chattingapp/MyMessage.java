package com.example.chattingapp;
public class MyMessage {
    private String sender;
    private String text;
    private boolean isOutgoing;

    public MyMessage(){

    }
    public MyMessage(String sender, String text, Boolean isOutgoing){
        this.sender = sender;
        this.text = text;
        this.isOutgoing = isOutgoing;
    }
    public String getSender() {
        return sender;
    }

    public String getText() {
        return text;
    }
    public boolean getIsOutgoing() {
        return isOutgoing;
    }


}
