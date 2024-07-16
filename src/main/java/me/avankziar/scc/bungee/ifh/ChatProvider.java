package main.java.me.avankziar.scc.bungee.ifh;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import me.avankziar.ifh.general.chat.Chat;
import main.java.me.avankziar.scc.bungee.SCC;
import main.java.me.avankziar.scc.bungee.handler.ChatHandler;
import main.java.me.avankziar.scc.general.assistance.ChatApiOld;
import main.java.me.avankziar.scc.general.objects.Channel;
import main.java.me.avankziar.scc.general.objects.Components;
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
		ChatHandler ch = new ChatHandler(plugin);
		Components components = ch.getComponent(Channel.ChatFormatPlaceholder.MESSAGE.getPlaceholder(),
				rawMessage, SCC.getPlugin().getProxy().getConsole(), null, new ArrayList<>(), new ArrayList<>(), usedChannel, null, null, null, null,
				usedChannel.getInChatColorMessage());
		TextComponent txc2 = ChatApiOld.tc("");
		txc2.setExtra(components.getComponentsWithMentions());
		return txc2;
	}
	
	@Override
	public net.kyori.adventure.text.Component parseMessageToAdventure(String rawMessage)
	{
		return null;
	}
	
	@Override
	public net.kyori.adventure.text.Component parseMessageToAdventure(String rawMessage, String channel)
	{
		return null;
	}
}