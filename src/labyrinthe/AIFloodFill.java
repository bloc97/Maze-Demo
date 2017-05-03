/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinthe;

import java.awt.Graphics2D;
import javax.swing.JComponent;
import static labyrinthe.Helper.canDeplace;
import static labyrinthe.Helper.directionToChar;
import static labyrinthe.Helper.getFloodFillRelative;

/**
 *
 * @author bowen
 */
public class AIFloodFill implements AI {
    private int direction; //0N, 1E, 2S, 3W
    
    public AIFloodFill() {
        direction = Helper.randomRange(1, 2);
    }
    public boolean getCorner(int d, int d2, int x, int y, int w, int h, ListeMuret murs) {
        
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
    public boolean topLeft(int x, int y, int w, int h, ListeMuret murs) { //checks for topleft junction
        if (x-1 < 0 || y-1 < 0) {
            return true;
        }
        if (murs.chercheMuret(x-1, y, true) != null || murs.chercheMuret(x, y-1, false) != null) {
            return true;
        }
        return false;
    }
    public boolean topRight(int x, int y, int w, int h, ListeMuret murs) { //checks for topright junction
        if (x+1 > w || y-1 < 0) {
            return true;
        }
        if (murs.chercheMuret(x+1, y, true) != null || murs.chercheMuret(x+1, y-1, false) != null) {
            return true;
        }
        return false;
    }
    public boolean bottomLeft(int x, int y, int w, int h, ListeMuret murs) {
        if (x-1 < 0 || y+1 > h) {
            return true;
        }
        if (murs.chercheMuret(x-1, y+1, true) != null || murs.chercheMuret(x, y+1, false) != null) {
            return true;
        }
        return false;
    }
    public boolean bottomRight(int x, int y, int w, int h, ListeMuret murs) {
        if (x+1 > w || y+1 > h) {
            return true;
        }
        if (murs.chercheMuret(x+1, y+1, true) != null || murs.chercheMuret(x+1, y+1, false) != null) {
            return true;
        }
        return false;
    }
    
    public boolean canMove(int d, int x, int y, int w, int h, ListeMuret murs) {
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
                return canMove((d+4)%4, x, y, w, h, murs);
        }
    }
    

    @Override
    public char getNextDirection(int x, int y, int w, int h, ListeMuret murs, Muret sortie, JComponent affichage) {
        //System.out.println(canMoveTop + " " + canMoveRight + " " + canMoveBottom + " " + canMoveLeft);
        //System.out.println(canMove(0, x, y, w, h, murs) + " " + canMove(1, x, y, w, h, murs) + " " + canMove(2, x, y, w, h, murs) + " " + canMove(3, x, y, w, h, murs));
        
        int exitX = (sortie.isHorz) ? sortie.x : sortie.x-1;
        int exitY = (sortie.isHorz) ? sortie.y-1 : sortie.y;
        
        
        if (!canMove(direction, x, y, w, h, murs)) {
            boolean canTurnLeft = false, canTurnRight = false;
            
            if (canMove(direction+1, x, y, w, h, murs) && getFloodFillRelative(x, y, direction+1, w, h, murs)[exitX][exitY]) {
                canTurnRight = true;
            }
            if (canMove(direction-1, x, y, w, h, murs) && getFloodFillRelative(x, y, direction-1, w, h, murs)[exitX][exitY]) {
                canTurnLeft = true;
            }
            
            if (canTurnLeft && canTurnRight) {
                direction = (Math.random() < 0.5) ? (direction+1)%4 : (direction-1)%4;
                //direction = ((direction+1)%4 == 1 || (direction+1)%4 == 2) ? (direction+1)%4 : (direction-1)%4;
            } else if (canTurnLeft) {
                direction = (direction-1)%4;
            } else if (canTurnRight) {
                direction = (direction+1)%4;
            } else if (canMove(direction+2, x, y, w, h, murs)) {
                direction = (direction+2)%4;
            }
            return directionToChar(direction);
        }
        
        boolean canTurnLeftCorner = false, canTurnRightCorner = false;
        if (canMove(direction+1, x, y, w, h, murs) && getCorner(direction, direction+1, x, y, w, h, murs) && getFloodFillRelative(x, y, direction+1, w, h, murs)[exitX][exitY]) {
            canTurnRightCorner = true;
        }
        
        if (canMove(direction-1, x, y, w, h, murs) && getCorner(direction, direction-1, x, y, w, h, murs) && getFloodFillRelative(x, y, direction-1, w, h, murs)[exitX][exitY]) {
            canTurnLeftCorner = true;
        }
        
        if (canTurnLeftCorner && canTurnRightCorner) {
            direction = (Math.random() < 0.5) ? (direction+1)%4 : (direction-1)%4;
            //direction = ((direction+1)%4 == 1 || (direction+1)%4 == 2) ? (direction+1)%4 : (direction-1)%4;
        } else if (canTurnLeftCorner) {
            if (getFloodFillRelative(x, y, direction, w, h, murs)[exitX][exitY]) {
                direction = (Math.random() < 0.5) ? (direction-1)%4 : direction;
                //direction = ((direction-1)%4 == 1 || (direction-1)%4 == 2) ? (direction-1)%4 : direction;
            } else {
                direction = (direction-1)%4;
            }
        } else if (canTurnRightCorner) {
            if (getFloodFillRelative(x, y, direction, w, h, murs)[exitX][exitY]) {
                direction = (Math.random() < 0.5) ? (direction+1)%4 : direction;
                //direction = ((direction+1)%4 == 1 || (direction+1)%4 == 2) ? (direction+1)%4 : direction;
            } else {
                direction = (direction+1)%4;
            }
        }
        //System.out.println(direction);
        return directionToChar(direction);
        
    }

    @Override
    public void paint(Graphics2D g2, double sqSize) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
