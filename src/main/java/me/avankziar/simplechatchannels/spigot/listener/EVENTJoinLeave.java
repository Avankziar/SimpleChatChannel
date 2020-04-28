package main.java.me.avankziar.simplechatchannels.spigot.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.Utility;
import main.java.me.avankziar.simplechatchannels.spigot.interfaces.TemporaryChannel;
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
		if(plugin.getYamlHandler().get().getBoolean("Bungee", false))
		{
			event.setJoinMessage("");
			return;
		}
		event.setJoinMessage("");
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage();
		Player player = event.getPlayer();
		String pn = player.getName();
		utility.controlChannelSaves(player);
		String oldplayername = (String) plugin.getMysqlHandler().getDataI(
				player.getUniqueId().toString(), "player_name", "player_uuid");
		//Names Aktualisierung
		if(!oldplayername.equals(pn))
		{
			plugin.getMysqlHandler().updateDataI(player, pn, "player_name");
		}
		if((boolean) plugin.getMysqlHandler().getDataI(player.getUniqueId().toString(), "joinmessage", "player_uuid"))
		{
			player.spigot().sendMessage(utility.tctl(utility.getActiveChannels(player)));
			///Herzlich willkommen zurück &f%player% &6auf unserem Server &b[Bitte servername einfügen]
			player.spigot().sendMessage(utility.tctl(plugin.getYamlHandler().getL().getString(
					language+".EventJoinLeave.Welcome").replace("%player%", pn)));
		}
		plugin.getMysqlHandler().updateDataII(player, pn, "ignore_name", "ignore_uuid");
		Boolean globaljoin = plugin.getYamlHandler().get().getBoolean("ShowJoinMessageGlobal", true);
		if(globaljoin==false)
		{
			return;
		}
		for(Player all : Bukkit.getOnlinePlayers())
		{
			if(!all.getName().equals(player.getName()))
			{
				if((boolean) plugin.getMysqlHandler().getDataI(player.getUniqueId().toString(), "joinmessage", "player_uuid"))
				{
					///%player% &6hat den Server betreten!
					TextComponent msg = utility.apichat(
							plugin.getYamlHandler().getL().getString(language+".EventJoinLeave.PlayerEnter").replace("%player%", pn), 
							ClickEvent.Action.SUGGEST_COMMAND, "@"+player.getName()+" ", 
							HoverEvent.Action.SHOW_TEXT, 
							plugin.getYamlHandler().getL().getString(language+".ChannelExtra.Hover.Message").replace("%player%", pn), 
							false);
					all.spigot().sendMessage(msg);
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
		Utility utility = plugin.getUtility();
		String scc = ".CmdScc.";
		String language = utility.getLanguage();
		
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
    			newcreator.spigot().sendMessage(utility.tctl(
    					plugin.getYamlHandler().getL().getString(language+scc+"CCLeave.NewCreator")
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
			if((boolean) plugin.getMysqlHandler().getDataI(player.getUniqueId().toString(), "joinmessage", "player_uuid"))
			{
				///%player% &4hat den Server verlassen!
				String msg = plugin.getYamlHandler().getL().getString(
						language+".EventJoinLeave.PlayerQuit").replace("%player%", pn);
				all.spigot().sendMessage(utility.tctl(msg));
			}
		}
		return;
	}
}
