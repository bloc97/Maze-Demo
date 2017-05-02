/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinthe;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.util.LinkedList;

/**
 *
 * @author bowen
 */
public class Personnage {
    private int x,y;
    private int vies;
    
    private LinkedList<Point> history;
    
    public Personnage(int x, int y, int vies) {
        this.x = x;
        this.y = y;
        this.vies = vies;
        history = new LinkedList<>();
        history.add(new Point(x, y));
    }
    
    public int vies() {
        return vies;
    }
    public void hurt() {
        vies--;
    }
    public int x() {
        return x;
    }
    public int y() {
        return y;
    }
    public void setX(int k) {
        x = k;
        history.add(new Point(x, y));
    }
    public void setY(int k) {
        y = k;
        history.add(new Point(x, y));
    }
    public void addX(int k) {
        setX(x + k);
    }
    public void addY(int k) {
        setY(y + k);
    }
    public void dessine(Graphics2D g2, double sqSize) {
        
        double paddingSize = (double)sqSize/2;
        Ellipse2D.Double oval = new Ellipse2D.Double((x+1)*sqSize + paddingSize/2, (y+1)*sqSize + paddingSize/2, sqSize - paddingSize, sqSize - paddingSize);
        g2.setColor(new Color(178,242,236));
        g2.fill(oval);
        double diffSize = paddingSize/1.5;
        Ellipse2D.Double oval2 = new Ellipse2D.Double((x+1)*sqSize + diffSize/2, (y+1)*sqSize + diffSize/2, sqSize - diffSize, sqSize - diffSize);
        g2.setStroke(new BasicStroke((float)sqSize/12));
        g2.setColor(new Color(121,166,162));
        g2.draw(oval2);
        
        if (sqSize <= 10) {
            g2.setColor(Color.RED);
            g2.fill(oval);
        }
        
    }
}
