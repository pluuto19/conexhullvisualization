package com.asher.convexhulls;

public class point {
    String name;
    int x;
    int y;
    float angle;
    boolean isFirstPointOnHull;
    boolean isOnHull;
    point(int x,int y){
        this.x=x;
        this.y=y;
    }
    point(){}
    public void setX(int x) {
        this.x = x;
    }
    public void setY(int y) {
        this.y = y;
    }
    public float getAngle(){
        return angle;
    }
}
