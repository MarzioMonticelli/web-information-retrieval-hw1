package com.weird.hw1.rank_aggregation;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import com.weird.hw1.ese.utils.entities.ETParser;
import com.weird.hw1.ese.utils.entities.entries.BasicEntry;
import com.weird.hw1.ese.utils.entities.entries.LabeledOutputEntry;
import com.weird.hw1.ese.utils.entities.entries.OutputEntry;
import com.weird.hw1.ese.utils.structures.HTLEvaluate;

public class FaginsAlgorithm {
	protected static boolean VERB = false, VVERB = false;

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
		System.out.println("Text: " + textFilePath);
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
		System.out.println("Title: " + titleFilePath);
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
		System.out.println("Output: " + outputFilePath);
		File outputFile = new File(outputFilePath);

		if (VERB) {
			System.out.println("############ Fagin's Algorithm results #############");
		}

		List<OutputEntry> Results = new LinkedList<>();
		for (Integer i : titleHTL.keySet()) {
			List<List<LabeledOutputEntry>> fullList = new LinkedList<>();

			HashMap<Integer, OutputEntry> titlehList = new HashMap<>();
			HashMap<Integer, OutputEntry> texthList = new HashMap<>();

			List<LabeledOutputEntry> textList = new LinkedList<>();
			List<LabeledOutputEntry> titleList = new LinkedList<>();

			List<OutputEntry> txList = new LinkedList<>();
			List<OutputEntry> tlList = new LinkedList<>();

			for (BasicEntry beti : titleHTL.getEntriesFor(i)) {
				OutputEntry t = (OutputEntry) beti;
				LabeledOutputEntry t1 = new LabeledOutputEntry(t.getQueryId(), t.getDocId(), t.getRank(), t.getScore(),
						"tit");
				titleList.add(t1);
				titlehList.put(t.getDocId(), t);
				tlList.add(t);
			}

			for (BasicEntry bete : textHTL.getEntriesFor(i)) {
				OutputEntry t = (OutputEntry) bete;
				LabeledOutputEntry t1 = new LabeledOutputEntry(t.getQueryId(), t.getDocId(), t.getRank(), t.getScore(),
						"txt");
				textList.add(t1);
				texthList.put(t.getDocId(), t);
				txList.add(t);
			}

			fullList.add(titleList);
			fullList.add(textList);

			// List<LabeledOutputEntry> pq;
			List<OutputEntry> pqhm = new LinkedList<>();
			// pq = new LinkedList<>();

			Integer k = K;
			if (K == null) {
				k = Math.min(titleList.size(), textList.size());
				if (VERB) {
					System.out.println("* Auto-K: K setted to " + k);
				}
			}

			try {
				// pq = startFaginsAlgorithm(titleList, textList, k, Ratio);
				pqhm = startFaginsAlgorithm(titlehList, texthList, tlList, txList, k, Ratio);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
			int maxLMess = 0;

			for (OutputEntry e : pqhm) {
				Results.add(e);
				if (VERB) {
					String Mess = "QId: " + e.getQueryId() + ", DocID: " + e.getDocId() + ", Rank: " + e.getRank()
							+ ", Score: " + e.getScore();
					System.out.println(Mess);
					if (maxLMess < Mess.length())
						maxLMess = Mess.length();
				}
			}
			if (VERB) {
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
			System.out.println("Ranks Aggregation successful performed.\nThe results are in the file: "
					+ outputFile.getAbsolutePath());
		}

	}

	private static List<OutputEntry> startFaginsAlgorithm(HashMap<Integer, OutputEntry> titlehList,
			HashMap<Integer, OutputEntry> texthList, List<OutputEntry> titleList, List<OutputEntry> textList, Integer k,
			Integer ratio) {
		HashMap<Integer, OutputEntry> SeenList = new HashMap<>();
		LinkedList<OutputEntry> resultSet = new LinkedList<>();
		//HashMap<Integer, OutputEntry> notmatched = new HashMap<>();

		int minl = Math.min(titleList.size(), textList.size());
		int counter = 0;
		
		for (int i = 0; i < minl; i++) {
			if (counter == k) {
				break;
			}
			
			OutputEntry txt = textList.get(i);

			if (!SeenList.containsKey(txt.getDocId())) {
				SeenList.put(txt.getDocId(), txt);
			} else {
				SeenList.remove(txt.getDocId());
				OutputEntry loctit = titlehList.get(txt.getDocId());
				
				double locscore = ((loctit.getScore() * ratio) + txt.getScore() )/2;
				txt.setScore(locscore);
				resultSet.add(txt);
				
				counter++;
			}

			OutputEntry tit = titleList.get(i);

			if (!SeenList.containsKey(tit.getDocId())) {
				SeenList.put(tit.getDocId(), tit);
			} else {
				SeenList.remove(tit.getDocId());
				OutputEntry loctxt = texthList.get(tit.getDocId());
				
				double locscore = loctxt.getScore() + (tit.getScore() * ratio);
				locscore /= 2;
				tit.setScore(locscore);
				
				resultSet.add(tit);
				counter++;
			}
		}

		for (Integer id : SeenList.keySet()) {
			OutputEntry txt = texthList.get(id);
			OutputEntry tit = titlehList.get(id);
			
			
			int qId = (tit == null)? txt.getQueryId() : tit.getQueryId();
			int dId = (tit == null)? txt.getDocId() : tit.getDocId();
			double titScore = (tit == null)? 0.0 : tit.getScore();
			double txtScore = (txt == null)? 0.0 : txt.getScore();
			
			double score = ((titScore * ratio) + txtScore)/2;
			OutputEntry res = new OutputEntry(qId, dId, 0, score);
			resultSet.add(res);
		}
		
		return getTopK1(k, resultSet);
	}

	public static List<LabeledOutputEntry> startFaginsAlgorithm(List<LabeledOutputEntry> titleList,
			List<LabeledOutputEntry> textList, int k, int ratio) throws Exception {
		List<LabeledOutputEntry> S = new LinkedList<>(); // Data items of which
															// lists has already
															// been seen by the
															// algorithm
		List<PosList> Spos = new LinkedList<>();

		if (VVERB) {
			System.out.println("\nCalculating the firsts K results:");
		}

		// access lists (simulating parallelism) with sorted access
		int cont = 0;
		int lce = -1;
		if (titleList.size() > textList.size()) {
			for (int i = 0; i < textList.size(); i++) {
				if (cont == k) {
					if (VVERB)
						System.out.println("\n K euqals objects founded!");
					break;
				} else {

					lce = ListContains(S, textList.get(i));
					if (lce != -1) {
						Spos.add(new PosList(i, "txt", lce));
						cont++;
						if (VVERB)
							System.out.println("\n Seen list CONTAINS:" + textList.get(i).toString());
					} else {
						S.add(textList.get(i));
						if (VVERB)
							System.out.println("\n Seen list  NOT contains:" + textList.get(i).toString());
					}

					lce = ListContains(S, titleList.get(i));
					if (lce != -1) {
						Spos.add(new PosList(i, "tit", lce));
						cont++;
						if (VVERB)
							System.out.println("\n Seen list CONTAINS:" + titleList.get(i).toString());
					} else {
						S.add(titleList.get(i));
						if (VVERB)
							System.out.println("\n Seen list  NOT contains:" + titleList.get(i).toString());
					}
				}
			}
			if (cont != k) { // in this case i have to check remaining elements
								// in titleList
				for (int i = textList.size() - 1; i < titleList.size(); i++) {
					if (cont == k) {
						if (VVERB)
							System.out.println("\n K euqals objects founded!");
						break;
					} else {
						lce = ListContains(S, titleList.get(i));
						if (lce != -1) {
							Spos.add(new PosList(i, "tit", lce));
							cont++;
							if (VVERB)
								System.out.println("\n Seen list CONTAINS:" + titleList.get(i).toString());
						} else {
							S.add(titleList.get(i));
							if (VVERB)
								System.out.println("\n Seen list  NOT contains:" + titleList.get(i).toString());
						}
					}
				}
			}
		} else if (titleList.size() < textList.size()) {
			for (int i = 0; i < titleList.size(); i++) {
				if (cont == k) {
					break;
				} else {
					lce = ListContains(S, titleList.get(i));
					if (lce != -1) {
						Spos.add(new PosList(i, "tit", lce));
						cont++;
						if (VVERB)
							System.out.println("\n Seen list CONTAINS:" + titleList.get(i).toString());
					} else {
						S.add(titleList.get(i));
						if (VVERB)
							System.out.println("\n Seen list  NOT contains:" + titleList.get(i).toString());
					}

					lce = ListContains(S, textList.get(i));
					if (lce != -1) {
						Spos.add(new PosList(i, "txt", lce));
						cont++;
						if (VVERB)
							System.out.println("\n Seen list CONTAINS:" + textList.get(i).toString());
					} else {
						S.add(textList.get(i));
						if (VVERB)
							System.out.println("\n Seen list  NOT contains:" + textList.get(i).toString());
					}
				}
			}
			if (cont != k) { // in this case i have to check remaining elements
								// in textList
				for (int i = titleList.size() - 1; i < textList.size(); i++) {
					if (cont == k) {
						if (VVERB)
							System.out.println("\n K euqals objects founded!");
						break;
					} else {
						lce = ListContains(S, textList.get(i));
						if (lce != -1) {
							Spos.add(new PosList(i, "txt", lce));
							cont++;
							if (VVERB)
								System.out.println("\n Seen list CONTAINS:" + textList.get(i).toString());
						} else {
							S.add(textList.get(i));
							if (VVERB)
								System.out.println("\n Seen list  NOT contains:" + textList.get(i).toString());
						}

					}
				}
			}
		} else { // lists are of the same size <-- this part is called every
					// time (text list and title list are of the same size)
			for (int i = 0; i < titleList.size(); i++) {
				if (cont == k) {
					break;
				} else {
					lce = ListContains(S, titleList.get(i));
					if (lce != -1) {
						Spos.add(new PosList(i, "tit", lce));
						cont++;
						if (VVERB)
							System.out.println("\n List CONTAINS:" + titleList.get(i).toString());
					} else {
						S.add(titleList.get(i));
						if (VVERB)
							System.out.println("\n List  NOT contains:" + titleList.get(i).toString());
					}

					lce = ListContains(S, textList.get(i));
					if (lce != -1) {
						Spos.add(new PosList(i, "txt", lce));
						cont++;
						if (VVERB)
							System.out.println("\n List CONTAINS:" + textList.get(i).toString());
					} else {
						S.add(textList.get(i));
						if (VVERB)
							System.out.println("\n List  NOT contains:" + textList.get(i).toString());
					}

				}
			}
		}

		/*
		 * if (VVERB) { System.out.println("\n Titles list size:" +
		 * titleList.size()); System.out.println("\n Texts list size:" +
		 * textList.size()); System.out.println("\n Seen list S size:" +
		 * S.size()); System.out.println("\n Seen list Spos size:" +
		 * Spos.size()); }
		 */
		// now for each element in S (top-k candidates) i compute the score
		// according to aggregation function
		// in order to do so, random access is performed for all data items such
		// that the score can be computed.

		return calculateResultSet(S, Spos, titleList, textList, ratio, k);

	}

	/**
	 * Compute the score for each element in s according to ratio and return the
	 * top k elements
	 * 
	 * @param s
	 *            list of Entries ( labeledOutputEntriey )
	 * @param spos
	 *            the list take care of entries' index in different lists
	 * @param textList
	 *            list of text entries
	 * @param titleList
	 *            list of title entries
	 * @param ratio
	 *            the ratio that is applied to calculate relative score
	 * @param k
	 *            the size of the result set R
	 * @return list of labeledOutputEntryies : the result set R ( of size k )
	 * @throws Exception
	 */
	private static List<LabeledOutputEntry> calculateResultSet(List<LabeledOutputEntry> s, List<PosList> spos,
			List<LabeledOutputEntry> titleList, List<LabeledOutputEntry> textList, int ratio, int k) throws Exception {
		List<LabeledOutputEntry> R = new LinkedList<>();
		LabeledOutputEntry locentry;
		double locscore;
		for (int i = 0; i < s.size(); i++) {
			locentry = s.get(i);
			locscore = 0;
			PosList pos = inSposSeenEntry(i, spos);
			if (pos != null) {
				if (pos.getListID() == "txt") {
					locscore = textList.get(pos.getPosition()).getScore();
					if (locentry.getLabel() == "tit") {
						locscore += (locentry.getScore() * ratio);
						locscore /= 2;
						R.add(new LabeledOutputEntry(locentry.getQueryId(), locentry.getDocId(), 0, locscore, "rs"));
					} else {
						// !! DANGEROUS ERROR !!
						throw new Exception(
								"DANGEROUS ERROR during the calculation of local score - line 323 - plees fix code!");
					}
				} else if (pos.getListID() == "tit") {
					locscore = titleList.get(pos.getPosition()).getScore() * ratio;
					if (locentry.getLabel() == "txt") {
						locscore += locentry.getScore();
						locscore /= 2;
						R.add(new LabeledOutputEntry(locentry.getQueryId(), locentry.getDocId(), 0, locscore, "rs"));
					} else {
						// !! DANGEROUS ERROR !!
						throw new Exception(
								"DANGEROUS ERROR during the calculation of local score - line 323 - plees fix code!");
					}
				}
			} else {
				R.add(new LabeledOutputEntry(locentry.getQueryId(), locentry.getDocId(), 0, locentry.getScore(), "rs"));
			}
		}

		return getTopK(k, R);
	}

	private static PosList inSposSeenEntry(int a, List<PosList> spos) {
		for (int i = 0; i < spos.size(); i++) {
			if (spos.get(i).getSeenPosition() == a) {
				return spos.get(i);
			}
		}
		return null;
	}

	/**
	 * Check is list s contains the Output entry loe but with different label
	 * 
	 * @param s
	 *            list of lists of Entries
	 * @param loe
	 *            the entry we what to check
	 * @return integer: the relative index or -1 if it is not found
	 */
	private static int ListContains(List<LabeledOutputEntry> s, LabeledOutputEntry loe) {
		if (s.size() <= 0) {
			return -1;
		} else {
			for (int i = 0; i < s.size(); i++) {
				LabeledOutputEntry loc = s.get(i);
				if (loc.getDocId() == loe.getDocId() && loc.getQueryId() == loe.getQueryId()
						&& loc.getLabel() != loe.getLabel()) {
					return i;
				}
			}
		}
		return -1;
	}

	protected static List<OutputEntry> getTopK1(int K, List<OutputEntry> R) {
		List<OutputEntry> res = new LinkedList<>();

		PriorityQueue<OutputEntry> pq = new PriorityQueue<>(new Comparator<OutputEntry>() {

			@Override
			public int compare(OutputEntry arg0, OutputEntry arg1) {
				if (arg0.getScore() == arg1.getScore()) {
					return 0;
				}
				return (arg0.getScore() - arg1.getScore() < 0) ? 1 : -1;
			}
		});

		for (OutputEntry loe : R) {
			if (loe != null) {
				pq.add(loe);
			}
		}

		for (int i = 1; i <= K; i++) {
			OutputEntry l = pq.poll();
			if (l != null) {
				l.setRank(i);
				res.add(l);
			}
		}
		return res;
	}

	protected static List<LabeledOutputEntry> getTopK(int K, List<LabeledOutputEntry> R) {
		List<LabeledOutputEntry> res = new LinkedList<>();
		/*
		 * List<Integer> resseen = new LinkedList<>(); int lmax = 0; int rank =
		 * 1; LabeledOutputEntry loc;
		 * 
		 * for (int i = 0; i < R.size(); i++) { loc = R.get(i); for (int j = 0;
		 * j < R.size(); j++) { if (j != i) { if (R.get(j).getScore() >
		 * loc.getScore() && !resseen.contains(j)) { loc = R.get(j);
		 * resseen.add(j); lmax = j; } } } if (res.size() == K) { break; } else
		 * { if (!res.contains(R.get(lmax))) { R.get(lmax).setRank(rank);
		 * rank++; res.add(R.get(lmax)); } } } if (res.size() < K) {
		 * System.out.println();
		 * System.out.println("ERROR DURING TOP K EVALUATING:");
		 * System.out.println("R:" + R.toString()); System.out.println("R.size:"
		 * + R.size()); System.out.println("K:" + K); System.out.println();
		 * System.out.println("RES:" + res.toString()); return res;
		 * 
		 * } else { return res; }
		 */

		PriorityQueue<LabeledOutputEntry> pq = new PriorityQueue<>(new Comparator<LabeledOutputEntry>() {

			@Override
			public int compare(LabeledOutputEntry o1, LabeledOutputEntry o2) {
				return (int) (o2.getScore() - o1.getScore());
			}
		});

		for (LabeledOutputEntry loe : R) {
			pq.add(loe);
		}

		for (int i = 1; i <= K; i++) {
			LabeledOutputEntry l = pq.poll();
			l.setRank(i);
			res.add(l);
		}
		return res;
	}

	protected static OutputEntry getOutputEntry(List<OutputEntry> l, int docId, int position) {

		// Linear scan starting from the actual position
		for (int i = position; i < l.size(); i++) {
			if (l.get(i).getDocId() == docId) {
				return l.get(i);
			}
		}

		return null;
	}

	static class PosList {
		protected int position;
		protected String listid;
		protected int seenposition;

		public PosList(int pos, String label, int spos) {
			this.position = pos;
			this.listid = label;
			this.seenposition = spos;
		}

		public int getPosition() {
			return this.position;
		}

		public void setPosition(int pos) {
			this.position = pos;
		}

		public String getListID() {
			return this.listid;
		}

		public void setListID(String id) {
			this.listid = id;
		}

		public int getSeenPosition() {
			return this.seenposition;
		}

		public void setSeenPosition(int pos) {
			this.seenposition = pos;
		}

	}

}
