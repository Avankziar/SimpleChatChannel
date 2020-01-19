package main.java.de.avankziar.simplechatchannels.spigot.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;

import main.java.de.avankziar.simplechatchannels.spigot.SimpleChatChannels;

@SuppressWarnings("deprecation")
public class EVENTTabCompleter implements Listener
{
	private SimpleChatChannels plugin;
	
	public EVENTTabCompleter(SimpleChatChannels plugin) 
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onTabComplet(PlayerChatTabCompleteEvent event)
	{
		if(event.getLastToken().equalsIgnoreCase("@"))
		{
			event.getTabCompletions().clear();
			event.getTabCompletions().addAll(plugin.getBackgroundTask().getPlayers());
		}
	}
}
