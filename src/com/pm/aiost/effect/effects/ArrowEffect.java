package com.pm.aiost.effect.effects;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import com.pm.aiost.effect.Effect;
import com.pm.aiost.effect.EffectType;
import com.pm.aiost.effect.EffectTypes;
import com.pm.aiost.misc.menu.menus.request.EffectActionMenu;
import com.pm.aiost.misc.menu.menus.request.EffectConditionMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest;
import com.pm.aiost.player.ServerPlayer;

public class ArrowEffect extends Effect {

	public ArrowEffect() {
	}

	public ArrowEffect(byte[] actions, byte condition) {
		super(actions, condition);
	}

	@Override
	public void onPlayerInteract(ServerPlayer serverPlayer, PlayerInteractEvent event) {
		ensureArrow(serverPlayer.player);
	}

	protected void ensureArrow(Player player) {
		PlayerInventory inv = player.getInventory();
		if (inv.contains(Material.ARROW))
			return;
		inv.setItem(searchFreeSlot(inv), new ItemStack(Material.ARROW));
//		PacketSender.send(player, PacketFactory.packetSetSlot(0, searchFreeSlot(inv),
//				new net.minecraft.world.item.ItemStack(Items.ARROW)));
	}

	// TODO this overrides items somehow!
	public static int searchFreeSlot(PlayerInventory inv) {
		for (int i = 9; i < 45; i++) {
			if (inv.getItem(i) == NULL)
				return i;
		}
		return 9;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new MultiMenuRequest(requestConsumer, targetConsumer,
				new Supplier[] { EffectActionMenu::new, EffectConditionMenu::getMenu },
				new Consumer[] { this::setActions, this::setCondition });
	}

	@Override
	public EffectType<? extends ArrowEffect> getType() {
		return EffectTypes.ARROW;
	}
}
