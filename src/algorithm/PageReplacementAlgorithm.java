package algorithm;

import graphics.ScrnSimulatorOutput.AlgoResult;

public interface PageReplacementAlgorithm {
    //Run the algorithm and return a fully-populated AlgoResult.
    AlgoResult run(String algorithmName, int[] referenceString, int frameCount);
}