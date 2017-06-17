# FuckingVolumeSlider

[源码GitHub地址](https://github.com/shellljx/FuckingVolumeSlider)，设计来自于[该贴内](https://www.reddit.com/r/ProgrammerHumor/comments/6f8ory/launch_a_90db_volume_slider_over_300_metres/)

前几天看到一个反人类音量滑动条设计的有趣帖子[原帖地址](https://www.reddit.com/r/ProgrammerHumor/comments/6f8ory/launch_a_90db_volume_slider_over_300_metres/)，网友设计了各种反人类的滑动条(这些脑洞我是服气的!)，于是我抽空在 android 上面实现了一个其中比较有趣的设计，点击喇叭弹射出去圆点控制音量。在写这个控件的时候遇到的一些知识点分享给大家。[视频演示地址](http://7vzpfd.com1.z0.glb.clouddn.com/shamuNBD92Glijx06172017222123.mp4)

效果图:

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
**1. 喇叭底座与声波绘制**

由效果图可以看出，喇叭底座的右上角和右下角为圆角，其余的角都不是圆角。非圆角通过 path.lineTo 可以非常容易的画出，画圆角折线有很多种方法，其中最简单的就是给画笔 paint 添加 CornerPathEffect 就可以轻松的画出圆角，我开始也是这么做的(too yang too simple) 。在这个控件的场景中这么做并不合理，因为后面还要在喇叭的区域绘制水波纹，而 PathEffect 只是让画笔绘制出的效果看着像圆角，水波纹是绘制在底座的 path 上的，而底座的 path 并不是圆角，所以绘制完水波纹之后圆角被填充回了直角。

还有什么简单办法可以绘制圆角折线呢？那就是使用贝塞尔曲线了，可以用二阶贝塞尔曲线来作为圆角完美解决，要想完美衔接需要两个衔接的折线点分别和控制点相对于贝塞尔曲线的起点和终点是对称的：

![](http://7vzpfd.com1.z0.glb.clouddn.com/yuanjiao.gif)

整个底座也是相对于一个中轴是对称的，所以只要选取出上半部或者下半部的点就可以通过中轴来算错所有的点。

声波到底怎么画出来呢？声波的头部也是圆角，刚开始的时候我简单的以为通过 `path.addArc()` 画出一个圆弧，然后圆弧的 paint 设置一个宽度和 `Paint.Cap.ROUND` 就可以完成，其实这种方法也会和后面画水波纹的时候冲突的，因为圆弧的 path 所围成的区域并不是一个弧线区域，这都是 paint 宽度造成的视觉效果。

后来我在这篇文章里得到了启发 [Android SmileyRating](https://blog.mindorks.com/android-smileyrating-how-i-solved-it-9b5ee30f2c34)，声波可以用四条曲线围起来啊，左右两条二阶贝塞尔曲线，上下两条三阶贝塞尔曲线，一条声波通过 10 个点就可以画出来，而声波也相对于一个中轴对称的，所以只要取出上半部或者下半部的 5 个点就可以确定所有的点。

![]http://7vzpfd.com1.z0.glb.clouddn.com/1-RDpoOMgInWPG_o1x90kdhQ.jpeg)

**2. 水波纹绘制与旋转**

水波纹的绘制用到 `canvas.clipPath()` 方法把喇叭的 path 裁剪出来再绘制圆形水波纹就可以了。旋转是对喇叭的 path 执行 Matrix 旋转操作。
```java
//clip
canvas.clipPath(mDrawPath);
canvas.drawCircle(mSpeaker.getRotationX(), mSpeaker.getRotationY(), mRippleRadius, mRipplePaint);
//matrix rotate
mRotationMatrix.preRotate(mDegree, mSpeaker.getRotationX(), mSpeaker.getRotationY());
mDrawPath.addPath(mSpeakerPath, mRotationMatrix);
```
**球的运动曲线**

其实球的运动曲线不是抛物线，而是一条二阶贝塞尔曲线，通过调整控制点的位置来达到和抛物线类似的效果。

