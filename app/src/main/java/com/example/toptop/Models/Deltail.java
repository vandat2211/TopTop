package com.example.toptop.Models;

import java.util.List;

public class Deltail {
    private String hast_task_name;
    private List<MediaObjectt> mediaObjecttList;

    public Deltail(String namehasttask, List<MediaObjectt> mediaObjecttList) {
        this.hast_task_name = namehasttask;
        this.mediaObjecttList = mediaObjecttList;
    }

    public Deltail() {

    }

    public String getNamehasttask() {
        return hast_task_name;
    }

    public void setNamehasttask(String namehasttask) {
        this.hast_task_name = namehasttask;
    }

    public List<MediaObjectt> getMedia() {
        return mediaObjecttList;
    }

    public void setMedia(List<MediaObjectt> media) {
        this.mediaObjecttList = media;
    }
}
