package main.java.me.avankziar.simplechatchannels.spigot.listener;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import main.java.me.avankziar.simplechatchannels.handlers.ConvertHandler;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.ChatUser;
import main.java.me.avankziar.simplechatchannels.objects.IgnoreObject;
import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.database.MysqlHandler;
import main.java.me.avankziar.simplechatchannels.spigot.objects.ChatUserHandler;
import main.java.me.avankziar.simplechatchannels.spigot.objects.TemporaryChannel;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class EVENTJoinLeave implements Listener
{
	private SimpleChatChannels plugin;
	
	public EVENTJoinLeave(SimpleChatChannels plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onJoin(PlayerJoinEvent event)
	{
		Player player = event.getPlayer();
		ChatUser cu = plugin.getUtility().controlChannelSaves(player);
		event.setJoinMessage("");
		if(plugin.getYamlHandler().get().getBoolean("Bungee", false))
		{
			return;
		}
		String pn = player.getName();
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
		if(cu.isOptionChannelMessage())
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getUtility().getActiveChannels(cu)));
			///Herzlich willkommen zurück &f%player% &6auf unserem Server &b[Bitte servername einfügen]
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(
					"EventJoinLeave.Welcome").replace("%player%", pn)));
		}
		Boolean globaljoin = plugin.getYamlHandler().get().getBoolean("ShowJoinMessageGlobal", true);
		if(globaljoin==false)
		{
			return;
		}
		for(Player all : Bukkit.getOnlinePlayers())
		{
			if(!all.getName().equals(player.getName()))
			{
				ChatUser allcu = ChatUserHandler.getChatUser(all.getUniqueId());
				if(allcu != null)
				{
					if(allcu.isOptionChannelMessage())
					{
						///%player% &6hat den Server betreten!
						TextComponent msg = ChatApi.apiChat(
								plugin.getYamlHandler().getL().getString("EventJoinLeave.PlayerEnter")
								.replace("%player%", pn), 
								ClickEvent.Action.SUGGEST_COMMAND, "@"+player.getName()+" ", 
								HoverEvent.Action.SHOW_TEXT, 
								plugin.getYamlHandler().getL().getString("ChannelExtra.Hover.Message")
								.replace("%player%", pn));
						all.spigot().sendMessage(msg);
					}
				}
			}
		}
		return;
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event)
	{
		if(plugin.getYamlHandler().get().getBoolean("Bungee", false))
		{
			if(SimpleChatChannels.editorplayers.contains(event.getPlayer().getName()))
			{
				SimpleChatChannels.editorplayers.remove(event.getPlayer().getName());
			}
		}
		if(plugin.getYamlHandler().get().getBoolean("Bungee", false))
		{
			event.setQuitMessage("");
			return;
		}
		String scc = "CmdScc.";
		
		TemporaryChannel cc = TemporaryChannel.getCustomChannel(event.getPlayer());
		if(cc!=null)
		{
			cc.removeMembers(event.getPlayer());
			if(cc.getCreator().getName().equals(event.getPlayer().getName()))
			{
				Player newcreator = null;
    			for(Player pp : cc.getMembers())
    			{
    				if(pp!=null)
    				{
    					newcreator = pp;
    				}
    			}
    			cc.setCreator(newcreator);
    			///Du wurdest der neue Ersteller der CustomChannels %channel%
    			newcreator.spigot().sendMessage(ChatApi.tctl(
    					plugin.getYamlHandler().getL().getString(scc+"CCLeave.NewCreator")
    					.replace("%channel%", cc.getName())));
			}
		}
		event.setQuitMessage("");
		Player player = event.getPlayer();
		String pn = player.getName();
		Boolean globalleave = plugin.getYamlHandler().get().getBoolean("ShowLeaveMessageGlobal", false);
		if(globalleave==false)
		{
			return;
		}
		for(Player all : Bukkit.getOnlinePlayers())
		{
			ChatUser allcu = ChatUserHandler.getChatUser(all.getUniqueId());
			if(allcu != null)
			{
				if(allcu.isOptionChannelMessage())
				{
					///%player% &4hat den Server verlassen!
					String msg = plugin.getYamlHandler().getL().getString(
							"EventJoinLeave.PlayerQuit").replace("%player%", pn);
					all.spigot().sendMessage(ChatApi.tctl(msg));
				}
			}
		}
		return;
	}
}
