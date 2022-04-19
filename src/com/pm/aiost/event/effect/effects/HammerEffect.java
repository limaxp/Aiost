package com.pm.aiost.event.effect.effects;

import static com.pm.aiost.misc.utils.ChatColor.BOLD;

import java.util.Collection;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.event.effect.blueprints.SimplePlayerBlockEffect;
import com.pm.aiost.item.Items;
import com.pm.aiost.misc.menu.menus.request.EffectActionMenu;
import com.pm.aiost.misc.menu.menus.request.EffectConditionMenu;
import com.pm.aiost.misc.menu.menus.request.NumberMenu;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.misc.utils.nms.NMS;
import com.pm.aiost.player.ServerPlayer;

public class HammerEffect extends SimplePlayerBlockEffect {

	private int radius;

	public HammerEffect() {
	}

	public HammerEffect(byte[] actions, byte condition, int radius) {
		super(actions, condition);
		this.radius = radius;
	}

	@Override
	public void runEffect(ServerPlayer serverPlayer, Block block) {
		hammerEffect(serverPlayer.player, Items.AIR, block, radius);
	}

	@Override
	public void onProjectileHit(ProjectileHitEvent event) {
		if (event.getHitBlock() != null)
			hammerEffect(Items.AIR, event.getHitBlock(), event.getHitBlockFace(), radius);
	}

	@Override
	public void onBlockBreak(ServerPlayer serverPlayer, BlockBreakEvent event) {
		hammerEffect(event.getPlayer().getInventory().getItemInMainHand(), event.getBlock(),
				serverPlayer.lastInteractedBlockface, radius);
	}

	public static void hammerEffect(LivingEntity entity, ItemStack is, Block block, int radius) {
		if (!canDestroyBlock(is, block))
			return;
		Vector entityDirection = entity.getLocation().getDirection();
		BlockFace blockFace = NMS.notchToBlockFace(
				NMS.getEnumDirection(entityDirection.getX(), entityDirection.getY(), entityDirection.getZ()));
		breakBlocks(is, block, blockFace, radius);
	}

	public static void hammerEffect(ItemStack is, Block block, BlockFace blockFace, int radius) {
		if (!canDestroyBlock(is, block))
			return;
		breakBlocks(is, block, blockFace, radius);
	}

	public static boolean canDestroyBlock(ItemStack is, Block block) {
		return NMS.getNMS(is).getItem().canDestroySpecialBlock(NMS.getNMS(block));
	}

	public static void breakBlocks(ItemStack is, Block block, BlockFace blockFace, int radius) {
		Collection<ItemStack> drops = block.getDrops(is);
		int amount = 0;
		Material type = block.getType();
		if (blockFace == BlockFace.DOWN || blockFace == BlockFace.UP) {
			for (int x = -radius; x <= radius; x++) {
				for (int z = -radius; z <= radius; z++) {
					Block relativeBlock = block.getRelative(x, 0, z);
					if (type == relativeBlock.getType()) {
						relativeBlock.setType(Material.AIR);
						amount++;
					}
				}
			}
		} else if (blockFace == BlockFace.EAST || blockFace == BlockFace.WEST) {
			for (int z = -radius; z <= radius; z++) {
				for (int y = -radius; y <= radius; y++) {
					Block relativeBlock = block.getRelative(0, y, z);
					if (type == relativeBlock.getType()) {
						relativeBlock.setType(Material.AIR);
						amount++;
					}
				}
			}
		} else if (blockFace == BlockFace.NORTH || blockFace == BlockFace.SOUTH) {
			for (int x = -radius; x <= radius; x++) {
				for (int y = -radius; y <= radius; y++) {
					Block relativeBlock = block.getRelative(x, y, 0);
					if (type == relativeBlock.getType()) {
						relativeBlock.setType(Material.AIR);
						amount++;
					}
				}
			}
		}

		for (ItemStack drop : drops) {
			drop.setAmount(amount);
			block.getWorld().dropItemNaturally(block.getLocation(), drop);
		}
	}

	@Override
	public boolean equals(Effect effect) {
		if (!super.equals(effect))
			return false;
		if (((HammerEffect) effect).radius != radius)
			return false;
		return true;
	}

	@Override
	public void load(ConfigurationSection effectSection) {
		super.load(effectSection);
		radius = effectSection.getInt("radius");
	}

	@Override
	public void load(Effect effect) {
		super.load(effect);
		HammerEffect hammerEffect = (HammerEffect) effect;
		if (hammerEffect.radius != 0)
			radius = hammerEffect.radius;
	}

	@Override
	public void load(INBTTagCompound nbt) {
		super.load(nbt);
		radius = nbt.getInt("radius");
	}

	@Override
	public INBTTagCompound save(INBTTagCompound nbt) {
		super.save(nbt);
		nbt.setInt("radius", radius);
		return nbt;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new SimpleMultiMenuRequest(requestConsumer, targetConsumer,
				new Supplier[] { EffectActionMenu::new, EffectConditionMenu::getMenu,
						() -> new NumberMenu(BOLD + "Choose radius") },
				new Consumer[] { this::setActions, this::setCondition,
						(radius) -> this.radius = ((Double) radius).intValue() });
	}

	@Override
	public EffectType<? extends HammerEffect> getType() {
		return EffectTypes.HAMMER_EFFECT;
	}

	@Override
	public void setDefault() {
		super.setDefault();
		radius = 1;
	}

	@Override
	public void createDescription(List<String> list) {
		super.createDescription(list);
		list.add(ChatColor.GRAY + "Radius: " + ChatColor.DARK_GRAY + radius);
	}

	public void setRadius(int radius) {
		this.radius = radius;
	}

	public int getRadius() {
		return radius;
	}
}
