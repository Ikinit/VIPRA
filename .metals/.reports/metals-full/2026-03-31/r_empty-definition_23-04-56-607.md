error id: file:///C:/Users/Mac%20Calimba/Documents/GitHub/VIPRA/src/graphics/ScrnMainMenuHowToUse.java:javax/swing/JPanel#
file:///C:/Users/Mac%20Calimba/Documents/GitHub/VIPRA/src/graphics/ScrnMainMenuHowToUse.java
empty definition using pc, found symbol in pc: javax/swing/JPanel#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 8729
uri: file:///C:/Users/Mac%20Calimba/Documents/GitHub/VIPRA/src/graphics/ScrnMainMenuHowToUse.java
text:
```scala
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

        algoDesc = makeText("Select your preferred algorithm/s by choosing from the options.");
        c.add(algoDesc);

        algoImage = makeImagePanel(null, "algo");
        c.add(algoImage);

        outputDesc = makeText("The output of the simulation will be shown here.");
        c.add(outputDesc);

        tableTitleLabel = new JLabel("Input your string separated by spaces");
        tableTitleLabel.setFont(branding.jetBrainsBMedium);
        tableTitleLabel.setForeground(branding.light);
        c.add(tableTitleLabel);

        tableImage = makeImagePanel(null, "input");
        c.add(tableImage);

        removeHint = makeText("You can select random input or import from txt files");
        c.add(removeHint);

        buttonsImage = makeImagePanel(null, "buttons");
        c.add(buttonsImage);

        outputImage = makeImagePanel(null, "output");
        c.add(outputImage);

        backButton = new JButton("Back To Menu") {
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
        int titleH = (int) (H * 0.12);

        int leftX  = pad;
        int leftW  = (int) (W * 0.27);

        int midX   = (int) (W * 0.31);
        int midW   = (int) (W * 0.36);

        int rightX = (int) (W * 0.69);
        int rightW = W - rightX - pad;

        int topRowY = titleH;
        int topRowH = (int) (H * 0.46);

        int botRowY = (int) (H * 0.60);
        int botRowH = H - botRowY - (int)(H * 0.04);

        int btnW = 200, btnH = 48;

        titleLabel.setBounds(0, (int)(titleH * 0.1), W, (int)(titleH * 0.75));

        int algoDescH = (int)(H * 0.13);
        algoDesc.setBounds(leftX, topRowY, leftW, algoDescH);

        int algoImgY = topRowY + algoDescH + (int)(H * 0.01);
        int algoImgH = (int)(H * 0.30);
        algoImage.setBounds(leftX, algoImgY, leftW, algoImgH);

        int outputDescY = algoImgY + algoImgH + (int)(H * 0.02);
        int outputDescH = (int)(H * 0.10);
        outputDesc.setBounds(leftX, outputDescY, leftW, outputDescH);

        backButton.setBounds(leftX, H - btnH - pad, btnW, btnH);

        int tableLabelH = (int)(H * 0.07);
        tableTitleLabel.setBounds(midX, topRowY, midW, tableLabelH);

        int tableImgH = (int)(H * 0.36);
        tableImage.setBounds(midX, topRowY + tableLabelH + 4, midW, tableImgH);

        int outX = midX;
        int outW = W - midX - pad;
        outputImage.setBounds(outX, botRowY, outW, botRowH);

        int btnsImgH = (int)(H * 0.18);
        buttonsImage.setBounds(rightX, topRowY, rightW, btnsImgH);

        int removeHintY = topRowY + btnsImgH + (int)(H * 0.02);
        int removeHintH = (int)(H * 0.18);
        removeHint.setBounds(rightX, removeHintY, rightW, removeHintH);
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
        J@@Panel panel = new JPanel() {
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

                if (currentImage == null) return;

                int pw = getWidth(), ph = getHeight();
                int imgW = currentImage.getWidth(this);
                int imgH = currentImage.getHeight(this);
                if (imgW <= 0 || imgH <= 0) return;

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
```


#### Short summary: 

empty definition using pc, found symbol in pc: javax/swing/JPanel#