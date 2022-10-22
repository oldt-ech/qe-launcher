package com._247ffa.qe.runner;

public class Settings {
	private int maxPlayers = 16;
	private String validationEndpoint = "https://api.247ffa.com/api/v1/servers";
	private String validationQuake = "C:\\Program Files (x86)\\Steam\\steamapps\\common\\Quake\\rerelease\\Quake_x64_steam.exe"
			+ " -skipmovies +g_showintromovie 0 +developer 1";
	private int gameModeIndex = 2;

	public int getGameModeIndex() {
		return gameModeIndex;
	}

	public void setGameModeIndex(int gameTypeIndex) {
		this.gameModeIndex = gameTypeIndex;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public String getValidationEndpoint() {
		return validationEndpoint;
	}

	public void setValidationEndpoint(String validationEndpoint) {
		this.validationEndpoint = validationEndpoint;
	}

	public String getValidationQuake() {
		return validationQuake;
	}

	public void setValidationQuake(String validationQuake) {
		this.validationQuake = validationQuake;
	}

}
