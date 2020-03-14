package me.vinceh121.jsonpathcli.commands;

import me.vinceh121.jsonpathcli.AbstractCommand;
import me.vinceh121.jsonpathcli.ICommand;
import me.vinceh121.jsonpathcli.JsonPathCli;

public class CommandHelp extends AbstractCommand {

	public CommandHelp(JsonPathCli cli) {
		super(cli);
	}

	@Override
	public String getName() {
		return "help";
	}

	@Override
	public void execute(String[] args) {
		System.out.println("JSONPath-CLI uses a (almost) Sqlite-style CLI interface;"
				+ "to execute a special command, preceed it with '/' and to make a JSONPath query, type it directly.\n"
				+ "\tCommand list:\n");

		for (ICommand cmd : instance.getRegisteredCommands()) {
			System.out.println("\t" + cmd.getName() + "\t" + cmd.getDescription());
		}
		System.out.println();
	}

	@Override
	public String getDescription() {
		return "duh";
	}
}
