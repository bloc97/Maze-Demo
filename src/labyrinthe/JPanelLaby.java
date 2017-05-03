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
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import labyrinthe.RandomGenerators.GeneratorType;

/**
 *
 * @author bowen
 */
//Redefine the JPanel for maze
public class JPanelLaby extends JPanel {

    //Attributes
    private int xsize, ysize;
    private final AffichageLaby affichageLaby;
    private final JPanel controlPanel;

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
        controlPanel = new JPanel();
        
        addButtons();
        addEventListeners();
    }
    
    public JPanel controlPanel() {
        return controlPanel;
    }
    
    //Method to add button
    private void addButtons() {
        JPanel movementButtons = controlPanel;
        movementButtons.setPreferredSize(new Dimension(200, 0));
        FlowLayout experimentLayout = new FlowLayout(FlowLayout.CENTER);
        movementButtons.setLayout(experimentLayout);
        movementButtons.add(new JButton("                    H                    "));
        movementButtons.add(new JButton("G"));
        movementButtons.add(new JButton("D"));
        movementButtons.add(new JButton("                    B                    "));
        Vector<String> types = new Vector<>();
        types.add("Naive Uniform");
        //movementButtons.add(myCombo);
        this.add(movementButtons, BorderLayout.LINE_END);
        
        invalidate();
        updateUI();
        repaint();
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
    public void generateNewMaze(int w, int h, float density, GeneratorType type, long delayms, int lives, double animSeconds) {
        if (w < 3) {
            System.out.println("Width cannot be smaller than 3!");
            w = 3;
        }
        if (h < 3) {
            System.out.println("Height cannot be smaller than 3!");
            h = 3;
        }
        if (w > 500) {
            System.out.println("Width cannot be bigger than 500!");
            w = 500;
        }
        if (h > 500) {
            System.out.println("Height cannot be bigger than 500!");
            h = 500;
        }
        affichageLaby.setLabyrinthe(new Labyrinthe(w, h, density, delayms, lives));
        affichageLaby.labyrinthe().generate(type, affichageLaby, animSeconds);
    }

    
}
