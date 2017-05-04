/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinthe;

import java.util.Iterator;

/**
 *
 * @author Bowen Peng, Zhenglong, Devoir 2
 */
//Iterator to parse through all of the nodes
public class IteratorMuret implements Iterator<Muret> { //Iterateur pour Muret
    //Declare the node attribute
    private NoeudMuret node;
    //Assign the node to the first node
    public IteratorMuret(NoeudMuret premier) {
        node = premier;
    }
    
    //Check if it has a next Node
    @Override
    public boolean hasNext() {
        return node != null;
    }

    //Goto next node
    @Override
    public Muret next() {
        Muret mur = node.get();
        node = node.next();
        return mur;
    }

    
}
