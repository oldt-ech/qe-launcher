package com._247ffa.qe.runner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;

import com._247ffa.qe.runner.action.Action;
import com._247ffa.qe.runner.action.ConsoleTyper;
import com._247ffa.qe.runner.action.Launcher;
import com._247ffa.qe.runner.action.Noop;
import com._247ffa.qe.runner.action.OSCommand;
import com._247ffa.qe.runner.action.Validator;

public class Main {

	private final String defaultProperties;

	public Main() {
		defaultProperties = "default.properties";
	}

	public static void main(String[] args) throws FileNotFoundException, IOException {
		Main main = new Main();
		Properties properties = main.loadProperties();
		Action action = main.parseAction(properties);
		action.perform();
	}

	protected Properties loadProperties() throws FileNotFoundException, IOException {
		Properties properties = new Properties();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		String props = System.getProperty("props");
		if (props == null) {
			properties.load(classLoader.getResourceAsStream(defaultProperties));
		} else {
			properties.load(new FileInputStream(props));
		}

		return properties;
	}

	protected Action parseAction(Properties properties) {
		Settings settings = new Settings();
		Action action = new Noop();
		boolean online = false;
		Map<String, String> sortedProperties = new TreeMap<String, String>();
		for (Entry<Object, Object> item : properties.entrySet()) {
			sortedProperties.put((String) item.getKey(), (String) item.getValue());
		}

		for (Entry<String, String> item : sortedProperties.entrySet()) {
			if (item.getKey().contains("quake->")) {
				action = new Launcher(action, item.getValue(), settings.getMaxPlayers(), settings.getGameModeIndex());
			} else if (item.getKey().contains("os->")) {
				action = new OSCommand(action, item.getValue());
			} else if (item.getKey().contains("setMaxPlayers->")) {
				settings.setMaxPlayers(Integer.valueOf(item.getValue()));
			} else if (item.getKey().contains("setGameModeIndex->")) {
				settings.setGameModeIndex(Integer.valueOf(item.getValue()));
			} else if (item.getKey().contains("exeConfig->")) {
				action = new ConsoleTyper(action, item.getValue(), "exec qelauncher.cfg");
			} else if (item.getKey().contains("ifDownDetermine->")) {
				online = new ServerOnlineChecker(item.getValue()).check();
			} else if (item.getKey().contains("ifDownQuake->")) {
				if (!online) {
					action = new Launcher(action, item.getValue(), settings.getMaxPlayers(),
							settings.getGameModeIndex());
				}
			} else if (item.getKey().contains("ifDownOs->")) {
				if (!online) {
					action = new OSCommand(action, item.getValue());
				}
			} else if (item.getKey().contains("ifDownExeDownConfig->")) {
				if (!online) {
					action = new ConsoleTyper(action, item.getValue(), "exec qelauncherdown.cfg");
				}
			} else if (item.getKey().contains("ifDownExeConfig->")) {
				if (!online) {
					action = new ConsoleTyper(action, item.getValue(), "exec qelauncher.cfg");
				}
			}

			// deprecated stuff - 3.8. This was too tightly coupled and complicated. Control
			// logic shouldn't be in action chain.
			else if (item.getKey().contains("setValidationEndpoint->")) {
				settings.setValidationEndpoint(item.getValue());
			} else if (item.getKey().contains("quakeIfDown->")) {
				// chain:
				// 1 - bring quake instance into focus by running with no args
				// 2 - type quit in console
				// 3 - launch again if down
				String validationQuake = settings.getValidationQuake();
				String commandNoArgs = validationQuake.substring(0, validationQuake.indexOf(".exe") + 4);
				action = new Validator(action, settings.getValidationEndpoint(), item.getValue(),
						new Launcher(new ConsoleTyper(new Noop(), commandNoArgs, "~quit"), validationQuake,
								settings.getMaxPlayers(), settings.getGameModeIndex()));
			} else if (item.getKey().contains("setValidationQuake->")) {
				settings.setValidationEndpoint(item.getValue());
			}
		}
		return action;
	}
}
