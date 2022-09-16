package com._247ffa.qe.runner.action;

import java.io.IOException;
import java.time.LocalDateTime;

public class OSCommand extends ActionDecorator {

	public OSCommand(Action action, String command) {
		super(action, command);
	}

	@Override
	public void perform() {
		super.perform();
		try {
			String timestamp = LocalDateTime.now().toString();
			String formattedCommand = command.replace("<TIMESTAMP>", timestamp.replace(':', '-'));
			Runtime.getRuntime().exec(formattedCommand);
			System.out.println(LocalDateTime.now().toString() + " - Running command: " + formattedCommand);
			Thread.sleep(10000);
		} catch (InterruptedException | IOException e) {
			System.err.println(LocalDateTime.now().toString() + " - Unable to run command " + command + ". Exception: "
					+ e.getMessage());
		}
	}

}
