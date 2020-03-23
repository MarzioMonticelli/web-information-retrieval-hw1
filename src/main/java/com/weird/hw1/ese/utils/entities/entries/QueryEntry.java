package com.weird.hw1.ese.utils.entities.entries;

public abstract class QueryEntry {
	protected int QueryId;
	
	public QueryEntry(int queryId) {
		this.QueryId = queryId;
	}

	public int getQueryId() {
		return QueryId;
	}

	public void setQueryId(int queryId) {
		QueryId = queryId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + QueryId;
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
		QueryEntry other = (QueryEntry) obj;
		if (QueryId != other.QueryId)
			return false;
		return true;
	}
	
}
