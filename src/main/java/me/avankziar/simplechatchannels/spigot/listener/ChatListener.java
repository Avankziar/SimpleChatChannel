package main.java.me.avankziar.simplechatchannels.spigot.listener;

import java.util.LinkedHashMap;
import java.util.concurrent.ExecutionException;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.ServerLocation;
import main.java.me.avankziar.scc.objects.chat.Channel;
import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.handler.ChatHandler;

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
	public void onChat(AsyncPlayerChatEvent event) throws InterruptedException, ExecutionException
	{
		Player player = (Player) event.getPlayer();
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
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("ChatListener.NoChannelIsNullChannel")));
			return;
		}
		ch.startChat(player, usedChannel, message);
	}
}
