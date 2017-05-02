/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinthe;

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
    
    public boolean canMoveTop(int x, int y, int w, int h, ListeMuret murs) {
        if (y-1 < 0) {
            return false;
        }
        return murs.chercheMuret(x, y, true) != null;
    }
    public boolean canMoveRight(int x, int y, int w, int h, ListeMuret murs) {
        if (x+1 >= w) {
            return false;
        }
        return murs.chercheMuret(x+1, y, false) != null;
    }
    public boolean canMoveBottom(int x, int y, int w, int h, ListeMuret murs) {
        if (y+1 >= h) {
            return false;
        }
        return murs.chercheMuret(x, y+1, true) != null;
    }
    public boolean canMoveLeft(int x, int y, int w, int h, ListeMuret murs) {
        if (x-1 < 0) {
            return false;
        }
        return murs.chercheMuret(x, y, false) != null;
    }


    @Override
    public char getNextDirection(int x, int y, int w, int h, ListeMuret murs, Muret sortie, JComponent affichage) {
        boolean canMoveTop = canDeplace(x, y, 'N', murs);
        boolean canMoveRight = canDeplace(x, y, 'E', murs);
        boolean canMoveBottom = canDeplace(x, y, 'S', murs);
        boolean canMoveLeft = canDeplace(x, y, 'W', murs);
        
        System.out.println(canMoveTop + " " + canMoveRight + " " + canMoveBottom + " " + canMoveLeft);
        System.out.println(canMove(0, x, y, w, h, murs) + " " + canMove(1, x, y, w, h, murs) + " " + canMove(2, x, y, w, h, murs) + " " + canMove(3, x, y, w, h, murs));
        
        int exitX = (sortie.isHorz) ? sortie.x : sortie.x-1;
        int exitY = (sortie.isHorz) ? sortie.y-1 : sortie.y;
        
        boolean foundDirection = false;
        
        if (!canMove(direction, x, y, w, h, murs)) {
            if (canMove(direction+1, x, y, w, h, murs) && getFloodFillRelative(x, y, direction+1, w, h, murs)[exitX][exitY]) {
                direction = (direction+1)%4;
            } else if (canMove(direction-1, x, y, w, h, murs) && getFloodFillRelative(x, y, direction-1, w, h, murs)[exitX][exitY]) {
                direction = (direction-1)%4;
            } else if (canMove(direction+2, x, y, w, h, murs)) {
                direction = (direction+2)%4;
            }
            foundDirection = true;
        }
        
        if (!foundDirection && canMove(direction+1, x, y, w, h, murs) && getCorner(direction, direction+1, x, y, w, h, murs) && getFloodFillRelative(x, y, direction+1, w, h, murs)[exitX][exitY]) {
            direction = (direction+1)%4;
            foundDirection = true;
        }
        
        if (!foundDirection && canMove(direction-1, x, y, w, h, murs) && getCorner(direction, direction-1, x, y, w, h, murs) && getFloodFillRelative(x, y, direction-1, w, h, murs)[exitX][exitY]) {
            direction = (direction-1)%4;
            foundDirection = true;
        }
        /*
        switch(direction) {
            case 0:
                if (!canMoveTop) {
                    if (canMoveRight && Helper.getFloodFillRelative(x, y, 1, w, h, murs)[exitX][exitY]) {
                        direction = 1; //Right
                    } else if (canMoveLeft && Helper.getFloodFillRelative(x, y, 3, w, h, murs)[exitX][exitY]) {
                        direction = 3; //Left
                    } else if (canMoveBottom) {
                        direction = 2; //Bottom;
                    }
                    break;
                }

                if (canMoveRight && topRight(x, y, w, h, murs)) {
                    if (Helper.getFloodFillRelative(x, y, 1, w, h, murs)[exitX][exitY]) {
                        direction = 1; //Right
                        break;
                    }
                }

                if (canMoveLeft && topLeft(x, y, w, h, murs)) {
                    if (Helper.getFloodFillRelative(x, y, 3, w, h, murs)[exitX][exitY]) {
                        direction = 3; //Right
                        break;
                    }
                }
                
                break;
            case 1:
                if (!canMoveRight) {
                    if (canMoveBottom && Helper.getFloodFillRelative(x, y, 2, w, h, murs)[exitX][exitY]) {
                        direction = 2; 
                    } else if (canMoveTop && Helper.getFloodFillRelative(x, y, 0, w, h, murs)[exitX][exitY]) {
                        direction = 0;
                    } else if (canMoveLeft) {
                        direction = 3;
                    }
                    break;
                }

                if (canMoveBottom && bottomRight(x, y, w, h, murs)) {
                    if (Helper.getFloodFillRelative(x, y, 2, w, h, murs)[exitX][exitY]) {
                        direction = 2;
                        break;
                    }
                }

                if (canMoveTop && topRight(x, y, w, h, murs)) {
                    if (Helper.getFloodFillRelative(x, y, 0, w, h, murs)[exitX][exitY]) {
                        direction = 0;
                        break;
                    }
                }
                break;
            case 2:
                if (!canMoveBottom) {
                    if (canMoveRight && Helper.getFloodFillRelative(x, y, 1, w, h, murs)[exitX][exitY]) {
                        direction = 1; 
                    } else if (canMoveLeft && Helper.getFloodFillRelative(x, y, 3, w, h, murs)[exitX][exitY]) {
                        direction = 3;
                    } else if (canMoveTop) {
                        direction = 0;
                    }
                    break;
                }

                if (canMoveRight && bottomRight(x, y, w, h, murs)) {
                    if (Helper.getFloodFillRelative(x, y, 1, w, h, murs)[exitX][exitY]) {
                        direction = 1;
                        break;
                    }
                }

                if (canMoveLeft && bottomLeft(x, y, w, h, murs)) {
                    if (Helper.getFloodFillRelative(x, y, 3, w, h, murs)[exitX][exitY]) {
                        direction = 3;
                        break;
                    }
                }
                break;
            case 3:
                if (!canMoveLeft) {
                    if (canMoveBottom && Helper.getFloodFillRelative(x, y, 2, w, h, murs)[exitX][exitY]) {
                        direction = 2; 
                    } else if (canMoveTop && Helper.getFloodFillRelative(x, y, 0, w, h, murs)[exitX][exitY]) {
                        direction = 0;
                    } else if (canMoveRight) {
                        direction = 1;
                    }
                    break;
                }

                if (canMoveBottom && bottomLeft(x, y, w, h, murs)) {
                    if (Helper.getFloodFillRelative(x, y, 2, w, h, murs)[exitX][exitY]) {
                        direction = 2;
                        break;
                    }
                }

                if (canMoveTop && topLeft(x, y, w, h, murs)) {
                    if (Helper.getFloodFillRelative(x, y, 0, w, h, murs)[exitX][exitY]) {
                        direction = 0;
                        break;
                    }
                }
                break;
            default:
                throw new IllegalStateException("Illegal AI Direction State.");
        }*/
        System.out.println(direction);
        return directionToChar(direction);
        
    }

}
