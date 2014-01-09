package com.kill3rtaco.txml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A simple wrapper for a Map<String, String>.
 * @author KILL3RTACO
 *
 */
public class AttributeMap {
	
	private List<String>	_keys, _values;
	
	public AttributeMap() {
		_keys = new ArrayList<String>();
		_values = new ArrayList<String>();
	}
	
	public AttributeMap(Map<String, String> map) {
		this();
		for(String k : map.keySet()) {
			put(k, map.get(k));
		}
	}
	
	public void put(String key, String value) {
		if(_keys.contains(key)) {
			_values.set(_keys.indexOf(key), value);
		} else {
			_keys.add(key);
			_values.add(value);
		}
	}
	
	public void putAll(Map<String, String> map) {
		putAll(new ArrayList<String>(map.keySet()), new ArrayList<String>(map.values()));
	}
	
	public void putAll(List<String> keys, List<String> values) {
		if(keys.size() != values.size()) {
			throw new TXMLException("key and value lists are not the same size");
		}
		for(int i = 0; i < keys.size(); i++) {
			put(keys.get(i), values.get(i));
		}
	}
	
	public boolean containsKey(String key) {
		return _keys.contains(key);
	}
	
	public List<String> getKeys() {
		return _keys;
	}
	
	public List<String> getValues() {
		return _values;
	}
	
	public String get(String key) {
		if(containsKey(key)) {
			return _values.get(_keys.indexOf(key));
		}
		return null;
	}
	
}
