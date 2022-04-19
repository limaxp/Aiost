package com.pm.aiost.misc.menu.request;

import java.util.function.Consumer;

import com.pm.aiost.misc.menu.Menu;
import com.pm.aiost.player.ServerPlayer;

public abstract class MenuRequest {

	public static final Consumer<ServerPlayer> EMPTY_CONSUMER = (serverPlayer) -> {
	};

	public abstract void setResult(ServerPlayer serverPlayer, Object obj);

	public abstract void openPrev(ServerPlayer serverPlayer);

	public abstract void open(ServerPlayer serverPlayer);

	public final void doOpenRequest(ServerPlayer serverPlayer) {
		serverPlayer.popMenuRequest();
		openRequest(serverPlayer);
	}

	protected abstract void openRequest(ServerPlayer serverPlayer);

	public final void doOpenTarget(ServerPlayer serverPlayer) {
		serverPlayer.popMenuRequest();
		openTarget(serverPlayer);
	}

	protected void openTarget(ServerPlayer serverPlayer) {
		openRequest(serverPlayer);
	}

	public abstract boolean hasPrevMenu();

	public abstract boolean hasNextMenu();

	public abstract Menu getMenu();

	public abstract Menu getMenu(int index);
}
