package main.java.de.avankziar.simplechatchannels.spigot;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class BackgroundTask 
{
	private SimpleChatChannels plugin;
	private ArrayList<String> players;
	
	public BackgroundTask(SimpleChatChannels plugin) 
	{
		this.plugin = plugin;
		runTask();
	}
	
	private void runTask()
	{
		new BukkitRunnable() 
		{
			@Override
			public void run() 
			{
				for(Player player : Bukkit.getOnlinePlayers())
				{
					if(!players.contains(player.getName()))
					{
						players.add(player.getName());
					}
				}
			}
		}.runTaskTimerAsynchronously(plugin, 0L, 15*20L);
	}
	
	public ArrayList<String> getPlayers()
	{
		return players;
	}
}
