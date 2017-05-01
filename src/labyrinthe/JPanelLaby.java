/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinthe;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 *
 * @author bowen
 */
public class JPanelLaby extends JPanel{
    
    private int xsize, ysize;
    private final AffichageLaby affichageLaby;
    
    public JPanelLaby(int xsize, int ysize) {
        this.xsize = xsize;
        this.ysize = ysize;
        this.setLayout(new BorderLayout(0, 0));
        affichageLaby = new AffichageLaby();
        affichageLaby.setPreferredSize(new Dimension(xsize-200, ysize));
        this.add(affichageLaby, BorderLayout.CENTER);
        addButtons();
        addEventListeners();
    }
    
    private void addButtons() {
        JPanel movementButtons = new JPanel();
        movementButtons.add(new JButton("hello"));
        movementButtons.add(new JButton("hello2"));
        movementButtons.add(new JButton("hello3"));
        this.add(movementButtons, BorderLayout.LINE_END);
    }
    
    private void addEventListeners() {
        
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Rectangle r = e.getComponent().getBounds();
                int h = r.height;
                int w = r.width;
                JPanelLaby thisScene = ((JPanelLaby)(e.getComponent()));
                thisScene.xsize = w;
                thisScene.ysize = h;
                affichageLaby.setCustomSize(xsize-200, ysize);
                thisScene.updateUI();
            }
        });
    }
    
    public void setLabyrinthe(Labyrinthe labyrinthe) {
        affichageLaby.setLabyrinthe(labyrinthe);
    }
    
    
}
