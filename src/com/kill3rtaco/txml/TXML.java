package com.kill3rtaco.txml;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class containing utility methods
 * @author KILL3RTACO
 *
 */
public class TXML {
	
	/** The default indent factor **/
	public static final int			INDENT_FACTOR	= 2;
	
	/** The Character '&amp;'. */
	public static final Character	AMP				= new Character('&');
	
	/** The Character '''. */
	public static final Character	APOS			= new Character('\'');
	
	/** The Character '!'. */
	public static final Character	BANG			= new Character('!');
	
	/** The Character '='. */
	public static final Character	EQ				= new Character('=');
	
	/** The Character '>'. */
	public static final Character	GT				= new Character('>');
	
	/** The Character '&lt;'. */
	public static final Character	LT				= new Character('<');
	
	/** The Character '?'. */
	public static final Character	QUEST			= new Character('?');
	
	/** The Character '"'. */
	public static final Character	QUOT			= new Character('"');
	
	/** The Character '/'. */
	public static final Character	SLASH			= new Character('/');
	
	/** Puncuation characters*/
	public static final String		PUNC			= "!\"#$%&'()*+,./\\\\:;<=>?@\\^_`{|}~-";
	
	private TXML() {
	}
	
	/**
	 * Get the contents of a file
	 * @param file The file to read from
	 * @return The files contents
	 * @throws TXMLException If the given file is {@code null}, is a directory, or does not exist.
	 */
	public static String stringFromFile(File file) {
		if(file == null) {
			throw new TXMLException("Input cannot be null");
		} else if(file.isDirectory()) {
			throw new TXMLException("Input cannot be a directoy");
		}
		try {
			return stringFromStream(new FileInputStream(file));
		} catch (FileNotFoundException e) {
			throw new TXMLException(e);
		}
	}
	
	/**
	 * Get the contents from an InputStream
	 * @param stream The stream to read
	 * @return The contents of the stream
	 */
	public static String stringFromStream(InputStream stream) {
		String source = "";
		Scanner x = new Scanner(stream);
		while (x.hasNextLine()) {
			source += x.nextLine() + " \n";
		}
		source = source.trim();
		x.close();
		return source;
	}
	
	/**
	 * Get a String containing spaces based on the amount of indents.
	 * @param indent How many times to indent.
	 * @param indentFactor The size of the indent.
	 * @returna String containing spaces based on the amount of indents.
	 */
	public static String getSpaces(int indent, int indentFactor) {
		String spaces = "";
		for(int i = 0; i < indent * indentFactor; i++) {
			spaces += " ";
		}
		return spaces;
	}
	
	//copied from org.json.JSONML and modified as needed.
	private static Object parseNode(XMLTokener x, XMLNode node) {
// Test for and skip past these forms:
//      <!-- ... -->
//      <![  ... ]]>
//      <!   ...   >
//      <?   ...  ?>
		
		while (true) {
			if(!x.more()) {
				throw x.syntaxError("Bad XML");
			}
			Object token = x.nextContent();
			if(token == TXML.LT) {
				token = x.nextToken();
				if(token instanceof Character) {
					if(token == TXML.SLASH) {
						
// Close tag </
						
						token = x.nextToken();
						if(!(token instanceof String)) {
							throw new TXMLException(
									"Expected a closing name instead of '" +
											token + "'.");
						}
						if(x.nextToken() != TXML.GT) {
							throw x.syntaxError("Misshaped close tag");
						}
						return token;
					} else if(token == TXML.BANG) {
						
// <!
						
						char c = x.next();
						if(c == '-') {
							if(x.next() == '-') {
								x.skipPast("-->");
							} else {
								x.back();
							}
						} else if(c == '[') {
							token = x.nextToken();
							if(token.equals("CDATA") && x.next() == '[') {
								x.nextCDATA();
								//skip
							} else {
								throw x.syntaxError("Expected 'CDATA['");
							}
						} else {
							int i = 1;
							do {
								token = x.nextMeta();
								if(token == null) {
									throw x.syntaxError("Missing '>' after '<!'.");
								} else if(token == TXML.LT) {
									i += 1;
								} else if(token == TXML.GT) {
									i -= 1;
								}
							} while (i > 0);
						}
					} else if(token == TXML.QUEST) {
						
// <?
						
						x.skipPast("?>");
					} else {
						throw x.syntaxError("Misshaped tag");
					}
					
// Open tag <
					
				} else {
					if(!(token instanceof String)) {
						throw x.syntaxError("Bad tagName '" + token + "'.");
					}
					String tagName = (String) token;
					XMLNode newNode = new XMLNode(tagName);
//					newja = new JSONArray();
//					newjo = new JSONObject();
//					if(arrayForm) {
//						newja.put(tagName);
//						if(ja != null) {
//							ja.put(newja);
//						}
//					} else {
//						newjo.put("tagName", tagName);
//						if(ja != null) {
//							ja.put(newjo);
//						}
//					}
					//TODO marker
					if(node != null) {
						if(node.hasText()) {
							throw x.syntaxError("Nodes cannot contain text and nodes");
						}
						node.addNode(newNode);
					}
					
					token = null;
					for(;;) {
						if(token == null) {
							token = x.nextToken();
						}
						if(token == null) {
							throw x.syntaxError("Misshaped tag");
						}
						if(!(token instanceof String)) {
							break;
						}
						
// attribute = value
						
						String attribute = (String) token;
//						if(!arrayForm && ("tagName".equals(attribute) || "childNode".equals(attribute))) {
//							throw x.syntaxError("Reserved attribute.");
//						}
						token = x.nextToken();
						if(token == TXML.EQ) {
							token = x.nextToken();
							if(!(token instanceof String)) {
								throw x.syntaxError("Missing value");
							}
							newNode.setAttribute(attribute, (String) token);
							token = null;
						} else {
							newNode.setAttribute(attribute, "");
						}
					}
//					if(arrayForm && newjo.length() > 0) {
//						newja.put(newjo);
//					}
					
// Empty tag <.../>
					
					if(token == TXML.SLASH) {
						if(x.nextToken() != TXML.GT) {
							throw x.syntaxError("Misshaped tag");
						}
						newNode.setSelfEnding(true);
//						if(ja == null) {
//							if(arrayForm) {
//								return newja;
//							} else {
//								return newjo;
//							}
//						}
						if(node == null) {
							return newNode;
						}
						
// Content, between <...> and </...>
						
					} else {
						if(token != TXML.GT) {
							throw x.syntaxError("Misshaped tag");
						}
						Object close = parseNode(x, newNode);
						String closeTag = close instanceof String ? (String) close : ((XMLNode) close).name();
						if(closeTag != null) {
							if(!closeTag.equals(tagName)) {
								throw x.syntaxError("Mismatched '" + tagName +
										"' and '" + closeTag + "'");
							}
							tagName = null;
//							if(!arrayForm && newja.length() > 0) {
//								newjo.put("childNodes", newja);
//							}
							
							if(node == null) {
								return newNode;
							}
							
						}
					}
				}
			} else {
				if(node != null) {
					if(node.hasNodes()) {
						throw x.syntaxError("XMLNodes cannot contain text and nodes");
					}
					node.setText(token.toString());
				}
			}
		}
		
	}
	
	/**
	 * Get a list of nodes using an XMLTokener
	 * @param x The XMLTokener to use
	 * @return A list of nodes
	 */
	public static List<XMLNode> parseXML(XMLTokener x) {
		List<XMLNode> nodes = new ArrayList<XMLNode>();
		while (true) {
			if(x.more()) {
				nodes.add((XMLNode) parseNode(x, null));
			} else {
				break;
			}
		}
		return nodes;
	}
	
	/**
	 * Get a list of nodes from a source string
	 * @param source The string to read from
	 * @return a list of nodes
	 */
	public static List<XMLNode> parseXML(String source) {
		return parseXML(new XMLTokener(source));
	}
	
	/**
	 * Get a list of nodes from a stream
	 * @param source The stream to read
	 * @return a list of nodes
	 */
	public static List<XMLNode> parseXML(InputStream source) {
		return parseXML(new XMLTokener(stringFromStream(source)));
	}
	
	/**
	 * Get a list of nodes from a Reader
	 * @param source the reader to read from
	 * @return a list of nodes
	 */
	public static List<XMLNode> parseXML(Reader source) {
		return parseXML(new XMLTokener(source));
	}
}
