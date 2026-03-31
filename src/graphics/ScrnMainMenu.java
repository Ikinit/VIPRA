package graphics;

import engine.MainEngine;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class ScrnMainMenu extends JPanel {
    private Branding branding;
    private MainEngine mainEngine;
    private JPanel parentContainer;
    
    private GridBagConstraints gbc;
    private JButton simulateBtn, howToUseBtn, settingsBtn, exitBtn;

    public ScrnMainMenu(MainEngine mainEngine, Branding branding, JPanel parentContainer) {
        this.branding = branding;
        this.mainEngine = mainEngine;
        this.parentContainer = parentContainer;

        setLayout(new GridBagLayout());
        setBackground(branding.dark);
        gbc = new GridBagConstraints();

        initializeMainPanel();
        setActionListeners();
    }

    public void initializeMainPanel() {
        JLabel simulatorLabel = new JLabel("VIPRA");
        simulatorLabel.setFont(branding.jetBrainsBGiant);
        simulatorLabel.setForeground(branding.light);

        JLabel subtitleLabel = new JLabel("Virtual Interactive Page Replacement Algorithm");        
        subtitleLabel.setFont(branding.jetBrainsBMedium);
        subtitleLabel.setForeground(branding.light);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.insets = new Insets(0, 0, 0, 0);
        add(simulatorLabel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 30, 0);
        add(subtitleLabel, gbc);

        simulateBtn = createMainMenuButton(branding.lightIcoSimulate, "Simulate");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        gbc.insets = new Insets(30, 110, 30, 110);
        add(simulateBtn, gbc);

        howToUseBtn = createMainMenuButton(branding.lightIcoHowToUse, "How To Use");
        gbc.gridx = 1;
        gbc.gridy = 2;
        add(howToUseBtn, gbc);

        settingsBtn = createMainMenuButton(branding.lightIcoSettings, "Settings");
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(settingsBtn, gbc);

        exitBtn = createMainMenuButton(branding.lightIcoExit, "Exit");
        gbc.gridx = 1;
        gbc.gridy = 3;
        add(exitBtn, gbc);
    }

    public void setActionListeners() {
        simulateBtn.addActionListener(e -> {
            CardLayout cl = (CardLayout) parentContainer.getLayout();
            cl.show(parentContainer, "SimulatorMain");
        });
        
        howToUseBtn.addActionListener(e -> {
            CardLayout cl = (CardLayout) parentContainer.getLayout();
            cl.show(parentContainer, "HowToUse");
        });

        settingsBtn.addActionListener(e -> {
            CardLayout cl = (CardLayout) parentContainer.getLayout();
            cl.show(parentContainer, "Settings");
        });

        exitBtn.addActionListener(e -> System.exit(0));
    }
    
    public void refreshIcons() {
        System.out.println("@ Main Menu icons switch");
        simulateBtn.setIcon(branding.lightIcoSimulate);
        howToUseBtn.setIcon(branding.lightIcoHowToUse);
        settingsBtn.setIcon(branding.lightIcoSettings);
        exitBtn.setIcon(branding.lightIcoExit);
        repaint();
        revalidate();
    }

    public JButton createMainMenuButton(ImageIcon icon, String text) {
        JButton btn = new JButton(text, icon) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.setColor(branding.light);
                g2.setStroke(new BasicStroke(3f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(branding.jetBrainsBMedium);
        btn.setForeground(branding.light);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalTextPosition(SwingConstants.CENTER);
        btn.setVerticalTextPosition(SwingConstants.BOTTOM);
        btn.setHorizontalAlignment(SwingConstants.CENTER);
        btn.setIconTextGap(10);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        btn.setPreferredSize(new Dimension(300, 160));
        btn.setBorder(new EmptyBorder(0, 20, 0, 20));
        btn.setBackground(branding.dark);

        btn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(branding.darkGray);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                btn.setBackground(branding.dark);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                btn.setBackground(branding.darkGray);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                btn.setBackground(branding.darkGray);
            }
        });

        return btn;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (branding.backgroundDark != null) {
            g.drawImage(branding.backgroundDark, 0, 0, getWidth(), getHeight(), this);
        }
    }
}