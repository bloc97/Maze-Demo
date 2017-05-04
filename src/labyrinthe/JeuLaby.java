/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinthe;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import javax.swing.JComboBox;
import labyrinthe.AI.AIType;
import labyrinthe.RandomGenerators.GeneratorType;

/**
 *
 * @author Bowen Peng, Zhenglong, Devoir 2
 */
public class JeuLaby {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice(); //Trouver la taille de l'ecran
        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        int defaultW = width*9/10; //Afficher un window un peu plus petit
        int defaultH = height*9/10;
        
        //Valeurs defaut
        float density = 0.3f;
        int l = 12;
        int h = 12;
        long delayms = 3000l;
        int lives = 5;
        
        if (args.length >= 2) {
            h = Integer.parseInt(args[0]);
            l = Integer.parseInt(args[1]);
        }
        if (args.length == 5) {
            density = Float.parseFloat(args[2]);
            delayms = Long.parseLong(args[3])*1000;
            lives = Integer.parseInt(args[4]);
        }
        
        //Creer le Frame, Panel et start le thread leger
        JFrameLaby frame = new JFrameLaby(defaultW, defaultH);
        JPanelLaby panel = new JPanelLaby(defaultW, defaultH, l, h, density, GeneratorType.NAIVEUNIFORM, delayms, lives, 10, AIType.GREEDYFILL);
        frame.add(panel);
        
        panel.setFocusable(true);
        
        panel.thread.start();
        
        
        
    }
    
    
}
