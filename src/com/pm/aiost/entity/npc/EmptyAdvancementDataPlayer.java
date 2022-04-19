package com.pm.aiost.entity.npc;

import java.io.File;
import java.lang.reflect.Field;
import java.util.Set;

import com.pm.aiost.misc.log.Logger;

import net.minecraft.server.v1_15_R1.Advancement;
import net.minecraft.server.v1_15_R1.AdvancementDataPlayer;
import net.minecraft.server.v1_15_R1.AdvancementProgress;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.MinecraftServer;

public class EmptyAdvancementDataPlayer extends AdvancementDataPlayer {

	private static Field g;
	private static Field h;
	private static Field i;

	static {
		try {
			g = AdvancementDataPlayer.class.getDeclaredField("g");
			g.setAccessible(true);

			h = AdvancementDataPlayer.class.getDeclaredField("h");
			h.setAccessible(true);

			i = AdvancementDataPlayer.class.getDeclaredField("i");
			i.setAccessible(true);
		} catch (NoSuchFieldException | SecurityException e) {
			Logger.warn("EmptyAdvancementDataPlayer: Error on reflection of fields!");
			e.printStackTrace();
		}
	}

	public EmptyAdvancementDataPlayer(MinecraftServer minecraftserver, File file, EntityPlayer entityplayer) {
		super(minecraftserver, file, entityplayer);
		b();
	}

	public void a(Advancement advancement) {
	}

	public void a(EntityPlayer entityplayer) {
	}

	public void b() {
		clear(this);
	}

	public void b(EntityPlayer entityplayer) {
	}

	public void c() {
	}

	public AdvancementProgress getProgress(Advancement advancement) {
		return new AdvancementProgress();
	}

	public boolean grantCriteria(Advancement advancement, String s) {
		return false;
	}

	public boolean revokeCritera(Advancement advancement, String s) {
		return false;
	}

	@SuppressWarnings("rawtypes")
	public static void clear(AdvancementDataPlayer data) {
		data.a();
		data.data.clear();
		try {
			((Set) g.get(data)).clear();
			((Set) h.get(data)).clear();
			((Set) i.get(data)).clear();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
}
