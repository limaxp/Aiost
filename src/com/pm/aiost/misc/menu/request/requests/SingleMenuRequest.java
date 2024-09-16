package com.pm.aiost.misc.menu.request.requests;

import java.util.function.Consumer;

import com.pm.aiost.misc.menu.Menu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.player.ServerPlayer;

public class SingleMenuRequest extends MenuRequest {

	protected Menu menu;
	protected Consumer<Object> resultConsumer;

	public SingleMenuRequest(Menu menu, Consumer<ServerPlayer> consumer, boolean isSaved) {
		this(menu, consumer, consumer, isSaved);
	}

	public SingleMenuRequest(Menu menu, Consumer<ServerPlayer> consumer, boolean isSaved,
			Consumer<Object> resultConsumer) {
		this(menu, consumer, consumer, isSaved, resultConsumer);
	}

	public SingleMenuRequest(Menu menu, Consumer<ServerPlayer> requestConsumer, Consumer<ServerPlayer> targetConsumer,
			boolean isSaved) {
		this(menu, requestConsumer, targetConsumer, isSaved, EMPTY_RESULT_CONSUMER);
	}

	public SingleMenuRequest(Menu menu, Consumer<ServerPlayer> requestConsumer, Consumer<ServerPlayer> targetConsumer,
			boolean isSaved, Consumer<Object> resultConsumer) {
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