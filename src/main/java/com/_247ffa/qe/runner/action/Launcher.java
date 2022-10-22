package com._247ffa.qe.runner.action;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

public class Launcher extends ActionDecorator {

	protected final int maxPlayers;
	protected final int gameModeIndex;

	public Launcher(Action action, String command, int maxPlayers, int gameModeIndex) {
		super(action, command);
		this.maxPlayers = maxPlayers;
		this.gameModeIndex = gameModeIndex;
	}

	protected void launchDelay() throws InterruptedException {
		Optional<String> delay = Optional.ofNullable(System.getProperty("debugLaunchDelay"))
				.or(() -> Optional.of("30000"));
		Thread.sleep(Integer.valueOf(delay.get()));
	}

	protected void menuDelay(Robot robot) throws InterruptedException {
		Optional<String> delay = Optional.ofNullable(System.getProperty("debugMenuDelay"))
				.or(() -> Optional.of("10000"));
		Thread.sleep(Integer.valueOf(delay.get()));
	}

	protected void launch() {
		try {
			launchDelay();
			Runtime run = Runtime.getRuntime();
			Robot robot;
			robot = new Robot();
			run.exec(command);
			launchDelay();

			menuDelay(robot);
			menuDelay(robot);
			menuDelay(robot);
			init(robot);
			menuDelay(robot);
			mainMenu(robot);
			menuDelay(robot);
			multiplayerOnlineLocalMenu(robot);
			menuDelay(robot);
			multiplayerCategoryMenu(robot);
			menuDelay(robot);
			multiplayerMatchSettingsMenu(robot);
			menuDelay(robot);
			multiplayerLobbyMenu(robot);
		} catch (IOException | InterruptedException | AWTException e) {
			System.err.println(LocalDateTime.now().toString() + " - Couldn't launch Quake. Exception " + e);
		}
	}

	protected void multiplayerLobbyMenu(Robot robot) throws InterruptedException {
		click(KeyEvent.VK_DOWN, robot);
		click(KeyEvent.VK_DOWN, robot);
		click(KeyEvent.VK_DOWN, robot);
		click(KeyEvent.VK_DOWN, robot);
		click(KeyEvent.VK_DOWN, robot);
		click(KeyEvent.VK_DOWN, robot);
		click(KeyEvent.VK_DOWN, robot);
		click(KeyEvent.VK_DOWN, robot);
		click(KeyEvent.VK_DOWN, robot);
		click(KeyEvent.VK_DOWN, robot);
		click(KeyEvent.VK_UP, robot);
		click(KeyEvent.VK_ENTER, robot);
	}

	protected void multiplayerMatchSettingsMenu(Robot robot) throws InterruptedException {
		click(KeyEvent.VK_UP, robot);
		setMaxPlayers(robot);
		click(KeyEvent.VK_DOWN, robot);
		click(KeyEvent.VK_DOWN, robot);
		click(KeyEvent.VK_DOWN, robot);
		setGameMode(robot);
		for (int i = 0; i < 15; i++) {
			// future proofing a little bit... changing game mode changes number of down
			// arrow presses to get to 'Play'
			// so send more than needed
			click(KeyEvent.VK_DOWN, robot);
		}
		click(KeyEvent.VK_ENTER, robot);
	}

	protected void setGameMode(Robot robot) throws InterruptedException {
		click(KeyEvent.VK_ENTER, robot);
		click(KeyEvent.VK_UP, robot); // starts at offset 2, go to top
		for (int i = 1; i < gameModeIndex; i++) {
			click(KeyEvent.VK_DOWN, robot);
		}
		click(KeyEvent.VK_ENTER, robot);
	}

	protected void setMaxPlayers(Robot robot) throws InterruptedException {
		for (int i = 0; i < 8; i++) {
			click(KeyEvent.VK_LEFT, robot);
		}
		int initialOffset = 2;
		for (int i = 0; i < maxPlayers - initialOffset; i++) {
			click(KeyEvent.VK_RIGHT, robot);
		}
	}

	protected void multiplayerCategoryMenu(Robot robot) throws InterruptedException {
		click(KeyEvent.VK_DOWN, robot);
		click(KeyEvent.VK_ENTER, robot);
	}

	protected void multiplayerOnlineLocalMenu(Robot robot) throws InterruptedException {
		click(KeyEvent.VK_ENTER, robot);
	}

	protected void mainMenu(Robot robot) throws InterruptedException {
		click(KeyEvent.VK_DOWN, robot);
		click(KeyEvent.VK_ENTER, robot);
	}

	protected void init(Robot robot) throws InterruptedException {
		click(KeyEvent.VK_ESCAPE, robot);
	}

	protected void click(int event, Robot robot) throws InterruptedException {
		robot.keyPress(event);
		Thread.sleep(100);
		robot.keyRelease(event);
		Thread.sleep(500);
	}

	@Override
	public void perform() {
		super.perform();
		System.out.println(LocalDateTime.now().toString() + " - Launching " + command);
		launch();
	}
}
