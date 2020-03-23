package com.weird.hw1.plot;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.weird.hw1.ese.nMDCG;

public class CurvePlotternMDCG {
	private static final List<Integer> K = new LinkedList<>();
	private static File EvaluatedFile = null, KnowledgeFile = null, OutputFile = null;
	private static String Title = null;
	private static Color CurveColor = Color.BLUE;

	private static void usage() {
		System.out.println("command -koghe");
		System.out.println("\t-k: a list of values for K");
		System.out.println("\t-g: ground truth file");
		System.out.println("\t-e: evaluated values file");
		System.out.println("\t-o: output file");
		System.out.println("\t-T: image Title");
		System.out.println("\t-c: curve color: blue, red, green, black, gray, yellow");
		System.out.println("\t-h: print this help");
	}

	public static void main(String[] args) {
		// Input parsing
		List<String> params = new LinkedList<>();

		for (String arg : args) {
			params.add(arg);
		}

		for (int i = 0; i < params.size(); i++) {
			if (params.get(i).equals("-h")) {
				usage();
				System.exit(0);
			}
			if (i + 1 < params.size()) {
				if (params.get(i).equals("-k") || params.get(i).equals("-K")) {
					for (int j = i + 1; j < params.size() && !params.get(j).startsWith("-"); j++, i++) {
						try {
							K.add(Integer.parseInt(params.get(j)));
						} catch (NumberFormatException e) {
							break;
						}
					}
				} else if (params.get(i).equals("-g")) {
					KnowledgeFile = new File(params.get(i + 1));
					if (!KnowledgeFile.exists()) {
						System.out.println("the file " + KnowledgeFile.getAbsolutePath() + " does NOT exist");
						System.exit(-1);
					}
					i++;
				} else if (params.get(i).equals("-e")) {
					EvaluatedFile = new File(params.get(i + 1));
					if (!EvaluatedFile.exists()) {
						System.out.println("the file " + EvaluatedFile.getAbsolutePath() + " does NOT exist");
						System.exit(-1);
					}
					i++;
				} else if (params.get(i).equals("-o")) {
					OutputFile = new File(params.get(i + 1));
					i++;
				} else if (params.get(i).equals("-T")) {
					Title = params.get(++i);
				} else if (params.get(i).equals("-c")) {
					String c = params.get(++i);
					switch (c) {
					case ("red"):
						CurveColor = Color.RED;
						break;
					case ("black"):
						CurveColor = Color.BLACK;
						break;
					case ("gray"):
						CurveColor = Color.GRAY;
						break;
					case ("green"):
						CurveColor = Color.GREEN;
						break;
					case ("yellow"):
						CurveColor = Color.YELLOW;
						break;
					case ("blue"):
						CurveColor = Color.BLUE;
						break;
					default:
						CurveColor = Color.BLUE;
						System.out.println(
								"Could not find color: " + c + ", using BLUE. Print help for available colors");
						break;
					}

				} else {
					usage();
					System.exit(-1);
				}
			} else {
				usage();
				System.exit(-1);
			}
		}

		// check that all parameters are setted
		if (K.isEmpty()) {
			System.out.println("You have not defined any value for K");
			System.exit(-1);
		} else if (EvaluatedFile == null) {
			System.out.println("You have not defined any value for the evaluated values file");
			System.exit(-1);
		} else if (KnowledgeFile == null) {
			System.out.println("You have not defined any value for the knowledge file");
			System.exit(-1);
		} else if (OutputFile == null) {
			System.out.println("You have not defined any value for the output file");
			System.exit(-1);
		}

		PlotResults();

	}

	private static Map<Integer, Double> calculateForEachK() {
		Map<Integer, Double> Results = new HashMap<>();

		// Order K values
		Collections.sort(K);

		// calculate for each K
		for (Integer k : K) {
			Map<Integer, Double> R = nMDCG.calculateAverage_nMDCG(KnowledgeFile, EvaluatedFile, k);

			// merge results
			for (Integer kv : R.keySet()) {
				Results.put(kv, R.get(kv));
			}
		}

		return Results;
	}

	private static void PlotResults() {
		Map<Integer, Double> Results = calculateForEachK();

		final XYSeries xy = new XYSeries("nMDCG/K", true, false);
		for (Integer key : Results.keySet()) {
			xy.add(key, Results.get(key));
		}

		final XYSeriesCollection dataset = new XYSeriesCollection(xy);
		JFreeChart xyLineChart = ChartFactory.createXYLineChart(Title, "K", "nMDCG", dataset, PlotOrientation.VERTICAL,
				true, true, false);

		XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) xyLineChart.getXYPlot().getRenderer();

		// displays a mark on each point of the dataset in the plot
		renderer.setBaseShapesVisible(true);
		// colors the line
		renderer.setSeriesPaint(0, (CurveColor == null) ? Color.BLUE : CurveColor);

		int width = 640, height = 480;
		try {
			ChartUtilities.saveChartAsJPEG(OutputFile, xyLineChart, width, height);
		} catch (IOException e) {
		} finally {
			System.out.println("Chart Successfully Plotted at " + OutputFile.getAbsolutePath());
		}
	}
}
