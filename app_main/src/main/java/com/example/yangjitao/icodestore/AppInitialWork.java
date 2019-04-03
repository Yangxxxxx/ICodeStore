package com.example.yangjitao.icodestore;

import android.content.Context;
import android.util.Log;

import com.example.yangjitao.icodestore.CrashProcess.CrashHandler;
import com.example.yangjitao.icodestore.LocalPhotoExhibition.LocalPhotoManager;
import com.example.yangjitao.icodestore.Utils.CommonUtils;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by yangjitao on 17/5/3.
 */
public class AppInitialWork {

    public void init(Context context){
        initImageLoader(context);
        LocalPhotoManager.getInstance(context).init();
        CrashHandler.getInstance().init(context);
        Log.e("yang", "win project github test");
    }

    public static void initImageLoader(Context context) {
        if(ImageLoader.getInstance().isInited()) return;

        ImageLoaderConfiguration config =
                new ImageLoaderConfiguration.Builder(context)
                        .memoryCacheExtraOptions(640, 640)
                        .diskCacheFileCount(3000)
                        .diskCacheSize(200 * 1024 * 1024)
                        .diskCacheFileNameGenerator(new HashCodeFileNameGenerator())
                        .threadPoolSize(3)
                        .threadPriority(Thread.NORM_PRIORITY - 1)
                        .defaultDisplayImageOptions(CommonUtils.getDefaultDisplayOption().build())
                        .build();

        ImageLoader.getInstance().init(config);
    }


    /** 单利类*/
    private static AppInitialWork ourInstance = new AppInitialWork();

    public static AppInitialWork getInstance() {
        return ourInstance;
    }

    private AppInitialWork() {
    }
}
