package com.asher.convexhulls;

public class point {
    boolean isLeft;
    String name;
    int x;
    int y;
    float angle;
    int area;
    boolean isFirstPointOnHull;
    boolean isOnHull;
    boolean flag;

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
    public String getName() {
        return name;
    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
