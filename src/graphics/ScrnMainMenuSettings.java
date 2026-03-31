package graphics;

import engine.MainEngine;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ScrnMainMenuSettings extends JPanel {
    private Branding branding;
    private MainEngine mainEngine;
    private JPanel parentContainer;
    
    private Runnable themeChangeListener;

    private JLabel titleLabel;
    private JLabel themeSectionLabel;
    private JPanel themeUnderline;
    private JPanel darkCheckbox, lightCheckbox;
    private JLabel darkLabel, lightLabel;
    private JButton backButton;
    private JPanel container;
    
    private boolean darkSelected  = true;
    private boolean lightSelected = false;

    public ScrnMainMenuSettings(MainEngine mainEngine, Branding branding, JPanel parentContainer) {
        this.branding = branding;
        this.mainEngine = mainEngine;
        this.parentContainer = parentContainer;

        setLayout(new BorderLayout());
        setBackground(branding.dark);

        initializeMainPanel();
    }

    public void setThemeChangeListener(Runnable listener) {
        this.themeChangeListener = listener;
    }
    
    public void initializeMainPanel() {
        JPanel margin = new JPanel(new BorderLayout());
        margin.setOpaque(false);

        int paddingX = 300, paddingY = 60;
        margin.add(blankPanel(paddingY, 0, false), BorderLayout.NORTH);
        margin.add(blankPanel(paddingY, 0, false), BorderLayout.SOUTH);
        margin.add(blankPanel(0, paddingX, true),  BorderLayout.WEST);
        margin.add(blankPanel(0, paddingX, true),  BorderLayout.EAST);

        container = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(branding.dark);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 30, 30);
                g2.setColor(branding.light);
                g2.setStroke(new BasicStroke(3f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 30, 30);
                g2.dispose();
            }
        };
        container.setOpaque(false);

        buildContent(container);

        container.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                repositionContent(container.getWidth(), container.getHeight());
            }
        });

        margin.add(container, BorderLayout.CENTER);
        add(margin, BorderLayout.CENTER);
    }
    
    public void buildContent(JPanel c) {
        titleLabel = new JLabel("Settings", SwingConstants.CENTER);
        titleLabel.setFont(branding.jetBrainsBExtraLarge);
        titleLabel.setForeground(branding.light);
        c.add(titleLabel);
        
        themeSectionLabel = new JLabel("Theme");
        themeSectionLabel.setFont(branding.jetBrainsBLarge);
        themeSectionLabel.setForeground(branding.light);
        c.add(themeSectionLabel);
        
        themeUnderline = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(branding.light);
                g2.setStroke(new BasicStroke(3f));
                g2.drawLine(0, 0, getWidth(), 0);
                g2.dispose();
            }
        };
        themeUnderline.setOpaque(false);
        c.add(themeUnderline);
        
        darkCheckbox = makeCheckbox(true);
        lightCheckbox = makeCheckbox(false);
        c.add(darkCheckbox);
        c.add(lightCheckbox);

        darkLabel = new JLabel("Dark");
        darkLabel.setFont(branding.jetBrainsBMedium);
        darkLabel.setForeground(branding.light);
        c.add(darkLabel);

        lightLabel = new JLabel("Light");
        lightLabel.setFont(branding.jetBrainsBMedium);
        lightLabel.setForeground(branding.light);
        c.add(lightLabel);
        
        backButton = new JButton("Back to Menu") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(branding.dark);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                g2.setColor(branding.light);
                g2.setStroke(new BasicStroke(3f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, getHeight(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        backButton.setFont(branding.jetBrainsBMedium);
        backButton.setForeground(branding.light);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            if (parentContainer != null) {
                CardLayout cl = (CardLayout) parentContainer.getLayout();
                cl.show(parentContainer, "MainMenu");
            }
        });
        c.add(backButton);
    }
    
    public void repositionContent(int W, int H) {
        int pad = (int) (W * 0.05);
        
        titleLabel.setBounds(0, (int)(H * 0.04), W, (int)(H * 0.15));
        
        int sectionY = (int) (H * 0.22);
        int sectionH = (int) (H * 0.09);
        themeSectionLabel.setBounds(pad, sectionY, (int)(W * 0.6), sectionH);
        
        themeUnderline.setBounds(pad, sectionY + sectionH, W - pad * 2, 2);
        
        int rowY = sectionY + sectionH + (int)(H * 0.04);
        int cbSize = (int) (H * 0.07);
        int gap = (int) (W * 0.015);

        int darkX  = pad * 2;
        darkCheckbox.setBounds(darkX, rowY, cbSize, cbSize);
        darkLabel.setBounds(darkX + cbSize + gap, rowY, (int)(W * 0.12), cbSize);

        int lightX = (int)(W * 0.48);
        lightCheckbox.setBounds(lightX, rowY, cbSize, cbSize);
        lightLabel.setBounds(lightX + cbSize + gap, rowY, (int)(W * 0.12), cbSize);
        
        int btnW = 200, btnH = 48;
        backButton.setBounds(pad, H - btnH - pad, btnW, btnH);
    }
    
    public JPanel makeCheckbox(boolean isDark) {
        JPanel cb = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int s = Math.min(getWidth(), getHeight());
                int arc = (int)(s * 0.2);
                
                g2.setColor(branding.light);
                g2.setStroke(new BasicStroke(3f));
                g2.drawRoundRect(1, 1, s - 2, s - 2, arc, arc);
                
                boolean active = isDark ? darkSelected : lightSelected;
                if (active) {
                    int inset = (int)(s * 0.18);
                    g2.fillRoundRect(inset, inset, s - inset * 2, s - inset * 2, arc / 2, arc / 2);
                }
                g2.dispose();
            }
        };
        cb.setOpaque(false);
        cb.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        cb.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                boolean wasAlreadySelected = isDark ? darkSelected : lightSelected;
                if (wasAlreadySelected) return;

                darkSelected = isDark;
                lightSelected = !isDark;
                
                branding.toggleTheme();
                
                if (themeChangeListener != null) themeChangeListener.run();
            }
        });

        return cb;
    }
    
    public JPanel blankPanel(int height, int width, boolean isHorizontal) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        if (isHorizontal) p.setPreferredSize(new Dimension(width, 0));
        else p.setPreferredSize(new Dimension(0, height));
        return p;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (branding.backgroundDark != null) {
            g.drawImage(branding.backgroundDark, 0, 0, getWidth(), getHeight(), this);
        }
    }
}