package com.example.administrator.sometest.eglTest.VideoPlayer;

import android.os.Handler;
import android.os.HandlerThread;
import android.view.Surface;

import java.util.List;

/**
 * Created by Administrator on 2017/9/28 0028.
 */

public class NewVideoPlayer {
    private static final NewVideoPlayer ourInstance = new NewVideoPlayer();

    public static NewVideoPlayer getInstance() {
        return ourInstance;
    }

    private NewVideoPlayer() {
        init();
    }
    /***********************************************************************************************/
    private Handler mhandler;
    private RenderModel renderModel;

    public void playerInit(final List<RenderModel.VideoInfo> videoInfoList, final PlayerInitListener listener){
        mhandler.post(new Runnable() {
            @Override
            public void run() {
                List<RenderModel.VideoInfo> acceptList = renderModel.prePare(videoInfoList, null);
                if(listener != null){
                    listener.onSuccess(acceptList);
                }
            }
        });
    }

    public void play(final Surface surface){
        mhandler.post(new Runnable() {
            @Override
            public void run() {
                renderModel.realScreenRender(surface);
            }
        });
    }

    public void offScreenPlay(final String savePath, final OffScreenRenderEndListener listener){
        mhandler.post(new Runnable() {
            @Override
            public void run() {
                renderModel.offScreenRender(savePath);
                if(listener != null) listener.onRenderEnd();
            }
        });
    }

    public void bothRender(final String savePath, final OffScreenRenderEndListener listener, final Surface surface){
        mhandler.post(new Runnable() {
            @Override
            public void run() {
                renderModel.bothRender(surface, savePath);
                if(listener != null) listener.onRenderEnd();
            }
        });
    }

    public void stop(){
        renderModel.setRenderStarte(false);
    }


    private void init(){
        renderModel = new RenderModel();

        HandlerThread handlerThread = new HandlerThread("videoPlayerThread");
        handlerThread.start();
        mhandler = new Handler(handlerThread.getLooper());
    }


    public interface PlayerInitListener{
       void onSuccess(List<RenderModel.VideoInfo> videoInfoList);
    }

    public interface OffScreenRenderEndListener{
        void onRenderEnd();
    }

}
