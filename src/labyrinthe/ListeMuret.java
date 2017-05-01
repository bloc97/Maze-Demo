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
public class ListeMuret implements Iterable<Muret> {
    private NoeudMuret first;
    
    public ListeMuret() {
    }
    public ListeMuret(NoeudMuret first) {
        this.first = first;
    }
    public void push(Muret mur) {
        first = new NoeudMuret(mur, first);
    }
    public Muret pop() {
        Muret mur = first.get();
        first = first.next();
        return mur;
    }
    @Override
    public Iterator<Muret> iterator() {
        return new IteratorMuret(first);
    }
    public void hide() {
        for (Muret thisMur: this) {
            thisMur.hide();
        }
    }
    public void show() {
        for (Muret thisMur: this) {
            thisMur.show();
        }
    }
    public Muret chercheMuret(Muret mur) {
        for (Muret thisMur : this) {
            if (thisMur.equals(mur)) {
                return thisMur;
            }
        }
        return null;
    }
    
}
