package com.pm.aiost.misc.menu.request.requests;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.pm.aiost.misc.menu.Menu;
import com.pm.aiost.player.ServerPlayer;

public abstract class MultiCallbackMenuRequest extends MultiMenuRequest {

	protected Consumer<Object> consumer;

	public MultiCallbackMenuRequest(Supplier<Menu>[] menuSupplier, Consumer<Object>[] resultConsumer,
			Consumer<Object> consumer) {
		super(menuSupplier, resultConsumer);
	}

	@Override
	public void setResult(ServerPlayer serverPlayer, Object obj) {
		super.setResult(serverPlayer, obj);
		consumer.accept(obj);
	}
}
