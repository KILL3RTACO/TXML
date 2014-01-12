package com.kill3rtaco.txml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an ordered list of key and value pairs
 * @author KILL3RTACO
 *
 */
public class AttributeMap {
	
	private List<String>	_keys, _values;
	
	/**
	 * Create a new AttributeMap with no key and value pairs
	 */
	public AttributeMap() {
		_keys = new ArrayList<String>();
		_values = new ArrayList<String>();
	}
	
	/**
	 * Construct a new AttributeMap based off the given map. The key and value pairs will be added
	 * in the order they are in the map.
	 * @param map A Map of key and value pairs
	 */
	public AttributeMap(Map<String, String> map) {
		this();
		putAll(map);
	}
	
	/**
	 * Add a new key and value pair
	 * @param key The key
	 * @param value The value
	 */
	public void put(String key, String value) {
		if(_keys.contains(key)) {
			_values.set(_keys.indexOf(key), value);
		} else {
			_keys.add(key);
			_values.add(value);
		}
	}
	
	/**
	 * Add all the key and value pairs from the given Map to this AttributeMap
	 * @param map A Map containing key and value pairs to be added to this AttributeMap
	 */
	public void putAll(Map<String, String> map) {
		putAll(new ArrayList<String>(map.keySet()), new ArrayList<String>(map.values()));
	}
	
	/**
	 * Add all the key and value pairs from the given Lists to this AttributeMap
	 * @param keys A List of keys
	 * @param values A List of values
	 */
	public void putAll(List<String> keys, List<String> values) {
		if(keys.size() != values.size()) {
			throw new TXMLException("key and value lists are not the same size");
		}
		for(int i = 0; i < keys.size(); i++) {
			put(keys.get(i), values.get(i));
		}
	}
	
	/**
	 * Test if this AttributeMap contains the given key
	 * @param key The key to test for
	 * @return true of this AttributeMap contains the given key
	 */
	public boolean containsKey(String key) {
		return _keys.contains(key);
	}
	
	/**
	 * Get a list of keys contained in this AttributeMap
	 * @return a list of keys contained in this AttributeMap
	 */
	public List<String> getKeys() {
		return _keys;
	}
	
	/**
	 * Get a list of values contained in this AttributeMap
	 * @return a list of values contained in this AttributeMap
	 */
	public List<String> getValues() {
		return _values;
	}
	
	/**
	 * Return the value to which the specified key is mapped, or null if this AttributeMap does not contain
	 * the given key
	 * @param key The key to get the value of
	 * @return The value of the given key, or null if this AttributeMap does not contain the given key
	 */
	public String get(String key) {
		if(containsKey(key)) {
			return _values.get(_keys.indexOf(key));
		}
		return null;
	}
	
	/**
	 * Sort the keys alphabetically and adjust values to match
	 * @return this
	 */
	public AttributeMap sortAlphabetically() {
		Map<String, String> m = toMap();
		Collections.sort(_keys);
		_values.clear();
		for(String s : _keys) {
			_values.add(m.get(s));
		}
		return this;
	}
	
	/**
	 * Convert this AttributeMap to a Map
	 * @return A Map containing the key and value pairs contained within this AttributeMap
	 */
	public Map<String, String> toMap() {
		Map<String, String> map = new HashMap<String, String>();
		for(String s : getKeys()) {
			map.put(s, get(s));
		}
		return map;
	}
	
}
