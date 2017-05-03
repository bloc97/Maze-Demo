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
        NAIVEWALL, GREEDYFILL, DEADENDFILL, DEPTHFIRST, BREADTHFIRST, DIJKSTRA, ASTAR, 
    }
    
    public char getNextDirection(int x, int y, int w, int h, ListeMuret murs, Muret sortie, JComponent affichage);
    public void paint(Graphics2D g2, double sqSize);
}
