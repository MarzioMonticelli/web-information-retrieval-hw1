package com.weird.hw1.ese;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.weird.hw1.ese.utils.entities.entries.BasicEntry;
import com.weird.hw1.ese.utils.entities.results.ResultMDCG;
import com.weird.hw1.ese.utils.structures.HTLEvaluate;
import com.weird.hw1.ese.utils.structures.HTLGroundTruth;
import com.weird.hw1.ese.utils.structures.HashTableList;
import com.weird.hw1.ese.utils.structures.HashTableListGen;

/**
 * normalized Modified ​ Discounted ​ Cumulative ​ Gain
 * 
 * @author fra
 */
public class nMDCG {
	protected static HashTableList knowledgeList;
	protected static HashTableList evaluateList;

	protected static File knowledgeFile, evaluateFile;

	protected static HashTableListGen<Integer, ResultMDCG> Results = new HashTableListGen<>();
	protected static Map<Integer, Double> AvgResult = new HashMap<>();

	protected static void usage() {
		System.out.println("Usage: command OPTIONS FILE_1 FILE_2");
		System.out.println("\t-k define the value for K [0 for the maximum possible value]");
	}

	protected static int parseArguments(String[] args) {
		// System.out.println("Average nMDCG");
		if (args.length != 4) {
			usage();
			System.exit(0);
		}
		List<String> params = new LinkedList<>();
		for (String arg : args) {
			params.add(arg);
		}

		// Search K parameter
		Integer K = null;
		for (int index = 0; index < params.size(); index++) {
			if (params.get(index).equals("-k")) {
				try {
					if (index + 1 >= params.size()) {
						System.out.println("K Undefined");
						System.exit(-1);
					}
					K = Integer.parseInt(params.get(index + 1));
					params.remove(index + 1);
					params.remove(index);
					break;
				} catch (NumberFormatException e) {
					System.out.println("Invalid K: " + params.get(index + 1));
					System.exit(-1);
				}
			}
		}

		// Mandatory K parameter
		if (K == null) {
			System.out.println("You have to define the \"-k\" parameter");
			usage();
			System.exit(-1);
		}

		// Check on number of remaining arguments
		if (params.size() < 2) {
			System.out.println("Insufficient number of parameters passed");
			usage();
			System.exit(-1);
		}

		String evaluatePath = params.get(0), knowledgePath = params.get(1);

		// checks existence of the file to evaluate
		evaluateFile = new File(evaluatePath);
		if (!evaluateFile.exists() || !evaluateFile.isFile()) {
			System.out.println("Can not find evaluate file: " + evaluatePath);
			System.exit(0);
		}

		// checks existence of the knowledge file
		knowledgeFile = new File(knowledgePath);
		if (!knowledgeFile.exists() || !evaluateFile.isFile()) {
			System.out.println("Can not find knowledge file: " + knowledgePath);
			System.exit(0);
		}

		return K;

	}

	public static void main(String[] args) {

		int K = parseArguments(args);

		nMDCG.calculateAverage_nMDCG(knowledgeFile, evaluateFile, K);

	}

	public static synchronized Map<Integer, Double> calculateAverage_nMDCG(File knowledgeFile, File evaluateFile,
			int K) {
		// Data structure for store the tsv file entries
		evaluateList = new HTLEvaluate();
		evaluateList.fillFromFile(evaluateFile);
		knowledgeList = new HTLGroundTruth();
		knowledgeList.fillFromFile(knowledgeFile);

		Integer max = evaluateList.getLastEntryId();

		Results = new HashTableListGen<>();

		// Maximum K
		if (K==0) {
			for (int qId = 1; qId <= max; qId++){
				List<BasicEntry> eEntries = evaluateList.getEntriesFor(qId);
				List<BasicEntry> kEntries = knowledgeList.getEntriesFor(qId);
				if (!eEntries.isEmpty() && !kEntries.isEmpty()) {
					
					int maxE = (eEntries.size() <= kEntries.size())? eEntries.size() : kEntries.size();
					if (maxE > K) {
						K = maxE;
					}
				}
			}
		}
		
		
		int num = 0;
		for (int qId = 1; qId <= max; qId++) {
			List<BasicEntry> eEntries = evaluateList.getEntriesFor(qId);
			if (eEntries.isEmpty()) {
				continue;
			}
			double scoreT = calculatenMDCG(qId, K);
			// System.out.println("Query Id: " + qId + " K: " + k + " Score: " +
			// scoreT);
			Results.insert(K, new ResultMDCG(qId, scoreT, K));
			num++;
		}

		// System.out.println("Results:" + Results);
		AvgResult = new HashMap<Integer, Double>();

		// Print average Results for each k in K
		for (Integer k : Results.keySet()) {
			Collection<ResultMDCG> values = Results.getEntriesFor(k);

			double score = 0;
			for (ResultMDCG v : values) {
				score += v.getScore();
			}

			double avScore = score / num;
			System.out.print("Average nMDCG for K " + k + " (score/num = " + score + "/" + num + "): ");
			System.out.printf("%.3f\n", avScore);

			AvgResult.put(k, avScore);
		}

		return AvgResult;

	}

	protected static double calculatenMDCG(int queryId, int k) {
		double MDCG = MDCG(queryId, k);
		double MaximumMDCG = nMDCG.MaximumMDCG(queryId, k);

		// System.out.println("qID: "+ queryId +"\tk[" + k +"]\tMDCG: " + MDCG +
		// " MDCG * 2: " + (int) (MDCG * 2) +" MaximumMDCG: " + MaximumMDCG );

		return MDCG / MaximumMDCG;
	}

	public static double MDCG(int queryId, int k) {
		// System.out.println("K: " + k);
		int base = relevance(evaluateList.getEntriesFor(queryId).get(0).getDocId(), queryId, k);
		double sum = 0.0;

		for (int i = 1; i < k; i++) {
			double log2 = Math.log10(i + 1) / Math.log10(2);
			int rel = relevance(evaluateList.getEntriesFor(queryId).get(i).getDocId(), queryId, k);
			sum += (rel / log2);
		}

		// System.out.println("-------------------------------------------");

		return base + sum;
	}

	public static double MaximumMDCG(int queryId, int k) {
		double sum = 1;

		int knowNumEntries = knowledgeList.getEntriesFor(queryId).size();
		// System.out.println("K: " + k + " queryId: " + queryId + " knowledge
		// number of entry for qid: " + knowNumEntries);
		for (int i = 2; i <= k && i <= knowNumEntries; i++) {
			double log2 = Math.log10(i) / Math.log10(2);
			sum += 1 / log2;
		}
		return sum;
	}

	protected static int relevance(int docId, int queryId, int k) {
		List<BasicEntry> entriesList = knowledgeList.getEntriesFor(queryId);

		int r = 0;
		for (BasicEntry bE : entriesList) {
			if (r++ == k) {
				break;
			}
			if (bE.getDocId() == docId) {
				// System.out.println("Relevance for query " + queryId + " for
				// docID " + docId + " for k " + k + " is 1");
				return 1;
			}
		}
		// System.out.println("Relevance for query " + queryId + " for docID " +
		// docId + " for k " + k + " is 0");
		return 0;
	}

	public static synchronized HashTableListGen<Integer, ResultMDCG> getResults() {
		return Results;
	}

	public static synchronized Map<Integer, Double> getAvgResult() {
		return AvgResult;
	}
}
