package com.asher.convexhulls;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;


public class JarvisMarch {
    static int maxY;
    static int maxIdx = -1;
    static point maxPoint;
    private static float currAngle = 0.0f;
    private static int index=1;
    static LinkedList<point> hull = new LinkedList<>();
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
    public static void jarvisMarch(List<point> points, Stage stg, Group grp){
        stg.getScene().setOnKeyPressed(null);
        findMaxY(points);
        points.set(maxIdx, points.get(index));
        points.set(index, maxPoint);
        for(int i = 0 ; i < points.size() ;i++){
            points.get(i).isOnHull=false;
            points.get(i).isFirstPointOnHull=false;
        }
        hull.add(points.get(index));
        points.get(1).isOnHull = true;
        points.get(1).isFirstPointOnHull = true;
        while( ! (hull.get(0)==hull.get(hull.size()-1) && hull.size()>=3) ){
            for(int j = 0 ; j < points.size() ; j++){
                if(points.get(index)==points.get(j)){
                    points.get(j).angle = 0.0f;
                }else if(points.get(j).isOnHull && !points.get(j).isFirstPointOnHull){
                    points.get(j).angle = Float.MAX_VALUE;
                }else{
                    points.get(j).angle = ConvexHullUtil.myAngle(points.get(index), points.get(j));
                }
            }
            points.sort(Comparator.comparingDouble(point::getAngle));
            index = 1;
            for (int i = 1 ; i < points.size() ; i++){
                if( ((points.get(i).isOnHull && points.get(i).isFirstPointOnHull)||(!points.get(i).isOnHull)) && points.get(i).angle >= currAngle){
                    points.get(i).isOnHull = true;
                    hull.add(points.get(i));
                    currAngle = points.get(i).angle;
                    Line l = new Line(points.get(0).x, points.get(0).y, points.get(i).x, points.get(i).y);
                    l.setStroke(Color.web("#008000"));
                    grp.getChildren().add(l);
                    break;
                }else{
                    index++;
                }
            }
        }
    }
}
