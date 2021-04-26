package main.java.me.avankziar.scc.spigot.guihandling;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.Repairable;

public class InventarHandler
{
	public static ArrayList<Enchantment> enchantments;
	
	public static void initEnchantments()
	{
		enchantments = new ArrayList<>();
		enchantments.add(Enchantment.ARROW_DAMAGE);
		enchantments.add(Enchantment.ARROW_FIRE);
		enchantments.add(Enchantment.ARROW_INFINITE);
		enchantments.add(Enchantment.ARROW_KNOCKBACK);
		enchantments.add(Enchantment.BINDING_CURSE);
		enchantments.add(Enchantment.CHANNELING);
		enchantments.add(Enchantment.DAMAGE_ALL);
		enchantments.add(Enchantment.DAMAGE_ARTHROPODS);
		enchantments.add(Enchantment.DAMAGE_UNDEAD);
		enchantments.add(Enchantment.DEPTH_STRIDER);
		enchantments.add(Enchantment.DIG_SPEED);
		enchantments.add(Enchantment.DURABILITY);
		enchantments.add(Enchantment.FIRE_ASPECT);
		enchantments.add(Enchantment.FROST_WALKER);
		enchantments.add(Enchantment.IMPALING);
		enchantments.add(Enchantment.KNOCKBACK);
		enchantments.add(Enchantment.LOOT_BONUS_BLOCKS);
		enchantments.add(Enchantment.LOOT_BONUS_MOBS);
		enchantments.add(Enchantment.LOYALTY);
		enchantments.add(Enchantment.LUCK);
		enchantments.add(Enchantment.LURE);
		enchantments.add(Enchantment.MENDING);
		enchantments.add(Enchantment.MULTISHOT);
		enchantments.add(Enchantment.OXYGEN);
		enchantments.add(Enchantment.PIERCING);
		enchantments.add(Enchantment.PROTECTION_ENVIRONMENTAL);
		enchantments.add(Enchantment.PROTECTION_EXPLOSIONS);
		enchantments.add(Enchantment.PROTECTION_FALL);
		enchantments.add(Enchantment.PROTECTION_FIRE);
		enchantments.add(Enchantment.PROTECTION_PROJECTILE);
		enchantments.add(Enchantment.QUICK_CHARGE);
		enchantments.add(Enchantment.RIPTIDE);
		enchantments.add(Enchantment.SILK_TOUCH);
		enchantments.add(Enchantment.SOUL_SPEED);
		enchantments.add(Enchantment.SWEEPING_EDGE);
		enchantments.add(Enchantment.THORNS);
		enchantments.add(Enchantment.VANISHING_CURSE);
		enchantments.add(Enchantment.WATER_WORKER);
	}
	
	@SuppressWarnings("deprecation")
	public static boolean isSimilarShort(ItemStack item, ItemStack[] filter)
	{
		for(ItemStack is : filter)
		{
			if (is == null || item == null) 
	        {
	            continue;
	        }
	        final ItemStack i = item.clone();
	        final ItemStack f = is.clone();
	        if(i.getType() != f.getType())
	        {
	        	continue;
	        }
	        if(i.hasItemMeta() == true && f.hasItemMeta() == true)
	        {
	        	if(i.getItemMeta() != null && f.getItemMeta() != null)
	        	{
	        		if(i.getItemMeta() instanceof Damageable && f.getItemMeta() instanceof Damageable)
	        		{
	        			Damageable id = (Damageable) i.getItemMeta();
	        			id.setDamage(0);
	        			i.setItemMeta((ItemMeta) id);
	        			Damageable od = (Damageable) f.getItemMeta();
	        			od.setDamage(0);
	        			f.setItemMeta((ItemMeta) od);
	        		}
		        	if(i.getItemMeta() instanceof Repairable && f.getItemMeta() instanceof Repairable)
	            	{
	            		Repairable ir = (Repairable) i.getItemMeta();
	            		ir.setRepairCost(0);
	            		i.setItemMeta((ItemMeta) ir);
	            		Repairable or = (Repairable) f.getItemMeta();
	            		or.setRepairCost(0);
	            		f.setItemMeta((ItemMeta) or);
	            	}
		        	if(i.getItemMeta() instanceof EnchantmentStorageMeta && f.getItemMeta() instanceof EnchantmentStorageMeta)
		        	{
		        		EnchantmentStorageMeta iesm = (EnchantmentStorageMeta) i.getItemMeta();
		        		i.setItemMeta(orderStorageEnchantments(iesm));
		        		EnchantmentStorageMeta oesm = (EnchantmentStorageMeta) f.getItemMeta();
		        		f.setItemMeta(orderStorageEnchantments(oesm));
		        	}
		        	if(i.getItemMeta().hasEnchants() && i.getType() != Material.ENCHANTED_BOOK 
		        			&& f.getItemMeta().hasEnchants() && i.getType() != Material.ENCHANTED_BOOK)
		        	{
		        		i.setItemMeta(orderEnchantments(i.getItemMeta()));
		        		f.setItemMeta(orderEnchantments(f.getItemMeta()));	
		        	}
		        	i.setAmount(1);
		        	f.setAmount(1);
		        	if(i.getItemMeta().toString().equals(f.getItemMeta().toString()))
		        	{
		        		return true;
		        	}
	        	}
	        } else
	        {
	        	i.setAmount(1);
	        	f.setAmount(1);
	        	i.setDurability((short) 0);
	        	f.setDurability((short) 0);
	        	if(i.toString().equals(f.toString()))
	        	{
	        		return true;
	        	}
	        }
		}
		return false;
	}
	
	public static boolean isFull(Inventory inv)
	{
		int i = 0;
		for(ItemStack is : inv.getContents())
		{
			if(is != null)
			{
				if(is.getType() != Material.AIR)
				{
					i++;
				}
			}
		}
		if(i<54)
		{
			return false;
		}
		return true;
	}
	
	public static EnchantmentStorageMeta orderStorageEnchantments(EnchantmentStorageMeta esm)
	{
		EnchantmentStorageMeta resm = esm.clone();
		for(Enchantment enchan : esm.getStoredEnchants().keySet())
		{
			resm.removeStoredEnchant(enchan);
		}
		for(Enchantment enchan : enchantments)
		{
			if(esm.hasStoredEnchant(enchan))
			{
				resm.addStoredEnchant(enchan, esm.getStoredEnchantLevel(enchan), true);
			}
		}
		return resm;
	}
	
	public static ItemMeta orderEnchantments(ItemMeta i)
	{
		ItemMeta ri = i.clone();
		for(Enchantment enchan : i.getEnchants().keySet())
		{
			ri.removeEnchant(enchan);
		}
		for(Enchantment enchan : enchantments)
		{
			if(i.hasEnchant(enchan))
			{
				ri.addEnchant(enchan, i.getEnchantLevel(enchan), true);
			}
		}
		return ri;
	}
}
