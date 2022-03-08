package com.example.toptop.Models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MediaObjectt implements Serializable {
    private String video_id,video_heart,video_comment,follower;
    private String video_des,video_uri;
    private String user_id,user_name,profileImage;
    private String sound_id;
    private String sound_name;
    private String user_phone,hast_task_id,hast_task_name;

    public String getHast_task_id() {
        return hast_task_id;
    }

    public void setHast_task_id(String hast_task_id) {
        this.hast_task_id = hast_task_id;
    }

    public String getHast_task_name() {
        return hast_task_name;
    }

    public void setHast_task_name(String hast_task_name) {
        this.hast_task_name = hast_task_name;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    private String timestamp;


    public MediaObjectt(String video_id, String video_heart, String video_comment, String video_des, String video_uri, String user_id, String user_name, String profileImage, String sound_id, String sound_name, String user_phone, String hast_task_id, String hast_task_name, String timestamp,String follower) {
        this.video_id = video_id;
        this.video_heart = video_heart;
        this.video_comment = video_comment;
        this.video_des = video_des;
        this.video_uri = video_uri;
        this.user_id = user_id;
        this.user_name = user_name;
        this.profileImage = profileImage;
        this.sound_id = sound_id;
        this.sound_name = sound_name;
        this.user_phone = user_phone;
        this.hast_task_id = hast_task_id;
        this.hast_task_name = hast_task_name;
        this.timestamp = timestamp;
        this.follower=follower;
    }

    public MediaObjectt() {

    }
    public String getUser_phone() {
        return user_phone;
    }

    public void setUser_phone(String user_phone) {
        this.user_phone = user_phone;
    }


    public String getVideo_des() {
        return video_des;
    }

    public void setVideo_des(String video_des) {
        this.video_des = video_des;
    }

    public String getVideo_uri() {
        return video_uri;
    }

    public void setVideo_uri(String video_uri) {
        this.video_uri = video_uri;
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

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getVideo_heart() {
        return video_heart;
    }

    public void setVideo_heart(String video_heart) {
        this.video_heart = video_heart;
    }

    public String getVideo_comment() {
        return video_comment;
    }

    public void setVideo_comment(String video_comment) {
        this.video_comment = video_comment;
    }

    public String getSound_id() {
        return sound_id;
    }

    public void setSound_id(String sound_id) {
        this.sound_id = sound_id;
    }

    public String getSound_name() {
        return sound_name;
    }

    public void setSound_name(String sound_name) {
        this.sound_name = sound_name;
    }

    public String getFollower() {
        return follower;
    }

    public void setFollower(String follower) {
        this.follower = follower;
    }
}
