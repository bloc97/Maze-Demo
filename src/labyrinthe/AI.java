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
 * @author Bowen Peng, Zhenglong, Devoir 2
 */
public interface AI { //Interface AI pour tout les types de AI
    public enum AIType {
        NAIVEWALL, MEMORYWALL, MEMORYFILLWALL, GREEDYFILL, DEPTHFIRST, BREADTHFIRST //Types de AI
        //TODO: Dead end fill, General DIJKSTRA, A*
    }
    
    public static AI getNewAI(AIType type) { //Cree le AI avec le AIType
        switch (type) {
            case NAIVEWALL:
                return new AITurn();
            case MEMORYWALL:
                return new AITurn(true);
            case MEMORYFILLWALL:
                return new AITurn(true, true);
            case GREEDYFILL:
                return new AIGreedyFloodFill();
            case DEPTHFIRST:
                return new AIDepthFirst();
            case BREADTHFIRST:
                return new AIBreadthFirst();
            default :
                return new AITurn();
        }
    }
    
    public char getNextDirection(int x, int y, int w, int h, ListeMuret murs, Muret sortie, JComponent affichage, int animWait); //Retourner le prochain movement (si il y a un path trouve)
    public void paint(Graphics2D g2, double sqSize); //Paint pour le AI, display l'information
}
