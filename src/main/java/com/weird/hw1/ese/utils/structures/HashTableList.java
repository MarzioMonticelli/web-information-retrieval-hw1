package com.weird.hw1.ese.utils.structures;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.weird.hw1.ese.utils.entities.entries.BasicEntry;

public abstract class HashTableList {

	protected Map<Integer, List<BasicEntry>> map;

	public HashTableList() {
		this.map = new HashMap<>();
	}

	public void insert(BasicEntry basicEntry) {
		if (this.map.containsKey(basicEntry.getQueryId())) {
			List<BasicEntry> basicEntryList = map.get(basicEntry.getQueryId());
			basicEntryList.add(basicEntry);
		} else {
			List<BasicEntry> basicEntryList = new LinkedList<>();
			basicEntryList.add(basicEntry);
			this.map.put(basicEntry.getQueryId(), basicEntryList);
		}
	}

	public List<BasicEntry> getEntriesFor(int queryId) {
		List<BasicEntry> list = this.map.get(queryId);

		return (list != null) ? list : new LinkedList<>();
	}

	public void printStatus() {
		try {
			File f = new File("./out");
			FileWriter fw = new FileWriter(f);

			for (Integer i : map.keySet()) {
				for (BasicEntry be : map.get(i)) {
					String M = i + ": " + be + "\n";
					fw.write(M);
				}
			}
			fw.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public int size() {
		return this.map.size();
	}

	public void fillFromFile(File file) {
		throw new UnsupportedOperationException();
	}

	public Integer getLastEntryId() {
		Set<Integer> s = map.keySet();
		Integer max = 0;
		for (Integer i : s) {
			if (i > max) {
				max = i;
			}
		}

		return max;
	}

	public Set<Integer> keySet() {
		return this.map.keySet();
	}
}
