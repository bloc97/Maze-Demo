/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinthe;

/**
 *
 * @author bowen
 */
public class JeuLaby {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        JFrameLaby frame = new JFrameLaby(800, 600);
        JPanelLaby panel = new JPanelLaby(800, 600);
        frame.add(panel);
        
        Labyrinthe laby = new Labyrinthe(30, 30, 0.1f, 1000l, 5);
        panel.setLabyrinthe(laby);
        
    }
    
}
