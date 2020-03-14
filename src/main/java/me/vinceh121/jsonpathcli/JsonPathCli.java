package me.vinceh121.jsonpathcli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Hashtable;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.InvalidPathException;

import me.vinceh121.jsonpathcli.commands.CommandHelp;
import me.vinceh121.jsonpathcli.commands.CommandOpen;
import me.vinceh121.jsonpathcli.commands.CommandPretty;
import net.minidev.json.JSONAwareEx;
import net.minidev.json.JSONStyle;

public class JsonPathCli {
	private final Hashtable<String, ICommand> commands = new Hashtable<String, ICommand>();
	private final JSONStyle stylePrettyPrint = new PrettyJSONStyle();
	private final JSONStyle styleCompressed = JSONStyle.MAX_COMPRESS;
	private DocumentContext document;
	private boolean prettyPrint = true;

	public static void main(String[] args) {
		final JsonPathCli cli = new JsonPathCli();
		try {
			cli.startConsole();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public JsonPathCli() {
		registerCommands();
	}

	private void startConsole() throws IOException {
		System.out.println("JSONPath-CLI by vinceh121\n");
		System.out.println("To show the help page: /help");
		final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		for (;;) {
			System.out.print("> ");
			final String input = br.readLine();
			if (input == null)
				return;
			processInput(input);
		}
	}

	public void processInput(String input) {
		if (input.startsWith("/")) {
			processCommand(input.substring(1));
		} else {
			processJsonPath(input);
		}
	}

	public void registerCommand(ICommand cmd) {
		this.commands.put(cmd.getName(), cmd);
	}

	public void processJsonPath(String path) {
		if (document == null) {
			System.out.println("No document is loaded");
			return;
		}

		final Object result;

		try {
			result = document.read(path);
		} catch (InvalidPathException pe) {
			System.out.println("Invalid path: " + pe.getLocalizedMessage());
			return;
		}

		if (result instanceof JSONAwareEx) {
			final JSONAwareEx js = (JSONAwareEx) result;
			System.out.println(js.toJSONString(getStyleToUse()));
		} else {
			System.out.println(result.toString());
			System.err.println("Result wasn't a json thing, printed anyway the " + result.getClass());
		}
	}

	private JSONStyle getStyleToUse() {
		return isPrettyPrint() ? stylePrettyPrint : styleCompressed;
	}

	private void processCommand(String command) {
		final String[] decomp = command.split(" ");
		final ICommand cmd = commands.get(decomp[0]);
		final String[] args = new String[decomp.length - 1];
		System.arraycopy(decomp, 1, args, 0, args.length);
		cmd.execute(args);
	}

	private void registerCommands() {
		registerCommand(new CommandOpen(this));
		registerCommand(new CommandHelp(this));
		registerCommand(new CommandPretty(this));
	}

	public DocumentContext getDocument() {
		return document;
	}

	public void setDocument(DocumentContext document) {
		this.document = document;
	}

	public boolean isPrettyPrint() {
		return prettyPrint;
	}

	public void setPrettyPrint(boolean prettyPrint) {
		this.prettyPrint = prettyPrint;
	}

	public ICommand[] getRegisteredCommands() {
		return commands.values().toArray(new ICommand[commands.size()]);
	}

}
