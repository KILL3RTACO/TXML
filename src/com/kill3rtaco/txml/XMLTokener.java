package com.kill3rtaco.txml;


public class XMLTokener {
	
	private boolean	_eof;
	private long	_index;
	private long	_line;
	private String	_source;
	
	public XMLTokener(String source) {
		_eof = false;
		_index = 0;
		_line = 0;
	}
	
//	public List<XMLNode> parse() {
//		return parseContents(_source);
//	}
//	
//	private List<XMLNode> parseContents(String source) {
//		
//	}
//	
//	private XMLNode parseNode(String node) {
//		
//	}
	
}
