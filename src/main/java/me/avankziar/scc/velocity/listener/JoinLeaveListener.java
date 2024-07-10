package main.java.me.avankziar.scc.velocity.listener;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.event.player.ServerConnectedEvent;
import com.velocitypowered.api.proxy.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.handlers.ConvertHandler;
import main.java.me.avankziar.scc.general.objects.ChatUser;
import main.java.me.avankziar.scc.general.objects.IgnoreObject;
import main.java.me.avankziar.scc.general.objects.UsedChannel;
import main.java.me.avankziar.scc.velocity.SCC;
import main.java.me.avankziar.scc.velocity.assistance.Utility;
import main.java.me.avankziar.scc.velocity.handler.ChatHandler;
import main.java.me.avankziar.scc.velocity.objects.ChatUserHandler;
import main.java.me.avankziar.scc.velocity.objects.chat.TemporaryChannel;

public class JoinLeaveListener
{
	private SCC plugin;
	
	public JoinLeaveListener(SCC plugin)
	{
		this.plugin = plugin;
	}
	
	@Subscribe
	public void onPlayerJoin(PostLoginEvent event)
	{
		final Player player = event.getPlayer();
		String pn = player.getUsername();
		final boolean firsttimejoin = !plugin.getMysqlHandler().exist(MysqlType.CHATUSER,
				"`player_uuid` = ?", player.getUniqueId().toString());
		
		ChatUser cu = plugin.getUtility().controlUsedChannels(player);
		ArrayList<UsedChannel> usedChannelslist = ConvertHandler.convertListV(plugin.getMysqlHandler().getFullList(MysqlType.USEDCHANNEL,
				"`id` ASC", "`player_uuid` = ?", player.getUniqueId().toString()));
		LinkedHashMap<String, UsedChannel> usedChannels = new LinkedHashMap<>();
		for(UsedChannel uc : usedChannelslist)
		{
			usedChannels.put(uc.getUniqueIdentifierName(), uc);
		}
		
		Utility.playerUsedChannels.put(player.getUniqueId().toString(),	usedChannels);
		SCC.onlinePlayers.add(player.getUsername());
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
		
		if(cu.isOptionChannelMessage())
		{
			String activeChannel = plugin.getUtility().getActiveChannels(cu, usedChannelslist);
			player.sendMessage(ChatApi.tl(activeChannel));
			ChatHandler.getChatHistory(player.getUniqueId()).add(activeChannel);
			if(firsttimejoin)
			{
				String tc = plugin.getYamlHandler().getLang().getString(
						"JoinListener.Welcome").replace("%player%", pn);
				for(Player all : plugin.getServer().getAllPlayers())
				{
					all.sendMessage(ChatApi.tl(tc));
					ChatHandler.getChatHistory(all.getUniqueId()).add(tc);
				}
			}
		}
		cu.setLastTimeJoined(System.currentTimeMillis());
		plugin.getMysqlHandler().updateData(MysqlType.CHATUSER, cu,
				"`player_uuid` = ?", cu.getUUID());
		String msg = ChatApi.clickHover(
				plugin.getYamlHandler().getLang().getString("JoinListener.Join").replace("%player%", pn), 
				"SUGGEST_COMMAND",
				plugin.getUtility().getPlayerMsgCommand(pn), 
				"SHOW_TEXT", 
				plugin.getUtility().getPlayerHover(pn));
		for(Player all : plugin.getServer().getAllPlayers())
		{
			ChatUser allcu = ChatUserHandler.getChatUser(all.getUniqueId());
			if(allcu != null)
			{
				if(allcu.isOptionJoinMessage())
				{
					all.sendMessage(ChatApi.tl(msg));
					ChatHandler.getChatHistory(all.getUniqueId()).add(msg);
				}
			}
		}
	}
	
	@Subscribe
	public void onPlayerQuit(DisconnectEvent event)
	{
		//ADDME:Wenn ein Spieler wegen der Whitelist disconnetet, so soll der hier nicht auftauchen.
		Player player = event.getPlayer();
		ChatHandler.chatHistory.remove(player.getUniqueId());
		String pn = player.getUsername();
		SCC.onlinePlayers.remove(player.getUsername());
		plugin.editorplayers.remove(player.getUsername());
		Utility.playerUsedChannels.remove(player.getUniqueId().toString());
		
		TemporaryChannel cc = TemporaryChannel.getCustomChannel(pn);
		if(cc != null)
		{
			cc.removeMembers(event.getPlayer());
			if(cc.getCreator().getUsername().equals(pn))
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
    			if(newcreator!=null)
    			{
    				newcreator.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Leave.NewCreator")
        					.replace("%channel%", cc.getName())));
    			}
			}
		}
		String msg = plugin.getYamlHandler().getLang().getString("LeaveListener.Leave").replace("%player%", pn);
		for(Player all : plugin.getServer().getAllPlayers())
		{
			if(!all.getUsername().equals(pn))
			{
				ChatUser allcu = ChatUserHandler.getChatUser(all.getUniqueId());
				if(allcu != null)
				{
					if(allcu.isOptionJoinMessage())
					{
						ChatHandler.getChatHistory(all.getUniqueId()).add(msg);
						all.sendMessage(ChatApi.tl(msg));
					}
				}
			}
		}
	}
	
	@Subscribe
	public void onServerSwitch(ServerConnectedEvent event)
	{
		if(event.getPreviousServer().isEmpty())
		{
			return;
		}
		/* NOT NEEDED at the moment
		for(String s : ChatHandler.chatHistory.get(event.getPlayer().getUniqueId()))
		{
			event.getPlayer().sendMessage(ChatApi.tl(s));
		}*/
	}
}