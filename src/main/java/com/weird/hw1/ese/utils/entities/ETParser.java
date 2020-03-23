package com.weird.hw1.ese.utils.entities;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.weird.hw1.ese.utils.entities.entries.OutputEntry;
import com.weird.hw1.ese.utils.structures.HTLEvaluate;

public class ETParser {
	public static HTLEvaluate parseFile(File file) throws IOException {
		HTLEvaluate tList = new HTLEvaluate();
		
		CSVParser parser = new CSVParser(new FileReader(file), CSVFormat.TDF);
		for (CSVRecord record : parser.getRecords()) {
			try {
				int queryId = Integer.parseInt(record.get(0));
				int docId = Integer.parseInt(record.get(1));
				int rank = Integer.parseInt(record.get(2));
				double score = Double.parseDouble(record.get(3));
				tList.insert(new OutputEntry(queryId, docId, rank, score));
			} catch (NumberFormatException e) {
				continue;
			}
		}
		
		parser.close();
		
		return tList;
	}
}
