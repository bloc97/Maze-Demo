/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinthe;

/**
 *
 * @author Bowen Peng, Zhenglong, Devoir 2
 */
public class Muret {
    final public int x, y;
    final public boolean isHorz;
    private boolean isVisible;
    //Constructors for the walls
    public Muret(int x, int y, boolean isHorz) {
        this.x = x;
        this.y = y;
        this.isHorz = isHorz;
        show();
    }
    //function to hide the walls
    final public void hide() {
        isVisible = false;
    }
    //function to show the walls
    final public void show() {
        isVisible = true;
    }
    //Visibility check
    public boolean visible() {
        return isVisible;
    }

    //Check if the object is equal to a wall
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
