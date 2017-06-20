package com.licrafter.library;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Property;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by lijx on 2017/6/13.
 */

public class VolumeSliderView extends View {

    private Paint mSpeakerPaint;
    private Paint mSliderPaint;
    private Paint mRipplePaint;
    private Paint mBallPaint;
    private Path mSpeakerPath;
    private Path mDrawPath;
    private Speaker mSpeaker;
    private RectF mSpeakerRect;
    private Matrix mRotationMatrix;
    private Point mBallCenter;
    private Point mBallStart;
    private Point mBallEnd;
    private Point mBallControll;

    private OnVolumeSlideListener mListener;
    private ObjectAnimator mPressAnimator;
    private AnimatorSet mUpAnimator;
    private ObjectAnimator mDegreeAnimator;
    private ObjectAnimator mBallAnimator;

    private float mWidth;
    private float mRippleRadius;
    private float mDegree;
    private float mSpace = 80;
    private float mBallRadius = 20;
    private float mRippleMaxRadius;
    private boolean mDrawRipple;
    private boolean mHasup;


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

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //turn off hardwareAccelerated
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

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

        mBallPaint = new Paint();
        mBallPaint.setColor(Color.parseColor("#82529d"));
        mBallPaint.setAntiAlias(true);

        mSpeakerPath = new Path();
        CornerPathEffect mPathEffect = new CornerPathEffect(15);
        mSpeakerPaint.setPathEffect(mPathEffect);
        mRipplePaint.setPathEffect(mPathEffect);

        mRotationMatrix = new Matrix();
        mDrawPath = new Path();
        mSpeakerRect = new RectF();
        mBallCenter = new Point();
        mBallStart = new Point();
        mBallEnd = new Point();
        mBallControll = new Point();

        initAnimator();
    }

    private void initAnimator() {
        mPressAnimator = ObjectAnimator.ofFloat(this, DEGREE, 0, -45);
        mPressAnimator.setDuration(1000);

        mUpAnimator = new AnimatorSet();
        mDegreeAnimator = new ObjectAnimator();
        mDegreeAnimator.setTarget(this);
        mDegreeAnimator.setProperty(DEGREE);
        mBallAnimator = new ObjectAnimator();
        mBallAnimator.setTarget(this);
        mBallAnimator.setProperty(BALLLOCATION);
        mUpAnimator.playTogether(mDegreeAnimator, mBallAnimator);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        float mHeight = mWidth / 2;

        mSpeaker = Speaker.newInstance(Math.round((mWidth - mSpace) / 5), Math.round(mHeight));
        mSpeakerRect.set(mSpeaker.getGap(), mHeight - mSpeaker.getHeight(), mSpeaker.getGap() + mSpeaker.getWidth(), mHeight);
        mRippleMaxRadius = mSpeaker.getWidth();
        setMeasuredDimension(Math.round(mWidth), Math.round(mHeight));

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mSliderPaint.setStrokeWidth(mSpeaker.getGap() * 0.4f);
        canvas.drawLine(getSliderLeftX(), mSpeaker.getRotationY()
                , mWidth - mBallRadius, mSpeaker.getRotationY(), mSliderPaint);

        mRotationMatrix.reset();
        mDrawPath.reset();
        mSpeaker.fillPath(mSpeakerPath);
        mRotationMatrix.preRotate(mDegree, mSpeaker.getRotationX(), mSpeaker.getRotationY());
        mDrawPath.addPath(mSpeakerPath, mRotationMatrix);
        canvas.drawPath(mDrawPath, mSpeakerPaint);
        if (mDrawRipple) {
            canvas.clipPath(mDrawPath);
            canvas.drawCircle(mSpeaker.getRotationX(), mSpeaker.getRotationY(), mRippleRadius, mRipplePaint);
        }
        if (mBallCenter.x != 0) {
            canvas.drawCircle(mBallCenter.x, mBallCenter.y, mBallRadius, mBallPaint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (check(x, y)) {
                    mHasup = false;
                    startPressAnimator();
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!check(x, y) && !mHasup) {
                    startUpAnimator();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (check(x, y)) {
                    startUpAnimator();
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void startPressAnimator() {
        mDrawRipple = true;
        mPressAnimator.start();
    }

    private void startUpAnimator() {
        mDrawRipple = false;
        mHasup = true;

        mPressAnimator.cancel();
        if (mListener != null) {
            mListener.result((int) (100 * Math.abs(mDegree) / 45));
        }
        initBallPoints();
        mDegreeAnimator.setFloatValues(mDegree, 0);
        mBallAnimator.setObjectValues(mBallStart, mBallEnd);
        mBallAnimator.setInterpolator(new AccelerateInterpolator());
        mBallAnimator.setEvaluator(new PathEvaluator(mBallControll));
        mUpAnimator.setDuration(100 + (int) (100 * getDegreeFraction()));
        mUpAnimator.start();
    }


    private void initBallPoints() {
        float diff = (float) (Math.tan(Math.abs(mDegree) / 180 * Math.PI) * (mSpeaker.getWidth() + mSpace));
        mBallStart.set(getSliderLeftX(), mSpeaker.getRotationY() - diff);
        mBallEnd.set(getSliderLength() * getDegreeFraction() + getSliderLeftX(), mSpeaker.getRotationY());
        mBallControll = new Point((mBallStart.x + mBallEnd.x) * 0.5f, mBallStart.y - mSpeaker.getWidth() * getDegreeFraction());
    }

    private float getSliderLength() {
        return mWidth - mBallRadius - getSliderLeftX();
    }

    private float getSpeakerRightX() {
        return mSpeaker.getGap() + mSpeaker.getWidth();
    }

    private float getSliderLeftX() {
        return getSpeakerRightX() + mSpace;
    }

    private float getDegreeFraction() {
        return Math.abs(mDegree) / 45;
    }


    public void setDegree(float degree) {
        mDegree = degree;
        mRippleRadius = mRippleMaxRadius * getDegreeFraction();
        invalidate();
    }

    public void setBallLocation(Point point) {
        mBallCenter.set(point.x, point.y);
        invalidate();
    }

    public Point getBallLocation() {
        return mBallCenter;
    }

    public float getDegree() {
        return mDegree;
    }

    private boolean check(float x, float y) {
        return mSpeakerRect.contains(x, y);
    }

    public interface OnVolumeSlideListener {
        void result(int volume);
    }

    public void setOnVolumeSlideListener(OnVolumeSlideListener listener) {
        mListener = listener;
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

    public static final Property<VolumeSliderView, Point> BALLLOCATION = new Property<VolumeSliderView, Point>(Point.class, "ballLocation") {
        @Override
        public Point get(VolumeSliderView object) {
            return object.getBallLocation();
        }

        @Override
        public void set(VolumeSliderView object, Point value) {
            object.setBallLocation(value);
        }
    };
}
