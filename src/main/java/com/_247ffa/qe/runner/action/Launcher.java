package com._247ffa.qe.runner.action;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.time.LocalDateTime;

public class Launcher extends ActionDecorator {

	protected final int maxPlayers;
	
	public Launcher(Action action, String command, int maxPlayers) {
		super(action, command);
		this.maxPlayers = maxPlayers;
	}
	
	protected void menuDelay(Robot robot) throws InterruptedException {
		Thread.sleep(10000);
	}

	protected void launch() {
		try {
			Runtime run = Runtime.getRuntime();
			Robot robot;
			robot = new Robot();
			run.exec(command);

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
		click(KeyEvent.VK_DOWN, robot);
		click(KeyEvent.VK_DOWN, robot);
		click(KeyEvent.VK_DOWN, robot);
		click(KeyEvent.VK_DOWN, robot);
		click(KeyEvent.VK_DOWN, robot);
		click(KeyEvent.VK_DOWN, robot);
		click(KeyEvent.VK_DOWN, robot);
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
