error id: file:///C:/Users/Mac%20Calimba/Documents/GitHub/VIPRA/src/graphics/ScrnSimulatorMain.java:javax/swing/JPanel#
file:///C:/Users/Mac%20Calimba/Documents/GitHub/VIPRA/src/graphics/ScrnSimulatorMain.java
empty definition using pc, found symbol in pc: javax/swing/JPanel#
empty definition using semanticdb
empty definition using fallback
non-local guesses:

offset: 216
uri: file:///C:/Users/Mac%20Calimba/Documents/GitHub/VIPRA/src/graphics/ScrnSimulatorMain.java
text:
```scala
package graphics;

import engine.MainEngine;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;

public class ScrnSimulatorMain extends JPan@@el {
    private Branding branding;
    private MainEngine mainEngine;
    private JPanel parentContainer;

    private JPanel mainPanel;
    private JPanel leftPanel, rightPanel, algoListPanel;

    private JTextArea inputArea;
    private JSpinner pageFrameSpinner;

    public ScrnSimulatorMain(MainEngine mainEngine, Branding branding, JPanel parentContainer) {
        this.branding = branding;
        this.mainEngine = mainEngine;
        this.parentContainer = parentContainer;

        setLayout(new BorderLayout());
        setBackground(branding.dark);

        initializeMainPanel();
        initializePanels();
    }

    public void initializeMainPanel() {
        JPanel wrapper = new JPanel(new BorderLayout());
        wrapper.setBackground(branding.dark);

        int mw = 20, mh = 20;

        JPanel north = blankPanel(branding.dark, 0, mh);
        JPanel south = blankPanel(branding.dark, 0, mh);
        JPanel west = blankPanel(branding.dark, mw, 0);
        JPanel east = blankPanel(branding.dark, mw, 0);

        wrapper.add(north, BorderLayout.NORTH);
        wrapper.add(south, BorderLayout.SOUTH);
        wrapper.add(west, BorderLayout.WEST);
        wrapper.add(east, BorderLayout.EAST);

        mainPanel = new JPanel(new BorderLayout(15, 0));
        mainPanel.setBackground(branding.dark);
        wrapper.add(mainPanel, BorderLayout.CENTER);

        add(wrapper, BorderLayout.CENTER);
    }

    public void initializePanels() {
        initializeLeftPanel();
        initializeRightPanel();

        JButton backBtn = createOtherBtn("← Back");
        backBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
        backBtn.addActionListener(e -> {
            CardLayout cl = (CardLayout) parentContainer.getLayout();
            cl.show(parentContainer, "MainMenu");
        });

        JPanel topBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        topBar.setOpaque(false);
        topBar.setPreferredSize(new Dimension(0, 54));
        topBar.add(backBtn);

        mainPanel.add(topBar, BorderLayout.NORTH);
        mainPanel.add(leftPanel, BorderLayout.WEST);
        mainPanel.add(rightPanel, BorderLayout.CENTER);
    }

    private void initializeLeftPanel() {
        leftPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(branding.dark);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 18, 18);
                g2.setColor(branding.light);
                g2.setStroke(new BasicStroke(3f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 18, 18);
                g2.dispose();
            }
        };
        leftPanel.setOpaque(false);
        leftPanel.setPreferredSize(new Dimension(700, 0));
        
        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(30, 30, 20, 30));

        // Input String label
        JLabel inputLabel = createHeaderLabel("Input String", false);
        inputLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(inputLabel);
        content.add(Box.createVerticalStrut(12));
        // Subtext String label
        JLabel subtextLabel = createHeaderLabel("Space Separated Values (e.g., 1 2 3 4)", false);
        subtextLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(subtextLabel);
        content.add(Box.createVerticalStrut(12));

        // Text area
        inputArea = new JTextArea(){};
        inputArea.setFont(branding.jetBrainsRMedium);
        inputArea.setForeground(branding.light);
        inputArea.setBackground(branding.dark);
        inputArea.setCaretColor(branding.light);
        inputArea.setLineWrap(true);
        inputArea.setWrapStyleWord(true);
        inputArea.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(branding.light, 2, true),
            new EmptyBorder(8, 16, 8, 16)
        ));
        inputArea.setAlignmentX(Component.LEFT_ALIGNMENT);
        inputArea.setPreferredSize(new Dimension(Integer.MAX_VALUE, 110));
        inputArea.setMaximumSize(new Dimension(Integer.MAX_VALUE, 110));
        content.add(inputArea);
        content.add(Box.createVerticalStrut(28));

        // Number of Page Frame label
        JLabel frameLabel = createHeaderLabel("Number of Page Frame (3-10)", false);
        frameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        content.add(frameLabel);
        content.add(Box.createVerticalStrut(12));

        // Spinner
        SpinnerNumberModel spinnerModel = new SpinnerNumberModel(4, 1, 20, 1);
        pageFrameSpinner = new JSpinner(spinnerModel);
        pageFrameSpinner.setAlignmentX(Component.LEFT_ALIGNMENT);
        pageFrameSpinner.setMaximumSize(new Dimension(200, 44));
        pageFrameSpinner.setPreferredSize(new Dimension(200, 44));
        pageFrameSpinner.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(branding.light, 2, true),
            new EmptyBorder(2, 4, 2, 4)
        ));

        // limit the values to 3-10 and make the spinner more compact
         pageFrameSpinner.addChangeListener(e -> {
            int value = (int) pageFrameSpinner.getValue();
            if (value < 3) {
                pageFrameSpinner.setValue(3);
            } else if (value > 10) {
                pageFrameSpinner.setValue(10);
            }
         });

        // Style the text field
        JComponent spinnerEditor = pageFrameSpinner.getEditor();
        if (spinnerEditor instanceof JSpinner.DefaultEditor) {
            JTextField tf = ((JSpinner.DefaultEditor) spinnerEditor).getTextField();
            tf.setFont(branding.jetBrainsRMedium);
            tf.setForeground(branding.light);
            tf.setBackground(branding.dark);
            tf.setCaretColor(branding.light);
            tf.setBorder(new EmptyBorder(0, 8, 0, 4));
            tf.setHorizontalAlignment(JTextField.LEFT);
        }

        // Style the spinner itself
        pageFrameSpinner.setBackground(branding.dark);
        pageFrameSpinner.setForeground(branding.light);
        pageFrameSpinner.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(branding.light, 2, true), // white rounded border
            new EmptyBorder(5,5,5,5)
        ));

        // Style the arrow buttons
        for (Component comp : pageFrameSpinner.getComponents()) {
            if (comp instanceof JButton button) {
                button.setBackground(branding.dark);
                button.setForeground(branding.light);
                button.setBorder(new EmptyBorder(2, 4, 2, 4));
                button.setFocusPainted(false);
                button.setContentAreaFilled(false);
                button.setOpaque(false);

                // Custom paint for triangle arrows
                String name = button.getName();
                button.setIcon(new Icon() {
                    @Override
                    public void paintIcon(Component c, Graphics g, int x, int y) {
                        Graphics2D g2 = (Graphics2D) g.create();
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setColor(branding.light);

                        int w = getIconWidth();
                        int h = getIconHeight();

                        // Up arrow for the top button, down arrow for the bottom
                        if ("Spinner.nextButton".equals(name)) {
                            int[] xp = {x, x + w / 2, x + w};
                            int[] yp = {y + h, y, y + h};
                            g2.fillPolygon(xp, yp, 3);
                        } else {
                            int[] xp = {x, x + w / 2, x + w};
                            int[] yp = {y, y + h, y};
                            g2.fillPolygon(xp, yp, 3);
                        }
                        g2.dispose();
                    }

                    @Override public int getIconWidth()  { return 8; }
                    @Override public int getIconHeight() { return 6; }
                });
            }
        }

        content.add(pageFrameSpinner);
        content.add(Box.createVerticalStrut(28));

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        btnRow.setOpaque(false);
        btnRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        btnRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 54));

        JButton randomBtn = createOtherBtn("Random Inputs");
        randomBtn.setPreferredSize(new Dimension(210, 44));
        randomBtn.setMaximumSize(new Dimension(210, 44));

        JButton importBtn = createOtherBtn("Import File");
        importBtn.setPreferredSize(new Dimension(210, 44));
        importBtn.setMaximumSize(new Dimension(210, 44));

        randomBtn.addActionListener(e -> generateRandomInput());
        importBtn.addActionListener(e -> importFromFile());

        btnRow.add(randomBtn);
        btnRow.add(importBtn);
        content.add(btnRow);
        content.add(Box.createVerticalGlue());

        leftPanel.add(content, BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(0, 30, 30, 30));

        JButton simulateBtn = new JButton("Simulate") {
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
        simulateBtn.setFont(branding.jetBrainsBMedium);
        simulateBtn.setForeground(branding.light);
        simulateBtn.setBackground(branding.dark);
        simulateBtn.setContentAreaFilled(false);
        simulateBtn.setBorderPainted(false);
        simulateBtn.setFocusPainted(false);
        simulateBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        simulateBtn.setPreferredSize(new Dimension(0, 52));
        simulateBtn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e)  { simulateBtn.setBackground(branding.darkGray); }
            @Override public void mouseExited(MouseEvent e)   { simulateBtn.setBackground(branding.dark);     }
            @Override public void mousePressed(MouseEvent e)  { simulateBtn.setBackground(branding.darkGray); }
            @Override public void mouseReleased(MouseEvent e) { simulateBtn.setBackground(branding.darkGray); }
        });
        simulateBtn.addActionListener(e -> {
            if (!validatePreSimulation()) return;

            int frameCount = (int) pageFrameSpinner.getValue();
            String[] tokens = inputArea.getText().trim().split("\\s+");
            int[] refString = new int[tokens.length];
            for (int i = 0; i < tokens.length; i++)
                refString[i] = Integer.parseInt(tokens[i]);

            List<ScrnSimulatorOutput.AlgoResult> results =
                mainEngine.runSimulation(refString, frameCount);

            mainEngine.getGUI().getSimulatorOutput().loadSimulationResults(results);

            CardLayout cl = (CardLayout) parentContainer.getLayout();
            cl.show(parentContainer, "SimulatorOutput");
        });

        footer.add(simulateBtn, BorderLayout.CENTER);
        leftPanel.add(footer, BorderLayout.SOUTH);
    }

    public void initializeRightPanel() {
        rightPanel = new JPanel(new BorderLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(branding.dark);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                g2.setColor(branding.light);
                g2.setStroke(new BasicStroke(3f));
                g2.drawRoundRect(1, 1, getWidth() - 2, getHeight() - 2, 20, 20);
                g2.dispose();
            }
        };
        rightPanel.setOpaque(false);

        String[] algorithms = {
            "First-In-First-Out (FIFO)",
            "Last Recently Used (LRU)",
            "Optimal Page Replacement Algorithm (OPT)",
            "Second Chance Algorithm",
            "Enhanced Second Chance Algorithm",
            "Least Frequently Used (LFU)",
            "Most Frequently Used (MFU)",
        };

        JLabel algorithmLabel = createHeaderLabel("Select Page Replacement Algorithm", false);
        algorithmLabel.setBorder(new EmptyBorder(16, 16, 8, 16));
        rightPanel.add(algorithmLabel, BorderLayout.NORTH);

        algoListPanel = new JPanel();
        algoListPanel.setOpaque(false);
        algoListPanel.setLayout(new BoxLayout(algoListPanel, BoxLayout.Y_AXIS));
        rightPanel.add(algoListPanel, BorderLayout.CENTER);

        for (String algo : algorithms) {
            JButton algoBtn = createOtherBtn(algo);
            algoBtn.addActionListener(e -> {
                boolean isNowSelected = !mainEngine.getChosenAlgorithms().get(algo);
                mainEngine.setChosenAlgorithm(algo, isNowSelected);
                // Set the "resting" color based on selected state
                algoBtn.putClientProperty("selected", isNowSelected);
                algoBtn.setBackground(isNowSelected ? branding.selected : branding.dark);
            });

            algoBtn.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    algoBtn.setBackground(branding.darkGray); // always highlight on hover
                }
                @Override
                public void mouseExited(MouseEvent e) {
                    // Restore based on selected state, not always dark
                    Boolean selected = (Boolean) algoBtn.getClientProperty("selected");
                    algoBtn.setBackground(Boolean.TRUE.equals(selected) ? branding.selected : branding.dark);
                }
                @Override
                public void mousePressed(MouseEvent e)  { algoBtn.setBackground(branding.selected); }
                @Override
                public void mouseReleased(MouseEvent e) {
                    Boolean selected = (Boolean) algoBtn.getClientProperty("selected");
                    algoBtn.setBackground(Boolean.TRUE.equals(selected) ? branding.selected : branding.dark);
                }
            });

            algoListPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            algoListPanel.add(algoBtn);
        }
    }

    // ==================================================
    //                  UTILITY METHODS
    // ==================================================

    public void importFromFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Import Input String from Text File");
        chooser.setFileFilter(
            new javax.swing.filechooser.FileNameExtensionFilter("Text files (*.txt)", "txt"));
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

        MainEngine.ImportResult result = mainEngine.importFromFile(chooser.getSelectedFile());
        if (!result.valid) { showValidationError(result.errorMessage); return; }

        inputArea.setText(result.inputString);
        pageFrameSpinner.setValue(result.frameCount);
    }

    public void generateRandomInput() {
        MainEngine.RandomInput ri = mainEngine.generateRandomInput();
        inputArea.setText(ri.inputString);
        pageFrameSpinner.setValue(ri.frameCount);
    }

    public boolean validatePreSimulation() {
        int frameCount = (int) pageFrameSpinner.getValue();
        MainEngine.ValidationResult result =
            mainEngine.validateSimulationInput(inputArea.getText(), frameCount);
        if (!result.valid) { showValidationError(result.errorMessage); return false; }
        return true;
    }

    public JButton createOtherBtn(String text) {
        JButton btn = new JButton(text) {
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
        btn.setFont(branding.jetBrainsBMedium);
        btn.setForeground(branding.light);
        btn.setBackground(branding.dark);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setPreferredSize(new Dimension(120, 44));
        btn.setMaximumSize(new Dimension(350, 44));

        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e)  { btn.setBackground(branding.darkGray); }
            @Override public void mouseExited(MouseEvent e)   { btn.setBackground(branding.dark);     }
            @Override public void mousePressed(MouseEvent e)  { btn.setBackground(branding.darkGray); }
            @Override public void mouseReleased(MouseEvent e) { btn.setBackground(branding.darkGray); }
        });

        return btn;
    }

    public JButton createProcessButton(ImageIcon icon, String text) {
        JButton btn = new JButton(text, icon) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                g2.setColor(branding.light);
                g2.setStroke(new BasicStroke(3f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 14, 14);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        btn.setFont(branding.jetBrainsBMedium);
        btn.setForeground(branding.light);
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setIconTextGap(10);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 62));
        btn.setPreferredSize(new Dimension(350, 62));
        btn.setBorder(new EmptyBorder(0, 20, 0, 20));
        btn.setBackground(branding.dark);

        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(branding.darkGray); }
            @Override public void mouseExited(MouseEvent e) { btn.setBackground(branding.dark);}
            @Override public void mousePressed(MouseEvent e) { btn.setBackground(branding.darkGray); }
            @Override public void mouseReleased(MouseEvent e) { btn.setBackground(branding.darkGray); }
        });

        return btn;
    }

    public JLabel createHeaderLabel(String text, boolean muted) {
        JLabel lbl = new JLabel(text, SwingConstants.LEFT);
        lbl.setFont(branding.jetBrainsBMedium);
        lbl.setForeground(muted ? branding.darkGray : branding.light);
        return lbl;
    }

    public void styleTextField(JTextField field) {
        field.setFont(branding.jetBrainsRMedium);
        field.setForeground(branding.light);
        field.setBackground(branding.dark);
        field.setCaretColor(branding.light);
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(branding.light, 2, true),
            new EmptyBorder(6, 10, 6, 10)
        ));
    }

    public void styleComboBox(JComboBox<String> box) {
        box.setFont(branding.jetBrainsRMedium);
        box.setForeground(branding.light);
        box.setBackground(branding.dark);
        box.setBorder(new LineBorder(branding.light, 3, true));
        box.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                setBackground(isSelected ? branding.darkGray : branding.dark);
                setForeground(branding.light);
                setFont(branding.jetBrainsRMedium);
                setBorder(new EmptyBorder(6, 10, 6, 10));
                return this;
            }
        });
    }

    public void styleHeaderLabel(JLabel label, boolean enabled) {
        label.setForeground(enabled ? branding.light : branding.darkGray);
    }

    public void styleProcessField(JTextField field, boolean enabled) {
        field.setEnabled(enabled);
        field.setFont(branding.jetBrainsBMedium);
        field.setBackground(branding.dark);
        field.setForeground(enabled ? branding.light : branding.darkGray);
        field.setCaretColor(enabled ? branding.light : branding.darkGray);
        field.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(enabled ? branding.light : branding.darkGray, 2, true),
            new EmptyBorder(6, 10, 6, 10)
        ));
    }

    public JPanel blankPanel(Color bg, int w, int h) {
        JPanel p = new JPanel();
        p.setBackground(bg);
        if (w > 0) p.setPreferredSize(new Dimension(w, 0));
        if (h > 0) p.setPreferredSize(new Dimension(0, h));
        return p;
    }

    public void showValidationError(String message) {
        JOptionPane.showMessageDialog(this, message, "Invalid Input", JOptionPane.ERROR_MESSAGE);
    }

    public boolean isInteger(String str) {
        if (str == null || str.isEmpty()) return false;
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void refreshStyles() {
        // Input text area
        inputArea.setForeground(branding.light);
        inputArea.setBackground(branding.dark);
        inputArea.setCaretColor(branding.light);
        inputArea.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(branding.light, 2, true),
            new EmptyBorder(8, 16, 8, 16)
        ));

        // Spinner
        pageFrameSpinner.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(branding.light, 2, true),
            new EmptyBorder(2, 4, 2, 4)
        ));
        JComponent spinnerEditor = pageFrameSpinner.getEditor();
        if (spinnerEditor instanceof JSpinner.DefaultEditor) {
            JTextField tf = ((JSpinner.DefaultEditor) spinnerEditor).getTextField();
            tf.setForeground(branding.light);
            tf.setBackground(branding.dark);
            tf.setOpaque(true);
            tf.setCaretColor(branding.light);
        }
        pageFrameSpinner.setBackground(branding.dark);

        // Repaint panels to trigger paintComponent (rounded borders)
        leftPanel.repaint();
        rightPanel.repaint();

        // Refresh all buttons in left panel recursively
        refreshButtonStyles(leftPanel);
        refreshButtonStyles(rightPanel);
        refreshButtonStyles(algoListPanel);
    }

    private void refreshButtonStyles(java.awt.Container container) {
        for (java.awt.Component c : container.getComponents()) {
            if (c instanceof JButton) {
                JButton btn = (JButton) c;
                btn.setForeground(branding.light);
                Boolean selected = (Boolean) btn.getClientProperty("selected");
                btn.setBackground(Boolean.TRUE.equals(selected) ? branding.selected : branding.dark);
            } else if (c instanceof java.awt.Container) {
                refreshButtonStyles((java.awt.Container) c);
            }
        }
    }

    // ==================================================
    //                GETTERS AND SETTERS
    // ==================================================

    public JPanel getLeftPanel() { return leftPanel; }
    public JPanel getRightPanel() { return rightPanel; }
    public JTextArea getInputArea() { return inputArea; }
    public int getPageFrameCount() { return (int) pageFrameSpinner.getValue(); }
}
```


#### Short summary: 

empty definition using pc, found symbol in pc: javax/swing/JPanel#