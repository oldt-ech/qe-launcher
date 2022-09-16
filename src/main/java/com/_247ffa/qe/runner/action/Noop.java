package com._247ffa.qe.runner.action;

import java.time.LocalDateTime;

public class Noop implements Action {

	@Override
	public void perform() {
		System.out.println(LocalDateTime.now().toString() + " - Running...");
	}
}
