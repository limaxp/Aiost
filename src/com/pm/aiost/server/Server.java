package com.pm.aiost.server;

public class Server {

	private int id;
	private String name;
	private ServerType type;
	private ServerState state;

	Server(org.bukkit.Server bukkitServer) {
		this(-1, bukkitServer.getMotd(), null, null);
	}

	public Server(int id, String name, ServerType type, ServerState state) {
		this.id = id;
		this.name = name;
		this.type = type;
		this.state = state;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	void setType(ServerType type) {
		this.type = type;
	}

	public ServerType getType() {
		return type;
	}

	public ServerState getState() {
		return state;
	}
}
