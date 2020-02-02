package main.java.me.avankziar.simplechatchannels.spigot.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.interfaces.CustomChannel;

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
		if(plugin.getYamlHandler().get().getString("bungee").equals("true"))
		{
			event.setJoinMessage("");
			return;
		}
		event.setJoinMessage("");
		String language = plugin.getYamlHandler().get().getString("language");
		Player player = event.getPlayer();
		plugin.getUtility().controlChannelSaves(player);
		if((boolean) plugin.getMysqlInterface().getDataI(player, "joinmessage", "player_uuid"))
		{
			player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getUtility().getActiveChannels(player)));
			player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".EVENT_JoinLeave.msg01")
					.replaceAll("%player%", player.getName())));
		}
		Boolean globaljoin = plugin.getYamlHandler().get().getString("showjoinmessageglobal").equals("true");
		if(globaljoin==false)
		{
			return;
		}
		for(Player all : Bukkit.getOnlinePlayers())
		{
			if(!all.getName().equals(player.getName()))
			{
				if((boolean) plugin.getMysqlInterface().getDataI(player, "joinmessage", "player_uuid"))
				{
					all.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".EVENT_JoinLeave.msg02")
							.replaceAll("%player%", player.getName())));
				}
			}
		}
		return;
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event)
	{
		if(plugin.getYamlHandler().get().getString("bungee").equals("false"))
		{
			if(SimpleChatChannels.editorplayers.contains(event.getPlayer().getName()))
			{
				SimpleChatChannels.editorplayers.remove(event.getPlayer().getName());
			}
		}
		if(plugin.getYamlHandler().get().getString("bungee").equals("true"))
		{
			event.setQuitMessage("");
			return;
		}
		String scc = ".CMD_SCC.";
		String language = plugin.getYamlHandler().get().getString("language");
		
		CustomChannel cc = CustomChannel.getCustomChannel(event.getPlayer());
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
    			newcreator.spigot().sendMessage(plugin.getUtility().tcl(
    					plugin.getYamlHandler().getL().getString(language+scc+"leavechannel.msg02")
    					.replaceAll("%channel%", cc.getName())));
			}
		}
		event.setQuitMessage("");
		Player player = event.getPlayer();
		Boolean globalleave = plugin.getYamlHandler().get().getString("showleavemessageglobal").equals("true");
		if(globalleave==false)
		{
			return;
		}
		for(Player all : Bukkit.getOnlinePlayers())
		{
			if((boolean) plugin.getMysqlInterface().getDataI(player, "joinmessage", "player_uuid"))
			{
				all.spigot().sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_JoinLeave.msg03")
						.replaceAll("%player%", player.getName())));
			}
		}
		return;
	}
}
