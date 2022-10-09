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
		int maxPlayers = 16;
		String validationEndpoint = "https://api.247ffa.com/api/v1/servers";
		String validationQuake = "C:\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe"
				+ " -skipmovies +g_showintromovie 0 +developer 1";
		Action action = new Noop();
		Map<String, String> sortedProperties = new TreeMap<String, String>();
		for (Entry<Object, Object> item : properties.entrySet()) {
			sortedProperties.put((String) item.getKey(), (String) item.getValue()); // ya it's gross :P
		}

		for (Entry<String, String> item : sortedProperties.entrySet()) {
			if (item.getKey().contains("quake->")) {
				action = new Launcher(action, item.getValue(), maxPlayers);
			} else if (item.getKey().contains("os->")) {
				action = new OSCommand(action, item.getValue());
			} else if (item.getKey().contains("setMaxPlayers->")) {
				maxPlayers = Integer.valueOf(item.getValue());
			} else if (item.getKey().contains("setValidationEndpoint->")) {
				validationEndpoint = item.getValue();
			} else if (item.getKey().contains("quakeIfDown->")) {
				// chain:
				// 1 - bring quake instance into focus by running with no args
				// 2 - type quit in console
				// 3 - launch again if down
				String commandNoArgs = validationQuake.substring(0, validationQuake.indexOf(".exe")+4);
				action = new Validator(action, validationEndpoint, item.getValue(), 
							new Launcher(
								new ConsoleTyper(new Noop(), commandNoArgs, "~quit"), 
							validationQuake, maxPlayers));
			} else if (item.getKey().contains("setValidationQuake->")) {
				validationQuake = item.getValue();
			}
		}
		return action;
	}
}
