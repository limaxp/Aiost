package com.pm.aiost.misc.packet.listen;

import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;

import com.pm.aiost.player.ServerPlayer;

import io.netty.channel.Channel;

public class PacketWatcher {

	private static final String PACKET_HANDLER_CHANNEL = "packet_handler";
	private static final String DECODER_CHANNEL = "message_redecoder";
	private static final String ENCODER_CHANNEL = "message_reencoder";

	public static void inject(ServerPlayer serverPlayer) {
		Channel channel = ((CraftPlayer) serverPlayer.player).getHandle().playerConnection.networkManager.channel;
		if (channel != null) {
			channel.pipeline().addBefore(PACKET_HANDLER_CHANNEL, DECODER_CHANNEL, new AiostPacketDecoder(serverPlayer));
			channel.pipeline().addBefore(PACKET_HANDLER_CHANNEL, ENCODER_CHANNEL, new AiostPacketEncoder(serverPlayer));
		}
	}

	public static void eject(ServerPlayer serverPlayer) {
		Channel channel = ((CraftPlayer) serverPlayer.player).getHandle().playerConnection.networkManager.channel;
		if (channel != null) {
			if (channel.pipeline().get(DECODER_CHANNEL) != null)
				channel.pipeline().remove(DECODER_CHANNEL);
			if (channel.pipeline().get(ENCODER_CHANNEL) != null)
				channel.pipeline().remove(ENCODER_CHANNEL);
		}
	}
}
