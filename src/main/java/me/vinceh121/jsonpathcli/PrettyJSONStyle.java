package me.vinceh121.jsonpathcli;

import java.io.IOException;

import net.minidev.json.JSONStyle;

public class PrettyJSONStyle extends JSONStyle {
	private int indent = 2, currentIndent = 0;
	private char indentChar = ' ';

	@Override
	public void objectStart(Appendable out) throws IOException {
		out.append("{\n");
		currentIndent++;
		out.append(getIndentStr());
	}

	@Override
	public void objectStop(Appendable out) throws IOException {
		currentIndent--;
		out.append("}\n");
		out.append(getIndentStr());
	}
	
	@Override
	public void objectNext(Appendable out) throws IOException {
		out.append(",\n");
		out.append(getIndentStr());
	}
	
	@Override
	public void objectEndOfKey(Appendable out) throws IOException {
		out.append(": ");
	}
	
	@Override
	public void arrayStart(Appendable out) throws IOException {
		out.append("[\n");
		currentIndent++;
		out.append(getIndentStr());
	}
	
	@Override
	public void arrayStop(Appendable out) throws IOException {
		currentIndent--;
		out.append("\n]");
		out.append(getIndentStr());
	}
	
	
	@Override
	public void arrayNextElm(Appendable out) throws IOException {
		out.append(",\n");
		out.append(getIndentStr());
	}

	private String getIndentStr() {
		final StringBuilder sb = new StringBuilder();
		for (int i = 0; i < indent + currentIndent; i++)
			sb.append(indentChar);
		return sb.toString();
	}

}
