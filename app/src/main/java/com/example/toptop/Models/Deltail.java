package com.example.toptop.Models;

import java.util.List;

public class Deltail {
    private String hast_task_name;
    private List<MediaObjectt> media;

    public Deltail(String namehasttask, List<MediaObjectt> media) {
        this.hast_task_name = namehasttask;
        this.media = media;
    }

    public String getNamehasttask() {
        return hast_task_name;
    }

    public void setNamehasttask(String namehasttask) {
        this.hast_task_name = namehasttask;
    }

    public List<MediaObjectt> getMedia() {
        return media;
    }

    public void setMedia(List<MediaObjectt> media) {
        this.media = media;
    }
}
