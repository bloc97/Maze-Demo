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
public class Labyrinthe {
    private int l, h;
    private Personnage pers;
    private ListeMuret murs;
    private Muret sortie;
    public Labyrinthe(int w, int h, float density, long delayms, int lives) {
        System.out.println("Initialising Maze...");
        this.l = w;
        this.h = h;
        murs = generateWalls(w, h, density);
        int[] pos = generatePossiblePositions(w, h, murs);
        System.out.println("Starting Maze.");
        pers = new Personnage(pos[0], pos[1], lives);
        sortie = new Muret(pos[2], pos[3], pos[4] == 1);
    }
    public static ListeMuret generateWalls(int w, int h, float density) {
        System.out.print("Generating World... ");
        
        ListeMuret liste = new ListeMuret();
        
        
        for (int x=0; x<w; x++) { //Murets horizontaux
            for (int y=0; y<=h; y++) {
                if (y == 0 || y == h) {
                    liste.push(new Muret(x, y, true));
                } else {
                    if (Math.random() < density) {
                        liste.push(new Muret(x, y, true));
                    }
                }
            }
        }
        
        for (int x=0; x<=w; x++) { //Murets verticaux
            for (int y=0; y<h; y++) {
                if (x == 0 || x == h) {
                    liste.push(new Muret(x, y, false));
                } else {
                    if (Math.random() < density) {
                        liste.push(new Muret(x, y, false));
                    }
                }
            }
        }
        
        System.out.println("Done.");
        
        return liste;
    }
    public static int[] generatePossiblePositions(int w, int h, ListeMuret murs) { //Trouver les positions possibles pour le personnage et la sortie (pas de labyrinthe impossible)
        
        System.out.print("Generating Exit Position... ");
        //Generate exit
        int lastRow = h-1;
        int lastCol = w-1;
        
        int maxX = lastCol;
        int maxY = h;
        boolean isHorz = true;
        boolean[][] maxGrid = getFloodFill(lastCol, lastRow, w, h, murs);
        int maxFill = getFillSize(maxGrid);
        
        int maxExitProgress = h/2 + w/2;
        int exitProgress = 0;
        
        if (maxFill > (h*w)/4) { //If the corner exit is large enough
            
        } else {
            for (int i=h/2; i<h; i++) { //Right side openings
                boolean[][] grid = getFloodFill(lastCol, i, w, h, murs);
                int fillSize = getFillSize(grid);
                //printFill(grid);
                //System.out.println(fillSize);
                if (maxFill <= fillSize) {
                    maxX = w;
                    maxY = i;
                    maxFill = fillSize;
                    isHorz = false;
                    maxGrid = grid;
                }
                exitProgress++;
                System.out.println(((double)exitProgress/maxExitProgress)*100 + "%");
            }
            for (int i=w/2; i<w; i++) { //Bottom side openings
                boolean[][] grid = getFloodFill(i, lastRow, w, h, murs);
                int fillSize = getFillSize(grid);
                //printFill(grid);
                //System.out.println(fillSize);
                if (maxFill <= fillSize) {
                    maxX = i;
                    maxY = h;
                    maxFill = fillSize;
                    isHorz = true;
                    maxGrid = grid;
                }
                exitProgress++;
                System.out.println(((double)exitProgress/maxExitProgress)*100 + "%");
            }
        }
        
        System.out.println("Done.");
        //Generate starting position
        System.out.print("Generating Starting Position... ");
        //Get furthest point from exit (Von Neumann distance)
        int ptX = 0;
        int ptY = 0;
        int lastDist = 0;
        for (int x=0; x<w; x++) {
            for (int y=0; y<h; y++) {
                if (maxGrid[x][y]) {
                    int currentDist = (w-x)+(h-y); //Von Neumann distance
                    if (currentDist > lastDist) {
                        lastDist = currentDist;
                        ptX = x;
                        ptY = y;
                    }
                }
            }
        }
        
        final int r = (w+h)/8; //Random distance around the furthest point
        
        while(true) {
            int rX = ptX + (int)(Math.random()*r);
            int rY = ptY + (int)(Math.random()*r);
            if (rX >= w) rX = w-1;
            if (rY >= h) rY = h-1;
            if (maxGrid[rX][rY]) {
                System.out.println("Done.");
                return new int[] {rX, rY, maxX, maxY, (isHorz) ? 1 : 0};
            }
        }
        
    }
    public static void printFill(boolean[][] grid) {
        for (int y=0; y<grid[0].length; y++) {
            for (int x=0; x<grid.length; x++) {
                System.out.print((grid[x][y]) ? "1" : "0");
            }
            System.out.println("");
        }
    }
    public static int getFillSize(boolean[][] grid) {
        int total = 0;
        for (int x=0; x<grid.length; x++) {
            for (int y=0; y<grid[0].length; y++) {
                if (grid[x][y]) {
                    total++;
                }
            }
        }
        return total;
    }
    public static boolean[][] getFloodFill(int x, int y, int w, int h, ListeMuret murs) {
        boolean[][] grid = new boolean[w][h];
        recursiveFloodFill(x, y, grid, murs);
        return grid;
    }
    public static void recursiveFloodFill(int x, int y, boolean[][] grid, ListeMuret murs) {
        if (grid[x][y] || x < 0 || x >= grid.length || y < 0 || y >= grid[0].length) {
            return;
        }
        grid[x][y] = true;
        if (murs.chercheMuret(new Muret(x, y, true)) == null) { //No top wall
            recursiveFloodFill(x, y-1, grid, murs);
        }
        if (murs.chercheMuret(new Muret(x, y+1, true)) == null) { //No bottom wall
            recursiveFloodFill(x, y+1, grid, murs);
        }
        if (murs.chercheMuret(new Muret(x, y, false)) == null) { //No left wall
            recursiveFloodFill(x-1, y, grid, murs);
        }
        if (murs.chercheMuret(new Muret(x+1, y, false)) == null) { //No right wall
            recursiveFloodFill(x+1, y, grid, murs);
        }
        
    }
    public int w() {
        return l;
    }
    public int h() {
        return h;
    }
    public Personnage personnage() {
        return pers;
    }
    public ListeMuret murs() {
        return murs;
    }
    public Muret sortie() {
        return sortie;
    }
    public boolean canDeplace(char direction) {
        switch(direction) {
            case 'H':
                for (Muret mur : murs) {
                    if (pers.x() == mur.x && pers.y() == mur.y && mur.isHorz) {
                        return false;
                    }
                }
                break;
            case 'B':
                for (Muret mur : murs) {
                    if (pers.x() == mur.x && pers.y() == mur.y+1 && mur.isHorz) {
                        return false;
                    }
                }
                break;
            case 'G':
                for (Muret mur : murs) {
                    if (pers.x() == mur.x && pers.y() == mur.y && !mur.isHorz) {
                        return false;
                    }
                }
                break;
            case 'D':
                for (Muret mur : murs) {
                    if (pers.x() == mur.x+1 && pers.y() == mur.y && !mur.isHorz) {
                        return false;
                    }
                }
                break;
            default :
                throw new UnsupportedOperationException("Cannot move to direction " + direction);
        }
        return true;
    }
    public boolean deplace(char direction) {
        if (canDeplace(direction)) {
        switch(direction) {
            case 'H':
                pers.addY(-1);
                break;
            case 'B':
                pers.addY(1);
                break;
            case 'G':
                pers.addX(-1);
                break;
            case 'D':
                pers.addX(1);
                break;
            }
            return true;
        } else {
            return false;
        }
    }
    
}
