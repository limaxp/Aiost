package com.pm.aiost.misc.particleEffect.particle.particles;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Particle.DustOptions;
import org.bukkit.World;
import org.bukkit.block.data.BlockData;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.pm.aiost.item.ItemLoader;
import com.pm.aiost.misc.menu.menus.request.BooleanMenu;
import com.pm.aiost.misc.menu.menus.request.DataParticleMenu;
import com.pm.aiost.misc.menu.menus.request.NumberMenu;
import com.pm.aiost.misc.menu.menus.request.enumeration.EnumerationMenus;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.misc.packet.PacketFactory;
import com.pm.aiost.misc.packet.PacketSender;
import com.pm.aiost.misc.particleEffect.particle.ParticleType;
import com.pm.aiost.misc.particleEffect.particle.ParticleTypes;
import com.pm.aiost.misc.particleEffect.particle.RandomColorDustOptions;
import com.pm.aiost.misc.utils.ChatColor;
import com.pm.aiost.misc.utils.nbt.NBTHelper;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.misc.utils.nbt.custom.NBTCompound;
import com.pm.aiost.misc.utils.nms.NMS;
import com.pm.aiost.player.ServerPlayer;

public class DataParticle<T> extends Particle {

	protected @Nullable T data;

	public DataParticle() {
	}

	public DataParticle(org.bukkit.Particle particle, int count, float offsetX, float offsetY, float offsetZ,
			float extra, boolean longDistance, T data) {
		super(particle, count, offsetX, offsetY, offsetZ, extra, longDistance);
		this.data = data;
	}

	public DataParticle(org.bukkit.Particle particle, int count, float offset, float extra, boolean longDistance,
			T data) {
		super(particle, count, offset, extra, longDistance);
		this.data = data;
	}

	@Override
	public void spawn(World world, double x, double y, double z, float yaw, float pitch) {
		world.spawnParticle(particle, x, y, z, count, offsetX, offsetY, offsetZ, extra, data, longDistance);
	}

	@Override
	public void spawn(double x, double y, double z, float yaw, float pitch, Iterable<Player> player) {
		PacketSender.send(player, PacketFactory.packetParticles(NMS.getNMS(particle, data), longDistance, x, y, z,
				offsetX, offsetY, offsetZ, extra, count));
	}

	@Override
	public void load(ConfigurationSection section) {
		super.load(section);
		data = loadData(section);
	}

	@Override
	public void load(INBTTagCompound nbt) {
		super.load(nbt);
		data = loadData(nbt);
	}

	@Override
	public void save(INBTTagCompound nbt) {
		super.save(nbt);
		saveData(nbt);
	}

	@SuppressWarnings("unchecked")
	protected final T loadData(ConfigurationSection section) {
		if (section.contains("material"))
			return (T) loadBlockData(section.getString("material"));

		if (section.getBoolean("randomColor"))
			return (T) loadRandomColorDustOptions(section);

		if (section.contains("color"))
			return (T) loadDustOptions(section);

		if (section.contains("item"))
			return (T) ItemLoader.loadItemOrNull(section.get("item"));

		return null;
	}

	@SuppressWarnings("unchecked")
	private final T loadData(INBTTagCompound nbt) {
		if (nbt.hasKey("material"))
			return (T) loadBlockData(nbt.getString("material"));

		if (nbt.hasKey("randomColor"))
			return (T) new RandomColorDustOptions(nbt.getFloat("size"));

		if (nbt.hasKey("color"))
			return (T) new DustOptions(Color.fromRGB(nbt.getInt("color")), nbt.getFloat("size"));

		if (nbt.hasKey("item"))
			return (T) NBTHelper.loadItem(nbt.getCompound("item"));

		return data;
	}

	private final void saveData(INBTTagCompound nbt) {
		if (data instanceof BlockData)
			nbt.setString("material", ((BlockData) data).getMaterial().name());

		else if (data instanceof RandomColorDustOptions) {
			nbt.setBoolean("randomColor", true);
			nbt.setFloat("size", ((RandomColorDustOptions) data).getSize());
		}

		else if (data instanceof DustOptions) {
			DustOptions dustOptions = (DustOptions) data;
			nbt.setFloat("size", dustOptions.getSize());
			nbt.setInt("color", dustOptions.getColor().asRGB());
		}

		else if (data instanceof ItemStack)
			nbt.set("item", NBTHelper.saveItem(new NBTCompound(), (ItemStack) data));
	}

	private static BlockData loadBlockData(String materialName) {
		return Bukkit.createBlockData(Material.valueOf(materialName));
	}

	private static DustOptions loadDustOptions(ConfigurationSection section) {
		if (section.contains("size"))
			return new DustOptions(loadColor(section.get("color")), (float) section.getDouble("size"));
		else
			return new DustOptions(loadColor(section.get("color")), 1);
	}

	private static DustOptions loadRandomColorDustOptions(ConfigurationSection section) {
		if (section.contains("size"))
			return new RandomColorDustOptions((float) section.getDouble("size"));
		else
			return new RandomColorDustOptions(1);
	}

	private static Color loadColor(Object obj) {
		if (obj instanceof Integer)
			return Color.fromRGB(((Integer) obj).intValue());
		else if (obj instanceof List) {
			@SuppressWarnings("unchecked")
			List<Integer> list = (List<Integer>) obj;
			if (list.size() == 1)
				return Color.fromRGB(list.get(0));
			else if (list.size() == 3)
				return Color.fromRGB(list.get(0), list.get(1), list.get(2));
		}
		return null;
	}

	@Override
	public ParticleType<? extends DataParticle<?>> getType() {
		return ParticleTypes.DATA_PARTICLE;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof DataParticle == false)
			return false;
		if (!data.equals(((DataParticle<?>) obj).data))
			return false;
		return super.equals(obj);
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuRequest getMenuRequest(Consumer<ServerPlayer> requestConsumer, Consumer<ServerPlayer> targetConsumer) {
		return new SimpleMultiMenuRequest(requestConsumer, targetConsumer,
				new Supplier[] { () -> EnumerationMenus.PARTICLE_MENU,
						() -> new NumberMenu(ChatColor.BOLD + "Choose count"),
						() -> new NumberMenu(ChatColor.BOLD + "Choose offset x"),
						() -> new NumberMenu(ChatColor.BOLD + "Choose offset y"),
						() -> new NumberMenu(ChatColor.BOLD + "Choose offset z"),
						() -> new NumberMenu(ChatColor.BOLD + "Choose extra"),
						() -> BooleanMenu.create(ChatColor.BOLD + "Use long distance?"), DataParticleMenu::getMenu },

				new Consumer[] { (particle) -> this.particle = (org.bukkit.Particle) particle,
						(count) -> this.count = ((Double) count).intValue(),
						(offsetX) -> this.offsetX = ((Double) offsetX).floatValue(),
						(offsetY) -> this.offsetY = ((Double) offsetY).floatValue(),
						(offsetZ) -> this.offsetZ = ((Double) offsetZ).floatValue(),
						(extra) -> this.extra = ((Double) extra).floatValue(),
						(longDistance) -> this.longDistance = (Boolean) longDistance, (data) -> {
							if (particle.getDataType().isAssignableFrom(data.getClass()))
								this.data = (T) data;
							else
								this.data = getDefaultData();
						} });
	}

	@SuppressWarnings("unchecked")
	public T getDefaultData() {
		Class<?> clazz = particle.getDataType();
		if (clazz == BlockData.class)
			return (T) Bukkit.createBlockData(Material.STONE);
		else if (clazz == ItemStack.class)
			return (T) new ItemStack(Material.STONE);
		else if (clazz == DustOptions.class)
			return (T) new DustOptions(Color.PURPLE, 1);

		return null;
	}

	@Override
	public void setDefault() {
		super.setDefault();
		data = getDefaultData();
	}

	@Override
	public void createDescription(List<String> list) {
		super.createDescription(list);
		list.add(ChatColor.GRAY + "data: " + ChatColor.DARK_GRAY + data);
	}
}