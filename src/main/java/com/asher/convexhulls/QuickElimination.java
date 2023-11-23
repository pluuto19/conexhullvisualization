package com.asher.convexhulls;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class QuickElimination {
    static point temp;
    static point i;
    static point j;
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
    static int jItr;
    static int tempItr;

    public static void findConvexHull(List<point> points, Stage stg, Group grp) {
        System.out.println("inserted2");
        stg.getScene().setOnKeyPressed(null);
        hull.add(findMinX(points));
        hull.add(findMinY(points));
        hull.add(findMaxX(points));
        hull.add(findMaxY(points));

        drawLine(hull.get(0), hull.get(1), grp);
        drawLine(hull.get(1), hull.get(2), grp);
        drawLine(hull.get(2), hull.get(3), grp);
        drawLine(hull.get(3), hull.get(0), grp);

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

        //get hull of region1
        if (region1.size() > 3) {
            jItr = 2;
            tempItr = 0;
            temp = region1.get(tempItr);
            i = region1.get(1);
            j = region1.get(jItr);
            while (j != region1.get(region1.size() - 1)) {
                if (area(temp, i, j) >= 0) {
                    tempItr++;
                    temp = region1.get(tempItr);
                    i = j;
                    jItr++;
                    j = region1.get(jItr);
                } else {
                    region1.remove(i);
                    if (tempItr - 1 < 0) {
                        tempItr = 0;
                        i = j;
                        j = region1.get(jItr);
                    } else {
                        jItr--;
                        i = region1.get(tempItr);
                        tempItr--;
                        temp = region1.get(tempItr);
                    }
                }
            }
            if (area(temp, i, j) < 0) {
                region1.remove(i);
            }
        }
        //get hull of region2
        if (region2.size() > 3) {
            jItr = 2;
            tempItr = 0;
            temp = region2.get(tempItr);
            i = region2.get(1);
            j = region2.get(jItr);
            while (j != region2.get(region2.size() - 1)) {
                if (area(temp, i, j) >= 0) {
                    tempItr++;
                    temp = region2.get(tempItr);
                    i = j;
                    jItr++;
                    j = region2.get(jItr);
                } else {
                    region2.remove(i);
                    if (tempItr - 1 < 0) {
                        tempItr = 0;
                        i = j;
                        j = region2.get(jItr);
                    } else {
                        jItr--;
                        i = region2.get(tempItr);
                        tempItr--;
                        temp = region2.get(tempItr);
                    }
                }
            }
            if (area(temp, i, j) < 0) {
                region2.remove(i);
            }
        }
        //get hull of region3
        if (region3.size() > 3) {
            jItr = 2;
            tempItr = 0;
            temp = region3.get(tempItr);
            i = region3.get(1);
            j = region3.get(jItr);
            while (j != region3.get(region3.size() - 1)) {
                if (area(temp, i, j) >= 0) {
                    tempItr++;
                    temp = region3.get(tempItr);
                    i = j;
                    jItr++;
                    j = region3.get(jItr);
                } else {
                    region3.remove(i);
                    if (tempItr - 1 < 0) {
                        tempItr = 0;
                        i = j;
                        j = region3.get(jItr);
                    } else {
                        jItr--;
                        i = region3.get(tempItr);
                        tempItr--;
                        temp = region3.get(tempItr);
                    }
                }
            }
            if (area(temp, i, j) < 0) {
                region3.remove(i);
            }
        }
        //get hull of region4
        if (region4.size() > 3) {
            jItr = 2;
            tempItr = 0;
            temp = region4.get(tempItr);
            i = region4.get(1);
            j = region4.get(jItr);
            while (j != region4.get(region4.size() - 1)) {
                if (area(temp, i, j) >= 0) {
                    tempItr++;
                    temp = region4.get(tempItr);
                    i = j;
                    jItr++;
                    j = region4.get(jItr);
                } else {
                    region4.remove(i);
                    if (tempItr - 1 < 0) {
                        tempItr = 0;
                        i = j;
                        j = region4.get(jItr);
                    } else {
                        jItr--;
                        i = region4.get(tempItr);
                        tempItr--;
                        temp = region4.get(tempItr);
                    }
                }
            }
            if (area(temp, i, j) < 0) {
                region4.remove(i);
            }
        }

        for (int k = 0; k < region1.size(); k++) {
            System.out.print(region1.get(k).name + " ");
        }
        for (int k = 0; k < region2.size(); k++) {
            System.out.print(region2.get(k).name + " ");
        }
        for (int k = 0; k < region3.size(); k++) {
            System.out.print(region3.get(k).name + " ");
        }
        for (int k = 0; k < region4.size(); k++) {
            System.out.print(region4.get(k).name + " ");
        }
    }

    private static int area(point a, point b, point c) {
        return ((b.x - a.x) * (c.y - a.y)) - ((b.y - a.y) * (c.x - a.x));
    }

    private static void drawLine(point a, point b, Group grp) {
        Line l1 = new Line(a.x, a.y, b.x, b.y);
        l1.setStroke(Color.web("#008000"));
        grp.getChildren().add(l1);
    }
}

//        if (region1.size() > 3) {
//                temp = region1.get(tempItr);
//                i = region1.get(1);
//                j = region1.get(jItr);
//                KeyFrame kf = new KeyFrame(Duration.seconds(0.2), actionEvent -> {
//                if (area(temp, i, j) >= 0) {
//                tempItr++;
//                temp = region1.get(tempItr);
//                i = j;
//                jItr++;
//                j = region1.get(jItr);
//                } else {
//                region1.remove(i);
//                if (tempItr - 1 < 0) {
//        tempItr = 0;
//        i = j;
//        j = region1.get(jItr);
//        } else {
//        jItr--;
//        i = region1.get(tempItr);
//        tempItr--;
//        temp = region1.get(tempItr);
//        }
//        }
//        if(j != region1.get(region1.size() - 1)){
//        if (area(temp, i, j) < 0) {
//        region1.remove(i);
//        }
//        tl.stop();
//        }
//        });
//        tl.getKeyFrames().add(kf);
//        }
