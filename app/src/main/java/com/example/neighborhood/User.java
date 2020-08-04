package com.example.neighborhood;

import java.util.ArrayList;

public class User {

    private int id;
    private String nickname;
    private String mail;
    private String place;
    private int age;
    private String sex;
    private String description;
    private ArrayList friendRequests;
    private ArrayList friendList;

    public User(int id, String nickname, String mail, String place, int age, String sex, String description, ArrayList friendRequests, ArrayList friendList) {
        this.id = id;
        this.nickname = nickname;
        this.mail = mail;
        this.place = place;
        this.age = age;
        this.sex = sex;
        this.description = description;
        this.friendRequests = friendRequests;
        this.friendList = friendList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList getFriendRequests() {
        return friendRequests;
    }

    public void setFriendRequests(ArrayList friendRequests) {
        this.friendRequests = friendRequests;
    }

    public ArrayList getFriendList() {
        return friendList;
    }

    public void setFriendList(ArrayList friendList) {
        this.friendList = friendList;
    }
}
