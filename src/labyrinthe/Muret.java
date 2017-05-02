/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinthe;

/**
 *
 * @author bowen
 */
public class Muret {
    final public int x, y;
    final public boolean isHorz;
    private boolean isVisible;
    public Muret(int x, int y, boolean isHorz) {
        this.x = x;
        this.y = y;
        this.isHorz = isHorz;
        show();
    }
    final public void hide() {
        isVisible = false;
    }
    final public void show() {
        isVisible = true;
    }
    public boolean visible() {
        return isVisible;
    }
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Muret) {
            Muret mur = (Muret)obj;
            if (mur.x == x && mur.y == y && mur.isHorz == isHorz) {
                return true;
            }
        }
        return false;
    }

}
