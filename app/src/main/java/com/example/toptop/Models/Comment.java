package com.example.toptop.Models;

public class Comment {
    String cID,comment,user_id,user_name,time_comment,image_user,heart_comment,video_id,count_comment;

    public String getCount_comment() {
        return count_comment;
    }

    public void setCount_comment(String count_comment) {
        this.count_comment = count_comment;
    }

    public Comment(String cID, String comment, String user_id, String user_name, String time_comment, String image_user, String heart_comment, String video_id, String count_comment) {
        this.cID = cID;
        this.comment = comment;
        this.user_id = user_id;
        this.user_name = user_name;
        this.time_comment=time_comment;
        this.image_user=image_user;
        this.heart_comment=heart_comment;
        this.video_id=video_id;
        this.count_comment=count_comment;
    }

    public Comment() {
    }

    public String getVideo_id() {
        return video_id;
    }

    public void setVideo_id(String video_id) {
        this.video_id = video_id;
    }

    public String getImage_user() {
        return image_user;
    }

    public void setImage_user(String image_user) {
        this.image_user = image_user;
    }

    public String getHeart_comment() {
        return heart_comment;
    }

    public void setHeart_comment(String heart_comment) {
        this.heart_comment = heart_comment;
    }

    public String getcID() {
        return cID;
    }

    public String getTime_comment() {
        return time_comment;
    }

    public void setTime_comment(String time_comment) {
        this.time_comment = time_comment;
    }

    public void setcID(String cID) {
        this.cID = cID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
}
