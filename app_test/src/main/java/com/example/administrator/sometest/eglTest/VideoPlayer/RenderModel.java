package com.example.administrator.sometest.eglTest.VideoPlayer;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import android.view.Surface;

import com.example.administrator.sometest.eglTest.STextureRender;
import com.example.administrator.sometest.eglTest.SpeedControl;
import com.example.administrator.sometest.eglTest.SurfaceDecoder;
import com.example.administrator.sometest.eglTest.SurfaceEncoder;
import com.example.administrator.sometest.eglTest.gles.EglCore;
import com.example.administrator.sometest.eglTest.gles.FullFrameRect;
import com.example.administrator.sometest.eglTest.gles.OffscreenSurface;
import com.example.administrator.sometest.eglTest.gles.Texture2dProgram;
import com.example.administrator.sometest.eglTest.gles.WindowSurface;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import static android.opengl.GLES20.GL_CLAMP_TO_EDGE;
import static android.opengl.GLES20.GL_COLOR_ATTACHMENT0;
import static android.opengl.GLES20.GL_FRAMEBUFFER;
import static android.opengl.GLES20.GL_LINEAR;
import static android.opengl.GLES20.GL_TEXTURE_2D;
import static android.opengl.GLES20.GL_TEXTURE_MAG_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_MIN_FILTER;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_S;
import static android.opengl.GLES20.GL_TEXTURE_WRAP_T;
import static android.opengl.GLES20.glBindFramebuffer;
import static android.opengl.GLES20.glBindTexture;
import static android.opengl.GLES20.glFramebufferTexture2D;
import static android.opengl.GLES20.glGenTextures;
import static android.opengl.GLES20.glTexImage2D;
import static android.opengl.GLES20.glTexParameterf;
import static android.opengl.GLES20.glTexParameteri;

/**
 * Created by Administrator on 2017/9/28 0028.
 */

public class RenderModel {

    private List<STextureRender> sTextureRenderList = new ArrayList<>();
    private List<SurfaceDecoder> surfaceDecoderList = new ArrayList<>();

    private boolean rendering = false;
    private final Object renderLock = new Object();
    private Surface disPlaySurface;

    private SurfaceEncoder surfaceEncoder;

    private FullFrameRect mFullScreenFUDisplay;

    private float[] matrix = new float[16];

    private List<VideoInfo> videoInfoList = new ArrayList<>();

//    private String saveVideoPath;

    private EglCore mEglCoreDecode;
    private OffscreenSurface surfaceDecode;

    private boolean decodeEnd;//一次视频解码结束

    private long presentationTime;

    public void setDisPlaySurface(Surface surface){
        disPlaySurface = surface;
    }

    public void setRenderStarte(boolean state){
        rendering = state;
    }

    private SpeedControl speedControl = new SpeedControl();

    private Semaphore encodeLock = new Semaphore(1);


    public List<VideoInfo> prePare(List<VideoInfo> videoInfoList, String saveVideoPath){
        decodeEnd = false;
        rendering = true;
        this.videoInfoList = videoInfoList;
//        this.saveVideoPath = saveVideoPath;
        List<VideoInfo> acceptList = new ArrayList<>();
        surfaceEncoder = new SurfaceEncoder();
        surfaceEncoder.VideoEncodePrepare(null);

        mEglCoreDecode = new EglCore(null, EglCore.FLAG_RECORDABLE);
        surfaceDecode = new OffscreenSurface(mEglCoreDecode, 900, 900);
        surfaceDecode.makeCurrent();

        for (VideoInfo videoInfo: videoInfoList){
            final STextureRender sTextureRender = new STextureRender();
            sTextureRender.setRectCoor(videoInfo.getShowRectCoor());

            if(videoInfo.getVideoState() == VideoInfo.FILEVIDEO) {
                sTextureRender.surfaceCreated();
                SurfaceDecoder surfaceDecoder = new SurfaceDecoder();
                try {
                    surfaceDecoder.SurfaceDecoderPrePare(sTextureRender.getSurface(), videoInfo.getVideoPath());
                    surfaceDecoder.setDoLoop(true);//debug for 单个视频播放
                } catch (Exception e) {
                    return acceptList;
                }
                surfaceDecoderList.add(surfaceDecoder);

                if(surfaceDecoderList.size() == 2){
                    surfaceDecoder.setFramePresentationListener(framePresentationListener);
                }
            }else {
                sTextureRender.setTextureId(videoInfo.getCameraTextureId());
            }

            sTextureRenderList.add(sTextureRender);
            acceptList.add(videoInfo);
            if(acceptList.size() >= 3) break;
        }
        return acceptList;
    }

    public void realScreenRender(Surface surface){
        renderBefore();

        EglCore mEglCore = new EglCore(mEglCoreDecode.mEGLContext, EglCore.FLAG_RECORDABLE);
        WindowSurface mInputWindowSurface = new WindowSurface(mEglCore, surface, true);
        mInputWindowSurface.makeCurrent();

        while (true) {
            synchronized (renderLock) {
                if (!rendering) {
                    break;
                }
            }

            GLES20.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

            for (STextureRender sTextureRender: sTextureRenderList){
                sTextureRender.getSurfaceTexture().updateTexImage();
                sTextureRender.drawFrame(0, false);
            }

            mInputWindowSurface.swapBuffers();
        }

        mEglCore.release();
        mInputWindowSurface.release();
        renderAfter();
    }


    public void offScreenRender(String savePath){
        Log.e("yang11", "enter offScreenRender: " + Thread.currentThread().getName());

        frameIndex = 0;
        surfaceEncoder.newMediaMuxer(savePath);
        renderBefore();
        for(SurfaceDecoder surfaceDecoder: surfaceDecoderList){//离屏模式下不循环
            surfaceDecoder.setDoLoop(false);
        }

        EglCore mEglCore = new EglCore(mEglCoreDecode.mEGLContext, EglCore.FLAG_RECORDABLE);
        WindowSurface mInputWindowSurface = new WindowSurface(mEglCore, surfaceEncoder.getEncoderSurface(), true);
        mInputWindowSurface.makeCurrent();

        while (true) {
            synchronized (renderLock) {
                if (!rendering || decodeEnd) {
                    break;
                }
            }
//            try {
//                encodeLock.acquire();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
            for (STextureRender sTextureRender: sTextureRenderList){
                if(sTextureRender.getSurfaceTexture() != null) {
                    sTextureRender.getSurfaceTexture().updateTexImage();
                }
                sTextureRender.drawFrame(0, false);
            }

            DecoderEncoderSynCtrl.getInstance().notifyEveryOne();
            surfaceEncoder.drainEncoder(false);


//            mInputWindowSurface.setPresentationTime(computePresentationTimeNsec(frameIndex++));
            mInputWindowSurface.setPresentationTime(presentationTime);
            Log.e("yang12", "presentationtime: " + presentationTime);
            mInputWindowSurface.swapBuffers();
            frameIndex++;
        }


        Log.e("yang10", "frameIndex: " + frameIndex);
        surfaceEncoder.drainEncoder(true);
        surfaceEncoder.release();

        mEglCore.release();
        mInputWindowSurface.release();
        renderAfter();
        DecoderEncoderSynCtrl.getInstance().notifyEveryOne();

        Log.e("yang11", "leave offScreenRender: " + Thread.currentThread().getName());
    }

    int frameIndex = 0;
    int i = 0;
    public void bothRender(Surface surface, String savePath){
        frameIndex = 0;
        surfaceEncoder.newMediaMuxer(savePath);
        renderBefore();
        Matrix.setIdentityM(matrix, 0);
        mFullScreenFUDisplay = new FullFrameRect(new Texture2dProgram(
                Texture2dProgram.ProgramType.TEXTURE_2D));

        int[] framebuffers = new int[1];
        GLES20.glGenFramebuffers(1, framebuffers, 0);
        int mFilterBuffer = framebuffers[0];
        glBindFramebuffer(GL_FRAMEBUFFER, mFilterBuffer);
        int mFilterTexture = getFrameBufferTexture(900, 900);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, mFilterTexture, 0);
        glBindFramebuffer(GL_FRAMEBUFFER, 0);


        EglCore  mEglCore2 = new EglCore(mEglCoreDecode.mEGLContext, EglCore.FLAG_RECORDABLE);
        WindowSurface  mInputWindowSurface2 = new WindowSurface(mEglCore2, surface, true);

        EglCore mEglCore = new EglCore(mEglCoreDecode.mEGLContext, EglCore.FLAG_RECORDABLE);
        WindowSurface mInputWindowSurface = new WindowSurface(mEglCore, surfaceEncoder.getEncoderSurface(), true);

        //同时渲染到离屏surface和物理屏幕
        while (true) {
            synchronized (renderLock) {
                if (!rendering || decodeEnd) {
                    break;
                }
            }
            mInputWindowSurface.makeCurrent();
            GLES20.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
            for (STextureRender sTextureRender: sTextureRenderList){
                sTextureRender.getSurfaceTexture().updateTexImage();
                sTextureRender.drawFrame(mFilterBuffer, false);
            }

            mFullScreenFUDisplay.drawFrame(mFilterTexture, matrix);
            surfaceEncoder.drainEncoder(false);
            mInputWindowSurface.swapBuffers();

            mInputWindowSurface2.makeCurrent();
            mFullScreenFUDisplay.drawFrame(mFilterTexture, matrix);
            mInputWindowSurface2.swapBuffers();
        }
        mInputWindowSurface.makeCurrent();
        surfaceEncoder.drainEncoder(true);
        surfaceEncoder.release();


        //渲染到物理屏幕上
        while (true) {
            synchronized (renderLock) {
                if (!rendering) {
                    break;
                }
            }
            mInputWindowSurface.makeCurrent();//为啥非得再这个makeCurrent下才能画出来，在mInputWindowSurface2.makeCurrent()下，updateTexImage会报错？？？？
            GLES20.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
            for (STextureRender sTextureRender: sTextureRenderList){
                sTextureRender.getSurfaceTexture().updateTexImage();
                sTextureRender.drawFrame(mFilterBuffer, false);
            }
            mInputWindowSurface.swapBuffers();

            mInputWindowSurface2.makeCurrent();
            mFullScreenFUDisplay.drawFrame(mFilterTexture, matrix);
            mInputWindowSurface2.swapBuffers();
        }

        mEglCore.release();
        mInputWindowSurface.release();
        mEglCore2.release();
        mInputWindowSurface2.release();

        GLES20.glDeleteFramebuffers(1, new int[]{mFilterBuffer}, 0);
        GLES20.glDeleteTextures(1, new int[]{mFilterTexture}, 0);
        renderAfter();
    }

    private SurfaceDecoder.DecodeLoopListener decodeLoopListener = new SurfaceDecoder.DecodeLoopListener() {
        @Override
        public void onceLoopEnd() {
            decodeEnd = true;
        }
    };

    private SurfaceDecoder.FramePresentationListener framePresentationListener = new SurfaceDecoder.FramePresentationListener() {
        @Override
        public void onFramePresent(long time) {
            presentationTime = time * 1000;
//            encodeLock.tryAcquire();
//            encodeLock.release();
        }
    };


    private void renderBefore(){
        rendering = true;
        startDecoderThread();
    }

    private void renderAfter(){
        stopDecoderThead();
        mEglCoreDecode.release();
        surfaceDecode.release();
        sTextureRenderList.clear();
        surfaceDecoderList.clear();
    }


    /** 开启解码线程*/
    private void startDecoderThread(){
        for (final SurfaceDecoder surfaceDecoder: surfaceDecoderList){
            new Thread(){
                @Override
                public void run() {
                    super.run();
                    surfaceDecoder.setDecodeLoopListener(decodeLoopListener);
                    surfaceDecoder.doExtract();
                }
            }.start();
        }
    }

    /** 关闭解码线程*/
    private void stopDecoderThead(){
        for (final SurfaceDecoder surfaceDecoder: surfaceDecoderList){
            surfaceDecoder.setStop(true);
        }
    }


    long presentTime = 0;
    private  long computePresentationTimeNsec(int frameIndex) {
        final long ONE_BILLION = 1000000000;
        return frameIndex * ONE_BILLION / 5;
    }

//    public void run() {
//        EglCore mEglCoreDecode = new EglCore(null, EglCore.FLAG_RECORDABLE);
//        OffscreenSurface surfaceDecode = new OffscreenSurface(mEglCoreDecode, 900, 900);
//        surfaceDecode.makeCurrent();
//
//        Matrix.setIdentityM(matrix, 0);
//        mFullScreenFUDisplay = new FullFrameRect(new Texture2dProgram(
//                Texture2dProgram.ProgramType.TEXTURE_2D));
//
//
//        for (VideoInfo videoInfo: videoInfoList){
//            final STextureRender sTextureRender = new STextureRender();
//            sTextureRender.surfaceCreated();
//            sTextureRender.setRectCoor(videoInfo.getShowRectCoor());
//
//            DecoderThread decoderThread = new DecoderThread(sTextureRender.getSurface(), isLoop, videoInfo.getVideoPath());
//            if(decoderThread.init()){
//                decoderThread.start();
//            }else {
//                break;
//            }
//            sTextureRenderList.add(sTextureRender);
//            acceptList.add(videoInfo);
//        }
//        initSemaphore.release();
//
//        try {
//            playSemaphore.acquire();
//            playSemaphore.acquire();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        playSemaphore.release();
//
//
//        if(renderState != REAL_SCREEN) {
//            surfaceEncoder = new SurfaceEncoder();
//            surfaceEncoder.VideoEncodePrepare(saveVideoPath);
//        }
//
//        int[] framebuffers = new int[1];
//        GLES20.glGenFramebuffers(1, framebuffers, 0);
//        int mFilterBuffer = framebuffers[0];
//        glBindFramebuffer(GL_FRAMEBUFFER, mFilterBuffer);
//        int mFilterTexture = getFrameBufferTexture(900, 900);
//        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, mFilterTexture, 0);
//        glBindFramebuffer(GL_FRAMEBUFFER, 0);
//
//
//        EglCore mEglCore2 = null;
//        WindowSurface mInputWindowSurface2 = null;
//        if(renderState != OFF_SCREEN) {
//            mEglCore2 = new EglCore(mEglCoreDecode.mEGLContext, EglCore.FLAG_RECORDABLE);
//            mInputWindowSurface2 = new WindowSurface(mEglCore2, disPlaySurface, true);
//        }
//
//
//        EglCore mEglCore = null;
//        WindowSurface mInputWindowSurface = null;
//        if(renderState != REAL_SCREEN) {
//            mEglCore = new EglCore(mEglCoreDecode.mEGLContext, EglCore.FLAG_RECORDABLE);
//            mInputWindowSurface = new WindowSurface(mEglCore, surfaceEncoder.getEncoderSurface(), true);
//        }
//
//
//        while (true) {
//            synchronized (renderLock) {
//                if (!rendering) {
//                    break;
//                }
//            }
//
//            if(renderState != REAL_SCREEN) {
//                mInputWindowSurface.makeCurrent();
//            }else {
//                mInputWindowSurface2.makeCurrent();
//            }
//            GLES20.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
//            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
//            for (STextureRender sTextureRender: sTextureRenderList){
//                if (!rendering) {
//                    break;
//                }
//                sTextureRender.getSurfaceTexture().updateTexImage();
//                sTextureRender.drawFrame(mFilterBuffer, false);
//            }
//
//            if(renderState != REAL_SCREEN) {
//                mFullScreenFUDisplay.drawFrame(mFilterTexture, matrix);
//                surfaceEncoder.drainEncoder(false);
//                mInputWindowSurface.swapBuffers();
//            }
//
//            if(renderState != OFF_SCREEN) {
//                mInputWindowSurface2.makeCurrent();
//                mFullScreenFUDisplay.drawFrame(mFilterTexture, matrix);
//                mInputWindowSurface2.swapBuffers();
//            }
//        }
//
//        if(renderState != REAL_SCREEN) {
//            mInputWindowSurface.makeCurrent();
//            surfaceEncoder.drainEncoder(true);
//            surfaceEncoder.release();
//        }
//
//        mEglCoreDecode.release();
//        surfaceDecode.release();
//        mEglCore.release();
//        mInputWindowSurface.release();
//
//        if(renderState != OFF_SCREEN) {
//            mEglCore2.release();
//            mInputWindowSurface2.release();
//        }
//
//        GLES20.glDeleteFramebuffers(1, new int[]{mFilterBuffer}, 0);
//        GLES20.glDeleteTextures(1, new int[]{mFilterTexture}, 0);
//
//        if(playEndListener != null){
//            playEndListener.onEnd();
//        }
//    }


    private int getFrameBufferTexture(int width, int height) {
        int texture[] = new int[1];
        glGenTextures(1, texture, 0); // Generate one texture

        glBindTexture(GL_TEXTURE_2D, texture[0]); // Bind the texture
        // fbo_texture
        int[] buf = new int[width * height];
        IntBuffer texBuffer = ByteBuffer.allocateDirect(buf.length * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
        glTexImage2D(GL_TEXTURE_2D, 0, GLES20.GL_RGBA, width, height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, texBuffer); // Create


        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameterf(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);

        // Unbind the texture
        glBindTexture(GL_TEXTURE_2D, 0);
        return texture[0];
    }

    public static class VideoInfo{
        private String VideoPath;
        private float[] showRectCoor;
        private int videoWidth;
        private int videoHeight;

        private int videoState = FILEVIDEO;
        public final static int FILEVIDEO = 0;
        public final static int CAMERAVIDEO = 1;

        private int cameraTextureId;



        public String getVideoPath() {
            return VideoPath;
        }

        public void setVideoPath(String videoPath) {
            VideoPath = videoPath;
        }

        public float[] getShowRectCoor() {
            return showRectCoor;
        }

        public void setShowRectCoor(float[] showRect) {
            this.showRectCoor = showRect;
        }

        public int getVideoWidth() {
            return videoWidth;
        }

        public void setVideoWidth(int videoWidth) {
            this.videoWidth = videoWidth;
        }

        public int getVideoHeight() {
            return videoHeight;
        }

        public void setVideoHeight(int videoHeight) {
            this.videoHeight = videoHeight;
        }

        public void setVideoState(int videoState) {
            this.videoState = videoState;
        }

        public int getCameraTextureId() {
            return cameraTextureId;
        }

        public void setCameraTextureId(int cameraTextureId) {
            this.cameraTextureId = cameraTextureId;
        }

        public int getVideoState() {
            return videoState;
        }
    }
}
