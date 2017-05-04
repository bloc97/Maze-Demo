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
public class ListeMuret implements Iterable<Muret> {
    //First node attribute
    private NoeudMuret first;
    
    public ListeMuret() {
    }

    //Constructor
    public ListeMuret(NoeudMuret first) {
        this.first = first;
    }
    //Push fucntion allow to add
    public void push(Muret mur) {
        first = new NoeudMuret(mur, first);
    }
    public Muret pop() {
        Muret mur = first.get();
        first = first.next();
        return mur;
    }
    //Check if can remove a wall
    public boolean remove(Muret mur) {
        NoeudMuret currentNoeud = first;
        while(currentNoeud.next() instanceof NoeudMuret) {
            if (currentNoeud.next().get().equals(mur)) {
                currentNoeud.setNext(currentNoeud.next().next());
                return true;
            }
            currentNoeud = currentNoeud.next();
        }
        return false;
    }
    //Return the size of a wall
    public int size() {
        int size = 0;
        for (Muret mur : this) {
            size++;
        }
        return size;
    }
    //Iterator
    public Iterator<Muret> iterator() {
        return new IteratorMuret(first);
    }
    public void hide() {
        for (Muret thisMur: this) {
            thisMur.hide();
        }
    }
    //Display the walls
    public void show() {
        for (Muret thisMur: this) {
            thisMur.show();
        }
    }
    //Look for a wall
    public Muret chercheMuret(Muret mur) {
        for (Muret thisMur : this) {
            if (thisMur.equals(mur)) {
                return thisMur;
            }
        }
        return null;
    }
    //look for a specific wall
    public Muret chercheMuret(int x, int y, boolean isHorz) {
        return chercheMuret(new Muret(x, y, isHorz));
    }
    
    public Muret chercheMuret(int x, int y, int direction) {
        switch(direction) {
            case 0:
                return chercheMuret(x, y, true);
            case 2:
                return chercheMuret(x, y+1, true);
            case 3:
                return chercheMuret(x, y, false);
            case 1:
                return chercheMuret(x+1, y, false);
            default :
                return chercheMuret(x, y, (direction+4)%4);
        }
    }
    
    //Look for a specific wall using specific input chars
    
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
