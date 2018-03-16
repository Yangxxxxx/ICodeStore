package com.example.administrator.sometest.eglTest;

import android.opengl.GLES20;
import android.opengl.Matrix;
import android.util.Log;
import android.view.Surface;

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
 * Created by Administrator on 2017/9/26 0026.
 */

public class MultiChannelVideoPLayer {
    private static final MultiChannelVideoPLayer ourInstance = new MultiChannelVideoPLayer();

    public static MultiChannelVideoPLayer getInstance() {
        return ourInstance;
    }

    private MultiChannelVideoPLayer() {
    }
    /***********************************************************************************************/
    private PlayThread playThread;

    private List<STextureRender> sTextureRenderList = new ArrayList<>();

    private List<SurfaceDecoder> surfaceDecoderList = new ArrayList<>();

    private boolean isLoop;

    public static final int OFF_SCREEN = 0;//离屏渲染（用于保存显示的视频到文件）
    public static final int REAL_SCREEN = 1;//显示到物理屏幕
    public static final int BOTH_SCREEN = 2;//显示的时候，保存成视频文件
    private int renderState = REAL_SCREEN;

    public void setRenderState(int state){
        renderState = state;
    }


    public List<VideoInfo> init(List<VideoInfo> videoInfoList, String saveVideoPath){
        sTextureRenderList.clear();
        surfaceDecoderList.clear();

        playThread = new PlayThread(videoInfoList, saveVideoPath);
        playThread.start();
        playThread.waitInit();
        return playThread.getAcceptVideo();
    }

    public void play(Surface surface){
        playThread.setRenderStarte(true);
        playThread.startPlay(surface);
    }

    public void playOffScreen(PlayEndListener playEndListener){
        setRenderState(OFF_SCREEN);
        setLoop(false);
        playThread.setPlayEndListener(playEndListener);
        play(null);
    }

    public void stop(){
        playThread.setRenderStarte(false);
        for (SurfaceDecoder surfaceDecoder: surfaceDecoderList){
            surfaceDecoder.setStop(true);
        }
    }

    public void setLoop(boolean isLoop){
        this.isLoop = isLoop;

        for(SurfaceDecoder surfaceDecoder: surfaceDecoderList){
            surfaceDecoder.setDoLoop(isLoop);
        }
    }


    class PlayThread extends Thread{
        private boolean rendering = false;
        private final Object renderLock = new Object();
        private Surface disPlaySurface;

        private SurfaceEncoder surfaceEncoder;

        private FullFrameRect mFullScreenFUDisplay;

        private float[] matrix = new float[16];

        private Semaphore playSemaphore = new Semaphore(1);

        private Semaphore initSemaphore = new Semaphore(1);

        private List<VideoInfo> videoInfoList = new ArrayList<>();
        private List<VideoInfo> acceptList = new ArrayList<>();

        private String saveVideoPath;

        private PlayEndListener playEndListener;

        public PlayThread(List<VideoInfo> videoInfoList, String saveVideoPath){
            this.videoInfoList = videoInfoList;
            this.saveVideoPath = saveVideoPath;
        }

        public void setDisPlaySurface(Surface surface){
            disPlaySurface = surface;
        }

        public void setPlayEndListener(PlayEndListener playEndListener){
            this.playEndListener = playEndListener;
        }

        public void setRenderStarte(boolean state){
            rendering = state;
        }

        public void startPlay(Surface surface){
            playSemaphore.tryAcquire();
            playSemaphore.release();
            disPlaySurface = surface;
        }

        public List<VideoInfo> getAcceptVideo(){
            return  acceptList;
        }

        /** 等待初始化完成*/
        public void waitInit(){
            try {
                initSemaphore.acquire();
                initSemaphore.acquire();
                initSemaphore.release();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            EglCore mEglCoreDecode = new EglCore(null, EglCore.FLAG_RECORDABLE);
            OffscreenSurface surfaceDecode = new OffscreenSurface(mEglCoreDecode, 900, 900);
            surfaceDecode.makeCurrent();

            Matrix.setIdentityM(matrix, 0);
            mFullScreenFUDisplay = new FullFrameRect(new Texture2dProgram(
                    Texture2dProgram.ProgramType.TEXTURE_2D));


            for (VideoInfo videoInfo: videoInfoList){
                final STextureRender sTextureRender = new STextureRender();
                sTextureRender.surfaceCreated();
                sTextureRender.setRectCoor(videoInfo.getShowRectCoor());

                DecoderThread decoderThread = new DecoderThread(sTextureRender.getSurface(), isLoop, videoInfo.getVideoPath());
                if(decoderThread.init()){
                    decoderThread.start();
                }else {
                    break;
                }
                sTextureRenderList.add(sTextureRender);
                acceptList.add(videoInfo);
            }
            initSemaphore.release();

            try {
                playSemaphore.acquire();
                playSemaphore.acquire();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            playSemaphore.release();


            if(renderState != REAL_SCREEN) {
                surfaceEncoder = new SurfaceEncoder();
                surfaceEncoder.VideoEncodePrepare(saveVideoPath);
            }

            int[] framebuffers = new int[1];
            GLES20.glGenFramebuffers(1, framebuffers, 0);
            int mFilterBuffer = framebuffers[0];
            glBindFramebuffer(GL_FRAMEBUFFER, mFilterBuffer);
            int mFilterTexture = getFrameBufferTexture(900, 900);
            glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, mFilterTexture, 0);
            glBindFramebuffer(GL_FRAMEBUFFER, 0);


            EglCore mEglCore2 = null;
            WindowSurface mInputWindowSurface2 = null;
            if(renderState != OFF_SCREEN) {
                mEglCore2 = new EglCore(mEglCoreDecode.mEGLContext, EglCore.FLAG_RECORDABLE);
                mInputWindowSurface2 = new WindowSurface(mEglCore2, disPlaySurface, true);
            }


            EglCore mEglCore = null;
            WindowSurface mInputWindowSurface = null;
            if(renderState != REAL_SCREEN) {
                mEglCore = new EglCore(mEglCoreDecode.mEGLContext, EglCore.FLAG_RECORDABLE);
                mInputWindowSurface = new WindowSurface(mEglCore, surfaceEncoder.getEncoderSurface(), true);
            }


            while (true) {
                synchronized (renderLock) {
                    if (!rendering) {
                        break;
                    }
                }

                if(renderState != REAL_SCREEN) {
                    mInputWindowSurface.makeCurrent();
                }else {
                    mInputWindowSurface2.makeCurrent();
                }
                GLES20.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
                GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
                for (STextureRender sTextureRender: sTextureRenderList){
                    if (!rendering) {
                        break;
                    }
                    sTextureRender.getSurfaceTexture().updateTexImage();
                    sTextureRender.drawFrame(mFilterBuffer, false);
                }

                if(renderState != REAL_SCREEN) {
                    mFullScreenFUDisplay.drawFrame(mFilterTexture, matrix);
                    surfaceEncoder.drainEncoder(false);
                    mInputWindowSurface.swapBuffers();
                }

                if(renderState != OFF_SCREEN) {
                    mInputWindowSurface2.makeCurrent();
                    mFullScreenFUDisplay.drawFrame(mFilterTexture, matrix);
                    mInputWindowSurface2.swapBuffers();
                }
            }

            if(renderState != REAL_SCREEN) {
                mInputWindowSurface.makeCurrent();
                surfaceEncoder.drainEncoder(true);
                surfaceEncoder.release();
            }

            mEglCoreDecode.release();
            surfaceDecode.release();
            mEglCore.release();
            mInputWindowSurface.release();

            if(renderState != OFF_SCREEN) {
                mEglCore2.release();
                mInputWindowSurface2.release();
            }

            GLES20.glDeleteFramebuffers(1, new int[]{mFilterBuffer}, 0);
            GLES20.glDeleteTextures(1, new int[]{mFilterTexture}, 0);

            if(playEndListener != null){
                playEndListener.onEnd();
            }
        }


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
    }

    /** 视频解码线程*/
    class DecoderThread extends Thread{
        private Surface surface;
        private SurfaceDecoder surfaceDecoder;
        boolean isLoop;
        private String filePath;

        public DecoderThread(Surface surface, boolean isLoop, String filePath){
            this.surface = surface;
            this.isLoop = isLoop;
            this.filePath = filePath;
        }


        public boolean init(){
            try {
                surfaceDecoder = new SurfaceDecoder();
                surfaceDecoder.SurfaceDecoderPrePare(surface, filePath);
                surfaceDecoderList.add(surfaceDecoder);
                surfaceDecoder.setDoLoop(isLoop);
                return true;
            }catch (Exception e){
                Log.e("yang", "SurfaceDecoderPrePare: " + e.toString());
                e.printStackTrace();
                return false;
            }
        }

        public void run() {
            super.run();
            surfaceDecoder.doExtract();
        }
    }


    public static class VideoInfo{
        private String VideoPath;
        private float[] showRectCoor;
        private int videoWidth;
        private int videoHeight;

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
    }

    public interface PlayEndListener{
        void onEnd();
    }

}
