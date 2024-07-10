package main.java.me.avankziar.scc.spigot.assistance;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.handlers.ConvertHandler;
import main.java.me.avankziar.scc.general.objects.ChatUser;
import main.java.me.avankziar.scc.general.objects.ItemJson;
import main.java.me.avankziar.scc.general.objects.StaticValues;
import main.java.me.avankziar.scc.spigot.SCC;
import main.java.me.avankziar.scc.spigot.database.MysqlHandler;
import main.java.me.avankziar.scc.spigot.objects.ChatUserHandler;
import main.java.me.avankziar.scc.spigot.objects.PluginSettings;

public class BackgroundTask 
{
	private SCC plugin;
	private ArrayList<String> players;
	
	public BackgroundTask(SCC plugin) 
	{
		this.plugin = plugin;
		players = new ArrayList<String>();
		runTask();
		runBungeeLocationTask();
		runItemStackTask();
		if(PluginSettings.settings.isBungee())
		{
			runMysqlRowsCounts();
		} else
		{
			unmuteTask();
			if(plugin.getYamlHandler().getConfig().getBoolean("CleanUp.RunAutomaticByRestart", true))
			{
				runTaskCleanUp();
				runTaskCleanUpMails();
			}
		}
	}
	
	private void runTaskCleanUpMails()
	{
		final int days = plugin.getYamlHandler().getConfig().getInt("CleanUp.DeleteReadedMailWhichIsOlderThanDays", 120);
		final long d = (long)days*1000L*60*60*24;
		final long lasttime = System.currentTimeMillis()-d;
		plugin.getMysqlHandler().deleteData(MysqlType.MAIL, "`readeddate` != ? AND `readeddate` < ?", 0, lasttime);
	}
	
	public void unmuteTask()
	{
		new BukkitRunnable()
		{
			
			@Override
			public void run()
			{
				for(Player player : plugin.getServer().getOnlinePlayers())
				{
					ChatUser cu = (ChatUser) plugin.getMysqlHandler().getData(MysqlType.CHATUSER, "`player_uuid` = ?",
							player.getUniqueId().toString());
					if(cu == null)
					{
						continue;
					}
					if(cu.getMuteTime() != 0)
					{
						long mutetime = cu.getMuteTime();
						if(mutetime < System.currentTimeMillis())
						{
							cu.setMuteTime(0L);
							plugin.getMysqlHandler().updateData(MysqlType.CHATUSER, cu, "`player_uuid` = ?",
									player.getUniqueId().toString());
							ChatUser chu = ChatUserHandler.getChatUser(player.getUniqueId());
							if(chu != null)
							{
								chu.setMuteTime(0L);
							}
							player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Mute.Unmute")));
						}
					}
				}
			}
		}.runTaskTimerAsynchronously(plugin, 1L, 20L*15);
	}
	
	private void runTaskCleanUp()
	{
		final int days = plugin.getYamlHandler().getConfig().getInt("CleanUp.DeletePlayerWhichJoinIsOlderThanDays", 120);
		final long d = (long)days*1000L*60*60*24;
		final long lasttime = System.currentTimeMillis()-d;
		final ArrayList<ChatUser> users = ConvertHandler.convertListI(plugin.getMysqlHandler()
				.getFullList(MysqlType.CHATUSER, "`id` ASC", "?", 1));
		new BukkitRunnable()
		{
			int count = 0;
			int deleted = 0;
			@Override
			public void run()
			{
				if(count >= users.size())
				{
					cancel();
					plugin.getLogger().log(Level.INFO, "Deleted "+deleted+" ChatUsers in the CleanUp Task!");
					return;
				}
				ChatUser user = users.get(count);
				if(lasttime >= user.getLastTimeJoined())
				{
					final String uuid = user.getUUID();
					plugin.getMysqlHandler().deleteData(MysqlType.USEDCHANNEL, "`player_uuid` = ?", uuid);
					plugin.getMysqlHandler().deleteData(MysqlType.ITEMJSON, "`owner` = ?", uuid);
					plugin.getMysqlHandler().deleteData(MysqlType.IGNOREOBJECT, "`player_uuid` = ? OR `ignore_uuid` = ?", uuid, uuid);
					plugin.getMysqlHandler().deleteData(MysqlType.CHATUSER, "`player_uuid` = ?", uuid);
					deleted++;
				}
				count++;
			}
		}.runTaskTimerAsynchronously(plugin, 20L*15, 1L);
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
		}.runTaskTimerAsynchronously(plugin, 20L, 20L*15);
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
        player.sendPluginMessage(SCC.getPlugin(), StaticValues.SCC_TOPROXY, stream.toByteArray());
	}
	
	private void runMysqlRowsCounts()
	{
		new BukkitRunnable()
		{
			
			@Override
			public void run()
			{
				for(Player loneOne : plugin.getServer().getOnlinePlayers())
				{
					if(loneOne != null)
					{
						ByteArrayOutputStream stream = new ByteArrayOutputStream();
				        DataOutputStream out = new DataOutputStream(stream);
				        try {
							out.writeUTF(StaticValues.SCC_TASK_MYSQLPERFORMANCE);
							out.writeUTF(PluginSettings.settings.getServer());
							out.writeInt(MysqlHandler.inserts);
							out.writeInt(MysqlHandler.updates);
							out.writeInt(MysqlHandler.deletes);
							out.writeInt(MysqlHandler.reads);
						} catch (IOException e) {
							e.printStackTrace();
						}
				        loneOne.sendPluginMessage(SCC.getPlugin(), StaticValues.SCC_TOPROXY, stream.toByteArray());
				        MysqlHandler.resetsRows();
				        break;
					}
				}
				
			}
		}.runTaskTimerAsynchronously(plugin, 20L*60*5, 20L*60*5);
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
					if(plugin.getMysqlHandler().exist(MysqlType.ITEMJSON, "`owner` = ? AND `itemname` = ?", owner, itemname))
					{
						plugin.getMysqlHandler().updateData(MysqlType.ITEMJSON, ij, "`owner` = ? AND `itemname` = ?", owner, itemname);
					} else
					{
						plugin.getMysqlHandler().create(MysqlType.ITEMJSON, ij);
					}
				}
			}
		}.runTaskTimerAsynchronously(plugin, 10L, 7*20L);
	}
}
