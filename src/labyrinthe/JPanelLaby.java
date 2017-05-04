/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package labyrinthe;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import static java.lang.Thread.sleep;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.text.NumberFormatter;
import labyrinthe.AI.AIType;
import labyrinthe.RandomGenerators.GeneratorType;

/**
 *
 * @author Bowen Peng, Zhenglong, Devoir 2
 */
//Redefine the JPanel for maze
public class JPanelLaby extends JPanel implements Runnable { //Classe JPanel pour le jeu

    //Attributes
    private int xsize, ysize;
    private final AffichageLaby affichageLaby;
    private final JPanel controlPanel;
    
    private final JPanel thisPanel = this;
    
    public final Thread thread;
    
    private int w, h;
    private float density;
    private GeneratorType type;
    private long delayms;
    private int lives;
    private int waitms;
    private boolean doGenAnim = true;
    private AIType aiType;
    private int aiMovementDelay = 100;
    private int aiAnimationDelay = 10;
    
    private boolean isAIEnabled = false;
    private boolean isAutoRestart = false;
    private boolean disableAutoHide = false;
    
    private float density0 = 0.4f; //Densite defaut sauvegardee pour tout les generateurs
    private float density1 = 1f;
    private float density2 = 1f;
    private float density3 = 1f;
    
    private boolean generateMazeNext = true;
    

    //Constructor
    public JPanelLaby(int xsize, int ysize, int w, int h, float density, GeneratorType type, long delayms, int lives, int waitms, AIType aiType) {
        this.xsize = xsize;
        this.ysize = ysize;
        thread = new Thread(this);
        
        this.w = w;
        this.h = h;
        this.density = density;
        this.type = type;
        this.delayms = delayms;
        this.lives = lives;
        this.waitms = waitms;
        this.aiType = aiType;
        
        
        setSize(xsize, ysize);
        //Set the layout
        this.setLayout(new BorderLayout(0, 0));
        //Display maze
        affichageLaby = new AffichageLaby();
        controlPanel = new JPanel();
        affichageLaby.setPreferredSize(new Dimension(xsize-200, ysize));
        this.add(controlPanel, BorderLayout.LINE_END);
        this.add(affichageLaby, BorderLayout.CENTER);
        //Add buttons and event listeners
        addControls();
        addEventListeners();
        
        
    }
    
    public JPanel controlPanel() {
        return controlPanel;
    }
    public AffichageLaby affichageLaby() {
        return affichageLaby;
    }
    
    
    //Ajouter les controles et le menu
    private void addControls() {
        
        JButton upButton = new JButton("^ (W)"); 
        JButton downButton = new JButton("v (S)"); 
        JButton leftButton = new JButton("< (A)"); 
        JButton rightButton = new JButton("> (D)"); 
        JButton visibleButton = new JButton("Visible");
        JButton invisibleButton = new JButton("Invisible");
        JButton resetButton = new JButton("Reset");
        
        JLabel genLabel = new JLabel("Generator: ");
        genLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JFormattedTextField widthField = new JFormattedTextField(new NumberFormatter());
        JFormattedTextField heightField = new JFormattedTextField(new NumberFormatter());
        widthField.setValue(w);
        heightField.setValue(h);
        
        JLabel densityLabel = new JLabel("Density: " + density);
        densityLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JSlider densitySlider = new JSlider(0, 1000, (int)(density*1000));
        densitySlider.setMajorTickSpacing(100);
        densitySlider.setMinorTickSpacing(50);
        densitySlider.setPaintTicks(true);
        densitySlider.setPaintLabels(true);
        Hashtable labelTable = new Hashtable();
        labelTable.put(0, new JLabel("0.0") );
        labelTable.put(250, new JLabel("0.25") );
        labelTable.put(500, new JLabel("0.5") );
        labelTable.put(750, new JLabel("0.75") );
        labelTable.put(1000, new JLabel("1.0") );
        densitySlider.setLabelTable(labelTable);
        
        JSlider genSpeedSlider = new JSlider(0, 50, (waitms>50) ? 0 : 50-waitms);
        
        JButton genButton = new JButton("Generate!");
        
        JComboBox<String> genCombo = new JComboBox(new String[] {"Naive Uniform","Recursive","Depth-First", "Prim"});
        switch (type) {
            case NAIVEUNIFORM:
                density0 = density;
                genCombo.setSelectedIndex(0);
                break;
            case RECURSIVE:
                density1 = density;
                genCombo.setSelectedIndex(1);
                break;
            case DEPTHFIRST:
                density2 = density;
                genCombo.setSelectedIndex(2);
                break;
            case PRIM:
                density3 = density;
                genCombo.setSelectedIndex(3);
                break;
        }
        
        JLabel aiLabel = new JLabel("AI Type: ");
        aiLabel.setFont(new Font("Arial", Font.BOLD, 14));
        
        JComboBox<String> aiCombo = new JComboBox(new String[] {"Naive Wall Follow","Wall + Memory","Wall + Flood Fill + Memory","Greedy + Flood Fill + Memory","Depth-First", "Breadth-First"});
        
        switch (aiType) {
            case NAIVEWALL:
                aiCombo.setSelectedIndex(0);
                break;
            case MEMORYWALL:
                aiCombo.setSelectedIndex(1);
                break;
            case MEMORYFILLWALL:
                aiCombo.setSelectedIndex(2);
                break;
            case GREEDYFILL:
                aiCombo.setSelectedIndex(3);
                break;
            case DEPTHFIRST:
                aiCombo.setSelectedIndex(4);
                break;
            case BREADTHFIRST:
                aiCombo.setSelectedIndex(5);
                break;
        }
        
        
        JSlider aiSpeedSlider = new JSlider(0, 1000, (aiMovementDelay>1000) ? 0 : 1000-aiMovementDelay);
        JSlider aiAnimSlider = new JSlider(0, 100, (aiAnimationDelay>100) ? 0 : 100-aiAnimationDelay);
        
        JCheckBox enableAICheck = new JCheckBox("Enable AI");
        JCheckBox enableRestartCheck = new JCheckBox("Automatic Restart");
        JCheckBox disableHideCheck = new JCheckBox("Disable Auto-Hide");
        
        upButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                affichageLaby.labyrinthe().deplace('H');
                affichageLaby.labyrinthe().purgeAI();
                repaint();
            }
        });
        downButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                affichageLaby.labyrinthe().deplace('B');
                affichageLaby.labyrinthe().purgeAI();
                repaint();
            }
        });
        
        leftButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                affichageLaby.labyrinthe().deplace('G');
                affichageLaby.labyrinthe().purgeAI();
                repaint();
            }
        });
        
        rightButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                affichageLaby.labyrinthe().deplace('D');
                affichageLaby.labyrinthe().purgeAI();
                repaint();
            }
        });
        visibleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!affichageLaby.labyrinthe().isGenerating()) {
                    affichageLaby.labyrinthe().murs().show();
                }
                repaint();
            }
        });
        invisibleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!affichageLaby.labyrinthe().isGenerating()) {
                    affichageLaby.labyrinthe().murs().hide();
                }
                repaint();
            }
        });
        resetButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!affichageLaby.labyrinthe().isGenerating()) {
                    affichageLaby.labyrinthe().reset();
                }
                repaint();
            }
        });
        
        densitySlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                density = densitySlider.getValue()/1000f;
                switch (type) {
                    case NAIVEUNIFORM:
                        density0 = density;
                        break;
                    case RECURSIVE:
                        density1 = density;
                        break;
                    case DEPTHFIRST:
                        density2 = density;
                        break;
                    case PRIM:
                        density3 = density;
                        break;
                }
                densityLabel.setText("Density: " + density);
            }
        });
        genSpeedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                waitms = 50-genSpeedSlider.getValue();
                if (waitms == 0) {
                    doGenAnim = false;
                } else {
                    doGenAnim = true;
                }
            }
        });
        aiSpeedSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                aiMovementDelay = 1000-aiSpeedSlider.getValue();
                
            }
        });
        aiAnimSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                aiAnimationDelay = 100-aiAnimSlider.getValue();
                affichageLaby.labyrinthe().setAIAnimDelay(aiAnimationDelay);
            }
        });
        genCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (genCombo.getSelectedIndex()) {
                    case 0:
                        type = GeneratorType.NAIVEUNIFORM;
                        density = density0;
                        break;
                    case 1:
                        type = GeneratorType.RECURSIVE;
                        density = density1;
                        break;
                    case 2:
                        type = GeneratorType.DEPTHFIRST;
                        density = density2;
                        break;
                    case 3:
                        type = GeneratorType.PRIM;
                        density = density3;
                        break;
                }
                densityLabel.setText("Density: " + density);
                densitySlider.setValue((int)(density * 1000));
            }
        });
        genButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                w = Integer.parseInt(widthField.getText());
                h = Integer.parseInt(heightField.getText());
                if (w > 500) {
                    w = 500;
                    widthField.setValue(w);
                } else if (w < 3) {
                    w = 3;
                    widthField.setValue(w);
                }
                if (h > 500) {
                    h = 500;
                    heightField.setValue(h);
                } else if (h < 3) {
                    h = 3;
                    heightField.setValue(h);
                }
                generateMazeNext = true;
            }
        });
        
        aiCombo.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (aiCombo.getSelectedIndex()) {
                    case 0:
                        aiType = AIType.NAIVEWALL;
                        break;
                    case 1:
                        aiType = AIType.MEMORYWALL;
                        break;
                    case 2:
                        aiType = AIType.MEMORYFILLWALL;
                        break;
                    case 3:
                        aiType = AIType.GREEDYFILL;
                        break;
                    case 4:
                        aiType = AIType.DEPTHFIRST;
                        break;
                    case 5:
                        aiType = AIType.BREADTHFIRST;
                        break;
                }
                affichageLaby.labyrinthe().setAIType(aiType);
            }
        });
        enableAICheck.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) { //checkbox has been selected
                    isAIEnabled = true;
                    affichageLaby.labyrinthe().enableAI();
                } else { //checkbox has been deselected
                    isAIEnabled = false;
                    affichageLaby.labyrinthe().disableAI();
                };
            }
        });
        
        enableRestartCheck.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    isAutoRestart = true;
                    affichageLaby.labyrinthe().enableRestart();
                } else {
                    isAutoRestart = false;
                    affichageLaby.labyrinthe().disableRestart();
                };
            }
        });
        
        disableHideCheck.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                if(e.getStateChange() == ItemEvent.SELECTED) {
                    disableAutoHide = true;
                } else {
                    disableAutoHide = false;
                };
            }
        });
                        
        //Ajouter tout les controles sur un GridBagLayout
        JPanel cpanel = controlPanel;
        cpanel.setPreferredSize(new Dimension(220, 0));
        cpanel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.ipadx = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;
        cpanel.add(upButton, c);
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        cpanel.add(leftButton, c);
        c.gridx = 1;
        c.gridy = 1;
        cpanel.add(rightButton, c);
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 2;
        cpanel.add(downButton, c);
        c.insets = new Insets(40, 0, 5, 0);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 3;
        cpanel.add(visibleButton, c);
        c.insets = new Insets(0, 0, 0, 0);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 4;
        cpanel.add(invisibleButton, c);
        c.insets = new Insets(40, 0, 0, 0);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 5;
        cpanel.add(resetButton, c);
        c.insets = new Insets(40, 0, 0, 0);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 6;
        cpanel.add(genLabel, c);
        c.insets = new Insets(0, 0, 5, 0);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 7;
        cpanel.add(genCombo, c);
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 8;
        cpanel.add(widthField, c);
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 8;
        cpanel.add(heightField, c);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 9;
        cpanel.add(densityLabel, c);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 10;
        cpanel.add(densitySlider, c);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 11;
        cpanel.add(new JLabel("Generation Speed:"), c);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 12;
        cpanel.add(genSpeedSlider, c);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 13;
        cpanel.add(genButton, c);
        c.insets = new Insets(40, 0, 0, 0);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 14;
        cpanel.add(aiLabel, c);
        c.insets = new Insets(0, 0, 5, 0);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 15;
        cpanel.add(aiCombo, c);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 16;
        cpanel.add(new JLabel("AI Animation Speed:"), c);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 17;
        cpanel.add(aiAnimSlider, c);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 18;
        cpanel.add(new JLabel("AI Movement Speed:"), c);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 19;
        cpanel.add(aiSpeedSlider, c);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 20;
        cpanel.add(enableAICheck, c);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 21;
        cpanel.add(enableRestartCheck, c);
        c.insets = new Insets(20, 0, 0, 0);
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 22;
        cpanel.add(disableHideCheck, c);
        //movementButtons.add(myCombo);
        
    }

    //Event Listeners (Souris et clavier)
    private void addEventListeners() {
        
        this.addMouseListener(new MouseInputAdapter() {

            @Override
            public void mousePressed(MouseEvent me) {
                requestFocusInWindow(true);
            }
        });
        
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                Rectangle r = e.getComponent().getBounds();
                int h = r.height;
                int w = r.width;
                JPanelLaby thisScene = ((JPanelLaby)(e.getComponent()));
                thisScene.xsize = w;
                thisScene.ysize = h;
                int sqCustomSize = Math.min(xsize-200, ysize);
                affichageLaby.setCustomSize(sqCustomSize, sqCustomSize);
                thisPanel.updateUI();
            }
        });
        
        this.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent ke) {
            }

            @Override
            public void keyPressed(KeyEvent e) {
                switch(e.getKeyChar()) {
                    case 'w':
                        affichageLaby.labyrinthe().deplace('H');
                        affichageLaby.labyrinthe().purgeAI();
                        break;
                    case 's':
                        affichageLaby.labyrinthe().deplace('B');
                        affichageLaby.labyrinthe().purgeAI();
                        break;
                    case 'a':
                        affichageLaby.labyrinthe().deplace('G');
                        affichageLaby.labyrinthe().purgeAI();
                        break;
                    case 'd':
                        affichageLaby.labyrinthe().deplace('D');
                        affichageLaby.labyrinthe().purgeAI();
                        break;
                    //case ' ':
                        //affichageLaby.labyrinthe().stepAI();
                        //break;
                }
                //System.out.println(e.getKeyChar());
                repaint();
            }

            @Override
            public void keyReleased(KeyEvent ke) {
            }
        });
    }
    //Queue the maze generation
    public void queueGenerateNewMaze() {
        generateMazeNext = true;
    }
    private void generateNewMaze() {
        if (affichageLaby.labyrinthe() != null) {
            affichageLaby.labyrinthe().purge();
        }
        if (w < 3) {
            System.out.println("Width cannot be smaller than 3!");
            w = 3;
        }
        if (h < 3) {
            System.out.println("Height cannot be smaller than 3!");
            h = 3;
        }
        if (w > 500) {
            System.out.println("Width cannot be bigger than 500!");
            w = 500;
        }
        if (h > 500) {
            System.out.println("Height cannot be bigger than 500!");
            h = 500;
        }
        affichageLaby.setLabyrinthe(new Labyrinthe(w, h, this, density, type, delayms, lives, waitms, doGenAnim, aiType, aiMovementDelay, aiAnimationDelay, isAIEnabled, isAutoRestart, disableAutoHide));
        affichageLaby.labyrinthe().generate();
        generateMazeNext = false;
    }

    @Override
    public void run() { //Repaint each second to prevent grayscreen bug + uses threads to repaint the AI for animations
        //Cannot repaint during an event, must use threads for animating the map and AI generation
        while (true) {
            
            try {
                updateUI();
                repaint();
                if (generateMazeNext) {
                    generateNewMaze();
                }
                if (isAIEnabled) {
                    affichageLaby.labyrinthe().stepAI();
                }
                sleep((aiMovementDelay < 10) ? 10 : aiMovementDelay);
            } catch (InterruptedException ex) {
                Logger.getLogger(JPanelLaby.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    
}
