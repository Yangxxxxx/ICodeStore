package com.example.administrator.sometest.eglTest;

import android.app.Activity;
import android.graphics.SurfaceTexture;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLExt;
import android.opengl.EGLSurface;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.administrator.sometest.R;

import javax.microedition.khronos.opengles.GL10;

import static android.opengl.GLES10.glClear;
import static android.opengl.GLES10.glClearColor;

public class EGLTestActivity_bak extends Activity implements Callback, TextureView.SurfaceTextureListener, View.OnClickListener {


    private EGLThread eglThread = new EGLThread();

    private EGLThread eglThread2 = new EGLThread();

    private STextureRender sTextureRender = new STextureRender();

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
        if(id == R.id.sv2){

            new Thread(){
                @Override
                public void run() {
                    super.run();
//                    SurfaceTexture st = new SurfaceTexture(sTextureRender.getTextureId());
//                    Surface surface = new Surface(st);
                    new SurfaceDecoder().SurfaceDecoderPrePare(sTextureRender.getSurface(), null);

                }
            }.start();

            Toast.makeText(this, "onclicked ", Toast.LENGTH_SHORT).show();
            v.setClickable(false);
        }
    }

    public void surfaceCreated(SurfaceHolder holder) {
        eglThread.setRenderStarte(true);
        eglThread.start();

//        eglThread2.setRenderStarte(true);
//        eglThread2.start();


    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        eglThread.setRenderStarte(false);

//        eglThread2.setRenderStarte(false);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }


    class EGLThread extends Thread implements OnTouchListener{
        private boolean rendering = false;
        private final Object renderLock = new Object();
        private GL10 gl;
        private float red = 0.2f, green = 0.3f, blue = 0.8f;
        private SurfaceView view;

        public void setView(SurfaceView surfaceView){
            view = surfaceView;
            view.setOnTouchListener(this);
        }

        public void setRenderStarte(boolean state){
            rendering = state;
        }

        public void run() {
            EGLDisplay mEGLDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
            if (mEGLDisplay == EGL14.EGL_NO_DISPLAY) {
                throw new RuntimeException("unable to get EGL14 display");
            }
            int[] version = new int[2];
            if (!EGL14.eglInitialize(mEGLDisplay, version, 0, version, 1)) {
                mEGLDisplay = null;
                throw new RuntimeException("unable to initialize EGL14");
            }

            // Configure EGL for pbuffer and OpenGL ES 2.0, 24-bit RGB.
            int[] attribList = {
                    EGL14.EGL_RED_SIZE, 8,
                    EGL14.EGL_GREEN_SIZE, 8,
                    EGL14.EGL_BLUE_SIZE, 8,
                    EGL14.EGL_ALPHA_SIZE, 8,
                    EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
                    EGL14.EGL_SURFACE_TYPE, EGL14.EGL_PBUFFER_BIT,
                    EGL14.EGL_NONE
            };
            EGLConfig[] configs = new EGLConfig[1];
            int[] numConfigs = new int[1];
            if (!EGL14.eglChooseConfig(mEGLDisplay, attribList, 0, configs, 0, configs.length,
                    numConfigs, 0)) {
                throw new RuntimeException("unable to find RGB888+recordable ES2 EGL config");
            }

            EGLConfig configEncoder = getConfig(mEGLDisplay, 2);

            // Configure context for OpenGL ES 2.0.
            int[] attrib_list = {
                    EGL14.EGL_CONTEXT_CLIENT_VERSION, 2,
                    EGL14.EGL_NONE
            };

            EGLContext mEGLContextDecode = EGL14.eglCreateContext(mEGLDisplay, configs[0], EGL14.EGL_NO_CONTEXT,
                    attrib_list, 0);

            EGLContext mEGLContextDisplay = EGL14.eglCreateContext(mEGLDisplay, configs[0], mEGLContextDecode,
                    attrib_list, 0);
            checkEglError("eglCreateContext");
            if (mEGLContextDisplay == null) {
                throw new RuntimeException("null context");
            }

            // Create a pbuffer surface.
            int[] surfaceAttribs = {
                    EGL14.EGL_WIDTH, 1920,
                    EGL14.EGL_HEIGHT, 1080,
                    EGL14.EGL_NONE
            };

            int[] surfaceAttribs2 = {
                    EGL14.EGL_NONE
            };
            EGLSurface mEGLSurfaceDisplay = EGL14.eglCreateWindowSurface(mEGLDisplay, configEncoder, view.getHolder(),
                    surfaceAttribs2, 0);
            checkEglError("eglCreatePbufferSurface");
            if (mEGLSurfaceDisplay == null) {
                throw new RuntimeException("surface was null");
            }

            EGLSurface mEGLSurfaceDecode = EGL14.eglCreatePbufferSurface(mEGLDisplay, configs[0], surfaceAttribs, 0);
            if (!EGL14.eglMakeCurrent(mEGLDisplay, mEGLSurfaceDecode, mEGLSurfaceDecode, mEGLContextDecode)) {
                throw new RuntimeException("eglMakeCurrent failed");
            }

            sTextureRender.surfaceCreated();


            if (!EGL14.eglMakeCurrent(mEGLDisplay, mEGLSurfaceDisplay, mEGLSurfaceDisplay, mEGLContextDisplay)) {
                throw new RuntimeException("eglMakeCurrent failed");
            }


            while (true) {
                synchronized (renderLock) {
                    if (!rendering) {
                        break;
                    }
                }
//                render(null);
                sTextureRender.drawFrame(0, false);
                EGL14.eglSwapBuffers(mEGLDisplay, mEGLSurfaceDisplay);
            }
            Log.d("OpenGlDemo >>>", "Stop rendering");// Finalize
            EGL14.eglMakeCurrent(mEGLDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_CONTEXT);
            EGL14.eglDestroyContext(mEGLDisplay, mEGLContextDisplay);
            EGL14.eglDestroySurface(mEGLDisplay, mEGLSurfaceDisplay);
            gl = null;
        }

        private void render(GL10 gl) {
            glClearColor(red, green, blue, 1.0f);
            glClear(GL10.GL_COLOR_BUFFER_BIT);
        }

        public boolean onTouch(View view, MotionEvent e) {
            red = e.getX() / view.getWidth();
            green = e.getY() / view.getHeight();
            blue = 1.0f;
            return true;
        }
    }

    private EGLConfig getConfig(EGLDisplay mEGLDisplay, int version) {
        int renderableType = EGL14.EGL_OPENGL_ES2_BIT;
        if (version >= 3) {
            renderableType |= EGLExt.EGL_OPENGL_ES3_BIT_KHR;
        }

        // The actual surface is generally RGBA or RGBX, so situationally omitting alpha
        // doesn't really help.  It can also lead to a huge performance hit on glReadPixels()
        // when reading into a GL_RGBA buffer.
        int[] attribList = {
                EGL14.EGL_RED_SIZE, 8,
                EGL14.EGL_GREEN_SIZE, 8,
                EGL14.EGL_BLUE_SIZE, 8,
                EGL14.EGL_ALPHA_SIZE, 8,
                EGL14.EGL_RENDERABLE_TYPE, renderableType,
                EGL14.EGL_NONE, 0,      // placeholder for recordable [@-3]
                EGL14.EGL_NONE
        };

        EGLConfig[] configs = new EGLConfig[1];
        int[] numConfigs = new int[1];
        if (!EGL14.eglChooseConfig(mEGLDisplay, attribList, 0, configs, 0, configs.length,
                numConfigs, 0)) {
            return null;
        }
        return configs[0];
    }


    private void checkEglError(String msg) {
        int error;
        if ((error = EGL14.eglGetError()) != EGL14.EGL_SUCCESS) {
            throw new RuntimeException(msg + ": EGL error: 0x" + Integer.toHexString(error));
        }
    }

    private static final String VERTEX_SHADER = "attribute vec4 vPosition;\n"
            + "attribute vec2 a_texCoord;\n"
            + "varying vec2 tc;\n"
            + "void main() {\n"
            + "gl_Position = vPosition;\n"
            + "tc = a_texCoord;\n"
            + "}\n";

    private static final String FRAGMENT_SHADER = "precision mediump float;\n"
            + "uniform sampler2D tex_y;\n"
            + "uniform sampler2D tex_u;\n"
            + "uniform sampler2D tex_v;\n"
            + "varying vec2 tc;\n"
            + "void main() {\n"
            + "vec4 c = vec4((texture2D(tex_y, tc).r - 16./255.) * 1.164);\n"
            + "vec4 U = vec4(texture2D(tex_u, tc).r - 128./255.);\n"
            + "vec4 V = vec4(texture2D(tex_v, tc).r - 128./255.);\n"
            + "c += V * vec4(1.596, -0.813, 0, 0);\n"
            + "c += U * vec4(0, -0.392, 2.017, 0);\n"
            + "c.a = 1.0;" + "\n"
            + "gl_FragColor = c;\n"
            + "}\n";

}