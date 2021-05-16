package main.java.me.avankziar.scc.bungee.listener;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.scc.bungee.assistance.Utility;
import main.java.me.avankziar.scc.bungee.database.MysqlHandler;
import main.java.me.avankziar.scc.bungee.database.MysqlHandler.Type;
import main.java.me.avankziar.scc.bungee.objects.ChatUserHandler;
import main.java.me.avankziar.scc.bungee.objects.PluginSettings;
import main.java.me.avankziar.scc.bungee.objects.chat.TemporaryChannel;
import main.java.me.avankziar.scc.handlers.ConvertHandler;
import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.ChatUser;
import main.java.me.avankziar.scc.objects.KeyHandler;
import main.java.me.avankziar.scc.objects.chat.IgnoreObject;
import main.java.me.avankziar.scc.objects.chat.UsedChannel;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.scheduler.ScheduledTask;
import net.md_5.bungee.event.EventHandler;

public class JoinLeaveListener implements Listener
{
	private SimpleChatChannels plugin;
	private LinkedHashMap<String, ScheduledTask> map = new LinkedHashMap<>();
	
	public JoinLeaveListener(SimpleChatChannels plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void inJoin(PostLoginEvent event)
	{
		final ProxiedPlayer player = event.getPlayer();
		String pn = player.getName();
		final boolean firsttimejoin = !plugin.getMysqlHandler().exist(MysqlHandler.Type.CHATUSER,
				"`player_uuid` = ?", player.getUniqueId().toString());
		/*
		 * Player check and init
		 */
		ScheduledTask task = plugin.getProxy().getScheduler().schedule(plugin, new Runnable() 
		{
			@Override
			public void run() 
			{
				if(!player.isConnected())
				{
					return;
				}
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
					player.sendMessage(ChatApi.tctl(plugin.getUtility().getActiveChannels(cu, usedChannelslist)));
					if(firsttimejoin)
					{
						TextComponent tc = ChatApi.tctl(plugin.getYamlHandler().getLang().getString(
								"JoinListener.Welcome").replace("%player%", pn));
						for(ProxiedPlayer all : plugin.getProxy().getPlayers())
						{
							all.sendMessage(tc);
						}
					}
				}
				
				TextComponent msg = ChatApi.apiChat(
						plugin.getYamlHandler().getLang().getString("JoinListener.Join").replace("%player%", pn), 
						ClickEvent.Action.SUGGEST_COMMAND,
						plugin.getUtility().getPlayerMsgCommand(pn), 
						HoverEvent.Action.SHOW_TEXT, 
						plugin.getUtility().getPlayerHover(pn));
				for(ProxiedPlayer all : plugin.getProxy().getPlayers())
				{
					ChatUser allcu = ChatUserHandler.getChatUser(all.getUniqueId());
					if(allcu != null)
					{
						if(allcu.isOptionJoinMessage())
						{
							all.sendMessage(msg);
						}
					}
				}
				int unreadedMails = plugin.getMysqlHandler().getCount(Type.MAIL, "`id`",
						"`reciver_uuid` = ? AND `readeddate` = ?", player.getUniqueId().toString(), 0);
				if(unreadedMails > 0)
				{
					player.sendMessage(ChatApi.apiChat(plugin.getYamlHandler().getLang().getString("JoinListener.HasNewMail")
							.replace("%count%", String.valueOf(unreadedMails)),
							ClickEvent.Action.RUN_COMMAND,
							PluginSettings.settings.getCommands(KeyHandler.MAIL).trim(),
							HoverEvent.Action.SHOW_TEXT,
							plugin.getYamlHandler().getLang().getString("CmdMail.Send.Hover")));
				}
				map.get(pn).cancel();
				map.remove(pn);
			}
		}, 3L, 1L, TimeUnit.SECONDS);
		map.put(pn, task);
	}
	
	@EventHandler
	public void onLeave(PlayerDisconnectEvent event)
	{
		//ADDME:Wenn ein Spieler wegen der Whitelist disconnetet, so soll der hier nicht auftauchen.
		ProxiedPlayer player = event.getPlayer();
		String pn = player.getName();
		Utility.playerUsedChannels.remove(player.getUniqueId().toString());
		
		TemporaryChannel cc = TemporaryChannel.getCustomChannel(pn);
		if(cc != null)
		{
			cc.removeMembers(event.getPlayer());
			if(cc.getCreator().getName().equals(pn))
			{
				ProxiedPlayer newcreator = null;
    			for(ProxiedPlayer pp : cc.getMembers())
    			{
    				if(pp != null)
    				{
    					newcreator = pp;
    				}
    			}
    			cc.setCreator(newcreator);
    			if(newcreator!=null)
    			{
    				newcreator.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Leave.NewCreator")
        					.replace("%channel%", cc.getName())));
    			}
			}
		}
		String msg = plugin.getYamlHandler().getLang().getString("LeaveListener.Leave").replace("%player%", pn);
		for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers())
		{
			ChatUser allcu = ChatUserHandler.getChatUser(all.getUniqueId());
			if(allcu != null)
			{
				if(allcu.isOptionJoinMessage())
				{
					all.sendMessage(ChatApi.tctl(msg));
				}
			}
		}
	}
}
