package main.java.me.avankziar.scc.spigot.ifh;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.chat.Channel;
import main.java.me.avankziar.scc.objects.chat.Components;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;
import main.java.me.avankziar.scc.spigot.handler.ChatHandler;
import net.md_5.bungee.api.chat.TextComponent;

public class ChatProvider implements main.java.me.avankziar.ifh.general.chat.Chat
{
	@Override
	public boolean supportRolePlayNames()
	{
		return false;
	}	
	
	@Override
	public boolean supportChannels()
	{
		return true;
	}
	
	@Override
	public boolean supportChatTitles()
	{
		return true;
	}
	
	@Override
	public boolean supportEmojis()
	{
		return true;
	}
	
	@Override
	public LinkedHashMap<String, String> getAllEmojis()
	{
		LinkedHashMap<String, String> map = ChatHandler.emojiList;
		return map;
	}
	
	@Override
	public TextComponent parseMessage(String rawMessage)
	{
		return parseMessage(rawMessage, SimpleChatChannels.getPlugin().getYamlHandler().getConfig().getString("BroadCast.UsingChannel"));
	}
	
	@Override
	public TextComponent parseMessage(String rawMessage, String channel)
	{
		SimpleChatChannels plugin = SimpleChatChannels.getPlugin();
		Channel usedChannel = plugin.getChannel(channel);
		if(usedChannel == null)
		{
			return null;
		}
		ChatHandler ch = new ChatHandler(plugin);
		Components components = ch.getComponent(Channel.ChatFormatPlaceholder.MESSAGE.getPlaceholder(),
				rawMessage, plugin.getServer().getConsoleSender(), null, new ArrayList<>(), new ArrayList<>(), usedChannel, null, null, null, null,
				usedChannel.getInChatColorMessage());
		TextComponent txc2 = ChatApi.tc("");
		txc2.setExtra(components.getComponentsWithMentions());
		return txc2;
	}
}