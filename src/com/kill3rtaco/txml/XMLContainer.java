package com.kill3rtaco.txml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Represents a container in XML. This can be either an {@link XMLDocument} or an {@link XMLNode}.
 * @author KILL3RTACO
 *
 */
public abstract class XMLContainer {
	
	protected List<XMLNode>	_nodes;
	
	public XMLContainer() {
		_nodes = new ArrayList<XMLNode>();
	}
	
	/**
	 * Gets whether this {@link XMLContainer} ends itself (always false for {@link XMLDocument}). For instance, 
	 * in the XMLNode represented by <pre>
	 * {@code <node bleep="bloop"/>}
	 * </pre> this returns true.
	 * @return Whether this XMLContainer ends itself.
	 */
	public abstract boolean isSelfEnding();
	
	/**
	 * Add an {@link XMLNode} to this container. This yields the same result as
	 * <pre>
	 * addNode(new XMLNode(name)) </pre>
	 * @param name The name of the new XMLNode to add.
	 * @return The node added
	 * @throws TXMLException If this container is self-ending or something else goes wrong
	 */
	public XMLNode addNode(String name) {
		return addNode(new XMLNode(name));
	}
	
	/**
	 * Add an {@link XMLNode} to this container. This yields the same result as
	 * <pre>
	 * addNode(new XMLNode(name)) </pre>
	 * @param name The name of the new XMLNode to add.
	 * @param attributes The attributes of the new XMLNode
	 * @return The node added
	 * @throws TXMLException If this container is self-ending or something else goes wrong
	 */
	public XMLNode addNode(String name, Map<String, String> attributes) {
		return addNode(new XMLNode(name, attributes));
	}
	
	/**
	 * Add an {@link XMLNode} to this container.
	 * @param node The node to add.
	 * @return The node added.
	 * @throws TXMLException If this container is self-ending or something else goes wrong
	 */
	public XMLNode addNode(XMLNode node) {
		if(isSelfEnding()) {
			throw new TXMLException("Cannot add nodes to self-ending nodes");
		}
		_nodes.add(node);
		return node;
	}
	
	/**
	 * Get a list of top-level nodes in this conatiner.
	 * @return The nodes in this container.
	 */
	public List<XMLNode> nodes() {
		return _nodes;
	}
	
	/**
	 * Get a list of top-level nodes in this container with the given attributes.
	 * @param attributes The attributes to test for.
	 * @param strict If true, nodes must have all the attributes given to return true, otherwise nodes
	 * may have one of any of the nodes given.
	 * @return A list of XMLNodes
	 */
	public List<XMLNode> getNodesWithAtrributes(String[] attributes, boolean strict) {
		return getNodesWithAttributes(Arrays.asList(attributes), strict);
	}
	
	/**
	 * Get a list of top-level nodes in this container with the given attributes.
	 * @param attributes The attributes to test for.
	 * @param strict If true, nodes must have all the attributes given to return true, otherwise nodes
	 * may have one of any of the nodes given.
	 * @return A list of XMLNodes
	 */
	public List<XMLNode> getNodesWithAttributes(List<String> attributes, boolean strict) {
		List<XMLNode> list = new ArrayList<XMLNode>();
		for(XMLNode n : _nodes) {
			if(n.hasAttributes(attributes, strict)) {
				list.add(n);
			}
		}
		return list;
	}
	
	/**
	 * Get a list of top-level nodes whose name {@code equals()} the given name.
	 * @param name The name to look for.
	 * @return a list of top-level nodes whose name equal the given name.
	 */
	public List<XMLNode> getNodes(String name) {
		return getNodes(new String[]{name});
	}
	
	/**
	 * Get a list of top-level nodes whose name {@code equals()} of the given names.
	 * @param name The names to look for.
	 * @return a list of top-level nodes whose name equal any of the given names.
	 */
	public List<XMLNode> getNodes(String[] names) {
		List<XMLNode> list = new ArrayList<XMLNode>();
		for(XMLNode n : _nodes) {
			for(String s : names) {
				if(n.name().equals(s)) {
					list.add(n);
					break;
				}
			}
		}
		return list;
	}
	
	/**
	 * Empty this container.
	 */
	public void empty() {
		_nodes = new ArrayList<XMLNode>();
	}
	
	/**
	 * Gets whether this container is empty or not. This yields the same result as
	 * <pre>
	 * getNodes().isEmpty()</pre>
	 * @return
	 */
	public boolean isEmpty() {
		return _nodes.isEmpty();
	}
	
	/**
	 * Gets the amount of nodes in this container.  This yields the same result as
	 * <pre>
	 * getNodes().size()</pre>
	 * @return
	 */
	public int size() {
		return _nodes.size();
	}
}
