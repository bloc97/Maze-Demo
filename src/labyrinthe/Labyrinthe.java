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
    public Labyrinthe(int l, int h, float density, long delayms, int lives) {
        this.l = l;
        this.h = h;
        murs = generateWalls(l, h, density);
        int[] pos = generatePositions(murs);
        pers = new Personnage(pos[0], pos[1], lives);
        sortie = new Muret(pos[2], pos[3], pos[4] == 1);
    }
    public static ListeMuret generateWalls(int l, int h, float density) {
        ListeMuret liste = new ListeMuret();
        
        
        for (int x=0; x<l; x++) { //Murets horizontaux
            for (int y=0; y<=h; y++) {
                if (y == 0 || y == h) {
                    liste.push(new Muret(x, y, true));
                } else {
                    if (Math.random() < density) {
                        liste.push(new Muret(x, y, true));
                    }
                }
            }
        }
        
        for (int x=0; x<=l; x++) { //Murets verticaux
            for (int y=0; y<h; y++) {
                if (x == 0 || x == h) {
                    liste.push(new Muret(x, y, false));
                } else {
                    if (Math.random() < density) {
                        liste.push(new Muret(x, y, false));
                    }
                }
            }
        }
        
        
        return liste;
    }
    public static int[] generatePositions(ListeMuret murs) {
        return new int[] {0,0,0,0,0};
    }
    public int l() {
        return l;
    }
    public int h() {
        return h;
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
