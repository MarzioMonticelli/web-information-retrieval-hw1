package com.weird.hw1.plot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import com.weird.hw1.ese.nMDCG;

public class BarPlotHWnMDCGResults {

	protected static void usage() {
		System.out.println("command -K ks... -f (FILE, GROUP, SCORER)+... -g GROUND_TRUTH_FILE -o OUTPUT_FILE");
	}

	protected static List<Integer> Ks = new LinkedList<>();
	protected static List<File> Files = new LinkedList<>();
	protected static List<String> Groups = new LinkedList<>();
	protected static List<String> Scorers = new LinkedList<>();
	protected static File ChartFile = null;
	protected static File GT = null;

	public static void main(String[] args) {
		List<String> params = new ArrayList<>(args.length);
		for (String arg : args) {
			params.add(arg);
		}

		for (int index = 0; index < params.size();) {
			String par = params.get(index);
			if (!par.startsWith("-")) {
				System.out.println("Skipping " + par);
				index++;
				continue;
			}

			String nPar = params.get(++index);

			// grub Ks
			if (par.equals("-K")) {
				int ij = index;
				while (!nPar.startsWith("-") && ij < params.size()) {
					try {
						int k = Integer.parseInt(nPar);
						if (k <= 0) {
							throw new NumberFormatException();
						}
						Ks.add(k);
						nPar = params.get(++ij);
					} catch (NumberFormatException e) {
						System.out.println("Invalid K value: " + nPar);
						System.exit(-1);
					}
				}
				index = ij;
			}

			// grub files and titles
			if (par.equals("-f")) {
				int ij = index;
				while (!nPar.startsWith("-") && ij < params.size()) {
					// grub file
					File checkFile = new File(nPar);
					if (!checkFile.exists()) {
						System.out.println("File " + nPar + " does not exist");
						System.exit(-1);
					}

					Files.add(checkFile);

					// grub title
					nPar = params.get(++ij);

					if (nPar.startsWith("-")) {
						System.out.println("Malformed arguments. Missing one title. Exiting...");
						System.exit(-1);
					}

					Groups.add(nPar);
					nPar = params.get(++ij);

					if (nPar.startsWith("-")) {
						System.out.println("Malformed arguments. Missing one title. Exiting...");
						System.exit(-1);
					}

					Scorers.add(nPar);
					nPar = params.get(++ij);
				}

				index = ij;
			}

			// grub ground truth file
			if (par.equals("-g")) {
				GT = new File(nPar);
				if (!GT.exists()) {
					System.out.println("File " + nPar + " does not exist");
					System.exit(-1);
				}
				index++;
			}

			// grub output file
			if (par.equals("-o")) {
				ChartFile = new File(nPar);
				index++;
			}

		}

		System.out.println("Data parsed: ");
		System.out.println("Ks: " + Ks);
		System.out.println("Files: " + Files);
		System.out.println("Groups: " + Groups);
		System.out.println("Scorers: " + Scorers);
		System.out.println("GT: " + GT);

		if (GT == null) {
			System.out.println("Ground Truth file undefined. Exiting.");
			System.exit(-1);
		} else if (Files.isEmpty()) {
			System.out.println("No files passed. Exiting.");
			System.exit(-1);
		} else if (Groups.isEmpty()) {
			System.out.println("No Groups passed. Exiting.");
			System.exit(-1);
		} else if (Scorers.isEmpty()) {
			System.out.println("No Groups passed. Exiting.");
			System.exit(-1);
		} else if (Ks.isEmpty()) {
			System.out.println("No Ks passed. Exiting.");
			System.exit(-1);
		} else if (ChartFile == null) {
			System.out.println("No Chart File passed. Exiting.");
			System.exit(-1);
		}

		if (Files.size() != Groups.size() || Files.size() != Scorers.size()) {
			System.out.println("Different sizes: " + Files.size() + " Files, " + Groups.size() + " Groups, "
					+ Scorers.size() + " Scorer names.");
			System.exit(-1);
		}

		// calculate all the results
		HashMap<FGS, Map<Integer, Double>> AllResults = new HashMap<>();
		for (int index = 0; index < Files.size(); index++) {
			File evaluateFile = Files.get(index);

			// System.out.println("FGS: " + mainFGS.toString());
			System.out.println(Groups.get(index) + " - " + Scorers.get(index));
			for (Integer K : Ks) {
				FGS mainFGS = new FGS(Files.get(index), Groups.get(index), Scorers.get(index), K);
				Map<Integer, Double> Results = nMDCG.calculateAverage_nMDCG(GT, evaluateFile, K);
				// System.out.println("FGS: " + mainFGS.toString() + " K: " + K
				// + " Score: " + Results.get(K));
				AllResults.put(mainFGS, Results);
			}
			System.out.println("");
		}

		// System.out.println("All Results: " + AllResults);

		// collect all the groups
		List<String> groups = new LinkedList<>();
		for (FGS fgs : AllResults.keySet()) {
			if (!groups.contains(fgs.getGroup())) {
				groups.add(fgs.getGroup());
			}
		}

		DefaultCategoryDataset dataset = new DefaultCategoryDataset();

		// Order the groups
		Collections.sort(groups);

		for (String group : groups) {
			// collect all the scorers for each groups
			List<String> scorers = new LinkedList<>();
			for (FGS fgs : AllResults.keySet()) {
				if (fgs.getGroup().equals(group) && !scorers.contains(fgs.getScorer())) {
					scorers.add(fgs.getScorer());
				}
			}

			// Order the scorers
			Collections.sort(scorers);

			// add to the Histogram Dataset
			for (String scorer : scorers) {
				// System.out.println("Scorer: " + scorer);
				for (FGS fgs : AllResults.keySet()) {
					if (fgs.getGroup().equals(group) && fgs.getScorer().equals(scorer)) {

						// key K, value score
						Map<Integer, Double> r = AllResults.get(fgs);
						for (Integer k : r.keySet()) {
							// System.out.println("Inserting: " + group + "-"
							// +scorer + " " + k + ": " + r.get(k));
							dataset.addValue(r.get(k), group + "-" + scorer, k);
						}
						System.out.println("");
					}
				}
			}

			JFreeChart barChart = ChartFactory.createBarChart("nMDCG", "K", "Score", dataset, PlotOrientation.VERTICAL,
					true, true, false);

			int width = 640; /* Width of the image */
			int height = 480; /* Height of the image */
			try {
				ChartUtilities.saveChartAsJPEG(ChartFile, barChart, width, height);
			} catch (IOException e) {
			}

		}
	}

	static class FGS {
		File file;
		String Group, Scorer;
		Integer K;

		public FGS(File f, String group, String scorer, Integer k) {
			this.file = f;
			this.Group = group;
			this.Scorer = scorer;
			this.K = k;
		}

		public File getFile() {
			return file;
		}

		public String getGroup() {
			return Group;
		}

		public String getScorer() {
			return Scorer;
		}

		public Integer getK() {
			return K;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((Group == null) ? 0 : Group.hashCode());
			result = prime * result + ((K == null) ? 0 : K.hashCode());
			result = prime * result + ((Scorer == null) ? 0 : Scorer.hashCode());
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			FGS other = (FGS) obj;
			if (Group == null) {
				if (other.Group != null)
					return false;
			} else if (!Group.equals(other.Group))
				return false;
			if (K == null) {
				if (other.K != null)
					return false;
			} else if (!K.equals(other.K))
				return false;
			if (Scorer == null) {
				if (other.Scorer != null)
					return false;
			} else if (!Scorer.equals(other.Scorer))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "FGS [file=" + file + ", Group=" + Group + ", Scorer=" + Scorer + ", K=" + K + "]";
		}

	}

}
