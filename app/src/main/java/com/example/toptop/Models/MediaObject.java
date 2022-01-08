package com.example.toptop.Models;

import android.graphics.Bitmap;

public class MediaObject {
    public MediaObject(){

    }


    public String getUsers() {
        return users;
    }

    public void setUsers(String users) {
        this.users = users;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMedia_url() {
        return media_url;
    }

    public void setMedia_url(String media_url) {
        this.media_url = media_url;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public String getTim() {
        return tim;
    }

    public void setTim(String tim) {
        this.tim = tim;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public MediaObject(String users, String description, String media_url, String sound, String tim, String comment, String img) {
        this.users = users;
        this.description = description;
        this.media_url = media_url;
        this.sound = sound;
        this.tim = tim;
        this.comment = comment;
        this.img = img;
    }

    private String users,description,media_url,sound,tim,comment,img;

}
