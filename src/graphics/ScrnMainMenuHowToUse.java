package graphics;

import engine.MainEngine;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ScrnMainMenuHowToUse extends JPanel {
    private Branding branding;
    private MainEngine mainEngine;
    private JPanel parentContainer;

    private JLabel titleLabel;
    private JTextArea algoDesc, outputDesc, removeHint;
    private JLabel tableTitleLabel;
    private JPanel algoImage, tableImage, buttonsImage, outputImage;
    private JButton backButton;

    public ScrnMainMenuHowToUse(MainEngine mainEngine, Branding branding, JPanel parentContainer) {
        this.branding = branding;
        this.mainEngine = mainEngine;
        this.parentContainer = parentContainer;

        setLayout(new BorderLayout());
        setBackground(branding.dark);

        initializeMainPanel();
    }
    
    public void initializeMainPanel() {
        JPanel margin = new JPanel(new BorderLayout());
        margin.setOpaque(false);

        int m = 40;
        margin.add(blankPanel(m, 0, false), BorderLayout.NORTH);
        margin.add(blankPanel(m, 0, false), BorderLayout.SOUTH);
        margin.add(blankPanel(0, m, true),  BorderLayout.WEST);
        margin.add(blankPanel(0, m, true),  BorderLayout.EAST);

        JPanel container = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(branding.dark);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.setColor(branding.light);
                g2.setStroke(new BasicStroke(3f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 18, 18);
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
        titleLabel = new JLabel("How To Use", SwingConstants.CENTER);
        titleLabel.setFont(branding.jetBrainsBExtraLarge);
        titleLabel.setForeground(branding.light);
        c.add(titleLabel);

        algoDesc = makeText("Select your preferred algorithm by choosing from the dropdown menu.");
        c.add(algoDesc);

        algoImage = makeImagePanel(null, "algo");
        c.add(algoImage);

        outputDesc = makeText("The output of the simulation will be shown here along with the Gantt chart and the computation table.");
        c.add(outputDesc);

        tableTitleLabel = new JLabel("Input your data on this table shown below");
        tableTitleLabel.setFont(branding.jetBrainsBMedium);
        tableTitleLabel.setForeground(branding.light);
        c.add(tableTitleLabel);

        tableImage = makeImagePanel(null, "input");
        c.add(tableImage);

        removeHint = makeText("To remove multiple processes, select the process under the Process Id column and select the Remove Process button.");
        c.add(removeHint);

        buttonsImage = makeImagePanel(null, "buttons");
        c.add(buttonsImage);

        outputImage = makeImagePanel(null, "output");
        c.add(outputImage);

        backButton = new JButton("Back to Menu") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
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
        backButton.setBackground(branding.dark);
        backButton.setContentAreaFilled(false);
        backButton.setBorderPainted(false);
        backButton.setFocusPainted(false);
        backButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        backButton.addActionListener(e -> {
            CardLayout cl = (CardLayout) parentContainer.getLayout();
            cl.show(parentContainer, "MainMenu");
        });
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                backButton.setBackground(branding.darkGray);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                backButton.setBackground(branding.dark);
            }

            @Override
            public void mousePressed(MouseEvent e) {
                backButton.setBackground(branding.darkGray);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                backButton.setBackground(branding.darkGray);
            }
        });
        c.add(backButton);
    }
    
    public void refreshStyles() {
        titleLabel.setForeground(branding.light);
        tableTitleLabel.setForeground(branding.light);
        algoDesc.setForeground(branding.light);
        outputDesc.setForeground(branding.light);
        removeHint.setForeground(branding.light);
        backButton.setForeground(branding.light);
        backButton.setBackground(branding.dark);
        
        algoImage.repaint();
        tableImage.repaint();
        buttonsImage.repaint();
        outputImage.repaint();
        
        repaint();
        revalidate();
    }
    
    public void repositionContent(int W, int H) {
        int pad = (int) (W * 0.04);
        int titleH = (int) (H * 0.09);
        
        int leftW = (int) (W * 0.26);
        int midX = (int) (W * 0.30);
        int midW = (int) (W * 0.48);
        int rightX = (int) (W * 0.79);
        int rightW = W - rightX - pad;

        int topRowY = titleH;
        int topRowH = (int) (H * 0.50);
        int botRowY = (int) (H * 0.56);
        int botRowH = H - botRowY - pad;

        int btnW = 200, btnH = 48;
        
        titleLabel.setBounds(0, (int)(titleH * 0.1), W, (int)(titleH * 0.8));
        
        algoDesc.setBounds(pad, topRowY, leftW, (int)(H * 0.12));
        algoImage.setBounds(pad + (int)(leftW * 0.1), topRowY + (int)(H * 0.13), (int)(leftW * 0.85), (int)(topRowH * 0.62));
        outputDesc.setBounds(pad, botRowY, leftW, (int)(H * 0.14));
        
        int tableLabelH = (int)(H * 0.05);
        tableTitleLabel.setBounds(midX, topRowY, midW, tableLabelH);
        tableImage.setBounds(midX, topRowY + tableLabelH + 4, midW, (int)(topRowH * 0.48));
        removeHint.setBounds(midX, topRowY + tableLabelH + 4 + (int)(topRowH * 0.48) + 8, (int)(midW * 0.68), (int)(H * 0.18));
        
        buttonsImage.setBounds(rightX, topRowY, rightW, topRowH);
        
        outputImage.setBounds(midX + (int)(midW * 0.38), botRowY, W - (midX + (int)(midW * 0.38)) - pad, botRowH);
        backButton.setBounds(pad, H - btnH - pad, btnW, btnH);
    }
    
    public JPanel blankPanel(int height, int width, boolean isHorizontal) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        if (isHorizontal) p.setPreferredSize(new Dimension(width, 0));
        else p.setPreferredSize(new Dimension(0, height));
        return p;
    }

    public JTextArea makeText(String t) {
        JTextArea ta = new JTextArea(t);
        ta.setFont(branding.jetBrainsBMedium);
        ta.setForeground(branding.light);
        ta.setOpaque(false);
        ta.setEditable(false);
        ta.setFocusable(false);
        ta.setLineWrap(true);
        ta.setWrapStyleWord(true);
        return ta;
    }

    public JPanel makeImagePanel(Image image, String imageType) {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                
                Image currentImage = null;
                if ("algo".equals(imageType)) {
                    currentImage = branding.darkHowToUseAlgo;
                } else if ("input".equals(imageType)) {
                    currentImage = branding.darkHowToUseInput;
                } else if ("buttons".equals(imageType)) {
                    currentImage = branding.darkHowToUseButtons;
                } else if ("output".equals(imageType)) {
                    currentImage = branding.darkHowToUseOutput;
                }
                
                int pw = getWidth(), ph = getHeight();
                int imgW = currentImage.getWidth(this);
                int imgH = currentImage.getHeight(this);
                
                double scale = Math.min((double) pw / imgW, (double) ph / imgH);
                int drawW = (int) (imgW * scale);
                int drawH = (int) (imgH * scale);

                int drawX = (pw - drawW) / 2;
                int drawY = (ph - drawH) / 2;

                g2.drawImage(currentImage, drawX, drawY, drawW, drawH, this);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        return panel;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (branding.backgroundDark != null) {
            g.drawImage(branding.backgroundDark, 0, 0, getWidth(), getHeight(), this);
        }
    }
}