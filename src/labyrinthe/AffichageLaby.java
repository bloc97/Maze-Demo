/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinthe;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import javax.swing.JComponent;

/**
 *
 * @author bowen
 */
public class AffichageLaby extends JComponent {
    private Labyrinthe laby;
    private int w, h;
    public void setLabyrinthe(Labyrinthe labyrinthe) {
        laby = labyrinthe;
    }
    public void setCustomSize(int w, int h) {
        this.w = w;
        this.h = h;
        setPreferredSize(new Dimension(w, h));
    }
    
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
        int sqSize = Math.min(w/(laby.l()+2), h/laby.h());
        for (Muret muret : laby.murs) {
            if (muret.visible()) {
                int x0, y0, x1, y1;
                x0 = muret.x*sqSize;
                y0 = muret.y*sqSize;
                if (muret.isHorz) {
                    x1 = (muret.x+1)*sqSize;
                    y1 = (muret.y)*sqSize;
                } else {
                    x1 = (muret.x)*sqSize;
                    y1 = (muret.y+1)*sqSize;
                }
                g2.drawLine(x0, y0, x1, y1);
            }
        }
    };
    protected void postPaint() {
        
    };
}
