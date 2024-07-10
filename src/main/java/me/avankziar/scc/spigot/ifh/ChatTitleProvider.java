package main.java.me.avankziar.scc.spigot.ifh;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.general.objects.ChatTitle;
import main.java.me.avankziar.scc.general.objects.StaticValues;
import main.java.me.avankziar.scc.spigot.SCC;

public class ChatTitleProvider implements main.java.me.avankziar.ifh.general.chat.ChatTitle
{
	@Override
	public ArrayList<String> getChatTitle()
	{
		ArrayList<String> list = new ArrayList<>();
		for(ChatTitle ct : SCC.chatTitlesPrefix)
		{
			list.add(ct.getUniqueIdentifierName());
		}
		for(ChatTitle ct : SCC.chatTitlesSuffix)
		{
			list.add(ct.getUniqueIdentifierName());
		}
		return list;
	}
	
	@Override
	public ArrayList<String> getChatTitle(UUID uuid)
	{
		Player player = SCC.getPlugin().getServer().getPlayer(uuid);
		if(player == null)
		{
			return null;
		}
		ArrayList<String> list = new ArrayList<>();
		for(ChatTitle ct : SCC.chatTitlesPrefix)
		{
			if(player.hasPermission(ct.getPermission()))
			{
				list.add(ct.getUniqueIdentifierName());
			}
		}
		for(ChatTitle ct : SCC.chatTitlesSuffix)
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
		for(ChatTitle ct : SCC.chatTitlesPrefix)
		{
			if(ct.getUniqueIdentifierName().equalsIgnoreCase(uniquechattitle))
			{
				return false;
			}
		}
		for(ChatTitle ct : SCC.chatTitlesSuffix)
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
			SCC.chatTitlesPrefix.add(ct);
		} else
		{
			SCC.chatTitlesSuffix.add(ct);
		}
		SCC.chatTitlesPrefix.sort(Comparator.comparing(ChatTitle::getWeight));
		Collections.reverse(SCC.chatTitlesPrefix);
		SCC.chatTitlesSuffix.sort(Comparator.comparing(ChatTitle::getWeight));
		Collections.reverse(SCC.chatTitlesSuffix);
		SCC.logger.log(Level.INFO, "Register "+uniquechattitle+" ChatTitle!");
		if(registerOnBungee)
		{
			send(uniquechattitle, isPrefix, inChatName, inChatColorCode, suggestCommand, hover, permission, weight);
		}
		return true;
	}
	
	@Override
	public Boolean isPrefix(String uniquechattitle)
	{
		for(ChatTitle ct : SCC.chatTitlesPrefix)
		{
			if(uniquechattitle.equalsIgnoreCase(ct.getUniqueIdentifierName()))
			{
				return ct.isPrefix();
			}
		}
		for(ChatTitle ct : SCC.chatTitlesSuffix)
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
		for(ChatTitle ct : SCC.chatTitlesPrefix)
		{
			if(uniquechattitle.equalsIgnoreCase(ct.getUniqueIdentifierName()))
			{
				return ct.getInChatName();
			}
		}
		for(ChatTitle ct : SCC.chatTitlesSuffix)
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
		for(ChatTitle ct : SCC.chatTitlesPrefix)
		{
			if(uniquechattitle.equalsIgnoreCase(ct.getUniqueIdentifierName()))
			{
				return ct.getInChatColorCode();
			}
		}
		for(ChatTitle ct : SCC.chatTitlesSuffix)
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
		for(ChatTitle ct : SCC.chatTitlesPrefix)
		{
			if(uniquechattitle.equalsIgnoreCase(ct.getUniqueIdentifierName()))
			{
				return ct.getClick();
			}
		}
		for(ChatTitle ct : SCC.chatTitlesSuffix)
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
		for(ChatTitle ct : SCC.chatTitlesPrefix)
		{
			if(uniquechattitle.equalsIgnoreCase(ct.getUniqueIdentifierName()))
			{
				return ct.getHover();
			}
		}
		for(ChatTitle ct : SCC.chatTitlesSuffix)
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
		for(ChatTitle ct : SCC.chatTitlesPrefix)
		{
			if(uniquechattitle.equalsIgnoreCase(ct.getUniqueIdentifierName()))
			{
				return ct.getPermission();
			}
		}
		for(ChatTitle ct : SCC.chatTitlesSuffix)
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
		for(ChatTitle ct : SCC.chatTitlesPrefix)
		{
			if(uniquechattitle.equalsIgnoreCase(ct.getUniqueIdentifierName()))
			{
				return ct.getWeight();
			}
		}
		for(ChatTitle ct : SCC.chatTitlesSuffix)
		{
			if(uniquechattitle.equalsIgnoreCase(ct.getUniqueIdentifierName()))
			{
				return ct.getWeight();
			}
		}
		return null;
	}

	private void send(String uniquechattitle, boolean isPrefix, String inChatName,
			String inChatColorCode, String suggestCommand, String hover, String permission, int weight)
    {
    	ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.SCC_REGISTERCHATTITLE);
			out.writeUTF(uniquechattitle);
			out.writeBoolean(isPrefix);
			out.writeUTF(inChatName);
			out.writeUTF(inChatColorCode);
			out.writeUTF(suggestCommand);
			out.writeUTF(hover);
			out.writeUTF(permission);
			out.writeInt(weight);
		} catch (IOException e) {
			e.printStackTrace();
		}
        for(Player player : Bukkit.getOnlinePlayers())
        {
        	if(player != null && player.isOnline())
        	{
        		player.sendPluginMessage(SCC.getPlugin(), StaticValues.SCC_TOPROXY, stream.toByteArray());
        		break;
        	}
        }
    }
}