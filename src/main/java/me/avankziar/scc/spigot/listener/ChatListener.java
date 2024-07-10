package main.java.me.avankziar.scc.spigot.listener;

import java.util.LinkedHashMap;
import java.util.concurrent.ExecutionException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.objects.Channel;
import main.java.me.avankziar.scc.general.objects.ServerLocation;
import main.java.me.avankziar.scc.spigot.SCC;
import main.java.me.avankziar.scc.spigot.handler.ChatHandlerAdventure;
import main.java.me.avankziar.scc.spigot.objects.PluginSettings;

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
	public void onChat(AsyncPlayerChatEvent event) throws InterruptedException, ExecutionException
	{
		/*if(PluginSettings.settings.isBungee())
		{
			return;
		}*/
		Player player = (Player) event.getPlayer();
		if(plugin.editorplayers.contains(player.getName()))
		{
			return;
		}
		event.setCancelled(true);
		if(PluginSettings.settings.isBungee())
		{
			return;
		}
		
		String message = event.getMessage().trim();
		ChatHandlerAdventure ch = new ChatHandlerAdventure(plugin);
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
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("ChatListener.NoChannelIsNullChannel")));
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
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("ChatListener.StringTrim")));
				return;
			}
			message = message.substring(usedChannel.getSymbol().length());
		}
		ch.startChat(player, usedChannel, message);
	}
}
