package com.pm.aiost.misc.packet;

import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.nms.NMS;
import com.pm.aiost.player.ServerPlayer;

import io.netty.channel.Channel;
import net.minecraft.network.Connection;

public class PacketWatcher {

	private static final String PACKET_HANDLER_CHANNEL = "packet_handler";
	private static final String DECODER_CHANNEL = "message_redecoder";
	private static final String ENCODER_CHANNEL = "message_reencoder";

	public static void inject(ServerPlayer serverPlayer) {
		try {
			Connection connection = (Connection) NMS.SERVERCOMMONPACKETLISTENERIMPL_GET_CONNECTION
					.invoke(NMS.to(serverPlayer.player).connection);
			Channel channel = connection.channel;
			if (channel != null) {
				channel.pipeline().addBefore(PACKET_HANDLER_CHANNEL, DECODER_CHANNEL,
						new AiostPacketDecoder(serverPlayer));
				channel.pipeline().addBefore(PACKET_HANDLER_CHANNEL, ENCODER_CHANNEL,
						new AiostPacketEncoder(serverPlayer));
			}
		} catch (Throwable e) {
			Logger.err("PacketWatcher: Error on netty injection !", e);
		}
	}

	public static void eject(ServerPlayer serverPlayer) {
		try {
			Connection connection = (Connection) NMS.SERVERCOMMONPACKETLISTENERIMPL_GET_CONNECTION
					.invoke(NMS.to(serverPlayer.player).connection);
			Channel channel = connection.channel;
			if (channel != null) {
				if (channel.pipeline().get(DECODER_CHANNEL) != null)
					channel.pipeline().remove(DECODER_CHANNEL);
				if (channel.pipeline().get(ENCODER_CHANNEL) != null)
					channel.pipeline().remove(ENCODER_CHANNEL);
			}
		} catch (Throwable e) {
			Logger.err("PacketWatcher: Error on netty ejection !", e);
		}
	}
}
