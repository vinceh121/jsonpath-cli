package me.vinceh121.jsonpathcli;

public abstract class AbstractCommand implements ICommand {
	protected JsonPathCli instance;

	public AbstractCommand(JsonPathCli cli) {
		this.instance = cli;
	}

	public String joinArgs(String[] args) {
		return String.join(" ", args);
	}
}
