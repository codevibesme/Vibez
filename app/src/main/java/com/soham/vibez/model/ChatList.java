package com.soham.vibez.model;

public class ChatList {
    String lastMessage;
    String user, id, dp;
    public ChatList() {
    }

    public ChatList(String lastMessage, String user, String id, String dp) {
        this.lastMessage = lastMessage;
        this.user = user;
        this.id = id;
        this.dp = dp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDp() {
        return dp;
    }

    public void setDp(String dp) {
        this.dp = dp;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }
}
