package net.frozenorb.foxtrot.abilities.managers;

import lombok.Getter;
import net.frozenorb.foxtrot.Foxtrot;
import net.frozenorb.foxtrot.abilities.Ability;
import net.frozenorb.foxtrot.abilities.impl.*;
import net.frozenorb.foxtrot.abilities.impl.halloween.*;
import net.frozenorb.foxtrot.abilities.impl.partners.*;
import org.bukkit.Location;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by PVPTUTORIAL | Created on 01/04/2020
 */

@Getter
public class AbilityManager {
    private Foxtrot plugin;
    private HashMap<String, Ability> abilities = new HashMap<>();
    private ConcurrentHashMap<Location, Long> antiBuilding = new ConcurrentHashMap<>();

    public AbilityManager(Foxtrot plugin) {
        this.plugin = plugin;

        this.load();
    }

    private void load() {

        //disabled already
        this.abilities.put("ROCKET", new RocketAbility(plugin));
//        this.abilities.put("FREEZE_GUN", new FreezeGunAbility(plugin));
        this.abilities.put("BELCH_BOMB", new BelchBombAbility(plugin));
//        this.abilities.put("AIR_STRIKE", new AirStrikeAbility(plugin));
//        this.abilities.put("FAST_TRACK", new FastTrackAbility(plugin));
        this.abilities.put("MED_KIT", new MedkitAbility(plugin));
        //        this.abilities.put("STRENGTH_2", new CloutStrengthAbility(plugin));
        this.abilities.put("SWITCHER", new SwitcherAbility(plugin));
        this.abilities.put("TURBEN_FISH", new TurbenFishAbility(plugin));
        this.abilities.put("SWITCH_STICK", new SwitchStickAbility(plugin));
        //TODO: Figure out
//        this.abilities.put("PORTABLE_BACKSTAB", new PortableBackstabAbility(plugin));
        this.abilities.put("TELEPORT_STAR", new KleanAbilityStar(plugin));
        this.abilities.put("RETREAT", new GankahRetreatAbility(plugin));
        this.abilities.put("EXOTIC_BONE", new CrystalRodAbility(plugin));
//        this.abilities.put("POTION_CHECKER", new PotionCheckerAbility(plugin));
//        this.abilities.put("SYRINGE", new SyringeAbility(plugin));
//        this.abilities.put("MASHUP", new CelestialMashupAbility(plugin));
//        this.abilities.put("COCAINE", new CocaineAbility(plugin));
        this.abilities.put("POCKET_BARD", new PocketBardAbility(plugin));
//        this.abilities.put("FROST_BITE", new AudiIceAbility(plugin));
//        this.abilities.put("QUICK_SAND", new KleanQuicksandAbility(plugin));
        this.abilities.put("CLOCK", new LamboClockAbility(plugin));
//        this.abilities.put("DISABLER", new PremieresDisablerAbility(plugin));
//        this.abilities.put("ROTTEN_EGG", new RottenEggAbility(plugin));
//        this.abilities.put("VAMPIRE", new VampireAbility(plugin));
//        this.abilities.put("TRICK_OR_TREAT", new TrickOrTreatAbility(plugin));
        this.abilities.put("SWAPPER_AXE", new SetonicSwapperAbility(plugin));
    }

    public Ability getByName(String name) {
        if (!this.abilities.containsKey(name.toUpperCase()) || this.abilities.get(name.toUpperCase()) == null) {
            return null;
        }

        return this.abilities.get(name.toUpperCase());
    }


    public String getPotionName(PotionEffectType potionEffectType) {
        if (PotionEffectType.INCREASE_DAMAGE.equals(potionEffectType)) {
            return "Strength";
        } else if (PotionEffectType.FIRE_RESISTANCE.equals(potionEffectType)) {
            return "Fire Resistance";
        } else if (PotionEffectType.DAMAGE_RESISTANCE.equals(potionEffectType)) {
            return "Resistance";
        } else if (PotionEffectType.ABSORPTION.equals(potionEffectType)) {
            return "Absorption";
        } else if (PotionEffectType.INVISIBILITY.equals(potionEffectType)) {
            return "Invisibility";
        } else if (PotionEffectType.JUMP.equals(potionEffectType)) {
            return "Jump Boost";
        } else if (PotionEffectType.REGENERATION.equals(potionEffectType)) {
            return "Regeneration";
        } else if (PotionEffectType.SPEED.equals(potionEffectType)) {
            return "Speed";
        }

        return "None";
    }

    public PotionEffectType getPotionType(String string) {
        if (string.equalsIgnoreCase("Strength")) {
            return PotionEffectType.INCREASE_DAMAGE;
        } else if (string.equalsIgnoreCase("Fire Resistance")) {
            return PotionEffectType.FIRE_RESISTANCE;
        } else if (string.equalsIgnoreCase("Resistance")) {
            return PotionEffectType.DAMAGE_RESISTANCE;
        } else if (string.equalsIgnoreCase("Absorption")) {
            return PotionEffectType.ABSORPTION;
        } else if (string.equalsIgnoreCase("Invisibility")) {
            return PotionEffectType.INVISIBILITY;
        } else if (string.equalsIgnoreCase("Jump Boost")) {
            return PotionEffectType.JUMP;
        } else if (string.equalsIgnoreCase("Regeneration")) {
            return PotionEffectType.REGENERATION;
        } else if (string.equalsIgnoreCase("Speed")) {
            return PotionEffectType.SPEED;
        }

        return null;
    }
}
