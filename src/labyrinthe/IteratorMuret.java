/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinthe;

import java.util.Iterator;

/**
 *
 * @author bowen
 */
public class IteratorMuret implements Iterator<Muret> {
    
    private NoeudMuret node;
    
    public IteratorMuret(NoeudMuret premier) {
        node = premier;
    }
    
    @Override
    public boolean hasNext() {
        return node != null;
    }

    @Override
    public Muret next() {
        Muret mur = node.get();
        node = node.next();
        return mur;
    }

    
}
