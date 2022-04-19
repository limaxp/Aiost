package com.pm.aiost.misc.menu.request.requests;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.pm.aiost.misc.menu.Menu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.player.ServerPlayer;

public abstract class MultiMenuRequest extends MenuRequest {

	protected Supplier<Menu>[] menuSupplier;
	protected Menu[] menus;
	protected Consumer<Object>[] resultConsumer;
	protected int currentIndex;

	public MultiMenuRequest(Supplier<Menu>[] menuSupplier, Consumer<Object>[] resultConsumer) {
		this.menuSupplier = menuSupplier;
		this.menus = new Menu[menuSupplier.length];
		this.resultConsumer = resultConsumer;
	}

	@Override
	public void setResult(ServerPlayer serverPlayer, Object obj) {
		resultConsumer[currentIndex].accept(obj);
		if (currentIndex < menuSupplier.length - 1)
			getMenu(++currentIndex).open(serverPlayer);
		else
			doOpenTarget(serverPlayer);
	}

	@Override
	public void openPrev(ServerPlayer serverPlayer) {
		if (currentIndex > 0)
			menus[--currentIndex].open(serverPlayer);
		else
			doOpenRequest(serverPlayer);
	}

	@Override
	public void open(ServerPlayer serverPlayer) {
		getMenu(currentIndex = 0).open(serverPlayer);
	}

	@Override
	public boolean hasPrevMenu() {
		return currentIndex > 0;
	}

	@Override
	public boolean hasNextMenu() {
		return currentIndex < menus.length - 1;
	}

	@Override
	public Menu getMenu() {
		return getMenu(currentIndex);
	}

	@Override
	public Menu getMenu(int index) {
		Menu menu = menus[index];
		if (menu == null) {
			menu = menuSupplier[index].get();
			menus[index] = menu;
		}
		return menu;
	}

	public static class SimpleMultiMenuRequest extends MultiMenuRequest {

		protected Consumer<ServerPlayer> requestConsumer;
		protected Consumer<ServerPlayer> targetConsumer;

		public SimpleMultiMenuRequest(Consumer<ServerPlayer> requestConsumer, Consumer<ServerPlayer> targetConsumer,
				Supplier<Menu>[] menuSupplier, Consumer<Object>[] resultConsumer) {
			super(menuSupplier, resultConsumer);
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
