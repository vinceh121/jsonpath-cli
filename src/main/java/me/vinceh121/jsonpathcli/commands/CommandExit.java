package me.vinceh121.jsonpathcli.commands;

import me.vinceh121.jsonpathcli.AbstractCommand;
import me.vinceh121.jsonpathcli.JsonPathCli;

public class CommandExit extends AbstractCommand {

	public CommandExit(JsonPathCli cli) {
		super(cli);
	}

	@Override
	public String getName() {
		return "exit";
	}

	@Override
	public void execute(String[] args) {
		System.out.println("Bye!");
		System.exit(0);
	}

}
