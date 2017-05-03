/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinthe;

import java.awt.Graphics2D;
import javax.swing.JComponent;

/**
 *
 * @author bowen
 */
public interface AI {
    public enum AIType {
        NAIVEWALL, MEMORYWALL, PLEDGEWALL, PMWALL, GREEDYFILL, DEPTHFIRST, BREADTHFIRST
        //DEAD end Fill, DIJKSTRA, ASTAR
    }
    
    public static AI getNewAI(AIType type) {
        switch (type) {
            case NAIVEWALL:
                return new AITurn();
            case MEMORYWALL:
                return new AITurn(true);
            case PLEDGEWALL:
                return new AITurn(false, true);
            case PMWALL:
                return new AITurn(true, true);
            case GREEDYFILL:
                return new AIGreedyFloodFill();
            case DEPTHFIRST:
                return new AIDepthFirst();
            case BREADTHFIRST:
                return new AIBreadthFirst();
            default :
                return new AIGreedyFloodFill();
        }
    }
    
    public char getNextDirection(int x, int y, int w, int h, ListeMuret murs, Muret sortie, JComponent affichage, int animWait);
    public void paint(Graphics2D g2, double sqSize);
}
