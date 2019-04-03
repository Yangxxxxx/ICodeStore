package com.example.yangjitao.icodestore.LocalPhotoExhibition;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Photo bean
 * Created by Flying SnowBean on 16-4-4.
 */
public class Photo implements Parcelable {
    private String path;
    private long dataAdded;
    private long dataModified;
    private String thumbnailPath;

    public Photo(String path, long dataAdded, long dataModified) {
        this.path = path;
        this.dataAdded = dataAdded;
        this.dataModified = dataModified;
    }

    protected Photo(Parcel in) {
        path = in.readString();
        dataAdded = in.readLong();
        dataModified = in.readLong();
    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel in) {
            return new Photo(in);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }
    };

    public String getPath() {
//        if(thumbnailPath != null) return thumbnailPath;
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public long getDataAdded() {
        return dataAdded;
    }

    public void setDataAdded(long dataAdded) {
        this.dataAdded = dataAdded;
    }

    public long getDataModified() {
        return dataModified;
    }

    public void setDataModified(long dataModified) {
        this.dataModified = dataModified;
    }

    public String getThumbnailPath() {
        if(thumbnailPath != null) return thumbnailPath;
        return path;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(path);
        dest.writeLong(dataAdded);
        dest.writeLong(dataModified);
        dest.writeString(thumbnailPath);
    }
}
