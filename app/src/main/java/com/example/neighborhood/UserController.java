package com.example.neighborhood;

public class UserController {

    public void searchUser(){

    }

    public void showUser(){

    }

    public void createUser(String nickname, String email, String pwd){
        //User user = new User();
    }

    public void deleteUser(int id){

    }

    public void changeUserProfile(){

    }

    public void showFriendRequests(){

    }

    public void sendFriendRequest(int id){
        //add user (id) to requested list
    }

    public void acceptFriendRequest(int id){
        //delete from Requested list (id)
        //add to Freidnlist (id)
    }

    public void declineFriendRequest(int id){
        //delete Request from id
    }

    public void logIn(String nickname, String pwd){
        //check name and pwd with DB and Log in or show Failed
    }

    public void createChat(int id, int otherId){
        //create a chat with user id and another userId
    }
}
