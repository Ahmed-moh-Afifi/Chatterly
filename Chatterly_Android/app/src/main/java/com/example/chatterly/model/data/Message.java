package com.example.chatterly.model.data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class Message {
    private final int id;
    private String uid;
    private final String body;
    private final List<String> images;
    private final List<String> videos;
    private final List<String> audios;
    private final LocalDateTime publishDate;
    private Integer childPostId;
    private Integer messageRepliedTo;
    private final String authorId;
    private List<Object> widgets; // Placeholder for Widget
    private int reactionsCount;
    private int repliesCount;
    private final int chatId;
    private Chat chat;
    private User author;

    public Message(int id, String body, List<String> images, List<String> videos, List<String> audios,
                   LocalDateTime publishDate, String authorId, int reactionsCount, int repliesCount, int chatId) {
        this.id = id;
        this.body = body;
        this.images = images;
        this.videos = videos;
        this.audios = audios;
        this.publishDate = publishDate;
        this.authorId = authorId;
        this.reactionsCount = reactionsCount;
        this.repliesCount = repliesCount;
        this.chatId = chatId;
        this.uid = UUID.randomUUID().toString();
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public int getRepliesCount() {
        return repliesCount;
    }

    public void setRepliesCount(int repliesCount) {
        this.repliesCount = repliesCount;
    }

    public int getReactionsCount() {
        return reactionsCount;
    }

    public void setReactionsCount(int reactionsCount) {
        this.reactionsCount = reactionsCount;
    }

    public List<Object> getWidgets() {
        return widgets;
    }

    public void setWidgets(List<Object> widgets) {
        this.widgets = widgets;
    }

    public Integer getMessageRepliedTo() {
        return messageRepliedTo;
    }

    public void setMessageRepliedTo(Integer messageRepliedTo) {
        this.messageRepliedTo = messageRepliedTo;
    }

    public Integer getChildPostId() {
        return childPostId;
    }

    public void setChildPostId(Integer childPostId) {
        this.childPostId = childPostId;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getChatId() {
        return chatId;
    }

    public String getAuthorId() {
        return authorId;
    }

    public LocalDateTime getPublishDate() {
        return publishDate;
    }

    public List<String> getAudios() {
        return audios;
    }

    public List<String> getVideos() {
        return videos;
    }

    public List<String> getImages() {
        return images;
    }

    public String getBody() {
        return body;
    }

    public int getId() {
        return id;
    }

    // Getters and Setters
}