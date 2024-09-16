package com.pm.aiost.misc.menu.request;

import java.util.function.Consumer;

import com.pm.aiost.misc.menu.Menu;
import com.pm.aiost.player.ServerPlayer;

public abstract class MenuRequest {

	public static final Consumer<ServerPlayer> EMPTY_CONSUMER = (serverPlayer) -> {
	};

	public static final Consumer<Object> EMPTY_RESULT_CONSUMER = (result) -> {
	};

	protected Consumer<ServerPlayer> requestConsumer;
	protected Consumer<ServerPlayer> targetConsumer;
	protected boolean isSaved;

	public MenuRequest(Consumer<ServerPlayer> requestConsumer, Consumer<ServerPlayer> targetConsumer, boolean isSaved) {
		this.requestConsumer = requestConsumer;
		this.targetConsumer = targetConsumer;
		this.isSaved = isSaved;
	}

	public abstract void setResult(ServerPlayer serverPlayer, Object obj);

	public abstract void openPrev(ServerPlayer serverPlayer);

	public abstract void open(ServerPlayer serverPlayer);

	public final void cancel(ServerPlayer serverPlayer) {
		serverPlayer.popMenuRequest();
		requestConsumer.accept(serverPlayer);
	}

	public final void finish(ServerPlayer serverPlayer) {
		if (!isSaved)
			serverPlayer.popMenuRequest();
		targetConsumer.accept(serverPlayer);
	}

	public abstract boolean hasPrevMenu();

	public abstract boolean hasNextMenu();

	public abstract Menu getMenu();

	public abstract Menu getMenu(int index);

	public void setSaved(boolean isSaved) {
		this.isSaved = isSaved;
	}

	public boolean isSaved() {
		return isSaved;
	}
}
