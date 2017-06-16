package com.licrafter.library;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * author: shell
 * date 16/7/21 下午2:53
 **/
public class PathEvaluator implements TypeEvaluator<Point> {

    private Point mAuxiliaryPoint;

    public PathEvaluator(Point pointF) {
        this.mAuxiliaryPoint = pointF;
    }

    @Override
    public Point evaluate(float t, Point startPoint, Point endPoint) {

        return CalculateBezierPointForQuadratic(t, startPoint, mAuxiliaryPoint, endPoint);
    }

    /**
     * B(t) = (1 - t)^2 * P0 + 2t * (1 - t) * P1 + t^2 * P2, t ∈ [0,1]
     *
     * @param t  曲线长度比例
     * @param p0 起始点
     * @param p1 控制点
     * @param p2 终止点
     * @return t对应的点
     */
    public static Point CalculateBezierPointForQuadratic(float t, Point p0, Point p1, Point p2) {
        Point point = new Point();
        float temp = 1 - t;
        point.x = temp * temp * p0.x + 2 * t * temp * p1.x + t * t * p2.x;
        point.y = temp * temp * p0.y + 2 * t * temp * p1.y + t * t * p2.y;
        return point;
    }
}
