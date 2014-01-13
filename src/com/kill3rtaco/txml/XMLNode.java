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
public class XMLNode extends XMLContainer {
	
	protected String		_name;
	protected AttributeMap	_attributes;
	protected boolean		_selfEnding	= false;
	protected String		_text;
	
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
	 * @param attributes A map of attributes for the node, cannot be null
	 * @throws TXMLException If the given name is invalid for any reason
	 */
	public XMLNode(String name, Map<String, String> attributes) {
		this(name, attributes, new ArrayList<XMLNode>());
	}
	
	/**
	 * Creates a new {@link XMLNode}. 
	 * @param name The name of the node. For instance, in the XMLNode representing {@code <node bleep="bloop"/>},
	 * the name would be 'node'. Cannot be null or empty
	 * @param attributes A map of attributes for the node, cannot be null
	 * @param nodes A list of nodes contained within this node
	 * @throws TXMLException If the given name is invalid for any reason
	 */
	public XMLNode(String name, Map<String, String> attributes, List<XMLNode> nodes) {
		super();
		setName(name);
		if(attributes == null) {
			_attributes = new AttributeMap();
		} else {
			_attributes = new AttributeMap(attributes);
		}
		if(nodes == null) {
			_nodes = new ArrayList<XMLNode>();
		} else {
			_nodes = nodes;
		}
		_text = "";
	}
	
	/**
	 * Add a node to this node
	 * @throws TXMLException if this node contains text
	 */
	public XMLNode addNode(XMLNode node) {
		if(hasText()) {
			throw new TXMLException("Can only add nodes if the node does not contain any text");
		}
		return super.addNode(node);
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
	
	public boolean hasAttribute(String attribute, String value) {
		return _attributes.get(attribute).equals(value);
	}
	
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
		} else if(name.toString().toUpperCase().startsWith("XML")) {
			throw new TXMLException("Name '" + name + "'is invalid,"
					+ " names of nodes cannot start with the letters 'xml'");
		} else if(name.contains(" ")) {
			throw new TXMLException("Name '" + name + "'is invalid,"
					+ " names of nodes cannot contain spaces");
		}
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
		String attrs = "", nl = (addNewLines ? "\n" : ""), spaces = TXML.getSpaces(indent, indentFactor);
		for(String s : _attributes.getKeys()) {
			attrs += s + "=\"" + _attributes.get(s) + "\" ";
		}
		String str = spaces + "<" + (_name + " " + attrs).trim() + (se ? "/" : "") + ">";
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
	
}
