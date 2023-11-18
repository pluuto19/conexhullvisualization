package com.asher.convexhulls;

import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.HashSet;


class testBruteforce extends Application {
    ArrayList<point> point;
    ArrayList<Line> lines;
    Pane pane;
    Canvas canvas;
    GraphicsContext gc;
    Text t1, t2, t3, t4, t5, t6, t7, t8, t9, t10, t11, t12;
    Button b1;
    long initialtime, finaltime, timecomplexity;
    long initialmemoryusage, finalmemoryusage, memorycomplexity;

    @Override
    public void start(Stage stage) throws Exception {
        pane = new Pane();
        canvas = new Canvas(650, 500);
        gc = canvas.getGraphicsContext2D();
        pane.getChildren().add(canvas);

        Scene scene = new Scene(pane, 650, 1000);

        stage.setTitle("Brute Force Convex Hull");
        scene.setFill(Color.LIGHTGRAY);

        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, 650, 500);

        t1 = new Text("Before Execution time");
        t1.setX(10);
        t1.setY(525);
        t3 = new Text("After Execution time");
        t3.setX(10);
        t3.setY(600);
        t5 = new Text("Time complexity");
        t5.setX(10);
        t5.setY(675);
        t7 = new Text("Initial Memory Usage");
        t7.setX(10);
        t7.setY(750);
        t9 = new Text("Final Memory Usage");
        t9.setX(10);
        t9.setY(825);
        t11 = new Text("Memory Complexity");
        t11.setX(10);
        t11.setY(900);

        b1 = new Button("Run");
        b1.setLayoutX(300);
        b1.setLayoutY(950);
        b1.setPrefWidth(90);
        b1.setPrefHeight(30);
        b1.setOnAction(event -> run(event));

        Button clearButton = new Button("Clear");
        clearButton.setLayoutX(400);
        clearButton.setLayoutY(950);
        clearButton.setPrefWidth(90);
        clearButton.setPrefHeight(30);
        clearButton.setOnAction(event -> {
            pane.getChildren().removeIf(node -> node instanceof Circle || node instanceof Text && "dots".equals(node.getId()));
            pane.getChildren().removeIf(node -> node instanceof Line);
            lines.clear();
            gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
            pane.getChildren().remove(t2);
            pane.getChildren().remove(t4);
            pane.getChildren().remove(t6);
            pane.getChildren().remove(t8);
            pane.getChildren().remove(t10);
            pane.getChildren().remove(t12);
            point.clear();
        });

        point = new ArrayList<>();
        pane.setOnMouseClicked(event -> {
            double x = event.getX();
            double y = event.getY();


            point p = new point((int) x, (int) y);
            if (isPointWithinCanvas(p)) {
                point.add(new point((int) x, (int) y));
                givedotscordinates(point, "p" + point.size(), point.size() - 1);
            }
        });

        pane.getChildren().add(t1);
        pane.getChildren().add(t3);
        pane.getChildren().add(t5);
        pane.getChildren().add(t7);
        pane.getChildren().add(t9);
        pane.getChildren().add(t11);
        pane.getChildren().add(b1);
        pane.getChildren().add(clearButton);

        stage.setScene(scene);
        stage.show();

    }

    public void run(ActionEvent event) {
        initialmemoryusage = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        initialtime = System.currentTimeMillis();
        t2 = new Text(String.valueOf(initialtime) + " miliseconds");
        t2.setX(200);
        t2.setY(525);
        pane.getChildren().add(t2);

        t8 = new Text(String.valueOf(initialmemoryusage));
        t8.setX(200);
        t8.setY(750);
        pane.getChildren().add(t8);
        findConvexHull(point);
//        point p0 = getLowestPoint(convexHull);
//        convexHull.sort((p1,p2) -> comparepoint(p0,p1,p2));
//        for (int i = 0; i < convexHull.size(); i++) {
//            point p1 = convexHull.get(i);
//            point p2 = convexHull.get((i + 1) % convexHull.size());
//            drawLine(p1, p2);
//        }


    }

    public boolean isPointWithinCanvas(point point) {
        double canvasWidth = canvas.getWidth();
        double canvasHeight = canvas.getHeight();
        return point.getX() >= 0 && point.getX() <= canvasWidth && point.getY() >= 0 && point.getY() <= canvasHeight;
    }

    public void givedotscordinates(ArrayList<point> p, String label, int i) {
        createdots(p.get(i).getX(), p.get(i).getY(), label);
    }

    public void createdots(double x, double y, String label) {
        Circle dots = new Circle(x, y, 5);
        dots.setFill(javafx.scene.paint.Color.RED);
        dots.setStroke(javafx.scene.paint.Color.BLACK);

        String formattedX = String.format("%.2f", x);
        String formattedY = String.format("%.2f", y);

        Text text = new Text(label + "(" + formattedX + "," + formattedY + ")");
        text.setId("dots");
        text.setX(x - 10);
        text.setY(y - 10);
        pane.getChildren().add(dots);
        pane.getChildren().add(text);

    }

    public void findConvexHull(ArrayList<point> point) {
        int n = point.size();
        if (n < 3) {
            return;
        }
        ArrayList<point> convexhull = new ArrayList<>();
        boolean onsameside = true;

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (!point.get(i).equals(point.get(j))) {
                    onsameside = true;
                    for (int k = 0; k < n; k++) {
                        if (!point.get(i).equals(point.get(k)) && !point.get(j).equals(point.get(k))) {
                            double cross = crossproduct(point.get(i), point.get(j), point.get(k));
                            if (cross >= 0) {
                                onsameside = false;
                                break;
                            }
                        }

                    }
                    if (onsameside) {
                        if (!convexhull.contains(point.get(i))) {
                            convexhull.add(point.get(i));
                        }
                        if (!convexhull.contains(point.get(j))) {
                            convexhull.add(point.get(j));
                        }
                    }
                }

            }
        }


        for (int i = 0; i < convexhull.size(); i++) {
            point p1 = convexhull.get(i);
            point p2 = convexhull.get((i + 1) % convexhull.size());

            drawLine(p1, p2);
        }

    }

    private double crossproduct(point p, point q, point r) {
        double pqx = p.getX() - q.getX();
        double pqy = p.getY() - q.getY();
        double qrx = r.getX() - q.getX();
        double qry = r.getY() - q.getY();

        return (pqx * qry) - (pqy * qrx);
    }
    private void drawLine(point p1, point p2) {
        gc.setStroke(Color.BLUE);
        gc.setLineWidth(2);
        gc.strokeLine(p1.getX(), p1.getY(), p2.getX(), p2.getY());
}
}
