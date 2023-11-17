package com.asher.convexhulls;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.Comparator;
import java.util.List;
import java.util.Stack;

public class GrahamScan {
    static int maxY;
    static int maxIdx = -1;
    static point maxPoint;
    static Stack<point> stack = new Stack<>();
    static point last;
    static point secondLast;
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
    public static void grahamScan(List<point> points, Stage stg, Group grp){
        stg.getScene().setOnKeyPressed(null);
        findMaxY(points);
        points.set(maxIdx, points.get(0));
        points.set(0, maxPoint);
        for(int i = 0 ; i < points.size() ; i++){points.get(i).angle = ConvexHullUtil.myAngle(points.get(0), points.get(i));}
        points.sort(Comparator.comparingDouble(point::getAngle));
        stack.push(points.get(0));
        secondLast = points.get(0);
        stack.push(points.get(1));
        last = points.get(1);
        drawLine(last, secondLast, grp);

        for (int i = 2 ; i < points.size() ; i++){
            if(ConvexHullUtil.ccwSlope(secondLast, last, points.get(i)) == 1 || ConvexHullUtil.ccwSlope(secondLast, last, points.get(i)) == 0 ){
                stack.push(points.get(i));
                secondLast = last;
                last = points.get(i);
                drawLine(last, secondLast, grp);
            }
            else{
                grp.getChildren().remove(lines.pop());
                stack.pop();
                last = secondLast;
                secondLast = stack.get(stack.size()-2);
                i--;
            }
        }
        stack.add(stack.get(0));
        last = stack.get(stack.size()-1);
        secondLast = stack.get(stack.size()-2);
        drawLine(last,secondLast,grp);
    }
    private static void drawLine(point a, point b, Group grp){
        Line l1 = new Line(a.x, a.y, b.x, b.y);
        l1.setStroke(Color.web("#008000"));
        grp.getChildren().add(l1);
        lines.push(l1);
    }
}
