package com.example.administrator.sometest.eglTest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.example.administrator.sometest.R;

import java.util.ArrayList;
import java.util.List;

public class EGLTestActivity2 extends Activity implements Callback, View.OnClickListener {
    private float[][] coors = new float[][]{
            new float[]{
                    -1f, -1f, 1.0f,
                    -0.5f, -1f, 1.0f,
                    -1f, -0.5f, 1.0f,
                    -0.5f, -0.5f, 1.0f
            },
            new float[]{
                    -0.5f, -1f, 1.0f,
                    0.5f, -1f, 1.0f,
                    -0.5f, -0.5f, 1.0f,
                    0.5f, -0.5f, 1.0f
            },
            new float[]{
                    0.5f, -1f, 1.0f,
                    1f, -1f, 1.0f,
                    0.5f, -0.5f, 1.0f,
                    1f, -0.5f, 1.0f
            },

            new float[]{
                    -1.0f, 0.5f, 1.0f,
                    -0.5f, 0.5f, 1.0f,
                    -1.0f, 1.0f, 1.0f,
                    -0.5f, 1.0f, 1.0f
            },
            new float[]{
                    -0.5f, 0.5f, 1.0f,
                    0.5f, 0.5f, 1.0f,
                    -0.5f, 1.0f, 1.0f,
                    0.5f, 1.0f, 1.0f
            },
            new float[]{
                    0.5f, 0.5f, 1.0f,
                    1f, 0.5f, 1.0f,
                    0.5f, 1.0f, 1.0f,
                    1f, 1f, 1.0f
            },


            new float[]{
                    -1f, -0.5f, 1.0f,
                    -0.5f, -0.5f, 1.0f,
                    -1f, 0.5f, 1.0f,
                    -0.5f, 0.5f, 1.0f
            },

            new float[]{
                    -0.5f, -0.5f, 1.0f,
                    0.5f, -0.5f, 1.0f,
                    -0.5f, 0.5f, 1.0f,
                    0.5f, 0.5f, 1.0f
            },
            new float[]{
                    0.5f, -0.5f, 1.0f,
                    1f, -0.5f, 1.0f,
                    0.5f, 0.5f, 1.0f,
                    1f, 0.5f, 1.0f
            },


    };

    ProgressDialog progressDialog;
    SurfaceView surfaceView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_egltest2);
        surfaceView = (SurfaceView)findViewById(R.id.sv1);
        surfaceView.getHolder().addCallback(this);
        findViewById(R.id.bt1).setOnClickListener(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

        List<MultiChannelVideoPLayer.VideoInfo> videoInfoList = genDebugData();
        videoDisplay(videoInfoList);
    }

    private void videoDisplay(final List<MultiChannelVideoPLayer.VideoInfo> videoInfoList){
        final String savePath = "/sdcard/"+System.currentTimeMillis()+".mp4";
        final List<MultiChannelVideoPLayer.VideoInfo> acceptList  = MultiChannelVideoPLayer.getInstance().init(videoInfoList, savePath);
        if(acceptList.size() != 0 && acceptList.size() < videoInfoList.size()){
            progressDialog.show();
            MultiChannelVideoPLayer.getInstance().playOffScreen(new MultiChannelVideoPLayer.PlayEndListener() {
                @Override
                public void onEnd() {
                    progressDialog.dismiss();
                    videoInfoList.removeAll(acceptList);
                    MultiChannelVideoPLayer.VideoInfo videoInfo = new MultiChannelVideoPLayer.VideoInfo();
                    videoInfo.setVideoPath(savePath);
                    videoInfo.setShowRectCoor(new float[]{
                            -1, -1, 1,
                            1, -1, 1,
                            -1, 1, 1,
                            1, 1, 1
                    });
                    videoInfoList.add(0, videoInfo);
                    videoDisplay(videoInfoList);
                }
            });
        }else if(acceptList.size() == videoInfoList.size()){
            MultiChannelVideoPLayer.getInstance().setLoop(true);
            surfaceView.post(new Runnable() {
                @Override
                public void run() {
                    if(acceptList.size() == 0){
                        MultiChannelVideoPLayer.getInstance().setRenderState(MultiChannelVideoPLayer.REAL_SCREEN);
                    }else {
                        MultiChannelVideoPLayer.getInstance().setRenderState(MultiChannelVideoPLayer.BOTH_SCREEN);
                    }
                    MultiChannelVideoPLayer.getInstance().play(surfaceView.getHolder().getSurface());
                }
            });
        }
    }


    boolean displayScreen;

    @Override
    public void onClick(final View v) {
        int id = v.getId();
        if (id == R.id.bt1) {
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

//        MultiChannelVideoPLayer.getInstance().play(surfaceView.getHolder().getSurface());
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        MultiChannelVideoPLayer.getInstance().stop();
    }

    private List<MultiChannelVideoPLayer.VideoInfo>  genDebugData(){
        List<MultiChannelVideoPLayer.VideoInfo> videoInfoList = new ArrayList<>();
        for(int i = 0; i < 9; i++){
            MultiChannelVideoPLayer.VideoInfo videoInfo = new MultiChannelVideoPLayer.VideoInfo();
            videoInfo.setShowRectCoor(coors[i]);
            videoInfoList.add(videoInfo);
        }
        return videoInfoList;
    }

}