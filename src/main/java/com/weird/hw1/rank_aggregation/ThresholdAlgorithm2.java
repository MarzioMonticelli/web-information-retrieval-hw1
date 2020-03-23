package com.weird.hw1.rank_aggregation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.weird.hw1.ese.utils.entities.ETParser;
import com.weird.hw1.ese.utils.entities.entries.BasicEntry;
import com.weird.hw1.ese.utils.entities.entries.OutputEntry;
import com.weird.hw1.ese.utils.structures.HTLEvaluate;

public class ThresholdAlgorithm2 {

	protected static boolean VERB = false, VVERB = false;
	// protected static Integer K = null;

	protected static void usage() {
		System.out.println("Usage: command [ --verbose | --Vverbose | -r | -k ] FILE_Text FILE_Title File_Output");
	}

	public static void main(String[] args) {
		// System.out.println("Average nMDCG");
		if (args.length < 3) {
			usage();
			System.exit(-1);
		}

		Integer K = null;
		Integer Ratio = 1;

		// argument parsing
		List<String> params = new LinkedList<>();
		for (String arg : args) {
			params.add(arg);
		}

		// Search -* parameters
		for (int index = 0; index < params.size();) {
			String param = params.get(index);
			if (param.equals("-k") || param.equals("-K")) {
				try {
					if (index + 1 >= params.size()) {
						System.out.println("[ERR] K Undefined");
						System.exit(-1);
					}
					K = Integer.parseInt(params.get(index + 1));
					params.remove(index + 1);
					params.remove(index);
				} catch (NumberFormatException e) {
					System.out.println("Invalid K: " + params.get(index + 1));
					System.exit(-1);
				}
			} else if (("--verbose").equals(param)) {
				VERB = true;
				params.remove(index);
			} else if (("--Vverbose").equals(param)) {
				VERB = true;
				VVERB = true;
				params.remove(index);
			} else if (("-r").equals(param)) {
				try {
					if (index + 1 >= params.size()) {
						System.out.println("[ERR] Ratio Undefined");
						System.exit(-1);
					}
					Ratio = Integer.parseInt(params.get(index + 1));
					params.remove(index + 1);
					params.remove(index);
				} catch (NumberFormatException e) {
					System.out.println("Invalid R: " + params.get(index + 1));
					System.exit(-1);
				}
			} else {
				index++;
			}
		}

		// Check on number of remaining arguments
		if (params.size() < 3) {
			usage();
			System.exit(-1);
		}

		// First Mandatory Parameter: Text File
		// Parse Text-file
		String textFilePath = params.get(0);
		if (VERB) {
			System.out.println("Text: " + textFilePath);
		}
		File textFile = new File(textFilePath);
		if (!textFile.exists() || !textFile.isFile()) {
			System.out.println("Can not find evaluate file: " + textFilePath);
			System.exit(0);
		}

		HTLEvaluate textHTL = new HTLEvaluate();

		try {
			textHTL = ETParser.parseFile(textFile);
		} catch (IOException e) {
			System.out.println("Can not parse file: " + textFile.getName());
			e.printStackTrace();
			System.exit(0);
		}

		// Second Mandatory Parameter: Title File
		// Parse Title-file
		String titleFilePath = params.get(1);
		if (VERB) {
			System.out.println("Title: " + titleFilePath);
		}
		File titleFile = new File(titleFilePath);
		if (!titleFile.exists() || !titleFile.isFile()) {
			System.out.println("Can not find evaluate file: " + titleFilePath);
			System.exit(0);
		}

		HTLEvaluate titleHTL = new HTLEvaluate();

		try {
			titleHTL = ETParser.parseFile(titleFile);
		} catch (IOException e) {
			System.out.println("Can not parse file: " + titleFile.getName());
			e.printStackTrace();
			System.exit(0);
		}

		// Third Mandatory Parameter: Output File
		String outputFilePath = params.get(2);
		if (VERB) {
			System.out.println("Output: " + outputFilePath);
		}
		File outputFile = new File(outputFilePath);

		if (VERB) {
			System.out.println("############ Threshold Algorithm results #############\n");
		}

		List<OutputEntry> Results = new LinkedList<>();

		// Integer i = 2;
		for (Integer i : titleHTL.keySet()) {
			Integer k = K;

			HashMap<Integer, OutputEntry> titleRand = new HashMap<>();
			HashMap<Integer, OutputEntry> textRand = new HashMap<>();

			List<OutputEntry> titleSeq = new LinkedList<>();
			List<OutputEntry> textSeq = new LinkedList<>();

			for (BasicEntry beti : titleHTL.getEntriesFor(i)) {
				OutputEntry t = (OutputEntry) beti;
				titleRand.put(t.getDocId(), t);
				titleSeq.add(t);
			}

			for (BasicEntry bete : textHTL.getEntriesFor(i)) {
				OutputEntry t = (OutputEntry) bete;
				textRand.put(t.getDocId(), t);
				textSeq.add(t);
			}

			if (K == null) {
				k = Math.min(titleRand.size(), textRand.size());
				if (VERB) {
					System.out.println("* Auto-K: K setted to " + k);
				}
			}

			List<OutputEntry> pq = startThresholdAlgorithm(textRand, titleRand, textSeq, titleSeq, k, Ratio);
			int maxLMess = 0;

			// Store results
			Results.addAll(pq);

			if (VERB) {
				for (OutputEntry e : pq) {
					String Mess = "QId: " + e.getQueryId() + ", DocID: " + e.getDocId() + ", Rank: " + e.getRank()
							+ ", Score: " + e.getScore();
					System.out.println(Mess);
					if (maxLMess < Mess.length())
						maxLMess = Mess.length();
				}
				for (int j = 0; j < maxLMess; j++) {
					System.out.print('-');
				}
				System.out.println("");
			}
		}

		// Write results to File
		try {
			CSVPrinter csvPrinter = new CSVPrinter(new FileWriter(outputFile), CSVFormat.TDF);

			// Print header
			csvPrinter.printRecord("Query_ID", "Doc_ID", "Rank", "Score");

			// Print records (Iterable<String>)
			csvPrinter.printRecords(Results);

			csvPrinter.close();
		} catch (IOException e) {
			System.out.println("Error Trying to write to the Output File");
			e.printStackTrace();
			System.exit(-1);
		} finally {

			System.out.println(
					"[Threshold Algorithm] Ranks Aggregation successful performed.\nThe results are in the file: "
							+ outputFile.getAbsolutePath());

		}

	}

	private static List<OutputEntry> startThresholdAlgorithm(HashMap<Integer, OutputEntry> textRand,
			HashMap<Integer, OutputEntry> titleRand, List<OutputEntry> textSeq, List<OutputEntry> titleSeq, Integer k,
			Integer ratio) {

		// Result priority queue ordered on score
		PriorityQueue<OutputEntry> R = new PriorityQueue<>(new Comparator<OutputEntry>() {

			@Override
			public int compare(OutputEntry arg0, OutputEntry arg1) {
				if (arg0.getScore() == arg1.getScore()) {
					return 0;
				}
				return (arg0.getScore() - arg1.getScore() < 0) ? -1 : 1;
			}
		});

		thresholdLoop(textRand, titleRand, textSeq, titleSeq, k, ratio, R);

		// Returns the top-K elements of R
		return getTopK(R, k);
	}

	private static void thresholdLoop(HashMap<Integer, OutputEntry> textRand, HashMap<Integer, OutputEntry> titleRand,
			List<OutputEntry> textSeq, List<OutputEntry> titleSeq, Integer k, Integer ratio,
			PriorityQueue<OutputEntry> R) {

		double T = 0.0;

		int[] positions = new int[2];

		loop: while (positions[0] < textSeq.size() && positions[1] < titleSeq.size()) {
			{
				OutputEntry textSE = textSeq.get(positions[0]);
				if (VERB) {
					System.out.println(
							"Sequentially getting " + (positions[0] + 1) + "-th element of Text: " + textSE.getDocId());
				}
				OutputEntry titleRA = titleRand.get(textSE.getDocId());
				double titleRAScore = 0.0;
				if (titleRA != null) {
					titleRAScore = titleRA.getScore();
				}

				if (VERB) {
					System.out.println("Randomly getting element of Title with docID: " + textSE.getDocId() + ": "
							+ ((titleRA != null) ? "found" : "not present"));
				}

				double score = (textSE.getScore() + (ratio * titleRAScore)) / 2;

				if (VERB) {
					System.out.println("Computed Score: " + score);
				}

				if (!R.contains(textSE)) {
					R.add(new OutputEntry(textSE.getQueryId(), textSE.getDocId(), 0, score));
					if (R.size() > k) {
						R.poll();
					}
					if (VERB) {
						System.out.println("Added in R");
					}
				} else {
					if (VERB) {
						System.out.println("Yet Present in R");
					}
				}

				T = score;
				positions[0]++;
			}
			{
				OutputEntry titleSE = titleSeq.get(positions[1]);

				OutputEntry textRA = textRand.get(titleSE.getDocId());
				if (VERB) {

					System.out.println("Sequentially getting " + (positions[1] + 1) + "-th element of Title: "
							+ titleSE.getDocId());
				}
				double textRAScore = 0.0;
				if (textRA != null) {
					textRAScore = textRA.getScore();
					// } else {
					// System.out.println("[QID: " + titleSE.getQueryId() +
					// "] Doc Id: " + titleSE.getDocId()
					// + " Not found in text");
				}

				if (VERB) {
					System.out.println("Randomly getting element of Text with docID: " + titleSE.getDocId() + ": "
							+ ((textRA != null) ? "found" : "not present"));
				}
				double score = (textRAScore + (ratio * titleSE.getScore())) / 2;

				if (VERB) {
					System.out.println("Computed Score: " + score);
				}
				if (!R.contains(titleSE)) {
					R.add(new OutputEntry(titleSE.getQueryId(), titleSE.getDocId(), 0, score));
					if (R.size() > k) {
						R.poll();
					}
					if (VERB) {
						System.out.println("Added in R");
					}
				} else {
					if (VERB) {
						System.out.println("Yet Present in R");
					}
				}

				T += score;
				positions[1]++;
			}

			if (R.size() >= k) {
				OutputEntry[] rr = R.toArray(new OutputEntry[0]);
				for (OutputEntry r : rr) {
					if (r.getScore() < T) {
						continue loop;
					}
				}

				return;
			}
		}
	}

	/**
	 * Returns the top-K element from the priority queue R
	 * 
	 * @param R
	 * @param K
	 * @return
	 */
	private static List<OutputEntry> getTopK(PriorityQueue<OutputEntry> R, int K) {
		List<OutputEntry> TopK = new ArrayList<>(K);

		if (K>R.size()) {
			K = R.size();
		}
		
		for (int i = K; i >= 1; i--) {
			OutputEntry e = R.poll();
			// correctly setting the rank field
			e.setRank(i);
			TopK.add(e);
		}

		// reorder
		Collections.sort(TopK, new Comparator<OutputEntry>() {

			@Override
			public int compare(OutputEntry o1, OutputEntry o2) {
				return o1.getRank() - o2.getRank();
			}
		});

		return TopK;
	}

}
