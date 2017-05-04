/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinthe;

import java.awt.Point;
import static java.lang.Thread.sleep;
import java.util.Collections;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import static labyrinthe.Helper.getFillSize;
import static labyrinthe.Helper.getFloodFill;
import static labyrinthe.Helper.getMuretFromDirection;
import static labyrinthe.Helper.getPointFromDirection;
import static labyrinthe.Helper.hasUnvisited;
import static labyrinthe.Helper.isBorderMuret;
import static labyrinthe.Helper.isVisited;
import static labyrinthe.Helper.randomRange;

/**
 *
 * @author Bowen Peng, Zhenglong, Devoir 2
 */
public abstract class RandomGenerators {
    //Rng for walls
    
    public enum GeneratorType {
        NAIVEUNIFORM, RECURSIVE, DEPTHFIRST, PRIM;
    }
    
    public static void uniformGenerateWalls(int w, int h, float density, ListeMuret liste, JComponent affichage, int waitms, boolean doAnim) { //Naive uniform generation
        System.out.println("Generating World...");
        
        
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
            try {
                affichage.repaint();
                sleep(waitms);
            } catch (InterruptedException ex) {
                Logger.getLogger(RandomGenerators.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        for (int x=0; x<=w; x++) { //Murets verticaux
            for (int y=0; y<h; y++) {
                if (x == 0 || x == w) {
                    liste.push(new Muret(x, y, false));
                } else {
                    if (Math.random() < density) {
                        liste.push(new Muret(x, y, false));
                    }
                }
            }
            
            try {
                affichage.repaint();
                sleep(waitms);
            } catch (InterruptedException ex) {
                Logger.getLogger(RandomGenerators.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("Done.");
        
    }
    
    public static void recursiveGenerateWalls(int w, int h, float density, ListeMuret liste, JComponent affichage, int waitms, boolean doAnim) { //Recursive generation, separate the maze in two recursively until the separation is smaller than 1.
        System.out.println("Generating World...");
        
        
        for (int i=0; i<w; i++) { //Murs horizontaux
            liste.push(new Muret(i, 0, true));
            liste.push(new Muret(i, h, true));
        }
        for (int i=0; i<w; i++) { //Murs verticaux
            liste.push(new Muret(0, i, false));
            liste.push(new Muret(w, i, false));
        }
        
        rGenW(0, 0, w, h, density, liste, affichage, waitms, doAnim);
        
        System.out.println("Done.");
        
    }
    private static void rGenW(int x0, int y0, int x1, int y1, float density, ListeMuret liste, JComponent affichage, int waitms, boolean doAnim) {
        
        if (x1-x0 <= 1 || y1-y0 <= 1) {
            return;
        }
        
        boolean isHorz = false;
        
        if (x1-x0 < y1-y0) { //If the area has more height
            isHorz = true;
        } else if (x1-x0 > y1-y0) { //If the area has more width
            isHorz = false;
        } else if (Math.random() < 0.5) {
            isHorz = true;
        }
        
        if (isHorz) {
            
            if (y1-y0 <= 1) {
                return;
            }
            
            int randY = Helper.randomRange(y0+1, y1-1);
            
            final LinkedList<Integer> horzWalls = new LinkedList(); //Generate walls based on density, less density, more holes.
            for (int i=x0; i<x1; i++) {
                horzWalls.add(i);
            }
            int maxWalls = x1-x0-1;
            int walls = (int)(density*maxWalls);
            if (walls == 0) walls = (Math.random() < density) ? 1 : 0;
            
            int addedWallsCount = 0;
            
            Collections.shuffle(horzWalls);
            
            for (Integer val : horzWalls) {
                if (addedWallsCount >= walls) {
                    break;
                }
                liste.push(new Muret(val, randY, true));
                addedWallsCount++;
            }
            
            /*
            int randXopening = randomRange(x0, x1-1);
            
            for (int i=x0; i<x1; i++) {
                if (i != randXopening) {
                    liste.push(new Muret(i, randY, true));
                }
            }*/

            if (doAnim) {
                try {
                    affichage.repaint();
                    sleep(waitms);
                } catch (InterruptedException ex) {
                    Logger.getLogger(RandomGenerators.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            rGenW(x0, y0, x1, randY, density, liste, affichage, waitms, doAnim);
            rGenW(x0, randY, x1, y1, density, liste, affichage, waitms, doAnim);
            
        } else {
            
            if (x1-x0 <= 1) {
                return;
            }
            
            int randX = Helper.randomRange(x0+1, x1-1);
            
            final LinkedList<Integer> vertWalls = new LinkedList(); //Generate walls based on density, less density, more holes.
            for (int i=y0; i<y1; i++) {
                vertWalls.add(i);
            }
            int maxWalls = y1-y0-1;
            int walls = (int)(density*maxWalls);
            if (walls == 0) walls = (Math.random() < density) ? 1 : 0;
            
            int addedWallsCount = 0;
            
            Collections.shuffle(vertWalls);
            
            for (Integer val : vertWalls) {
                if (addedWallsCount >= walls) {
                    break;
                }
                liste.push(new Muret(randX, val, false));
                addedWallsCount++;
            }
            
            /*
            int randYopening = randomRange(y0, y1-1);
            
            for (int i=y0; i<y1; i++) {
                if (i != randYopening) {
                    liste.push(new Muret(randX, i, false));
                }
            }*/

            if (doAnim) {
                try {
                    affichage.repaint();
                    sleep(waitms);
                } catch (InterruptedException ex) {
                    Logger.getLogger(RandomGenerators.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            rGenW(x0, y0, randX, y1, density, liste, affichage, waitms, doAnim);
            rGenW(randX, y0, x1, y1, density, liste, affichage, waitms, doAnim);
            
        }
        
        
        
    }
    
    
    public static void depthFirstGenerateWalls(int w, int h, float density, ListeMuret liste, JComponent affichage, int waitms, boolean doAnim) { //Recursive generation, separate the maze in two recursively until the separation is smaller than 1.
        System.out.println("Generating World...");
        
        fillWallsHide(w, h, liste);
        
        //dFrGenW(randomRange(0, w-1), randomRange(0, h-1), new boolean[w][h], liste, affichage, sleepTimeMs, sleepTimeNs);
        dFGenW(w, h, density, liste, affichage, waitms, doAnim);
        
        
        System.out.println("Done.");
        
    }
    private static void dFGenW(int w, int h, float density, ListeMuret liste, JComponent affichage, int waitms, boolean doAnim) { //Iterative version, longer but no stack overflow.
        
        boolean[][] visited = new boolean[w][h];
        
        Point currentPoint = new Point(randomRange(0, w-1), randomRange(0, h-1));
        LinkedList<Point> stack = new LinkedList<>();
        
        visited[currentPoint.x][currentPoint.y] = true;
        
        while (hasUnvisited(visited)) {
            int x = currentPoint.x;
            int y = currentPoint.y;
            
            LinkedList<Integer> unvisitedDirections = new LinkedList<>();
            for (int i=0; i<4; i++) {
                if (!isVisited(x, y, i, visited)) {
                    unvisitedDirections.push(i);
                }
                Muret trouveMuret = liste.chercheMuret(x, y, i);
                if (trouveMuret instanceof Muret) trouveMuret.show();
            }

            
            if (unvisitedDirections.size() > 0) {
                
                Collections.shuffle(unvisitedDirections);
                int direction = unvisitedDirections.getFirst();
                stack.push(currentPoint);
                liste.remove(Helper.getMuretFromDirection(x, y, direction));
                
                for (int i=1; i<4; i++) { //Remove walls for density control
                    if (Math.random() > density) {
                        Muret muret = getMuretFromDirection(x, y, (direction+i)%4);
                        if (!isBorderMuret(muret, w, h)) {
                            liste.remove(muret);
                        }
                    }
                }
                
                Point newPoint = getPointFromDirection(x, y, direction);
                visited[newPoint.x][newPoint.y] = true;
                currentPoint = newPoint;
                
                if (doAnim) {
                    try {
                        affichage.repaint();
                        sleep(waitms);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(RandomGenerators.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
            } else if (stack.size() > 0) {
                currentPoint = stack.pop();
            } else {
                return;
            }
            
        }
    }
    private static void dFrGenW(int x, int y, boolean[][] visited, ListeMuret liste, JComponent affichage, int waitms, boolean doAnim) { //Recursive version, stack overflows!
        visited[x][y] = true;
        
        LinkedList<Integer> directions = new LinkedList<>();
        for (int i=0; i<4; i++) {
            directions.push(i);
            Muret trouveMuret = liste.chercheMuret(x, y, i);
            if (trouveMuret instanceof Muret) trouveMuret.show();
        }
        Collections.shuffle(directions);
        
        for (int i : directions) {
            if (!Helper.isVisited(x, y, i, visited)) {
                //System.out.println(x + " " + y + " " + i);
                liste.remove(Helper.getMuretFromDirection(x, y, i));
                
                if (doAnim) {
                    try {
                        affichage.repaint();
                        sleep(waitms);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(RandomGenerators.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
                Point newPoint = Helper.getPointFromDirection(x, y, i);
                dFrGenW(newPoint.x, newPoint.y, visited, liste, affichage, waitms, doAnim);
            }
        }
        return;
        
    }
    
    
    public static void primGenerateWalls(int w, int h, float density, ListeMuret liste, JComponent affichage, int waitms, boolean doAnim) {
        System.out.println("Generating World...");
        
        fillWallsHide(w, h, liste);
        
        LinkedList<Muret> wallList = new LinkedList<>();
        
        int x0 = randomRange(0, w-1);
        int y0 = randomRange(0, h-1);
        
        boolean[][] visited = new boolean[w][h];
        for (int i=0; i<4; i++) {
            Muret mur = liste.chercheMuret(x0, y0, i);
            if (mur instanceof Muret) {
                if (Math.random() < density) {
                    mur.show();
                }
                wallList.push(mur);
            }
        }
        visited[x0][y0] = true;
        
        while (wallList.size() > 0) {
            Collections.shuffle(wallList);
            Muret mur = wallList.getFirst();
            
            if (mur.isHorz) {
                boolean isTopVisited = isVisited(mur.x, mur.y-1, visited);
                boolean isBottomVisited = isVisited(mur.x, mur.y, visited);
                if (isTopVisited ^ isBottomVisited) {
                    liste.remove(mur);
                    if (isTopVisited) {
                        visited[mur.x][mur.y] = true;
                        for (int i=0; i<4; i++) {
                            Muret newMur = liste.chercheMuret(mur.x, mur.y, i);
                            if (newMur instanceof Muret) {
                                if (Math.random() < density) {
                                    newMur.show();
                                }
                                wallList.push(newMur);
                            }
                        }
                    } else {
                        visited[mur.x][mur.y-1] = true;
                        for (int i=0; i<4; i++) {
                            Muret newMur = liste.chercheMuret(mur.x, mur.y-1, i);
                            if (newMur instanceof Muret) {
                                if (Math.random() < density) {
                                    newMur.show();
                                }
                                wallList.push(newMur);
                            }
                        }
                    }
                } else {
                    wallList.remove(mur);
                }
            } else {
                boolean isLeftVisited = isVisited(mur.x-1, mur.y, visited);
                boolean isRightVisited = isVisited(mur.x, mur.y, visited);
                if (isLeftVisited ^ isRightVisited) {
                    liste.remove(mur);
                    if (isLeftVisited) {
                        visited[mur.x][mur.y] = true;
                        for (int i=0; i<4; i++) {
                            Muret newMur = liste.chercheMuret(mur.x, mur.y, i);
                            if (newMur instanceof Muret) {
                                if (Math.random() < density) {
                                    newMur.show();
                                }
                                wallList.push(newMur);
                            }
                        }
                    } else {
                        visited[mur.x-1][mur.y] = true;
                        for (int i=0; i<4; i++) {
                            Muret newMur = liste.chercheMuret(mur.x-1, mur.y, i);
                            if (newMur instanceof Muret) {
                                if (Math.random() < density) {
                                    newMur.show();
                                }
                                wallList.push(newMur);
                            }
                        }
                    }
                } else {
                    wallList.remove(mur);
                }
            }
            
            if (doAnim) {
                try {
                    affichage.repaint();
                    sleep(waitms);
                } catch (InterruptedException ex) {
                    Logger.getLogger(RandomGenerators.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            
        }
        LinkedList<Muret> invisibleMurs = new LinkedList<>();
        
        for (Muret mur : liste) {
            if (mur instanceof Muret) {
                if (!mur.visible()) {
                    invisibleMurs.push(mur);
                }
            }
        }
        for (Muret invMur : invisibleMurs) {
            liste.remove(invMur);
        }
        
        
    }
    
    private static void fillWallsHide(int w, int h, ListeMuret liste) {
        for (int i=0; i<w; i++) { //Murs horizontaux
            for (int j=0; j<=h; j++) {
                Muret newMuret = new Muret(i, j, true);
                if (j == 0 || j == h) {
                    
                } else {
                    newMuret.hide();
                }
                liste.push(newMuret);
            }
        }
        for (int i=0; i<=w; i++) { //Murs verticaux
            for (int j=0; j<h; j++) {
                Muret newMuret = new Muret(i, j, false);
                if (i == 0 || i == w) {
                    
                } else {
                    newMuret.hide();
                }
                liste.push(newMuret);
            }
        }
    }
    
    public static int[] disjointMazeGeneratePossiblePositions(int w, int h, ListeMuret murs) { //Trouver les positions possibles pour le personnage et la sortie dans un labyrinthe disjoint (pour eviter les labyrinthes impossibles)
        
        System.out.println("Finding Optimal Exit Position...");
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
        
        int acceptableFillSize = (h*w)/8;
        
        //for (int i=h/2; i<h; i++) { //Right side openings
        for (int i=w-1; i>=w/2; i--) { //Bottom side openings

            if (maxFill > acceptableFillSize) {
                break;
            }

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
            System.out.println(exitProgress + "/" + maxExitProgress);
        }
        for (int i=h-1; i>=h/2; i--) { //Right side openings

            if (maxFill > acceptableFillSize) { //If the exit is large enough
                break;
            }

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
            System.out.println(exitProgress + "/" + maxExitProgress);
        }
        
        
        System.out.println("Done.");
        //Generate starting position
        System.out.println("Generating Starting Position...");
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
                System.out.println("Done, Spawning at " + rX + ", " + rY);
                return new int[] {rX, rY, maxX, maxY, (isHorz) ? 1 : 0};
            }
        }
        
    }
    public static int[] connectedMazeGeneratePossiblePositions(int w, int h, ListeMuret murs) { //Trouver les positions possibles pour le personnage et la sortie dans un labyrinthe de connexite simple
        
        System.out.println("Generating Random Exit Position...");
        
        boolean isHorz = (Math.random() < 0.5) ? true : false;
        
        int lastRow = h-1;
        int lastCol = w-1;
        int maxX, maxY;
        if (isHorz) {
            maxY = h;
            maxX = Helper.randomRange(w-(w/3), lastCol);
        } else {
            maxX = w;
            maxY = Helper.randomRange(h-(h/3), lastRow);
        }
        
        System.out.println("Done.");
        
        System.out.println("Generating Random Starting Position...");
        
        double r = Math.min(w, h)/4;
        
        int rX = (int)(Math.random()*r);
        int rY = (int)(Math.random()*r);
        System.out.println("Done, Spawning at " + rX + ", " + rY);
        return new int[] {rX, rY, maxX, maxY, (isHorz) ? 1 : 0};
        
    }
    
    
    
}
