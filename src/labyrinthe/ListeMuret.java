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
    public boolean remove(Muret mur) {
        NoeudMuret currentNoeud = first;
        while(currentNoeud.next() instanceof NoeudMuret) {
            if (currentNoeud.next().get().equals(mur)) {
                currentNoeud.setNext(currentNoeud.next().next());
                return true;
            }
        }
        return false;
    }
    public int size() {
        int size = 0;
        for (Muret mur : this) {
            size++;
        }
        return size;
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
    public Muret chercheMuret(int x, int y, boolean isHorz) {
        return chercheMuret(new Muret(x, y, isHorz));
    }
    public Muret chercheMuret(int x, int y, char cardinalDirection) {
        switch(cardinalDirection) {
            case 'N':
                return chercheMuret(x, y, true);
            case 'S':
                return chercheMuret(x, y+1, true);
            case 'W':
                return chercheMuret(x, y, false);
            case 'E':
                return chercheMuret(x+1, y, false);
            default :
                throw new UnsupportedOperationException("Cannot search cardinal direction " + cardinalDirection);
        }
    }
    
}
