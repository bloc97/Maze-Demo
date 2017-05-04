/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinthe;

import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;

/**
 *
 * @author Bowen Peng, Zhenglong, Devoir 2
 */
//Helper class
public abstract class Helper { //Helper class
    //Classe helper avec des fonctions souvents utilisees

    //Print out the filled region
    public static void printFill(boolean[][] grid) {
        for (int y=0; y<grid[0].length; y++) {
            for (int x=0; x<grid.length; x++) {
                System.out.print((grid[x][y]) ? "1" : "0");
            }
            System.out.println("");
        }
    }
    //return the fill size available
    public static int getFillSize(boolean[][] grid) {
        int total = 0;
        for (int x=0; x<grid.length; x++) {
            for (int y=0; y<grid[0].length; y++) {
                if (grid[x][y]) {
                    total++;
                }
            }
        }
        return total;
    }
    //Flood fill relative to a position
    public static boolean[][] getFloodFillRelative(int x, int y, int direction, int w, int h, ListeMuret murs) {
        boolean[][] grid = new boolean[w][h];
        grid[x][y] = true;
        
        switch(direction) {
            case 0:
                recursiveFloodFill(x, y-1, grid, murs);
                break;
            case 1:
                recursiveFloodFill(x+1, y, grid, murs);
                break;
            case 2:
                recursiveFloodFill(x, y+1, grid, murs);
                break;
            case 3:
                recursiveFloodFill(x-1, y, grid, murs);
                break;
            default:
                return getFloodFillRelative(x, y, (direction+4)%4, w, h, murs);
        }
        return grid;
    }
    //get the flooded grid
    public static boolean[][] getFloodFill(int x, int y, int w, int h, ListeMuret murs) {
        boolean[][] grid = new boolean[w][h];
        recursiveFloodFill(x, y, grid, murs);
        return grid;
    }

    //fill recursively
    public static void recursiveFloodFill(int x, int y, boolean[][] grid, ListeMuret murs) {
        if (x < 0 || x >= grid.length || y < 0 || y >= grid[0].length) {
            return;
        }
        if (grid[x][y]) {
            return;
        }
        grid[x][y] = true;
        if (murs.chercheMuret(x, y, 'N') == null) { //No top wall
            recursiveFloodFill(x, y-1, grid, murs);
        }
        if (murs.chercheMuret(x, y, 'S') == null) { //No bottom wall
            recursiveFloodFill(x, y+1, grid, murs);
        }
        if (murs.chercheMuret(x, y, 'W') == null) { //No left wall
            recursiveFloodFill(x-1, y, grid, murs);
        }
        if (murs.chercheMuret(x, y, 'E') == null) { //No right wall
            recursiveFloodFill(x+1, y, grid, murs);
        }
        
    }
    //Check if movable
    public static boolean canDeplace(int x, int y, char cardinalDirection, ListeMuret murs) {
        return (murs.chercheMuret(x, y, cardinalDirection) == null);
    }

    //return a random  between some min and max range
    public static int randomRange(int min, int max) {
        int range = (max - min) + 1;
        return (int) (Math.random() * range) + min;
    }
    public static char directionToChar(int direction) {
        switch (direction) {
            case 0:
                return 'H';
            case 1:
                return 'D';
            case 2:
                return 'B';
            case 3:
                return 'G';
            default:
                return directionToChar((direction+4)%4);
        }
    }
    public static Shape createArrow(Point p0, Point p1) {
        Polygon arrow = new Polygon();
        arrow.addPoint(-6,1);
        arrow.addPoint(3,1);
        arrow.addPoint(3,3);
        arrow.addPoint(6,0);
        arrow.addPoint(3,-3);
        arrow.addPoint(3,-1);
        arrow.addPoint(-6,-1);


        Point midPoint = new Point((int)((p0.x + p1.x)/2.0), (int)((p0.y + p1.y)/2.0));

        double rotate = Math.atan2(p1.y - p0.y, p1.x - p0.x);

        AffineTransform transform = new AffineTransform();
        transform.translate(midPoint.x, midPoint.y);
        double ptDistance = p0.distance(p1);
        double scale = ptDistance / 12.0;
        transform.scale(scale, scale);
        transform.rotate(rotate);

        return transform.createTransformedShape(arrow);
    }
    
    public static Shape createLos(Point p0, Point p1) {
        Polygon los = new Polygon();
        los.addPoint(-1,-1);
        los.addPoint(-2,1);
        los.addPoint(2,1);
        los.addPoint(3,-1);
        los.addPoint(-1,-1);


        Point midPoint = new Point((int)((p0.x + p1.x)/2.0), (int)((p0.y + p1.y)/2.0));

        double rotate = Math.atan2(p1.y - p0.y, p1.x - p0.x);

        AffineTransform transform = new AffineTransform();
        transform.translate(midPoint.x, midPoint.y);
        double ptDistance = p0.distance(p1);
        double scale = ptDistance / 5;
        transform.scale(scale, scale);
        transform.rotate(rotate+Math.PI);

        return transform.createTransformedShape(los);
    }

    public static Muret getMuretFromDirection(int x, int y, int d) {
        switch (d) {
            case 0:
                return new Muret(x, y, true);
            case 1:
                return new Muret(x + 1, y, false);
            case 2:
                return new Muret(x, y + 1, true);
            case 3:
                return new Muret(x, y, false);
            default:
                return getMuretFromDirection(x, y, (d + 4) % 4);
        }
    }

    public static Point getPointFromDirection(int x, int y, int d) {
        switch (d) {
            case 0:
                return new Point(x, y - 1);
            case 1:
                return new Point(x + 1, y);
            case 2:
                return new Point(x, y + 1);
            case 3:
                return new Point(x - 1, y);
            default:
                return getPointFromDirection(x, y, (d + 4) % 4);
        }
    }

    public static boolean isVisited(int x, int y, int d, boolean[][] visited) {
        switch (d) {
            case 0:
                return isVisited(x, y - 1, visited);
            case 1:
                return isVisited(x + 1, y, visited);
            case 2:
                return isVisited(x, y + 1, visited);
            case 3:
                return isVisited(x - 1, y, visited);
            default:
                return isVisited(x, y, (d + 4) % 4, visited);
        }
    }

    public static boolean isVisited(int x, int y, boolean[][] visited) {
        if (x < 0 || y < 0 || x >= visited.length || y >= visited[0].length) {
            return true;
        }
        return visited[x][y];
    }
    
    public static boolean hasUnvisited(boolean[][] visited) {
        for (int x=0; x<visited.length; x++) {
            for (int y=0; y<visited[0].length; y++) {
                if (!visited[x][y]) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public static boolean isBorderMuret(Muret muret, int w, int h) {
        if ((muret.x == 0 || muret.x == w) && !muret.isHorz) {
            return true;
        }
        if ((muret.y == 0 || muret.y == h) && muret.isHorz) {
            return true;
        }
        return false;
    }

    public static boolean canMove(int d, int x, int y, int w, int h, ListeMuret murs) {
        switch (d) {
            case 0:
                return murs.chercheMuret(x, y, 'N') == null;
            case 1:
                return murs.chercheMuret(x, y, 'E') == null;
            case 2:
                return murs.chercheMuret(x, y, 'S') == null;
            case 3:
                return murs.chercheMuret(x, y, 'W') == null;
            default:
                return canMove((d + 4) % 4, x, y, w, h, murs);
        }
    }
    
}
