package com.asher.convexhulls;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.LinkedList;

public class Main extends Application {
    int winW = 1100;
    int winH = 700;
    String[] options = {"Line Intersection (Slope of two lines)", "Line Intersection (Area between three points)", "Research Method Intersection","Brute Force", "Jarvis March", "Graham Scan", "Pairwise Elimination", "Research Method Hull"};
    ComboBox<String> oprs = new ComboBox<>(FXCollections.observableArrayList(options));
    GridPane gridPane = new GridPane(3,3);
    Button confirm = new Button("Launch");
    Scene selectOperation = new Scene(gridPane, (double) winW /2, (double) winH /2);
    Group group = new Group();
    Scene drawLines = new Scene(group,winW, winH);
    LinkedList<point> points = new LinkedList<>();
    int count = 0;
    @Override
    public void start(Stage stage){
        oprs.setPromptText("Select an algorithm");
        oprs.setPrefSize(300,10);

        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(oprs,1,1);
        gridPane.add(confirm,2,2);
        gridPane.setVgap(25);
        gridPane.setHgap(25);

        confirm.setOnMouseClicked(e->{
            try{
                switch (oprs.getValue()){
                    case "Line Intersection (Slope of two lines)":
                        drawLineIntersection("Line Intersection (Slope of two lines)",stage);
                        break;
                    case "Line Intersection (Area between three points)":
                        drawLineIntersection("Line Intersection (Area between three points)",stage);
                        break;
                    case "Brute Force":
                        System.out.println("bcd");
                        break;
                    case "Jarvis March":
                        System.out.println("efsedf");
                        break;
                    case "Pairwise Elimination":
                        System.out.println("ada");
                        break;
                }
            }catch (NullPointerException ignored){
            }
        });

        stage.setTitle("Convex Hull");
        stage.setScene(selectOperation);
        stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
    public void drawLineIntersection(String meth ,Stage stg){
        drawLines.setOnMouseClicked(e->{
            points.add( new point( (int) e.getX() , (int) e.getY() ) );
            if(count<4) group.getChildren().add(new Circle( points.get(count).x , points.get(count).y ,4.0f));
            helper(meth, drawLines,group,points);
        });

        stg.setScene(drawLines);
    }
    public void helper(String meth, Scene scn ,Group grp ,LinkedList<point> points){
        count++;
        if(count == 4){
            grp.getChildren().add(new Line(points.get(0).x,points.get(0).y, points.get(1).x, points.get(1).y));
            grp.getChildren().add(new Line(points.get(2).x,points.get(2).y, points.get(3).x, points.get(3).y));
            if(meth.equals("Line Intersection (Slope of two lines)")){
                if(ConvexHullUtil.doIntersectSlope(points.get(0),points.get(1),points.get(2),points.get(3))){
                    Text isec = new Text(430,650,"Lines DO intersect");
                    isec.setFont(Font.font("Arial", 24));
                    grp.getChildren().add(isec);
                    scn.setFill(Color.web("#8fdb91"));
                }else{
                    Text isec = new Text(430, 650, "Lines DO NOT intersect");
                    isec.setFont(Font.font("Arial", 24));
                    grp.getChildren().add(isec);
                    scn.setFill(Color.web("#f74f58"));
                }
            }else{
                if(ConvexHullUtil.doIntersectArea(points.get(0),points.get(1),points.get(2),points.get(3))){
                    Text isec = new Text(430,650,"Lines DO intersect");
                    isec.setFont(Font.font("Arial", 24));
                    grp.getChildren().add(isec);
                    scn.setFill(Color.web("#8fdb91"));
                }else{
                    Text isec = new Text(430, 650, "Lines DO NOT intersect");
                    isec.setFont(Font.font("Arial", 24));
                    grp.getChildren().add(isec);
                    scn.setFill(Color.web("#f74f58"));
                }
            }
            scn.setOnMouseClicked(null);
        }
    }
}