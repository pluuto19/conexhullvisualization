package com.asher.convexhulls;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.LinkedList;

public class Main extends Application {
    long startTime, elapsedTime;
    float memBefore, memAfter, memUsed;
    int winW = 1100;
    int winH = 700;
    String[] options = {"Line Intersection (Slope of two lines)", "Line Intersection (Area between three points)", "Line Intersection (Sweep Line)", "Brute Force", "Jarvis March", "Graham Scan", "Quick Elimination", "Quick Hull"};
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
                    case "Line Intersection (Sweep Line)":
                        drawLineIntersection("Line Intersection (Sweep Line)", stage);
                        break;
                    case "Brute Force":
                        drawConvexHull("Brute Force", stage);
                        break;
                    case "Jarvis March":
                        drawConvexHull("Jarvis March", stage);
                        break;
                    case "Graham Scan":
                        drawConvexHull("Graham Scan", stage);
                        break;
                    case "Quick Elimination":
                        drawConvexHull("Quick Elimination", stage);
                        break;
                    case "Quick Hull":
                        drawConvexHull("Quick Hull", stage);
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
        drawGraphOnCanvas(group);
        drawLines.setOnMouseClicked(e -> {
            points.add(new point((int) e.getX(), (int) e.getY()));
            Circle newCircle = new Circle(points.get(count).x, points.get(count).y, 4.0f, Color.web("#FF0000"));
            Label newLabel = new Label(e.getX() + " , " + e.getY() );
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
            scn.setOnMouseClicked(null);
            startTime = System.nanoTime();
            memBefore = (float) Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
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
            } else if (meth.equals("Line Intersection (Area between three points)")) {
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
            } else {
                points.get(0).name = "l1";
                points.get(1).name = "l1";
                points.get(2).name = "l2";
                points.get(3).name = "l2";
                if (ConvexHullUtil.doIntersectSweepLine(points.get(0), points.get(1), points.get(2), points.get(3), Arrays.asList(points.get(0), points.get(1), points.get(2), points.get(3)))) {
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
            elapsedTime = System.nanoTime() - startTime;
            memAfter = (float) Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            memUsed = memAfter - memBefore;
            System.out.println("Time in milliseconds: " + (float) elapsedTime / 1000000);
            System.out.println("Memory used in Kilobytes: " + memUsed);
            displayComplexities((float) elapsedTime / 1000000, (int) memUsed);
        }
    }
    public void drawConvexHull(String meth, Stage stg) {
        pointsCanvas.setFill(Color.web("#000000"));
        drawGraphOnCanvas(convexGroup);
        pointsCanvas.setOnMouseClicked(e -> {
            points.add(new point((int) e.getX(), (int) e.getY()));
            Label newLabel = new Label(e.getX() + " , " + e.getY());
            points.get(count).name = e.getX() + " , " + e.getY();
            convexGroup.getChildren().add(new Circle(points.get(count).x, points.get(count).y, 3.0f, Color.web("#FF0000")));
            newLabel.setLayoutX(points.get(count).x - 25);
            newLabel.setLayoutY(points.get(count).y - 25);
            newLabel.setFont(Font.font("Consolas", 13));
            newLabel.setTextFill(Color.web("#FFFFFF"));
            convexGroup.getChildren().add(newLabel);
            count++;
        });
        pointsCanvas.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER) {
                pointsCanvas.setOnMouseClicked(null);
                switch (meth) {
                    case "Brute Force":
                        startTime = System.nanoTime();
                        memBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                        BruteForce.bruteForce(points, stg, convexGroup);
                        elapsedTime = System.nanoTime() - startTime;
                        memAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                        memUsed = memAfter - memBefore;
                        System.out.println("Time in milliseconds: " + (float) elapsedTime / 1000000);
                        System.out.println("Memory used in Kilobytes: " + memUsed / 1024);
                        displayComplexities((float) elapsedTime / 1000000, (int) memUsed);
                        break;
                    case "Jarvis March":
                        startTime = System.nanoTime();
                        memBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                        JarvisMarch.jarvisMarch(points, stg, convexGroup);
                        elapsedTime = System.nanoTime() - startTime;
                        memAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                        memUsed = memAfter - memBefore;
                        System.out.println("Time in milliseconds: " + (float) elapsedTime / 1000000);
                        System.out.println("Memory used in Kilobytes: " + memUsed / 1024);
                        displayComplexities((float) elapsedTime / 1000000, (int) memUsed);
                        break;
                    case "Graham Scan":
                        startTime = System.nanoTime();
                        memBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                        GrahamScan.grahamScan(points, stg, convexGroup);
                        elapsedTime = System.nanoTime() - startTime;
                        memAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                        memUsed = memAfter - memBefore;
                        System.out.println("Time in milliseconds: " + (float) elapsedTime / 1000000);
                        System.out.println("Memory used in Kilobytes: " + memUsed / 1024);
                        displayComplexities((float) elapsedTime / 1000000, (int) memUsed);
                        break;
                    case "Quick Elimination":
                        startTime = System.nanoTime();
                        memBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                        QuickElimination.findConvexHull(points, stg, convexGroup);
                        elapsedTime = System.nanoTime() - startTime;
                        memAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                        memUsed = memAfter - memBefore;
                        System.out.println("Time in milliseconds: " + (float) elapsedTime / 1000000);
                        System.out.println("Memory used in Kilobytes: " + memUsed / 1024);
                        displayComplexities((float) elapsedTime / 1000000, (int) memUsed);
                        break;
                    case "Quick Hull":
                        startTime = System.nanoTime();
                        memBefore = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                        QuickHull.quickHull(points, stg, convexGroup);
                        elapsedTime = System.nanoTime() - startTime;
                        memAfter = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
                        memUsed = memAfter - memBefore;
                        System.out.println("Time in milliseconds: " + (float) elapsedTime / 1000000);
                        System.out.println("Memory used in Kilobytes: " + memUsed / 1024);
                        displayComplexities((float) elapsedTime / 1000000, (int) memUsed);
                        break;
                }
            }
        });
        stg.setScene(pointsCanvas);
    }
    private void displayComplexities(float time, int space) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Complexities");
        alert.setHeaderText(null);
        alert.setContentText(null);
        TextArea textArea;
        if (space == 0) {
            textArea = new TextArea("Execution Time (millisecond) : " + time + "\n\nJVM ran the Garbage Collector");
        } else {
            textArea = new TextArea("Execution Time (millisecond) : " + time + "\n\nMemory Taken (bytes): " + space);
        }

        textArea.setEditable(false);
        textArea.setWrapText(true);

        alert.getDialogPane().setExpandableContent(textArea);
        alert.getDialogPane().setExpanded(true);

        alert.getButtonTypes().setAll(ButtonType.OK);
        alert.showAndWait();
    }
    private void drawGraphOnCanvas(Group grp){

        for(int y = -25 ; y <= winH ; y+=75){
            Label newLabel = new Label(Integer.toString(y));
            newLabel.setFont(Font.font("Consolas", 11));
            newLabel.setTextFill(Color.web("#FFFFFF"));
            newLabel.setLayoutX(5);
            newLabel.setLayoutY(y + 5);

            Line newLine = new Line();
            newLine.setStroke(Color.web("#949494"));
            newLine.setStartX(0);
            newLine.setStartY(y);
            newLine.setEndX(winW);
            newLine.setEndY(y);
            grp.getChildren().add(newLine);
            grp.getChildren().add(newLabel);
        }

        for(int x = -25 ; x <= winW ; x+=75){
            Label newLabel = new Label(Integer.toString(x));
            newLabel.setFont(Font.font("Consolas", 11));
            newLabel.setTextFill(Color.web("#FFFFFF"));
            newLabel.setLayoutX(x + 5);
            newLabel.setLayoutY(5);

            Line newLine = new Line();
            newLine.setStroke(Color.web("#949494"));
            newLine.setStartX(x);
            newLine.setStartY(0);
            newLine.setEndX(x);
            newLine.setEndY(winH);
            grp.getChildren().add(newLine);
            grp.getChildren().add(newLabel);
        }
    }
}