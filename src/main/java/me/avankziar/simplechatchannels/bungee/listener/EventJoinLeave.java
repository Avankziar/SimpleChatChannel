package main.java.me.avankziar.simplechatchannels.bungee.listener;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.CustomChannel;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
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
		String language = plugin.getYamlHandler().get().getString("language");
		String pn = player.getName();
		plugin.getUtility().controlChannelSaves(player);
		if((boolean) plugin.getMysqlHandler().getDataI(player, "joinmessage", "player_uuid"))
		{
			player.sendMessage(plugin.getUtility().tcl(plugin.getUtility().getActiveChannels(player)));
			///Herzlich willkommen zurück &f%player% &6auf unserem Server &b[Bitte servername einfügen]
			player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(
					language+".EventJoinLeave.Welcome").replace("%player%", pn)));
		}
		plugin.getMysqlHandler().updateDataII(player, player.getName(), "ignore_name", "ignore_uuid");
		Boolean globaljoin = plugin.getYamlHandler().get().getString("ShowjoinmessageGlobal").equals("true");
		if(globaljoin==false)
		{
			return;
		}
		for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers())
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
		String language = plugin.getUtility().getLanguage();
		String pn = player.getName();
		String scc = ".CmdScc.";
		if(plugin.editorplayers.contains(player.getName()))
		{
			plugin.editorplayers.remove(player.getName());
			String µ = "µ";
			String message = "editor"+µ+player.getName()+µ+"remove";
			plugin.getUtility().sendSpigotMessage("simplechatchannels:sccbungee", message);
		}
		CustomChannel cc = CustomChannel.getCustomChannel(pn);
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
    			newcreator.sendMessage(plugin.getUtility().tcl(
    					plugin.getYamlHandler().getL().getString(language+scc+"CCLeave.NewCreator")
    					.replace("%channel%", cc.getName())));
			}
		}
		if(!plugin.getMysqlHandler().hasAccount(player))
		{
			plugin.getMysqlHandler().createAccount(player);
		}
		Boolean globalleave = plugin.getYamlHandler().get().getBoolean("ShowleavemessageGlobal", false);
		if(globalleave==false)
		{
			return;
		}
		for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers())
		{
			if((boolean) plugin.getMysqlHandler().getDataI(player, "joinmessage", "player_uuid"))
			{
				///%player% &4hat den Server verlassen!
				String msg = plugin.getYamlHandler().getL().getString(
						language+".EventJoinLeave.PlayerQuit").replace("%player%", pn);
				all.sendMessage(plugin.getUtility().tcl(msg));
			}
		}
		return;
	}
}
