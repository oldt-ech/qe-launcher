package com._247ffa.qe.runner;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class Launcher {
	protected final String command;

	public Launcher(String command) {
		this.command = command;
	}

	// todo: should really check stdout for specific strings
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
		} catch (IOException e) {
			throw new RuntimeException("Couldn't find quake exe!", e);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		} catch (AWTException e) {
			throw new RuntimeException(e);
		}
	}

	private void multiplayerLobbyMenu(final Robot robot) throws InterruptedException {
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

	private void multiplayerMatchSettingsMenu(final Robot robot) throws InterruptedException {
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

	private void multiplayerCategoryMenu(final Robot robot) throws InterruptedException {
		click(KeyEvent.VK_DOWN, robot);
		click(KeyEvent.VK_ENTER, robot);
	}

	private void multiplayerOnlineLocalMenu(final Robot robot) throws InterruptedException {
		click(KeyEvent.VK_ENTER, robot);
	}

	protected void mainMenu(final Robot robot) throws InterruptedException {
		click(KeyEvent.VK_DOWN, robot);
		click(KeyEvent.VK_ENTER, robot);
	}

	protected void init(Robot robot) throws InterruptedException {
		click(KeyEvent.VK_ESCAPE, robot);
	}
	
	protected void click(final int event, final Robot robot) throws InterruptedException {
		robot.keyPress(event);
		Thread.sleep(100);
		robot.keyRelease(event);
		Thread.sleep(500);
	}
}
