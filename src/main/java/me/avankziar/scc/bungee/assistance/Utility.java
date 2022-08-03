package main.java.me.avankziar.scc.bungee.assistance;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.scc.bungee.database.MysqlHandler;
import main.java.me.avankziar.scc.bungee.database.MysqlHandler.Type;
import main.java.me.avankziar.scc.bungee.objects.BypassPermission;
import main.java.me.avankziar.scc.bungee.objects.ChatUserHandler;
import main.java.me.avankziar.scc.bungee.objects.PluginSettings;
import main.java.me.avankziar.scc.bungee.objects.chat.TemporaryChannel;
import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.ChatUser;
import main.java.me.avankziar.scc.objects.KeyHandler;
import main.java.me.avankziar.scc.objects.PermanentChannel;
import main.java.me.avankziar.scc.objects.ServerLocation;
import main.java.me.avankziar.scc.objects.StaticValues;
import main.java.me.avankziar.scc.objects.chat.Channel;
import main.java.me.avankziar.scc.objects.chat.IgnoreObject;
import main.java.me.avankziar.scc.objects.chat.UsedChannel;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Utility 
{
	private static SimpleChatChannels plugin;
	public static ArrayList<String> onlineplayers = new ArrayList<>();
	public static LinkedHashMap<String, LinkedHashMap<String, UsedChannel>> playerUsedChannels = new LinkedHashMap<>();
	
	public Utility(SimpleChatChannels plugin)
	{
		Utility.plugin = plugin;
	}
	
	public void sendToSpigot(ProxiedPlayer player, int argsAmount , String cmd, String...objects)
	{
		ByteArrayOutputStream streamout = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(streamout);
        try {
        	out.writeUTF(StaticValues.SCC_TASK_ARG);
			out.writeUTF(player.getUniqueId().toString());
			out.writeUTF(cmd);
			out.writeInt(argsAmount);
			for(String o : objects)
			{
				out.writeUTF(o);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.getServer().getInfo().sendData(StaticValues.SCC_TOSPIGOT, streamout.toByteArray());
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
		if(percent >= percentOfSimilarity)
		{
			return true;
		}
		return false;
	}
	
	public boolean getIgnored(ProxiedPlayer target, ProxiedPlayer player, boolean privatechat)
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
					player.sendMessage(ChatApi.tctl(
							plugin.getYamlHandler().getLang().getString("ChatListener.PlayerIgnoreYou")));
					return false;
				}
			}
			return true;
		}
		return false;
	}
	
	public long getMutedTime(ProxiedPlayer player)
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
				ac += c.getJoinPart()+comma;
			}
		}
		if(cu.isOptionSpy()) {ac += plugin.getYamlHandler().getLang().getString("Join.Spy")+comma;}
		return ac.substring(0, ac.length()-2);
	}
	
	public boolean containsBadWords(ProxiedPlayer player, String message)
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
	
	public ChatUser controlUsedChannels(ProxiedPlayer player)
	{
		ChatUser cu = new ChatUser(player.getUniqueId().toString(), player.getName(),
				"", 0L, 0L, false, true, System.currentTimeMillis(), plugin.getYamlHandler().getConfig().getBoolean("JoinMessageDefaultValue"),
				new ServerLocation("", "default", 0.0, 0.0, 0.0, 0.0F, 0.0F));
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
			Channel cnull = SimpleChatChannels.nullChannel;
			if(cnull != null)
			{
				if(player.hasPermission(cnull.getPermission()))
				{
					UsedChannel uc = new UsedChannel(cnull.getUniqueIdentifierName(), player.getUniqueId().toString(), true);
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
	
	public void updateUsedChannels(ProxiedPlayer player)
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
		if(cnull != null)
		{
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
			if(c.getSymbol().equalsIgnoreCase("null"))
			{
				return "";
			}
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
		if(c == null)
		{
			return "";
		}
		return plugin.getYamlHandler().getLang().getString("ChatListener.ChannelHover")
				.replace("%channel%", c.getUniqueIdentifierName())
				.replace("%channelcolor%", c.getInChatColorMessage());
	}
	
	public String getPlayerMsgCommand(String playername)
	{
		return PluginSettings.settings.getCommands(KeyHandler.MSG)+playername+" ";
	}
	
	public String getPlayerHover(String playername)
	{
		return plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover").replace("%player%", playername);
	}
	
	public boolean existMethod(Class<?> externclass, String method)
	{
	    try 
	    {
	    	Method[] mtds = externclass.getMethods();
	    	for(Method methods : mtds)
	    	{
	    		if(methods.getName().equalsIgnoreCase(method))
	    		{
	    	    	//SimpleChatChannels.log.info("Method "+method+" in Class "+externclass.getName()+" loaded!");
	    	    	return true;
	    		}
	    	}
	    	return false;
	    } catch (Exception e) 
	    {
	    	return false;
	    }
	}
	
	public void isAfk(ProxiedPlayer player, ProxiedPlayer targed)
	{
		if(plugin.getPlayerTimes() != null)
		{
			if(!plugin.getPlayerTimes().isActive(targed.getUniqueId()))
			{
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMsg.IsAfk")));
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
}
