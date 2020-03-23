package com.weird.hw1.ese.utils.entities.results;

public class ResultMDCG extends Result {

	protected int K;
	
	public ResultMDCG(int queryID, double score, int k) {
		super(queryID, score);
		K = k;
	}

	public int getK() {
		return K;
	}

	public void setK(int k) {
		K = k;
	}

	@Override
	public String toString() {
		return "ResultMDCG [K=" + K + ", QueryID=" + QueryID + ", Score=" + Score + "]";
	}
}
