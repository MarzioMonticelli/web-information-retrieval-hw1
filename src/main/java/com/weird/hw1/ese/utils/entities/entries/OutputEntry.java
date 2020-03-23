package com.weird.hw1.ese.utils.entities.entries;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OutputEntry extends BasicEntry implements Comparable<OutputEntry>, Iterable<String> {

	protected int Rank;
	protected double Score;

	public OutputEntry(int queryId, int documentId, int rank, double score) {
		super(queryId, documentId);
		this.Rank = rank;
		this.Score = score;
	}

	public int getRank() {
		return Rank;
	}

	public void setRank(int rank) {
		Rank = rank;
	}

	public double getScore() {
		return Score;
	}

	public void setScore(double score) {
		Score = score;
	}

	@Override
	public String toString() {
		return "OutputEntry [Rank=" + Rank + ", Score=" + Score + ", QueryId=" + QueryId + ", DocId=" + DocId + "]";
	}

	@Override
	public int compareTo(OutputEntry o) {
		if (o == null) {
			return 1;
		}
		if (o.Rank != this.Rank) {			
			return o.Rank - this.Rank;
		} else {
			return (int) Math.ceil(o.getScore() - this.getScore());
		}
	}

	@Override
	public Iterator<String> iterator() {
		List<String> data = new ArrayList<>();
		
		data.add("" + this.getQueryId());
		data.add("" + this.getDocId());
		data.add("" + this.getRank());
		data.add("" + this.getScore());
		
		return data.iterator();
	}

}
