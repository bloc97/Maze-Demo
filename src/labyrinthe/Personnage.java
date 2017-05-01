/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinthe;

import java.awt.Graphics;

/**
 *
 * @author bowen
 */
public class Personnage {
    private int x,y;
    private int vies;
    
    public Personnage(int x, int y, int vies) {
        this.x = x;
        this.y = y;
        this.vies = vies;
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
    }
    public void setY(int k) {
        y = k;
    }
    public void addX(int k) {
        x = x+k;
    }
    public void addY(int k) {
        y = y+k;
    }
    public void dessine(Graphics g, int x1, int y1, int x2, int y2) {
        
    }
}
