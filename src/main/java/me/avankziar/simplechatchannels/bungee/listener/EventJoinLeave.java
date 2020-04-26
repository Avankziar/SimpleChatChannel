package main.java.me.avankziar.simplechatchannels.bungee.listener;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.TemporaryChannel;
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
		String language = utility.getLanguage();
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
			player.sendMessage(utility.tctl(utility.getActiveChannels(player)));
			///Herzlich willkommen zurück &f%player% &6auf unserem Server &b[Bitte servername einfügen]
			player.sendMessage(utility.tctl(plugin.getYamlHandler().getL().getString(
					language+".EventJoinLeave.Welcome").replace("%player%", pn)));
		}
		plugin.getMysqlHandler().updateDataII(player, pn, "ignore_name", "ignore_uuid");
		Boolean globaljoin = plugin.getYamlHandler().get().getBoolean("ShowJoinMessageGlobal", true);
		if(globaljoin==false)
		{
			return;
		}
		for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers())
		{
			if(!all.getName().equals(pn))
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
					all.sendMessage(msg);
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
		String language = utility.getLanguage();
		String pn = player.getName();
		String scc = ".CmdScc.";
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
    				newcreator.sendMessage(utility.tctl(
        					plugin.getYamlHandler().getL().getString(language+scc+"CCLeave.NewCreator")
        					.replace("%channel%", cc.getName())));
    			}
			}
		}
		if(!plugin.getMysqlHandler().hasAccount(player))
		{
			plugin.getMysqlHandler().createAccount(player);
		}
		Boolean globalleave = plugin.getYamlHandler().get().getBoolean("ShowLeaveMessageGlobal", false);
		if(globalleave==false)
		{
			return;
		}
		for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers())
		{
			if((boolean) plugin.getMysqlHandler().getDataI(player.getUniqueId().toString(), "joinmessage", "player_uuid"))
			{
				///%player% &4hat den Server verlassen!
				String msg = plugin.getYamlHandler().getL().getString(
						language+".EventJoinLeave.PlayerQuit").replace("%player%", pn);
				all.sendMessage(utility.tctl(msg));
			}
		}
		return;
	}
}
