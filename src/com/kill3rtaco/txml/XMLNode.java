package com.kill3rtaco.txml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents an XML node. {@link XMLNode}s hold attribute values as well as other {@link XMLNode}s.
 * @author KILL3RTACO
 *
 */
public class XMLNode extends XMLContainer {
	
	private String			_name;
	private AttributeMap	_attributes;
	private boolean			_selfEnding	= false;
	
	/**
	 * Creates a new {@link XMLNode}
	 * @param name The name of the node. For instance, in the XMLNode representing {@code <node bleep="bloop"/>},
	 * the name would be 'node'
	 */
	public XMLNode(String name) {
		this(name, new HashMap<String, String>());
	}
	
	/**
	 * Creates a new {@link XMLNode}. 
	 * @param name The name of the node. For instance, in the XMLNode representing {@code <node bleep="bloop"/>},
	 * the name would be 'node'
	 * @param attributes A map of attributes for the node, cannot be null
	 * @throws IllegalArgumentException If the given variable for {@code attributes} is null
	 */
	public XMLNode(String name, Map<String, String> attributes) {
		this(name, attributes, new ArrayList<XMLNode>());
	}
	
	/**
	 * Creates a new {@link XMLNode}. 
	 * @param name The name of the node. For instance, in the XMLNode representing {@code <node bleep="bloop"/>},
	 * the name would be 'node'
	 * @param attributes A map of attributes for the node, cannot be null
	 * @param nodes A list of nodes contained within this node
	 * @throws IllegalArgumentException If the given variable for {@code attributes} is null
	 */
	public XMLNode(String name, Map<String, String> attributes, List<XMLNode> nodes) {
		super();
		if(attributes == null) {
			throw new IllegalArgumentException("attributes variable cannot be null");
		}
		_name = name;
		_attributes = new AttributeMap(attributes);
		_nodes = nodes;
	}
	
	/**
	 * Get the name of this node. For instance, in the XMLNode representing {@code <node bleep="bloop"/>}, the
	 * name would be 'node'
	 * @return the name of this node.
	 */
	public String getName() {
		return _name;
	}
	
	/**
	 * Get a map of all the attributes for this node.
	 * @return This nodes attributes
	 */
	public AttributeMap getAttributes() {
		return _attributes;
	}
	
	/**
	 * Test whether this node has the given attribute or not.
	 * @param attr The attribute to test for
	 * @return Whether this node has the given attribute or not.
	 */
	public boolean hasAttribute(String attr) {
		return _attributes.containsKey(attr);
	}
	
	/**
	 * Test whether this node has the given attributes or not.
	 * @param attrs The attributes to test for.
	 * @param strict If true, this node must have all the attributes given to return true, otherwise this node
	 * may have one of any of the nodes given.
	 * @return whether this node has the given attributes or not.
	 */
	public boolean hasAttributes(String[] attrs, boolean strict) {
		return hasAttributes(Arrays.asList(attrs), strict);
	}
	
	/**
	 * Test whether this node has the given attributes or not.
	 * @param attrs The attributes to test for.
	 * @param strict If true, this node must have all the attributes given to return true, otherwise this node
	 * may have one of any of the nodes given.
	 * @return whether this node has the given attributes or not.
	 */
	public boolean hasAttributes(List<String> attrs, boolean strict) {
		for(String a : attrs) {
			if(strict) {
				if(hasAttribute(a)) {
					continue;
				} else {
					return false;
				}
			} else {
				if(hasAttribute(a)) {
					break;
				}
			}
		}
		return true;
	}
	
	/**
	 * Set the name of this node.
	 * @param name The new name
	 * @return this
	 */
	public XMLNode setName(String name) {
		_name = name;
		return this;
	}
	
	/**
	 * Set this nodes attributes. This is not the same as {@code setAttributes()}. This method sets the
	 * {@link Map} of this nodes attributes, so some values may be overwritten or completely removed altogether
	 * @param attributes The new attributes
	 * @return this
	 */
	public XMLNode setAttributesMap(Map<String, String> attributes) {
		_attributes = new AttributeMap(attributes);
		return this;
	}
	
	/**
	 * Set whether this node ends itself
	 * @param selfEnding
	 * @return this
	 * @throws TXMLException If this node is not empty
	 */
	public XMLNode setSelfEnding(boolean selfEnding) {
		if(selfEnding && !_nodes.isEmpty()) {
			throw new TXMLException("Not allowed to set selfEnding to true when a node contains nodes");
		}
		_selfEnding = selfEnding;
		return this;
	}
	
	/**
	 * Set an attribute of this node. Note that if the attribute already exists that it will be overwritten.
	 * Attributes are defined via {@code name="value"}. For instance, in the XMLNode represented by 
	 * <pre>
	 * {@code <node bleep="bloop"/>}
	 * </pre> the value of the attribute 'bleep' is 'bloop'.
	 * @param attr The attribute name
	 * @param value The attribute value
	 * @return this
	 */
	public XMLNode setAttribute(String attr, String value) {
		_attributes.put(attr, value);
		return this;
	}
	
	/**
	 * Set multiple attributes at once. Keys in the given Map act as the attribute names, while the Values act
	 * as the attribute values. Attributes are defined via {@code name="value"}. For instance, in the XMLNode 
	 * represented by<pre>
	 * {@code <node bleep="bloop"/>}
	 * </pre>  the value of the attribute 'bleep' is 'bloop'.
	 * @param attrs The attributes to set
	 * @return
	 */
	public XMLNode setAttributes(Map<String, String> attrs) {
		_attributes.putAll(attrs);
		return this;
	}
	
	@Override
	public boolean selfEnding() {
		return _selfEnding;
	}
	
	public String toString() {
		return toString(TXML.INDENT_FACTOR);
	}
	
	public String toString(int indentFactor) {
		return toString(0, indentFactor);
	}
	
	private String toString(int indent, int indentFactor) {
		if(indentFactor < 0) {
			indentFactor = 0;
		}
//		System.out.println("1");
		String spaces = "";
		boolean se = selfEnding(), addNewLines = indentFactor > 0;
		for(int i = 0; i < indent * indentFactor; i++) {
			spaces += " ";
		}
		String attrs = "";
		for(String s : _attributes.getKeys()) {
			attrs += s + "=\"" + _attributes.get(s) + "\" ";
		}
		String str = spaces + "<" + (_name + " " + attrs).trim() + (se ? "/" : "") + ">" + (addNewLines ? "\n" : "");
		for(XMLNode n : _nodes) {
//			System.out.println("2");
			str += spaces + n.toString(++indent, indentFactor) + (addNewLines ? "\n" : "");
		}
		if(!se) {
			str += spaces + "</" + _name + ">";
		}
		return str;
	}
	
}
