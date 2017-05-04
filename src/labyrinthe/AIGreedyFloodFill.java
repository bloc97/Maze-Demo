/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinthe;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import static java.lang.Thread.sleep;
import java.util.Collections;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import static labyrinthe.Helper.canDeplace;
import static labyrinthe.Helper.canMove;
import static labyrinthe.Helper.createArrow;
import static labyrinthe.Helper.directionToChar;
import static labyrinthe.Helper.getFloodFillRelative;

/**
 *
 * @author Bowen Peng, Zhenglong, Devoir 2
 */
public class AIGreedyFloodFill implements AI {
    private int direction; //0N, 1E, 2S, 3W
    private int[][] visited;
    
    private boolean[][] fillingBoard;
    
    public AIGreedyFloodFill() {
        direction = Helper.randomRange(1, 2);
    }
    public static boolean getCorner(int d, int d2, int x, int y, int w, int h, ListeMuret murs) { //Trouver si il y a une jonction
        
        if ((d == 0 && d2 == 3) || (d == 3 && d2 == 0)) {
            return topLeft(x, y, w, h, murs);
        } else if ((d == 0 && d2 == 1) || (d == 1 && d2 == 0)) {
            return topRight(x, y, w, h, murs);
        } else if ((d == 2 && d2 == 3) || (d == 3 && d2 == 2)) {
            return bottomLeft(x, y, w, h, murs);
        } else if ((d == 2 && d2 == 1) || (d == 1 && d2 == 2)) {
            return bottomRight(x, y, w, h, murs);
        }
        return getCorner((d+4)%4, (d2+4)%4, x, y, w, h, murs);
        
    }
    public static boolean topLeft(int x, int y, int w, int h, ListeMuret murs) { //checks for topleft junction
        if (x-1 < 0 || y-1 < 0) {
            return true;
        }
        if (murs.chercheMuret(x-1, y, true) != null || murs.chercheMuret(x, y-1, false) != null) {
            return true;
        }
        return false;
    }
    public static boolean topRight(int x, int y, int w, int h, ListeMuret murs) { //checks for topright junction
        if (x+1 > w || y-1 < 0) {
            return true;
        }
        if (murs.chercheMuret(x+1, y, true) != null || murs.chercheMuret(x+1, y-1, false) != null) {
            return true;
        }
        return false;
    }
    public static boolean bottomLeft(int x, int y, int w, int h, ListeMuret murs) { //etc
        if (x-1 < 0 || y+1 > h) {
            return true;
        }
        if (murs.chercheMuret(x-1, y+1, true) != null || murs.chercheMuret(x, y+1, false) != null) {
            return true;
        }
        return false;
    }
    public static boolean bottomRight(int x, int y, int w, int h, ListeMuret murs) {
        if (x+1 > w || y+1 > h) {
            return true;
        }
        if (murs.chercheMuret(x+1, y+1, true) != null || murs.chercheMuret(x+1, y+1, false) != null) {
            return true;
        }
        return false;
    }
    
    
    private static double distanceScore(int x, int y, Muret sortie) {
        //return Math.abs(sortie.x-x) + Math.abs(sortie.y - y);
        return (sortie.x-x)*(sortie.x-x) + (sortie.y-y)*(sortie.y-y);
        //return Math.sqrt((sortie.x-x)*(sortie.x-x) + (sortie.y-y)*(sortie.y-y));
    }
    private static double distanceScore(int d, int x, int y, Muret sortie) { //score a minimiser
        switch (d) {
            case 0:
                return distanceScore(x, y-1, sortie);
            case 1:
                return distanceScore(x+1, y, sortie);
            case 2:
                return distanceScore(x, y+1, sortie);
            case 3:
                return distanceScore(x-1, y, sortie);
            default:
                return distanceScore((d+4)%4, x, y, sortie);
        }
    }

    @Override
    public char getNextDirection(int x, int y, int w, int h, ListeMuret murs, Muret sortie, JComponent affichage, int animWait) {
        
        if (visited == null) {
            visited = new int[w][h];
            for (int i=0; i<w; i++) {
                for (int j=0; j<h; j++) {
                    visited[i][j] = -1;
                }
            }
        }
        
        int exitX = (sortie.isHorz) ? sortie.x : sortie.x-1;
        int exitY = (sortie.isHorz) ? sortie.y-1 : sortie.y;
        
        double minScore = distanceScore(x, y, sortie);
        
        boolean canMoveForward = canMove(direction, x, y, w, h, murs);
        boolean canMoveLeft = canMove(direction-1, x, y, w, h, murs);
        boolean canMoveRight = canMove(direction+1, x, y, w, h, murs);
        
        if (canMoveForward && !canMoveLeft && !canMoveRight) {
            fillingBoard = getFloodFillRelative(x, y, direction, w, h, murs);
            
            try {
                affichage.repaint();
                sleep(animWait);
            } catch (InterruptedException ex) {
                Logger.getLogger(RandomGenerators.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if (fillingBoard[exitX][exitY]) {
                visited[x][y] = direction; //Helps prevent walking back and forth in the same location
                return directionToChar(direction);
            }
        }
        
        LinkedList<Integer> optimalSolutions = new LinkedList<>();
        LinkedList<Integer> otherSolutions = new LinkedList<>();
        
        
        for (int i=0; i<4; i++) {
            if (canMove(i, x, y, w, h, murs)) {
                double newScore = distanceScore(i, x, y, sortie);
                boolean[][] tempFillingBoard = getFloodFillRelative(x, y, i, w, h, murs);
                if (tempFillingBoard[exitX][exitY]) {
                    fillingBoard = tempFillingBoard;
                    if (newScore < minScore && visited[x][y] != i) {
                        minScore = newScore;
                        optimalSolutions.add(i);
                    } else {
                        otherSolutions.add(i);
                    }
                }
                
            }
        }
        
        try {
            affichage.repaint();
            sleep(animWait);
        } catch (InterruptedException ex) {
            Logger.getLogger(RandomGenerators.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        if (optimalSolutions.size() > 0) {
            Collections.shuffle(optimalSolutions);
            direction = optimalSolutions.getFirst();
        } else {
            Collections.shuffle(otherSolutions);
            direction = otherSolutions.getFirst();
            if (direction == visited[x][y]) { //Try not do the same movements as last time
                direction = otherSolutions.getLast();
            }
        }
    
        visited[x][y] = direction; //Prevent walking back and forth in the same location
        return directionToChar(direction);
        
    }

    @Override
    public void paint(Graphics2D g2, double sqSize) {
        if (visited == null || g2 == null) {
            return;
        }
        
        g2.setColor(new Color(20, 90, 120));
        g2.setStroke(new BasicStroke(2));
        if (fillingBoard != null) {
            for (int x=0; x<visited.length; x++) {
                for (int y=0; y<visited[0].length; y++) {
                    if(fillingBoard[x][y]) {
                        g2.fillRect((int)((x+1)*sqSize), (int)((y+1)*sqSize), (int)sqSize, (int)sqSize);
                        //g2.draw(createArrow(new Point((int)((x+1+0.5f)*sqSize), (int)((y+1+0.8f)*sqSize)), new Point((int)((x+1+0.5f)*sqSize), (int)((y+1+0.2f)*sqSize))));
                    }
                }
            }
        }
        g2.setColor(Color.CYAN);
        for (int x=0; x<visited.length; x++) {
            for (int y=0; y<visited[0].length; y++) {
                /*
                if (visited[x][y] != -1) {
                    g2.drawString(visited[x][y] + "", (x+1+0.4f)*(float)sqSize, (y+1+0.5f)*(float)sqSize);
                }*/
                switch(visited[x][y]) {
                    case 0:
                        g2.draw(createArrow(new Point((int)((x+1+0.5f)*sqSize), (int)((y+1+0.8f)*sqSize)), new Point((int)((x+1+0.5f)*sqSize), (int)((y+1+0.2f)*sqSize))));
                        break;
                    case 1:
                        g2.draw(createArrow(new Point((int)((x+1+0.2f)*sqSize), (int)((y+1+0.5f)*sqSize)), new Point((int)((x+1+0.8f)*sqSize), (int)((y+1+0.5f)*sqSize))));
                        break;
                    case 2:
                        g2.draw(createArrow(new Point((int)((x+1+0.5f)*sqSize), (int)((y+1+0.2f)*sqSize)), new Point((int)((x+1+0.5f)*sqSize), (int)((y+1+0.8f)*sqSize))));
                        break;
                    case 3:
                        g2.draw(createArrow(new Point((int)((x+1+0.8f)*sqSize), (int)((y+1+0.5f)*sqSize)), new Point((int)((x+1+0.2f)*sqSize), (int)((y+1+0.5f)*sqSize))));
                        break;
                }
            }
        }
    }

}
