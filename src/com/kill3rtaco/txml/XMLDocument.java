package com.kill3rtaco.txml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an XML document. An {@link XMLDocument} holds {@link XMLNode}s.
 * @author KILL3RTACO
 *
 */
public class XMLDocument extends XMLContainer {
	
	/**
	 * Constructs an empty XMLDocument.
	 */
	public XMLDocument() {
		_nodes = new ArrayList<XMLNode>();
	}
	
	/**
	 * Reads and parses an XML document from a file.
	 * @param file The file to read from
	 * @throws FileNotFoundException If the file was not found
	 */
	public XMLDocument(File file) throws FileNotFoundException {
		this(new FileInputStream(file));
	}
	
	public XMLDocument(InputStream stream) {
		this(TXML.parseXML(stream));
	}
	
	public XMLDocument(String source) {
		this(TXML.parseXML(source));
	}
	
	/**
	 * Constructs an XMLDocument, adding the nodes to the document
	 * @param nodes
	 */
	public XMLDocument(List<XMLNode> nodes) {
		_nodes = nodes;
	}
	
	@Override
	public boolean isSelfEnding() {
		return false;
	}
	
	public String toString() {
		return toString(TXML.INDENT_FACTOR);
	}
	
	public String toString(int indentFactor) {
		if(indentFactor < 0) {
			indentFactor = 0;
		}
		String str = "";
		for(XMLNode n : _nodes) {
			str += n.toString(indentFactor) + (indentFactor > 0 ? "\n" : "");
		}
		return str.trim();
	}
	
}
