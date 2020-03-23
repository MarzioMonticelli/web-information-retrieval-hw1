package com.weird.hw1.ese.utils.structures;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.weird.hw1.ese.utils.entities.entries.GroundTruthEntry;

public class HTLGroundTruth extends HashTableList {

	@Override
	public void fillFromFile(File knowledgeFile) {
		try {
		// Initialize Parsers
		CSVParser knowledgeParser = new CSVParser(new FileReader(knowledgeFile), CSVFormat.TDF);

		// extracts all the records
		List<CSVRecord> knowledgeRecords = knowledgeParser.getRecords();

		for (CSVRecord knowledgeRecord : knowledgeRecords) {
			try {
				Iterator<String> kRIterator = knowledgeRecord.iterator();
				int kQID = Integer.parseInt(kRIterator.next());
				int kDID = Integer.parseInt(kRIterator.next());

				GroundTruthEntry gEntry = new GroundTruthEntry(kQID, kDID);

				// save entry in data structure
				this.insert(gEntry);
			} catch (NumberFormatException e) {
				// used to skip the Header
				// (it is not a number, so it will generate an Number format
				// exception)
				continue;
			}
		}

		knowledgeParser.close();
		} catch (IOException e) {
		}
	}
}
