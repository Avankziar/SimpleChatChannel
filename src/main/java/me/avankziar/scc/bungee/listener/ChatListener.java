package main.java.me.avankziar.scc.bungee.listener;

import java.util.LinkedHashMap;
import java.util.logging.Level;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.scc.bungee.handler.ChatHandler;
import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.ServerLocation;
import main.java.me.avankziar.scc.objects.chat.Channel;
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
		if(event.isCancelled())
		{
			return;
		}
		if(event.getMessage().startsWith("/"))
		{
			ProxiedPlayer player = (ProxiedPlayer) event.getSender();
			plugin.getLogger().log(Level.INFO, player.getName() +": "+event.getMessage());
			return;
		}
		ProxiedPlayer player = (ProxiedPlayer) event.getSender();
		if(plugin.editorplayers.contains(player.getName()))
		{
			return;
		}
		String message = event.getMessage().trim();
		event.setCancelled(true);
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
		}
		if(usedChannel == null)
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("ChatListener.NoChannelIsNullChannel")));
			return;
		}
		/*
		 * Trim the orginal message, and if the message is empty, so return;
		 */
		if(!usedChannel.getUniqueIdentifierName().equals(SimpleChatChannels.nullChannel.getUniqueIdentifierName())
				&& !usedChannel.getUniqueIdentifierName().equals("Private"))
		{
			if(message.length() <= usedChannel.getSymbol().length())
			{
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("ChatListener.StringTrim")));
				return;
			}
			message = message.substring(usedChannel.getSymbol().length());
		}
		ch.startChat(player, usedChannel, message);
	}
}
