package main.java.me.avankziar.scc.bungee.ifh;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.logging.Level;

import main.java.me.avankziar.scc.bungee.SCC;
import main.java.me.avankziar.scc.bungee.assistance.Utility;
import main.java.me.avankziar.scc.general.objects.Channel;
import main.java.me.avankziar.scc.general.objects.UsedChannel;

public class ChannelProvider implements main.java.me.avankziar.ifh.general.chat.Channel
{	
	@Override
	public ArrayList<String> getChannels()
	{
		ArrayList<String> list = new ArrayList<>();
		for(String c : SCC.channels.keySet())
		{
			list.add(c);
		}
		return list;
	}
	
	@Override
	public ArrayList<String> getChannels(UUID uuid)
	{
		ArrayList<String> list = new ArrayList<>();
		LinkedHashMap<String, UsedChannel> map = Utility.playerUsedChannels.get(uuid.toString());
		for(String c : SCC.channels.keySet())
		{
			if(map.containsKey(c))
			{
				list.add(c);
			}
		}
		return list;
	}
	
	@Override
	public ArrayList<String> getActiveChannels(UUID uuid)
	{
		ArrayList<String> list = new ArrayList<>();
		for(Entry<String, UsedChannel> c : Utility.playerUsedChannels.get(uuid.toString()).entrySet())
		{
			if(c.getValue().isUsed())
			{
				list.add(c.getKey());
			}			
		}
		return list;
	}
	
	@Override
	public boolean registerChannel(
			String uniqueChannelName, String symbol, String inChatName, String inChatColorMessage,
			String permission, String joinPart, String chatFormat,
			boolean useSpecificServer, boolean useSpecificWorld, int useBlockRadius, 
			long minimumTimeBetweenMessages, long minimumTimeBetweenSameMessage, double percentOfSimiliarityOrLess,
			String timeColor, String playernameCustomColor,
			String seperatorBetweenPrefix, String seperatorBetweenSuffix,
			String mentionSound,
			boolean useColor, boolean useItemReplacer, boolean useBookReplacer,
			boolean useRunCommandReplacer, boolean useSuggestCommandReplacer, boolean useWebsiteReplacer,
			boolean useEmojiReplacer, boolean useMentionReplacer, boolean usePositionReplacer,
			boolean registerOnBungee)
	{
		if(uniqueChannelName == null || symbol == null || inChatName == null || inChatColorMessage == null
				|| permission == null || joinPart == null || chatFormat == null || timeColor == null
				|| playernameCustomColor == null || seperatorBetweenPrefix == null || seperatorBetweenSuffix == null
				|| mentionSound == null 
				|| uniqueChannelName.equalsIgnoreCase("permanent") || uniqueChannelName.equalsIgnoreCase("temporary")
				|| uniqueChannelName.equalsIgnoreCase("private"))
		{
			return false;
		}
		LinkedHashMap<String, String> serverReplacerMap = new LinkedHashMap<>();
		LinkedHashMap<String, String> serverCommandMap = new LinkedHashMap<>();
		LinkedHashMap<String, String> serverHoverMap = new LinkedHashMap<>();
		LinkedHashMap<String, String> worldReplacerMap = new LinkedHashMap<>();
		LinkedHashMap<String, String> worldCommandMap = new LinkedHashMap<>();
		LinkedHashMap<String, String> worldHoverMap = new LinkedHashMap<>();
		Channel c = new Channel(
				uniqueChannelName,
				false,
				symbol,
				inChatName,
				inChatColorMessage,
				permission,
				joinPart,
				chatFormat,
				new ArrayList<>(),
				new ArrayList<>(),
				useSpecificServer,
				useSpecificWorld,
				useBlockRadius,
				minimumTimeBetweenMessages,
				minimumTimeBetweenSameMessage,
				percentOfSimiliarityOrLess,
				timeColor,
				playernameCustomColor,
				"&r",
				seperatorBetweenPrefix,
				seperatorBetweenSuffix,
				mentionSound,
				serverReplacerMap, serverCommandMap, serverHoverMap,
				worldReplacerMap, worldCommandMap, worldHoverMap,
				useColor, useItemReplacer, useBookReplacer,
				useRunCommandReplacer, useSuggestCommandReplacer, useWebsiteReplacer,
				useEmojiReplacer, useMentionReplacer, usePositionReplacer);
		SCC.logger.log(Level.INFO, "Register "+c.getUniqueIdentifierName()+" Channel!");
		return true;
	}
	
	@Override
	public boolean registerChannel(
			String uniqueChannelName, String symbol, String inChatName, String inChatColorMessage,
			String permission, String joinPart, String chatFormat,
			String timeColor, String playernameCustomColor,
			String mentionSound,
			boolean useColor, boolean useItemReplacer, boolean useBookReplacer,
			boolean useRunCommandReplacer, boolean useSuggestCommandReplacer, boolean useWebsiteReplacer,
			boolean useEmojiReplacer, boolean useMentionReplacer, boolean usePositionReplacer,
			boolean registerOnBungee)
	{
		return registerChannel(uniqueChannelName, symbol, inChatName, inChatColorMessage,
				permission, joinPart, chatFormat,
				false, false, 0, 500L, 1000L, 75.0, 
				timeColor, playernameCustomColor,
				"", "", mentionSound, 
				useColor, useItemReplacer, useBookReplacer, 
				useRunCommandReplacer, useSuggestCommandReplacer,
				useWebsiteReplacer, useEmojiReplacer,
				useMentionReplacer, usePositionReplacer, registerOnBungee);
	}
	
	@Override
	public String getSymbol(String uniqueChannelName)
	{
		for(Entry<String, Channel> c : SCC.channels.entrySet())
		{
			if(uniqueChannelName.equalsIgnoreCase(c.getKey()))
			{
				return c.getValue().getSymbol();
			}
		}
		return null;
	}
	
	@Override
	public String getInChatName(String uniqueChannelName)
	{
		for(Entry<String, Channel> c : SCC.channels.entrySet())
		{
			if(uniqueChannelName.equalsIgnoreCase(c.getKey()))
			{
				return c.getValue().getInChatName();
			}
		}
		return null;
	}
	
	@Override
	public String getInChatColor(String uniqueChannelName)
	{
		for(Entry<String, Channel> c : SCC.channels.entrySet())
		{
			if(uniqueChannelName.equalsIgnoreCase(c.getKey()))
			{
				return c.getValue().getInChatColorMessage();
			}
		}
		return null;
	}
	
	@Override
	public String getPermission(String uniqueChannelName)
	{
		for(Entry<String, Channel> c : SCC.channels.entrySet())
		{
			if(uniqueChannelName.equalsIgnoreCase(c.getKey()))
			{
				return c.getValue().getPermission();
			}
		}
		return null;
	}
	
	@Override
	public String getChatFormat(String uniqueChannelName)
	{
		for(Entry<String, Channel> c : SCC.channels.entrySet())
		{
			if(uniqueChannelName.equalsIgnoreCase(c.getKey()))
			{
				return c.getValue().getChatFormat();
			}
		}
		return null;
	}
	
	@Override
	public Boolean isSpecificServer(String uniqueChannelName)
	{
		for(Entry<String, Channel> c : SCC.channels.entrySet())
		{
			if(uniqueChannelName.equalsIgnoreCase(c.getKey()))
			{
				return c.getValue().isSpecificServer();
			}
		}
		return null;
	}
	
	@Override
	public Boolean isSpecificWorld(String uniqueChannelName)
	{
		for(Entry<String, Channel> c : SCC.channels.entrySet())
		{
			if(uniqueChannelName.equalsIgnoreCase(c.getKey()))
			{
				return c.getValue().isSpecificWorld();
			}
		}
		return null;
	}
	
	@Override
	public Boolean hasBlockRadius(String uniqueChannelName)
	{
		for(Entry<String, Channel> c : SCC.channels.entrySet())
		{
			if(uniqueChannelName.equalsIgnoreCase(c.getKey()))
			{
				return c.getValue().getBlockRadius() > 0;
			}
		}
		return null;
	}
	
	@Override
	public Integer getBlockRadius(String uniqueChannelName)
	{
		for(Entry<String, Channel> c : SCC.channels.entrySet())
		{
			if(uniqueChannelName.equalsIgnoreCase(c.getKey()))
			{
				return c.getValue().getBlockRadius();
			}
		}
		return null;
	}
	
	@Override
	public String getMentionSound(String uniqueChannelName)
	{
		for(Entry<String, Channel> c : SCC.channels.entrySet())
		{
			if(uniqueChannelName.equalsIgnoreCase(c.getKey()))
			{
				return c.getValue().getMentionSound();
			}
		}
		return null;
	}
	
	@Override
	public boolean isUsedColor(String uniqueChannelName)
	{
		for(Entry<String, Channel> c : SCC.channels.entrySet())
		{
			if(uniqueChannelName.equalsIgnoreCase(c.getKey()))
			{
				return c.getValue().isUseColor();
			}
		}
		return false;
	}
	
	@Override
	public boolean isUsedItemReplacer(String uniqueChannelName)
	{
		for(Entry<String, Channel> c : SCC.channels.entrySet())
		{
			if(uniqueChannelName.equalsIgnoreCase(c.getKey()))
			{
				return c.getValue().isUseItemReplacer();
			}
		}
		return false;
	}
	
	@Override
	public boolean isUsedBookReplacer(String uniqueChannelName)
	{
		for(Entry<String, Channel> c : SCC.channels.entrySet())
		{
			if(uniqueChannelName.equalsIgnoreCase(c.getKey()))
			{
				return c.getValue().isUseBookReplacer();
			}
		}
		return false;
	}

	@Override
	public boolean isUsedRunCommandReplacer(String uniqueChannelName)
	{
		for(Entry<String, Channel> c : SCC.channels.entrySet())
		{
			if(uniqueChannelName.equalsIgnoreCase(c.getKey()))
			{
				return c.getValue().isUseRunCommandReplacer();
			}
		}
		return false;
	}

	@Override
	public boolean isUsedSuggestCommandReplacer(String uniqueChannelName)
	{
		for(Entry<String, Channel> c : SCC.channels.entrySet())
		{
			if(uniqueChannelName.equalsIgnoreCase(c.getKey()))
			{
				return c.getValue().isUseSuggestCommandReplacer();
			}
		}
		return false;
	}
	
	@Override
	public boolean isUsedWebsiteReplacer(String uniqueChannelName)
	{
		for(Entry<String, Channel> c : SCC.channels.entrySet())
		{
			if(uniqueChannelName.equalsIgnoreCase(c.getKey()))
			{
				return c.getValue().isUseWebsiteReplacer();
			}
		}
		return false;
	}
	
	@Override
	public boolean isUsedEmojiReplacer(String uniqueChannelName)
	{
		for(Entry<String, Channel> c : SCC.channels.entrySet())
		{
			if(uniqueChannelName.equalsIgnoreCase(c.getKey()))
			{
				return c.getValue().isUseEmojiReplacer();
			}
		}
		return false;
	}
	
	@Override
	public boolean isUsedMentionReplacer(String uniqueChannelName)
	{
		for(Entry<String, Channel> c : SCC.channels.entrySet())
		{
			if(uniqueChannelName.equalsIgnoreCase(c.getKey()))
			{
				return c.getValue().isUseMentionReplacer();
			}
		}
		return false;
	}
	
	@Override
	public boolean isUsedPositionReplacer(String uniqueChannelName)
	{
		for(Entry<String, Channel> c : SCC.channels.entrySet())
		{
			if(uniqueChannelName.equalsIgnoreCase(c.getKey()))
			{
				return c.getValue().isUsePositionReplacer();
			}
		}
		return false;
	}
}