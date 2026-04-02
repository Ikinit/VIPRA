package engine;

import algorithm.ESCA;
import algorithm.FIFO;
import algorithm.LFU;
import algorithm.LRU;
import algorithm.MFU;
import algorithm.OPT;
import algorithm.PageReplacementAlgorithm;
import algorithm.SCA;
import graphics.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class MainEngine {
    private MainGUI gui;
    private HashMap<String, Boolean> chosenAlgorithms;
    private final Map<String, PageReplacementAlgorithm> algorithmRegistry = new HashMap<>();

    public static final int MIN_TOKENS = 10;
    public static final int MAX_TOKENS = 40;
    public static final int MIN_PAGE_VAL = 0;
    public static final int MAX_PAGE_VAL = 20;
    public static final int MIN_FRAMES = 3;
    public static final int MAX_FRAMES = 10;

    public MainEngine() {
        chosenAlgorithms = new HashMap<>();
        initializeAlgorithms();
        initializeRegistry();
    }

    public void initializeAlgorithms() {
        String[] algorithms = {
            "First-In-First-Out (FIFO)",
            "Last Recently Used (LRU)",
            "Optimal Page Replacement Algorithm (OPT)",
            "Second Chance Algorithm",
            "Enhanced Second Chance Algorithm",
            "Least Frequently Used (LFU)",
            "Most Frequently Used (MFU)",
        };
        for (String algorithm : algorithms) {
            chosenAlgorithms.put(algorithm, false);
        }
    }

    private void initializeRegistry() {
        algorithmRegistry.put("First-In-First-Out (FIFO)", new FIFO());
        algorithmRegistry.put("Last Recently Used (LRU)", new LRU());
        algorithmRegistry.put("Optimal Page Replacement Algorithm (OPT)", new OPT());
        algorithmRegistry.put("Second Chance Algorithm", new SCA());
        algorithmRegistry.put("Enhanced Second Chance Algorithm", new ESCA());
        algorithmRegistry.put("Least Frequently Used (LFU)", new LFU());
        algorithmRegistry.put("Most Frequently Used (MFU)", new MFU());
    }

    // ==================================================
    //               SIMULATION LOGIC
    // ==================================================

    // Runs selected algorithms and returns results for ScrnSimulatorOutput
    public List<ScrnSimulatorOutput.AlgoResult> runSimulation(int[] referenceString, int frameCount) {
        return getSelectedAlgorithmNames().stream().map(algoName -> {
            PageReplacementAlgorithm algo = algorithmRegistry.get(algoName);
            if (algo != null)
                return algo.run(algoName, referenceString, frameCount);

            // Fallback placeholder for unimplemented algorithms
            ScrnSimulatorOutput.AlgoResult r = new ScrnSimulatorOutput.AlgoResult();
            r.algorithmName = algoName + " (not yet implemented)";
            r.referenceString = referenceString;
            r.pageFrameCount = frameCount;
            r.totalPageFaults = 0;
            r.frameStates = new int[referenceString.length][frameCount];
            r.hits = new boolean[referenceString.length];
            for (int[] row : r.frameStates) java.util.Arrays.fill(row, -1);
            return r;
        }).collect(Collectors.toList());
    }

    private int[][] makePlaceholderFrameStates(int steps, int frames) {
        int[][] states = new int[steps][frames];
        for (int[] row : states)
            java.util.Arrays.fill(row, -1);
        return states;
    }

    // ==================================================
    //               VALIDATION LOGIC
    // ==================================================

    public static class ValidationResult {
        public final boolean valid;
        public final String errorMessage;
        public final int[] parsedTokens;

        private ValidationResult(boolean valid, String errorMessage, int[] parsedTokens) {
            this.valid = valid;
            this.errorMessage = errorMessage;
            this.parsedTokens = parsedTokens;
        }

        public static ValidationResult ok(int[] tokens) {
            return new ValidationResult(true, null, tokens);
        }
        public static ValidationResult fail(String msg) {
            return new ValidationResult(false, msg, null);
        }
    }

    // Validates raw input string, frame count, and algorithm selection
    public ValidationResult validateSimulationInput(String rawInput, int frameCount) {
        if (rawInput == null || rawInput.trim().isEmpty())
            return ValidationResult.fail("Input string cannot be empty.");

        String[] tokens = rawInput.trim().split("\\s+");

        if (tokens.length < MIN_TOKENS || tokens.length > MAX_TOKENS)
            return ValidationResult.fail(
                "Input string must be between " + MIN_TOKENS + " and " + MAX_TOKENS + " tokens.");

        int[] values = new int[tokens.length];
        for (int i = 0; i < tokens.length; i++) {
            if (!isInteger(tokens[i]))
                return ValidationResult.fail("Input string must contain only integers separated by spaces.");
            values[i] = Integer.parseInt(tokens[i]);
            if (values[i] < MIN_PAGE_VAL || values[i] > MAX_PAGE_VAL)
                return ValidationResult.fail("Page reference values must be between " + MIN_PAGE_VAL + " and " + MAX_PAGE_VAL + ".");
        }

        if (frameCount < MIN_FRAMES || frameCount > MAX_FRAMES)
            return ValidationResult.fail("Number of page frames must be between " + MIN_FRAMES + " and " + MAX_FRAMES + ".");

        if (getSelectedAlgorithmNames().isEmpty())
            return ValidationResult.fail("Please select at least one page replacement algorithm.");

        return ValidationResult.ok(values);
    }

    // ==================================================
    //               INPUT GENERATION
    // ==================================================

    public static class RandomInput {
        public final String inputString;
        public final int frameCount;

        public RandomInput(String inputString, int frameCount) {
            this.inputString = inputString;
            this.frameCount = frameCount;
        }
    }

    // Generates a random reference string and frame count within allowed bounds
    public RandomInput generateRandomInput() {
        Random random = new Random();
        int tokenCount = MIN_TOKENS + random.nextInt(MAX_TOKENS - MIN_TOKENS + 1);
        int frames = MIN_FRAMES + random.nextInt(MAX_FRAMES - MIN_FRAMES + 1);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < tokenCount; i++) {
            sb.append(random.nextInt(MAX_PAGE_VAL + 1));
            if (i < tokenCount - 1) sb.append(" ");
        }
        return new RandomInput(sb.toString(), frames);
    }

    // ==================================================
    //               FILE IMPORT
    // ==================================================

    public static class ImportResult {
        public final boolean valid;
        public final String errorMessage;
        public final String inputString;
        public final int frameCount;

        private ImportResult(boolean valid, String errorMessage, String inputString, int frameCount) {
            this.valid = valid;
            this.errorMessage = errorMessage;
            this.inputString = inputString;
            this.frameCount = frameCount;
        }

        public static ImportResult ok(String inputString, int frameCount) {
            return new ImportResult(true, null, inputString, frameCount);
        }
        public static ImportResult fail(String msg) {
            return new ImportResult(false, msg, null, -1);
        }
    }

    // Reads, parses, and validates a .txt import file
    public ImportResult importFromFile(File file) {
        String inputString = null;
        int frames = -1;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.equalsIgnoreCase("Input String:")) {
                    String next = br.readLine();
                    if (next != null) inputString = next.trim();
                } else if (line.equalsIgnoreCase("Number of frames:")) {
                    String next = br.readLine();
                    if (next != null) {
                        try { frames = Integer.parseInt(next.trim()); }
                        catch (NumberFormatException ignored) {}
                    }
                }
            }
        } catch (IOException e) {
            return ImportResult.fail("Failed to read file: " + e.getMessage());
        }

        if (inputString == null || inputString.isEmpty())
            return ImportResult.fail("Could not find 'Input String:' in the file.");

        String[] tokens = inputString.split("\\s+");
        if (tokens.length < MIN_TOKENS || tokens.length > MAX_TOKENS)
            return ImportResult.fail("Input string must have between " +
                MIN_TOKENS + " and " + MAX_TOKENS + " tokens.");

        for (String token : tokens) {
            if (!isInteger(token))
                return ImportResult.fail("Input contains non-integer value: " + token);
            int val = Integer.parseInt(token);
            if (val < MIN_PAGE_VAL || val > MAX_PAGE_VAL)
                return ImportResult.fail(
                    "Page reference values must be between " + MIN_PAGE_VAL +
                    " and " + MAX_PAGE_VAL + ". Found: " + val);
        }

        if (frames < MIN_FRAMES || frames > MAX_FRAMES)
            return ImportResult.fail("Number of frames must be between " + MIN_FRAMES + " and " + MAX_FRAMES + ". Found: " + frames);

        return ImportResult.ok(inputString, frames);
    }

    // ==================================================
    //               HELPERS
    // ==================================================

    public List<String> getSelectedAlgorithmNames() {
        return chosenAlgorithms.entrySet().stream()
            .filter(java.util.Map.Entry::getValue)
            .map(java.util.Map.Entry::getKey)
            .collect(Collectors.toList());
    }

    public static boolean isInteger(String str) {
        if (str == null || str.isEmpty()) return false;
        try { Integer.parseInt(str); return true; }
        catch (NumberFormatException e) { return false; }
    }

    // ==================================================
    //               GETTERS AND SETTERS
    // ==================================================

    public void setGUI(MainGUI gui) { this.gui = gui; }
    public MainGUI getGUI() { return gui; }
    public HashMap<String, Boolean> getChosenAlgorithms() { return chosenAlgorithms; }

    public void setChosenAlgorithm(String algorithm, boolean isChosen) {
        if (chosenAlgorithms.containsKey(algorithm)) {
            chosenAlgorithms.put(algorithm, isChosen);
            System.out.println("Algorithm " + algorithm + " set to " + isChosen);
        }
    }
}