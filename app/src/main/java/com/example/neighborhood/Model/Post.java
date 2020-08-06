package com.example.neighborhood.Model;

public class Post
{
    private String postID;
    private String postText;
    private String postDate;
    private String eventDate;
    private String eventTime;
    private String publisher;

    public Post (String postID, String postText, String postDate, String eventDate, String eventTime, String publisher)
    {
        this.postID = postID;
        this.postText = postText;
        this.postDate = postDate;
        this.eventDate = eventDate;
        this.eventTime = eventTime;
        this.publisher = publisher;
    }

    public Post ()
    {
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getPostText() {
        return postText;
    }

    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getPostDate() {
        return postDate;
    }

    public void setPostDate(String postDate) {
        this.postDate = postDate;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventTime() {
        return eventTime;
    }

    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }
}
