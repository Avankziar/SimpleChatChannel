package main.java.me.avankziar.scc.velocity.assistance;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.objects.Channel;
import main.java.me.avankziar.scc.general.objects.ChatUser;
import main.java.me.avankziar.scc.general.objects.IgnoreObject;
import main.java.me.avankziar.scc.general.objects.KeyHandler;
import main.java.me.avankziar.scc.general.objects.PermanentChannel;
import main.java.me.avankziar.scc.general.objects.ServerLocation;
import main.java.me.avankziar.scc.general.objects.StaticValues;
import main.java.me.avankziar.scc.general.objects.UsedChannel;
import main.java.me.avankziar.scc.velocity.SCC;
import main.java.me.avankziar.scc.velocity.handler.ChatHandler;
import main.java.me.avankziar.scc.velocity.objects.BypassPermission;
import main.java.me.avankziar.scc.velocity.objects.ChatUserHandler;
import main.java.me.avankziar.scc.velocity.objects.PluginSettings;
import main.java.me.avankziar.scc.velocity.objects.chat.TemporaryChannel;

public class Utility 
{
	private static SCC plugin;
	public static ArrayList<String> onlineplayers = new ArrayList<>();
	public static LinkedHashMap<String, LinkedHashMap<String, UsedChannel>> playerUsedChannels = new LinkedHashMap<>();
	
	public Utility(SCC plugin)
	{
		Utility.plugin = plugin;
	}
	
	public void sendToSpigot(Player player, int argsAmount , String cmd, String...objects)
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
        player.getCurrentServer().ifPresent(y -> y.sendPluginMessage(MinecraftChannelIdentifier.from(StaticValues.SCC_TOSPIGOT), streamout.toByteArray()));
	}
	
	public String removeColor(String msg)
	{
		return ChatHandler.stripColor(msg);
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
	
	public boolean getIgnored(Player target, Player player, boolean privatechat)
	{
		IgnoreObject io = (IgnoreObject) plugin.getMysqlHandler().getData(MysqlType.IGNOREOBJECT,
				"`player_uuid` = ? AND `ignore_uuid` = ?",
				target.getUniqueId().toString(), player.getUniqueId().toString());
		if(io != null)
		{
			if(privatechat)
			{
				if(player.hasPermission(BypassPermission.PERMBYPASSIGNORE))
				{
					player.sendMessage(ChatApi.tl(
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
					if(SCC.nullChannel.getUniqueIdentifierName().equals(uc.getUniqueIdentifierName()))
					{
						c = SCC.nullChannel;
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
		ChatUser cu = new ChatUser(player.getUniqueId().toString(), player.getUsername(),
				"", 0L, 0L, false, true, System.currentTimeMillis(), plugin.getYamlHandler().getConfig().getBoolean("JoinMessageDefaultValue"),
				new ServerLocation("", "default", 0.0, 0.0, 0.0, 0.0F, 0.0F));
		if(!plugin.getMysqlHandler().exist(MysqlType.CHATUSER,
				"`player_uuid` = ?", player.getUniqueId().toString()))
		{
			plugin.getMysqlHandler().create(MysqlType.CHATUSER, cu);
			for(Channel c : SCC.channels.values())
			{
				if(player.hasPermission(c.getPermission()))
				{
					UsedChannel uc = new UsedChannel(c.getUniqueIdentifierName(), player.getUniqueId().toString(), true);
					plugin.getMysqlHandler().create(MysqlType.USEDCHANNEL, uc);
				}
			}
			Channel cnull = SCC.nullChannel;
			if(cnull != null)
			{
				if(player.hasPermission(cnull.getPermission()))
				{
					UsedChannel uc = new UsedChannel(cnull.getUniqueIdentifierName(), player.getUniqueId().toString(), true);
					plugin.getMysqlHandler().create(MysqlType.USEDCHANNEL, uc);
				}
			}
		} else
		{
			cu = (ChatUser) plugin.getMysqlHandler().getData(MysqlType.CHATUSER, 
					"`player_uuid` = ?", player.getUniqueId().toString());
			updateUsedChannels(player);
		}
		return cu;
	}
	
	public void updateUsedChannels(Player player)
	{
		for(Channel c : SCC.channels.values())
		{
			if(player.hasPermission(c.getPermission()))
			{
				if(!plugin.getMysqlHandler().exist(MysqlType.USEDCHANNEL, "`uniqueidentifiername` = ? AND `player_uuid` = ?",
						c.getUniqueIdentifierName(), player.getUniqueId().toString()))
				{
					UsedChannel uc = new UsedChannel(c.getUniqueIdentifierName(), player.getUniqueId().toString(), true);
					plugin.getMysqlHandler().create(MysqlType.USEDCHANNEL, uc);
				}
			} else
			{
				plugin.getMysqlHandler().deleteData(MysqlType.USEDCHANNEL, "`uniqueidentifiername` = ? AND `player_uuid` = ?",
						c.getUniqueIdentifierName(), player.getUniqueId().toString());
			}
		}
		Channel cnull = SCC.nullChannel;
		if(cnull != null)
		{
			if(player.hasPermission(cnull.getPermission()))
			{
				if(!plugin.getMysqlHandler().exist(MysqlType.USEDCHANNEL, "`uniqueidentifiername` = ? AND `player_uuid` = ?",
						cnull.getUniqueIdentifierName(), player.getUniqueId().toString()))
				{
					UsedChannel uc = new UsedChannel(cnull.getUniqueIdentifierName(), player.getUniqueId().toString(), true);
					plugin.getMysqlHandler().create(MysqlType.USEDCHANNEL, uc);
				}
			} else
			{
				plugin.getMysqlHandler().deleteData(MysqlType.USEDCHANNEL, "`uniqueidentifiername` = ? AND `player_uuid` = ?",
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
			if(SCC.nullChannel.getUniqueIdentifierName().equals(uniqueIdentifierName))
			{
				c = SCC.nullChannel;
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
			if(SCC.nullChannel.getUniqueIdentifierName().equals(uniqueIdentifierName))
			{
				c = SCC.nullChannel;
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
	
	public void isAfk(Player player, Player targed)
	{
		if(plugin.getPlayerTimes() != null)
		{
			if(!plugin.getPlayerTimes().isActive(targed.getUniqueId()))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdMsg.IsAfk")));
			}
		}
	}
	
	public void updatePermanentChannels(PermanentChannel pc)
	{
		if(plugin.getMysqlHandler().exist(MysqlType.PERMANENTCHANNEL, "`id` = ?", pc.getId()))
		{
			plugin.getMysqlHandler().updateData(MysqlType.PERMANENTCHANNEL, pc, "`id` = ?", pc.getId());
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
		if(plugin.getMysqlHandler().exist(MysqlType.CHATUSER, "player_uuid = ?", uuid))
		{
			name = ((ChatUser) plugin.getMysqlHandler().getData(MysqlType.CHATUSER,
					"player_uuid = ?", uuid)).getName();
			return name;
		}
		return null;
	}
	
	public static UUID convertNameToUUID(String playername)
	{
		UUID uuid = null;
		if(plugin.getMysqlHandler().exist(MysqlType.CHATUSER, "player_name = ?", playername))
		{
			uuid = UUID.fromString(((ChatUser) plugin.getMysqlHandler().getData(MysqlType.CHATUSER,
					"player_name = ?", playername)).getUUID());
			return uuid;
		}
		return null;
	}
}
