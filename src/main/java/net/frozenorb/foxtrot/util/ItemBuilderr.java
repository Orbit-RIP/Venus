package net.frozenorb.foxtrot.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Color;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemBuilderr {
	private ItemStack is;

	public ItemBuilderr(Material m){
		this(m, 1);
	}

	public ItemBuilderr(ItemStack is){
		this.is=is;
	}

	public ItemBuilderr(Material m, int amount){
		is= new ItemStack(m, amount);
	}

	public ItemBuilderr(Material m, int amount, byte durability){
		is = new ItemStack(m, amount, durability);
	}

	public ItemBuilderr clone(){
		return new ItemBuilderr(is);
	}

	public ItemBuilderr setDurability(short dur){
		is.setDurability(dur);
		return this;
	}

	public ItemBuilderr setName(String name){
		ItemMeta im = is.getItemMeta();
		im.setDisplayName(name);
		is.setItemMeta(im);
		return this;
	}

	public ItemBuilderr addUnsafeEnchantment(Enchantment ench, int level){
		is.addUnsafeEnchantment(ench, level);
		return this;
	}

	public ItemBuilderr removeEnchantment(Enchantment ench){
		is.removeEnchantment(ench);
		return this;
	}

	public ItemBuilderr setSkullOwner(String owner){
		try{
			SkullMeta im = (SkullMeta)is.getItemMeta();
			im.setOwner(owner);
			is.setItemMeta(im);
		}catch(ClassCastException expected){}
		return this;
	}

	public ItemBuilderr addEnchant(Enchantment ench, int level){
		ItemMeta im = is.getItemMeta();
		im.addEnchant(ench, level, true);
		is.setItemMeta(im);
		return this;
	}

	public ItemBuilderr addEnchantments(Map<Enchantment, Integer> enchantments){
		is.addEnchantments(enchantments);
		return this;
	}

	public ItemBuilderr setInfinityDurability(){
		is.setDurability(Short.MAX_VALUE);
		return this;
	}

	public ItemBuilderr setLore(String... lore){
		ItemMeta im = is.getItemMeta();
		im.setLore(Arrays.asList(lore));
		is.setItemMeta(im);
		return this;
	}

	public ItemBuilderr setLore(List<String> lore) {
		ItemMeta im = is.getItemMeta();
		im.setLore(lore);
		is.setItemMeta(im);
		return this;
	}

	public ItemBuilderr removeLoreLine(String line){
		ItemMeta im = is.getItemMeta();
		List<String> lore = new ArrayList<>(im.getLore());
		if(!lore.contains(line))return this;
		lore.remove(line);
		im.setLore(lore);
		is.setItemMeta(im);
		return this;
	}

	public ItemBuilderr removeLoreLine(int index){
		ItemMeta im = is.getItemMeta();
		List<String> lore = new ArrayList<>(im.getLore());
		if(index<0||index>lore.size())return this;
		lore.remove(index);
		im.setLore(lore);
		is.setItemMeta(im);
		return this;
	}

	public ItemBuilderr addLoreLine(String line){
		ItemMeta im = is.getItemMeta();
		List<String> lore = new ArrayList<>();
		if(im.hasLore())lore = new ArrayList<>(im.getLore());
		lore.add(line);
		im.setLore(lore);
		is.setItemMeta(im);
		return this;
	}

	public ItemBuilderr addLoreLine(String line, int pos){
		ItemMeta im = is.getItemMeta();
		List<String> lore = new ArrayList<>(im.getLore());
		lore.set(pos, line);
		im.setLore(lore);
		is.setItemMeta(im);
		return this;
	}

	public ItemBuilderr setDyeColor(DyeColor color){
		this.is.setDurability(color.getData());
		return this;
	}

	public ItemBuilderr setWoolColor(DyeColor color){
		if(!is.getType().equals(Material.WOOL))return this;
		this.is.setDurability(color.getData());
		return this;
	}

	public ItemBuilderr setLeatherArmorColor(Color color){
		try{
			LeatherArmorMeta im = (LeatherArmorMeta)is.getItemMeta();
			im.setColor(color);
			is.setItemMeta(im);
		}catch(ClassCastException expected){}
		return this;
	}

	public ItemStack toItemStack(){
		return is;
	}
}