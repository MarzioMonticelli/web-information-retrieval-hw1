package com.weird.hw1.ese.utils.entities.entries;

public abstract class BasicEntry extends QueryEntry {
	protected int DocId;

	public BasicEntry(int queryId, int documentId) {
		super(queryId);
		this.DocId = documentId;
	}

	public int getQueryId() {
		return QueryId;
	}

	public int getDocId() {
		return DocId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + DocId;
		result = prime * result + super.QueryId;
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
		BasicEntry other = (BasicEntry) obj;
		if (DocId != other.DocId)
			return false;
		if (QueryId != other.QueryId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "BasicEntry [QueryId=" + QueryId + ", DocId=" + DocId + "]";
	}

}
