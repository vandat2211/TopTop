package com.example.toptop.Models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MediaObjectt {
    private int video_id,video_heart,video_comment;
    private String video_des,video_uri;
    private int following,follower,sum_heart;
    private String user_id,user_name,profileImage;
    private int sound_id;
    private String sound_name;

    public MediaObjectt(int video_id, int video_heart, int video_comment, String video_des, String video_uri, int following, int follower, int sum_heart, String user_id, String user_name, String profileImage, int sound_id, String sound_name) {
        this.video_id = video_id;
        this.video_heart = video_heart;
        this.video_comment = video_comment;
        this.video_des = video_des;
        this.video_uri = video_uri;
        this.following = following;
        this.follower = follower;
        this.sum_heart = sum_heart;
        this.user_id = user_id;
        this.user_name = user_name;
        this.profileImage = profileImage;
        this.sound_id = sound_id;
        this.sound_name = sound_name;
    }

    public MediaObjectt() {

    }

    public int getVideo_id() {
        return video_id;
    }

    public void setVideo_id(int video_id) {
        this.video_id = video_id;
    }

    public int getVideo_heart() {
        return video_heart;
    }

    public void setVideo_heart(int video_heart) {
        this.video_heart = video_heart;
    }

    public int getVideo_comment() {
        return video_comment;
    }

    public void setVideo_comment(int video_comment) {
        this.video_comment = video_comment;
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

    public int getSound_id() {
        return sound_id;
    }

    public void setSound_id(int sound_id) {
        this.sound_id = sound_id;
    }

    public String getSound_name() {
        return sound_name;
    }

    public void setSound_name(String sound_name) {
        this.sound_name = sound_name;
    }

    @Override
    public String toString() {
        return "MediaObjectt{" +
                "video_id=" + video_id +
                ", video_heart=" + video_heart +
                ", video_comment=" + video_comment +
                ", video_des='" + video_des + '\'' +
                ", video_uri='" + video_uri + '\'' +
                ", following=" + following +
                ", follower=" + follower +
                ", sum_heart=" + sum_heart +
                ", user_id='" + user_id + '\'' +
                ", user_name='" + user_name + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", sound_id=" + sound_id +
                ", sound_name='" + sound_name + '\'' +
                '}';
    }
    public Map<String, Object> toMap(){
        HashMap<String,Object> result =new HashMap<>();
        result.put("videos/"+String.valueOf(video_id)+"user_name",user_name);
        result.put("profileImage",profileImage);
        return result;
    }

    public MediaObjectt(String user_name, String profileImage) {
        this.user_name = user_name;
        this.profileImage = profileImage;
    }

    public MediaObjectt(String user_name) {
        this.user_name = user_name;
    }
}
