package com.example.touchpad;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

public class MainActivity extends AppCompatActivity{
    private MyClient myClient;
    private final static int ACTION_DOWN = -1;
    private final static int ACTION_CLICK = -2;
    private final static int ACTION_UP = -3;

    private final static int POINTER_ONE = -4;
    private final static int POINTER_TWO = -5;
    private final static int POINTER_THREE = -6;

    private int currPointerCount;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setScreenBrightness(10);
        myClient = new MyClient();
        gestureDetector  = new GestureDetector(this, simpleOnGestureListener);
    }


    float preX;
    float PreY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float offsetX = Math.abs(event.getX() - preX);
        float offsetY = Math.abs(event.getY()-PreY);
        preX = event.getX();
        PreY = event.getY();

        if(offsetX > 1 || offsetY >1) {
            Log.e("yang", "onTouchEvent " + offsetX + " " + offsetY);
        }

        gestureDetector.onTouchEvent(event);
        if(event.getPointerCount() != currPointerCount){
            currPointerCount = event.getPointerCount();
            int pointerValue = POINTER_ONE;
            pointerValue = currPointerCount == 2 ? POINTER_TWO : pointerValue;
            pointerValue = currPointerCount == 3 ? POINTER_THREE : pointerValue;
            myClient.writeInt(pointerValue);
        }

        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                if(offsetX > 1 || offsetY >1) {
                    int coorValue = (int) event.getX() * 10000 + (int) event.getY();
                    myClient.writeInt(coorValue);
                }
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                myClient.writeInt(ACTION_UP);
                currPointerCount = 0;
                break;
        }

        return super.onTouchEvent(event);
    }

    private GestureDetector.SimpleOnGestureListener simpleOnGestureListener = new GestureDetector.SimpleOnGestureListener(){
        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.e("yang", "onSingleTapUp " + e.getSize());
            myClient.writeInt(ACTION_CLICK);
            return super.onSingleTapConfirmed(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//            Log.e("yang", "onScroll " + distanceX + " " + distanceY);
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        //        @Override
//        public boolean onSingleTapConfirmed(MotionEvent e) {
//            Log.e("yang", "onSingleTapConfirmed");
//            return super.onSingleTapConfirmed(e);
//        }

        @Override
        public boolean onDown(MotionEvent e) {
//            Log.e("yang", "onDown ");
            myClient.writeInt(ACTION_DOWN);
            return super.onDown(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.e("yang", "onFling");
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.e("yang", "onDoubleTap");
            myClient.writeInt(ACTION_CLICK);
            return super.onDoubleTap(e);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myClient.close();
    }


    private void setScreenBrightness(int paramInt){
        Window localWindow = getWindow();
        WindowManager.LayoutParams localLayoutParams = localWindow.getAttributes();
        float f = paramInt / 255.0F;
        localLayoutParams.screenBrightness = f;
        localWindow.setAttributes(localLayoutParams);
    }
}
