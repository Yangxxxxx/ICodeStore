package com.example.yangjitao.icodestore.LocalPhotoExhibition;

import java.util.List;

public class Album {
    private String name;
    private String id;
    private String coverPath;
    private int photoNumber;
    private List<Photo> childPhotoList;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public int getPhotoNumber() {
        return photoNumber;
    }

    public void setPhotoNumber(int photoNumber) {
        this.photoNumber = photoNumber;
    }

    public List<Photo> getChildPhotoList() {
        return childPhotoList;
    }

    public void setChildPhotoList(List<Photo> childPhotoList) {
        this.childPhotoList = childPhotoList;
    }
}
