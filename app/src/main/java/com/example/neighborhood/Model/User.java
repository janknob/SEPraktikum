package com.example.neighborhood.Model;

public class User {

    private String id;
    private String username;
    private String imgurl;
    private String location;
    private String desc;
    private String sex;
    private String age;

    public User (String id, String username, String imgurl, String location, String desc, String sex, String age)
    {
        this.id = id;
        this.username = username;
        this.imgurl = imgurl;
        this.location = location;
        this.desc = desc;
        this.sex = sex;
        this.age = age;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imageurl) {
        this.imgurl = imageurl;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
