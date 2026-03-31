package algorithm;

import graphics.ScrnSimulatorOutput.AlgoResult;

public class LFU implements PageReplacementAlgorithm {

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
		int[] frequencies = new int[frameCount];
		int[] insertedAt = new int[frameCount];
		java.util.Arrays.fill(frames, -1);
		java.util.Arrays.fill(insertedAt, -1);

		int filled = 0;
		int pageFaults = 0;

		for (int step = 0; step < referenceString.length; step++) {
			int page = referenceString[step];

			boolean hit = false;
			int hitIndex = -1;
			for (int i = 0; i < frameCount; i++) {
				if (frames[i] == page) {
					hit = true;
					hitIndex = i;
					break;
				}
			}

			result.hits[step] = hit;

			if (hit) {
				frequencies[hitIndex]++;
			} else {
				pageFaults++;

				if (filled < frameCount) {
					frames[filled] = page;
					frequencies[filled] = 1;
					insertedAt[filled] = step;
					filled++;
				} else {
					int victim = 0;
					for (int i = 1; i < frameCount; i++) {
						if (frequencies[i] < frequencies[victim]) {
							victim = i;
						} else if (frequencies[i] == frequencies[victim]
								&& insertedAt[i] < insertedAt[victim]) {
							victim = i;
						}
					}

					frames[victim] = page;
					frequencies[victim] = 1;
					insertedAt[victim] = step;
				}
			}

			for (int i = 0; i < frameCount; i++) result.frameStates[step][i] = frames[i];
		}

		result.totalPageFaults = pageFaults;
		return result;
	}
}
