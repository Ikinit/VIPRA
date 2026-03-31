package algorithm;

import graphics.ScrnSimulatorOutput.AlgoResult;
import java.util.LinkedList;
import java.util.Queue;

public class FIFO implements PageReplacementAlgorithm {

    @Override
    public AlgoResult run(String algorithmName, int[] referenceString, int frameCount) {
        AlgoResult r      = new AlgoResult();
        r.algorithmName   = algorithmName;
        r.referenceString = referenceString;
        r.pageFrameCount  = frameCount;
        r.hits            = new boolean[referenceString.length];
        r.frameStates     = new int[referenceString.length][frameCount];

        for (int[] row : r.frameStates)
            java.util.Arrays.fill(row, -1);

        Queue<Integer> queue  = new LinkedList<>();
        int[] currentFrames   = new int[frameCount];
        java.util.Arrays.fill(currentFrames, -1);

        int faults = 0;

        for (int step = 0; step < referenceString.length; step++) {
            int page = referenceString[step];
            boolean hit = false;
            for (int f = 0; f < frameCount; f++) {
                if (currentFrames[f] == page) { hit = true; break; }
            }

            r.hits[step] = hit;

            if (!hit) {
                faults++;
                if (queue.size() < frameCount) {
                    for (int f = 0; f < frameCount; f++) {
                        if (currentFrames[f] == -1) {
                            currentFrames[f] = page;
                            break;
                        }
                    }
                } else {
                    int evicted = queue.poll();
                    for (int f = 0; f < frameCount; f++) {
                        if (currentFrames[f] == evicted) {
                            currentFrames[f] = page;
                            break;
                        }
                    }
                }
                queue.add(page);
            }
            r.frameStates[step] = currentFrames.clone();
        }
        r.totalPageFaults = faults;
        return r;
    }
}