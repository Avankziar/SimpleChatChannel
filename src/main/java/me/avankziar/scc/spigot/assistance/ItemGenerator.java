package main.java.me.avankziar.scc.spigot.assistance;

import java.io.IOException;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.binary.Base64;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import main.java.me.avankziar.scc.database.YamlManager.GuiType;
import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;
import main.java.me.avankziar.scc.spigot.guihandling.GUIApi.SettingsLevel;

public class ItemGenerator
{
	public static ItemStack create(String ID, YamlConfiguration itm, GuiType type, 
			boolean mustReplaceLore, SettingsLevel settingLevel,
			Boolean channel) throws IOException
	{
		ItemStack is = null;
		if(itm.getString(ID+"."+settingLevel.getName()+".Material") == null)
		{
			SimpleChatChannels.log.info("ItemGenerator cannot read the material of "+ID+"."+settingLevel.getName()+".Material");
			return null;
		}
		Material mat = Material.matchMaterial(itm.getString(ID+"."+settingLevel.getName()+".Material"));
		if(mat == Material.PLAYER_HEAD && itm.getString(ID+"."+settingLevel.getName()+".PlayerHeadTexture") != null)
		{
			is = getSkull(itm.getString(ID+"."+settingLevel.getName()+".PlayerHeadTexture"));
		} else
		{
			is = new ItemStack(mat);
		}
		ItemMeta im = is.getItemMeta();
		if(itm.getString(ID+"."+settingLevel.getName()+".Name") == null)
		{
			SimpleChatChannels.log.info("ItemGenerator cannot read the name of "+ID+"."+settingLevel.getName()+".Name");
			return null;
		}
		String name = itm.getString(ID+"."+settingLevel.getName()+".Name");
		im.setDisplayName(ChatApi.tl(replace(name, channel)));
		ArrayList<String> itf = null;
		if(itm.getStringList(ID+"."+settingLevel.getName()+".Itemflag") != null)
		{
			itf = (ArrayList<String>) itm.getStringList(ID+"."+settingLevel.getName()+".Itemflag");
			for(int i = 0 ; i < itf.size() ; i++)
			{
				ItemFlag it = ItemFlag.valueOf(itf.get(i));
				im.addItemFlags(it);
			}
		}
		ArrayList<String> ech = null;
		if(itm.getStringList(ID+"."+settingLevel.getName()+".Enchantments") != null)
		{
			ech = (ArrayList<String>) itm.getStringList(ID+"."+settingLevel.getName()+".Enchantments");
			for(int i = 0 ; i < ech.size() ; i++)
			{
				String[] a = ech.get(i).split(";");
				String b = a[0].toUpperCase();
				NamespacedKey key = NamespacedKey.minecraft(b.toLowerCase());
				Enchantment eh = Registry.ENCHANTMENT.get(key);
				int d = Integer.parseInt(a[1]);
				if(eh != null)
				{
					im.addEnchant(eh, d, true);	
				}
			}
		}
		ArrayList<String> desc = null;
		if(itm.getStringList(ID+"."+settingLevel.getName()+".Lore") != null)
		{
			if(mustReplaceLore)
			{
				desc = (ArrayList<String>) replace(itm.getStringList(ID+"."+settingLevel.getName()+".Lore"), 
						channel);
			} else
			{
				desc = (ArrayList<String>) color(itm.getStringList(ID+"."+settingLevel.getName()+".Lore"));
			}
		}
		im.setLore(desc);
		
		NamespacedKey gt = new NamespacedKey(SimpleChatChannels.getPlugin(), "guitype");
		PersistentDataContainer pdc = im.getPersistentDataContainer();
		pdc.set(gt, PersistentDataType.STRING, type.toString());
		
		is.setItemMeta(im);
		int amount = itm.getInt(ID+"."+settingLevel.getName()+".Amount", 1);
		if(amount > is.getMaxStackSize() || amount < 1)
		{
			amount = 1;
		}
		is.setAmount(amount);
		return is;
	}
	
	public static ArrayList<String> replace(List<String> lore, Boolean channel) throws IOException
	{
		ArrayList<String> desc = new ArrayList<String>();
		for(String s : lore)
		{
			s = ChatApi.tl(replace(s, channel));
			desc.add(s);
		}
		return desc;
	}
	
	public static String replace(String s, Boolean channel) throws IOException
	{
		String st = s;
		if(channel != null)
		{
			if(channel)
			{
				st = st.replace("%boolean%", SimpleChatChannels.getPlugin().getYamlHandler().getConfig().getString("Gui.ActiveTerm"));
			} else
			{
				st = st.replace("%boolean%", SimpleChatChannels.getPlugin().getYamlHandler().getConfig().getString("Gui.DeactiveTerm"));
			}
		}
		return st;
	}
	
	@SuppressWarnings("deprecation")
	public static ItemStack getSkull(String url) 
	{
        ItemStack skull = new ItemStack(Material.PLAYER_HEAD, 1, (short) 3);
        if (url == null || url.isEmpty())
            return skull;
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:{SKIN:{url:\"%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }
        profileField.setAccessible(true);
        try {
            profileField.set(skullMeta, profile);
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        skull.setItemMeta(skullMeta);
        return skull;
    }
	
	public static String getActiveTerm(boolean boo)
	{
		if(boo)
		{
			String s = SimpleChatChannels.getPlugin().getYamlHandler().getConfig().getString("Gui.ActiveTerm");
			return s;
		} else
		{
			String s = SimpleChatChannels.getPlugin().getYamlHandler().getConfig().getString("Gui.DeactiveTerm");
			return s;
		}
	}
	
	private static List<String> color(List<String> lore)
	{
		ArrayList<String> list = new ArrayList<String>();
		for(String s : lore)
		{
			list.add(ChatApi.tl(s));
		}
	    return list;
	}
	
	public static double getNumberFormat(double d)//FIN
	{
		BigDecimal bd = new BigDecimal(d).setScale(1, RoundingMode.HALF_UP);
		double newd = bd.doubleValue();
		return newd;
	}
	
	public static double getNumberFormat(double d, int scale)//FIN
	{
		BigDecimal bd = new BigDecimal(d).setScale(scale, RoundingMode.HALF_UP);
		double newd = bd.doubleValue();
		return newd;
	}

}
