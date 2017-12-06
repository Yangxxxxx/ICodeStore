package com.example.yangjitao.icodestore.Utils;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.example.yangjitao.icodestore.R;


/**
 *  用于设置View的背景【形状：圆角、圆形；drawable设置：state_press】
 */
public class BGFrameLayout extends FrameLayout {
    private Path mCirclePath = new Path();
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    private Paint mLayerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private boolean mIsCircle;
    private int mRoundCorner = -1;
    private Drawable mPressTureDra;
    private Drawable mPressFalseDra;

    public BGFrameLayout(Context context) {
        this(context, null);
    }

    public BGFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BGFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setWillNotDraw(false);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLACK);
        mLayerPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        initAttrs(context, attrs);
    }

    private void initAttrs(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BGFrameLayout);
            mRoundCorner = typedArray.getDimensionPixelSize(R.styleable.BGFrameLayout_bgCorner, -1);
            mIsCircle = typedArray.getBoolean(R.styleable.BGFrameLayout_bgIsCircle, false);
            mPressTureDra = typedArray.getDrawable(R.styleable.BGFrameLayout_bgPressTureDrawable);
            mPressFalseDra = typedArray.getDrawable(R.styleable.BGFrameLayout_bgPressFalseDrawable);
            typedArray.recycle();

            Drawable drawable = genBGDrawable();
            if(drawable != null){
                setBackground(drawable);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        resetCirclePath();
    }

    private void resetCirclePath() {
        float radius = Math.min(getMeasuredWidth(), getMeasuredHeight()) / 2f;
        mCirclePath.reset();
        if (mIsCircle) {
            mCirclePath.addCircle(getMeasuredWidth() / 2f, getMeasuredHeight() / 2f, radius, Path.Direction.CCW);
        } else if(mRoundCorner != -1){
            mCirclePath.addRoundRect(new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()), mRoundCorner, mRoundCorner, Path.Direction.CCW);
        }else {
            mCirclePath.addRect(new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight()), Path.Direction.CCW);
        }

    }


    @Override
    public void draw(Canvas canvas) {
        if(!haveShapeChange()) return;
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        canvas.saveLayer(0, 0, width, height, null, Canvas.ALL_SAVE_FLAG);
        canvas.drawPath(mCirclePath, mPaint);
        canvas.saveLayer(0, 0, width, height, mLayerPaint, Canvas.ALL_SAVE_FLAG);
        super.draw(canvas);
        canvas.restore();
        canvas.restore();
    }

    private boolean haveShapeChange(){
        boolean hasShapeSetting = mIsCircle || mRoundCorner != 1;
        boolean hasContent = getBackground() != null || getChildCount() != 0;
        return hasShapeSetting && hasContent;
    }

    private Drawable genBGDrawable(){
        int drawableCount = 0;
        StateListDrawable drawable = new StateListDrawable();
        if(mPressTureDra != null) {
            drawable.addState(new int[]{android.R.attr.state_pressed}, mPressTureDra);
            drawableCount++;
        }
        if(mPressFalseDra != null) {
            drawable.addState(new int[]{-android.R.attr.state_pressed}, mPressFalseDra);
            drawableCount++;
        }
        if(drawableCount == 0) return null;
        return drawable;
    }

}
