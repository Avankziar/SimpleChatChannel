package main.java.me.avankziar.scc.spigot.listener;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import main.java.me.avankziar.scc.handlers.ConvertHandler;
import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.ChatUser;
import main.java.me.avankziar.scc.objects.KeyHandler;
import main.java.me.avankziar.scc.objects.chat.IgnoreObject;
import main.java.me.avankziar.scc.objects.chat.UsedChannel;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;
import main.java.me.avankziar.scc.spigot.assistance.Utility;
import main.java.me.avankziar.scc.spigot.database.MysqlHandler;
import main.java.me.avankziar.scc.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.scc.spigot.objects.ChatUserHandler;
import main.java.me.avankziar.scc.spigot.objects.PluginSettings;
import main.java.me.avankziar.scc.spigot.objects.TemporaryChannel;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class JoinLeaveListener implements Listener
{
	private SimpleChatChannels plugin;
	
	public JoinLeaveListener(SimpleChatChannels plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent event)
	{
		if(PluginSettings.settings.isBungee())
		{
			return;
		}
		Player player = event.getPlayer();
		String pn = player.getName();
		final boolean firsttimejoin = !plugin.getMysqlHandler().exist(MysqlHandler.Type.CHATUSER,
				"`player_uuid` = ?", player.getUniqueId().toString());
		/*
		 * Player check and init
		 */
		ChatUser cu = plugin.getUtility().controlUsedChannels(player);
		ArrayList<UsedChannel> usedChannelslist = ConvertHandler.convertListV(plugin.getMysqlHandler().getAllListAt(Type.USEDCHANNEL,
				"`id`", false, "`player_uuid` = ?", player.getUniqueId().toString()));
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
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.CHATUSER, cu,
					"`player_uuid` = ?", cu.getUUID());
			
			
			ArrayList<IgnoreObject> iolist = ConvertHandler.convertListII(
					plugin.getMysqlHandler().getAllListAt(MysqlHandler.Type.IGNOREOBJECT, "`id`", true,
							"`ignore_uuid` = ?", player.getUniqueId().toString()));
			for(IgnoreObject io : iolist)
			{
				if(io.getIgnoreUUID().equals(player.getUniqueId().toString()))
				{
					IgnoreObject newio = io;
					newio.setIgnoreName(pn);
					plugin.getMysqlHandler().updateData(MysqlHandler.Type.IGNOREOBJECT, newio,
							"`player_uuid` = ? AND `ignore_uuid` = ?",
							io.getUUID(), io.getIgnoreUUID());
				}
			}
		}
		
		if(cu.isOptionChannelMessage())
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getUtility().getActiveChannels(cu, usedChannelslist)));
			if(firsttimejoin)
			{
				TextComponent tc = ChatApi.tctl(plugin.getYamlHandler().getLang().getString(
						"JoinListener.Welcome").replace("%player%", pn));
				for(Player all : plugin.getServer().getOnlinePlayers())
				{
					all.spigot().sendMessage(tc);
				}
			}
		}
		
		TextComponent msg = ChatApi.apiChat(
				plugin.getYamlHandler().getLang().getString("JoinListener.Join").replace("%player%", pn), 
				ClickEvent.Action.SUGGEST_COMMAND,
				plugin.getUtility().getPlayerMsgCommand(pn), 
				HoverEvent.Action.SHOW_TEXT, 
				plugin.getUtility().getPlayerHover(pn));
		for(Player all : plugin.getServer().getOnlinePlayers())
		{
			ChatUser allcu = ChatUserHandler.getChatUser(all.getUniqueId());
			if(allcu != null)
			{
				if(allcu.isOptionJoinMessage())
				{
					all.spigot().sendMessage(msg);
				}
			}
		}
		int unreadedMails = plugin.getMysqlHandler().getCount(Type.MAIL, "`id`",
				"`reciver_uuid` = ? AND `readeddate` = ?", player.getUniqueId().toString(), 0);
		if(unreadedMails > 0)
		{
			player.spigot().sendMessage(ChatApi.apiChat(
					plugin.getYamlHandler().getLang().getString("JoinListener.HasNewMail")
					.replace("%count%", String.valueOf(unreadedMails)),
					ClickEvent.Action.RUN_COMMAND,
					PluginSettings.settings.getCommands(KeyHandler.MAIL),
					HoverEvent.Action.SHOW_TEXT,
					plugin.getYamlHandler().getLang().getString("CmdMail.Send.Hover")));
		}
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event)
	{
		if(PluginSettings.settings.isBungee())
		{
			return;
		}
		Player player = event.getPlayer();
		String pn = player.getName();
		Utility.playerUsedChannels.remove(player.getUniqueId().toString());
		
		TemporaryChannel cc = TemporaryChannel.getCustomChannel(pn);
		if(cc != null)
		{
			cc.removeMembers(event.getPlayer());
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
}
