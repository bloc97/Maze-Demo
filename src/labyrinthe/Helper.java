/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinthe;

/**
 *
 * @author bowen
 */
public abstract class Helper {
    
    public static void printFill(boolean[][] grid) {
        for (int y=0; y<grid[0].length; y++) {
            for (int x=0; x<grid.length; x++) {
                System.out.print((grid[x][y]) ? "1" : "0");
            }
            System.out.println("");
        }
    }
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
    public static boolean[][] getFloodFill(int x, int y, int w, int h, ListeMuret murs) {
        boolean[][] grid = new boolean[w][h];
        recursiveFloodFill(x, y, grid, murs);
        return grid;
    }
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
    public static boolean canDeplace(int x, int y, char cardinalDirection, ListeMuret murs) {
        return (murs.chercheMuret(x, y, cardinalDirection) == null);
    }

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
    
}
