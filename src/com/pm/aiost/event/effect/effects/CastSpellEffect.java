package com.pm.aiost.event.effect.effects;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;

import com.pm.aiost.event.effect.Effect;
import com.pm.aiost.event.effect.EffectType;
import com.pm.aiost.event.effect.EffectTypes;
import com.pm.aiost.event.effect.blueprints.SimpleLivingEntityEffect;
import com.pm.aiost.item.spell.Spell;
import com.pm.aiost.item.spell.Spells;
import com.pm.aiost.misc.menu.menus.request.EffectActionMenu;
import com.pm.aiost.misc.menu.menus.request.EffectConditionMenu;
import com.pm.aiost.misc.menu.menus.request.enumeration.EnumerationMenus;
import com.pm.aiost.misc.menu.request.MenuRequest;
import com.pm.aiost.misc.menu.request.requests.MultiMenuRequest.SimpleMultiMenuRequest;
import com.pm.aiost.misc.registry.AiostRegistry;
import com.pm.aiost.misc.utils.nbt.custom.INBTTagCompound;
import com.pm.aiost.player.ServerPlayer;

public class CastSpellEffect extends SimpleLivingEntityEffect {

	private Spell spell;

	public CastSpellEffect() {
	}

	public CastSpellEffect(byte[] actions, byte condition, String spell) {
		this(actions, condition, AiostRegistry.SPELLS.get(spell));
	}

	public CastSpellEffect(byte[] actions, byte condition, Spell spell) {
		super(actions, condition);
		this.spell = spell;
	}

	@Override
	public void runEffect(ServerPlayer serverPlayer) {
		spell.doCast(serverPlayer);
	}

	@Override
	public void runEffect(LivingEntity entity) {
		spell.doCast(entity);
	}

	@Override
	public boolean equals(Effect effect) {
		if (!super.equals(effect))
			return false;
		CastSpellEffect castSpell = (CastSpellEffect) effect;
		if (castSpell.spell != spell)
			return false;
		return true;
	}

	@Override
	public void load(ConfigurationSection section) {
		super.load(section);
		spell = Spell.load(section);
	}

	@Override
	public void load(Effect effect) {
		super.load(effect);
		CastSpellEffect castSpell = (CastSpellEffect) effect;
		if (castSpell.spell != null)
			spell = castSpell.spell;
	}

	@Override
	public void load(INBTTagCompound nbt) {
		super.load(nbt);
		spell = AiostRegistry.SPELLS.get(nbt.getString("spell"));
	}

	@Override
	public INBTTagCompound save(INBTTagCompound nbt) {
		super.save(nbt);
		nbt.setString("spell", spell.getName());
		return nbt;
	}

	@SuppressWarnings("unchecked")
	@Override
	public MenuRequest getMenuRequest(ServerPlayer serverPlayer, Consumer<ServerPlayer> requestConsumer,
			Consumer<ServerPlayer> targetConsumer) {
		return new SimpleMultiMenuRequest(requestConsumer, targetConsumer,
				new Supplier[] { EffectActionMenu::new, EffectConditionMenu::getMenu,
						() -> EnumerationMenus.SPELL_MENU },
				new Consumer[] { this::setActions, this::setCondition, (spell) -> this.spell = (Spell) spell });
	}

	@Override
	public EffectType<? extends CastSpellEffect> getType() {
		return EffectTypes.CAST_SPELL;
	}

	@Override
	public void setDefault() {
		super.setDefault();
		spell = Spells.FLAME;
	}

	@Override
	public void createDescription(List<String> list) {
		super.createDescription(list);
		list.add(ChatColor.GRAY + "Spell: " + ChatColor.DARK_GRAY + spell.getDisplayName());
	}

	public Spell getSpell() {
		return spell;
	}

	public void setSpell(Spell spell) {
		this.spell = spell;
	}
}