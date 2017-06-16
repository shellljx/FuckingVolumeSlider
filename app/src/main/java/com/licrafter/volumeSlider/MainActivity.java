package com.licrafter.volumeSlider;

import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import com.licrafter.library.VolumeSliderView;

public class MainActivity extends AppCompatActivity {

    private VolumeSliderView mSliderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSliderView = (VolumeSliderView) findViewById(R.id.sliderView);
        mSliderView.setSweepDegree(45);
        final ObjectAnimator pressAnimator = ObjectAnimator.ofFloat(mSliderView, VolumeSliderView.DEGREE, 0, -45);
        pressAnimator.setDuration(1000);

        final ObjectAnimator upAnimator = new ObjectAnimator();
        upAnimator.setTarget(mSliderView);
        upAnimator.setProperty(VolumeSliderView.DEGREE);
        upAnimator.setDuration(100);

        mSliderView.setOnSliderTouchListener(new VolumeSliderView.OnSliderTouchListener() {
            @Override
            public void onPressDown() {
                pressAnimator.start();
            }

            @Override
            public void onUp() {
                pressAnimator.cancel();
                upAnimator.setFloatValues(mSliderView.getDegree(),0);
                upAnimator.start();
            }
        });
    }
}
