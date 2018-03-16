package com.example.administrator.sometest.eglTest;

import android.app.Activity;
import android.opengl.GLES20;
import android.opengl.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.administrator.sometest.R;
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

import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES10.glClear;
import static android.opengl.GLES10.glClearColor;
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

public class EGLTestActivity extends Activity implements Callback, View.OnClickListener {
    private EGLThread eglThread = new EGLThread();

//    private STextureRender sTextureRender;
    private List<STextureRender> sTextureRenderList = new ArrayList<>();

//    private SurfaceDecoder surfaceDecoder;
    private List<SurfaceDecoder> surfaceDecoderList = new ArrayList<>();


    private final int VIDEO_NUM = 7;

//    private float[][] coors = new float[][]{
//            new float[]{
//                    -1.0f, -1.0f, 1.0f,   // 0 bottom left
//                    0.0f, -1.0f, 1.0f,   // 1 bottom right
//                    -1.0f, 1.0f, 1.0f,   // 2 top left
//                    0.0f, 1.0f, 1.0f   // 3 top right
//            },
//            new float[]{
//                    0.0f, -1.0f, 1.0f,   // 0 bottom left
//                    1.0f, -1.0f, 1.0f,   // 1 bottom right
//                    0.0f, 1.0f, 1.0f,   // 2 top left
//                    1.0f, 1.0f, 1.0f   // 3 top right
//            }
//    };

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_egltest);
        SurfaceView view1 = (SurfaceView)findViewById(R.id.sv1);
        view1.getHolder().addCallback(this);
        eglThread.setView(view1);

        findViewById(R.id.sv2).setOnClickListener(this);
    }


    @Override
    public void onClick(final View v) {
        int id = v.getId();
        if (id == R.id.sv2) {

            for (final STextureRender sTextureRender : sTextureRenderList) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        try {
                            SurfaceDecoder surfaceDecoder = new SurfaceDecoder();
                            surfaceDecoder.SurfaceDecoderPrePare(sTextureRender.getSurface(), null);
                            surfaceDecoderList.add(surfaceDecoder);
                        }catch (Exception e){
                            Log.e("yang", "SurfaceDecoderPrePare: " + e.toString());
                            e.printStackTrace();
                        }

                    }
                }.start();
            }


            Toast.makeText(this, "onclicked ", Toast.LENGTH_SHORT).show();
            v.setClickable(false);
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        eglThread.setRenderStarte(true);
        eglThread.start();
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        for(SurfaceDecoder surfaceDecoder: surfaceDecoderList){
            surfaceDecoder.setStop(true);
        }
        eglThread.setRenderStarte(false);
    }


    class EGLThread extends Thread implements OnTouchListener{
        private boolean rendering = false;
        private final Object renderLock = new Object();
        private float red = 0.2f, green = 0.3f, blue = 0.8f;
        private SurfaceView view;

        private SurfaceEncoder surfaceEncoder;

        private FullFrameRect mFullScreenFUDisplay;

        private float[] matrix = new float[16];

        public void setView(SurfaceView surfaceView){
            view = surfaceView;
            view.setOnTouchListener(this);
        }

        public void setRenderStarte(boolean state){
            rendering = state;
        }

        public void run() {
            surfaceEncoder = new SurfaceEncoder();
            surfaceEncoder.VideoEncodePrepare(null);

            EglCore mEglCoreDecode = new EglCore(null, EglCore.FLAG_RECORDABLE);
            OffscreenSurface surfaceDecode = new OffscreenSurface(mEglCoreDecode, 900, 900);
            surfaceDecode.makeCurrent();

            Matrix.setIdentityM(matrix, 0);
            mFullScreenFUDisplay = new FullFrameRect(new Texture2dProgram(
                    Texture2dProgram.ProgramType.TEXTURE_2D));

            for (int i = 0; i < VIDEO_NUM; i++){
                STextureRender sTextureRender = new STextureRender();
                sTextureRender.surfaceCreated();
                sTextureRender.setRectCoor(coors[i]);
                sTextureRenderList.add(sTextureRender);
            }

            int[] framebuffers = new int[1];
            GLES20.glGenFramebuffers(1, framebuffers, 0);
            int mFilterBuffer = framebuffers[0];
            glBindFramebuffer(GL_FRAMEBUFFER, mFilterBuffer);
            int mFilterTexture = getFrameBufferTexture(900, 900);
            glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, mFilterTexture, 0);
            glBindFramebuffer(GL_FRAMEBUFFER, 0);


            EglCore mEglCore2 = new EglCore(mEglCoreDecode.mEGLContext, EglCore.FLAG_RECORDABLE);
            WindowSurface mInputWindowSurface2 = new WindowSurface(mEglCore2, view.getHolder().getSurface(), true);
//            mInputWindowSurface2.makeCurrent();


            EglCore mEglCore = new EglCore(mEglCoreDecode.mEGLContext, EglCore.FLAG_RECORDABLE);
            WindowSurface mInputWindowSurface = new WindowSurface(mEglCore, surfaceEncoder.getEncoderSurface(), true);
//            mInputWindowSurface.makeCurrent();



            while (true) {
                synchronized (renderLock) {
                    if (!rendering) {
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

            mEglCoreDecode.release();
            surfaceDecode.release();
            mEglCore.release();
            mInputWindowSurface.release();
            mEglCore2.release();
            mInputWindowSurface2.release();
            GLES20.glDeleteFramebuffers(1, new int[]{mFilterBuffer}, 0);
            GLES20.glDeleteTextures(1, new int[]{mFilterTexture}, 0);
        }

        private void render() {
            glClearColor(red, green, blue, 1.0f);
            glClear(GL10.GL_COLOR_BUFFER_BIT);
        }

        public boolean onTouch(View view, MotionEvent e) {
            red = e.getX() / view.getWidth();
            green = e.getY() / view.getHeight();
            blue = 1.0f;
            return true;
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

}