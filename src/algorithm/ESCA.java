package algorithm;

import graphics.ScrnSimulatorOutput.AlgoResult;
import java.util.LinkedList;
import java.util.Queue;

public class ESCA implements PageReplacementAlgorithm {

    @Override
    public AlgoResult run(String algorithmName, int[] referenceString, int frameCount) {
        AlgoResult result = new AlgoResult();
        result.algorithmName = algorithmName;
        result.referenceString = referenceString;
        result.pageFrameCount = frameCount;
        result.frameStates = new int[referenceString.length][frameCount];
        result.hits = new boolean[referenceString.length];

        for (int[] row : result.frameStates) java.util.Arrays.fill(row, -1);

        int[] frames = new int[frameCount];
        boolean[] referenceBits = new boolean[frameCount];
        boolean[] modifyBits = new boolean[frameCount];
        java.util.Arrays.fill(frames, -1);

        int pointer = 0, filled = 0, pageFaults = 0;

        for (int step = 0; step < referenceString.length; step++) {
            int page = referenceString[step];
            
            boolean hit = false;
            for (int i = 0; i < frameCount; i++) {
                if (frames[i] == page) {
                    hit = true;
                    referenceBits[i] = true;
                    modifyBits[i] = (step % 2 == 0);
                    break;
                }
            }

            result.hits[step] = hit;

            if (!hit) {
                pageFaults++;

                if (filled < frameCount) {
                    frames[filled] = page;
                    referenceBits[filled] = false;
                    modifyBits[filled] = false;
                    filled++;
                    pointer = filled % frameCount;
                } else {
                    int victim = -1;

                    for (int pass = 0; pass < 2 && victim == -1; pass++) {
                        for (int classTarget = 0; classTarget <= 3 && victim == -1; classTarget++) {
                            for (int scan = 0; scan < frameCount; scan++) {
                                int idx = (pointer + scan) % frameCount;
                                int rBit = referenceBits[idx] ? 1 : 0;
                                int mBit = modifyBits[idx] ? 1 : 0;
                                int cls = rBit * 2 + mBit;

                                if (cls == classTarget) {
                                    victim = idx;
                                    break;
                                }
                            }
                        }
                        
                        if (pass == 0 && victim == -1) {
                            for (int i = 0; i < frameCount; i++) referenceBits[i] = false;
                        }
                    }

                    frames[victim] = page;
                    referenceBits[victim] = false;
                    modifyBits[victim] = false;
                    pointer = (victim + 1) % frameCount;
                }
            }
            
            for (int i = 0; i < frameCount; i++) result.frameStates[step][i] = frames[i];
        }

        result.totalPageFaults = pageFaults;
        return result;
    }
    
}
