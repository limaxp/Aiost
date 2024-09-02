package com.pm.aiost.misc.menu.request.requests;

import java.util.function.Consumer;

import com.pm.aiost.misc.menu.Menu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.player.ServerPlayer;

public class NoMenuRequest extends MenuRequest {

	public NoMenuRequest(Consumer<ServerPlayer> requestConsumer, Consumer<ServerPlayer> targetConsumer,
			boolean isSaved) {
		super(requestConsumer, targetConsumer, isSaved);
	}

	@Override
	public void setResult(ServerPlayer serverPlayer, Object obj) {
		finish(serverPlayer);
	}

	@Override
	public void openPrev(ServerPlayer serverPlayer) {
		cancel(serverPlayer);
	}

	@Override
	public void open(ServerPlayer serverPlayer) {
		finish(serverPlayer);
	}

	@Override
	public boolean hasPrevMenu() {
		return false;
	}

	@Override
	public boolean hasNextMenu() {
		return false;
	}

	@Override
	public Menu getMenu() {
		return null;
	}

	@Override
	public Menu getMenu(int index) {
		return null;
	}
}
