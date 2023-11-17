package com.asher.convexhulls;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.List;

public class BruteForce {
    static boolean shouldContinue = true;
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
                            if(ConvexHullUtil.ccwSlope(points.get(i),points.get(j),points.get(k))==-1){
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
}
