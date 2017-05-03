package com.example.yangjitao.icodestore.LocalPhotoExhibition;

import android.database.ContentObserver;
import android.net.Uri;
import android.util.Log;

public class GalleryObserver extends ContentObserver {

    private OnGalleryChangeListener onGalleryChangeListener;

    public GalleryObserver(OnGalleryChangeListener onGalleryChangeListener) {
        super(null);
        this.onGalleryChangeListener = onGalleryChangeListener;
    }

    /**
     * 主要在onChange中响应数据库变化，并进行相应处理
     */
    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange);

        Log.e("yang", "photo changed: "+ selfChange + " " + uri);
        if(onGalleryChangeListener != null && uri.toString().equals("content://media/external/images/media")){
            onGalleryChangeListener.onGalleryChange();
        }
    }

//    @Override
//    public boolean deliverSelfNotifications() {
//        return true;
//    }

    public interface OnGalleryChangeListener{
        void onGalleryChange();
    }
}
