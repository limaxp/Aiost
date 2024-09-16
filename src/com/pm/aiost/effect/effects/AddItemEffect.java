package com.pm.aiost.effect.effects;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.effect.Effect;
import com.pm.aiost.effect.EffectType;
import com.pm.aiost.effect.EffectTypes;
import com.pm.aiost.effect.blueprints.SimpleLivingEntityEffect;
import com.pm.aiost.item.ItemLoader;
import com.pm.aiost.misc.menu.menus.request.CreateItemMenu;
import com.pm.aiost.misc.menu.menus.request.EffectActionMenu;
import com.pm.aiost.misc.menu.menus.request.EffectConditionMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest;
import com.pm.aiost.misc.nms.NBT;
import com.pm.aiost.player.ServerPlayer;

import net.minecraft.nbt.CompoundTag;

public class AddItemEffect extends SimpleLivingEntityEffect {

	private ItemStack item;

	public AddItemEffect() {
	}

	public AddItemEffect(byte[] actions, byte condition, ItemStack item) {
		super(actions, condition);
		this.item = item;
	}

	@Override
	public void runEffect(LivingEntity entity) {
		addItem(entity);
	}

	protected void addItem(LivingEntity entity) {
		if (entity instanceof Player)
			((Player) entity).getInventory().addItem(item);
		else
			entity.getEquipment().setItemInMainHand(item);
	}

	@Override
	public boolean equals(Effect effect) {
		if (!super.equals(effect))
			return false;
		if (!((AddItemEffect) effect).item.equals(item))
			return false;
		return true;
	}

	@Override
	public void load(ConfigurationSection section) {
		super.load(section);
		item = ItemLoader.loadItemOrNull(section.get("item"));
	}

	@Override
	public void load(Effect effect) {
		super.load(effect);
		AddItemEffect addItemEffect = (AddItemEffect) effect;
		if (addItemEffect.item != null)
			item = addItemEffect.item.clone();
	}

	@Override
	public void load(CompoundTag nbt) {
		super.load(nbt);
		item = NBT.loadItem(nbt.getCompound("item"));
	}

	@Override
	public CompoundTag save(CompoundTag nbt) {
		super.save(nbt);
		nbt.put("item", NBT.saveItem(new CompoundTag(), item));
		return nbt;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new MultiMenuRequest(false, requestConsumer, targetConsumer,
				new Supplier[] { EffectActionMenu::new, EffectConditionMenu::getMenu,
						() -> serverPlayer.getOrCreateMenu(CreateItemMenu.class, CreateItemMenu::new) },
				new Consumer[] { this::setActions, this::setCondition,
						(itemStack) -> this.item = (ItemStack) itemStack });
	}

	@Override
	public EffectType<? extends AddItemEffect> getType() {
		return EffectTypes.ADD_ITEM;
	}

	@Override
	public void setDefault() {
		super.setDefault();
		item = new ItemStack(Material.ARROW);
	}

	@Override
	public void createDescription(List<String> list) {
		super.createDescription(list);
		list.add(ChatColor.GRAY + "Item: " + ChatColor.DARK_GRAY + item);
	}

	public void setItem(ItemStack item) {
		this.item = item;
	}

	public ItemStack getItem() {
		return item;
	}
}