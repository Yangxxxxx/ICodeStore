package com.example.administrator.sometest.eglTest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.administrator.sometest.R;
import com.example.administrator.sometest.eglTest.VideoPlayer.NewVideoPlayer;
import com.example.administrator.sometest.eglTest.VideoPlayer.RenderModel;

import java.util.ArrayList;
import java.util.List;

public class EGLTestActivity3 extends Activity implements View.OnClickListener {
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
        setContentView(R.layout.activity_egltest3);
        surfaceView = (SurfaceView)findViewById(R.id.sv1);
        findViewById(R.id.bt1).setOnClickListener(this);
        findViewById(R.id.bt2).setOnClickListener(this);


        progressDialog = new ProgressDialog(this);
        progressDialog.setCanceledOnTouchOutside(false);

    }

    private void videoDisplay2(final List<RenderModel.VideoInfo> videoInfoList){
        new Thread(){
            @Override
            public void run() {
                super.run();
                if (videoInfoList.size() == 0 || videoInfoList.size() == 1) return;

                final String savePath = "/sdcard/" + System.currentTimeMillis() + "offscreen.mp4";
                NewVideoPlayer.getInstance().playerInit(videoInfoList, new NewVideoPlayer.PlayerInitListener() {
                    @Override
                    public void onSuccess(final List<RenderModel.VideoInfo> acceptList) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(EGLTestActivity3.this, "acceptList size: " + acceptList.size(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        if (acceptList.size() == 0) return;

                        if (acceptList.size() <= videoInfoList.size()) {
                            Log.e("yang9", "onRenderstart");
                            NewVideoPlayer.getInstance().offScreenPlay(savePath, new NewVideoPlayer.OffScreenRenderEndListener() {
                                @Override
                                public void onRenderEnd() {
                                    Log.e("yang9", "onRenderEnd");
                                    videoInfoList.removeAll(acceptList);
                                    RenderModel.VideoInfo videoInfo = new RenderModel.VideoInfo();
                                    videoInfo.setVideoPath(savePath);
                                    videoInfo.setShowRectCoor(new float[]{
                                            -1, -1, 1,
                                            1, -1, 1,
                                            -1, 1, 1,
                                            1, 1, 1
                                    });
                                    videoInfoList.add(0, videoInfo);

                                    videoDisplay2(videoInfoList);

                                }
                            });

                        }
                    }
                });

            }
        }.start();


    }

//    private void videoDisplay(final List<RenderModel.VideoInfo> videoInfoList){
//        final String savePath = "/sdcard/"+System.currentTimeMillis()+"offscreen.mp4";
//        NewVideoPlayer.getInstance().playerInit(videoInfoList, new NewVideoPlayer.PlayerInitListener() {
//            @Override
//            public void onSuccess(final List<RenderModel.VideoInfo> acceptList) {
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Toast.makeText(EGLTestActivity3.this, "acceptList size: " + acceptList.size(), Toast.LENGTH_SHORT).show();
//                    }
//                });
//
//                if(acceptList.size() == 0) return;
//
//                if(acceptList.size() == 1){
//                    if(progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
//                    NewVideoPlayer.getInstance().play(surfaceView.getHolder().getSurface());
//
//                }else if(acceptList.size() <= videoInfoList.size()){
//                    progressDialog.show();
//                    Log.e("yang", "onRenderstart");
//                    NewVideoPlayer.getInstance().offScreenPlay(savePath, new NewVideoPlayer.OffScreenRenderEndListener() {
//                        @Override
//                        public void onRenderEnd() {
//                            Log.e("yang", "onRenderEnd");
//                            videoInfoList.removeAll(acceptList);
//                            RenderModel.VideoInfo videoInfo = new RenderModel.VideoInfo();
//                            videoInfo.setVideoPath(savePath);
//                            videoInfo.setShowRectCoor(new float[]{
//                                    -1, -1, 1,
//                                    1, -1, 1,
//                                    -1, 1, 1,
//                                    1, 1, 1
//                            });
//                            videoInfoList.add(0, videoInfo);
//                            videoDisplay(videoInfoList);
//                        }
//                    });
//
//                }
////                else if(acceptList.size() == videoInfoList.size()){
////                    if(progressDialog.isShowing()) {
////                        progressDialog.dismiss();
////                    }
////                    NewVideoPlayer.getInstance().bothRender(savePath, new NewVideoPlayer.OffScreenRenderEndListener() {
////                        @Override
////                        public void onRenderEnd() {
////                            Log.e("yang", "onRenderEnd");
////                        }
////                    }, surfaceView.getHolder().getSurface());
////                }
//            }
//        });
//    }


    @Override
    public void onClick(final View v) {
        int id = v.getId();
        if (id == R.id.bt1) {
            List<RenderModel.VideoInfo> videoInfoList = genDebugData();
            videoDisplay2(videoInfoList);
        }

        if(id == R.id.bt2){
            NewVideoPlayer.getInstance().stop();
        }

    }


    private List<RenderModel.VideoInfo>  genDebugData(){
        List<RenderModel.VideoInfo> videoInfoList = new ArrayList<>();
        for(int i = 0; i < 9; i++){
            RenderModel.VideoInfo videoInfo = new RenderModel.VideoInfo();
            videoInfo.setShowRectCoor(coors[i]);
            videoInfoList.add(videoInfo);
        }
        return videoInfoList;
    }

}