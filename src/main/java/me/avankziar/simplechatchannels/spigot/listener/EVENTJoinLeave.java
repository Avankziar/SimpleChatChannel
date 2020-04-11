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
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
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
		if(plugin.getYamlHandler().get().getBoolean("bungee", false))
		{
			event.setJoinMessage("");
			return;
		}
		event.setJoinMessage("");
		String language = plugin.getUtility().getLanguage();
		Player player = event.getPlayer();
		String pn = player.getName();
		plugin.getUtility().controlChannelSaves(player);
		if((boolean) plugin.getMysqlHandler().getDataI(player, "joinmessage", "player_uuid"))
		{
			player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getUtility().getActiveChannels(player)));
			///Herzlich willkommen zurück &f%player% &6auf unserem Server &b[Bitte servername einfügen]
			player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(
					language+".EventJoinLeave.Welcome").replace("%player%", pn)));
		}
		plugin.getMysqlHandler().updateDataII(player, player.getName(), "ignore_name", "ignore_uuid");
		Boolean globaljoin = plugin.getYamlHandler().get().getBoolean("ShowJoinMessageGlobal", false);
		if(globaljoin==false)
		{
			return;
		}
		for(Player all : Bukkit.getOnlinePlayers())
		{
			if(!all.getName().equals(player.getName()))
			{
				if((boolean) plugin.getMysqlHandler().getDataI(player, "joinmessage", "player_uuid"))
				{
					///%player% &6hat den Server betreten!
					TextComponent msg = plugin.getUtility().tcl(
							plugin.getYamlHandler().getL().getString(language+".EventJoinLeave.PlayerEnter")
							.replace("%player%", pn));
					msg.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "@"+player.getName()+" "));
					msg.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
							, new ComponentBuilder(plugin.getUtility().tl(
									plugin.getYamlHandler().getL().getString(language+".ChannelExtra.Hover.Message")
									.replace("%player%", player.getName()))).create()));
					all.spigot().sendMessage(msg);
				}
			}
		}
		return;
	}
	
	@EventHandler
	public void onLeave(PlayerQuitEvent event)
	{
		if(plugin.getYamlHandler().get().getBoolean("bungee", false))
		{
			if(SimpleChatChannels.editorplayers.contains(event.getPlayer().getName()))
			{
				SimpleChatChannels.editorplayers.remove(event.getPlayer().getName());
			}
		}
		if(plugin.getYamlHandler().get().getBoolean("bungee", false))
		{
			event.setQuitMessage("");
			return;
		}
		String scc = ".CmdScc.";
		String language = plugin.getUtility().getLanguage();
		
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
    			///Du wurdest der neue Ersteller der CustomChannels %channel%
    			newcreator.spigot().sendMessage(plugin.getUtility().tcl(
    					plugin.getYamlHandler().getL().getString(language+scc+"CCLeave.NewCreator")
    					.replace("%channel%", cc.getName())));
			}
		}
		event.setQuitMessage("");
		Player player = event.getPlayer();
		String pn = player.getName();
		Boolean globalleave = plugin.getYamlHandler().get().getBoolean("ShowleavemessageGlobal", false);
		if(globalleave==false)
		{
			return;
		}
		for(Player all : Bukkit.getOnlinePlayers())
		{
			if((boolean) plugin.getMysqlHandler().getDataI(player, "joinmessage", "player_uuid"))
			{
				///%player% &4hat den Server verlassen!
				String msg = plugin.getYamlHandler().getL().getString(
						language+".EventJoinLeave.PlayerQuit").replace("%player%", pn);
				all.spigot().sendMessage(plugin.getUtility().tcl(msg));
			}
		}
		return;
	}
}
