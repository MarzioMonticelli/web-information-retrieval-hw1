package com.weird.hw1.ese;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import com.weird.hw1.ese.utils.entities.entries.BasicEntry;
import com.weird.hw1.ese.utils.entities.results.Result;
import com.weird.hw1.ese.utils.entities.results.ResultARP;
import com.weird.hw1.ese.utils.structures.HTLEvaluate;
import com.weird.hw1.ese.utils.structures.HTLGroundTruth;
import com.weird.hw1.ese.utils.structures.HashTableList;

public class AverageRPrecision {
	protected static List<Result> Results = new LinkedList<>();

	protected static void usage() {
		System.out.println("Command evaluateFile KnowledgeFile");
	}
	
	public static void main(String[] args) {
		//System.out.println("Average RPrecision");
		if (args.length != 2) {
			usage();
			System.exit(-1);
		}

		String evaluatePath = args[0], knowledgePath = args[1];

		// checks existence of the file to evaluate
		File evaluateFile = new File(evaluatePath);
		if (!evaluateFile.exists() || !evaluateFile.isFile()) {
			System.out.println("Can not find evaluate file: " + evaluatePath);
			System.exit(0);
		}

		// checks existence of the knowledge file
		File knowledgeFile = new File(knowledgePath);
		if (!knowledgeFile.exists() || !knowledgeFile.isFile()) {
			System.out.println("Can not find knowledge file: " + knowledgePath);
			System.exit(0);
		}

		double ARP = AverageRPrecision.calculateAverageRPrecision(evaluateFile, knowledgeFile);
		System.out.print("Average RPrecision: " + ARP + "\t->\t");
		System.out.printf("%.3f\n", ARP);
	}

	/**
	 * 
	 * @param evaluateFile
	 *            file to evaluate
	 * @param knowledgeFile
	 *            GroundTruth file
	 * @return the Averate R-Precision Score
	 */
	public static double calculateAverageRPrecision(File evaluateFile, File knowledgeFile) {
		// Data structure for store the tsv file entries
		HashTableList evaluateList = new HTLEvaluate();
		evaluateList.fillFromFile(evaluateFile);
		HashTableList knowledgeList = new HTLGroundTruth();
		knowledgeList.fillFromFile(knowledgeFile);

		// System.out.println("EvaluateList size: " + evaluateList.size());
		// System.out.println("KnowledgeList size: " + knowledgeList.size());

		List<Result> Results = new LinkedList<>();
		Integer max = evaluateList.getLastEntryId();
		if (max == null) {
			return 0.0;
		}
		
		for (int i = 1; i <= max; i++) {
			try {
				List<BasicEntry> eEntries = evaluateList.getEntriesFor(i);
				List<BasicEntry> kEntries = knowledgeList.getEntriesFor(i);

				int R = kEntries.size();
				int upBound = (R < eEntries.size()) ? R : eEntries.size();

				// count the min(R, evaluated) occurrences in the first R
				// groundTruth entries,
				// and save it in r.
				// So, we can calculate the R-Precision = r/R
				int r = 0;
				for (int j = 0; j < upBound; j++) {
					BasicEntry eEntry = eEntries.get(j);

					// Used to check if the Rank-Order is preserved: It IS
					// System.out.println("Rank: " + ((OutputEntry) eEntry).getRank());
					for (BasicEntry kEntry : kEntries) {
						if (kEntry.getDocId() == eEntry.getDocId()) {
							// If the docId is present both in the first min(R,
							// evaluated) entries of evaluated
							// set and in the first min(R, evaluated) entries of
							// the knowledge set
							r++;
							break;
						}
					}
				}

				if (R == 0) {
					continue;
				}
				// Calculate the R-Precision Score
				double RPscore = (double) r / (double) R;
				// System.out.println("Score for query id " + i + ": r=" + r + "
				// R=" + R + "\tScore=" + RPscore);
				Results.add(new ResultARP(i, RPscore));
			} catch (NullPointerException e) {
				continue;
			}
		}

		// Calculate the Average R-Precision score
		// 1/#scores * sum(scores)
		double AverageScore = 0;
		for (Result result : Results) {
			AverageScore += result.getScore();
		}
		AverageScore /= Results.size();

		return AverageScore;
	}

}
