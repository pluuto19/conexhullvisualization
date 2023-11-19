package com.asher.convexhulls;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.security.Key;
import java.util.List;
import java.util.Stack;

class animBruteForce {
    static boolean shouldContinue = true;
    static Stack<Line> ij = new Stack<>();
    static Stack<Line> jk = new Stack<>();
    static Timeline tl = new Timeline();
    static int i = 0, j = 0, k = 0;
    public static void bruteForce(List<point> points, Stage stg, Group grp) {
        stg.getScene().setOnKeyPressed(null);
        tl.setCycleCount(Timeline.INDEFINITE);

        KeyFrame kf = new KeyFrame(Duration.seconds(0.1f), actionEvent -> {
            if (points.get(i) != points.get(j)) {
                drawLineIJ(points.get(i), points.get(j), grp);
                if (points.get(k) != points.get(j) && points.get(k) != points.get(i)) {
                    drawLineJK(points.get(j), points.get(k), grp);
                    if (ConvexHullUtil.ccwSlope(points.get(i), points.get(j), points.get(k)) == 1) {
                        grp.getChildren().remove(ij.pop());
                        shouldContinue = false;
                    }
                    grp.getChildren().remove(jk.pop());
                }
                k++;
                if(!shouldContinue){
                    j++;
                    k=0;
                    shouldContinue = true;
                }
            }else{
                j++;
            }
            if (k >= points.size() && shouldContinue) {
                drawLineIJ(points.get(i), points.get(j), grp);
            }
            if (k >= points.size()) {
                j++;
                k = 0;
                shouldContinue = true;
            }
            if (j >= points.size()) {
                i++;
                j = 0;
            }
            if (i >= points.size()) {
                tl.stop();
            }
        });
        tl.getKeyFrames().add(kf);
        tl.play();
    }
    private static void drawLineIJ(point a, point b, Group grp) {
        if(ij.isEmpty()){
            Line l1 = new Line(a.x, a.y, b.x, b.y);
            l1.setStroke(Color.web("#4fcaf0"));
            grp.getChildren().add(l1);
            ij.push(l1);
        }
        if((a.x!=ij.peek().getStartX() && a.y!=ij.peek().getStartY()) || (b.x!=ij.peek().getEndX() && b.y!=ij.peek().getEndY()) ){
            Line l1 = new Line(a.x, a.y, b.x, b.y);
            l1.setStroke(Color.web("#4fcaf0"));
            grp.getChildren().add(l1);
            ij.push(l1);
        }
    }
    private static void drawLineJK(point a, point b, Group grp) {
        Line l1 = new Line(a.x, a.y, b.x, b.y);
        l1.setStroke(Color.web("#4fcaf0"));
        grp.getChildren().add(l1);
        jk.push(l1);
    }
}