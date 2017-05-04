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
 * @author bowen
 */
public class JeuLaby {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
        /*DisplayMode[] dms = gd.getDisplayModes();
        for (DisplayMode mode : dms) {
            System.out.println(mode.getWidth() + " x " + mode.getHeight() + " " + mode.getBitDepth() + "b @" + mode.getRefreshRate());
        }*/

        //Init parameters
        float density = 0.5f;
        int l = 20;
        int h = 20;
        long delayms = 1000l;
        int lives = 5;
        
        
        

        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        int defaultW = width*9/10;
        int defaultH = height*9/10;
        JFrameLaby frame = new JFrameLaby(defaultW, defaultH);
        JPanelLaby panel = new JPanelLaby(defaultW, defaultH, l, h, density, GeneratorType.NAIVEUNIFORM, delayms, lives, 10, AIType.GREEDYFILL);
        frame.add(panel);
        panel.setFocusable(true);
        panel.thread.start();
        //panel.generateNewMaze(l, h, density, GeneratorType.RECURSIVE, delayMs, lives, 2);
        
        
    }
    
    
}
