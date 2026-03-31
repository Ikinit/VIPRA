package algorithm;

import graphics.ScrnSimulatorOutput.AlgoResult;

public class OPT implements PageReplacementAlgorithm {

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
		java.util.Arrays.fill(frames, -1);

		int filled = 0;
		int pageFaults = 0;

		for (int step = 0; step < referenceString.length; step++) {
			int page = referenceString[step];

			boolean hit = false;
			for (int i = 0; i < frameCount; i++) {
				if (frames[i] == page) {
					hit = true;
					break;
				}
			}

			result.hits[step] = hit;

			if (!hit) {
				pageFaults++;

				if (filled < frameCount) {
					frames[filled] = page;
					filled++;
				} else {
					int victim = 0;
					int farthestNextUse = -1;

					for (int i = 0; i < frameCount; i++) {
						int currentFramePage = frames[i];
						int nextUse = Integer.MAX_VALUE;

						for (int lookAhead = step + 1; lookAhead < referenceString.length; lookAhead++) {
							if (referenceString[lookAhead] == currentFramePage) {
								nextUse = lookAhead;
								break;
							}
						}

						if (nextUse > farthestNextUse) {
							farthestNextUse = nextUse;
							victim = i;
						}
					}

					frames[victim] = page;
				}
			}

			for (int i = 0; i < frameCount; i++) result.frameStates[step][i] = frames[i];
		}

		result.totalPageFaults = pageFaults;
		return result;
	}
}
