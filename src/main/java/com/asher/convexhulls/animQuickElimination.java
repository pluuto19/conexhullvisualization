package com.asher.convexhulls;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

public class animQuickElimination {
    static int maxY;
    static int maxYIdx = -1;
    static point maxYPoint;

    private static point findMaxY(List<point> points) {
        maxY = points.get(0).y;
        maxYIdx = 0;
        maxYPoint = points.get(0);
        for (int i = 1; i < points.size(); i++) {
            if (points.get(i).y > maxY) {
                maxY = points.get(i).y;
                maxYIdx = i;
                maxYPoint = points.get(i);
            }
        }
        return maxYPoint;
    }

    static int minY;
    static int minYIdx = -1;
    static point minYPoint;

    private static point findMinY(List<point> points) {
        minY = points.get(0).y;
        minYIdx = 0;
        minYPoint = points.get(0);
        for (int i = 1; i < points.size(); i++) {
            if (points.get(i).y < minY) {
                minY = points.get(i).y;
                minYIdx = i;
                minYPoint = points.get(i);
            }
        }
        return minYPoint;
    }

    static int maxX;
    static int maxXIdx = -1;
    static point maxXPoint;

    private static point findMaxX(List<point> points) {
        maxX = points.get(0).x;
        maxXIdx = 0;
        maxXPoint = points.get(0);
        for (int i = 1; i < points.size(); i++) {
            if (points.get(i).x > maxX) {
                maxX = points.get(i).x;
                maxXIdx = i;
                maxXPoint = points.get(i);
            }
        }
        return maxXPoint;
    }

    static int minX;
    static int minXIdx = -1;
    static point minXPoint;

    private static point findMinX(List<point> points) {
        minX = points.get(0).x;
        minXIdx = 0;
        minXPoint = points.get(0);
        for (int i = 1; i < points.size(); i++) {
            if (points.get(i).x < minX) {
                minX = points.get(i).x;
                minXIdx = i;
                minXPoint = points.get(i);
            }
        }
        return minXPoint;
    }

    static ArrayList<point> hull = new ArrayList<>();
    static ArrayList<point> region1 = new ArrayList<>();
    static ArrayList<point> region2 = new ArrayList<>();
    static ArrayList<point> region3 = new ArrayList<>();
    static ArrayList<point> region4 = new ArrayList<>();
    static point r1temp, r2temp, r3temp, r4temp;
    static point r1i, r2i, r3i, r4i;
    static point r1j, r2j, r3j, r4j;
    static int r1jItr = 2, r2jItr = 2, r3jItr = 2, r4jItr = 2;
    static int r1tempItr = 0, r2tempItr = 0, r3tempItr = 0, r4tempItr = 0;
    static Timeline tl = new Timeline();
    static Timeline tl2 = new Timeline();
    static Timeline tl3 = new Timeline();
    static Timeline tl4 = new Timeline();
    static Stack<Line> r1lines = new Stack<Line>();
    static Stack<Line> r2lines = new Stack<Line>();
    static Stack<Line> r3lines = new Stack<Line>();
    static Stack<Line> r4lines = new Stack<Line>();

    public static void findConvexHull(List<point> points, Stage stg, Group grp) {
        tl.setCycleCount(Timeline.INDEFINITE);
        tl2.setCycleCount(Timeline.INDEFINITE);
        tl3.setCycleCount(Timeline.INDEFINITE);
        tl4.setCycleCount(Timeline.INDEFINITE);

        stg.getScene().setOnKeyPressed(null);
        hull.add(findMinX(points));
        hull.add(findMinY(points));
        hull.add(findMaxX(points));
        hull.add(findMaxY(points));

        Line l1 = new Line(hull.get(0).x, hull.get(0).y, hull.get(1).x, hull.get(1).y);
        l1.setStroke(Color.web("#008000"));
        grp.getChildren().add(l1);
        Line l2 = new Line(hull.get(1).x, hull.get(1).y, hull.get(2).x, hull.get(2).y);
        l2.setStroke(Color.web("#008000"));
        grp.getChildren().add(l2);
        Line l3 = new Line(hull.get(2).x, hull.get(2).y, hull.get(3).x, hull.get(3).y);
        l3.setStroke(Color.web("#008000"));
        grp.getChildren().add(l3);
        Line l4 = new Line(hull.get(3).x, hull.get(3).y, hull.get(0).x, hull.get(0).y);
        l4.setStroke(Color.web("#008000"));
        grp.getChildren().add(l4);

        // all points are inside quad
        for (int i = 0; i < points.size(); i++) {
            points.get(i).flag = false;
        }
        // check if any point in region 1, remove it from quad
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i) != hull.get(0) && points.get(i) != hull.get(1)) {
                if (area(hull.get(0), hull.get(1), points.get(i)) < 0) {
                    points.get(i).flag = true;
                }
            }
        }
        // check if any point in region 2, remove it from quad
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i) != hull.get(1) && points.get(i) != hull.get(2)) {
                if (area(hull.get(1), hull.get(2), points.get(i)) < 0) {
                    points.get(i).flag = true;
                }
            }
        }
        // check if any point in region 3, remove it from quad
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i) != hull.get(2) && points.get(i) != hull.get(3)) {
                if (area(hull.get(2), hull.get(3), points.get(i)) < 0) {
                    points.get(i).flag = true;
                }
            }
        }
        // check if any point in region 4, remove it from quad
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i) != hull.get(3) && points.get(i) != hull.get(0)) {
                if (area(hull.get(3), hull.get(0), points.get(i)) < 0) {
                    points.get(i).flag = true;
                }
            }
        }
        // add to region 1 array
        region1.add(hull.get(0));
        region1.add(hull.get(1));
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i) != region1.get(0) && points.get(i) != region1.get(1) && points.get(i).flag && area(region1.get(0), region1.get(1), points.get(i)) < 0) {
                region1.add(points.get(i));
            }
        }
        region1.sort(Comparator.comparingInt(point::getX));
        // add to region 2 array
        region2.add(hull.get(1));
        region2.add(hull.get(2));
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i) != region1.get(0) && points.get(i) != region2.get(1) && points.get(i).flag && area(region2.get(0), region2.get(1), points.get(i)) < 0) {
                region2.add(points.get(i));
            }
        }
        region2.sort(Comparator.comparingInt(point::getX));
        // add to region 3 array
        region3.add(hull.get(2));
        region3.add(hull.get(3));
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i) != region3.get(0) && points.get(i) != region3.get(1) && points.get(i).flag && area(region3.get(0), region3.get(1), points.get(i)) < 0) {
                region3.add(points.get(i));
            }
        }
        region3.sort(Comparator.comparingInt(point::getX).reversed());
        // add to region 4 array
        region4.add(hull.get(3));
        region4.add(hull.get(0));
        for (int i = 0; i < points.size(); i++) {
            if (points.get(i) != region4.get(0) && points.get(i) != region4.get(1) && points.get(i).flag && area(region4.get(0), region4.get(1), points.get(i)) < 0) {
                region4.add(points.get(i));
            }
        }
        region4.sort(Comparator.comparingInt(point::getX).reversed());

        if (region1.size() > 3) {
            System.out.println("called1");
            r1jItr = 2;
            r1tempItr = 0;
            r1temp = region1.get(r1tempItr);
            r1i = region1.get(1);
            r1j = region1.get(r1jItr);
            KeyFrame kf = new KeyFrame(Duration.seconds(0.2), actionEvent -> {
                if (area(r1temp, r1i, r1j) >= 0) {
                    drawLine(r1temp, r1i, grp, 1);
                    drawLine(r1i, r1j, grp, 1);
                    r1tempItr++;
                    r1temp = region1.get(r1tempItr);
                    r1i = r1j;
                    r1jItr++;
                    r1j = region1.get(r1jItr);
                } else {
                    region1.remove(r1i);
                    if (r1tempItr - 1 < 0) {
                        r1tempItr = 0;
                        r1i = r1j;
                        r1j = region1.get(r1jItr);
                        drawLine(r1temp, r1j, grp, 1);
                    } else {
                        grp.getChildren().remove(r1lines.pop());
                        r1jItr--;
                        r1i = region1.get(r1tempItr);
                        r1tempItr--;
                        r1temp = region1.get(r1tempItr);
                    }
                }
                if (r1j == region1.get(region1.size() - 1)) {
                    if (area(r1temp, r1i, r1j) < 0) {
                        region1.remove(r1i);
                        grp.getChildren().remove(r1lines.pop());
                        drawLine(r1temp, r1j, grp, 1);
                    }else{
                        drawLine(r1i, r1j, grp, 1);
                    }
                    grp.getChildren().remove(l1);
                    tl.stop();
                    tl2.play();
                }
            });
            tl.getKeyFrames().add(kf);
        }
        //get hull of region2
        if (region2.size() > 3) {
            System.out.println("called2");
            r2jItr = 2;
            r2tempItr = 0;
            r2temp = region2.get(r2tempItr);
            r2i = region2.get(1);
            r2j = region2.get(r2jItr);
            KeyFrame kf = new KeyFrame(Duration.seconds(0.2), actionEvent -> {
                if (area(r2temp, r2i, r2j) >= 0) {
                    drawLine2(r2temp, r2i, grp, 2);
                    drawLine2(r2i, r2j, grp, 2);
                    r2tempItr++;
                    r2temp = region2.get(r2tempItr);
                    r2i = r2j;
                    r2jItr++;
                    r2j = region2.get(r2jItr);
                } else {
                    region2.remove(r2i);
                    if (r2tempItr - 1 < 0) {
                        r2tempItr = 0;
                        r2i = r2j;
                        r2j = region2.get(r2jItr);
                        drawLine2(r2temp, r2j, grp, 2);
                    } else {
                        grp.getChildren().remove(r2lines.pop());
                        r2jItr--;
                        r2i = region2.get(r2tempItr);
                        r2tempItr--;
                        r2temp = region2.get(r2tempItr);
                    }
                }
                if (r2j == region2.get(region2.size() - 1)) {
                    if (area(r2temp, r2i, r2j) < 0) {
                        region2.remove(r2i);
                        grp.getChildren().remove(r2lines.pop());
                        drawLine2(r2temp, r2j, grp, 2);
                    } else {
                        drawLine2(r2i, r2j, grp, 2);
                    }
                    grp.getChildren().remove(l2);
                    tl2.stop();
                    tl3.play();
                }
            });
            tl2.getKeyFrames().add(kf);
        }

        if (region3.size() > 3) {
            System.out.println("called3");
            r3jItr = 2;
            r3tempItr = 0;
            r3temp = region3.get(r3tempItr);
            r3i = region3.get(1);
            r3j = region3.get(r3jItr);
            KeyFrame kf = new KeyFrame(Duration.seconds(0.2), actionEvent -> {
                if (area(r3temp, r3i, r3j) >= 0) {
                    drawLine3(r3temp, r3i, grp, 3);
                    drawLine3(r3i, r3j, grp, 3);
                    r3tempItr++;
                    r3temp = region3.get(r3tempItr);
                    r3i = r3j;
                    r3jItr++;
                    r3j = region3.get(r3jItr);
                } else {
                    region3.remove(r3i);
                    if (r3tempItr - 1 < 0) {
                        r3tempItr = 0;
                        r3i = r3j;
                        r3j = region3.get(r3jItr);
                        drawLine3(r3temp, r3j, grp, 3);
                    } else {
                        grp.getChildren().remove(r3lines.pop());
                        r3jItr--;
                        r3i = region3.get(r3tempItr);
                        r3tempItr--;
                        r3temp = region3.get(r3tempItr);
                    }
                }
                if (r3j == region3.get(region3.size() - 1)) {
                    if (area(r3temp, r3i, r3j) < 0) {
                        region3.remove(r3i);
                        grp.getChildren().remove(r3lines.pop());
                        drawLine3(r3temp, r3j, grp, 3);
                    } else {
                        drawLine3(r3i, r3j, grp, 3);
                    }
                    grp.getChildren().remove(l3);
                    tl3.stop();
                    tl4.play();
                }
            });
            tl3.getKeyFrames().add(kf);
        }

        if (region4.size() > 3) {
            System.out.println("called4");
            r4jItr = 2;
            r4tempItr = 0;
            r4temp = region4.get(r4tempItr);
            r4i = region4.get(1);
            r4j = region4.get(r4jItr);
            KeyFrame kf = new KeyFrame(Duration.seconds(0.2), actionEvent -> {
                if (area(r4temp, r4i, r4j) >= 0) {
                    drawLine4(r4temp, r4i, grp, 4);
                    drawLine4(r4i, r4j, grp, 4);
                    r4tempItr++;
                    r4temp = region4.get(r4tempItr);
                    r4i = r4j;
                    r4jItr++;
                    r4j = region4.get(r4jItr);
                } else {
                    region4.remove(r4i);
                    if (r4tempItr - 1 < 0) {
                        r4tempItr = 0;
                        r4i = r4j;
                        r4j = region4.get(r4jItr);
                        drawLine4(r4temp, r4j, grp, 4);
                    } else {
                        grp.getChildren().remove(r4lines.pop());
                        r4jItr--;
                        r4i = region4.get(r4tempItr);
                        r4tempItr--;
                        r4temp = region4.get(r4tempItr);
                    }
                }
                if (r4j == region4.get(region4.size() - 1)) {
                    if (area(r4temp, r4i, r4j) < 0) {
                        region4.remove(r4i);
                        grp.getChildren().remove(r4lines.pop());
                        drawLine4(r4temp, r4j, grp, 4);
                    } else {
                        drawLine4(r4i, r4j, grp, 4);
                    }
                    grp.getChildren().remove(l4);
                    tl4.stop();
                }
            });
            tl4.getKeyFrames().add(kf);
        }

        tl.play();
    }

    private static int area(point a, point b, point c) {
        return ((b.x - a.x) * (c.y - a.y)) - ((b.y - a.y) * (c.x - a.x));
    }
    private static void drawLine(point a, point b, Group grp, int region) {
        if(r1lines.isEmpty()){
            System.out.println("adding line: " + a.name + ", " + b.name);
            Line l1 = new Line(a.x, a.y, b.x, b.y);
            l1.setStroke(Color.web("#008000"));
            grp.getChildren().add(l1);
            if (region == 1) {
                r1lines.push(l1);
            } else if (region == 2) {
                r2lines.push(l1);
            } else if (region == 3) {
                r3lines.push(l1);
            } else if (region == 4) {
                r4lines.push(l1);
            }
        }
        if((a.x!=r1lines.peek().getStartX() && a.y!=r1lines.peek().getStartY()) || (b.x!=r1lines.peek().getEndX() && b.y!=r1lines.peek().getEndY()) ){
            System.out.println("adding line: " + a.name + ", " + b.name);
            Line l1 = new Line(a.x, a.y, b.x, b.y);
            l1.setStroke(Color.web("#008000"));
            grp.getChildren().add(l1);
            if (region == 1) {
                r1lines.push(l1);
            } else if (region == 2) {
                r2lines.push(l1);
            } else if (region == 3) {
                r3lines.push(l1);
            } else if (region == 4) {
                r4lines.push(l1);
            }
        }
    }
    private static void drawLine2(point a, point b, Group grp, int region) {
        if(r2lines.isEmpty()){
            System.out.println("adding line: " + a.name + ", " + b.name);
            Line l1 = new Line(a.x, a.y, b.x, b.y);
            l1.setStroke(Color.web("#008000"));
            grp.getChildren().add(l1);
            if (region == 1) {
                r1lines.push(l1);
            } else if (region == 2) {
                r2lines.push(l1);
            } else if (region == 3) {
                r3lines.push(l1);
            } else if (region == 4) {
                r4lines.push(l1);
            }
        }
        if((a.x!=r2lines.peek().getStartX() && a.y!=r2lines.peek().getStartY()) || (b.x!=r2lines.peek().getEndX() && b.y!=r2lines.peek().getEndY()) ){
            System.out.println("adding line: " + a.name + ", " + b.name);
            Line l1 = new Line(a.x, a.y, b.x, b.y);
            l1.setStroke(Color.web("#008000"));
            grp.getChildren().add(l1);
            if (region == 1) {
                r1lines.push(l1);
            } else if (region == 2) {
                r2lines.push(l1);
            } else if (region == 3) {
                r3lines.push(l1);
            } else if (region == 4) {
                r4lines.push(l1);
            }
        }
    }
    private static void drawLine3(point a, point b, Group grp, int region) {
        if(r3lines.isEmpty()){
            System.out.println("adding line: " + a.name + ", " + b.name);
            Line l1 = new Line(a.x, a.y, b.x, b.y);
            l1.setStroke(Color.web("#008000"));
            grp.getChildren().add(l1);
            if (region == 1) {
                r1lines.push(l1);
            } else if (region == 2) {
                r2lines.push(l1);
            } else if (region == 3) {
                r3lines.push(l1);
            } else if (region == 4) {
                r4lines.push(l1);
            }
        }
        if((a.x!=r3lines.peek().getStartX() && a.y!=r3lines.peek().getStartY()) || (b.x!=r3lines.peek().getEndX() && b.y!=r3lines.peek().getEndY()) ){
            System.out.println("adding line: " + a.name + ", " + b.name);
            Line l1 = new Line(a.x, a.y, b.x, b.y);
            l1.setStroke(Color.web("#008000"));
            grp.getChildren().add(l1);
            if (region == 1) {
                r1lines.push(l1);
            } else if (region == 2) {
                r2lines.push(l1);
            } else if (region == 3) {
                r3lines.push(l1);
            } else if (region == 4) {
                r4lines.push(l1);
            }
        }
    }
    private static void drawLine4(point a, point b, Group grp, int region) {
        if(r4lines.isEmpty()){
            System.out.println("adding line: " + a.name + ", " + b.name);
            Line l1 = new Line(a.x, a.y, b.x, b.y);
            l1.setStroke(Color.web("#008000"));
            grp.getChildren().add(l1);
            if (region == 1) {
                r1lines.push(l1);
            } else if (region == 2) {
                r2lines.push(l1);
            } else if (region == 3) {
                r3lines.push(l1);
            } else if (region == 4) {
                r4lines.push(l1);
            }
        }
        if((a.x!=r4lines.peek().getStartX() && a.y!=r4lines.peek().getStartY()) || (b.x!=r4lines.peek().getEndX() && b.y!=r4lines.peek().getEndY()) ){
            System.out.println("adding line: " + a.name + ", " + b.name);
            Line l1 = new Line(a.x, a.y, b.x, b.y);
            l1.setStroke(Color.web("#008000"));
            grp.getChildren().add(l1);
            if (region == 1) {
                r1lines.push(l1);
            } else if (region == 2) {
                r2lines.push(l1);
            } else if (region == 3) {
                r3lines.push(l1);
            } else if (region == 4) {
                r4lines.push(l1);
            }
        }
    }
}
