package com.asher.convexhulls;

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

}
