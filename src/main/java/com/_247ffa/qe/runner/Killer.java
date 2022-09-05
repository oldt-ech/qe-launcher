package com._247ffa.qe.runner;

import java.io.IOException;

public class Killer {
	public void kill() {
		try {
			Runtime.getRuntime().exec("taskkill /F /IM Quake_x64_steam.exe /T");
			try {
				Thread.sleep(10000); // let steam cloud sync
			} catch (InterruptedException e) {
				throw new RuntimeException("Could not kill server", e);
			}
		} catch (IOException e) {
			throw new RuntimeException("Could not kill Quake servers", e);
		}
	}
}
