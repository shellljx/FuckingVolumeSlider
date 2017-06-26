# FuckingVolumeSlider

[![](https://jitpack.io/v/shellljx/FuckingVolumeSlider.svg)](https://jitpack.io/#shellljx/FuckingVolumeSlider)

[源码GitHub地址](https://github.com/shellljx/FuckingVolumeSlider)，设计来自于[该贴内](https://www.reddit.com/r/ProgrammerHumor/comments/6f8ory/launch_a_90db_volume_slider_over_300_metres/)

前几天看到一个反人类音量滑动条设计的有趣帖子[原帖地址](https://www.reddit.com/r/ProgrammerHumor/comments/6f8ory/launch_a_90db_volume_slider_over_300_metres/)，网友设计了各种反人类的滑动条(这些脑洞我是服气的!)，于是我抽空在 android 上面实现了一个其中比较有趣的设计，点击喇叭弹射出去圆点控制音量。在写这个控件的时候遇到的一些知识点分享给大家。[视频演示地址](http://7vzpfd.com1.z0.glb.clouddn.com/shamuNBD92Glijx06172017222123.mp4)

效果图:

![](http://7vzpfd.com1.z0.glb.clouddn.com/fuckingslider.gif)

# Gradle

**Step 1.** Add it in your root build.gradle at the end of repositories:
```groovy
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```

**Step 2.** Add the dependency
```groovy
dependencies {
	    compile 'com.github.shellljx:TagViewGroup:-SNAPSHOT'
}
```

# 使用方法

**1. Define in xml**
```groovy
<com.licrafter.library.VolumeSliderView
    android:id="@+id/sliderView"
    android:layout_width="300dp"
    android:layout_height="wrap_content"/>
```
**2. Set Listener**
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

[控件实现思路](https://github.com/shellljx/FuckingVolumeSlider/wiki)
