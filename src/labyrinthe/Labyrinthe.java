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
    
    private AI ai = new AIGreedyFloodFill();
    
    private int winX, winY;

    //Constructor with 5 params: Lenght,height,density, time and number of lives
    public Labyrinthe(int w, int h, float density, long delayms, int lives) {
        this.l = w;
        this.h = h;
        this.density = density;
        this.initialLives = lives;
    }
    //Function to generate maze
    public void generate(JComponent affichage, double seconds) {
        System.out.println("Initialising Maze...");
        murs = new ListeMuret();
        //RandomGenerators.uniformGenerateWalls(l, h, density, murs, affichage, seconds);
        RandomGenerators.depthFirstGenerateWalls(l, h, density, murs, affichage, seconds);
        int[] pos = RandomGenerators.connectedMazeGeneratePossiblePositions(l, h, murs);
        System.out.println("Starting Maze.");
        pers = new Personnage(pos[0], pos[1], initialLives);
        sortie = new Muret(pos[2], pos[3], pos[4] == 1);
        
        winX = (sortie.isHorz) ? sortie.x : sortie.x-1;
        winY = (sortie.isHorz) ? sortie.y-1 : sortie.y;
        
        affichage.repaint();
    }
    //Ai implementation to get the next direction
    public void stepAI(JComponent affichage) {
        if (sortie == null) {
            return;
        }
        char dir = ai.getNextDirection(pers.x(), pers.y(), l, h, murs, sortie, affichage);
        
        if (pers.x() == winX && pers.y() == winY) {
            this.generate(affichage, 0);
            ai = new AIGreedyFloodFill();
            return;
        }
        
        if (canDeplace(dir)) {
            deplace(dir);
        }
    }
<<<<<<< HEAD
    
    
=======
    //Getters for width and height
>>>>>>> origin/master
    public int w() {
        return l;
    }
    public int h() {
        return h;
    }
<<<<<<< HEAD
    public AI ai() {
        return ai;
    }
=======
    //Getter for the player character
>>>>>>> origin/master
    public Personnage personnage() {
        return pers;
    }
    //Get walls
    public ListeMuret murs() {
        return murs;
    }
    //Get exit
    public Muret sortie() {
        return sortie;
    }
    //Condition check if can move otherwise there is a wall, return false
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
    //Move the character toward the specified direction location
    public boolean deplace(char direction) {
        
        if (pers.x() == winX && pers.y() == winY) {
            //WIN!
        }
        
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
