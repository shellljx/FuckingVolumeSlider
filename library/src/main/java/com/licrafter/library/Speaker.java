package com.licrafter.library;

import android.animation.FloatEvaluator;
import android.graphics.Path;
import android.support.v4.util.ArrayMap;

/**
 * Created by lijx on 2017/6/16.
 */

public class Speaker {
    private ArrayMap<Integer, Wawe> wawes = new ArrayMap<>();
    private Base base;
    private float rotationX;
    private float rotationY;
    private float gap;

    private int width;
    private int height;


    static Speaker newInstance(int width, int height) {
        return new Speaker(width, height);
    }

    private Speaker(int w, int h) {
        height = width = w;
        rotationY = h - height / 2f;
        gap = height * 0.2f;
        rotationX = gap;

        createBase();
        createSmallWawe(width * 0.75f, height / 2);
        createBigWawe(width*0.95f, height / 2);
    }

    private void createBase() {
        base = new Base(
                new Point(gap, rotationY + gap)
                , new Point(gap + width / 6f, rotationY + gap)
                , new Point(gap + width / 2 * 0.8f, rotationY + width / 2 * 0.8f)
                , new Point(gap + width / 2, rotationY + height * 0.5f)
                , new Point(gap + width / 2, rotationY + width / 2 * 0.8f));
        fillReflectionPoints(base);
    }

    private void createSmallWawe(float centerX, float centerY) {
        float fraction = 0.2f;
        FloatEvaluator evaluator = new FloatEvaluator();
        Point rightControll = new Point(evaluator.evaluate(fraction, centerX, centerX * 1.45), centerY);
        Point rightPoint = new Point(evaluator.evaluate(fraction, centerX * 0.95, centerX), evaluator.evaluate(fraction, centerY * 0.35 * 1.5, centerY));
        Wawe wawe = createWawe(
                rightControll, //right controll
                new Point(rightControll.x - gap * 0.45f, centerY), //left controll
                rightPoint, //right point
                new Point(rightPoint.x - gap * 0.7f, rightPoint.y), //left point
                new Point(rightPoint.x - gap * 0.4f, rightPoint.y - gap * 0.6f), //top right controll
                new Point(rightPoint.x - gap * 0.9f, rightPoint.y - gap * 0.4f) //top left controll
        );
        wawes.put(0, wawe);
    }

    private void createBigWawe(float centerX, float centerY) {
        float fraction = 0.2f;
        FloatEvaluator evaluator = new FloatEvaluator();
        Point rightControll = new Point(evaluator.evaluate(fraction, centerX, centerX * 1.45), centerY);
        Point rightPoint = new Point(evaluator.evaluate(fraction, centerX * 0.95, centerX), evaluator.evaluate(fraction, centerY * 0.35, centerY));
        Wawe wawe = createWawe(
                rightControll, //right controll
                new Point(rightControll.x - gap * 0.45f, centerY), //left controll
                rightPoint, //right point
                new Point(rightPoint.x - gap * 0.7f, rightPoint.y), //left point
                new Point(rightPoint.x - gap * 0.4f, rightPoint.y - gap * 0.6f), //top right controll
                new Point(rightPoint.x - gap * 0.9f, rightPoint.y - gap * 0.4f) //top left controll
        );
        wawes.put(1, wawe);
    }

    private Wawe createWawe(Point... points) {
        for (Point point : points) {
            point.trans(gap, rotationY - height / 2f);
        }
        Wawe wawe = new Wawe();
        wawe.RIGHT_CURVE[0] = points[2];
        wawe.RIGHT_CURVE[1] = points[0];
        wawe.LEFT_CURVE[0] = points[3];
        wawe.LEFT_CURVE[1] = points[1];
        wawe.RIGHT_CURVE[2] = getReflectionPointY(rotationY, wawe.RIGHT_CURVE[0]);
        wawe.LEFT_CURVE[2] = getReflectionPointY(rotationY, wawe.LEFT_CURVE[0]);
        wawe.CURVE_CTR[0] = points[4];
        wawe.CURVE_CTR[1] = points[5];
        wawe.CURVE_CTR[2] = getReflectionPointY(rotationY, points[4]);
        wawe.CURVE_CTR[3] = getReflectionPointY(rotationY, points[5]);
        return wawe;
    }

    private void fillReflectionPoints(Base base) {
        for (int i = 0; i < 5; i++) {
            base.points[9 - i] = getReflectionPointY(rotationY, base.points[i]);
        }
    }

    private Point getReflectionPointY(float centerY, Point point) {
        return new Point(point.x, centerY - (point.y - centerY));
    }

    private class Wawe {

        Point[] RIGHT_CURVE = new Point[3];
        Point[] LEFT_CURVE = new Point[3];
        Point[] CURVE_CTR = new Point[4];

        Path getPath() {
            Path path = new Path();
            path.moveTo(LEFT_CURVE[0].x, LEFT_CURVE[0].y);
            path.quadTo(LEFT_CURVE[1].x, LEFT_CURVE[1].y, LEFT_CURVE[2].x, LEFT_CURVE[2].y);
            path.cubicTo(CURVE_CTR[3].x, CURVE_CTR[3].y, CURVE_CTR[2].x, CURVE_CTR[2].y, RIGHT_CURVE[2].x, RIGHT_CURVE[2].y);
            path.quadTo(RIGHT_CURVE[1].x, RIGHT_CURVE[1].y, RIGHT_CURVE[0].x, RIGHT_CURVE[0].y);
            path.cubicTo(CURVE_CTR[0].x, CURVE_CTR[0].y, CURVE_CTR[1].x, CURVE_CTR[1].y, LEFT_CURVE[0].x, LEFT_CURVE[0].y);
            return path;
        }
    }

    private static class Base {
        private Point[] points = new Point[10];

        Base(Point... points) {
            System.arraycopy(points, 0, this.points, 0, 5);
        }

        void fillPath(Path path) {
            path.reset();
            path.moveTo(points[0].x, points[0].y);
            path.lineTo(points[1].x, points[1].y);
            path.lineTo(points[2].x, points[2].y);
            path.quadTo(points[3].x, points[3].y, points[4].x, points[4].y);
            path.lineTo(points[5].x, points[5].y);
            path.quadTo(points[6].x, points[6].y, points[7].x, points[7].y);
            path.lineTo(points[8].x, points[8].y);
            path.lineTo(points[9].x, points[9].y);
        }
    }

    float getRotationX() {
        return rotationX;
    }

    float getRotationY() {
        return rotationY;
    }

    float getGap() {
        return gap;
    }

    float getWidth() {
        return width;
    }

    float getHeight() {
        return height;
    }

    void fillPath(Path path) {
        base.fillPath(path);
        path.addPath(wawes.get(0).getPath());
        path.addPath(wawes.get(1).getPath());
    }
}
