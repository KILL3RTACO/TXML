package com.kill3rtaco.txml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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
	 * Constructs an XMLDocument from a file
	 * @param file The file to read from
	 * @throws FileNotFoundException If the file was not found
	 */
	public XMLDocument(File file) throws FileNotFoundException {
		this(new FileInputStream(file));
	}
	
	/**
	 * Construct an XMLDocument from an InputStream
	 * @param stream The stream to read from
	 */
	public XMLDocument(InputStream stream) {
		this(TXML.stringFromStream(stream));
	}
	
	/**
	 * Construct an XMLDocument from a source string
	 * @param source The source string to use
	 */
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
	
	/**
	 * Convert this XMLDocument to a String using the default indentFactor (TXML.INDENT_FACTOR).
	 * 
	 * The indentFactor is how many spaces to use every indent.
	 * @return this as a string
	 */
	public String toString() {
		return toString(TXML.INDENT_FACTOR);
	}
	
	/**
	 * Convert this XMLDocument to a String using the given indentFactor.
	 * 
	 * @param indentFactor How many spaces to use every indent
	 * @return this as a string
	 */
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
	
	/**
	 * Create an identical clone of this XMLDocument, where the nodes contained within the
	 * document have the same field values but that {@code doc == clone} returns false.
	 */
	public XMLDocument clone() {
		return new XMLDocument(cloneNodes());
	}
	
}
