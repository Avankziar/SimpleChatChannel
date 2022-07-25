package main.java.me.avankziar.scc.bungee.ifh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;
import java.util.logging.Level;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.scc.objects.chat.ChatTitle;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ChatTitleProvider implements main.java.me.avankziar.ifh.general.chat.ChatTitle
{
	@Override
	public ArrayList<String> getChatTitle()
	{
		ArrayList<String> list = new ArrayList<>();
		for(ChatTitle ct : SimpleChatChannels.chatTitlesPrefix)
		{
			list.add(ct.getUniqueIdentifierName());
		}
		for(ChatTitle ct : SimpleChatChannels.chatTitlesSuffix)
		{
			list.add(ct.getUniqueIdentifierName());
		}
		return list;
	}
	
	@Override
	public ArrayList<String> getChatTitle(UUID uuid)
	{
		ProxiedPlayer player = BungeeCord.getInstance().getPlayer(uuid);
		if(player == null)
		{
			return null;
		}
		ArrayList<String> list = new ArrayList<>();
		for(ChatTitle ct : SimpleChatChannels.chatTitlesPrefix)
		{
			if(player.hasPermission(ct.getPermission()))
			{
				list.add(ct.getUniqueIdentifierName());
			}
		}
		for(ChatTitle ct : SimpleChatChannels.chatTitlesSuffix)
		{
			if(player.hasPermission(ct.getPermission()))
			{
				list.add(ct.getUniqueIdentifierName());
			}
		}
		return list;
	}
	
	@Override
	public boolean registerChatTitle(String uniquechattitle, boolean isPrefix, String inChatName,
			String inChatColorCode, String suggestCommand, String hover, String permission, int weight,
			boolean registerOnBungee)
	{
		if(uniquechattitle == null || inChatName == null || inChatColorCode == null
				|| suggestCommand == null || hover == null || permission == null)
		{
			return false;
		}
		for(ChatTitle ct : SimpleChatChannels.chatTitlesPrefix)
		{
			if(ct.getUniqueIdentifierName().equalsIgnoreCase(uniquechattitle))
			{
				return false;
			}
		}
		for(ChatTitle ct : SimpleChatChannels.chatTitlesSuffix)
		{
			if(ct.getUniqueIdentifierName().equalsIgnoreCase(uniquechattitle))
			{
				return false;
			}
		}
		ChatTitle ct = new ChatTitle(uniquechattitle,
				isPrefix,
				inChatName,
				inChatColorCode,
				suggestCommand,
				hover,
				permission,
				weight);
		if(isPrefix)
		{
			SimpleChatChannels.chatTitlesPrefix.add(ct);
		} else
		{
			SimpleChatChannels.chatTitlesSuffix.add(ct);
		}
		SimpleChatChannels.chatTitlesPrefix.sort(Comparator.comparing(ChatTitle::getWeight));
		Collections.reverse(SimpleChatChannels.chatTitlesPrefix);
		SimpleChatChannels.chatTitlesSuffix.sort(Comparator.comparing(ChatTitle::getWeight));
		Collections.reverse(SimpleChatChannels.chatTitlesSuffix);
		SimpleChatChannels.log.log(Level.INFO, "Register "+uniquechattitle+" ChatTitle!");
		return true;
	}
	
	@Override
	public Boolean isPrefix(String uniquechattitle)
	{
		for(ChatTitle ct : SimpleChatChannels.chatTitlesPrefix)
		{
			if(uniquechattitle.equalsIgnoreCase(ct.getUniqueIdentifierName()))
			{
				return ct.isPrefix();
			}
		}
		for(ChatTitle ct : SimpleChatChannels.chatTitlesSuffix)
		{
			if(uniquechattitle.equalsIgnoreCase(ct.getUniqueIdentifierName()))
			{
				return ct.isPrefix();
			}
		}
		return null;
	}
	
	@Override
	public String getInChatName(String uniquechattitle)
	{
		for(ChatTitle ct : SimpleChatChannels.chatTitlesPrefix)
		{
			if(uniquechattitle.equalsIgnoreCase(ct.getUniqueIdentifierName()))
			{
				return ct.getInChatName();
			}
		}
		for(ChatTitle ct : SimpleChatChannels.chatTitlesSuffix)
		{
			if(uniquechattitle.equalsIgnoreCase(ct.getUniqueIdentifierName()))
			{
				return ct.getInChatName();
			}
		}
		return null;
	}
	
	@Override
	public String getInChatColor(String uniquechattitle)
	{
		for(ChatTitle ct : SimpleChatChannels.chatTitlesPrefix)
		{
			if(uniquechattitle.equalsIgnoreCase(ct.getUniqueIdentifierName()))
			{
				return ct.getInChatColorCode();
			}
		}
		for(ChatTitle ct : SimpleChatChannels.chatTitlesSuffix)
		{
			if(uniquechattitle.equalsIgnoreCase(ct.getUniqueIdentifierName()))
			{
				return ct.getInChatColorCode();
			}
		}
		return null;
	}
	
	@Override
	public String getClickEvent(String uniquechattitle)
	{
		for(ChatTitle ct : SimpleChatChannels.chatTitlesPrefix)
		{
			if(uniquechattitle.equalsIgnoreCase(ct.getUniqueIdentifierName()))
			{
				return ct.getClick();
			}
		}
		for(ChatTitle ct : SimpleChatChannels.chatTitlesSuffix)
		{
			if(uniquechattitle.equalsIgnoreCase(ct.getUniqueIdentifierName()))
			{
				return ct.getClick();
			}
		}
		return null;
	}
	
	@Override
	public String getHoverEvent(String uniquechattitle)
	{
		for(ChatTitle ct : SimpleChatChannels.chatTitlesPrefix)
		{
			if(uniquechattitle.equalsIgnoreCase(ct.getUniqueIdentifierName()))
			{
				return ct.getHover();
			}
		}
		for(ChatTitle ct : SimpleChatChannels.chatTitlesSuffix)
		{
			if(uniquechattitle.equalsIgnoreCase(ct.getUniqueIdentifierName()))
			{
				return ct.getHover();
			}
		}
		return null;
	}
	
	@Override
	public String getPermission(String uniquechattitle)
	{
		for(ChatTitle ct : SimpleChatChannels.chatTitlesPrefix)
		{
			if(uniquechattitle.equalsIgnoreCase(ct.getUniqueIdentifierName()))
			{
				return ct.getPermission();
			}
		}
		for(ChatTitle ct : SimpleChatChannels.chatTitlesSuffix)
		{
			if(uniquechattitle.equalsIgnoreCase(ct.getUniqueIdentifierName()))
			{
				return ct.getPermission();
			}
		}
		return null;
	}
	
	@Override
	public Integer getWeight(String uniquechattitle)
	{
		for(ChatTitle ct : SimpleChatChannels.chatTitlesPrefix)
		{
			if(uniquechattitle.equalsIgnoreCase(ct.getUniqueIdentifierName()))
			{
				return ct.getWeight();
			}
		}
		for(ChatTitle ct : SimpleChatChannels.chatTitlesSuffix)
		{
			if(uniquechattitle.equalsIgnoreCase(ct.getUniqueIdentifierName()))
			{
				return ct.getWeight();
			}
		}
		return null;
	}
}