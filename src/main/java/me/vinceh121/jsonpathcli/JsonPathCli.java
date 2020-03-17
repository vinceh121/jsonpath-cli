package me.vinceh121.jsonpathcli;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.EnumSet;
import java.util.Hashtable;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.json.JSONArray;
import org.json.JSONObject;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.InvalidPathException;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.JsonPathException;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JsonOrgJsonProvider;
import com.jayway.jsonpath.spi.json.JsonProvider;
import com.jayway.jsonpath.spi.mapper.JsonOrgMappingProvider;
import com.jayway.jsonpath.spi.mapper.MappingProvider;

import me.vinceh121.jsonpathcli.commands.CommandExit;
import me.vinceh121.jsonpathcli.commands.CommandHelp;
import me.vinceh121.jsonpathcli.commands.CommandOpen;
import me.vinceh121.jsonpathcli.commands.CommandPretty;

public class JsonPathCli {
	private final Hashtable<String, ICommand> commands = new Hashtable<String, ICommand>();
	private Configuration conf;
	private DocumentContext document;
	private boolean prettyPrint = true;

	public static void main(String[] args) {
		System.setErr(new PrintStream(new OutputStream() { // TODO change this ugliness
			@Override
			public void write(int b) throws IOException {
			}
		}));
		final JsonPathCli cli = new JsonPathCli(args);
		try {
			cli.startConsole();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public JsonPathCli(String[] args) {
		configureJsonPath();
		parseCommandLine(args);
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
		} catch (JsonPathException jpe) {
			System.out.println("JSONPath error: " + jpe.getLocalizedMessage());
			return;
		}

		if (result instanceof JSONObject) {
			final JSONObject js = (JSONObject) result;
			System.out.println(isPrettyPrint() ? js.toString(4) : js.toString());

		} else if (result instanceof JSONArray) {
			final JSONArray js = (JSONArray) result;
			System.out.println(isPrettyPrint() ? js.toString(4) : js.toString());

		} else if (result instanceof Number) {
			System.out.println(result);
		} else {
			System.out.println(result.toString());
			System.out.println("Result wasn't a json thing, printed anyway the " + result.getClass());
		}
	}

	private void configureJsonPath() {
		Configuration.setDefaults(new Configuration.Defaults() {

			private final JsonProvider jsonProvider = new JsonOrgJsonProvider();
			private final MappingProvider mappingProvider = new JsonOrgMappingProvider();

			@Override
			public JsonProvider jsonProvider() {
				return jsonProvider;
			}

			@Override
			public MappingProvider mappingProvider() {
				return mappingProvider;
			}

			@Override
			public Set<Option> options() {
				return EnumSet.noneOf(Option.class);
			}
		});
		this.conf = Configuration.defaultConfiguration();
	}

	private void processCommand(String command) {
		final String[] decomp = command.split(" ");
		final ICommand cmd = commands.get(decomp[0]);
		if (cmd == null) {
			System.out.println("Unknown command");
			return;
		}
		final String[] args = new String[decomp.length - 1];
		System.arraycopy(decomp, 1, args, 0, args.length);
		cmd.execute(args);
	}

	private void parseCommandLine(String[] args) {
		final Options opts = new Options();
		opts.addOption("h", "help", false, "Show CLI arguments help page");
		opts.addOption("o", "open", true, "Open a JSON file");
		final CommandLineParser parse = new DefaultParser();
		CommandLine cli = null;
		try {
			cli = parse.parse(opts, args);
		} catch (ParseException e) {
			System.out.println("Failed to parse CLI arguments: " + e.getLocalizedMessage());
			System.exit(-2);
		}

		if (cli.hasOption("h")) {
			final HelpFormatter help = new HelpFormatter();
			help.printHelp("JSONPath-CLI", opts);
			System.exit(0);
		}

		if (cli.hasOption("o")) {
			openDocument(cli.getOptionValue("o"));
		}
	}

	private void registerCommands() {
		registerCommand(new CommandOpen(this));
		registerCommand(new CommandHelp(this));
		registerCommand(new CommandPretty(this));
		registerCommand(new CommandExit(this));
	}

	public void openDocument(String path) {
		try {
			setDocument(JsonPath.parse(new File(path)));
			System.out.println("Opened file " + path);
		} catch (IOException e) {
			System.out.println("Failed to load: " + e.toString());
		}
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

	public Configuration getConfiguration() {
		return conf;
	}

}
