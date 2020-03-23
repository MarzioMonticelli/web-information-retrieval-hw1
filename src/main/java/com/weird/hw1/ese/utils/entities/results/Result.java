package com.weird.hw1.ese.utils.entities.results;

public abstract class Result {
	protected int QueryID;
	protected double Score;

	public Result(int queryID, double score) {
		this.QueryID = queryID;
		this.Score = score;
	}

	public int getQueryID() {
		return QueryID;
	}

	public void setQueryID(int queryID) {
		QueryID = queryID;
	}

	public double getScore() {
		return Score;
	}

	public void setScore(int score) {
		Score = score;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + QueryID;
		long temp;
		temp = Double.doubleToLongBits(Score);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Result other = (Result) obj;
		if (QueryID != other.QueryID)
			return false;
		if (Double.doubleToLongBits(Score) != Double.doubleToLongBits(other.Score))
			return false;
		return true;
	}

	
}
