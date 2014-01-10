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
	private String			_text;
	
	/**
	 * Creates a new {@link XMLNode}
	 * @param name The name of the node. For instance, in the XMLNode representing {@code <node bleep="bloop"/>},
	 * the name would be 'node'
	 * @throws TXMLException If the given value for {@code name} is null, empty, or has spaces
	 */
	public XMLNode(String name) {
		this(name, new HashMap<String, String>());
	}
	
	/**
	 * Creates a new {@link XMLNode}. 
	 * @param name The name of the node. For instance, in the XMLNode representing {@code <node bleep="bloop"/>},
	 * the name would be 'node'
	 * @param attributes A map of attributes for the node, cannot be null
	 * @throws TXMLException If the given value for {@code name} is null, empty, or has spaces
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
	 * @throws TXMLException If the given value for {@code name} is null, empty, or has spaces
	 */
	public XMLNode(String name, Map<String, String> attributes, List<XMLNode> nodes) {
		super();
		if(name == null || name.isEmpty()) {
			throw new TXMLException("name cannot be null or empty");
		} else if(name.contains(" ")) {
			throw new TXMLException("name cannot have spaces");
		}
		_name = name;
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
	
	public String text() {
		return _text;
	}
	
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
	public boolean isSelfEnding() {
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
		boolean se = isSelfEnding(), addNewLines = indentFactor > 0;
		String attrs = "", nl = (addNewLines ? "\n" : ""), spaces = getSpaces(indent, indentFactor);
		for(String s : _attributes.getKeys()) {
			attrs += s + "=\"" + _attributes.get(s) + "\" ";
		}
		String str = spaces + "<" + (_name + " " + attrs).trim() + (se ? "/" : "") + ">" + (hasText() ? "" : nl);
		if(hasText()) {
			return str + _text + "</" + _name + ">";
		}
		for(XMLNode n : _nodes) {
			str += n.toString(++indent, indentFactor) + nl;
		}
		if(!se) {
			str += spaces + "</" + _name + ">";
		}
		return str;
	}
	
	private String getSpaces(int indent, int indentFactor) {
		String spaces = "";
		for(int i = 0; i < indent * indentFactor; i++) {
			spaces += " ";
		}
		return spaces;
	}
	
}
