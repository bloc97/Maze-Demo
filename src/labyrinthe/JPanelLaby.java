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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 *
 * @author bowen
 */
//Redefine the JPanel for maze
public class JPanelLaby extends JPanel {

    //Attributes
    private int xsize, ysize;
    private final AffichageLaby affichageLaby;

    //Constructor
    public JPanelLaby(int xsize, int ysize) {
        this.xsize = xsize;
        this.ysize = ysize;
        
        //Set the layout
        this.setLayout(new BorderLayout(0, 0));
        //Display maze
        affichageLaby = new AffichageLaby();
        affichageLaby.setPreferredSize(new Dimension(xsize-200, ysize));
        this.add(affichageLaby, BorderLayout.CENTER);
        //Add buttons and event listeners
        addButtons();
        addEventListeners();
    }

    //Method to add button
    private void addButtons() {
        JPanel movementButtons = new JPanel();
        movementButtons.add(new JButton("hello"));
        movementButtons.add(new JButton("hello2"));
        movementButtons.add(new JButton("hello3"));
        this.add(movementButtons, BorderLayout.LINE_END);
    }

    //Events
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
                int sqCustomSize = Math.min(xsize-200, ysize);
                affichageLaby.setCustomSize(sqCustomSize, sqCustomSize);
                thisScene.updateUI();
            }
        });
        
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyChar()) {
                    case 'w':
                        affichageLaby.labyrinthe().deplace('H');
                        break;
                    case 's':
                        affichageLaby.labyrinthe().deplace('B');
                        break;
                    case 'a':
                        affichageLaby.labyrinthe().deplace('G');
                        break;
                    case 'd':
                        affichageLaby.labyrinthe().deplace('D');
                        break;
                    case ' ':
                        affichageLaby.labyrinthe().stepAI(affichageLaby);
                        break;
                }
                //System.out.println(e.getKeyChar());
                repaint();
            }

            @Override
            public void keyReleased(KeyEvent ke) {
            }
        });
    }
    //Generate the maze
    public void generateNewMaze(int w, int h, float density, long delayms, int lives, double animSeconds) {
        affichageLaby.setLabyrinthe(new Labyrinthe(w, h, density, delayms, lives));
        affichageLaby.labyrinthe().generate(affichageLaby, animSeconds);
    }

    
}
