package com.weird.hw1.ese.utils.structures;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import com.weird.hw1.ese.utils.entities.entries.OutputEntry;

public class HTLEvaluate extends HashTableList {
	
	@Override
	public void fillFromFile(File evaluateFile) {
		try {
			// Initialize Parsers
			CSVParser evaluateParser = new CSVParser(new FileReader(evaluateFile), CSVFormat.TDF);

			// extracts all the records
			List<CSVRecord> evaluateRecords = evaluateParser.getRecords();

			for (CSVRecord evaluateRecord : evaluateRecords) {

				Iterator<String> eRIterator = evaluateRecord.iterator();
				try {
					int eQID = Integer.parseInt(eRIterator.next());
					int eDID = Integer.parseInt(eRIterator.next());
					int eRNK = Integer.parseInt(eRIterator.next());
					double eSCR = Double.parseDouble(eRIterator.next());

					OutputEntry oEntry = new OutputEntry(eQID, eDID, eRNK, eSCR);

					// save entry in data structure
					this.insert(oEntry);
				} catch (NumberFormatException e) {
					// used to skip the Header
					// (it is not a number, so it will generate an Number format
					// exception)
					continue;
				}

			}

			evaluateParser.close();
		} catch (IOException e) {
		}
	}
	
}
