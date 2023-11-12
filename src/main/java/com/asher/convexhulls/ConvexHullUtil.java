package com.asher.convexhulls;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.Comparator;
import java.util.List;
public class ConvexHullUtil {
    private static int ccwSlope(point p0, point p1, point p2){
        int dx1,dx2,dy1,dy2;
        dx1 = p1.x - p0.x;
        dy1 = p1.y - p0.y;
        dx2 = p2.x - p0.x;
        dy2 = p2.y - p0.y;
        if(dx1*dy2 > dy1*dx2){
            return 1;
        }
        if (dx1*dy2 < dy1*dx2){
            return -1;
        }
        if ( (dx1*dx2 < 0) || (dy1*dy2 < 0) ){
            return -1;
        }
        if( (dx1*dx1 + dy1*dy1) < (dx2*dx2 + dy2*dy2) ){
            return 1;
        }
        return 0;
    }
    public static boolean doIntersectSlope(point p0, point p1, point p2, point p3){
        return (ccwSlope(p0,p1,p2)*ccwSlope(p0,p1,p3) <= 0) && (ccwSlope(p2,p3,p0)*ccwSlope(p2,p3,p1) <= 0);
    }
    private static int ccwArea(point p0, point p1, point p2){
        int area = (p1.x - p0.x)*(p2.y-p0.y) - (p1.y-p0.y)*(p2.x-p0.x);
        if(area<0){
            return -1;
        }else if(area>0){
            return 1;
        }else{
            return 0;
        }
    }
    public static boolean doIntersectArea(point p0, point p1, point p2, point p3){
        return (ccwArea(p0,p1,p2)*ccwArea(p0,p1,p3) <= 0) && (ccwArea(p2,p3,p0)*ccwArea(p2,p3,p1) <= 0);
    }
    public static float angle(point p1, point p2){
        int dx = p2.x - p1.x;
        int dy = p2.y - p1.y;
        int ax = Math.abs(dx);
        int ay = Math.abs(dy);
        float angle = (ax+ay==0) ? 0 : (float) dy/(ax+ay);
        if(dx<0){
            angle = 2 - angle;
        } else if (dy<0) {
            angle = 4 + angle;
        }
        return angle*90.0f;
    }
    static boolean shouldContinue = true;
    static int maxY = Integer.MIN_VALUE;
    static int maxIdx = -1;
    static point maxPoint;

    public static void bruteForce(List<point> points, Stage stg, Group grp){
        stg.getScene().setOnKeyPressed(null);
        for(int i = 0 ; i < points.size() ; i++){
            for (int j = 0 ; j < points.size() ; j++){
                if(points.get(i)!=points.get(j)){
                    //draw temp line ij
                    for(int k = 0 ; k < points.size() ; k++){
                        shouldContinue = true;
                        if(points.get(k) != points.get(j) && points.get(k)!=points.get(i)){
                            //draw line jk
                            if(ccwSlope(points.get(i),points.get(j),points.get(k))==-1){
                                //remove line jk
                                shouldContinue = false;
                            }
                            //remove line ij
                            if(!shouldContinue){
                                break;
                            }
                        }
                    }
                }
                if(shouldContinue){
                    //perma line ij
                    Line l = new Line(points.get(i).x, points.get(i).y, points.get(j).x, points.get(j).y);
                    l.setStroke(Color.web("#008000"));
                    grp.getChildren().add(l);
                }
            }
        }
    }
    public static void jarvisMarch(List<point> points, Stage stg, Group grp){
        stg.getScene().setOnKeyPressed(null);
        findMaxY(points);
        points.set(maxIdx, points.get(0));
        points.set(0, maxPoint);
        System.out.println("max y point : " + points.get(maxIdx).y);
        for(int i = 0 ; i < points.size() ;i++){
            points.get(i).isOnHull=false;
        }

        for(int i = 0 ; i < points.size() ; i++){
            for (int j = 0 ; j < points.size() ; j++){
                System.out.println("angle:  " + myAngle(points.get(i), points.get(j)));
                points.get(i).angle = myAngle(points.get(i), points.get(j));
            }
            points.sort(Comparator.comparingDouble(point::getAngle));

//            for (int j = 0 ; j < points.size() ; j++){
//                System.out.println("point "+ j + ",  angle:" + points.get(j).getAngle());
//            }
//            for(int x = 0 ; x<points.size() ;x++){
//                if(!points.get(x).isOnHull){
//                    Line l = new Line(points.get(i).x, points.get(i).y, points.get(j).x, points.get(j).y);
//                    l.setStroke(Color.web("#008000"));
//                    grp.getChildren().add(l);
//                    break;
//                }
//            }
            Line l = new Line(points.get(0).x, points.get(0).y, points.get(1).x, points.get(1).y);
            l.setStroke(Color.web("#008000"));
            grp.getChildren().add(l);
        }

    }

    private static void findMaxY(List<point> points) {
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i).y > maxY) {
                maxY = points.get(i).y;
                maxIdx = i;
                maxPoint = points.get(i);
            }
        }
    }

    private static float myAngle(point a, point b){
        float dx = b.x - a.x;
        float dy = b.y - a.y;
        double angle = Math.atan2(dy,dx);
        if(Math.toDegrees(angle)<0){
            return (float) (360.0f + Math.toDegrees(angle));
        }else{
            return (float) Math.toDegrees(angle);
        }
    }
}
