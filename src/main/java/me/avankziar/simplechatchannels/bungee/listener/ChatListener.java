package main.java.me.avankziar.simplechatchannels.bungee.listener;

import java.util.LinkedHashMap;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.handler.ChatHandler;
import main.java.me.avankziar.simplechatchannels.bungee.objects.chat.Channel;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.ServerLocation;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ChatListener implements Listener 
{
	private SimpleChatChannels plugin;
	public static LinkedHashMap<String, LinkedHashMap<String, Long>> spamMap = new LinkedHashMap<>();
	public static LinkedHashMap<String, LinkedHashMap<String, String>> spamMapII = new LinkedHashMap<>();
	public static LinkedHashMap<String, ServerLocation> playerLocation = new LinkedHashMap<>();
	
	public ChatListener(SimpleChatChannels plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onChat(ChatEvent event)
	{
		ProxiedPlayer player = (ProxiedPlayer) event.getSender();
		event.setCancelled(true);
		
		final String message = event.getMessage().trim();
		ChatHandler ch = new ChatHandler(plugin);
		/*
		 * PrePreChecks
		 */
		if(!ch.prePreCheck(player, message))
		{
			return;
		}
		
		/*
		 * Define which channel the players use.
		 */
		Channel usedChannel = null;
		for(String entrySymbol : SimpleChatChannels.channels.keySet())
		{
			if(message.startsWith(entrySymbol))
			{
				usedChannel = SimpleChatChannels.channels.get(entrySymbol);
				break;
			}
		}
		if(usedChannel == null && SimpleChatChannels.nullChannel != null)
		{
			usedChannel = SimpleChatChannels.nullChannel;
		} else
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("ChatListener.NoChannelIsNullChannel")));
			return;
		}
		ch.startChat(player, usedChannel, message);
	}
}
