package com.pm.aiost.misc.menu.request.requests;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.pm.aiost.misc.menu.Menu;
import com.pm.aiost.player.ServerPlayer;

public abstract class SingleMenuRequest extends CallbackMenuRequest {

	public SingleMenuRequest(Supplier<Menu> menuSupplier) {
		super(menuSupplier);
	}

	public SingleMenuRequest(Menu menu) {
		super(menu);
	}

	protected SingleMenuRequest() {
	}

	@Override
	public void setResult(ServerPlayer serverPlayer, Object obj) {
		doOpenTarget(serverPlayer);
		onResult(serverPlayer, obj);
	}

	public static abstract class SimpleSingleMenuRequest extends SingleMenuRequest {

		protected Consumer<ServerPlayer> requestConsumer;
		protected Consumer<ServerPlayer> targetConsumer;

		public SimpleSingleMenuRequest(Consumer<ServerPlayer> requestConsumer, Consumer<ServerPlayer> targetConsumer,
				Supplier<Menu> menuSupplier) {
			super(menuSupplier);
			this.requestConsumer = requestConsumer;
			this.targetConsumer = targetConsumer;
		}

		public SimpleSingleMenuRequest(Consumer<ServerPlayer> requestConsumer, Consumer<ServerPlayer> targetConsumer,
				Menu menu) {
			super(menu);
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
