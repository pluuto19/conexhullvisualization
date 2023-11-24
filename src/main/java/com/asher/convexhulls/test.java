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
// create a keyframe within keyframe, both within different timelines. pause outer keyframe when inner keyframe is playing.
class test {
    static boolean shouldContinue = true;
    static Stack<Line> ij = new Stack<>();
    static Stack<Line> jk = new Stack<>();
    static Timeline tl = new Timeline();
    static Timeline tl2 = new Timeline();
    static int i = 0, j = 0, k = 0;
    static boolean currentlyStopped = false;
    public static void bruteForce(List<point> points, Stage stg, Group grp) {
        stg.getScene().setOnKeyPressed(null);
        tl.setCycleCount(Timeline.INDEFINITE);
        tl2.setCycleCount(Timeline.INDEFINITE);

        KeyFrame kf = new KeyFrame(Duration.seconds(0.1f), actionEvent -> {
            System.out.println("consitently repeating");
            if (points.get(i) != points.get(j)) {
                drawLineIJ(points.get(i), points.get(j), grp); // ij selected
                KeyFrame kf2 = new KeyFrame(Duration.seconds(0.1f), actionEvent2 -> {
                    if (jk.size() >= 1) {
                        grp.getChildren().remove(jk.pop()); // ends here, if stack aint empty then pop then draw move this line to top
                    }
                    drawLineJK(points.get(j), points.get(k), grp); // jk selected, k loop starts here
                    if (ConvexHullUtil.ccwSlope(points.get(i), points.get(j), points.get(k)) == 1) {
                        grp.getChildren().remove(ij.pop());
                        shouldContinue = false;
                        tl.play();
                        tl2.pause();
                        currentlyStopped = true;
                    }
                    k++;

                    if (k >= points.size()) {
                        currentlyStopped = true;
                        shouldContinue = true;
                        tl.play();
                        tl2.pause();
                        System.out.println("stopped tl2 2");
                    }

                });

                if ( (k<points.size()) && (points.get(k) != points.get(j) && points.get(k) != points.get(i)) && !currentlyStopped ) {
                    System.out.println("got k, stopped tl1, tl2 running");
                    tl2.getKeyFrames().add(kf2);
                    tl2.play();
                    tl.pause();
                } else {
                    k++;
                }
            } else {
                j++;
            }

            if(!shouldContinue){
                j++;
                k=0;
                shouldContinue = true;
                currentlyStopped = false;
                tl.pause();
                tl2.play();
            }
            if (k >= points.size()) {
                if(shouldContinue){
                    drawLineIJ(points.get(i), points.get(j), grp);
                }
                j++;
                k = 0;
                shouldContinue = true;
                currentlyStopped = false;
                tl.pause();
                tl2.play();
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
        if (ij.isEmpty()) {
            Line l1 = new Line(a.x, a.y, b.x, b.y);
            l1.setStroke(Color.web("#4fcaf0"));
            grp.getChildren().add(l1);
            ij.push(l1);
        }
        if ((a.x != ij.peek().getStartX() && a.y != ij.peek().getStartY()) || (b.x != ij.peek().getEndX() && b.y != ij.peek().getEndY())) {
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