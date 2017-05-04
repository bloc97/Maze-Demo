/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinthe;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import javax.swing.JComponent;
import static labyrinthe.Helper.createLos;

/**
 *
 * @author Bowen Peng, Zhenglong, Devoir 2
 */

//Afficher le labyrinthe
public class AffichageLaby extends JComponent {
    private Labyrinthe laby;
    private int w, h;


    public AffichageLaby() {
        //this.setBackground(new Color(241, 239, 236));
    }

    //Getter
    public Labyrinthe labyrinthe() {
        return laby;
    }
    //Setter
    public void setLabyrinthe(Labyrinthe labyrinthe) {
        laby = labyrinthe;
        repaint();
    }
    //Set la taille
    public void setCustomSize(int w, int h) {
        this.w = w;
        this.h = h;
        setPreferredSize(new Dimension(w, h));
    }
    
    //Override le paintComponent

    @Override
    protected void paintComponent(Graphics g) {
        prePaint();
        super.paintComponent(g);
        onPaint((Graphics2D)g);
        postPaint();
    }
    
    protected void prePaint() {
        
    };

    protected void onPaint(Graphics2D g2) {
        
        if (laby == null) {
            return;
        }
        
        double sqSize = Math.min(w/(laby.w()+2), h/(laby.h()+2));
        
        g2.setColor(new Color(20,12,20));
        g2.fillRect(0, 0, (int)sqSize*(laby.w()+2), (int)sqSize*(laby.h()+2));
        
        
        if (laby.murs() == null) {
            return;
        }
        //Paint les informations du AI
        if (laby.ai() != null) {
            if (laby.isAIenabled()) {
                laby.ai().paint(g2, sqSize);
            }
        }
        //Paint les murs
        float wallWidth = (float)sqSize/10;
        float wallShadowWidth = wallWidth*1.5f;
        if (wallShadowWidth >= 0.5) {
            g2.setStroke(new BasicStroke(wallShadowWidth));
            g2.setColor(new Color(60,181,155));
            for (Muret muret : laby.murs()) {
                if (muret.visible() && !muret.equals(laby.sortie())) {
                    double x0, y0, x1, y1;
                    x0 = (muret.x+1)*sqSize;
                    y0 = (muret.y+1)*sqSize;
                    if (muret.isHorz) {
                        x1 = (muret.x+2)*sqSize;
                        y1 = (muret.y+1)*sqSize;
                    } else {
                        x1 = (muret.x+1)*sqSize;
                        y1 = (muret.y+2)*sqSize;
                    }
                    Line2D.Double line = new Line2D.Double(x0, y0, x1, y1);

                    g2.draw(line);
                }
            }
        }
        g2.setStroke(new BasicStroke(wallWidth));
        g2.setColor(new Color(72,245,209));
        for (Muret muret : laby.murs()) {
            if (muret.visible() && !muret.equals(laby.sortie())) {
                double x0, y0, x1, y1;
                x0 = (muret.x+1)*sqSize;
                y0 = (muret.y+1)*sqSize;
                if (muret.isHorz) {
                    x1 = (muret.x+2)*sqSize;
                    y1 = (muret.y+1)*sqSize;
                } else {
                    x1 = (muret.x+1)*sqSize;
                    y1 = (muret.y+2)*sqSize;
                }
                Line2D.Double line = new Line2D.Double(x0, y0, x1, y1);
                
                g2.draw(line);
                //g2.drawLine(x0, y0, x1, y1);
            }
        }
        //Paint le texte
        if (laby.isGeneratingWalls()) {
            g2.setColor(Color.RED);
            g2.setFont(new Font("Courier", Font.BOLD, (int)(sqSize/1.5f)));
            g2.drawString("Please Wait...", (int)sqSize, (int)sqSize*(laby.h()+1.8f));
        } else if (laby.isGenerating()) {
            g2.setColor(Color.GREEN);
            g2.setFont(new Font("Courier", Font.BOLD, (int)(sqSize/4f)));
            g2.drawString("Remember the Maze, you have "+ laby.getShowTimeDelaySeconds() + " Seconds.", (int)sqSize, (int)sqSize*(laby.h()+1.8f));
        }
        
        
        if (laby.personnage() == null || laby.sortie() == null) {
            return;
        }
        //Paint le personnage et ses vies
        laby.personnage().dessine(g2, sqSize);
        g2.setColor(Color.yellow);
        if (laby.isAIenabled()) {
            g2.setFont(new Font("Courier", Font.BOLD, (int)(sqSize/1.5f)));
            g2.drawString("AI ENABLED", (int)sqSize, (int)sqSize*(0.8f));
        } else {
            for (int i=0; i<laby.personnage().vies(); i++) {
                g2.fill(createLos(new Point((int)((i+1.2f)*sqSize), (int)((0.5)*sqSize)), new Point((int)((i+2.2f)*sqSize), (int)((0.5)*sqSize))));
            }
        }
        
        
        
    };
    protected void postPaint() {
        
    };
}
