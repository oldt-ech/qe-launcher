package com._247ffa.qe.runner.model;

public class Server {
	private String id;
	private String name;
	private Session session;

	public Session getSession() {
		return session;
	}

	public Server setSession(Session session) {
		this.session = session;
		return this;
	}

	public String getId() {
		return id;
	}

	public Server setId(String id) {
		this.id = id;
		return this;
	}

	public String getName() {
		return name;
	}

	public Server setName(String name) {
		this.name = name;
		return this;
	}
}
