package com.kill3rtaco.txml;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * TXML
 * Copyright (c) 2014 Caleb Downs, aka KILL3RTACO 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * Represents an XML node. {@link XMLNode}s hold attribute values as well as other {@link XMLNode}s.
 * @author KILL3RTACO
 *
 */
public class XMLNode extends XMLContainer {
	
	protected String		_name;
	protected AttributeMap	_attributes;
	protected boolean		_selfEnding;
	protected String		_text;
	
	protected XMLNode() {
		_name = null;
		_attributes = null;
		_selfEnding = true;
		_text = null;
	}
	
	/**
	 * Creates a new {@link XMLNode}
	 * @param name The name of the node. For instance, in the XMLNode representing {@code <node bleep="bloop"/>},
	 * the name would be 'node'
	 * @throws TXMLException If the given name is invalid for any reason
	 */
	public XMLNode(String name) {
		this(name, new HashMap<String, String>());
	}
	
	/**
	 * Creates a new {@link XMLNode}. 
	 * @param name The name of the node. For instance, in the XMLNode representing {@code <node bleep="bloop"/>},
	 * the name would be 'node'
	 * @param attributes A map of attributes for the node
	 * @throws TXMLException If the given name is invalid for any reason
	 */
	public XMLNode(String name, Map<String, String> attributes) {
		this(name, attributes, new ArrayList<XMLNode>());
	}
	
	/**
	 * Creates a new {@link XMLNode}. 
	 * @param name The name of the node. For instance, in the XMLNode representing {@code <node bleep="bloop"/>},
	 * the name would be 'node'
	 * @param attributes An AttributeMap of attributes for the node
	 * @throws TXMLException If the given name is invalid for any reason
	 */
	public XMLNode(String name, AttributeMap attributes) {
		this(name, attributes, new ArrayList<XMLNode>());
	}
	
	/**
	 * Creates a new {@link XMLNode}. 
	 * @param name The name of the node. For instance, in the XMLNode representing {@code <node bleep="bloop"/>},
	 * the name would be 'node'. Cannot be null or empty
	 * @param attributes A map of attributes for the node
	 * @param nodes A list of nodes to be contained within this node
	 * @throws TXMLException If the given name is invalid for any reason
	 */
	public XMLNode(String name, Map<String, String> attributes, List<XMLNode> nodes) {
		this(name, new AttributeMap(attributes), nodes);
	}
	
	/**
	 * Creates a new {@link XMLNode}. 
	 * @param name The name of the node. For instance, in the XMLNode representing {@code <node bleep="bloop"/>},
	 * the name would be 'node'. Cannot be null or empty
	 * @param attributes An AttributeMap of attributes for the node
	 * @param nodes A list of nodes to be contained within this node
	 * @throws TXMLException If the given name is invalid for any reason
	 * @param nodes
	 */
	public XMLNode(String name, AttributeMap attributes, List<XMLNode> nodes) {
		this(name, attributes, nodes, false);
	}
	
	protected XMLNode(String name, AttributeMap attributes, List<XMLNode> nodes, boolean selfEnding) {
		super();
		setName(name);
		if(attributes == null) {
			_attributes = new AttributeMap();
		} else {
			_attributes = attributes;
		}
		if(nodes == null || _selfEnding) {
			_nodes = new ArrayList<XMLNode>();
		} else {
			_nodes = nodes;
		}
		_selfEnding = selfEnding;
		_text = "";
	}
	
	/**
	 * Get the name of this node. For instance, in the XMLNode representing {@code <node bleep="bloop"/>}, the
	 * name would be 'node'
	 * @return the name of this node.
	 */
	public String name() {
		return _name;
	}
	
	/**
	 * Get a map of all the attributes for this node.
	 * @return This nodes attributes
	 */
	public AttributeMap attributes() {
		return _attributes;
	}
	
	/**
	 * Get the text contained within this node
	 * @return the text contained within this node
	 */
	public String text() {
		return _text;
	}
	
	/**
	 * Set the text contained within this node
	 * @param text The text to set
	 * @return this
	 */
	public XMLNode setText(String text) {
		if(!isEmpty()) {
			throw new TXMLException("Can only set text of any empty node");
		} else if(isSelfEnding()) {
			throw new TXMLException("Can only set text of a node that is not self-ending");
		}
		if(text == null) {
			text = "";
		}
		_text = text;
		return this;
	}
	
	/**
	 * Check if this node contains any text
	 * @return true if and only if {@code getText().isEmpty()}
	 */
	public boolean hasText() {
		return !_text.isEmpty();
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
	 * Test whether or not this node has the given attribute/value pair
	 * @param attribute The attribute to test
	 * @param value The value of the attribute to test
	 * @return true if this node has the attribute value pair
	 */
	public boolean hasAttribute(String attribute, String value) {
		if(hasAttribute(attribute)) {
			return getAttribute(attribute).equals(value);
		}
		return false;
	}
	
	/**
	 * Test whether this node has the given attribute/value pairs
	 * @param attributes
	 * @param strict
	 * @return
	 */
	public boolean hasAttributes(Map<String, String> attributes, boolean strict) {
		for(String k : attributes.keySet()) {
			String v = attributes.get(k);
			if(strict) {
				if(getAttribute(k).equals(v)) {
					continue;
				} else {
					return false;
				}
			} else {
				if(getAttribute(k).equals(v)) {
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
	 * @throws TXMLException If the given name is invalid for any reason
	 */
	public XMLNode setName(String name) {
		if(name == null || name.isEmpty()) {
			throw new TXMLException("Names of nodes cannot be null or empty");
		} else if(name.matches("[0-9" + TXML.PUNC + "](.+)?")) {
			throw new TXMLException("Name '" + name + "'is invalid,"
					+ " names of nodes cannot start with numbers or puncuation characters");
//		} else if(name.toString().toUpperCase().startsWith("XML")) {
//			throw new TXMLException("Name '" + name + "'is invalid,"
//					+ " names of nodes cannot start with the letters 'xml'");
		} else if(name.contains(" ")) {
			throw new TXMLException("Name '" + name + "'is invalid,"
					+ " names of nodes cannot contain spaces");
		}
		_name = name;
		return this;
	}
	
	/**
	 * Set this nodes attributes. This is not the same as {@code setAttributes()}. This method sets the
	 * AttributeMap of this nodes attributes, so some values may be overwritten or completely removed altogether
	 * @param attributes The new attributes
	 * @return this
	 */
	public XMLNode setAttributeMap(Map<String, String> attrs) {
		return setAttributeMap(new AttributeMap(attrs));
	}
	
	/**
	 * Set this nodes attributes. This is not the same as {@code setAttributes()}. This method sets the
	 * AttributeMap of this nodes attributes, so some values may be overwritten or completely removed altogether
	 * @param attributes The new attributes
	 * @return this
	 */
	public XMLNode setAttributeMap(AttributeMap attrs) {
		_attributes = attrs;
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
	
	/**
	 * Get the value of an attribute
	 * @param attribute The attribute value to get
	 * @return The value of the given attribute
	 */
	public String getAttribute(String attribute) {
		return _attributes.get(attribute);
	}
	
	/**
	 * Get whether this node ends itself or not (ex. <rt bleep="bloop"/>)
	 * @return Whether this node ends itself or not
	 */
	public boolean isSelfEnding() {
		return _selfEnding;
	}
	
	/**
	 * Convert this XMLNode to a String using the default indentFactor (TXML.INDENT_FACTOR).
	 * 
	 * The indentFactor is how many spaces to use every indent.
	 * @return this as a String
	 */
	public String toString() {
		return toString(TXML.INDENT_FACTOR);
	}
	
	/**
	 * Convert this XMLNode to a String using the given indentFactor.
	 * 
	 * @param indentFactor How many spaces to indent every indent
	 * @return this as a String
	 */
	public String toString(int indentFactor) {
		return toString(0, indentFactor);
	}
	
	/**
	 * Convert this XMLNode to a String using the given indent and indentFactor.
	 * 
	 * @param indent How many times to indent
	 * @param indentFactor How many spaces to use every indent
	 * @return this as a string
	 */
	public String toString(int indent, int indentFactor) {
		if(indentFactor < 0) {
			indentFactor = 0;
		}
		boolean se = isSelfEnding(), addNewLines = indentFactor > 0;
		String nl = (addNewLines ? "\n" : ""), spaces = TXML.getSpaces(indent, indentFactor);
		String str = spaces + "<" + (_name + " " + _attributes.toString()).trim() + (se ? "/" : "") + ">";
		if(!se) {
			if(!hasText()) {
				str += nl;
			}
		}
		if(hasText()) {
			return str + _text + "</" + _name + ">";
		}
		for(XMLNode n : _nodes) {
			str += n.toString(indent + 1, indentFactor) + nl;
		}
		if(!se) {
			str += spaces + "</" + _name + ">";
		}
		return str;
	}
	
	/**
	 * Create an identical clone of this XMLNode, such that the fields within this node and the new node are the
	 * same, but that {@code node == clone} returns false.
	 */
	public XMLNode clone() {
		String name = _name;
		AttributeMap attrs = _attributes.clone();
		List<XMLNode> nodes = cloneNodes();
		XMLNode clone = new XMLNode(name, attrs, nodes);
		if(hasText()) {
			clone.setText(_text);
		} else {
			clone.setSelfEnding(_selfEnding);
		}
		return clone;
	}
	
}
