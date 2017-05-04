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
import javax.swing.JComponent;
import static labyrinthe.Helper.canMove;
import static labyrinthe.Helper.createArrow;
import static labyrinthe.Helper.directionToChar;

/**
 *
 * @author Bowen Peng, Zhenglong, Devoir 2
 */
public class AITurn implements AI {
    private int direction; //0N, 1E, 2S, 3W
    private int leftRight; //Randomly choose which side of wall to follow
    private int posNeg;
    private int[][] visited;
    
    private boolean[][] fillingBoard;
    
    private boolean useMemory;
    private boolean useFill;
    
    public AITurn() {
        direction = Helper.randomRange(0, 3);
        leftRight = 0;
        posNeg = 0;
        //leftRight = (Math.random() < 0.5) ? 1 : -1;
        //posNeg = (leftRight == 1) ? -1 : 1;
    }
    public AITurn(boolean b) { //Si le AI utilise la memoire
        this();
        useMemory = b;
    }
    public AITurn(boolean b, boolean f) { //Si le AI utilise du floodFill
        this(b);
        useFill = f;
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
        
        if (leftRight == 0) {
            boolean canMoveByFill = true;
            if (useFill) {
                fillingBoard = Helper.getFloodFillRelative(x, y, direction, w, h, murs);
                canMoveByFill = fillingBoard[exitX][exitY];
            }
            if (canMove(direction, x, y, w, h, murs) && (canMoveByFill)) {
                visited[x][y] = direction;
                return directionToChar(direction);
            } else {
                boolean canMoveRight = canMove((direction+1+4)%4, x, y, w, h, murs) && (!useFill || Helper.getFloodFillRelative(x, y, (direction+1+4)%4, w, h, murs)[exitX][exitY]);
                boolean canMoveLeft = canMove((direction-1+4)%4, x, y, w, h, murs) && (!useFill || Helper.getFloodFillRelative(x, y, (direction-1+4)%4, w, h, murs)[exitX][exitY]);
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
        boolean[][] tempFill = null;
        while (true) {
            boolean canMoveByFill = true;
            if (useFill) {
                tempFill = Helper.getFloodFillRelative(x, y, newDirection, w, h, murs);
                canMoveByFill = tempFill[exitX][exitY];
            }
            boolean canMove = canMove(newDirection, x, y, w, h, murs) && (count>4 || canMoveByFill);
            if (canMove && (!useMemory || visited[x][y] != newDirection || count > 4)) {
                //direction = newDirection;
                break;
            }
            newDirection = (newDirection + posNeg + 4)%4;
            count++;
        }
        if (useFill) {
            fillingBoard = tempFill;
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
        if (useFill) {
            if (fillingBoard != null) {
                g2.setColor(new Color(20, 90, 120));
                for (int x=0; x<visited.length; x++) {
                    for (int y=0; y<visited[0].length; y++) {
                        if(fillingBoard[x][y]) {
                            g2.fillRect((int)((x+1)*sqSize), (int)((y+1)*sqSize), (int)sqSize, (int)sqSize);
                        }
                    }
                }
            }
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
