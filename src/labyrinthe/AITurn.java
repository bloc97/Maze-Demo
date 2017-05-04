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
 * @author bowen
 */
public class AITurn implements AI {
    private int direction; //0N, 1E, 2S, 3W
    private int leftRight; //Randomly choose which side of wall to follow
    private int posNeg;
    private int[][] visited;
    
    private boolean useMemory;
    
    public AITurn() {
        direction = Helper.randomRange(0, 3);
        leftRight = 0;
        posNeg = 0;
        //leftRight = (Math.random() < 0.5) ? 1 : -1;
        //posNeg = (leftRight == 1) ? -1 : 1;
    }
    public AITurn(boolean b) {
        this();
        useMemory = b;
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
        if (leftRight == 0) {
            if (canMove(direction, x, y, w, h, murs)) {
                visited[x][y] = direction;
                return directionToChar(direction);
            } else {
                boolean canMoveRight = canMove((direction+1+4)%4, x, y, w, h, murs);
                boolean canMoveLeft = canMove((direction-1+4)%4, x, y, w, h, murs);
                if (canMoveRight && !canMoveLeft) {
                    direction = (direction + 1 + 4) % 4;
                    leftRight = -1;
                    posNeg = 1;
                } else if (canMoveLeft && !canMoveRight) {
                    direction = (direction - 1 + 4) % 4;
                    leftRight = 1;
                    posNeg = -1;
                } else {
                    if (Math.random() < 0.5) { //Turn right
                        direction = (direction + 1 + 4) % 4;
                        leftRight = -1;
                        posNeg = 1;
                    } else { //Turn left
                        direction = (direction - 1 + 4) % 4;
                        leftRight = 1;
                        posNeg = -1;
                    }
                }
            }
        }
        
        int count = 0;
        int newDirection = (direction + leftRight + 4)%4;
        while (true) {
            boolean canMove = canMove(newDirection, x, y, w, h, murs);
            if (canMove && (!useMemory || visited[x][y] != newDirection || count > 4)) {
                //direction = newDirection;
                break;
            }
            newDirection = (newDirection + posNeg + 4)%4;
            count++;
        }
    
        visited[x][y] = newDirection;
        direction = newDirection;
        return directionToChar(newDirection);
        
    }

    @Override
    public void paint(Graphics2D g2, double sqSize) {
        if (visited == null || g2 == null) {
            return;
        }
        g2.setStroke(new BasicStroke(2));
        g2.setColor(Color.CYAN);
        for (int x=0; x<visited.length; x++) {
            for (int y=0; y<visited[0].length; y++) {
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
