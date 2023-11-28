package com.asher.convexhulls;

import javafx.scene.effect.Light;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class ConvexHullUtil {
    public static int ccwSlope(point p0, point p1, point p2) {
        int dx1, dx2, dy1, dy2;
        dx1 = p1.x - p0.x;
        dy1 = p1.y - p0.y;
        dx2 = p2.x - p0.x;
        dy2 = p2.y - p0.y;
        if (dx1 * dy2 > dy1 * dx2) {
            return 1;
        }
        if (dx1 * dy2 < dy1 * dx2) {
            return -1;
        }
        if ((dx1 * dx2 < 0) || (dy1 * dy2 < 0)) {
            return -1;
        }
        if ((dx1 * dx1 + dy1 * dy1) < (dx2 * dx2 + dy2 * dy2)) {
            return 1;
        }
        return 0;
    }

    public static boolean doIntersectSlope(point p0, point p1, point p2, point p3) {
        return (ccwSlope(p0, p1, p2) * ccwSlope(p0, p1, p3) <= 0) && (ccwSlope(p2, p3, p0) * ccwSlope(p2, p3, p1) <= 0);
    }

    public static int ccwArea(point p0, point p1, point p2) {
        int area = (p1.x - p0.x) * (p2.y - p0.y) - (p1.y - p0.y) * (p2.x - p0.x);
        if (area < 0) {
            return -1;
        } else if (area > 0) {
            return 1;
        } else {
            return 0;
        }
    }

    public static boolean doIntersectArea(point p0, point p1, point p2, point p3) {
        return (ccwArea(p0, p1, p2) * ccwArea(p0, p1, p3) <= 0) && (ccwArea(p2, p3, p0) * ccwArea(p2, p3, p1) <= 0);
    }

    public static float myAngle(point a, point b) {
        float dx = a.x - b.x;
        float dy = a.y - b.y;
        double angle = Math.atan2(dy, dx);
        if (Math.toDegrees(angle) < 0) {
            return (float) (360.0f + Math.toDegrees(angle));
        } else {
            return (float) Math.toDegrees(angle);
        }
    }

    public static boolean doIntersectSweepLine(point p0, point p1, point p2, point p3, List<point> points) {
        points.sort(Comparator.comparingDouble(point::getX));
        if (points.get(0).name.equals(points.get(1).name)) {
            System.out.println("here");
            return false;
        } else {
            System.out.println("else here");
            return doIntersectArea(p0, p1, p2, p3);
        }
    }
}
