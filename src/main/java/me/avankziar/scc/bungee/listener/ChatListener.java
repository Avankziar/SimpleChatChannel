package main.java.me.avankziar.scc.bungee.listener;

import java.util.LinkedHashMap;
import java.util.logging.Level;

import main.java.me.avankziar.scc.bungee.SCC;
import main.java.me.avankziar.scc.bungee.handler.ChatHandler;
import main.java.me.avankziar.scc.general.assistance.ChatApiOld;
import main.java.me.avankziar.scc.general.objects.Channel;
import main.java.me.avankziar.scc.general.objects.ServerLocation;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class ChatListener implements Listener 
{
	private SCC plugin;
	public static LinkedHashMap<String, LinkedHashMap<String, Long>> spamMap = new LinkedHashMap<>();
	public static LinkedHashMap<String, LinkedHashMap<String, String>> spamMapII = new LinkedHashMap<>();
	public static LinkedHashMap<String, ServerLocation> playerLocation = new LinkedHashMap<>();
	
	public ChatListener(SCC plugin)
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
		for(String entrySymbol : SCC.channels.keySet())
		{
			if(message.startsWith(entrySymbol))
			{
				usedChannel = SCC.channels.get(entrySymbol);
				break;
			}
		}
		if(usedChannel == null && SCC.nullChannel != null)
		{
			usedChannel = SCC.nullChannel;
		}
		if(usedChannel == null)
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("ChatListener.NoChannelIsNullChannel")));
			return;
		}
		/*
		 * Trim the orginal message, and if the message is empty, so return;
		 */
		if(!usedChannel.getUniqueIdentifierName().equals(SCC.nullChannel.getUniqueIdentifierName())
				&& !usedChannel.getUniqueIdentifierName().equals("Private"))
		{
			if(message.length() <= usedChannel.getSymbol().length())
			{
				player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("ChatListener.StringTrim")));
				return;
			}
			message = message.substring(usedChannel.getSymbol().length());
		}
		ch.startChat(player, usedChannel, message);
	}
}
