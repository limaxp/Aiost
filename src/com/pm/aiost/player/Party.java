package com.pm.aiost.player;

import java.util.Collections;
import java.util.List;

import com.pm.aiost.collection.list.UnorderedIdentityArrayList;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Party {

	public static final Object PARTY_INVITE_COOLDOWN = new Object();
	public static final int MAX_PARTY_SIZE = 4;

	private final List<ServerPlayer> member;

	public Party(ServerPlayer owner) {
		member = new UnorderedIdentityArrayList<ServerPlayer>(MAX_PARTY_SIZE);
		addMember(owner);
	}

	void addMember(ServerPlayer serverPlayer) {
		if (size() < MAX_PARTY_SIZE)
			member.add(serverPlayer);
	}

	void removeMember(ServerPlayer serverPlayer) {
		member.remove(serverPlayer);
	}

	public List<ServerPlayer> getMember() {
		return Collections.unmodifiableList(member);
	}

	public boolean contains(ServerPlayer serverPlayer) {
		return member.contains(serverPlayer);
	}

	public ServerPlayer getOwner() {
		return member.get(0);
	}

	public boolean isOwner(ServerPlayer serverPlayer) {
		return getOwner() == serverPlayer;
	}

	public int size() {
		return member.size();
	}

	public static void sendRequest(ServerPlayer serverPlayer, String requestPlayerName) {
		TextComponent message = new TextComponent(requestPlayerName + " has invited you to his party!\n");
		message.setBold(true);
		TextComponent accept = new TextComponent("[Accept] ");
		accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/partyJoin " + requestPlayerName));
		accept.setColor(ChatColor.GREEN);
		accept.setBold(true);
		TextComponent decline = new TextComponent("[Decline]");
		decline.setColor(ChatColor.RED);
		decline.setBold(true);
		message.addExtra(accept);
		message.addExtra(decline);
		serverPlayer.player.spigot().sendMessage(message);
	}
}
