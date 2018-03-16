package com.example.administrator.sometest.eglTest;

import android.opengl.GLES20;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.SurfaceView;

import com.example.administrator.sometest.R;
import com.example.administrator.sometest.eglTest.gles.EglCore;
import com.example.administrator.sometest.eglTest.gles.WindowSurface;

import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES20.glClear;
import static android.opengl.GLES20.glClearColor;

public class EGLTestSimpleActivity extends AppCompatActivity {
    SurfaceView surfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_egltest_simple);
        surfaceView  = (SurfaceView)findViewById(R.id.sv);

        surfaceView.post(new Runnable() {

            @Override
            public void run() {
                EglCore mEglCore = new EglCore(null, EglCore.FLAG_RECORDABLE);
                checkGlError("0");
                final WindowSurface mInputWindowSurface = new WindowSurface(mEglCore, surfaceView.getHolder().getSurface(), true);
                checkGlError("0.5");

                new Thread(){
                    @Override
                    public void run() {
                        super.run();
//                        EglCore mEglCore = new EglCore(null, EglCore.FLAG_RECORDABLE);
//                        WindowSurface mInputWindowSurface = new WindowSurface(mEglCore, surfaceView.getHolder().getSurface(), true);
//                        mInputWindowSurface.makeCurrent();

                        mInputWindowSurface.makeCurrent();

                        glClearColor(200, 0, 0, 1.0f);
                        checkGlError("1");
                        glClear(GL10.GL_COLOR_BUFFER_BIT);

                        mInputWindowSurface.swapBuffers();
                    }
                }.start();
            }
        });
    }


    public static void checkGlError(String op) {
        int error = GLES20.glGetError();
        if (error != GLES20.GL_NO_ERROR) {
            String msg = op + ": glError 0x" + Integer.toHexString(error);
            Log.e("yang", msg);
            throw new RuntimeException(msg);
        }
    }


}
