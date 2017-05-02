/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinthe;

import javax.swing.JComponent;

/**
 *
 * @author bowen
 */
public class Labyrinthe {
    private int l, h;
    private Personnage pers;
    private ListeMuret murs;
    private Muret sortie;
    
    private final float density;
    private final int initialLives;
    
    private AI ai = new AIFloodFill();
    
    private int winX, winY;
    
    public Labyrinthe(int w, int h, float density, long delayms, int lives) {
        this.l = w;
        this.h = h;
        this.density = density;
        this.initialLives = lives;
    }
    
    public void generate(JComponent affichage, double seconds) {
        System.out.println("Initialising Maze...");
        murs = new ListeMuret();
        //RandomGenerators.uniformGenerateWalls(l, h, density, murs, affichage, seconds);
        RandomGenerators.recursiveGenerateWalls(l, h, density, murs, affichage, seconds);
        int[] pos = RandomGenerators.recursiveGeneratePossiblePositions(l, h, murs);
        System.out.println("Starting Maze.");
        pers = new Personnage(pos[0], pos[1], initialLives);
        sortie = new Muret(pos[2], pos[3], pos[4] == 1);
        
        winX = (sortie.isHorz) ? sortie.x : sortie.x-1;
        winY = (sortie.isHorz) ? sortie.y-1 : sortie.y;
        
        affichage.repaint();
    }
    
    public void stepAI(JComponent affichage) {
        char dir = ai.getNextDirection(pers.x(), pers.y(), l, h, murs, sortie, affichage);
        
        if (pers.x() == winX && pers.y() == winY) {
            return;
        }
        
        if (canDeplace(dir)) {
            deplace(dir);
        }
    }
    
    public int w() {
        return l;
    }
    public int h() {
        return h;
    }
    public Personnage personnage() {
        return pers;
    }
    public ListeMuret murs() {
        return murs;
    }
    public Muret sortie() {
        return sortie;
    }
    public boolean canDeplace(char direction) {
        Muret mur;
        switch(direction) {
            case 'H':
                mur = murs.chercheMuret(pers.x(), pers.y(), 'N');
                break;
            case 'B':
                mur = murs.chercheMuret(pers.x(), pers.y(), 'S');
                break;
            case 'G':
                mur = murs.chercheMuret(pers.x(), pers.y(), 'W');
                break;
            case 'D':
                mur = murs.chercheMuret(pers.x(), pers.y(), 'E');
                break;
            default :
                throw new UnsupportedOperationException("Cannot move to direction " + direction);
        }
        if (mur instanceof Muret) {
            mur.show();
            pers.hurt();
            return false;
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
            default :
                throw new UnsupportedOperationException("Cannot move to direction " + direction);
            }
            return true;
        } else {
            return false;
        }
    }
    
}
