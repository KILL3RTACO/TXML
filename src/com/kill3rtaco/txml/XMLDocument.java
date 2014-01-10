package com.kill3rtaco.txml;

import java.io.File;
import java.util.ArrayList;

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
	 */
	public XMLDocument(File file) {
//		String source = TXML.stringFromFile(file);
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
