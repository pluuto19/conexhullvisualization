package com.asher.convexhulls;

import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.List;
import java.util.Stack;
import java.util.concurrent.locks.Lock;

public class BruteForce {
    static boolean shouldContinue = true;
    static Stack<Line> ij = new Stack<>();
    static Stack<Line> jk = new Stack<>();
    public static void bruteForce(List<point> points, Stage stg, Group grp) {
        stg.getScene().setOnKeyPressed(null);
        for (int i = 0; i < points.size(); i++) {
            for (int j = 0; j < points.size(); j++) {
                if (points.get(i) != points.get(j)) {
                    //draw temp line ij
                    drawLineIJ(points.get(i), points.get(j), grp);
                    for (int k = 0; k < points.size(); k++) {
                        shouldContinue = true;
                        if (points.get(k) != points.get(j) && points.get(k) != points.get(i)) {
                            //draw line jk
                            drawLineJK(points.get(j), points.get(k), grp);
                            if (ConvexHullUtil.ccwSlope(points.get(i), points.get(j), points.get(k)) == -1) {
                                //remove line ij
                                grp.getChildren().remove(ij.pop());
                                shouldContinue = false;
                            }
                            //remove line jk
                            grp.getChildren().remove(jk.pop());
                            if (!shouldContinue) {
                                break;
                            }
                        }
                    }
                }
                if (shouldContinue) {
                    //perma line ij
                    drawLineIJ(points.get(i), points.get(j), grp);
                }
            }
        }
    }

    private static void drawLineIJ(point a, point b, Group grp) {
        Line l1 = new Line(a.x, a.y, b.x, b.y);
        l1.setStroke(Color.web("#008000"));
        grp.getChildren().add(l1);
        ij.push(l1);
    }

    private static void drawLineJK(point a, point b, Group grp) {
        Line l1 = new Line(a.x, a.y, b.x, b.y);
        l1.setStroke(Color.web("#008000"));
        grp.getChildren().add(l1);
        jk.push(l1);
    }
}