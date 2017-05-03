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
 * @author bowen
 */
public class Labyrinthe {
    private int l, h;
    private float density;
    private GeneratorType type;
    private long delayms;
    private int initialLives;
    private int waitms;
    private boolean doAnim;
    
    private int aiDelay;
    
    private Personnage pers;
    private ListeMuret murs;
    private Muret sortie;
    
    private JPanelLaby affichage;
    
    private AI ai;
    private AIType aiType;
    
    private int winX, winY;
    
    private int[] lastStartingState;
    
    private GeneratorType lastType = NAIVEUNIFORM;
    
    private boolean isGenerating = true;
    private boolean isAIenabled;
    private boolean automaticRestart;
    
    private boolean hasGameEnded = false;
    
    private Timer aiTimer;

    //Constructor with 5 params: Lenght,height,density, time and number of lives
    public Labyrinthe(int w, int h, JPanelLaby affichage, float density, GeneratorType type, long delayms, int lives, int waitms, boolean doAnim, AIType aiType, int aiDelay, boolean isAIenabled, boolean automaticRestart) {
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
        this.aiDelay = aiDelay;
        this.isAIenabled = isAIenabled;
        this.automaticRestart = automaticRestart;
    }
    //Function to generate maze
    public void generate() {
        System.out.println("Initialising Maze...");
        isGenerating = true;
        if (aiTimer instanceof Timer) {
            aiTimer.stop();
        }
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
        isGenerating = false;
        aiTimer = new Timer(aiDelay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (isAIenabled) {
                    stepAI();
                    affichage.repaint();
                }
            }
        });
        
        if (isAIenabled) {
            enableAI();
        } else {
            startHideTimer();
        }
        
        affichage.repaint();
        hasGameEnded = false;
    }
    
    public void purgeAI() {
        if (hasGameEnded || isGenerating) {
            return;
        }
        this.ai = null;
        ai = AI.getNewAI(aiType);
    }
    
    public void purge() {
        disableAI();
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
                if (isAIenabled || hasGameEnded || isGenerating) {
                } else {
                    murs.hide();
                }
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
            if (isAIenabled) {
                enableAI();
            } else {
                startHideTimer();
            }
        }
        hasGameEnded = false;
    }
    public void setAIDelay(int aiDelay) {
        this.aiDelay = aiDelay;
        if (aiTimer instanceof Timer) {
            aiTimer.setDelay(aiDelay);
        }
    }
    public void enableRestart() {
        automaticRestart = true;
    }
    public void disableRestart() {
        automaticRestart = false;
    }
    public void enableAI() {
        if (isGenerating) {
            return;
        }
        isAIenabled = true;
        murs.show();
        aiTimer.start();
    }
    public void disableAI() {
        if (isGenerating) {
            return;
        }
        isAIenabled = false;
        aiTimer.stop();
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
        char dir = ai.getNextDirection(pers.x(), pers.y(), l, h, murs, sortie, affichage);
        
        deplace(dir);
    }
    
    //Getters for width and height
    public boolean isGenerating() {
        return isGenerating;
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
                        disableAI();
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
            hasGameEnded = true;
            if (automaticRestart) {
                disableAI();
                affichage.queueGenerateNewMaze();
            } else {
                int choice = JOptionPane.showConfirmDialog(null, "You won! Do you want to start a new maze?", "Victory", JOptionPane.YES_NO_OPTION);
                switch (choice) {
                    case 0:
                        disableAI();
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
