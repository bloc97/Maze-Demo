/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinthe;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import labyrinthe.AI.AIType;
import labyrinthe.RandomGenerators.GeneratorType;
import static labyrinthe.RandomGenerators.GeneratorType.NAIVEUNIFORM;

/**
 *
 * @author Bowen Peng, Zhenglong, Devoir 2
 */
public class Labyrinthe {
    private int l, h;
    private float density;
    private GeneratorType type;
    private long delayms;
    private int initialLives;
    private int waitms;
    private boolean doAnim;
    
    //private int aiDelay;
    private int aiAnimDelay;
    
    private Personnage pers;
    private ListeMuret murs;
    private Muret sortie;
    
    private JPanelLaby affichage;
    
    private AI ai;
    private AIType aiType;
    
    private int winX, winY;
    
    private int[] lastStartingState;
    
    private boolean isGenerating = true;
    private boolean finishedWallsGeneration = false;
    private boolean isAIenabled;
    private boolean automaticRestart;
    
    private boolean hasGameEnded = false;
    
    private boolean disableHide;
    
    //private Timer aiTimer;

    //Constructor with 5 params: Lenght,height,density, time and number of lives
    public Labyrinthe(int w, int h, JPanelLaby affichage, float density, GeneratorType type, long delayms, int lives, int waitms, boolean doAnim, AIType aiType, int aiDelay, int aiAnimDelay, boolean isAIenabled, boolean automaticRestart, boolean disableHide) {
        this.l = w;
        this.h = h;
        this.affichage = affichage;
        this.density = density;
        this.type = type;
        this.delayms = delayms;
        this.waitms = waitms;
        this.doAnim = doAnim;
        this.initialLives = lives;
        this.aiType = aiType;
        //this.aiDelay = aiDelay;
        this.aiAnimDelay = aiAnimDelay;
        this.isAIenabled = isAIenabled;
        this.automaticRestart = automaticRestart;
        this.disableHide = disableHide;
    }
    //Function to generate maze
    public void generate() {
        System.out.println("Initialising Maze...");
        isGenerating = true;
        finishedWallsGeneration = false;
        ai = null;
        murs = new ListeMuret();
        int[] pos;
        switch (type) {
            case NAIVEUNIFORM:
                RandomGenerators.uniformGenerateWalls(l, h, density, murs, affichage, waitms, doAnim);
                pos = RandomGenerators.disjointMazeGeneratePossiblePositions(l, h, murs);
                break;
            case RECURSIVE:
                RandomGenerators.recursiveGenerateWalls(l, h, density, murs, affichage, waitms, doAnim);
                pos = RandomGenerators.connectedMazeGeneratePossiblePositions(l, h, murs);
                break;
            case DEPTHFIRST:
                RandomGenerators.depthFirstGenerateWalls(l, h, density, murs, affichage, waitms, doAnim);
                pos = RandomGenerators.connectedMazeGeneratePossiblePositions(l, h, murs);
                break;
            case PRIM:
                RandomGenerators.primGenerateWalls(l, h, density, murs, affichage, waitms, doAnim);
                pos = RandomGenerators.connectedMazeGeneratePossiblePositions(l, h, murs);
                break;
            default:
                throw new IllegalArgumentException("Unknown generator type!");
        }
        
        System.out.println("Starting Maze.\n");
        lastStartingState = new int[] {pos[0], pos[1], initialLives};
        pers = new Personnage(pos[0], pos[1], initialLives);
        sortie = new Muret(pos[2], pos[3], pos[4] == 1);
        
        winX = (sortie.isHorz) ? sortie.x : sortie.x-1;
        winY = (sortie.isHorz) ? sortie.y-1 : sortie.y;
        ai = AI.getNewAI(aiType);
        
        
        if (isAIenabled || disableHide) {
            isGenerating = false;
        } else {
            startHideTimer();
        }
        
        affichage.repaint();
        finishedWallsGeneration = true;
        hasGameEnded = false;
    }
    
    public void purgeAI() {
        if (hasGameEnded || isGenerating) {
            return;
        }
        this.ai = null;
        ai = AI.getNewAI(aiType);
    }
    
    public int getShowTimeDelaySeconds() {
        return (int)delayms/1000;
    }
    
    public void purge() {
        purgeAI();
        disableRestart();
        isGenerating = true;
        hasGameEnded = true;
        pers = null;
        murs = null;
    }
    
    public void startHideTimer() {
        Timer hideTimer = new Timer((int)delayms, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isAIenabled || hasGameEnded || disableHide) {
                } else {
                    murs.hide();
                }
                isGenerating = false;
            }
        });
        hideTimer.setInitialDelay((int)delayms);
        hideTimer.setRepeats(false);
        hideTimer.start();
    }
    
    public void reset() {
        if (isGenerating) {
            return;
        }
        if (lastStartingState == null) {
            generate();
        } else {
            pers = new Personnage(lastStartingState[0], lastStartingState[1], lastStartingState[2]);
            ai = AI.getNewAI(aiType);
            murs.show();
            if (isAIenabled || disableHide) {
            } else {
                startHideTimer();
            }
        }
        hasGameEnded = false;
    }
    public void setAIAnimDelay(int aiAnimDelay) {
        this.aiAnimDelay = aiAnimDelay;
    }
    public void enableRestart() {
        automaticRestart = true;
    }
    public void disableRestart() {
        automaticRestart = false;
    }
    public void disableAutoHide() {
        disableHide = true;
    }
    public void enableAutoHide() {
        disableHide = false;
    }
    public void showWalls() {
        if (isGenerating) {
            return;
        }
        murs.show();
    }
    public void hideWalls() {
        if (isGenerating) {
            return;
        }
        murs.show();
    }
    public void enableAI() {
        isAIenabled = true;
        showWalls();
    }
    public void disableAI() {
        if (isGenerating) {
            return;
        }
        isAIenabled = false;
    }
    public boolean isAIenabled() {
        return isAIenabled;
    }
    
    //Ai implementation to get the next direction
    public void stepAI() {
        if (sortie == null || pers == null || affichage == null || isGenerating) {
            return;
        }
        if (hasGameEnded) {
            return;
        }
        if (ai == null) {
            ai = AI.getNewAI(aiType);
        }
        char dir = ai.getNextDirection(pers.x(), pers.y(), l, h, murs, sortie, affichage, aiAnimDelay);
        
        deplace(dir);
    }
    
    //Getters for width and height
    public boolean isGenerating() {
        return isGenerating;
    }
    public boolean isGeneratingWalls() {
        return !finishedWallsGeneration;
    }
    public int w() {
        return l;
    }
    public int h() {
        return h;
    }
    
    public AI ai() {
        return ai;
    }
    public AIType aiType() {
        return aiType;
    }
    public void setAIType(AIType type) {
        aiType = type;
        ai = AI.getNewAI(type);
    }
    
    //Getter for the player character

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
    private boolean tryDeplace(char direction) {
        
        
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
    public boolean checkEndGame() {
        affichage.repaint();
        
        if (hasGameEnded) {
            return true;
        }
        
        if (pers.vies() < 0) {
            showWalls();
            hasGameEnded = true;
            if (automaticRestart) {
                reset();
            } else {
                int choice = JOptionPane.showConfirmDialog(null, "You lost! Do you want to retry the current maze?", "Defeat", JOptionPane.YES_NO_CANCEL_OPTION);
                switch (choice) {
                    case 0:
                        reset();
                        break;
                    case 1:
                        affichage.queueGenerateNewMaze();
                        break;
                    case 2:
                        break;
                }
            }
            //System.out.println("YOU WIN!");
            return true;
        }
        
        if (pers.x() == winX && pers.y() == winY) {
            showWalls();
            hasGameEnded = true;
            if (automaticRestart) {
                affichage.queueGenerateNewMaze();
            } else {
                int choice = JOptionPane.showConfirmDialog(null, "You won! Do you want to start a new maze?", "Victory", JOptionPane.YES_NO_OPTION);
                switch (choice) {
                    case 0:
                        affichage.queueGenerateNewMaze();
                        break;
                    case 1:
                        break;
                }
            }
            //System.out.println("YOU WIN!");
            return true;
        }
        return false;
    }
    
    //Move the character toward the specified direction location
    public boolean deplace(char direction) {
        
        if (isGenerating) {
            return false;
        }
        if (checkEndGame()) {
            return false;
        }
        
        
        if (tryDeplace(direction)) {
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
            checkEndGame();
            return true;
        } else {
            //Display hurt animation
            checkEndGame();
            return false;
        }
    }
    
}
