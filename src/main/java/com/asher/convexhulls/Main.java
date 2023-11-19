package com.asher.convexhulls;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
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
    String[] options = {"Line Intersection (Slope of two lines)", "Line Intersection (Area between three points)", "Research Method Intersection", "Brute Force", "Jarvis March", "Graham Scan", "Quick Elimination", "Research Method Hull"};
    ComboBox<String> oprs = new ComboBox<>(FXCollections.observableArrayList(options));
    GridPane gridPane = new GridPane(3, 3);
    Button confirm = new Button("Launch");
    Scene selectOperation = new Scene(gridPane, (double) winW / 2, (double) winH / 2);
    Group group = new Group();
    Group convexGroup = new Group();
    Scene drawLines = new Scene(group, winW, winH);
    Scene pointsCanvas = new Scene(convexGroup, winW, winH);
    LinkedList<point> points = new LinkedList<>();
    int count = 0;
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        oprs.setPromptText("Select an algorithm");
        oprs.setPrefSize(300, 10);

        gridPane.setAlignment(Pos.CENTER);
        gridPane.add(oprs, 1, 1);
        gridPane.add(confirm, 2, 2);
        gridPane.setVgap(25);
        gridPane.setHgap(25);

        confirm.setOnMouseClicked(e -> {
            try {
                switch (oprs.getValue()) {
                    case "Line Intersection (Slope of two lines)":
                        drawLineIntersection("Line Intersection (Slope of two lines)", stage);
                        break;
                    case "Line Intersection (Area between three points)":
                        drawLineIntersection("Line Intersection (Area between three points)", stage);
                        break;
                    case "Brute Force":
                        drawConvexHull("Brute Force", stage);
                        break;
                    case "Jarvis March":
                        drawConvexHull("Jarvis March", stage);
                        break;
                    case "Graham Scan":
                        drawConvexHull("Graham Scan", stage);
                    case "Quick Elimination":
                        drawConvexHull("Quick Elimination", stage);
                        break;
                }
            } catch (NullPointerException ignored) {
            }
        });

        stage.setTitle("Geometric Algorithms");
        stage.setScene(selectOperation);
        stage.show();
    }

    public void drawLineIntersection(String meth, Stage stg) {
        drawLines.setFill(Color.web("#000000"));
        drawLines.setOnMouseClicked(e -> {
            points.add(new point((int) e.getX(), (int) e.getY()));
            Circle newCircle = new Circle(points.get(count).x, points.get(count).y, 4.0f, Color.web("#FF0000"));
            Label newLabel = new Label("p" + count);
            newLabel.setLayoutX(points.get(count).x - 25);
            newLabel.setLayoutY(points.get(count).y - 25);
            newLabel.setFont(Font.font("Consolas", 15));
            newLabel.setTextFill(Color.web("#FFFFFF"));
            group.getChildren().add(newCircle);
            group.getChildren().add(newLabel);
            count++;
            helper(meth, drawLines, group, points);
        });

        stg.setScene(drawLines);
    }

    public void helper(String meth, Scene scn, Group grp, LinkedList<point> points) {
        if (count == 4) {
            Line l1 = new Line(points.get(0).x, points.get(0).y, points.get(1).x, points.get(1).y);
            l1.setStroke(Color.web("#008000"));
            Line l2 = new Line(points.get(2).x, points.get(2).y, points.get(3).x, points.get(3).y);
            l2.setStroke(Color.web("#008000"));
            grp.getChildren().add(l1);
            grp.getChildren().add(l2);
            if (meth.equals("Line Intersection (Slope of two lines)")) {
                if (ConvexHullUtil.doIntersectSlope(points.get(0), points.get(1), points.get(2), points.get(3))) {
                    Label isec = new Label("Lines DO intersect");
                    isec.setLayoutX(430);
                    isec.setLayoutY(650);
                    isec.setFont(Font.font("Arial", 24));
                    isec.setTextFill(Color.web("#008000"));
                    grp.getChildren().add(isec);
//                    scn.setFill(Color.web("#8fdb91"));
                } else {
                    Label isec = new Label("Lines DO NOT intersect");
                    isec.setLayoutX(430);
                    isec.setLayoutY(650);
                    isec.setFont(Font.font("Arial", 24));
                    isec.setTextFill(Color.web("#FF0000"));
                    grp.getChildren().add(isec);
//                    scn.setFill(Color.web("#f74f58"));
                }
            } else {
                if (ConvexHullUtil.doIntersectArea(points.get(0), points.get(1), points.get(2), points.get(3))) {
                    Label isec = new Label("Lines DO intersect");
                    isec.setLayoutX(430);
                    isec.setLayoutY(650);
                    isec.setFont(Font.font("Arial", 24));
                    isec.setTextFill(Color.web("#008000"));
                    grp.getChildren().add(isec);
//                    scn.setFill(Color.web("#8fdb91"));
                } else {
                    Label isec = new Label("Lines DO NOT intersect");
                    isec.setLayoutX(430);
                    isec.setLayoutY(650);
                    isec.setFont(Font.font("Arial", 24));
                    isec.setTextFill(Color.web("#FF0000"));
                    grp.getChildren().add(isec);
//                    scn.setFill(Color.web("#f74f58"));
                }
            }
            scn.setOnMouseClicked(null);
        }
    }

    public void drawConvexHull(String meth, Stage stg) {
        pointsCanvas.setFill(Color.web("#000000"));
        pointsCanvas.setOnMouseClicked(e->{
            points.add(new point((int) e.getX(), (int) e.getY()));
            Label newLabel = new Label("p" + count);
            points.get(count).name = "p" + count;
            convexGroup.getChildren().add(new Circle(points.get(count).x, points.get(count).y, 3.0f, Color.web("#FF0000")));
            newLabel.setLayoutX(points.get(count).x - 25);
            newLabel.setLayoutY(points.get(count).y - 25);
            newLabel.setFont(Font.font("Consolas", 10));
            newLabel.setTextFill(Color.web("#FFFFFF"));
            convexGroup.getChildren().add(newLabel);
            count++;
        });
        pointsCanvas.setOnKeyPressed(e->{
            if(e.getCode() == KeyCode.ENTER){
                pointsCanvas.setOnMouseClicked(null);
                switch (meth){
                    case "Brute Force":
                        System.out.println("called");
                        testBruteForce.bruteForce(points, stg, convexGroup);
                        System.out.println("about to break");
                        break;
                    case "Jarvis March":
                        JarvisMarch.jarvisMarch(points, stg, convexGroup);
                        break;
                    case "Graham Scan":
                        GrahamScan.grahamScan(points, stg, convexGroup);
                        break;
                    case "Quick Elimination":
                        QuickElimination.findConvexHull(points,stg, convexGroup);
                        break;
                    case "Research Paper":
                        break;
                }
            }
        });
        stg.setScene(pointsCanvas);
    }
}