package com.weird.hw1.ese.utils.entities.entries;

public class LabeledOutputEntry extends OutputEntry implements Comparable<OutputEntry>, Iterable<String> {
	protected String label;

	public LabeledOutputEntry(int queryId, int documentId, int rank, double score, String label) {
		super(queryId, documentId, rank, score);
		// TODO Auto-generated constructor stub
		this.label = label;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return "LabeledOutputEntry [Rank=" + Rank + ", Score=" + Score + ", QueryId=" + QueryId + ", DocId=" + DocId
				+ " , Label=" + label + "]";
	}

}
