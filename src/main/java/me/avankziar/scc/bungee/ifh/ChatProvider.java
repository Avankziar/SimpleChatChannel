package main.java.me.avankziar.scc.bungee.ifh;

import java.util.LinkedHashMap;
import java.util.UUID;

import main.java.me.avankziar.ifh.general.chat.Chat;
import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import net.md_5.bungee.api.chat.TextComponent;

public class ChatProvider implements Chat
{
	@Override
	public String[] getActiveChannels(UUID arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getAllChannels()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getAllChatTitles()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LinkedHashMap<String, String> getAllEmojis()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getChannels(UUID arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getChatTitles(UUID arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getProvider()
	{
		return SimpleChatChannels.pluginName;
	}

	@Override
	public String getRolePlayName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TextComponent parseMessage(String arg0)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public TextComponent parseMessage(String arg0, String arg1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean supportChannels()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean supportChatTitles()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean supportEmojis()
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean supportRolePlayNames()
	{
		// TODO Auto-generated method stub
		return false;
	}
	
}