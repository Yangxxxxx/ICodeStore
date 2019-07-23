package com.gxz.example.videoedit;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * ================================================
 * 作    者：顾修忠-guxiuzhong@youku.com/gfj19900401@163.com
 * 版    本：
 * 创建日期：2017/3/2-下午7:06
 * 描    述：
 * 修订历史：
 * ================================================
 */

public class VideoExtractFrameAsyncUtils {

    private ThumbExtractListener listener;
    private  int extractW;
    private  int extractH;

    public VideoExtractFrameAsyncUtils(int extractW, int extractH, ThumbExtractListener listener) {
        this.listener = listener;
        this.extractW=extractW;
        this.extractH=extractH;
    }


    public void getVideoThumbnailsInfoForEdit(String videoPath, String OutPutFileDirPath, long startPosition, long endPosition, int thumbnailsCount) {
        MediaMetadataRetriever metadataRetriever = new MediaMetadataRetriever();
        metadataRetriever.setDataSource(videoPath);
        long interval = (endPosition - startPosition) / thumbnailsCount;
        long snapTime = startPosition;
        for (int i = 0; i < thumbnailsCount; i++) {
            if (stop) {
                Log.d("ExtractFrame", "-------ok-stop-stop-->>>>>>>>>");
                metadataRetriever.release();
                break;
            }

            if(snapTime <= endPosition){
                String path = extractFrame(metadataRetriever, snapTime, OutPutFileDirPath);
                sendAPic(path, snapTime);
            }else {
                String path = extractFrame(metadataRetriever, endPosition, OutPutFileDirPath);
                sendAPic(path, endPosition);
            }

            snapTime += interval;
        }
        metadataRetriever.release();
    }


    /**
     * 成功一张add一张
     *
     * @param path path
     * @param time time
     */
    private void sendAPic(String path, long time) {
        if(path == null) return;
        VideoEditInfo info = new VideoEditInfo();
        info.path = path;
        info.time = time;
        if(listener != null){
            listener.onOneThumbExtract(info);
        }
    }

    private String extractFrame(MediaMetadataRetriever metadataRetriever, long time, String OutPutFileDirPath) {
        Bitmap bitmap = metadataRetriever.getFrameAtTime(time * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        if (bitmap != null) {
            Bitmap bitmapNew= scaleImage(bitmap);
            String path= PictureUtils.saveImageToSDForEdit(bitmapNew, OutPutFileDirPath, System.currentTimeMillis() + "_" + time + PictureUtils.POSTFIX);
            if (bitmapNew!=null &&!bitmapNew.isRecycled()) {
                bitmapNew.recycle();
                bitmapNew = null;
            }
            return path;
        }
        return null;
    }

    /**
     * 设置固定的宽度，高度随之变化，使图片不会变形
     *
     * @param bm Bitmap
     * @return Bitmap
     */
    private Bitmap scaleImage(Bitmap bm) {
        if (bm == null) {
            return null;
        }
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = extractW * 1.0f / width;
//        float scaleHeight =extractH*1.0f / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleWidth);
        Bitmap newBm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix,
                true);
        if (!bm.isRecycled()) {
            bm.recycle();
            bm = null;
        }
        return newBm;
    }


    private volatile boolean stop;

    public void stopExtract() {
        stop = true;
    }
}
