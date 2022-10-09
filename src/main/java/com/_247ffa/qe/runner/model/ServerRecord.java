package com._247ffa.qe.runner.model;

import java.util.Date;
import java.util.UUID;

public class ServerRecord {

	private UUID requestUUID;
	private Date date;
	private int currentPlayers;
	
	public String getId() {
		return UUID.randomUUID().toString();
	}
	
	public String getMiniProfileId() {
		return miniProfileId;
	}

	public void setMiniProfileId(String miniProfileId) {
		this.miniProfileId = miniProfileId;
	}

	public String getMap() {
		return map;
	}

	public void setMap(String map) {
		this.map = map;
	}

	private int maxPlayers;
	private String name;
	private String miniProfileId;
	private String map;

	public UUID getRequestUUID() {
		return requestUUID;
	}

	public void setRequestUUID(UUID requestUUID) {
		this.requestUUID = requestUUID;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
