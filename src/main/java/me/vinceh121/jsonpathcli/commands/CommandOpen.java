package me.vinceh121.jsonpathcli.commands;

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

		instance.openDocument(path);
	}

	@Override
	public String getDescription() {
		return "Opens a JSON document by file path";
	}
	
}
