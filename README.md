# FuckingVolumeSlider
前几天看到一个反人类音量滑动条设计的有趣帖子[原帖地址](https://www.reddit.com/r/ProgrammerHumor/comments/6f8ory/launch_a_90db_volume_slider_over_300_metres/)，网友设计了各种反人类的滑动条(这些脑洞我是服气的!)，于是我抽空在 android 上面实现了一个其中比较有趣的设计，点击喇叭弹射出去圆点控制音量。在写这个控件的时候遇到的一些知识点分享给大家。[视频演示地址](http://7vzpfd.com1.z0.glb.clouddn.com/shamuNBD92Glijx06172017222123.mp4)

效果图

![](http://7vzpfd.com1.z0.glb.clouddn.com/fuckingslider.gif)

# 使用方法
```java
mSliderView = (VolumeSliderView) findViewById(R.id.sliderView);

mSliderView.setOnVolumeSlideListener(new VolumeSliderView.OnVolumeSlideListener() {
    @Override
    public void result(int volume) {
        mTextView.setText("音量: " + volume);
    }
});
```

# 原理
**1. 水波纹和喇叭底座绘制**

**2. 声波绘制**

**3. 旋转**
