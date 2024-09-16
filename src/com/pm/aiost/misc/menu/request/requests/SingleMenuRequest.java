package com.pm.aiost.misc.menu.request.requests;

import java.util.function.Consumer;

import com.pm.aiost.misc.menu.Menu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.player.ServerPlayer;

public class SingleMenuRequest extends MenuRequest {

	protected Menu menu;
	protected Consumer<Object> resultConsumer;

	public SingleMenuRequest(boolean isSaved, Menu menu, Consumer<ServerPlayer> consumer) {
		this(isSaved, menu, consumer, consumer, EMPTY_RESULT_CONSUMER);
	}

	public SingleMenuRequest(boolean isSaved, Menu menu, Consumer<ServerPlayer> consumer,
			Consumer<Object> resultConsumer) {
		this(isSaved, menu, consumer, consumer, resultConsumer);
	}

	public SingleMenuRequest(boolean isSaved, Menu menu, Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer, Consumer<Object> resultConsumer) {
		super(requestConsumer, targetConsumer, isSaved);
		this.menu = menu;
		this.resultConsumer = resultConsumer;
	}

	@Override
	public void setResult(ServerPlayer serverPlayer, Object obj) {
		finish(serverPlayer);
		onResult(serverPlayer, obj);
	}

	protected void onResult(ServerPlayer serverPlayer, Object obj) {
		resultConsumer.accept(obj);
	}

	@Override
	public void openPrev(ServerPlayer serverPlayer) {
		cancel(serverPlayer);
	}

	@Override
	public void open(ServerPlayer serverPlayer) {
		getMenu().open(serverPlayer);
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
		return menu;
	}

	@Override
	public Menu getMenu(int index) {
		return getMenu();
	}
}