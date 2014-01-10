package com.kill3rtaco.txml;

public class TXMLTester {
	
	public static void main(String[] args) {
		XMLDocument doc = new XMLDocument();
		XMLNode node = doc.addNode("rt").setAttribute("bleep", "bloop").setAttribute("bloop", "bleep");
		XMLNode n = node.addNode(new XMLNode("rawr"));
		n.setText("text");
		System.out.println(doc.toString());
	}
}
