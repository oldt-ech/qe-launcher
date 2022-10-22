package com._247ffa.qe.runner.model;

public class ServerOnlineInfo {
	private int serversOnline;
	private long time;

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getServersOnline() {
		return serversOnline;
	}

	public void setServersOnline(float serversOnline) {
		this.serversOnline = (int)serversOnline;
	}
}
