package main.java.me.avankziar.simplechatchannels.bungee.listener;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.assistance.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.database.MysqlHandler;
import main.java.me.avankziar.simplechatchannels.bungee.database.MysqlHandler.Type;
import main.java.me.avankziar.simplechatchannels.bungee.objects.ChatUserHandler;
import main.java.me.avankziar.simplechatchannels.bungee.objects.chat.TemporaryChannel;
import main.java.me.avankziar.simplechatchannels.handlers.ConvertHandler;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.ChatUser;
import main.java.me.avankziar.simplechatchannels.objects.IgnoreObject;
import main.java.me.avankziar.simplechatchannels.objects.UsedChannel;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class EventJoinLeave implements Listener
{
	private SimpleChatChannels plugin;
	
	public EventJoinLeave(SimpleChatChannels plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void inJoin(PostLoginEvent event)
	{
		ProxiedPlayer player = event.getPlayer();
		Utility utility = plugin.getUtility();
		String pn = player.getName();
		/*
		 * Player check and init
		 */
		ChatUser cu = utility.controlUsedChannels(player);
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
			player.sendMessage(ChatApi.tctl(utility.getActiveChannels(cu, usedChannelslist)));
			///Herzlich willkommen zurück &f%player% &6auf unserem Server &b[Bitte servername einfügen]
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString(
					"EventJoinLeave.Welcome").replace("%player%", pn)));
		}
		
		TextComponent msg = ChatApi.apiChat(
				plugin.getYamlHandler().getLang().getString("JoinListener.Join").replace("%player%", pn), 
				ClickEvent.Action.SUGGEST_COMMAND,
				plugin.getUtility().getPlayerMsgCommand(pn), 
				HoverEvent.Action.SHOW_TEXT, 
				plugin.getUtility().getPlayerHover(pn));
		for(ProxiedPlayer all : plugin.getProxy().getPlayers())
		{
			if(!all.getName().equals(player.getName()))
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
		}
		return;
	}
	
	@EventHandler
	public void onLeave(PlayerDisconnectEvent event)
	{
		ProxiedPlayer player = event.getPlayer();
		Utility utility = plugin.getUtility();
		String pn = player.getName();
		String scc = "CmdScc.";
		if(plugin.editorplayers.contains(pn))
		{
			plugin.editorplayers.remove(pn);
			String µ = "µ";
			String message = "editor"+µ+pn+µ+"remove";
			utility.sendSpigotMessage("simplechatchannels:sccbungee", message);
		}
		Utility.playerUsedChannels.remove(player.getUniqueId().toString());
		
		TemporaryChannel cc = TemporaryChannel.getCustomChannel(pn);
		if(cc!=null)
		{
			cc.removeMembers(event.getPlayer());
			if(cc.getCreator().getName().equals(pn))
			{
				ProxiedPlayer newcreator = null;
    			for(ProxiedPlayer pp : cc.getMembers())
    			{
    				if(pp!=null)
    				{
    					newcreator = pp;
    				}
    			}
    			cc.setCreator(newcreator);
    			if(newcreator!=null)
    			{
    				newcreator.sendMessage(ChatApi.tctl(
        					plugin.getYamlHandler().getLang().getString(scc+"CCLeave.NewCreator")
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
		return;
	}
}
