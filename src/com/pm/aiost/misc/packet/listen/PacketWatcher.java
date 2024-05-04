package com.pm.aiost.misc.packet.listen;

import java.lang.invoke.MethodHandle;

import com.pm.aiost.misc.log.Logger;
import com.pm.aiost.misc.utils.nms.NMS;
import com.pm.aiost.misc.utils.reflection.ReflectionUtils;
import com.pm.aiost.player.ServerPlayer;

import io.netty.channel.Channel;
import net.minecraft.network.Connection;
import net.minecraft.server.network.ServerCommonPacketListenerImpl;

public class PacketWatcher {

	private static final String PACKET_HANDLER_CHANNEL = "packet_handler";
	private static final String DECODER_CHANNEL = "message_redecoder";
	private static final String ENCODER_CHANNEL = "message_reencoder";

	private static final MethodHandle CONNECTION_GET;

	static {
		try {
			CONNECTION_GET = ReflectionUtils.unreflectGetter(ServerCommonPacketListenerImpl.class, "connection");
		} catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
			Logger.err("PacketWatcher: Error on nms field reflection!", e);
			throw new RuntimeException();
		}
	}

	public static void inject(ServerPlayer serverPlayer) {
		try {
			Connection connection = (Connection) CONNECTION_GET.invoke(NMS.getNMS(serverPlayer.player).connection);
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
			Connection connection = (Connection) CONNECTION_GET.invoke(NMS.getNMS(serverPlayer.player).connection);
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
