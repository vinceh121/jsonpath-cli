package me.vinceh121.jsonpathcli.commands;

import me.vinceh121.jsonpathcli.AbstractCommand;
import me.vinceh121.jsonpathcli.JsonPathCli;

public class CommandPretty extends AbstractCommand {

	public CommandPretty(JsonPathCli cli) {
		super(cli);
	}

	@Override
	public String getName() {
		return "pretty";
	}

	@Override
	public void execute(String[] args) {
		if (args.length != 1) {
			format();
			return;
		}
		
		instance.setPrettyPrint(Boolean.parseBoolean(args[0]));
	}
	
	private void format() {
		System.out.println("/pretty <true/false>");
	}
	
	@Override
	public String getDescription() {
		return "Turn on or off query result pretty printing";
	}

}
