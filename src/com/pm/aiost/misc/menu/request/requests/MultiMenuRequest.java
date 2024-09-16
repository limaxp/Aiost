package com.pm.aiost.misc.menu.request.requests;

import java.util.function.Consumer;
import java.util.function.Supplier;

import com.pm.aiost.misc.menu.Menu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.player.ServerPlayer;

public class MultiMenuRequest extends MenuRequest {

	protected Supplier<Menu>[] menuSupplier;
	protected Menu[] menus;
	protected Consumer<Object>[] resultConsumer;
	protected int currentIndex;

	public MultiMenuRequest(boolean isSaved, Consumer<ServerPlayer> consumer, Supplier<Menu>[] menuSupplier,
			Consumer<Object>[] resultConsumer) {
		this(isSaved, consumer, consumer, menuSupplier, resultConsumer);
	}

	public MultiMenuRequest(boolean isSaved, Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer, Supplier<Menu>[] menuSupplier, Consumer<Object>[] resultConsumer) {
		super(requestConsumer, targetConsumer, isSaved);
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
			finish(serverPlayer);
	}

	@Override
	public void openPrev(ServerPlayer serverPlayer) {
		if (currentIndex > 0)
			menus[--currentIndex].open(serverPlayer);
		else
			cancel(serverPlayer);
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
}
