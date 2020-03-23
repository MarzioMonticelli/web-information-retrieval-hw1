package com.weird.hw1.ese.utils.structures;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HashTableListGen<K, V> {
	
	protected Map<K, List<V>> map = new HashMap<>();
	
	public HashTableListGen() {
	}
	
	public void insert(K key, V value) {
		if (map.containsKey(key)) {
			List<V> list = map.get(key);
			list.add(value);
		} else {
			List<V> list = new LinkedList<>();
			list.add(value);
			map.put(key, list);
		}
	}
	
	public List<V> getEntriesFor(K key) {
		List<V> list = this.map.get(key);
		return (list != null)? list : new LinkedList<>();
	}
	
	public int size() {
		return this.map.size();
	}
	
	public void fillFromFile(File file) {
		throw new UnsupportedOperationException();
	}
	
	public Set<K> keySet() {
		return map.keySet();
	}

	@Override
	public String toString() {
		return "HashTableListGen [map=" + map + "]";
	}
	
	
}
