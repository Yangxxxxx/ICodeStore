package com.example.yangjitao.icodestore.LocalPhotoExhibition;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


public class LocalPhotoManager {
    private static LocalPhotoManager localPhotoManager;
    private final String TAG = LocalPhotoManager.class.getSimpleName();
    private ContentResolver mContentResolver;
    private Context mContext;
    private List<String> mBucketIds;

    /** 本地图片信息集合*/
    private List<Album> albumList = new ArrayList<>();

    /** 是否已初始化*/
    public boolean isInit = false;

    /** 缩略图list*/
    private HashMap<Integer, String> thumbPhotoMap = new HashMap<>();

    public static LocalPhotoManager getInstance(Context context){
        if(localPhotoManager == null){
            localPhotoManager = new LocalPhotoManager(context);
        }
        return localPhotoManager;
    }

    private LocalPhotoManager(Context context) {
        this.mContext = context;
        mContentResolver = context.getContentResolver();
        mBucketIds = new ArrayList<>();
//        init();

        //注册图库更新监听
        Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        context.getContentResolver().registerContentObserver(imageUri, false, new GalleryObserver(onGalleryChangeListener));
    }

    private GalleryObserver.OnGalleryChangeListener onGalleryChangeListener = new GalleryObserver.OnGalleryChangeListener() {
        @Override
        public void onGalleryChange() {
            notifyPhotoChange();
        }
    };

    private boolean isInitting = false;
    /** 通知图库有更新*/
    public void notifyPhotoChange(){
        if(isInitting) return;
        new Thread(){
            @Override
            public void run() {
                super.run();
                init();
            }
        }.start();
    }

    public List<Album> getAlbum() {
        return albumList;
    }

    public List<Album> init() {
        isInitting = true;
        mBucketIds.clear();
        albumList.clear();
        thumbPhotoMap.clear();

        long preTime = System.currentTimeMillis();

        getThumbnailPhoto();

        Log.e("yang", "getThumbnail time: "+ ((System.currentTimeMillis() - preTime)));

        String projects[] = new String[]{
                MediaStore.Images.Media.BUCKET_ID,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME
        };

        Cursor cursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                , projects
                , null
                , null
                , MediaStore.Images.Media.DATE_MODIFIED);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Album album = new Album();

                String buckedId = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));

                if (mBucketIds.contains(buckedId)) continue;

                mBucketIds.add(buckedId);

                String buckedName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                String coverPath = getFrontCoverData(buckedId);

                album.setId(buckedId);
                album.setName(buckedName);
                album.setCoverPath(coverPath);

                List<Photo> childPhotoList = getPhoto(buckedId);
                album.setChildPhotoList(childPhotoList);
                album.setPhotoNumber(childPhotoList.size());

                albumList.add(album);


            } while (cursor.moveToNext());

            cursor.close();
        }

        Log.e("yang", "getOrignal time: "+ ((System.currentTimeMillis() - preTime)));

        Album album = createAllPhotoAlbum();
        if(album != null) {
            albumList.add(0, album);
        }

        isInit = true;
        isInitting = false;
        return albumList;
    }

    public List<Photo> getPhoto(String buckedId) {
        List<Photo> photos = new ArrayList<>();

        Cursor cursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                , new String[]{MediaStore.Images.Media._ID, MediaStore.Images.Media.DATA, MediaStore.Images.Media.DATE_ADDED, MediaStore.Images.Media.DATE_MODIFIED}
                , MediaStore.Images.Media.BUCKET_ID + "=?"
                , new String[]{buckedId}
                , MediaStore.Images.Media.DATE_MODIFIED);
        if (cursor != null && cursor.moveToFirst()) {
            do {

                int imageID = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media._ID));
                String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                Long dataAdded = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                Long dataModified = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED));

//                String thumbnailPath = getThumbNail(imageID);
                String thumbnailPath = thumbPhotoMap.get(imageID);


                Photo photo = new Photo(path,dataAdded,dataModified);
                photo.setThumbnailPath(thumbnailPath);

                photos.add(photo);

            } while (cursor.moveToNext());
            cursor.close();
        }

        return photos;
    }


    private String getFrontCoverData(String bucketId) {
        String path = "empty";
        Cursor cursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{MediaStore.Images.Media.DATA}, MediaStore.Images.Media.BUCKET_ID + "=?", new String[]{bucketId}, MediaStore.Images.Media.DATE_MODIFIED);
        if (cursor != null && cursor.moveToFirst()) {
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
        }
        return path;
    }

    /** 获取缩略图列表*/
    private void getThumbnailPhoto() {
        Cursor cursor = MediaStore.Images.Thumbnails.query(mContentResolver
                , MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI
                ,new String[]{MediaStore.Images.Thumbnails.DATA, MediaStore.Images.Thumbnails.IMAGE_ID}
        );

        if (cursor == null || !cursor.moveToFirst()) return;

        do {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Thumbnails.IMAGE_ID));
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            thumbPhotoMap.put(id, path);
        } while (cursor.moveToNext());

        cursor.close();
    }

    private String getThumbNail(int imageID){
        String path = null;
        Cursor cursor = mContentResolver.query(
                MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI,
                new String[]{
                        MediaStore.Images.Thumbnails.DATA
                },
                MediaStore.Images.Thumbnails.IMAGE_ID+"="+imageID,
                null,
                null);
        if (cursor != null && cursor.moveToFirst()) {
            path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            cursor.close();
        }
        return path;
    }

    public List<Photo> getAllPhoto() {
        List<Photo> photos = new ArrayList<>();

       for(Album album: albumList){
           photos.addAll(album.getChildPhotoList());
       }

        Collections.sort(photos, new Comparator<Photo>() {
            @Override
            public int compare(Photo lhs, Photo rhs) {
                long l = lhs.getDataModified();
                long r = rhs.getDataModified();
                return  l > r ? -1 : (l == r ? 0 : 1);
            }
        });

        return photos;
    }


    private Album  createAllPhotoAlbum(){
        Album album  = new Album();
//        List<Photo> allPhotoList = new ArrayList<>();
//        for(Album item: albumList){
//            allPhotoList.addAll(item.getChildPhotoList());
//        }
        List<Photo> allPhotoList = getAllPhoto();


        if(allPhotoList.size() == 0) return null;

        album.setChildPhotoList(allPhotoList);
        album.setCoverPath(allPhotoList.get(0).getPath());
        album.setPhotoNumber(allPhotoList.size());
        album.setName("所有图片");
        return album;
    }
}
