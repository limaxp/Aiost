package com.pm.aiost.misc.menu.request.requests;

import java.util.function.Consumer;

import com.pm.aiost.misc.menu.Menu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.player.ServerPlayer;

public abstract class NoMenuRequest extends MenuRequest {

	@Override
	public void setResult(ServerPlayer serverPlayer, Object obj) {
		doOpenTarget(serverPlayer);
	}

	@Override
	public void openPrev(ServerPlayer serverPlayer) {
		doOpenRequest(serverPlayer);
	}

	@Override
	public void open(ServerPlayer serverPlayer) {
		doOpenTarget(serverPlayer);
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

	public static class SimpleNoMenuRequest extends NoMenuRequest {

		protected Consumer<ServerPlayer> requestConsumer;
		protected Consumer<ServerPlayer> targetConsumer;

		public SimpleNoMenuRequest(Consumer<ServerPlayer> requestConsumer, Consumer<ServerPlayer> targetConsumer) {
			this.requestConsumer = requestConsumer;
			this.targetConsumer = targetConsumer;
		}

		@Override
		public void openRequest(ServerPlayer serverPlayer) {
			requestConsumer.accept(serverPlayer);
		}

		@Override
		public void openTarget(ServerPlayer serverPlayer) {
			targetConsumer.accept(serverPlayer);
		}
	}
}
