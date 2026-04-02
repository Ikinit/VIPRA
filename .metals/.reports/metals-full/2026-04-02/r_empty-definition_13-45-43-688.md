error id: file:///C:/Users/Mac%20Calimba/Documents/GitHub/VIPRA/src/graphics/ScrnSimulatorOutput.java:
file:///C:/Users/Mac%20Calimba/Documents/GitHub/VIPRA/src/graphics/ScrnSimulatorOutput.java
empty definition using pc, found symbol in pc: 
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 322
uri: file:///C:/Users/Mac%20Calimba/Documents/GitHub/VIPRA/src/graphics/ScrnSimulatorOutput.java
text:
```scala
package graphics;

import engine.MainEngine;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.*;

public class ScrnSimulatorOutput@@ extends JPanel {

    private Branding branding;
    private MainEngine mainEngine;
    private JPanel parentContainer;

    private int animationStep = 0;
    private int totalSteps = 0;
    private final List<AlgoGridPanel> gridPanels = new ArrayList<>();

    private JLabel timerLabel;
    private JButton speedButton;
    private float currentSpeed = 1.0f;
    private static final float[] SPEEDS = {0.5f, 1.0f, 1.5f, 2.0f, 4.0f, 8.0f, 16.0f, 32.0f};
    private int speedIndex = 1;

    private JPanel scrollContent;
    private JScrollPane mainScroll;

    private Timer simulationTimer;
    private int currentTime = 0;

    // Holds the result of one algorithm run
    public static class AlgoResult {
        public String algorithmName;
        public int[] referenceString;
        public int pageFrameCount;
        public int totalPageFaults;
        public int[][] frameStates;
        public boolean[] hits;
    }

    public ScrnSimulatorOutput(MainEngine mainEngine, Branding branding, JPanel parentContainer) {
        this.branding = branding;
        this.mainEngine = mainEngine;
        this.parentContainer = parentContainer;
        setLayout(new BorderLayout());
        setBackground(branding.dark);
        buildTopBar();
        buildScrollArea();
    }

    private class AlgoGridPanel extends JPanel {
        private final AlgoResult r;
        private int revealedSteps = 0;
        private boolean animationComplete = false;
        static final int CELL_W = 42, CELL_H = 42, COL_GAP = 10;
        static final int COL_W = CELL_W + COL_GAP;
        static final int REF_H = 28, HIT_H = 30;

        AlgoGridPanel(AlgoResult r) {
            this.r = r;
            setOpaque(false);
        }

        void advance() {
            if (revealedSteps < r.referenceString.length) {
                revealedSteps++;
                if (revealedSteps == r.referenceString.length) {
                    animationComplete = true;
                }
                repaint();
            }
        }

        void reset() {
            revealedSteps = 0;
            animationComplete = false;
            repaint();
        }

        @Override
        public Dimension getPreferredSize() {
            int totalW = r.referenceString.length * COL_W;
            int rowsH = r.pageFrameCount * CELL_H;
            return new Dimension(totalW, REF_H + rowsH + HIT_H);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int steps = r.referenceString.length;
            int frames = r.pageFrameCount;

            // Reference numbers row — dim future columns
            g2.setFont(branding.jetBrainsBSmall);
            FontMetrics fmRef = g2.getFontMetrics();
            for (int s = 0; s < steps; s++) {
                boolean revealed = s < revealedSteps;
                g2.setColor(revealed ? branding.light
                                    : new Color(branding.light.getRed(),
                                                branding.light.getGreen(),
                                                branding.light.getBlue(), 60));
                String lbl = String.valueOf(r.referenceString[s]);
                int cx = s * COL_W + CELL_W / 2 - fmRef.stringWidth(lbl) / 2;
                g2.drawString(lbl, cx, REF_H - 6);
            }

            // Frame-state cell grid
            g2.setFont(branding.jetBrainsBMedium);
            FontMetrics fmCell = g2.getFontMetrics();
            int gridTop = REF_H;

            for (int f = 0; f < frames; f++) {
                int cellY = gridTop + f * CELL_H;
                for (int s = 0; s < steps; s++) {
                    int cellX = s * COL_W;
                    boolean revealed = s < revealedSteps;
                    boolean isCurrent = !animationComplete && s == revealedSteps - 1;

                    // Dim border for future columns, bright for current
                    Color borderColor = revealed
                            ? (isCurrent ? branding.light : new Color(
                                branding.light.getRed(), branding.light.getGreen(),
                                branding.light.getBlue(), 160))
                            : new Color(branding.light.getRed(), branding.light.getGreen(),
                                        branding.light.getBlue(), 40);

                    // Red tint for fault, green tint for hit, plain dark otherwise
                    Color fillColor = (isCurrent && !r.hits[s])
                            ? new Color(0xF44336).darker().darker()
                            : (isCurrent && r.hits[s])
                            ? new Color(0x4CAF50).darker().darker()
                            : branding.dark;

                    g2.setColor(fillColor);
                    g2.fillRoundRect(cellX, cellY, CELL_W, CELL_H, 10, 10);
                    g2.setColor(borderColor);
                    g2.setStroke(new BasicStroke(2f));
                    g2.drawRoundRect(cellX, cellY, CELL_W, CELL_H, 10, 10);

                    // Draw page number only for revealed columns
                    if (revealed) {
                        int val = r.frameStates[s][f];
                        if (val >= 0) {
                            String txt = String.valueOf(val);
                            int tx = cellX + (CELL_W - fmCell.stringWidth(txt)) / 2;
                            int ty = cellY + (CELL_H + fmCell.getAscent() - fmCell.getDescent()) / 2;
                            g2.setColor(branding.light);
                            g2.drawString(txt, tx, ty);
                        }
                    }
                }
            }

            // Hit / Miss labels for revealed columns
            g2.setFont(branding.jetBrainsBSmall);
            FontMetrics fmHit = g2.getFontMetrics();
            int hitY = gridTop + frames * CELL_H + 16;
            for (int s = 0; s < revealedSteps; s++) {
                String lbl = r.hits[s] ? "Hit" : "Miss";
                g2.setColor(r.hits[s] ? new Color(0x4CAF50) : new Color(0xF44336));
                int hx = s * COL_W + CELL_W / 2 - fmHit.stringWidth(lbl) / 2;
                g2.drawString(lbl, hx, hitY);
            }

            g2.dispose();
        }
    }

    // ==================================================
    //               TOP BAR
    // ==================================================
    private void buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(branding.dark);
        bar.setBorder(new EmptyBorder(16, 20, 10, 20));

        JButton backBtn = makePillButton("Back To Menu");
        backBtn.setPreferredSize(new Dimension(180, 48));
        backBtn.addActionListener(e -> {
            stopTimer();
            CardLayout cl = (CardLayout) parentContainer.getLayout();
            cl.show(parentContainer, "SimulatorMain");
        });

        timerLabel = new JLabel("Timer: 0:00");
        timerLabel.setFont(branding.jetBrainsBMedium);
        timerLabel.setForeground(branding.light);

        speedButton = makePillButton("1.0x  ▼");
        speedButton.setPreferredSize(new Dimension(130, 40));
        speedButton.addActionListener(e -> {
            JPopupMenu menu = new JPopupMenu();
            menu.setBackground(branding.dark);
            menu.setBorder(BorderFactory.createLineBorder(branding.light, 2, true));

            for (float spd : SPEEDS) {
                String label = spd + "x";
                JMenuItem item = new JMenuItem(label) {
                    @Override protected void paintComponent(Graphics g) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setColor(getBackground());
                        g2.fillRect(0, 0, getWidth(), getHeight());
                        g2.dispose();
                        super.paintComponent(g);
                    }
                };
                item.setFont(branding.jetBrainsBMedium);
                item.setForeground(branding.light);
                item.setBackground(branding.dark);
                item.setOpaque(true);
                item.setBorder(new EmptyBorder(8, 16, 8, 16));
                item.addMouseListener(new MouseAdapter() {
                    @Override public void mouseEntered(MouseEvent e) { item.setBackground(branding.darkGray); }
                    @Override public void mouseExited(MouseEvent e)  { item.setBackground(branding.dark);     }
                });
                item.addActionListener(ev -> {
                    currentSpeed = spd;
                    speedIndex = java.util.Arrays.binarySearch(SPEEDS, spd);
                    speedButton.setText(label + "  ▼");
                    if (simulationTimer != null && simulationTimer.isRunning())
                        simulationTimer.setDelay((int)(1000 / currentSpeed));
                });
                menu.add(item);
            }

            menu.show(speedButton, 0, speedButton.getHeight());
        });

        JLabel speedLbl = new JLabel("Speed");
        speedLbl.setFont(branding.jetBrainsBMedium);
        speedLbl.setForeground(branding.light);
        speedLbl.setBorder(new EmptyBorder(0, 24, 0, 8));

        JPanel centre = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        centre.setOpaque(false);
        centre.add(timerLabel);
        centre.add(speedLbl);
        centre.add(speedButton);

        JButton exportBtn = makePillButton("Export to PNG");
        exportBtn.setPreferredSize(new Dimension(180, 48));
        exportBtn.addActionListener(e -> exportToPng());

        bar.add(backBtn, BorderLayout.WEST);
        bar.add(centre, BorderLayout.CENTER);
        bar.add(exportBtn, BorderLayout.EAST);

        add(bar, BorderLayout.NORTH);
    }

    // ==================================================
    //               SCROLL AREA
    // ==================================================
    private void buildScrollArea() {
        scrollContent = new JPanel();
        scrollContent.setLayout(new BoxLayout(scrollContent, BoxLayout.Y_AXIS));
        scrollContent.setBackground(branding.dark);
        scrollContent.setBorder(new EmptyBorder(0, 20, 20, 20));

        mainScroll = new JScrollPane(scrollContent,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        mainScroll.setBorder(null);
        mainScroll.setBackground(branding.dark);
        mainScroll.getViewport().setBackground(branding.dark);

        mainScroll.getVerticalScrollBar().setUnitIncrement(16);
        mainScroll.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() {
                thumbColor = branding.light;
                trackColor = branding.dark;
            }
            @Override protected JButton createDecreaseButton(int o) { return zeroButton(); }
            @Override protected JButton createIncreaseButton(int o) { return zeroButton(); }
            private JButton zeroButton() {
                JButton b = new JButton(); b.setPreferredSize(new Dimension(0, 0)); return b;
            }
        });

        add(mainScroll, BorderLayout.CENTER);
    }

    // ==================================================
    //               LOAD SIMULATION RESULTS
    // ==================================================
    public void loadSimulationResults(List<AlgoResult> results) {
        stopTimer();
        scrollContent.removeAll();
        gridPanels.clear();
        animationStep = 0;
        totalSteps = results.stream()
                            .mapToInt(r -> r.referenceString.length)
                            .max().orElse(0);

        for (AlgoResult r : results) {
            scrollContent.add(Box.createVerticalStrut(16));
            scrollContent.add(buildAlgoPanel(r));
        }

        scrollContent.add(Box.createVerticalStrut(16));
        scrollContent.revalidate();
        scrollContent.repaint();
        startTimer();
    }

    // ==================================================
    //               ALGORITHM PANEL
    // ==================================================
    private JPanel buildAlgoPanel(AlgoResult r) {
        JPanel panel = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(branding.dark);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(branding.light);
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, 20, 20);
                g2.dispose();
            }
        };
        panel.setOpaque(false);
        panel.setAlignmentX(Component.LEFT_ALIGNMENT);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));

        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(new EmptyBorder(20, 24, 12, 24));
        header.add(makeMonoLabel(r.algorithmName,       branding.jetBrainsBMedium, 14));
        header.add(Box.createVerticalStrut(4));
        header.add(makeMonoLabel("Reference String: " + joinInts(r.referenceString), branding.jetBrainsRMedium, 12));
        header.add(Box.createVerticalStrut(2));
        header.add(makeMonoLabel("Page Frame: " + r.pageFrameCount, branding.jetBrainsRMedium, 12));
        header.add(Box.createVerticalStrut(2));
        header.add(makeMonoLabel("Total Page Fault: "  + r.totalPageFaults, branding.jetBrainsRMedium, 12));
        panel.add(header, BorderLayout.NORTH);

        // Register grid so the timer can drive it
        AlgoGridPanel grid = new AlgoGridPanel(r);
        gridPanels.add(grid);

        int gridH = grid.getPreferredSize().height + 22;
        JScrollPane hScroll = new JScrollPane(grid, JScrollPane.VERTICAL_SCROLLBAR_NEVER, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        hScroll.setBorder(new EmptyBorder(0, 14, 14, 14));
        hScroll.setOpaque(false);
        hScroll.getViewport().setOpaque(false);
        hScroll.getViewport().setBackground(branding.dark);
        hScroll.getHorizontalScrollBar().setUnitIncrement(20);
        hScroll.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 8));
        hScroll.getHorizontalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() {
                thumbColor = branding.light; trackColor = branding.dark;
            }
            @Override protected JButton createDecreaseButton(int o) {
                JButton b = new JButton(); b.setPreferredSize(new Dimension(0, 0)); return b;
            }
            @Override protected JButton createIncreaseButton(int o) {
                JButton b = new JButton(); b.setPreferredSize(new Dimension(0, 0)); return b;
            }
        });
        hScroll.setPreferredSize(new Dimension(0, gridH));
        hScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, gridH));
        panel.add(hScroll, BorderLayout.CENTER);
        return panel;
    }

    // ==================================================
    //               GRID
    // ==================================================
    private JPanel buildGrid(AlgoResult r) {
        int steps = r.referenceString.length;
        int frames = r.pageFrameCount;

        int cellW = 42, cellH = 42, gap = 0;
        int colW = cellW + 10;

        int totalW = steps * colW + gap;
        int rowsH = frames * (cellH + gap) + gap;
        int refH = 28;
        int hitH = 30;
        int totalH = refH + rowsH + hitH;

        JPanel canvas = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                int startX = gap;

                // Reference numbers row
                g2.setFont(branding.jetBrainsBSmall);
                g2.setColor(branding.light);
                FontMetrics fm = g2.getFontMetrics();
                for (int s = 0; s < steps; s++) {
                    String lbl = String.valueOf(r.referenceString[s]);
                    int cx = startX + s * colW + cellW / 2 - fm.stringWidth(lbl) / 2;
                    g2.drawString(lbl, cx, refH - 6);
                }

                // Page-frame cell grid
                g2.setFont(branding.jetBrainsBMedium);
                FontMetrics fmCell = g2.getFontMetrics();
                int gridTop = refH;

                for (int f = 0; f < frames; f++) {
                    int cellY = gridTop + f * (cellH + gap) + gap;
                    for (int s = 0; s < steps; s++) {
                        int cellX = startX + s * colW;
                        g2.setColor(branding.dark);
                        g2.fillRoundRect(cellX, cellY, cellW, cellH, 10, 10);
                        g2.setColor(branding.light);
                        g2.setStroke(new BasicStroke(2f));
                        g2.drawRoundRect(cellX, cellY, cellW, cellH, 10, 10);

                        int val = r.frameStates[s][f];
                        if (val >= 0) {
                            String txt = String.valueOf(val);
                            int tx = cellX + (cellW - fmCell.stringWidth(txt)) / 2;
                            int ty = cellY + (cellH + fmCell.getAscent() - fmCell.getDescent()) / 2;
                            g2.setColor(branding.light);
                            g2.drawString(txt, tx, ty);
                        }
                    }
                }

                // Hit / Miss row
                g2.setFont(branding.jetBrainsBSmall);
                int hitY = gridTop + rowsH + 16;
                for (int s = 0; s < steps; s++) {
                    String lbl = r.hits[s] ? "Hit" : "Miss";
                    Color  clr = r.hits[s]
                            ? new Color(0x4CAF50)
                            : new Color(0xF44336);
                    g2.setColor(clr);
                    FontMetrics fmH = g2.getFontMetrics();
                    int hx = startX + s * colW + cellW / 2 - fmH.stringWidth(lbl) / 2;
                    g2.drawString(lbl, hx, hitY);
                }

                g2.dispose();
            }

            @Override public Dimension getPreferredSize() {
                return new Dimension(totalW + gap, totalH);
            }
        };
        canvas.setOpaque(false);
        return canvas;
    }

    // ==================================================
    //               TIMER
    // ==================================================
    private void startTimer() {
        currentTime = 0;
        animationStep = 0;
        timerLabel.setText("Timer: 0:00");

        simulationTimer = new Timer((int)(1000 / currentSpeed), null);
        simulationTimer.addActionListener(e -> {
            currentTime++;
            int m = currentTime / 60, s = currentTime % 60;
            timerLabel.setText(String.format("Timer: %d:%02d", m, s));

            // Advance one column per tick, stop when all are revealed
            if (animationStep < totalSteps) {
                animationStep++;
                for (AlgoGridPanel gp : gridPanels)
                    gp.advance();
            } else {
                simulationTimer.stop();
            }
        });
        simulationTimer.start();
    }

    private void stopTimer() {
        if (simulationTimer != null && simulationTimer.isRunning())
            simulationTimer.stop();
    }

    private void cycleSpeed() {
        speedIndex = (speedIndex + 1) % SPEEDS.length;
        currentSpeed = SPEEDS[speedIndex];
        speedButton.setText(currentSpeed + "x  ▼");
        if (simulationTimer != null && simulationTimer.isRunning()) {
            simulationTimer.setDelay((int)(1000 / currentSpeed));
        }
    }

    // ==================================================
    //               EXPORT TO PNG
    // ==================================================
    private void exportToPng() {
        try {
            JFileChooser chooser = new JFileChooser();
            chooser.setDialogTitle("Export Simulation to PNG");
            chooser.setSelectedFile(new File("simulation_output.png"));
            if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

            File file = chooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".png"))
                file = new File(file.getAbsolutePath() + ".png");

            BufferedImage img = new BufferedImage(
                    scrollContent.getWidth(), scrollContent.getHeight(),
                    BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = img.createGraphics();
            scrollContent.paint(g2);
            g2.dispose();
            ImageIO.write(img, "PNG", file);

            JOptionPane.showMessageDialog(this,
                    "Exported to: " + file.getAbsolutePath(),
                    "Export Successful", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Export failed: " + ex.getMessage(),
                    "Export Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ==================================================
    //               HELPERS
    // ==================================================
    private JLabel makeMonoLabel(String text, Font font, int size) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(font.deriveFont((float) size));
        lbl.setForeground(branding.light);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);
        return lbl;
    }

    private JButton makePillButton(String text) {
        JButton btn = new JButton(text) {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), getHeight(), getHeight());
                g2.setColor(branding.light);
                g2.setStroke(new BasicStroke(2.5f));
                g2.drawRoundRect(1, 1, getWidth()-2, getHeight()-2, getHeight(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(branding.jetBrainsBMedium);
        btn.setForeground(branding.light);
        btn.setBackground(branding.dark);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(branding.darkGray); }
            @Override public void mouseExited(MouseEvent e)  { btn.setBackground(branding.dark); }
        });
        return btn;
    }

    private String joinInts(int[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            sb.append(arr[i]);
            if (i < arr.length - 1) sb.append(", ");
        }
        return sb.toString();
    }

    public JPanel blankPanel(Color bg, int w, int h) {
        JPanel p = new JPanel();
        p.setBackground(bg);
        if (w > 0) p.setPreferredSize(new Dimension(w, 0));
        if (h > 0) p.setPreferredSize(new Dimension(0, h));
        return p;
    }

    public void refreshStyles() {
        setBackground(branding.dark);
        scrollContent.setBackground(branding.dark);
        mainScroll.setBackground(branding.dark);
        mainScroll.getViewport().setBackground(branding.dark);

        mainScroll.getVerticalScrollBar().setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override protected void configureScrollBarColors() {
                thumbColor = branding.light;
                trackColor = branding.dark;
            }
            @Override protected JButton createDecreaseButton(int o) { return zeroButton(); }
            @Override protected JButton createIncreaseButton(int o) { return zeroButton(); }
            private JButton zeroButton() {
                JButton b = new JButton(); b.setPreferredSize(new Dimension(0, 0)); return b;
            }
        });

        revalidate();
        repaint();
    }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: 