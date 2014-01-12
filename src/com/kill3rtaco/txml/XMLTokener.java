package com.kill3rtaco.txml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;

/*
Copyright (c) 2002 JSON.org

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

The Software shall be used for Good, not Evil.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
*/

/**
 * The XMLTokener extends the JSONTokener to provide additional methods
 * for the parsing of TXML texts.
 * @author JSON.org
 * @version 2012-11-13
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class XMLTokener {
	
	//All occurances of JSONException changed to TXMLException
	//Methods and constructors from JSONTokener merged to this class
	
	private long							character;
	private boolean							eof;
	private long							index;
	private long							line;
	private char							previous;
	private Reader							reader;
	private boolean							usePrevious;
	
	/** The table of entity values. It initially contains Character values for
	 * amp, apos, gt, lt, quot.
	 */
	public static final java.util.HashMap	entity;
	
	static {
		entity = new java.util.HashMap(8);
		entity.put("amp", TXML.AMP);
		entity.put("apos", TXML.APOS);
		entity.put("gt", TXML.GT);
		entity.put("lt", TXML.LT);
		entity.put("quot", TXML.QUOT);
	}
	
	public XMLTokener(Reader reader) {
		this.reader = reader.markSupported()
				? reader
				: new BufferedReader(reader);
		this.eof = false;
		this.usePrevious = false;
		this.previous = 0;
		this.index = 0;
		this.character = 1;
		this.line = 1;
	}
	
	public XMLTokener(InputStream inputStream) throws TXMLException {
		this(new InputStreamReader(inputStream));
	}
	
	public XMLTokener(String s) {
		this(new StringReader(s));
	}
	
	/**
	 * Back up one character. This provides a sort of lookahead capability,
	 * so that you can test for a digit or letter before attempting to parse
	 * the next number or identifier.
	 */
	public void back() throws TXMLException {
		if(this.usePrevious || this.index <= 0) {
			throw new TXMLException("Stepping back two steps is not supported");
		}
		this.index -= 1;
		this.character -= 1;
		this.usePrevious = true;
		this.eof = false;
	}
	
	/**
	 * Get the hex value of a character (base16).
	 * @param c A character between '0' and '9' or between 'A' and 'F' or
	 * between 'a' and 'f'.
	 * @return  An int between 0 and 15, or -1 if c was not a hex digit.
	 */
	public static int dehexchar(char c) {
		if(c >= '0' && c <= '9') {
			return c - '0';
		}
		if(c >= 'A' && c <= 'F') {
			return c - ('A' - 10);
		}
		if(c >= 'a' && c <= 'f') {
			return c - ('a' - 10);
		}
		return -1;
	}
	
	public boolean end() {
		return this.eof && !this.usePrevious;
	}
	
	/**
	 * Determine if the source string still contains characters that next()
	 * can consume.
	 * @return true if not yet at the end of the source.
	 */
	public boolean more() throws TXMLException {
		this.next();
		if(this.end()) {
			return false;
		}
		this.back();
		return true;
	}
	
	/**
	 * Get the next character in the source string.
	 *
	 * @return The next character, or 0 if past the end of the source string.
	 */
	public char next() throws TXMLException {
		int c;
		if(this.usePrevious) {
			this.usePrevious = false;
			c = this.previous;
		} else {
			try {
				c = this.reader.read();
			} catch (IOException exception) {
				throw new TXMLException(exception);
			}
			
			if(c <= 0) { // End of stream
				this.eof = true;
				c = 0;
			}
		}
		this.index += 1;
		if(this.previous == '\r') {
			this.line += 1;
			this.character = c == '\n' ? 0 : 1;
		} else if(c == '\n') {
			this.line += 1;
			this.character = 0;
		} else {
			this.character += 1;
		}
		this.previous = (char) c;
		return this.previous;
	}
	
	/**
	 * Consume the next character, and check that it matches a specified
	 * character.
	 * @param c The character to match.
	 * @return The character.
	 * @throws TXMLException if the character does not match.
	 */
	public char next(char c) {
		char n = this.next();
		if(n != c) {
			throw this.syntaxError("Expected '" + c + "' and instead saw '" +
					n + "'");
		}
		return n;
	}
	
	/**
	 * Get the next n characters.
	 *
	 * @param n     The number of characters to take.
	 * @return      A string of n characters.
	 * @throws TXMLException
	 *   Substring bounds error if there are not
	 *   n characters remaining in the source string.
	 */
	public String next(int n) throws TXMLException {
		if(n == 0) {
			return "";
		}
		
		char[] chars = new char[n];
		int pos = 0;
		
		while (pos < n) {
			chars[pos] = this.next();
			if(this.end()) {
				throw this.syntaxError("Substring bounds error");
			}
			pos += 1;
		}
		return new String(chars);
	}
	
	/**
	 * Get the next char in the string, skipping whitespace.
	 * @throws TXMLException
	 * @return  A character, or 0 if there are no more characters.
	 */
	public char nextClean() throws TXMLException {
		for(;;) {
			char c = this.next();
			if(c == 0 || c > ' ') {
				return c;
			}
		}
	}
	
	/**
	 * Return the characters up to the next close quote character.
	 * Backslash processing is done. The formal JSON format does not
	 * allow strings in single quotes, but an implementation is allowed to
	 * accept them.
	 * @param quote The quoting character, either
	 *      <code>"</code>&nbsp;<small>(double quote)</small> or
	 *      <code>'</code>&nbsp;<small>(single quote)</small>.
	 * @return      A String.
	 * @throws TXMLException Unterminated string.
	 */
	public String nextString(char quote) throws TXMLException {
		char c;
		StringBuffer sb = new StringBuffer();
		for(;;) {
			c = this.next();
			switch(c) {
				case 0:
				case '\n':
				case '\r':
					throw this.syntaxError("Unterminated string");
				case '\\':
					c = this.next();
					switch(c) {
						case 'b':
							sb.append('\b');
							break;
						case 't':
							sb.append('\t');
							break;
						case 'n':
							sb.append('\n');
							break;
						case 'f':
							sb.append('\f');
							break;
						case 'r':
							sb.append('\r');
							break;
						case 'u':
							sb.append((char) Integer.parseInt(this.next(4), 16));
							break;
						case '"':
						case '\'':
						case '\\':
						case '/':
							sb.append(c);
							break;
						default:
							throw this.syntaxError("Illegal escape.");
					}
					break;
				default:
					if(c == quote) {
						return sb.toString();
					}
					sb.append(c);
			}
		}
	}
	
	/**
	 * Get the text up but not including the specified character or the
	 * end of line, whichever comes first.
	 * @param  delimiter A delimiter character.
	 * @return   A string.
	 */
	public String nextTo(char delimiter) throws TXMLException {
		StringBuffer sb = new StringBuffer();
		for(;;) {
			char c = this.next();
			if(c == delimiter || c == 0 || c == '\n' || c == '\r') {
				if(c != 0) {
					this.back();
				}
				return sb.toString().trim();
			}
			sb.append(c);
		}
	}
	
	/**
	 * Get the text up but not including one of the specified delimiter
	 * characters or the end of line, whichever comes first.
	 * @param delimiters A set of delimiter characters.
	 * @return A string, trimmed.
	 */
	public String nextTo(String delimiters) throws TXMLException {
		char c;
		StringBuffer sb = new StringBuffer();
		for(;;) {
			c = this.next();
			if(delimiters.indexOf(c) >= 0 || c == 0 ||
					c == '\n' || c == '\r') {
				if(c != 0) {
					this.back();
				}
				return sb.toString().trim();
			}
			sb.append(c);
		}
	}
	
	/**
	 * Get the text in the CDATA block.
	 * @return The string up to the <code>]]&gt;</code>.
	 * @throws TXMLException If the <code>]]&gt;</code> is not found.
	 */
	public String nextCDATA() throws TXMLException {
		char c;
		int i;
		StringBuffer sb = new StringBuffer();
		for(;;) {
			c = next();
			if(end()) {
				throw syntaxError("Unclosed CDATA");
			}
			sb.append(c);
			i = sb.length() - 3;
			if(i >= 0 && sb.charAt(i) == ']' &&
					sb.charAt(i + 1) == ']' && sb.charAt(i + 2) == '>') {
				sb.setLength(i);
				return sb.toString();
			}
		}
	}
	
	/**
	 * Get the next TXML outer token, trimming whitespace. There are two kinds
	 * of tokens: the '<' character which begins a markup tag, and the content
	 * text between markup tags.
	 *
	 * @return  A string, or a '<' Character, or null if there is no more
	 * source text.
	 * @throws TXMLException
	 */
	public Object nextContent() throws TXMLException {
		char c;
		StringBuffer sb;
		do {
			c = next();
		} while (Character.isWhitespace(c));
		if(c == 0) {
			return null;
		}
		if(c == '<') {
			return TXML.LT;
		}
		sb = new StringBuffer();
		for(;;) {
			if(c == '<' || c == 0) {
				back();
				return sb.toString().trim();
			}
			if(c == '&') {
				sb.append(nextEntity(c));
			} else {
				sb.append(c);
			}
			c = next();
		}
	}
	
	/**
	 * Return the next entity. These entities are translated to Characters:
	 *     <code>&amp;  &apos;  &gt;  &lt;  &quot;</code>.
	 * @param ampersand An ampersand character.
	 * @return  A Character or an entity String if the entity is not recognized.
	 * @throws TXMLException If missing ';' in TXML entity.
	 */
	public Object nextEntity(char ampersand) throws TXMLException {
		StringBuffer sb = new StringBuffer();
		for(;;) {
			char c = next();
			if(Character.isLetterOrDigit(c) || c == '#') {
				sb.append(Character.toLowerCase(c));
			} else if(c == ';') {
				break;
			} else {
				throw syntaxError("Missing ';' in TXML entity: &" + sb);
			}
		}
		String string = sb.toString();
		Object object = entity.get(string);
		return object != null ? object : ampersand + string + ";";
	}
	
	/**
	 * Returns the next TXML meta token. This is used for skipping over <!...>
	 * and <?...?> structures.
	 * @return Syntax characters (<code>< > / = ! ?</code>) are returned as
	 *  Character, and strings and names are returned as Boolean. We don't care
	 *  what the values actually are.
	 * @throws TXMLException If a string is not properly closed or if the TXML
	 *  is badly structured.
	 */
	public Object nextMeta() throws TXMLException {
		char c;
		char q;
		do {
			c = next();
		} while (Character.isWhitespace(c));
		switch(c) {
			case 0:
				throw syntaxError("Misshaped meta tag");
			case '<':
				return TXML.LT;
			case '>':
				return TXML.GT;
			case '/':
				return TXML.SLASH;
			case '=':
				return TXML.EQ;
			case '!':
				return TXML.BANG;
			case '?':
				return TXML.QUEST;
			case '"':
			case '\'':
				q = c;
				for(;;) {
					c = next();
					if(c == 0) {
						throw syntaxError("Unterminated string");
					}
					if(c == q) {
						return Boolean.TRUE;
					}
				}
			default:
				for(;;) {
					c = next();
					if(Character.isWhitespace(c)) {
						return Boolean.TRUE;
					}
					switch(c) {
						case 0:
						case '<':
						case '>':
						case '/':
						case '=':
						case '!':
						case '?':
						case '"':
						case '\'':
							back();
							return Boolean.TRUE;
					}
				}
		}
	}
	
	/**
	 * Get the next TXML Token. These tokens are found inside of angle
	 * brackets. It may be one of these characters: <code>/ > = ! ?</code> or it
	 * may be a string wrapped in single quotes or double quotes, or it may be a
	 * name.
	 * @return a String or a Character.
	 * @throws TXMLException If the TXML is not well formed.
	 */
	public Object nextToken() throws TXMLException {
		char c;
		char q;
		StringBuffer sb;
		do {
			c = next();
		} while (Character.isWhitespace(c));
		switch(c) {
			case 0:
				throw syntaxError("Misshaped element");
			case '<':
				throw syntaxError("Misplaced '<'");
			case '>':
				return TXML.GT;
			case '/':
				return TXML.SLASH;
			case '=':
				return TXML.EQ;
			case '!':
				return TXML.BANG;
			case '?':
				return TXML.QUEST;
				
// Quoted string
				
			case '"':
			case '\'':
				q = c;
				sb = new StringBuffer();
				for(;;) {
					c = next();
					if(c == 0) {
						throw syntaxError("Unterminated string");
					}
					if(c == q) {
						return sb.toString();
					}
					if(c == '&') {
						sb.append(nextEntity(c));
					} else {
						sb.append(c);
					}
				}
			default:
				
// Name
				
				sb = new StringBuffer();
				for(;;) {
					sb.append(c);
					c = next();
					if(Character.isWhitespace(c)) {
						return sb.toString();
					}
					switch(c) {
						case 0:
							return sb.toString();
						case '>':
						case '/':
						case '=':
						case '!':
						case '?':
						case '[':
						case ']':
							back();
							return sb.toString();
						case '<':
						case '"':
						case '\'':
							throw syntaxError("Bad character in a name");
					}
				}
		}
	}
	
	/**
	 * Skip characters until past the requested string.
	 * If it is not found, we are left at the end of the source with a result of false.
	 * @param to A string to skip past.
	 * @throws TXMLException
	 */
	public boolean skipPast(String to) throws TXMLException {
		boolean b;
		char c;
		int i;
		int j;
		int offset = 0;
		int length = to.length();
		char[] circle = new char[length];
		
		/*
		 * First fill the circle buffer with as many characters as are in the
		 * to string. If we reach an early end, bail.
		 */
		
		for(i = 0; i < length; i += 1) {
			c = next();
			if(c == 0) {
				return false;
			}
			circle[i] = c;
		}
		
		/* We will loop, possibly for all of the remaining characters. */
		
		for(;;) {
			j = offset;
			b = true;
			
			/* Compare the circle buffer with the to string. */
			
			for(i = 0; i < length; i += 1) {
				if(circle[j] != to.charAt(i)) {
					b = false;
					break;
				}
				j += 1;
				if(j >= length) {
					j -= length;
				}
			}
			
			/* If we exit the loop with b intact, then victory is ours. */
			
			if(b) {
				return true;
			}
			
			/* Get the next character. If there isn't one, then defeat is ours. */
			
			c = next();
			if(c == 0) {
				return false;
			}
			/*
			 * Shove the character in the circle buffer and advance the
			 * circle offset. The offset is mod n.
			 */
			circle[offset] = c;
			offset += 1;
			if(offset >= length) {
				offset -= length;
			}
		}
	}
	
	/**
	 * Skip characters until the next character is the requested character.
	 * If the requested character is not found, no characters are skipped.
	 * @param to A character to skip to.
	 * @return The requested character, or zero if the requested character
	 * is not found.
	 */
	public char skipTo(char to) throws TXMLException {
		char c;
		try {
			long startIndex = this.index;
			long startCharacter = this.character;
			long startLine = this.line;
			this.reader.mark(1000000);
			do {
				c = this.next();
				if(c == 0) {
					this.reader.reset();
					this.index = startIndex;
					this.character = startCharacter;
					this.line = startLine;
					return c;
				}
			} while (c != to);
		} catch (IOException exc) {
			throw new TXMLException(exc);
		}
		
		this.back();
		return c;
	}
	
	/**
	 * Make a TXMLException to signal a syntax error.
	 *
	 * @param message The error message.
	 * @return  A TXMLException object, suitable for throwing
	 */
	public TXMLException syntaxError(String message) {
		return new TXMLException(message + this.toString());
	}
	
	/**
	 * Make a printable string of this JSONTokener.
	 *
	 * @return " at {index} [character {character} line {line}]"
	 */
	public String toString() {
		return " at " + this.index + " [character " + this.character + " line " +
				this.line + "]";
	}
}
