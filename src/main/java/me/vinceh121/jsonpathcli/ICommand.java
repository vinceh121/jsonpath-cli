package me.vinceh121.jsonpathcli;

public interface ICommand {
	String getName();

	void execute(String[] args);
	
	default String getDescription() {
		return "no description provided";
	}

}
