package algorithm;

import graphics.ScrnSimulatorOutput.AlgoResult;
import java.util.LinkedList;
import java.util.Queue;

public class SCA implements PageReplacementAlgorithm {

    @Override
    public AlgoResult run(String algoName, int[] referenceString, int frameCount) {
        AlgoResult result = new AlgoResult();
        result.algorithmName = algoName;
        result.referenceString = referenceString;
        result.pageFrameCount = frameCount;
        result.frameStates = new int[referenceString.length][frameCount];
        result.hits = new boolean[referenceString.length];
        
        for (int[] row : result.frameStates) java.util.Arrays.fill(row, -1);

        int[] frames = new int[frameCount];
        boolean[] referenceBits = new boolean[frameCount];
        java.util.Arrays.fill(frames, -1);

        int pointer = 0, filled = 0, pageFaults = 0;

        for (int step = 0; step < referenceString.length; step++) {
            int page = referenceString[step];
            
            boolean hit = false;
            for (int i = 0; i < frameCount; i++) {
                if (frames[i] == page) {
                    hit = true;
                    referenceBits[i] = true;
                    break;
                }
            }

            result.hits[step] = hit;

            if (!hit) {
                pageFaults++;

                if (filled < frameCount) {
                    frames[filled] = page;
                    referenceBits[filled] = false;
                    filled++;
                    pointer = filled % frameCount;
                } else {
                    while (referenceBits[pointer]) {
                        referenceBits[pointer] = false;
                        pointer = (pointer + 1) % frameCount;
                    }
                    
                    frames[pointer] = page;
                    referenceBits[pointer] = false;
                    pointer = (pointer + 1) % frameCount;
                }
            }
            
            for (int i = 0; i < frameCount; i++) result.frameStates[step][i] = frames[i];
        }

        result.totalPageFaults = pageFaults;
        return result;
    }
}