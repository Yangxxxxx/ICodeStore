package com.gxz.example.videoedit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class VideoSeekBar extends View {
    private static final String TAG = VideoSeekBar.class.getSimpleName();
    private static final int TRANLUCENT_COVER_COLOR = 0x66000000;
    private static final int LINE_COLOR = 0xffffffff;
    private static final int EXPANDED_CLICK_AREA_WIDTH = 60;
    private Bitmap thumbImageLeft;
    private Bitmap thumbImageRight;
    private Paint paint;

    private double minDistanceRatio; //两个把手之间的最小距离比
    private int lineWidth;
    private boolean isSizeInfoAvailable;
    private float preActionX;
    private RectF leftHandleRect = new RectF();
    private RectF rightHandleRect = new RectF();
    private RectF pressedHandleRect;

    private OnRangeSeekBarChangeListener listener;

    public VideoSeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VideoSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public VideoSeekBar(Context context) {
        super(context);
        post(new Runnable() {
            @Override
            public void run() {
                init();
                isSizeInfoAvailable = true;
            }
        });
    }


    public interface OnRangeSeekBarChangeListener {
        void onRangeSeekBarValuesChanged(int action, boolean isLeftMoving);
    }


    public void setMinDistanceRatio(double ratio){
        minDistanceRatio = ratio;
    }

    public float getLeftOffset(){
        return leftHandleRect.left;
    }

    public float getRightOffset(){
        return rightHandleRect.right;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(!isSizeInfoAvailable) return;

        //画两侧点击区域
        canvas.drawBitmap(thumbImageLeft, null, leftHandleRect, paint);
        canvas.drawBitmap(thumbImageRight, null, rightHandleRect, paint);

        //画两侧透明层
        paint.setColor(TRANLUCENT_COVER_COLOR);
        canvas.drawRect(0, 0, leftHandleRect.left, getHeight(), paint);
        canvas.drawRect(rightHandleRect.right, 0, getWidth(), getHeight(), paint);

        //画上下侧线条
        paint.setColor(LINE_COLOR);
        canvas.drawLine(leftHandleRect.right, lineWidth/2, rightHandleRect.left, lineWidth/2, paint);
        canvas.drawLine(leftHandleRect.right, getHeight() - lineWidth/2, rightHandleRect.left, getHeight() - lineWidth/2, paint);

        //测试点击区域
//        paint.setColor(Color.parseColor("#88ff0000"));
//        canvas.drawRect(genClickArea(leftHandleRect), paint);
//        canvas.drawRect(genClickArea(rightHandleRect), paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        switch (action & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                if(pressRectArea(leftHandleRect, event)){
                    pressedHandleRect = leftHandleRect;
                }else if(pressRectArea(rightHandleRect, event)){
                    pressedHandleRect = rightHandleRect;
                }
                preActionX = event.getX();

                if (listener != null && pressedHandleRect != null) {
                    listener.onRangeSeekBarValuesChanged(MotionEvent.ACTION_DOWN, isLeftHandleMoving());
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(pressedHandleRect != null){
                    offsetRectX(pressedHandleRect, event.getX() - preActionX);
                    preActionX = event.getX();
                    if (listener != null) {
                        listener.onRangeSeekBarValuesChanged(MotionEvent.ACTION_MOVE, isLeftHandleMoving());
                    }
                    invalidate();
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (listener != null && pressedHandleRect != null) {
                    listener.onRangeSeekBarValuesChanged(MotionEvent.ACTION_UP, isLeftHandleMoving());
                }
                invalidate();
                pressedHandleRect = null;
                break;

            default:
                break;
        }
        return pressedHandleRect != null;
    }


    private void init() {
        //等比例缩放图片
        thumbImageLeft = BitmapFactory.decodeResource(getResources(), R.drawable.handle_left);
        int width = thumbImageLeft.getWidth();
        int height = thumbImageLeft.getHeight();
        int newWidth = dip2px(11);
        int newHeight = dip2px(55);
        float scaleWidth = newWidth * 1.0f / width;
        float scaleHeight = newHeight * 1.0f / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        thumbImageLeft = Bitmap.createBitmap(thumbImageLeft, 0, 0, width, height, matrix, true);
        thumbImageRight = thumbImageLeft;

        lineWidth = UIUtil.dip2px(getContext(), 2);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.parseColor("#ffffff"));
        paint.setStrokeWidth(lineWidth);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        final int handleWidth = 30;
        leftHandleRect.set(0, 0, handleWidth, getHeight());
        rightHandleRect.set(getWidth() - handleWidth, 0, getWidth(), getHeight());
    }

    private boolean isLeftHandleMoving(){
        return pressedHandleRect != null && pressedHandleRect == leftHandleRect;
    }

    private int dip2px(int dip) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) ((float) dip * scale + 0.5F);
    }

    private boolean pressRectArea(RectF rectF, MotionEvent event){
        return genClickArea(rectF).contains(event.getX(), event.getY());
    }

    private void offsetRectX(RectF rectF, float x){//范围再0 - getwidth() 之间
        float offsetX = x;

        //预防超出左右边界
        if(rectF.left + x < 0){
            offsetX = -rectF.left;
        }else if(rectF.right + x > getWidth()){
            offsetX = getWidth() - rectF.right;
        }

        //控制最小宽度
        float minDistance = (float) (minDistanceRatio * getWidth());
        if(x > 0 && pressedHandleRect == leftHandleRect){
            if(rightHandleRect.right - leftHandleRect.left - x < minDistance){
                offsetX = rightHandleRect.right - leftHandleRect.left - minDistance;
            }
        }else if(x < 0 && pressedHandleRect == rightHandleRect){
            if(rightHandleRect.right + x - leftHandleRect.left < minDistance){
                offsetX = minDistance - rightHandleRect.right + leftHandleRect.left;
            }
        }

        rectF.offset(offsetX, 0);
    }

    private RectF genClickArea(RectF org){
        RectF clickArea = new RectF(org); //扩大范围，便于点击
        clickArea.left -= EXPANDED_CLICK_AREA_WIDTH;
        clickArea.right += EXPANDED_CLICK_AREA_WIDTH;

        if(clickArea.left < 0) {
            clickArea.right -= clickArea.left;
            clickArea.left = 0;
        }
        if(clickArea.right > getWidth()){
            clickArea.left -= clickArea.right - getWidth();
            clickArea.right = getWidth();
        }
        return clickArea;
    }

    public void setOnRangeSeekBarChangeListener(OnRangeSeekBarChangeListener listener) {
        this.listener = listener;
    }
}
