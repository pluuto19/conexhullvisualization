package com.asher.convexhulls;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class animJarvisMarch {
    static int maxY;
    static int maxIdx = -1;
    static point maxPoint;
    private static float currAngle = 0.0f;
    private static int index = 1;
    static LinkedList<point> hull = new LinkedList<>();
    static Timeline tl = new Timeline();
    static Timeline tl2 = new Timeline();
    static int j = 0;
    static boolean shouldCont = false;
    static Stack<Line> lines = new Stack<>();
    private static void findMaxY(List<point> points) {
        maxY = points.get(0).y;
        maxIdx = 0;
        maxPoint = points.get(0);
        for (int i = 1; i < points.size(); i++) {
            if (points.get(i).y > maxY) {
                maxY = points.get(i).y;
                maxIdx = i;
                maxPoint = points.get(i);
            }
        }
    }
    public static void jarvisMarch(List<point> points, Stage stg, Group grp) {
        tl.setCycleCount(Timeline.INDEFINITE);
        tl2.setCycleCount(Timeline.INDEFINITE);
        stg.getScene().setOnKeyPressed(null);
        findMaxY(points);
        points.set(maxIdx, points.get(index));
        points.set(index, maxPoint);
        for (int i = 0; i < points.size(); i++) {
            points.get(i).isOnHull = false;
            points.get(i).isFirstPointOnHull = false;
        }
        hull.add(points.get(index));
        points.get(1).isOnHull = true;
        points.get(1).isFirstPointOnHull = true;

        KeyFrame kf = new KeyFrame(Duration.seconds(0.3), actionEvent -> {
            KeyFrame kf2 = new KeyFrame(Duration.seconds(0.2), actionEvent1 -> {
                if (lines.size() >= 1) grp.getChildren().remove(lines.pop());
                drawLine(points.get(j), points.get(index), grp);
                if (points.get(index) == points.get(j)) {
                    points.get(j).angle = 0.0f;
                } else if (points.get(j).isOnHull && !points.get(j).isFirstPointOnHull) {
                    points.get(j).angle = Float.MAX_VALUE;
                } else {
                    points.get(j).angle = ConvexHullUtil.myAngle(points.get(index), points.get(j));
                }
                j++;
                if (j >= points.size()) {
                    j = 0;
                    shouldCont = true;
                    tl2.stop();
                }
            });

            tl2.getKeyFrames().add(kf2);
            tl2.play();
            if(shouldCont){

                points.sort(Comparator.comparingDouble(point::getAngle));
                index = 1;
                for (int i = 1; i < points.size(); i++) {
                    if (((points.get(i).isOnHull && points.get(i).isFirstPointOnHull) || (!points.get(i).isOnHull)) && points.get(i).angle >= currAngle) {
                        points.get(i).isOnHull = true;
                        hull.add(points.get(i));
                        currAngle = points.get(i).angle;
                        drawLine(points.get(i), points.get(0), grp);
                        lines.pop();
                        break;
                    } else {
                        index++;
                    }
                }
                shouldCont = false;
                if ((hull.get(0) == hull.get(hull.size() - 1) && hull.size() >= 3)) {
                    tl.stop();
                }
            }
        });
        tl.getKeyFrames().add(kf);
        tl.play();
    }
    private static void drawLine(point a, point b, Group grp) {
        Line l1 = new Line(a.x, a.y, b.x, b.y);
        l1.setStroke(Color.web("#008000"));
        grp.getChildren().add(l1);
        lines.push(l1);
    }
}