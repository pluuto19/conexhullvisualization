package com.asher.convexhulls;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public class QuickHull {
    static int maxX;
    static int maxXIdx = -1;
    static point maxXPoint;
    private static point findMaxX(List<point> points) {
        maxX = points.get(0).x;
        maxXIdx = 0;
        maxXPoint = points.get(0);
        for (int i = 1; i < points.size(); i++) {
            if (points.get(i).x > maxX) {
                maxX = points.get(i).x;
                maxXIdx = i;
                maxXPoint = points.get(i);
            }
        }
        return maxXPoint;
    }
    static int minX;
    static int minXIdx = -1;
    static point minXPoint;

    private static point findMinX(List<point> points) {
        minX = points.get(0).x;
        minXIdx = 0;
        minXPoint = points.get(0);
        for (int i = 1; i < points.size(); i++) {
            if (points.get(i).x < minX) {
                minX = points.get(i).x;
                minXIdx = i;
                minXPoint = points.get(i);
            }
        }
        return minXPoint;
    }

    private static LinkedList<point> hull = new LinkedList<>();
    static int ccwValue;
    public static void quickHull(List<point> points, Stage stg, Group grp) {
        System.out.println("called");
        findMinX(points); //a
        findMaxX(points); //b
        hull.add(minXPoint); //0th
        hull.add(maxXPoint); //1st

        LinkedList<point> upperSet = new LinkedList<>();
        LinkedList<point> lowerSet = new LinkedList<>();
        for (int i = 0; i < points.size(); i++) {
            ccwValue = ConvexHullUtil.ccwSlope(hull.get(0), hull.get(1), points.get(i));
            if (ccwValue == -1) { // upperSet
                upperSet.add(points.get(i));
            } else if (ccwValue == 1) { //lower set
                lowerSet.add(points.get(i));
            } //ignore those who are collinear because automatically they are inside so no need
        }

        funcUpper(upperSet, minXPoint, maxXPoint);
        funcLower(lowerSet, minXPoint, maxXPoint);

        for (int i = 0; i < hull.size(); i++) {
            System.out.print(hull.get(i).name + ", ");
        }
    }
    private static void funcUpper(List<point> upperArr, point a, point b) {
        //each recursive call gets the set and extreme points a and b
        // each recursive call has to find its set's highest point
        int maxArea = Math.abs(area(a, b, upperArr.get(0)));
        int maxIdx = 0;
        point maxPoint = upperArr.get(0);
        for (int i = 1; i < upperArr.size(); i++) {
            int area = Math.abs(area(a, b, upperArr.get(i)));
            if (area > maxArea) {
                maxArea = area;
                maxIdx = i;
                maxPoint = upperArr.get(i);
            }
        }
        hull.add(maxPoint);

        // once the highest point is founded, create triangle a - b - maxPoint
        // and calculate set1: all points ccw to a-maxPoint
        // and calculate set2: all points ccw maxPoint-b
        LinkedList<point> set1 = new LinkedList<>();
        LinkedList<point> set2 = new LinkedList<>();

        for (int i = 0; i < upperArr.size(); i++) {
            int ccwValue = ConvexHullUtil.ccwSlope(a, maxPoint, upperArr.get(i));
            if (ccwValue == -1) {
                set1.add(upperArr.get(i));
            }
        }
        for (int i = 0; i < upperArr.size(); i++) {
            int ccwValue = ConvexHullUtil.ccwSlope(maxPoint, b, upperArr.get(i));
            if (ccwValue == -1) {
                set2.add(upperArr.get(i));
            }
        }

        //only recurse to the set if its size is not 0
        // but if the size is 1 just simply add that point directly to the hull[], no need to recurse
        if(set1.size()!=0){
            if(set1.size()==1){
                hull.add(set1.get(0));
            }else{
                // recurse here
                funcUpper(set1, a, maxPoint);
            }
        }
        if(set2.size()!=0){
            if(set2.size()==1){
                hull.add(set2.get(0));
            }else{
                // recurse here
                funcUpper(set2, maxPoint, b);
            }
        }

    }
    private static void funcLower(List<point> lowerArr, point a, point b) {
        //each recursive call gets the set and extreme points a and b
        // each recursive call has to find its set's lowest point
        int maxArea = Math.abs(area(a, b, lowerArr.get(0)));
        int maxIdx = 0;
        point maxPoint = lowerArr.get(0);
        for (int i = 1; i < lowerArr.size(); i++) {
            int area = Math.abs(area(a, b, lowerArr.get(i)));
            if (area > maxArea) {
                maxArea = area;
                maxIdx = i;
                maxPoint = lowerArr.get(i);
            }
        }
        hull.add(maxPoint);

        // once the highest point is founded, create triangle a - b - maxPoint
        // and calculate set1: all points cw to a-maxPoint
        // and calculate set2: all points cw maxPoint-b
        LinkedList<point> set1 = new LinkedList<>();
        LinkedList<point> set2 = new LinkedList<>();

        for (int i = 0; i < lowerArr.size(); i++) {
            int ccwValue = ConvexHullUtil.ccwSlope(a, maxPoint, lowerArr.get(i));
            if (ccwValue == 1) {
                set1.add(lowerArr.get(i));
            }
        }
        for (int i = 0; i < lowerArr.size(); i++) {
            int ccwValue = ConvexHullUtil.ccwSlope(maxPoint, b, lowerArr.get(i));
            if (ccwValue == 1) {
                set2.add(lowerArr.get(i));
            }
        }

        //only recurse to the set if its size is not 0
        // but if the size is 1 just simply add that point directly to the hull[], no need to recurse
        if(set1.size()!=0){
            if(set1.size()==1){
                hull.add(set1.get(0));
            }else{
                // recurse here
                funcLower(set1, a, maxPoint);
            }
        }
        if(set2.size()!=0){
            if(set2.size()==1){
                hull.add(set2.get(0));
            }else{
                // recurse here
                funcLower(set2, maxPoint, b);
            }
        }

    }

    private static void drawLine(Group grp) {
        hull.sort(Comparator.comparing(point::getName));
    }

    public static int area(point p0, point p1, point p2) {
        return (p1.x - p0.x) * (p2.y - p0.y) - (p1.y - p0.y) * (p2.x - p0.x);
    }
}
