package com._247ffa.qe.runner.action;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

public class ConsoleTyper extends ActionDecorator {

	protected final String input;

	public ConsoleTyper(Action action, String command, String input) {
		super(action, command);
		this.input = input;
	}

	protected void launchDelay() throws InterruptedException {
		Optional<String> delay = Optional.ofNullable(System.getProperty("debugLaunchDelay"))
				.or(() -> Optional.of("60000"));
		Thread.sleep(Integer.valueOf(delay.get()));
	}

	protected void menuDelay(Robot robot) throws InterruptedException {
		Optional<String> delay = Optional.ofNullable(System.getProperty("debugMenuDelay"))
				.or(() -> Optional.of("10000"));
		Thread.sleep(Integer.valueOf(delay.get()));
	}

	protected void type() {
		try {
			Runtime run = Runtime.getRuntime();
			Robot robot;
			robot = new Robot();
			run.exec(command);
			
			// let any movies play out (this has to be launched with no args to bring into focus)
			launchDelay();

			// clear out anything blocking console (the bethesda modal screens can block us here)
			click(KeyEvent.VK_ESCAPE, robot);
			
			// let anything blocking the console go away
			menuDelay(robot); 
			
			for (int i = 0; i < input.length(); i++) {
				if (input.charAt(i) == '~') {
					robot.keyPress(KeyEvent.VK_SHIFT);
					click(getEvent(input.charAt(i)), robot);
					robot.keyRelease(KeyEvent.VK_SHIFT);
				} else {
					click(getEvent(input.charAt(i)), robot);
				}
			}

			click(KeyEvent.VK_ENTER, robot);
			click(KeyEvent.VK_ESCAPE, robot);
		} catch (IOException | AWTException | InterruptedException e) {
			System.err.println(LocalDateTime.now().toString() + " - Couldn't type " + input + ". Exception " + e);
		}
	}

	protected int getEvent(char character) {
		switch (character) {
		case '~':
			return KeyEvent.VK_BACK_QUOTE;
		case 'a':
			return KeyEvent.VK_A;
		case 'b':
			return KeyEvent.VK_B;
		case 'c':
			return KeyEvent.VK_C;
		case 'd':
			return KeyEvent.VK_D;
		case 'e':
			return KeyEvent.VK_E;
		case 'f':
			return KeyEvent.VK_F;
		case 'g':
			return KeyEvent.VK_G;
		case 'h':
			return KeyEvent.VK_H;
		case 'i':
			return KeyEvent.VK_I;
		case 'j':
			return KeyEvent.VK_J;
		case 'k':
			return KeyEvent.VK_K;
		case 'l':
			return KeyEvent.VK_L;
		case 'm':
			return KeyEvent.VK_M;
		case 'n':
			return KeyEvent.VK_N;
		case 'o':
			return KeyEvent.VK_O;
		case 'p':
			return KeyEvent.VK_P;
		case 'q':
			return KeyEvent.VK_Q;
		case 'r':
			return KeyEvent.VK_R;
		case 's':
			return KeyEvent.VK_S;
		case 't':
			return KeyEvent.VK_T;
		case 'u':
			return KeyEvent.VK_U;
		case 'v':
			return KeyEvent.VK_V;
		case 'w':
			return KeyEvent.VK_W;
		case 'x':
			return KeyEvent.VK_X;
		case 'y':
			return KeyEvent.VK_Y;
		case 'z':
			return KeyEvent.VK_Z;
		case '1':
			return KeyEvent.VK_1;
		case '2':
			return KeyEvent.VK_2;
		case '3':
			return KeyEvent.VK_3;
		case '4':
			return KeyEvent.VK_4;
		case '5':
			return KeyEvent.VK_5;
		case '6':
			return KeyEvent.VK_6;
		case '7':
			return KeyEvent.VK_7;
		case '8':
			return KeyEvent.VK_8;
		case '9':
			return KeyEvent.VK_9;		
		case '*':
			return KeyEvent.VK_ASTERISK;
		case ' ':
			return KeyEvent.VK_SPACE;
		case '_':
			return KeyEvent.VK_UNDERSCORE;
		case '.':
			return KeyEvent.VK_PERIOD;
		case '+':
			return KeyEvent.VK_PLUS;
		}
		return character;
	}

	protected void click(int event, Robot robot) throws InterruptedException {
		robot.keyPress(event);
		Thread.sleep(250);
		robot.keyRelease(event);
		Thread.sleep(500);
	}

	@Override
	public void perform() {
		super.perform();
		System.out.println(LocalDateTime.now().toString() + " - Typing " + input);
		type();
	}
}
