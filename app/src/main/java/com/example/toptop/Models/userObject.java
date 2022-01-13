package com.example.toptop.Models;

public class userObject {
    private int following,follower,sum_heart;
    private String user_id,user_name,profileImage;

    public userObject() {
    }

    public userObject(int following, int follower, int sum_heart, String user_id, String user_name, String profileImage) {
        this.following = following;
        this.follower = follower;
        this.sum_heart = sum_heart;
        this.user_id = user_id;
        this.user_name = user_name;
        this.profileImage = profileImage;
    }

    public int getFollowing() {
        return following;
    }

    public void setFollowing(int following) {
        this.following = following;
    }

    public int getFollower() {
        return follower;
    }

    public void setFollower(int follower) {
        this.follower = follower;
    }

    public int getSum_heart() {
        return sum_heart;
    }

    public void setSum_heart(int sum_heart) {
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

    @Override
    public String toString() {
        return "userObject{" +
                "following=" + following +
                ", follower=" + follower +
                ", sum_heart=" + sum_heart +
                ", user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", profileImage='" + profileImage + '\'' +
                '}';
    }

}

