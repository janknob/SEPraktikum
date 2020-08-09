package com.example.neighborhood.Model;



public class User {

    private String id;
    private String nickname;
    private String mail;
    private String place;
    private String age;
    private String sex;
    private String description;
    private String imageUrl;


    public User()
    {

    }
    public User(String id, String nickname, String mail, String place, String age, String sex, String description,String imageUrl) {
        this.id = id;
        this.nickname = nickname;
        this.mail = mail;
        this.place = place;
        this.age = age;
        this.sex = sex;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl)
    {
        this.imageUrl = imageUrl;
    }
}
