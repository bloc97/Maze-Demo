/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinthe;

import javax.swing.JFrame;

/**
 *
 * @author Bowen Peng, Zhenglong, Devoir 2
 */
//Redefine the Jframe for maze
public class JFrameLaby extends JFrame {

    //Constuct the frame using a certain x and y size
    public JFrameLaby(int xsize, int ysize) { //JFrame pour le 'jeu'
        this.setTitle("Maze");
        this.setSize(xsize, ysize);
        
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        
    }   
}
