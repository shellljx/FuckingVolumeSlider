package com.licrafter.library;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by lijx on 2017/6/13.
 */

public class VolumeSliderView extends View {

    private Paint mSpeakerPaint;
    private Paint mSliderPaint;
    private Paint mRipplePaint;
    private Path mSpeakerPath;
    private Path mDrawPath;
    private Speaker mSpeaker;
    private RectF mSpeakerRect;
    private Matrix mRotationMatrix;

    private OnSliderTouchListener mListener;

    private float mWidth;
    private float mHeight;
    private float mRippleRadius;
    private float mDegree;
    private float mSpace = 20;
    private float mBallRadius;
    private float mRippleMaxRadius;
    private float mSweepDegree = 45;
    private boolean mShowRipple;


    public VolumeSliderView(Context context) {
        this(context, null);
    }

    public VolumeSliderView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VolumeSliderView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void init() {

        mSpeakerPaint = new Paint();
        mSpeakerPaint.setStyle(Paint.Style.FILL);
        mSpeakerPaint.setAntiAlias(true);
        mSpeakerPaint.setColor(Color.parseColor("#cdcdcd"));

        mRipplePaint = new Paint();
        mRipplePaint.setColor(Color.parseColor("#82529d"));
        mRipplePaint.setAntiAlias(true);

        mSliderPaint = new Paint();
        mSliderPaint.setColor(Color.parseColor("#CAC8CE"));
        mSliderPaint.setStrokeCap(Paint.Cap.ROUND);
        mSliderPaint.setStyle(Paint.Style.STROKE);
        mSliderPaint.setAntiAlias(true);

        mSpeakerPath = new Path();
        CornerPathEffect mPathEffect = new CornerPathEffect(15);
        mSpeakerPaint.setPathEffect(mPathEffect);
        mRipplePaint.setPathEffect(mPathEffect);

        mRotationMatrix = new Matrix();
        mDrawPath = new Path();
        mSpeakerRect = new RectF();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mHeight = mWidth / 2;

        mSpeaker = Speaker.newInstance(Math.round((mWidth - mSpace) / 5), Math.round(mHeight));
        mSpeakerRect.set(mSpeaker.getGap(), mHeight - mSpeaker.getHeight(), mSpeaker.getGap() + mSpeaker.getWidth(), mHeight);
        mRippleMaxRadius = mSpeaker.getWidth() * 1.05f;
        setMeasuredDimension(Math.round(mWidth), Math.round(mHeight));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mSliderPaint.setStrokeWidth(mSpeaker.getGap() * 0.4f);
        canvas.drawLine(mSpeaker.getGap() + mSpeaker.getWidth() + 100, mSpeaker.getRotationY()
                , mWidth - 50, mSpeaker.getRotationY(), mSliderPaint);

        mRotationMatrix.reset();
        mDrawPath.reset();
        mSpeaker.fillPath(mSpeakerPath);
        mRotationMatrix.preRotate(mDegree, mSpeaker.getRotationX(), mSpeaker.getRotationY());
        mDrawPath.addPath(mSpeakerPath, mRotationMatrix);
        canvas.drawPath(mDrawPath, mSpeakerPaint);

        canvas.clipPath(mDrawPath);
        if (mShowRipple){
            canvas.drawCircle(mSpeaker.getRotationX(), mSpeaker.getRotationY(), mRippleRadius, mRipplePaint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (check(event.getX(), event.getY())) {
                    mShowRipple = true;
                    mListener.onPressDown();
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (check(event.getX(), event.getY())) {
                    mShowRipple = false;
                    mListener.onUp();
                }
                break;
        }
        return super.onTouchEvent(event);
    }


    public void setRippleRadius(float radius) {
        mRippleRadius = radius;
    }

    public float getRippleRadius() {
        return mRippleRadius;
    }

    public void setDegree(float degree) {

        mDegree = degree;
        mRippleRadius = mRippleMaxRadius * Math.abs(mDegree / mSweepDegree);
        invalidate();
    }

    public void setSweepDegree(float sweepDegree) {
        mSweepDegree = sweepDegree;
    }

    public float getDegree() {
        return mDegree;
    }

    public interface OnSliderTouchListener {
        void onPressDown();

        void onUp();
    }

    public void setOnSliderTouchListener(OnSliderTouchListener listener) {
        mListener = listener;
    }

    public boolean check(float x, float y) {
        if (mSpeakerRect.contains(x, y) && mListener != null) {
            return true;
        }
        return false;
    }

    public static final Property<VolumeSliderView, Float> DEGREE = new Property<VolumeSliderView, Float>(Float.class, "degree") {
        @Override
        public Float get(VolumeSliderView object) {
            return object.getDegree();
        }

        @Override
        public void set(VolumeSliderView object, Float value) {
            object.setDegree(value);
        }
    };
}
