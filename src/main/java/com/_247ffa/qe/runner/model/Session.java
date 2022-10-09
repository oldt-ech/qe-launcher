package com._247ffa.qe.runner.model;

public class Session {

	private Session value;

	public Session getValue() {
		if (null == value) {
			return new Session();
		} else {
			return value;
		}
	}

	public void setValue(Session value) {
		this.value = value;
	}

	private int currentPlayers;
	private int maxPlayers;
	private String mapname;

	public int getCurrentPlayers() {
		return currentPlayers;
	}

	public void setCurrentPlayers(int currentPlayers) {
		this.currentPlayers = currentPlayers;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	public String getMapname() {
		return mapname;
	}

	public void setMapname(String mapname) {
		this.mapname = mapname;
	}
}
