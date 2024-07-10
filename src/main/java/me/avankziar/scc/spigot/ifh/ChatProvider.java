package main.java.me.avankziar.scc.spigot.ifh;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.objects.Channel;
import main.java.me.avankziar.scc.general.objects.ComponentsVelo;
import main.java.me.avankziar.scc.spigot.SCC;
import main.java.me.avankziar.scc.spigot.handler.ChatHandlerAdventure;
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
		LinkedHashMap<String, String> map = ChatHandlerAdventure.emojiList;
		return map;
	}
	
	@Override
	public net.md_5.bungee.api.chat.TextComponent parseMessage(String rawMessage)
	{
		return parseMessageToMD5Bungee(rawMessage);
	}
	
	@Override
	public net.md_5.bungee.api.chat.TextComponent parseMessage(String rawMessage, String channel)
	{
		return parseMessageToMD5Bungee(rawMessage, channel);
	}
	
	@Override
	public net.md_5.bungee.api.chat.TextComponent parseMessageToMD5Bungee(String rawMessage)
	{
		return parseMessage(rawMessage, SCC.getPlugin().getYamlHandler().getConfig().getString("BroadCast.UsingChannel"));
	}
	
	@Override
	public net.md_5.bungee.api.chat.TextComponent parseMessageToMD5Bungee(String rawMessage, String channel)
	{
		SCC plugin = SCC.getPlugin();
		Channel usedChannel = plugin.getChannel(channel);
		if(usedChannel == null)
		{
			return null;
		}
		ChatHandlerAdventure ch = new ChatHandlerAdventure(plugin);
		ComponentsVelo components = ch.getComponent(Channel.ChatFormatPlaceholder.MESSAGE.getPlaceholder(),
				rawMessage, plugin.getServer().getConsoleSender(), null, new ArrayList<>(), new ArrayList<>(), usedChannel, null, null, null, null,
				usedChannel.getInChatColorMessage());
		net.md_5.bungee.api.chat.TextComponent txc2 = new TextComponent("");
		txc2.setExtra(Arrays.asList(ChatApi.tctl(String.join("", components.getComponentsWithMentions()))));
		return txc2;
	}
	
	@Override
	public net.kyori.adventure.text.Component parseMessageToAdventure(String rawMessage)
	{
		return parseMessageToAdventure(rawMessage, SCC.getPlugin().getYamlHandler().getConfig().getString("BroadCast.UsingChannel"));
	}
	
	@Override
	public net.kyori.adventure.text.Component parseMessageToAdventure(String rawMessage, String channel)
	{
		SCC plugin = SCC.getPlugin();
		Channel usedChannel = plugin.getChannel(channel);
		if(usedChannel == null)
		{
			return null;
		}
		ChatHandlerAdventure ch = new ChatHandlerAdventure(plugin);
		ComponentsVelo components = ch.getComponent(Channel.ChatFormatPlaceholder.MESSAGE.getPlaceholder(),
				rawMessage, plugin.getServer().getConsoleSender(), null, new ArrayList<>(), new ArrayList<>(), usedChannel, null, null, null, null,
				usedChannel.getInChatColorMessage());
		return ChatApi.tl(String.join("", components.getComponentsWithMentions()));
	}
}