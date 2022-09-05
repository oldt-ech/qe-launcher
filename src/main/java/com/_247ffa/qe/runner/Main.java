package com._247ffa.qe.runner;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class Main {

	private static final String DEFAULT_PROPERTIES = "default.properties";
	private static final String BUNDLED_FFA_PROPERTIES = "247ffa.properties";
	
	public static void main(String[] args) throws FileNotFoundException, IOException {
		// kill any crashed / frozen / unresponsive servers
		new Killer().kill();

		// launch all servers and go to default multiplayer game
		loadCommands().forEach((cmd) -> {
			new Launcher(cmd).launch();
		});
	}

	public static List<String> loadCommands() throws FileNotFoundException, IOException {
		List<String> commands = new ArrayList<>();
		Properties properties = new Properties();
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		String props = System.getProperty("props");
		if (props == null) {
			properties.load(classLoader.getResourceAsStream(DEFAULT_PROPERTIES));
		} else if (BUNDLED_FFA_PROPERTIES.equals(props)) {
			properties.load(classLoader.getResourceAsStream(BUNDLED_FFA_PROPERTIES));
		} else {
			properties.load(new FileInputStream(props));
		}
		properties.forEach((k, v) -> commands.add((String) v));
		return commands;
	}
}
