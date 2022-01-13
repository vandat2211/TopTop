package com.example.toptop.Models;

public class Sound {
    private int sound_id;
    private String sound_name;

    public Sound() {
    }

    public Sound(int sound_id, String sound_name) {
        this.sound_id = sound_id;
        this.sound_name = sound_name;
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
        return "Sound{" +
                "sound_id=" + sound_id +
                ", sound_name='" + sound_name + '\'' +
                '}';
    }
}
