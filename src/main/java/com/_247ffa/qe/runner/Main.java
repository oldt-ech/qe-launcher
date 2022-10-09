package com._247ffa.qe.runner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.TreeMap;

import com._247ffa.qe.runner.action.Action;
import com._247ffa.qe.runner.action.OSCommand;
import com._247ffa.qe.runner.action.Launcher;
import com._247ffa.qe.runner.action.Noop;

public class Main {

	private final String defaultProperties;
	private final String bundledFFAProperties;

	public Main() {
		defaultProperties = "default.properties";
		bundledFFAProperties = "247ffa.properties";
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
		} else if (bundledFFAProperties.equals(props)) {
			properties.load(classLoader.getResourceAsStream(bundledFFAProperties));
		} else {
			properties.load(new FileInputStream(props));
		}

		return properties;
	}

	protected Action parseAction(Properties properties) {
		int maxPlayers = 16;
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
				System.out.println(LocalDateTime.now().toString() + " - Setting max players to " + maxPlayers);
			}
		}
		return action;
	}
}
