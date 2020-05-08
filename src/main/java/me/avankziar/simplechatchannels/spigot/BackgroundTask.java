package main.java.me.avankziar.simplechatchannels.spigot;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class BackgroundTask 
{
	private SimpleChatChannels plugin;
	private ArrayList<String> players;
	
	public BackgroundTask(SimpleChatChannels plugin) 
	{
		this.plugin = plugin;
		players = new ArrayList<String>();
		runTask();
		runItemStackTask();
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
	
	private void runItemStackTask()
	{
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				Utility.item.clear();
				Utility.itemname.clear();
				for(Player player : Bukkit.getOnlinePlayers())
				{
					if(player.getInventory().getItemInMainHand()!=null)
					{
						if(player.getInventory().getItemInMainHand().getType()!=Material.AIR)
						{
							if(player.hasPermission(Utility.PERMBYPASSITEMUPLOAD))
							{
								ItemStack i = player.getInventory().getItemInMainHand();
								String is = plugin.getUtility().convertItemStackToJson(player.getInventory().getItemInMainHand());
								String itemnames = "";
								if(i.hasItemMeta() && i.getItemMeta().hasDisplayName())
								{
									itemnames = i.getItemMeta().getDisplayName();
								} else
								{
									itemnames = "&o"+i.getType();
								}
								plugin.getUtility().sendBungeeItemMessage(player, itemnames, is);
								String uuid = player.getUniqueId().toString();
								if(Utility.item.containsKey(uuid) 
										&& Utility.itemname.containsKey(uuid))
								{
									Utility.item.replace(uuid, is);
									Utility.itemname.replace(uuid, itemnames);
								} else
								{
									Utility.item.put(uuid,is);
									Utility.itemname.put(uuid,itemnames);
								}
							} else
							{
								plugin.getUtility().sendBungeeItemClearMessage(player, "none", "none");
							}
						}
					}
				}
			}
		}.runTaskTimerAsynchronously(plugin, 10L, 5*20L);
	}
}
