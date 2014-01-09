package com.kill3rtaco.txml;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TXML {
	
	public static final int	INDENT_FACTOR	= 2;
	
	private TXML() {
	}
	
	/**
	 * Get the contents of a file
	 * @param file The file to read from
	 * @return The files contents
	 * @throws IllegalArgumentException If the given file is {@code null}, is a directory, or does not exist.
	 */
	public static String stringFromFile(File file) {
		if(file == null) {
			throw new IllegalArgumentException("The given file cannot be null");
		} else if(file.isDirectory()) {
			throw new IllegalArgumentException("The given file cannot be a directory");
		} else if(!file.exists()) {
			throw new IllegalArgumentException("The given file does not exist");
		}
		String source = "";
		try {
			Scanner x = new Scanner(file);
			while (x.hasNext()) {
				source += x.next() + " ";
			}
			source = source.trim();
			x.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return null;
		}
		return source;
	}
	
}
