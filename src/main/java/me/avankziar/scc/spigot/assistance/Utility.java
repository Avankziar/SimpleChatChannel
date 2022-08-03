package main.java.me.avankziar.scc.spigot.assistance;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;
import org.yaml.snakeyaml.external.biz.base64Coder.Base64Coder;

import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.ChatUser;
import main.java.me.avankziar.scc.objects.KeyHandler;
import main.java.me.avankziar.scc.objects.PermanentChannel;
import main.java.me.avankziar.scc.objects.ServerLocation;
import main.java.me.avankziar.scc.objects.chat.Channel;
import main.java.me.avankziar.scc.objects.chat.IgnoreObject;
import main.java.me.avankziar.scc.objects.chat.UsedChannel;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;
import main.java.me.avankziar.scc.spigot.database.MysqlHandler;
import main.java.me.avankziar.scc.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.scc.spigot.objects.BypassPermission;
import main.java.me.avankziar.scc.spigot.objects.ChatUserHandler;
import main.java.me.avankziar.scc.spigot.objects.PluginSettings;
import main.java.me.avankziar.scc.spigot.objects.TemporaryChannel;
import net.md_5.bungee.api.ChatColor;

public class Utility 
{
	private static SimpleChatChannels plugin;
	public static ArrayList<String> onlineplayers = new ArrayList<>();
	public static LinkedHashMap<String, LinkedHashMap<String, UsedChannel>> playerUsedChannels = new LinkedHashMap<>();
	
	public Utility(SimpleChatChannels plugin)
	{
		Utility.plugin = plugin;
	}
	
	public String removeColor(String msg)
	{
		return ChatColor.stripColor(msg);
	}
	
	public boolean isSimliarText(String text, String comparison, double percentOfSimilarity)
	{
		int i = 0;
		int matches = 0;
		while(i < text.length())
		{
			char c = text.charAt(i);
			if(i < comparison.length())
			{
				char c2 = comparison.charAt(i);
				if(c == c2)
				{
					matches++;
				}
			} else
			{
				break;
			}
			i++;
		}
		double percent = (((double) matches) / ((double) comparison.length()))*100.0 ;
		if(percent <= percentOfSimilarity)
		{
			return true;
		}
		return false;
	}
	
	public boolean getIgnored(Player target, Player player, boolean privatechat)
	{
		IgnoreObject io = (IgnoreObject) plugin.getMysqlHandler().getData(MysqlHandler.Type.IGNOREOBJECT,
				"`player_uuid` = ? AND `ignore_uuid` = ?",
				target.getUniqueId().toString(), player.getUniqueId().toString());
		if(io != null)
		{
			if(privatechat)
			{
				if(player.hasPermission(BypassPermission.PERMBYPASSIGNORE))
				{
					player.spigot().sendMessage(ChatApi.tctl(
							plugin.getYamlHandler().getLang().getString("ChatListener.PlayerIgnoreYou")));
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	public long getMutedTime(Player player)
	{
		ChatUser cu = ChatUserHandler.getChatUser(player.getUniqueId());
		if(cu == null)
		{
			return 0L;
		}
		return cu.getMuteTime();
	}
	
	public String getActiveChannels(ChatUser cu, ArrayList<UsedChannel> usedChannels)
	{
		String comma = plugin.getYamlHandler().getLang().getString("JoinListener.Comma");
		
		String ac = "";
		if(cu.getMuteTime() > System.currentTimeMillis()) 
		{
			ac = plugin.getYamlHandler().getLang().getString("JoinListener.YouMuted"); 
			return ac;
		}
		ac += plugin.getYamlHandler().getLang().getString("JoinListener.Pretext");
		for(UsedChannel uc : usedChannels)
		{
			if(uc.isUsed())
			{
				Channel c = plugin.getChannel(uc.getUniqueIdentifierName());
				if(c == null)
				{
					if(SimpleChatChannels.nullChannel.getUniqueIdentifierName().equals(uc.getUniqueIdentifierName()))
					{
						c = SimpleChatChannels.nullChannel;
					} else
					{
						continue;
					}
				}
				ac +=  c.getJoinPart()+comma;
			}
		}
		if(cu.isOptionSpy()) {ac += plugin.getYamlHandler().getLang().getString("Join.Spy")+comma;}
		return ac.substring(0, ac.length()-2);
	}
	
	public boolean containsBadWords(Player player, String message)
	{
		if(player.hasPermission(BypassPermission.PERMBYPASSWORDFILTER))
		{
			return false;
		}
		List<String> list = plugin.getYamlHandler().getWordFilter().getStringList("WordFilter");
		for(String s : list)
		{
			if(containsIgnoreCase(message, s))
			{
				return true;
			}
		}
		return false;
	}
	
	public ChatUser controlUsedChannels(Player player)
	{
		ChatUser cu = new ChatUser(player.getUniqueId().toString(), player.getName(),
				"", 0L, 0L, false, true, System.currentTimeMillis(), plugin.getYamlHandler().getConfig().getBoolean("JoinMessageDefaultValue"),
				new ServerLocation(PluginSettings.settings.getServer(), "default", 0.0, 0.0, 0.0, 0.0F, 0.0F));
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.CHATUSER,
				"`player_uuid` = ?", player.getUniqueId().toString()))
		{
			plugin.getMysqlHandler().create(MysqlHandler.Type.CHATUSER, cu);
			for(Channel c : SimpleChatChannels.channels.values())
			{
				if(player.hasPermission(c.getPermission()))
				{
					UsedChannel uc = new UsedChannel(c.getUniqueIdentifierName(), player.getUniqueId().toString(), true);
					plugin.getMysqlHandler().create(Type.USEDCHANNEL, uc);
				}
			}
		} else
		{
			cu = (ChatUser) plugin.getMysqlHandler().getData(MysqlHandler.Type.CHATUSER, 
					"`player_uuid` = ?", player.getUniqueId().toString());
			updateUsedChannels(player);
		}
		return cu;
	}
	
	public void updateUsedChannels(Player player)
	{
		for(Channel c : SimpleChatChannels.channels.values())
		{
			if(player.hasPermission(c.getPermission()))
			{
				if(!plugin.getMysqlHandler().exist(Type.USEDCHANNEL, "`uniqueidentifiername` = ? AND `player_uuid` = ?",
						c.getUniqueIdentifierName(), player.getUniqueId().toString()))
				{
					UsedChannel uc = new UsedChannel(c.getUniqueIdentifierName(), player.getUniqueId().toString(), true);
					plugin.getMysqlHandler().create(Type.USEDCHANNEL, uc);
				}
			} else
			{
				plugin.getMysqlHandler().deleteData(Type.USEDCHANNEL, "`uniqueidentifiername` = ? AND `player_uuid` = ?",
						c.getUniqueIdentifierName(), player.getUniqueId().toString());
			}
		}
		Channel cnull = SimpleChatChannels.nullChannel;
		if(player.hasPermission(cnull.getPermission()))
		{
			if(!plugin.getMysqlHandler().exist(Type.USEDCHANNEL, "`uniqueidentifiername` = ? AND `player_uuid` = ?",
					cnull.getUniqueIdentifierName(), player.getUniqueId().toString()))
			{
				UsedChannel uc = new UsedChannel(cnull.getUniqueIdentifierName(), player.getUniqueId().toString(), true);
				plugin.getMysqlHandler().create(Type.USEDCHANNEL, uc);
			}
		} else
		{
			plugin.getMysqlHandler().deleteData(Type.USEDCHANNEL, "`uniqueidentifiername` = ? AND `player_uuid` = ?",
					cnull.getUniqueIdentifierName(), player.getUniqueId().toString());
		}
	}
	
	public String getChannelNameSuggestion(Channel c, PermanentChannel pc, TemporaryChannel tc)
	{
		if(pc != null)
		{
			return c.getInChatName().replace("%channel%", pc.getNameColor()+pc.getName());
		} if(tc != null) 
		{
			return c.getInChatName().replace("%channel%", tc.getName());
		} else
		{
			return c.getInChatName();
		}
	}
	
	public String getChannelSuggestion(String uniqueIdentifierName, PermanentChannel pc)
	{
		Channel c = plugin.getChannel(uniqueIdentifierName);
		if(c == null)
		{
			if(SimpleChatChannels.nullChannel.getUniqueIdentifierName().equals(uniqueIdentifierName))
			{
				c = SimpleChatChannels.nullChannel;
			} else
			{
				return "";
			}
		}
		return getChannelSuggestion(c, pc);
	}
	
	public String getChannelSuggestion(Channel c, PermanentChannel pc)
	{
		/*
		 * Temporary not used in check, there only a player can access one temp channel at time.
		 */
		if(pc != null)
		{
			return c.getSymbol()+pc.getSymbolExtra();
		} else
		{
			return c.getSymbol();
		}
	}
	
	public String getChannelHover(String uniqueIdentifierName)
	{
		Channel c = plugin.getChannel(uniqueIdentifierName);
		if(c == null)
		{
			if(SimpleChatChannels.nullChannel.getUniqueIdentifierName().equals(uniqueIdentifierName))
			{
				c = SimpleChatChannels.nullChannel;
			} else
			{
				return "/";
			}
		}
		return getChannelHover(c);
	}
	
	public String getChannelHover(Channel c)
	{
		return plugin.getYamlHandler().getLang().getString("ChatListener.ChannelHover")
				.replace("%channel%", c.getUniqueIdentifierName())
				.replace("%channelcolor%", c.getInChatColorMessage());
	}
	
	public String getPlayerMsgCommand(String playername)
	{
		return PluginSettings.settings.getCommands(KeyHandler.MSG)+playername;
	}
	
	public String getPlayerHover(String playername)
	{
		return plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover").replace("%player%", playername);
	}
	
	public void isAfk(Player player, Player target)
	{
		if(plugin.getPlayerTimes() != null)
		{
			if(!plugin.getPlayerTimes().isActive(target.getUniqueId()))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdMsg.IsAfk")));
			}
		}
	}
	
	public void updatePermanentChannels(PermanentChannel pc)
	{
		if(plugin.getMysqlHandler().exist(MysqlHandler.Type.PERMANENTCHANNEL, "`id` = ?", pc.getId()))
		{
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.PERMANENTCHANNEL, pc, "`id` = ?", pc.getId());
		}
	}
	
	public static boolean containsIgnoreCase(String message, String searchStr)     
	{
	    if(message == null || searchStr == null) return false;

	    final int length = searchStr.length();
	    if (length == 0)
	        return true;

	    for (int i = message.length() - length; i >= 0; i--) 
	    {
	        if (message.regionMatches(true, i, searchStr, 0, length))
	        {
	        	return true;
	        }
	    }
	    return false;
	}
	
	public static String convertUUIDToName(String uuid)
	{
		String name = null;
		if(plugin.getMysqlHandler().exist(MysqlHandler.Type.CHATUSER, "player_uuid = ?", uuid))
		{
			name = ((ChatUser) plugin.getMysqlHandler().getData(MysqlHandler.Type.CHATUSER,
					"player_uuid = ?", uuid)).getName();
			return name;
		}
		return null;
	}
	
	public static UUID convertNameToUUID(String playername)
	{
		UUID uuid = null;
		if(plugin.getMysqlHandler().exist(MysqlHandler.Type.CHATUSER, "player_name = ?", playername))
		{
			uuid = UUID.fromString(((ChatUser) plugin.getMysqlHandler().getData(MysqlHandler.Type.CHATUSER,
					"player_name = ?", playername)).getUUID());
			return uuid;
		}
		return null;
	}
	
	@SuppressWarnings("deprecation")
	public String convertItemStackToJson(ItemStack itemStack) //FIN
	{
		/*
		 * so baut man das manuell
		 * ItemStack is = createHoverItem(p, bookpath+".hover.item."+ar[2], bok);
		 * emptyword.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM,new BaseComponent[]{new TextComponent(convertItemStackToJson((is)))}));
		 */
	    // ItemStack methods to get a net.minecraft.server.ItemStack object for serialization
	    Class<?> craftItemStackClazz = ReflectionUtil.getOBCClass("inventory.CraftItemStack");
	    Method asNMSCopyMethod = ReflectionUtil.getMethod(craftItemStackClazz, "asNMSCopy", ItemStack.class);

	    // NMS Method to serialize a net.minecraft.server.ItemStack to a valid Json string
	    Class<?> nmsItemStackClazz = ReflectionUtil.getNMSClass("world.item.ItemStack");
	    Class<?> nbtTagCompoundClazz = ReflectionUtil.getNMSClass("nbt.NBTTagCompound");
	    //1.17 Method saveNmsItemStackMethod = ReflectionUtil.getMethod(nmsItemStackClazz, "save", nbtTagCompoundClazz);
	    Method saveNmsItemStackMethod = ReflectionUtil.getMethod(nmsItemStackClazz, "b", nbtTagCompoundClazz); //1.18.1

	    Object nmsNbtTagCompoundObj; // This will just be an empty NBTTagCompound instance to invoke the saveNms method
	    Object nmsItemStackObj; // This is the net.minecraft.server.ItemStack object received from the asNMSCopy method
	    Object itemAsJsonObject; // This is the net.minecraft.server.ItemStack after being put through saveNmsItem method

	    try {
	        nmsNbtTagCompoundObj = nbtTagCompoundClazz.newInstance();
	        nmsItemStackObj = asNMSCopyMethod.invoke(null, itemStack);
	        itemAsJsonObject = saveNmsItemStackMethod.invoke(nmsItemStackObj, nmsNbtTagCompoundObj);
	    } catch (Throwable t) {
	        t.printStackTrace();
	        return null;
	    }

	    // Return a string representation of the serialized object
	    return itemAsJsonObject.toString();
	}
	
	public boolean getTarget(Player player) 
    {
    	int range = 20;
    	List<Entity> nearbyE = player.getNearbyEntities(range, range, range);
    	ArrayList<LivingEntity> livingE = new ArrayList<LivingEntity>();
     
    	for (Entity e : nearbyE) {
    		if (e instanceof LivingEntity) 
    		{
    			livingE.add((LivingEntity) e);
    		}
    	}
     
    	LivingEntity target = null;
    	BlockIterator bItr = new BlockIterator(player, range);
    	Block block;
    	Location loc;
    	int bx, by, bz;
    	double ex, ey, ez;
    	// loop through player's line of sight
    	while (bItr.hasNext()) 
    	{
    		block = bItr.next();
    		bx = block.getX();
    		by = block.getY();
    		bz = block.getZ();
    		// check for entities near this block in the line of sight
    		for (LivingEntity e : livingE) 
    		{
    			loc = e.getLocation();
    			ex = loc.getX();
    			ey = loc.getY();
    			ez = loc.getZ();
    			if ((bx - .75 <= ex && ex <= bx + 1.75)
    					&& (bz - .75 <= ez && ez <= bz + 1.75)
    					&& (by - 1 <= ey && ey <= by + 2.5)) {
    				// entity is close enough, set target and stop
    				target = e;
    				break;
    			}
    		}
    	}
    	if(target==null)
    	{
    		return false;
    	}
    	return true;
    }
	
	public String toBase64itemStack(ItemStack item) throws IllegalStateException
    {
    	try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            BukkitObjectOutputStream dataOutput = new BukkitObjectOutputStream(outputStream);
            dataOutput.writeObject(item);
            dataOutput.close();
            return Base64Coder.encodeLines(outputStream.toByteArray());
        } catch (Exception e) {
            throw new IllegalStateException("Unable to save item stacks.", e);
        }
    }
    
    public ItemStack fromBase64itemStack(String data)
    {
    	try {
            ByteArrayInputStream inputStream = new ByteArrayInputStream(Base64Coder.decodeLines(data));
            BukkitObjectInputStream dataInput = new BukkitObjectInputStream(inputStream);
            ItemStack item = (ItemStack) dataInput.readObject();
            dataInput.close();
            return item;
        } catch (IOException e)
		{
			e.printStackTrace();
		} catch (ClassNotFoundException e)
		{
			e.printStackTrace();
		}
    	return null;
    }
}
