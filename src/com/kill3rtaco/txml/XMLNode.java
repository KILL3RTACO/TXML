package com.kill3rtaco.txml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an XML node. {@link XMLNode}s hold attribute values as well as other {@link XMLNode}s.
 * @author KILL3RTACO
 *
 */
public class XMLNode {
	
	private String				_name;
	private Map<String, String>	_attributes;
	private List<XMLNode>		_nodes;
	
	/**
	 * Creates a new {@link XMLNode}
	 * @param name The name of the node. For instance, in '&lt;cookies&gt;', 'cookies' is the name
	 */
	public XMLNode(String name) {
		this(name, new HashMap<String, String>());
	}
	
	/**
	 * Creates a new {@link XMLNode}. 
	 * @param name The name of the node. For instance, in '&lt;cookies&gt;', 'cookies' is the name
	 * @param attributes A map of attributes for the node, cannot be null
	 * @throws IllegalArgumentException If the given variable for {@code attributes} is null
	 */
	public XMLNode(String name, Map<String, String> attributes) {
		this(name, attributes, new ArrayList<XMLNode>());
	}
	
	/**
	 * Creates a new {@link XMLNode}. 
	 * @param name The name of the node. For instance, in '&lt;cookies&gt;', 'cookies' is the name
	 * @param attributes A map of attributes for the node, cannot be null
	 * @param nodes A list of nodes contained within this node
	 * @throws IllegalArgumentException If the given variable for {@code attributes} is null
	 */
	public XMLNode(String name, Map<String, String> attributes, List<XMLNode> nodes) {
		if(attributes == null) {
			throw new IllegalArgumentException("attributes variable cannot be null");
		}
		_name = name;
		_attributes = attributes;
		_nodes = nodes;
	}
	
}
