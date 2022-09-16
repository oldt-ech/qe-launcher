package com._247ffa.qe.runner.action;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.time.LocalDateTime;

public class Launcher extends ActionDecorator {

	public Launcher(Action action, String command) {
		super(action, command);
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

	private void multiplayerLobbyMenu(Robot robot) throws InterruptedException {
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

	private void multiplayerMatchSettingsMenu(Robot robot) throws InterruptedException {
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

	private void multiplayerCategoryMenu(Robot robot) throws InterruptedException {
		click(KeyEvent.VK_DOWN, robot);
		click(KeyEvent.VK_ENTER, robot);
	}

	private void multiplayerOnlineLocalMenu(Robot robot) throws InterruptedException {
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
