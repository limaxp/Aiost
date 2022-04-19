package com.pm.aiost.server.messaging;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

public class AiostPluginMessageListener implements PluginMessageListener {

	public static void init(Plugin plugin) {
		Messenger messenger = plugin.getServer().getMessenger();
		AiostPluginMessageListener listener = new AiostPluginMessageListener();
		messenger.registerOutgoingPluginChannel(plugin, PluginMessage.CHANNEL_KEY);
		messenger.registerIncomingPluginChannel(plugin, PluginMessage.CHANNEL_KEY, listener);
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		if (channel.equals(PluginMessage.CHANNEL_KEY)) {
			ByteArrayDataInput in = ByteStreams.newDataInput(message);
			PluginMessages.get(in.readUTF()).accept(player, in);
		}
	}
}
