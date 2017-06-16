package com.licrafter.volumeSlider;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.licrafter.library.VolumeSliderView;

public class MainActivity extends AppCompatActivity {

    private VolumeSliderView mSliderView;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSliderView = (VolumeSliderView) findViewById(R.id.sliderView);
        mTextView = (TextView) findViewById(R.id.text);
        mSliderView.setOnVolumeSlideListener(new VolumeSliderView.OnVolumeSlideListener() {
            @Override
            public void result(int volume) {
                mTextView.setText("音量: " + volume);
            }
        });
    }
}
