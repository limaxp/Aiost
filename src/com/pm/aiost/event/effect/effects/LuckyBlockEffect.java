package com.pm.aiost.event.effect.effects;

import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Spider;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.pm.aiost.event.effect.EffectAction;
import com.pm.aiost.event.effect.EffectCondition;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.event.effect.blueprints.SingletonEffect;
import com.pm.aiost.misc.utils.ChatColor;
import com.pm.aiost.misc.utils.meta.MetaHelper;
import com.pm.aiost.player.ServerPlayer;

public class LuckyBlockEffect extends SingletonEffect {

	private static final byte[] ACTIONS = { EffectAction.BLOCK_BREAK };
	private static final Random RANDOM = new Random();

	public static final LuckyBlockEffect INSTANCE = new LuckyBlockEffect();

	protected LuckyBlockEffect() {
		super(ACTIONS, EffectCondition.SELF);
	}

	@Override
	public void onBlockBreak(ServerPlayer serverPlayer, BlockBreakEvent event) {
		luckyBlockEffect(serverPlayer, event.getBlock());
	}

	public static void luckyBlockEffect(ServerPlayer serverPlayer, Location loc) {
		luckyBlockEffect(serverPlayer, loc.getWorld().getBlockAt(loc));
	}

	public static void luckyBlockEffect(ServerPlayer serverPlayer, Block block) {
		Player player = serverPlayer.player;
		World world = block.getWorld();
		Location loc = block.getLocation().clone().add(0.5, 0, 0.5);
		world.playEffect(loc, Effect.MOBSPAWNER_FLAMES, 1);

		switch (RANDOM.nextInt(32)) {

		case 0:
			player.sendMessage("Take it! It's rare! right?");
			world.dropItemNaturally(loc, new ItemStack(Material.CHAINMAIL_BOOTS));
			world.dropItemNaturally(loc, new ItemStack(Material.CHAINMAIL_CHESTPLATE));
			world.dropItemNaturally(loc, new ItemStack(Material.CHAINMAIL_HELMET));
			world.dropItemNaturally(loc, new ItemStack(Material.CHAINMAIL_LEGGINGS));
			break;

		case 1:
			player.sendMessage("Ahhhh gold!");
			world.dropItemNaturally(loc, new ItemStack(Material.GOLD_INGOT, 15));
			break;

		case 2:
			player.sendMessage("The best tools in the world!");
			world.dropItemNaturally(loc, new ItemStack(Material.GOLDEN_AXE));
			world.dropItemNaturally(loc, new ItemStack(Material.GOLDEN_HOE));
			world.dropItemNaturally(loc, new ItemStack(Material.GOLDEN_PICKAXE));
			world.dropItemNaturally(loc, new ItemStack(Material.GOLDEN_SHOVEL));
			world.dropItemNaturally(loc, new ItemStack(Material.GOLDEN_SWORD));
			break;

		case 3:
			player.sendMessage("All the hoes you need!");
			world.dropItemNaturally(loc, new ItemStack(Material.DIAMOND_HOE));
			world.dropItemNaturally(loc, new ItemStack(Material.GOLDEN_HOE));
			world.dropItemNaturally(loc, new ItemStack(Material.IRON_HOE));
			world.dropItemNaturally(loc, new ItemStack(Material.STONE_HOE));
			world.dropItemNaturally(loc, new ItemStack(Material.WOODEN_HOE));
			break;

		case 4:
			player.sendMessage("I am sure you wanted to name something! right?");
			world.dropItemNaturally(loc, new ItemStack(Material.NAME_TAG, 5));
			break;

		case 5:
			player.sendMessage("What a nice chest!");
			world.dropItemNaturally(loc, new ItemStack(Material.ENDER_CHEST));
			break;

		case 6:
			player.sendMessage("You did it!");
			world.dropItemNaturally(loc, new ItemStack(Material.DIAMOND, 3));
			break;

		case 7:
			player.sendMessage("They say teleporting is fun! You should try it");
			world.dropItemNaturally(loc, new ItemStack(Material.ENDER_PEARL, 20));
			break;

		case 8:
			player.sendMessage("I have an eye on you!");
			world.dropItemNaturally(loc, new ItemStack(Material.ENDER_EYE, 10));
			break;

		case 9:
			player.sendMessage("Are you hungry?");
			world.dropItemNaturally(loc, new ItemStack(Material.COOKED_CHICKEN, 10));
			world.dropItemNaturally(loc, new ItemStack(Material.COOKED_BEEF, 10));
			world.dropItemNaturally(loc, new ItemStack(Material.COOKED_MUTTON, 10));
			world.dropItemNaturally(loc, new ItemStack(Material.COOKED_COD, 10));
			world.dropItemNaturally(loc, new ItemStack(Material.COOKED_PORKCHOP, 10));
			world.dropItemNaturally(loc, new ItemStack(Material.COOKED_RABBIT, 10));
			world.dropItemNaturally(loc, new ItemStack(Material.COOKED_SALMON, 10));
			break;

		case 10:
			player.sendMessage("Close but not close enough!");
			ItemStack oldLuckySword = MetaHelper.setMeta(new ItemStack(Material.GOLDEN_SWORD),
					ChatColor.GOLD + "Old Lucky Sword");
			oldLuckySword.addEnchantment(Enchantment.KNOCKBACK, 1);
			oldLuckySword.addEnchantment(Enchantment.DAMAGE_ALL, 3);
			oldLuckySword.addEnchantment(Enchantment.FIRE_ASPECT, 1);
			world.dropItemNaturally(loc, oldLuckySword);
			break;

		case 11:
			player.sendMessage("You are a potatoe!");
			world.dropItemNaturally(loc, new ItemStack(Material.POTATO, 20));
			world.dropItemNaturally(loc, new ItemStack(Material.BAKED_POTATO, 20));
			world.dropItemNaturally(loc, new ItemStack(Material.POISONOUS_POTATO, 20));
			break;

		case 12:
			player.sendMessage("Buff up!");
			world.dropItemNaturally(loc, new ItemStack(Material.BEACON));
			break;

		case 13:
			player.sendMessage("Gear up!");
			world.dropItemNaturally(loc, new ItemStack(Material.IRON_BOOTS));
			world.dropItemNaturally(loc, new ItemStack(Material.IRON_CHESTPLATE));
			world.dropItemNaturally(loc, new ItemStack(Material.IRON_LEGGINGS));
			world.dropItemNaturally(loc, new ItemStack(Material.IRON_HELMET));
			break;

		case 14:
			player.sendMessage("Wow its a star! Incredible");
			world.dropItemNaturally(loc, new ItemStack(Material.NETHER_STAR));
			break;

		case 15:
			player.sendMessage("Buy something nice with that!");
			block.setType(Material.EMERALD_BLOCK);
			break;

		case 16:
			player.sendMessage("Pretty nice right?");
			block.setType(Material.GOLD_BLOCK);
			break;

		case 17:
			player.sendMessage("Jackpot!");
			block.setType(Material.DIAMOND_BLOCK);
			break;

		case 18:
			player.sendMessage("There it is!");
			ItemStack luckySword = MetaHelper.setMeta(new ItemStack(Material.GOLDEN_SWORD),
					ChatColor.GOLD + "Lucky Sword");
			luckySword.addUnsafeEnchantment(Enchantment.KNOCKBACK, 5);
			luckySword.addUnsafeEnchantment(Enchantment.DAMAGE_ALL, 6);
			luckySword.addEnchantment(Enchantment.FIRE_ASPECT, 2);
			luckySword.addEnchantment(Enchantment.DAMAGE_ARTHROPODS, 2);
			luckySword.addEnchantment(Enchantment.DAMAGE_UNDEAD, 2);
			luckySword.addEnchantment(Enchantment.LOOT_BONUS_MOBS, 2);
			luckySword.addUnsafeEnchantment(Enchantment.DURABILITY, 4);
			world.dropItemNaturally(loc, luckySword);
			break;

		case 19:
			player.sendMessage("Have fun!");
			ItemStack knockbackStick = MetaHelper.setMeta(new ItemStack(Material.STICK),
					ChatColor.RED + "Baseball bat");
			knockbackStick.addUnsafeEnchantment(Enchantment.KNOCKBACK, 20);
			world.dropItemNaturally(loc, knockbackStick);
			break;

		case 20:
			player.sendMessage("I am sure you like magic!");
			world.dropItemNaturally(loc, new ItemStack(Material.ENCHANTING_TABLE));
			world.dropItemNaturally(loc, new ItemStack(Material.BOOKSHELF, 10));
			break;

		case 21:
			player.sendMessage("Apple up!");
			world.dropItemNaturally(loc, new ItemStack(Material.GOLDEN_APPLE, 3));
			break;

		case 22:
			player.sendMessage("You might find this one useful!");
			block.setType(Material.ANVIL);
			break;

		case 23:
			player.sendMessage("Happy birthday!");
			world.dropItemNaturally(loc, new ItemStack(Material.CAKE, 10));
			break;

		case 24:
			player.sendMessage("Information is key!");
			world.dropItemNaturally(loc, new ItemStack(Material.COMPASS));
			world.dropItemNaturally(loc, new ItemStack(Material.CLOCK));
			break;

		case 25:
			player.sendMessage("Buckets!");
			world.dropItemNaturally(loc, new ItemStack(Material.BUCKET, 3));
			world.dropItemNaturally(loc, new ItemStack(Material.LAVA_BUCKET, 3));
			world.dropItemNaturally(loc, new ItemStack(Material.WATER_BUCKET, 3));
			break;

		case 26:
			player.sendMessage("Nope!");
			block.setType(Material.LAVA);
			break;

		case 27:
			player.sendMessage("Hehehehe!");
			block.setType(Material.OBSIDIAN);
			break;

		case 28:
			player.sendMessage(ChatColor.RED + "Say hi to Hulk!");
			Entity giant = world.spawnEntity(loc.add(0.5D, 0, 0.5D), EntityType.GIANT);
			giant.setCustomName(ChatColor.RED + "Hulk");
			giant.setCustomNameVisible(true);
			break;

		case 29:
			player.sendMessage(ChatColor.RED + "I hate spiders!");
			Spider spiderQueen = (Spider) world.spawnEntity(loc, EntityType.SPIDER);
			spiderQueen.setCustomName(ChatColor.RED + "SpiderQueen");
			spiderQueen.setCustomNameVisible(true);
			spiderQueen.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 2000, 2));
			for (int i = 0; i < 10; i++)
				world.spawnEntity(loc, EntityType.CAVE_SPIDER);
			break;

		case 30:
			player.sendMessage(ChatColor.RED + "Burn!");
			for (int x = -2; x <= 2; x++) {
				for (int z = -2; z <= 2; z++) {
					Block relativeblock = block.getRelative(x, 0, z);
					if (relativeblock.getType() == Material.AIR)
						relativeblock.setType(Material.FIRE);
				}
			}
			break;

		case 31:
			player.sendMessage(ChatColor.RED + "Boom baby!");
			for (int i = 0; i < 5; i++)
				world.spawnEntity(loc, EntityType.CREEPER);
			break;

		default:
			break;
		}

	}

	@Override
	public EffectType<? extends LuckyBlockEffect> getType() {
		return EffectTypes.LUCKY_BLOCK;
	}

	public static LuckyBlockEffect getInstance() {
		return INSTANCE;
	}
}
