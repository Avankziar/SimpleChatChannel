package main.java.me.avankziar.simplechatchannels.bungee.listener;

import java.util.ArrayList;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.assistance.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.database.MysqlHandler;
import main.java.me.avankziar.simplechatchannels.bungee.objects.ChatUserHandler;
import main.java.me.avankziar.simplechatchannels.bungee.objects.TemporaryChannel;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.ChatUser;
import main.java.me.avankziar.simplechatchannels.objects.ConvertHandler;
import main.java.me.avankziar.simplechatchannels.objects.IgnoreObject;
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
		ChatUser cu = utility.controlChannelSaves(player);
		//Names Aktualisierung
		if(!cu.getName().equals(pn))
		{
			cu.setName(pn);
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.CHATUSER, cu,
					"`player_uuid` = ?", cu.getUUID());
			ChatUser.addChatUser(cu);
			int end = plugin.getMysqlHandler().lastID(MysqlHandler.Type.IGNOREOBJECT);
			ArrayList<IgnoreObject> iolist = ConvertHandler.convertListII(
					plugin.getMysqlHandler().getTop(MysqlHandler.Type.IGNOREOBJECT, "`id`", true, 0, end));
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
		if(cu.isOptionJoinMessage())
		{
			player.sendMessage(ChatApi.tctl(utility.getActiveChannels(cu)));
			///Herzlich willkommen zurück &f%player% &6auf unserem Server &b[Bitte servername einfügen]
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(
					"EventJoinLeave.Welcome").replace("%player%", pn)));
		}
		Boolean globaljoin = plugin.getYamlHandler().get().getBoolean("ShowJoinMessageGlobal", true);
		if(globaljoin==false)
		{
			return;
		}
		for(ProxiedPlayer all : plugin.getProxy().getPlayers())
		{
			if(!all.getName().equals(player.getName()))
			{
				ChatUser allcu = ChatUserHandler.getChatUser(all.getUniqueId());
				if(allcu != null)
				{
					if(allcu.isOptionJoinMessage())
					{
						///%player% &6hat den Server betreten!
						TextComponent msg = ChatApi.apiChat(
								plugin.getYamlHandler().getL().getString("EventJoinLeave.PlayerEnter")
								.replace("%player%", pn), 
								ClickEvent.Action.SUGGEST_COMMAND, "@"+player.getName()+" ", 
								HoverEvent.Action.SHOW_TEXT, 
								plugin.getYamlHandler().getL().getString("ChannelExtra.Hover.Message")
								.replace("%player%", pn));
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
    			///Du wurdest der neue Ersteller der CustomChannels %channel%
    			if(newcreator!=null)
    			{
    				newcreator.sendMessage(ChatApi.tctl(
        					plugin.getYamlHandler().getL().getString(scc+"CCLeave.NewCreator")
        					.replace("%channel%", cc.getName())));
    			}
			}
		}
		Boolean globalleave = plugin.getYamlHandler().get().getBoolean("ShowLeaveMessageGlobal", false);
		if(globalleave==false)
		{
			return;
		}
		ChatUser cu = ChatUserHandler.getChatUser(player.getUniqueId().toString());
		if(cu != null)
		{
			ChatUser.removeChatUser(cu);
		}
		for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers())
		{
			ChatUser allcu = ChatUserHandler.getChatUser(all.getUniqueId());
			if(allcu != null)
			{
				if(allcu.isOptionJoinMessage())
				{
					///%player% &4hat den Server verlassen!
					String msg = plugin.getYamlHandler().getL().getString(
							"EventJoinLeave.PlayerQuit").replace("%player%", pn);
					all.sendMessage(ChatApi.tctl(msg));
				}
			}
		}
		return;
	}
}
