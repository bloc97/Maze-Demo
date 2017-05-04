/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinthe;

import java.util.LinkedList;

/**
 *
 * @author Bowen Peng, Zhenglong, Devoir 2
 */
//Wall nodes
public class NoeudMuret {
    final private Muret element;
    private NoeudMuret suivant;

    //Constructor
    public NoeudMuret(Muret element, NoeudMuret next) {
        this.element = element;
        this.suivant = next;
    }
    //Get walls
    public Muret get() {
        return element;
    }
    //Next wall (node)
    public NoeudMuret next() {
        return suivant;
    }
    //Set next wall
    public void setNext(NoeudMuret next) {
        this.suivant = next;
    }
}
