package main.java.me.avankziar.scc.spigot.listener;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.handlers.ConvertHandler;
import main.java.me.avankziar.scc.general.objects.ChatUser;
import main.java.me.avankziar.scc.general.objects.IgnoreObject;
import main.java.me.avankziar.scc.general.objects.KeyHandler;
import main.java.me.avankziar.scc.general.objects.UsedChannel;
import main.java.me.avankziar.scc.spigot.SCC;
import main.java.me.avankziar.scc.spigot.assistance.Utility;
import main.java.me.avankziar.scc.spigot.objects.ChatUserHandler;
import main.java.me.avankziar.scc.spigot.objects.PluginSettings;
import main.java.me.avankziar.scc.spigot.objects.TemporaryChannel;

public class JoinLeaveListener implements Listener
{
	private SCC plugin;
	
	public JoinLeaveListener(SCC plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();
		final String pn = player.getName();
		final boolean firsttimejoin = !plugin.getMysqlHandler().exist(MysqlType.CHATUSER,
				"`player_uuid` = ?", player.getUniqueId().toString());
		event.setJoinMessage("");
		/*
		 * Player check and init
		 */
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				ChatUser cu = plugin.getUtility().controlUsedChannels(player);
				ArrayList<UsedChannel> usedChannelslist = ConvertHandler.convertListV(plugin.getMysqlHandler().getFullList(MysqlType.USEDCHANNEL,
						"`id` ASC", "`player_uuid` = ?", player.getUniqueId().toString()));
				LinkedHashMap<String, UsedChannel> usedChannels = new LinkedHashMap<>();
				for(UsedChannel uc : usedChannelslist)
				{
					usedChannels.put(uc.getUniqueIdentifierName(), uc);
				}
				
				Utility.playerUsedChannels.put(player.getUniqueId().toString(),	usedChannels);
				
				//Names Aktualisierung
				if(!cu.getName().equals(pn))
				{
					cu.setName(pn);
					plugin.getMysqlHandler().updateData(MysqlType.CHATUSER, cu,
							"`player_uuid` = ?", cu.getUUID());
					
					
					ArrayList<IgnoreObject> iolist = ConvertHandler.convertListII(
							plugin.getMysqlHandler().getFullList(MysqlType.IGNOREOBJECT, "`id` DESC",
									"`ignore_uuid` = ?", player.getUniqueId().toString()));
					for(IgnoreObject io : iolist)
					{
						if(io.getIgnoreUUID().equals(player.getUniqueId().toString()))
						{
							IgnoreObject newio = io;
							newio.setIgnoreName(pn);
							plugin.getMysqlHandler().updateData(MysqlType.IGNOREOBJECT, newio,
									"`player_uuid` = ? AND `ignore_uuid` = ?",
									io.getUUID(), io.getIgnoreUUID());
						}
					}
				}
				if(PluginSettings.settings.isBungee())
				{
					return;
				}
				cu.setLastTimeJoined(System.currentTimeMillis());
				plugin.getMysqlHandler().updateData(MysqlType.CHATUSER, cu,
						"`player_uuid` = ?", cu.getUUID());
				if(cu.isOptionChannelMessage())
				{
					player.spigot().sendMessage(ChatApi.tctl(plugin.getUtility().getActiveChannels(cu, usedChannelslist)));
					if(firsttimejoin)
					{
						String tc = plugin.getYamlHandler().getLang().getString(
								"JoinListener.Welcome").replace("%player%", pn);
						for(Player all : plugin.getServer().getOnlinePlayers())
						{
							all.spigot().sendMessage(ChatApi.tctl(tc));
						}
					}
				}
				
				String msg = ChatApi.clickHover(
						plugin.getYamlHandler().getLang().getString("JoinListener.Join").replace("%player%", pn), 
						"SUGGEST_COMMAND",
						plugin.getUtility().getPlayerMsgCommand(pn), 
						"SHOW_TEXT", 
						plugin.getUtility().getPlayerHover(pn));
				for(Player all : plugin.getServer().getOnlinePlayers())
				{
					ChatUser allcu = ChatUserHandler.getChatUser(all.getUniqueId());
					if(allcu != null)
					{
						if(allcu.isOptionJoinMessage())
						{
							all.spigot().sendMessage(ChatApi.tctl(msg));
						}
					}
				}
				int unreadedMails = plugin.getMysqlHandler().getCount(MysqlType.MAIL,
						"`reciver_uuid` = ? AND `readeddate` = ?", player.getUniqueId().toString(), 0);
				if(unreadedMails > 0)
				{
					player.spigot().sendMessage(ChatApi.tctl(ChatApi.clickHover(
							plugin.getYamlHandler().getLang().getString("JoinListener.HasNewMail")
							.replace("%count%", String.valueOf(unreadedMails)),
							"RUN_COMMAND",
							PluginSettings.settings.getCommands(KeyHandler.MAIL),
							"SHOW_TEXT",
							plugin.getYamlHandler().getLang().getString("CmdMail.Send.Hover"))));
				}
			}
		}.runTaskAsynchronously(plugin);
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event)
	{
		event.setQuitMessage("");
		if(PluginSettings.settings.isBungee())
		{
			return;
		}
		final Player player = event.getPlayer();
		final String pn = player.getName();
		
		new BukkitRunnable()
		{
			@Override
			public void run()
			{
				plugin.editorplayers.remove(player.getName());
				Utility.playerUsedChannels.remove(player.getUniqueId().toString());
				
				TemporaryChannel cc = TemporaryChannel.getCustomChannel(pn);
				if(cc != null)
				{
					cc.removeMembers(player);
					if(cc.getCreator().getName().equals(pn))
					{
						Player newcreator = null;
		    			for(Player pp : cc.getMembers())
		    			{
		    				if(pp != null)
		    				{
		    					newcreator = pp;
		    				}
		    			}
		    			cc.setCreator(newcreator);
		    			if(newcreator !=  null)
		    			{
		    				newcreator.spigot().sendMessage(ChatApi.tctl(
		    						plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Leave.NewCreator")
		        					.replace("%channel%", cc.getName())));
		    			}
					}
				}
				String msg = plugin.getYamlHandler().getLang().getString("LeaveListener.Leave").replace("%player%", pn);
				for(Player all : plugin.getServer().getOnlinePlayers())
				{
					ChatUser allcu = ChatUserHandler.getChatUser(all.getUniqueId());
					if(allcu != null)
					{
						if(allcu.isOptionJoinMessage())
						{
							all.spigot().sendMessage(ChatApi.tctl(msg));
						}
					}
				}
			}
		}.runTaskAsynchronously(plugin);
		
	}
}
