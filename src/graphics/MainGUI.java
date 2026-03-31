package graphics;

import engine.MainEngine;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class MainGUI {
    public Branding branding;
    private MainEngine engine;
    private JFrame mainFrame;
    private JPanel mainPanel;

    private ScrnSplashScreen scrnSplashScreen;
    private ScrnMainMenu scrnMainMenu;
    private ScrnMainMenuHowToUse scrnMainMenuHowToUse;
    private ScrnMainMenuSettings scrnMainMenuSettings;
    private ScrnSimulatorMain scrnSimulatorMain;
    private ScrnSimulatorOutput scrnSimulatorOutput;

    public MainGUI(MainEngine engine) {
        this.engine = engine;
        this.branding = new Branding();

        initializeMainFrame();
        initializeMainPanel();

        mainFrame.setVisible(true);
    }

    public void initializeMainFrame() {
        mainFrame = new JFrame();
        mainFrame.setSize(new Dimension(1240,720));
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setTitle("QuantumQueue");
        mainFrame.setLocationRelativeTo(null);
        mainFrame.setResizable(false);
    }

    public void initializeMainPanel(){
        mainPanel = new JPanel();
        mainPanel.setBackground(branding.dark);
        mainPanel.setLayout(new CardLayout());
        
        scrnSplashScreen = new ScrnSplashScreen(engine, branding, mainPanel);
        scrnMainMenu = new ScrnMainMenu(engine, branding, mainPanel);
        scrnMainMenuHowToUse = new ScrnMainMenuHowToUse(engine, branding, mainPanel);
        scrnMainMenuSettings = new ScrnMainMenuSettings(engine, branding, mainPanel);
        scrnMainMenuSettings.setThemeChangeListener(this::applyTheme);
        scrnSimulatorMain = new ScrnSimulatorMain(engine, branding, mainPanel);
        scrnSimulatorOutput = new ScrnSimulatorOutput(engine, branding, mainPanel);
        
        mainPanel.add(scrnSplashScreen, "SplashScreen");
        mainPanel.add(scrnMainMenu, "MainMenu");
        mainPanel.add(scrnMainMenuHowToUse, "HowToUse");
        mainPanel.add(scrnMainMenuSettings, "Settings");
        mainPanel.add(scrnSimulatorMain, "SimulatorMain");
        mainPanel.add(scrnSimulatorOutput, "SimulatorOutput");
        mainFrame.add(mainPanel);
    }
    
    public void applyTheme() {
        applyThemeRecursive(mainPanel);
        scrnSimulatorMain.refreshStyles();
        scrnSimulatorOutput.refreshStyles();
        scrnMainMenuHowToUse.refreshStyles();
        scrnMainMenu.refreshIcons();
        mainPanel.repaint();
    }

    private void applyThemeRecursive(Container parent) {
        for (Component c : parent.getComponents()) {
            Color bg = c.getBackground();
            if (bg != null) {
                if (bg.equals(branding.dark) || bg.equals(branding.light)) {
                    c.setBackground(branding.dark);
                }
            }
            
            Color fg = c.getForeground();
            if (fg != null && (fg.equals(branding.dark) || fg.equals(branding.light))) {
                c.setForeground(branding.light);
            }
            if (c instanceof Container) {
                applyThemeRecursive((Container) c);
            }
        }
    }

    // ==================================================
    //                GETTERS AND SETTERS
    // ==================================================

    public ScrnSimulatorMain getSimulatorMain() {
        return scrnSimulatorMain;
    }
    
    public ScrnSimulatorOutput getSimulatorOutput() {
        return scrnSimulatorOutput;
    }
}