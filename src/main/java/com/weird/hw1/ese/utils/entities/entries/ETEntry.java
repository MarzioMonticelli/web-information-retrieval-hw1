package com.weird.hw1.ese.utils.entities.entries;

public class ETEntry extends QueryEntry {
	protected double Score;

	public ETEntry(int queryId, double score) {
		super(queryId);
		this.Score = score;
	}

	public double getScore() {
		return Score;
	}

	public void setScore(double score) {
		Score = score;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		long temp;
		temp = Double.doubleToLongBits(Score);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		ETEntry other = (ETEntry) obj;
		if (Double.doubleToLongBits(Score) != Double.doubleToLongBits(other.Score))
			return false;
		return true;
	}

}
