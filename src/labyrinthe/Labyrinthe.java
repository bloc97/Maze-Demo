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
public class Labyrinthe {
    private int l, h;
    Personnage pers;
    ListeMuret murs;
    Muret sortie;
    public Labyrinthe(int l, int h, float density, long delayms, byte lives) {
        this.l = l;
        this.h = h;
        murs = generateWalls(density);
        int[] pos = generatePositions(murs);
        pers = new Personnage(pos[0], pos[1], lives);
        sortie = new Muret(pos[2], pos[3], pos[4] == 1);
    }
    public static ListeMuret generateWalls(float density) {
        return new ListeMuret(null);
    }
    public static int[] generatePositions(ListeMuret murs) {
        return new int[] {0,0,0,0,0};
    }
    public boolean canDeplace(char direction) {
        switch(direction) {
            case 'H':
                for (Muret mur : murs) {
                    if (pers.x() == mur.x && pers.y() == mur.y && mur.isHorz) {
                        return false;
                    }
                }
                break;
            case 'B':
                for (Muret mur : murs) {
                    if (pers.x() == mur.x && pers.y() == mur.y+1 && mur.isHorz) {
                        return false;
                    }
                }
                break;
            case 'G':
                for (Muret mur : murs) {
                    if (pers.x() == mur.x && pers.y() == mur.y && !mur.isHorz) {
                        return false;
                    }
                }
                break;
            case 'D':
                for (Muret mur : murs) {
                    if (pers.x() == mur.x+1 && pers.y() == mur.y && !mur.isHorz) {
                        return false;
                    }
                }
                break;
            default :
                throw new UnsupportedOperationException("Cannot move to direction " + direction);
        }
        return true;
    }
    public boolean deplace(char direction) {
        if (canDeplace(direction)) {
        switch(direction) {
            case 'H':
                pers.addY(-1);
                break;
            case 'B':
                pers.addY(1);
                break;
            case 'G':
                pers.addX(-1);
                break;
            case 'D':
                pers.addX(1);
                break;
            }
            return true;
        } else {
            return false;
        }
    }
    
}
