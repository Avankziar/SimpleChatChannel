package main.java.de.avankziar.simplechatchannels.bungee.listener;

import main.java.de.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.de.avankziar.simplechatchannels.bungee.interfaces.CustomChannel;
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

public class EVENTJoinLeave implements Listener
{
	private SimpleChatChannels plugin;
	
	public EVENTJoinLeave(SimpleChatChannels plugin)
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
		player.sendMessage(plugin.getUtility().tcl(plugin.getUtility().getActiveChannels(player)));
		player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(
				language+".EVENT_JoinLeave.msg01").replaceAll("%player%", pn)));
		for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers())
		{
			if(!all.getName().equals(player.getName()))
			{
				if((boolean) plugin.getMysqlInterface().getDataI(player, "joinmessage", "player_uuid"))
				{
					TextComponent msg = plugin.getUtility().tcl(
							plugin.getYamlHandler().getL().getString(language+".EVENT_JoinLeave.msg02")
							.replaceAll("%player%", pn));
					msg.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, "@"+player.getName()+" "));
					msg.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
							, new ComponentBuilder(plugin.getUtility().tl(
									plugin.getYamlHandler().getL().getString(language+".channelextra.hover.message")
									.replaceAll("%player%", player.getName()))).create()));
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
		String language = plugin.getYamlHandler().get().getString("language");
		String pn = player.getName();
		String scc = ".CMD_SCC.";
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
    			newcreator.sendMessage(plugin.getUtility().tcl(
    					plugin.getYamlHandler().getL().getString(language+scc+"leavechannel.msg02")
    					.replaceAll("%channel%", cc.getName())));
			}
		}
		if(!plugin.getMysqlInterface().hasAccount(player))
		{
			plugin.getMysqlInterface().createAccount(player);
		}
		for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers())
		{
			if((boolean) plugin.getMysqlInterface().getDataI(player, "joinmessage", "player_uuid"))
			{
				String msg = plugin.getYamlHandler().getL().getString(
						language+".EVENT_JoinLeave.msg03").replaceAll("%player%", pn);
				all.sendMessage(plugin.getUtility().tcl(msg));
			}
		}
		return;
	}
}
