package main.java.me.avankziar.scc.velocity.listener;

import java.util.LinkedHashMap;
import java.util.logging.Level;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.objects.Channel;
import main.java.me.avankziar.scc.general.objects.ServerLocation;
import main.java.me.avankziar.scc.velocity.SCC;
import main.java.me.avankziar.scc.velocity.handler.ChatHandler;

public class ChatListener
{
	private SCC plugin;
	public static LinkedHashMap<String, LinkedHashMap<String, Long>> spamMap = new LinkedHashMap<>();
	public static LinkedHashMap<String, LinkedHashMap<String, String>> spamMapII = new LinkedHashMap<>();
	public static LinkedHashMap<String, ServerLocation> playerLocation = new LinkedHashMap<>();
	
	public ChatListener(SCC plugin)
	{
		this.plugin = plugin;
	}
	
	@Subscribe
	public void onChat(PlayerChatEvent event)
	{
		if(event.getMessage().startsWith("/"))
		{
			Player player = (Player) event.getPlayer();
			plugin.getLogger().log(Level.INFO, player.getUsername() +": "+event.getMessage());
			return;
		}
		Player player = (Player) event.getPlayer();
		if(plugin.editorplayers.contains(player.getUsername()))
		{
			return;
		}
		String message = event.getMessage().trim();
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
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("ChatListener.NoChannelIsNullChannel")));
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
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("ChatListener.StringTrim")));
				return;
			}
			message = message.substring(usedChannel.getSymbol().length());
		}
		ch.startChat(player, usedChannel, message);
	}
}