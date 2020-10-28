package com.common.bean;



import java.io.Serializable;

public class ChatInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1360647504610967672L;
    private String command;
    private Long time;
    private String userId;
    private String content;

    public ChatInfo(String command, Long time, String userId, String content) {
        this.command = command;
        this.time = time;
        this.userId = userId;
        this.content = content;
    }

    public ChatInfo() {
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
