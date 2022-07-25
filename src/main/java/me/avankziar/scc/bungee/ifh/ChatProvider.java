package main.java.me.avankziar.scc.bungee.ifh;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import main.java.me.avankziar.ifh.general.chat.Chat;
import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.scc.bungee.handler.ChatHandler;
import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.chat.Channel;
import main.java.me.avankziar.scc.objects.chat.Components;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.chat.TextComponent;

public class ChatProvider implements Chat
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
		SimpleChatChannels plugin = SimpleChatChannels.getPlugin();
		Channel usedChannel = plugin.getChannel(plugin.getYamlHandler().getConfig().getString("BroadCast.UsingChannel"));
		if(usedChannel == null)
		{
			return null;
		}
		ChatHandler ch = new ChatHandler(plugin);
		Components components = ch.getComponent(Channel.ChatFormatPlaceholder.MESSAGE.getPlaceholder(),
				rawMessage, BungeeCord.getInstance().getConsole(), null, new ArrayList<>(), new ArrayList<>(), usedChannel, null, null, null, null,
				usedChannel.getInChatColorMessage());
		TextComponent txc2 = ChatApi.tc("");
		txc2.setExtra(components.getComponentsWithMentions());
		return txc2;
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
				rawMessage, BungeeCord.getInstance().getConsole(), null, new ArrayList<>(), new ArrayList<>(), usedChannel, null, null, null, null,
				usedChannel.getInChatColorMessage());
		TextComponent txc2 = ChatApi.tc("");
		txc2.setExtra(components.getComponentsWithMentions());
		return txc2;
	}
}