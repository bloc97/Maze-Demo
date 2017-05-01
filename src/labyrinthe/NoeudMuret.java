/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinthe;

import java.util.LinkedList;

/**
 *
 * @author bowen
 */
public class NoeudMuret {
    final private Muret element;
    private NoeudMuret suivant;
    public NoeudMuret(Muret element, NoeudMuret next) {
        this.element = element;
        this.suivant = next;
    }
    public Muret get() {
        return element;
    }
    public NoeudMuret next() {
        return suivant;
    }
    public void setNext(NoeudMuret next) {
        this.suivant = next;
    }
}
