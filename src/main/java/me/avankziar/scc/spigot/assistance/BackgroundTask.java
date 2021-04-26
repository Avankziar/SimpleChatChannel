package main.java.me.avankziar.scc.spigot.assistance;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.scc.objects.StaticValues;
import main.java.me.avankziar.scc.objects.chat.ItemJson;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;
import main.java.me.avankziar.scc.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.scc.spigot.objects.PluginSettings;

public class BackgroundTask 
{
	private SimpleChatChannels plugin;
	private ArrayList<String> players;
	
	public BackgroundTask(SimpleChatChannels plugin) 
	{
		this.plugin = plugin;
		players = new ArrayList<String>();
		runTask();
		runBungeeLocationTask();
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
	
	private void runBungeeLocationTask()
	{
		if(!PluginSettings.settings.isBungee())
		{
			return;
		}
		new BukkitRunnable()
		{
			
			@Override
			public void run()
			{
				for(Player player : Bukkit.getOnlinePlayers())
				{
					sendLocationToBungee(player);
				}
			}
		}.runTaskTimerAsynchronously(plugin, 20L, 20L*30);
	}
	
	public static void sendLocationToBungee(Player player)
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.SCC_TASK_LOCATIONUPDATE);
			out.writeUTF(player.getUniqueId().toString());
			out.writeUTF(PluginSettings.settings.getServer());
			out.writeUTF(player.getLocation().getWorld().getName());
			out.writeDouble(player.getLocation().getX());
			out.writeDouble(player.getLocation().getY());
			out.writeDouble(player.getLocation().getZ());
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(SimpleChatChannels.getPlugin(), StaticValues.SCC_TOBUNGEE, stream.toByteArray());
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
				for(Player player : Bukkit.getOnlinePlayers())
				{
					if(player.getInventory().getItemInMainHand() == null
							|| player.getInventory().getItemInMainHand().getType() == Material.AIR)
					{
						continue;
					}
					String owner = player.getUniqueId().toString();
					ItemStack i = player.getInventory().getItemInMainHand();
					String jsonString = plugin.getUtility().convertItemStackToJson(player.getInventory().getItemInMainHand());
					String itemname = "default";
					String itemDisplayName = 
							(i.hasItemMeta()) ?
							((i.getItemMeta().hasDisplayName()) ? i.getItemMeta().getDisplayName() : i.getType().toString())
							: i.getType().toString();
					ItemJson ij = new ItemJson(owner, itemname, itemDisplayName, jsonString, plugin.getUtility().toBase64itemStack(i));
					plugin.getMysqlHandler().updateData(Type.ITEMJSON, ij, "`owner` = ? AND `itemname` = ?", owner, itemname);
				}
			}
		}.runTaskTimerAsynchronously(plugin, 10L, 5*20L);
	}
}
