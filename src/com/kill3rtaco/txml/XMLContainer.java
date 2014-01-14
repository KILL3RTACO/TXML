package com.kill3rtaco.txml;

import java.util.ArrayList;
import java.util.Collection;
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
 * Represents a container in XML. This can be either an {@link XMLDocument} or an {@link XMLNode}.
 * @author KILL3RTACO
 *
 */
public abstract class XMLContainer implements Cloneable {
	
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
		if(node == null) {
			throw new TXMLException("Cannot add a null node");
		}
		_nodes.add(node);
		return node;
	}
	
	/**
	 * Returns whether this XMLContainer contains any nodes or not
	 * @return true if this container has nodes
	 */
	public boolean hasNodes() {
		return !_nodes.isEmpty();
	}
	
	/**
	 * Get a list of top-level nodes in this conatiner.
	 * @return The nodes in this container.
	 */
	public List<XMLNode> nodes() {
		return _nodes;
	}
	
	/**
	 * Remove the given XMLNode from this container
	 * @param node The ndoe to remove
	 * @return whether the node was removed
	 */
	public boolean remove(XMLNode node) {
		return _nodes.remove(node);
	}
	
	public XMLNode removeFirst() {
		if(_nodes.isEmpty()) {
			return null;
		}
		return _nodes.remove(0);
	}
	
	/**
	 * Remove the first top-level node within this container that has the given name
	 * @param name The name to search for
	 * @return The node that was removed, or null if no node was found
	 */
	public XMLNode removeFirst(String name) {
		if(isEmpty()) {
			return null;
		} else {
			List<XMLNode> nodes = get(name);
			if(nodes.isEmpty()) {
				return null;
			}
			XMLNode n = nodes.get(0);
			_nodes.remove(n);
			return n;
		}
	}
	
	/**
	 * Remove all of the given nodes from this container
	 * @param nodes The nodes to remove
	 * @return If one of the nodes was removed
	 */
	public boolean removeAll(Collection<XMLNode> nodes) {
		return _nodes.removeAll(nodes);
	}
	
	/**
	 * Remove all nodes in this container whose name matches the given name
	 * @param name The name of the nodes to remove
	 * @return A list of node that were removed
	 */
	public List<XMLNode> removeAll(String name) {
		return removeAll(newList(name));
	}
	
	/**
	 * Remove all nodes within this container whose name matches any of the given names
	 * @param names The names of the nodes to remove
	 * @return A list of nodes that were removed
	 */
	public List<XMLNode> removeAll(List<String> names) {
		List<XMLNode> removed = get(names);
		for(XMLNode n : removed) {
			_nodes.remove(n);
		}
		return removed;
	}
	
	/**
	 * Remove all nodes within this container whose name matches the given name, and whose attributes are the same
	 * as the given attributes (depending on {@code strict}).
	 * @param name The name of the node to remove
	 * @param attributes The attributes to match
	 * @param strict If true, nodes must have all the attributes to be removed, otherwise they may have any one
	 * of the given attributes
	 * @return A list of nodes that were removed
	 */
	public List<XMLNode> removeAll(String name, List<String> attributes, boolean strict) {
		return removeAll(newList(name), attributes, strict);
	}
	
	/**
	 * Remove all nodes within this container whose name matches any of the given names, and whose attributes 
	 * are the same as the given attributes (depending on {@code strict}).
	 * @param names The names of the nodes to remove
	 * @param attributes The attributes to match
	 * @param strict If true, nodes must have all the attributes to be removed, otherwise they may have any one
	 * of the given attributes
	 * @return A list of nodes that were removed
	 */
	public List<XMLNode> removeAll(List<String> names, List<String> attributes, boolean strict) {
		List<XMLNode> removed = get(names, attributes, strict);
		for(XMLNode n : removed) {
			_nodes.remove(n);
		}
		return removed;
	}
	
	/**
	 * Remove all nodes within this container whose name matches the given name, and whose attributes are the same
	 * as the given attributes (depending on {@code strict}).
	 * @param name The name of the node to remove
	 * @param attributes The attributes to match
	 * @param strict If true, nodes must have all the attributes and their values to be removed, otherwise they
	 * may have any one of the given attribute/value pairs
	 * @return A list of nodes that were removed
	 */
	public List<XMLNode> removeAll(String name, Map<String, String> attributes, boolean strict) {
		return removeAll(newList(name), attributes, strict);
	}
	
	/**
	 * Remove all nodes within this container whose name matches any of the given name, and whose attributes 
	 * are the same as the given attributes (depending on {@code strict}).
	 * @param name The names of the nodes to remove
	 * @param attributes The attributes to match
	 * @param strict If true, nodes must have all the attributes and their values to be removed, otherwise they
	 * may have any one of the given attribute/value pairs
	 * @return A list of nodes that were removed
	 */
	public List<XMLNode> removeAll(List<String> names, Map<String, String> attributes, boolean strict) {
		List<XMLNode> removed = get(names, attributes, strict);
		for(XMLNode n : removed) {
			_nodes.remove(n);
		}
		return removed;
	}
	
	/**
	 * Get the first node in this container, or null if this container is empty
	 * @return The first node
	 */
	public XMLNode getFirst() {
		if(isEmpty()) {
			return null;
		}
		return _nodes.get(0);
	}
	
	/**
	 * 
	 * @param index
	 * @return
	 */
	public XMLNode get(int index) {
		if(index < 0 || index >= size()) {
			return null;
		}
		return _nodes.get(index);
	}
	
	/**
	 * Get a list of top-level nodes whose name {@code equals()} the given name.
	 * @param name The name to look for.
	 * @return a list of top-level nodes whose name equal the given name.
	 */
	public List<XMLNode> get(String name) {
		List<String> names = new ArrayList<String>();
		names.add(name);
		return get(names);
	}
	
	/**
	 * Get a list of top-level nodes whose name matches any of the given names
	 * @param name The name of the nodes to get
	 * @return A list of nodes
	 */
	public List<XMLNode> get(List<String> names) {
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
	 * Get a list of top-level nodes in this container with the given attributes.
	 * @param attributes The attributes to test for.
	 * @param strict If true, nodes must have all the attributes given to return true, otherwise nodes
	 * may have one of any of the attributes given.
	 * @return A list of XMLNodes
	 */
	public List<XMLNode> get(List<String> attributes, boolean strict) {
		List<XMLNode> list = new ArrayList<XMLNode>();
		for(XMLNode n : _nodes) {
			if(n.hasAttributes(attributes, strict)) {
				list.add(n);
			}
		}
		return list;
	}
	
	/**
	 * Get a list of top-level nodes in this container that have the given attributes and their values.
	 * @param attributes The attributes to look for
	 * @param strict If true, nodes must have all key and value pairs, otherwise nodes may have one key and value
	 * pair in the given map
	 * @return a list of top-level nodes in this container that have the given attributes and their values.
	 */
	public List<XMLNode> get(Map<String, String> attributes, boolean strict) {
		List<XMLNode> list = new ArrayList<XMLNode>();
		for(XMLNode n : _nodes) {
			if(n.hasAttributes(attributes, strict)) {
				list.add(n);
			}
		}
		return list;
	}
	
	/**
	 * Get a list of top-level nodes whose name matches the given names, and whose attributes
	 * are the same as the given attributes (depending on {@code strict}).
	 * @param name The name of the nodes to get
	 * @param attributes The attributes to match
	 * @param strict If true, nodes must have all the attributes to be included in the returned list, otherwise
	 * they may have any one of the given attributes
	 * @return A list of nodes
	 */
	public List<XMLNode> get(String name, List<String> attributes, boolean strict) {
		return get(newList(name), attributes, strict);
	}
	
	/**
	 * Get a list of top-level nodes whose name matches any of the given names, and whose attributes 
	 * are the same as the given attributes (depending on {@code strict}).
	 * @param name The names of the nodes to get
	 * @param attributes The attributes to match
	 * @param strict If true, nodes must have all the attributes to be included in the returned list, otherwise
	 * they may have any one of the given attributes
	 * @return A list of nodes
	 */
	public List<XMLNode> get(List<String> names, List<String> attributes, boolean strict) {
		List<XMLNode> list = get(names);
		List<XMLNode> nodes = new ArrayList<XMLNode>();
		for(XMLNode n : list) {
			if(n.hasAttributes(attributes, strict)) {
				nodes.add(n);
			}
		}
		return nodes;
	}
	
	/**
	 * Get a list of top-level nodes whose name matches the given name, and whose attributes 
	 * are the same as the given attributes (depending on {@code strict}).
	 * @param name The names of the nodes to remove
	 * @param attributes The attribute/value pairs to match
	 * @param strict If true, nodes must have all the attributes and their values to be removed, otherwise they
	 * may have any one of the given attribute/value pairs
	 * @return A list of nodes 
	 */
	public List<XMLNode> get(String name, Map<String, String> attributes, boolean strict) {
		return get(newList(name), attributes, strict);
	}
	
	/**
	 * Get a list of top-level nodes whose name matches any of the given names, and whose attributes 
	 * are the same as the given attributes (depending on {@code strict}).
	 * @param name The names of the nodes to remove
	 * @param attributes The attribute/value pairs to match
	 * @param strict If true, nodes must have all the attributes and their values to be removed, otherwise they
	 * may have any one of the given attribute/value pairs
	 * @return A list of nodes 
	 */
	public List<XMLNode> get(List<String> names, Map<String, String> attributes, boolean strict) {
		List<XMLNode> list = get(names);
		List<XMLNode> nodes = new ArrayList<XMLNode>();
		for(XMLNode n : list) {
			if(n.hasAttributes(attributes, strict)) {
				nodes.add(n);
			}
		}
		return nodes;
	}
	
	/**
	 * Clear this container.
	 */
	public void clear() {
		_nodes = new ArrayList<XMLNode>();
	}
	
	/**
	 * Gets whether this container is empty or not. This yields the same result as
	 * <pre>
	 * get().isEmpty()</pre>
	 * @return
	 */
	public boolean isEmpty() {
		return _nodes.isEmpty();
	}
	
	/**
	 * Gets the amount of nodes in this container. This yields the same result as
	 * <pre>
	 * get().size()</pre>
	 * @return
	 */
	public int size() {
		return _nodes.size();
	}
	
	protected <T extends Object> List<T> newList(T... elements) {
		List<T> list = new ArrayList<T>();
		for(T e : elements) {
			list.add(e);
		}
		return list;
	}
	
	protected List<XMLNode> cloneNodes() {
		List<XMLNode> nodes = new ArrayList<XMLNode>();
		for(XMLNode n : _nodes) {
			nodes.add(n.clone());
		}
		return nodes;
	}
	
}
