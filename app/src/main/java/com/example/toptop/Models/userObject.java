package com.example.toptop.Models;

import java.io.Serializable;

public class userObject implements Serializable {
    private String following,follower,sum_heart;
    private String user_id;

    public String getOnlineStatus() {
        return onlineStatus;
    }

    public void setOnlineStatus(String onlineStatus) {
        this.onlineStatus = onlineStatus;
    }

    private String onlineStatus;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String email;
    private String user_name;
    private String profileImage;

    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }

    private String user_phone;

    public userObject(String following, String follower, String sum_heart, String user_id, String onlineStatus, String email, String user_name, String profileImage, String user_phone) {
        this.following = following;
        this.follower = follower;
        this.sum_heart = sum_heart;
        this.user_id = user_id;
        this.onlineStatus = onlineStatus;
        this.email = email;
        this.user_name = user_name;
        this.profileImage = profileImage;
        this.user_phone = user_phone;
    }

    public userObject() {
    }


    public String getFollowing() {
        return following;
    }

    public void setFollowing(String following) {
        this.following = following;
    }

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }

    public String getSum_heart() {
        return sum_heart;
    }

    public void setSum_heart(String sum_heart) {
        this.sum_heart = sum_heart;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }


}

