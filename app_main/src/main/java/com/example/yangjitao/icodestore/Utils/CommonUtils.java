package com.example.yangjitao.icodestore.Utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;

import com.example.yangjitao.icodestore.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.Timer;
import java.util.TimerTask;

public class CommonUtils {

    public static DisplayImageOptions.Builder getDefaultDisplayOption() {
        return new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.icon_load_error) //设置图片在下载期间显示的图片
                .showImageForEmptyUri(R.mipmap.icon_load_error)//设置图片Uri为空或是错误的时候显示的图片
                .showImageOnFail(R.mipmap.icon_load_error)  //设置图片加载/解码过程中错误时候显示的图片
                .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)//设置下载的图片是否缓存在SD卡中
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型
                .considerExifParams(true);
    }


    /** 获得屏幕信息*/
    public static DisplayMetrics getDisplayMetrics(Activity activity){
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm;
    }

    /**
     * 双击退出
     */
    private static Boolean canExit = false;

    public static boolean exitBy2Click() {
        if (!canExit) {
            canExit = true;
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    canExit = false;
                }
            }, 2000);
            return false;
        }

        return true;
    }

}
