package com.pm.aiost.misc.menu.bookMenu;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.BookMeta;

import com.pm.aiost.misc.menu.Menu;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.PacketSender;
import com.pm.aiost.player.ServerPlayer;

import net.md_5.bungee.api.chat.BaseComponent;

public class BookMenu implements Menu {

	private ItemStack bookItem;
	private BookMeta bookMeta;

	public BookMenu(String title, String author) {
		bookItem = new ItemStack(Material.WRITTEN_BOOK);
		bookMeta = (BookMeta) bookItem.getItemMeta();
		bookMeta.setTitle(title);
		bookMeta.setAuthor(author);
		bookItem.setItemMeta(bookMeta);
	}

	@Override
	public void open(Player player) {
		PlayerInventory inv = player.getInventory();
		ItemStack mainHand = inv.getItemInMainHand();
		inv.setItemInMainHand(bookItem);
		PacketSender.send(player, PacketFactory.packetOpenWindow(EquipmentSlot.HAND));
		inv.setItemInMainHand(mainHand);
	}

	@Override
	public void open(HumanEntity player) {
		open((Player) player);
	}

	@Override
	public void open(ServerPlayer serverPlayer) {
		open(serverPlayer.player);
	}

	public ItemStack getBook() {
		return bookItem;
	}

	public List<BaseComponent[]> getPages() {
		return bookMeta.spigot().getPages();
	}

	public void setPages(List<BaseComponent[]> pages) {
		bookMeta.spigot().setPages(pages);
	}

	public void setPages(BaseComponent[]... pages) {
		bookMeta.spigot().setPages(pages);
	}

	public void setPages_(List<String> pages) {
		bookMeta.setPages(pages);
	}

	public void setPages(String... pages) {
		bookMeta.setPages(pages);
	}

	public void addPage(BaseComponent... page) {
		bookMeta.spigot().addPage(page);
	}

	public void addPage(String page) {
		bookMeta.addPage(page);
	}

	/**
	 * Index starts at 1
	 */
	public void setPage(int index, BaseComponent... page) {
		bookMeta.spigot().setPage(index, page);
	}

	/**
	 * Index starts at 1
	 */
	public void setPage(int index, String page) {
		bookMeta.setPage(index, page);
	}

	public void setTitle(String title) {
		bookMeta.setTitle(title);
	}

	public boolean hasTitle() {
		return bookMeta.hasTitle();
	}

	public void setAuthor(String name) {
		bookMeta.setAuthor(name);
	}

	public boolean hasAuthor() {
		return bookMeta.hasAuthor();
	}

	public void update() {
		bookItem.setItemMeta(bookMeta);
	}
}
