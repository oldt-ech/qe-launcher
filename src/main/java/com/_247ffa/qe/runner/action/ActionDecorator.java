package com._247ffa.qe.runner.action;

public abstract class ActionDecorator implements Action {
	protected Action action;
	protected String command;
	
	public ActionDecorator(Action action, String input) {
		this.action = action;
		this.command = input;
	}
	
	public void perform() {
		action.perform();
	};
}
