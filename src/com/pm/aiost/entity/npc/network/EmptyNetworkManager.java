package com.pm.aiost.entity.npc.network;

import java.io.IOException;
import java.net.SocketAddress;

import io.netty.util.concurrent.GenericFutureListener;
import net.minecraft.server.v1_15_R1.EnumProtocolDirection;
import net.minecraft.server.v1_15_R1.NetworkManager;
import net.minecraft.server.v1_15_R1.Packet;

public class EmptyNetworkManager extends NetworkManager {
	public EmptyNetworkManager(EnumProtocolDirection flag) throws IOException {
		super(flag);
		initNetworkManager(this);
	}

	@Override
	public boolean isConnected() {
		return true;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void sendPacket(Packet packet, GenericFutureListener genericfuturelistener) {
	}

	private static void initNetworkManager(NetworkManager network) {
		network.channel = new EmptyChannel(null);
		SocketAddress socketAddress = new SocketAddress() {
			private static final long serialVersionUID = 8207338859896320185L;
		};
		network.socketAddress = socketAddress;
	}
}