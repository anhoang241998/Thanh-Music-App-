package com.example.app_media_music;

import android.icu.text.CaseMap;

public class Song {
    private String Title;
    private int File;
    private int Img;

    public Song(String title, int file, int img) {
        Title = title;
        File = file;
        Img  = img;
    }
    public int getImg() {
        return Img;
    }
    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getFile() {
        return File;
    }

    public void setFile(int file) {
        File = file;
    }
}
