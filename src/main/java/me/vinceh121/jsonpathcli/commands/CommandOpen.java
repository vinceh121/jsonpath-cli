package me.vinceh121.jsonpathcli.commands;

import java.io.File;
import java.io.IOException;

import com.jayway.jsonpath.JsonPath;

import me.vinceh121.jsonpathcli.AbstractCommand;
import me.vinceh121.jsonpathcli.JsonPathCli;

public class CommandOpen extends AbstractCommand {

	public CommandOpen(JsonPathCli cli) {
		super(cli);
	}

	public String getName() {
		return "open";
	}

	public void execute(String[] args) {
		final String path = joinArgs(args);

		try {
			instance.setDocument(JsonPath.parse(new File(path)));
			System.out.println("Opened file " + path);
		} catch (IOException e) {
			System.out.println("Failed to load: " + e.toString());
		}

	}

	@Override
	public String getDescription() {
		return "Opens a JSON document by file path";
	}
	
}
