/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinthe;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;

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


        int width = gd.getDisplayMode().getWidth();
        int height = gd.getDisplayMode().getHeight();
        int defaultW = width*9/10;
        int defaultH = height*9/10;
        JFrameLaby frame = new JFrameLaby(defaultW, defaultH);
        JPanelLaby panel = new JPanelLaby(defaultW, defaultH);
        frame.add(panel);
        panel.setFocusable(true);
        //panel.launch();
        
        panel.generateNewMaze(20, 20, 1f, 1000l, 5, 2);
        
        
    }
    
    
}
