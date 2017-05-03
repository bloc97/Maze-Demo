/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinthe;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import javax.swing.JComponent;

/**
 *
 * @author bowen
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
<<<<<<< HEAD
    
    
=======
    //Override le paintComponent
>>>>>>> origin/master
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
        
        //Color lastColor = g2.getColor();
        //g2.drawRect(0, 0, (int)sqSize*(laby.w()+2), (int)sqSize*(laby.h()+2));
        
        g2.setColor(new Color(20,12,20));
        g2.fillRect(0, 0, (int)sqSize*(laby.w()+2), (int)sqSize*(laby.h()+2));
        
        //g2.setColor(new Color(84,115,108));
        //g2.fillRect((int)sqSize*(1), (int)sqSize*(1), (int)sqSize*(laby.w()), (int)sqSize*(laby.h()));
        
        //g2.setColor(lastColor);
        
        if (laby.murs() == null) {
            return;
        }
        
        //Stroke lastStroke = g2.getStroke();
        
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
        
        if (laby.ai() == null) {
            return;
        }
        laby.ai().paint(g2, sqSize);
        
        if (laby.personnage() == null || laby.sortie() == null) {
            return;
        }
        //g2.setStroke(lastStroke);
        laby.personnage().dessine(g2, sqSize);
        g2.setColor(Color.yellow);
        g2.drawString("Vies: " + laby.personnage().vies(), 20, 20);
        
        
        
    };
    protected void postPaint() {
        
    };
}
