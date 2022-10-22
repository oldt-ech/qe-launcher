package com._247ffa.qe.runner.model;

import java.util.List;

public class ServerOnlineInfoReport {
	private String description;
	private List<ServerOnlineInfo> items;

	public ServerOnlineInfoReport() {
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<ServerOnlineInfo> getItems() {
		return items;
	}

	public void setItems(List<ServerOnlineInfo> items) {
		this.items = items;
	}

}
