package com._247ffa.qe.runner;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class Main {
	private static String DEFAULT_PATH = "C:/Program Files (x86)/Steam/steamapps/common"
			+ "/Quake/rerelease/Quake_x64_steam.exe -skipmovies +g_showintromovie 0 +developer 1";

	public static void main(String[] args) {
		Optional<String> propertiesPath = Optional.empty();
		if (1 == Array.getLength(args)) {
			propertiesPath = Optional.of(args[0]);
		}

		// kill any crashed / frozen / unresponsive servers
		new Killer().kill();

		// launch all servers and go to default multiplayer game
		loadCommands(propertiesPath).forEach((cmd) -> {
			new Launcher(cmd).launch();
		});
	}

	public static List<String> loadCommands(Optional<String> config) {
		List<String> commands = new ArrayList<>();
		config.ifPresentOrElse(value -> {
			Properties properties = new Properties();
			try {
				properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(config.get()));
			} catch (IOException e) {
				throw new RuntimeException("Can't load properties");
			}
			properties.forEach((k, v) -> commands.add((String) v));
		}, () -> {
			commands.add(DEFAULT_PATH);
		});
		return commands;
	}
}
