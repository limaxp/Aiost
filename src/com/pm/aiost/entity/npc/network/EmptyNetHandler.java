package com.pm.aiost.entity.npc.network;

import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.MinecraftServer;
import net.minecraft.server.v1_15_R1.NetworkManager;
import net.minecraft.server.v1_15_R1.Packet;
import net.minecraft.server.v1_15_R1.PlayerConnection;

public class EmptyNetHandler extends PlayerConnection {

	public EmptyNetHandler(MinecraftServer minecraftServer, NetworkManager networkManager, EntityPlayer entityPlayer) {
		super(minecraftServer, networkManager, entityPlayer);
	}

	@Override
	public void sendPacket(Packet<?> packet) {
	}
}