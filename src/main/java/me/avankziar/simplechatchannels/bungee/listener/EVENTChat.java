package main.java.me.avankziar.simplechatchannels.bungee.listener;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.CustomChannel;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class EVENTChat implements Listener 
{
	private SimpleChatChannels plugin;
	HashMap<String,String> reply = new HashMap<String, String>();
	private String language;
	
	public EVENTChat(SimpleChatChannels plugin)
	{
		this.plugin = plugin;
		language = plugin.getYamlHandler().get().getString("language");
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onChat(ChatEvent event) //Kein Local und World enthalten, siehe dazu CMD_SCCS
	{
		ProxiedPlayer player = (ProxiedPlayer) event.getSender();
		if(event.isCancelled())
		{
			return;
		}
		if(plugin.editorplayers.contains(player.getName()))
		{
			return;
		}
		if(plugin.getPunisher()!=null)
		{
			if(plugin.getPunisher().isJailed(player))
			{
				event.setCancelled(true);
				plugin.getUtility().sendMessage(player, plugin.getYamlHandler().getL().getString(language+".punisher.msg01"));
				return;
			}
		}
		if(event.getMessage().startsWith("/"))
		{
			return;
		}
		if(event.getMessage().length()<=1)
		{
			if(plugin.getYamlHandler().get().getString("denizen_active").equals("false"))
			{
				event.setCancelled(true);
				return;
			} else
			{
				return;
			}
		}
		
		String channelwithoutsymbol = plugin.getYamlHandler().getL().getString(language+".channelsymbol.withoutsymbol");
		String channel = plugin.getYamlHandler().getChannel(channelwithoutsymbol, event.getMessage());
		String symbol = plugin.getYamlHandler().getSymbol(channel);
		
		boolean timeofdays = plugin.getYamlHandler().get().getString("addingtimeofdays").equalsIgnoreCase("true");
		String timeofdaysformat = plugin.getUtility().getDate(plugin.getYamlHandler().get().getString("timeofdaysformat"));
		String timeofdaysoutput = plugin.getUtility().tl(plugin.getYamlHandler().get().getString("timeofdaysoutput")
				.replace("%time%", timeofdaysformat));
		
		if(channel.equalsIgnoreCase("local"))
		{
			if(plugin.getYamlHandler().get().getString("is_scc_on_spigot").equals("false"))
			{
				event.setCancelled(true);
				return;
			} else
			{
				return;
			}
		}
		if(channel.equalsIgnoreCase("world"))
		{
			if(plugin.getYamlHandler().get().getString("is_scc_on_spigot").equals("false"))
			{
				event.setCancelled(true);
				return;
			} else
			{
				return;
			}
		}
		event.setCancelled(true);
		String pl = player.getUniqueId().toString();
		boolean canchat = (boolean) plugin.getMysqlInterface().getDataI(player, "can_chat", "player_uuid");
		if(!canchat)
		{
			long millitime = (long) plugin.getMysqlInterface().getDataI(player, "mutetime", "player_uuid");
			String time = "";
			if(millitime==0)
			{
				time = "permanent";
			} else
			{
				millitime = (millitime-System.currentTimeMillis())/(1000*60);
				time = String.valueOf(millitime)+" min";
			}
			plugin.getUtility().sendMessage(player, plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg07")
					.replace("%time%", time));
			return;
		}
		
		if(channel.equalsIgnoreCase("global") || channel.equalsIgnoreCase("trade") || channel.equalsIgnoreCase("support") 
				|| channel.equalsIgnoreCase("auction") || channel.equalsIgnoreCase("team") || channel.equalsIgnoreCase("admin")) 
			//----------------------------------------------------------Trade Channel
		{
			if(!plugin.getUtility().hasChannelRights(player, "channel_"+channel))
			{
				return;
			}	
			
			if(plugin.getUtility().getWordfilter(event.getMessage().substring(symbol.length()))) //Wordfilter
			{
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg06"))));
				return;
			}
			
			TextComponent MSG = null;
			if(timeofdays == true) {MSG = plugin.getUtility().tc(timeofdaysoutput);}
			else {MSG = plugin.getUtility().tc("");}
			
			MSG.setExtra(plugin.getUtility().getAllTextComponentForChannels(
					player, event.getMessage(), channel, symbol, symbol.length()));
			
			plugin.getProxy().getConsole().sendMessage(MSG); //Console
			
			if(event.getMessage().substring(1).length()<0)
			{
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg08"))));
				return;
			}
			
			plugin.getUtility().sendAllMessage(player, "channel_"+channel, MSG);
			return;
		} else if(channel.equals("custom")) //----------------------------------------------------------Support Channel
		{
			if(!plugin.getUtility().hasChannelRights(player, "channel_custom"))
			{
				return;
			}
			
			if(CustomChannel.getCustomChannel(player)==null)
			{
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg11"))));
				return;
			}
			
			CustomChannel cc = CustomChannel.getCustomChannel(player);
			
			if(event.getMessage().length()>=symbol.length() 
					&& plugin.getUtility().getWordfilter(event.getMessage().substring(symbol.length()))) //Wordfilter
			{
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg06"))));
				return;
			}
			
			TextComponent MSG = null;
			if(timeofdays == true) {MSG = plugin.getUtility().tc(timeofdaysoutput);}
			else {MSG = plugin.getUtility().tc("");}
			
			MSG.setExtra(plugin.getUtility().getAllTextComponentForChannels(
					player, event.getMessage(), "custom", symbol, symbol.length()));
			
			plugin.getProxy().getConsole().sendMessage(MSG); //Console
			
			if(event.getMessage().substring(1).length()<0)
			{
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg08"))));
				return;
			}
			plugin.getUtility().spy(MSG);
			for(ProxiedPlayer all : plugin.getProxy().getPlayers())
			{
				for(ProxiedPlayer members : cc.getMembers())
				{
					if(members.getName().equals(all.getName()))
					{
						if((boolean) plugin.getMysqlInterface().getDataI(all, "channel_custom", "player_uuid"))
						{
							if(!plugin.getUtility().getIgnored(player,all))
							{
								all.sendMessage(MSG);
							}
						}
					}
				}
			}
			return;
		} else if(channel.equalsIgnoreCase("group")) //----------------------------------------------------------Gruppe Channel
		{
			if(!plugin.getUtility().hasChannelRights(player, "channel_group"))
			{
				return;
			}
			
			String[] eventmsg = event.getMessage().split(" ");
			int lenghteventmsg = eventmsg[0].length()+1;
			
			if(event.getMessage().length()>=lenghteventmsg 
					&& plugin.getUtility().getWordfilter(event.getMessage().substring(lenghteventmsg))) //Wordfilter
			{
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg06"))));
				return;
			}
			
			String preorsuffix = eventmsg[0].substring(symbol.length());
			String pors = preorsuffix;
			
			String ps = plugin.getUtility().getPreOrSuffix(preorsuffix);
			
			if(ps.equals("scc.no_prefix_suffix"))
			{
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg09"))));
				return;
			}
			
			TextComponent channels = plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+".channels.group")
					.replace("%group%", preorsuffix)));
			channels.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, 
					symbol+pors+" "));
			channels.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(
							plugin.getYamlHandler().getL().getString(language+".channelextra.hover.group"))).create()));
			
			List<BaseComponent> prefix = plugin.getUtility().getPrefix(player);
			
			TextComponent playertext = plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+".playercolor")+player.getName()));
			playertext.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, 
					plugin.getYamlHandler().getSymbol("pm")+player.getName()+" "));
			playertext.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(
							plugin.getYamlHandler().getL().getString(language+".channelextra.hover.message")
							.replace("%player%", player.getName()))).create()));
			
			List<BaseComponent> suffix = plugin.getUtility().getSuffix(player);
			
			List<BaseComponent> msg = plugin.getUtility().msgLater(player, lenghteventmsg, channel, event.getMessage());
			
			TextComponent MSG = null;
			if(timeofdays == true) {MSG = plugin.getUtility().tc(timeofdaysoutput);}
			else {MSG = plugin.getUtility().tc("");}
			
			MSG.setExtra(plugin.getUtility().getTCinLine(channels, prefix, playertext, suffix, msg));
			
			plugin.getProxy().getConsole().sendMessage(MSG); //Console
			
			if(event.getMessage().substring(lenghteventmsg).length()<0)
			{
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg08"))));
				return;
			}
			
			player.sendMessage(MSG);
			
			for(ProxiedPlayer all : plugin.getProxy().getPlayers())
			{
				if((boolean) plugin.getMysqlInterface().getDataI(all, "channel_group", "player_uuid"))
				{
					if(!all.equals(player))
					{
						if(!plugin.getUtility().getIgnored(player,all))
						{
							if(all.hasPermission(ps))
							{
								all.sendMessage(MSG);
							}
						}
					}
				}
			}
			
			plugin.getUtility().spy(MSG);
			return;
		} else if(channel.equalsIgnoreCase("pmre")) //Reply Message
		{
			if(!plugin.getUtility().hasChannelRights(player, "channel_pm"))
			{
				return;
			}
			if(!reply.containsKey(pl))
			{
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg05"))));
				return;
			}
			String target = reply.get(pl);
			if(ProxyServer.getInstance().getPlayer(UUID.fromString(target)) == null)
			{
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg02"))));
				return;
			}
			ProxiedPlayer tr = ProxyServer.getInstance().getPlayer(UUID.fromString(target));
			
			if(plugin.getAfkRecord()!=null)
			{
				if(plugin.getAfkRecord().isAfk(tr))
				{
					player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".afkrecord.msg01")));
					return;
				}
			}
			
			String trl = tr.getUniqueId().toString();
			
			TextComponent channel1 = plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+".channels.message")));
			channel1.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, 
					plugin.getYamlHandler().getSymbol("pm")+player.getName()+" "));
			channel1.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(
							plugin.getYamlHandler().getL().getString(language+".channelextra.hover.message")
							.replace("%player%", player.getName()))).create()));
			
			TextComponent channel2 = plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+".channels.message")));
			channel2.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, 
					plugin.getYamlHandler().getSymbol("pm")+tr.getName()+" "));
			channel2.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(
							plugin.getYamlHandler().getL().getString(language+".channelextra.hover.message")
							.replace("%player%", tr.getName()))).create()));
			
			TextComponent playertext = plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+".playercolor")+player.getName()));
			playertext.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, 
					plugin.getYamlHandler().getSymbol("pm")+player.getName()+" "));
			playertext.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(
							plugin.getYamlHandler().getL().getString(language+".channelextra.hover.message")
							.replace("%player%", player.getName()))).create()));
			
			TextComponent player2text = plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+".playertoplayer")+tr.getName()));
			player2text.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, 
					plugin.getYamlHandler().getSymbol("pm")+tr.getName()+" "));
			player2text.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(
							plugin.getYamlHandler().getL().getString(language+".channelextra.hover.message")
							.replace("%player%", player.getName()))).create()));
			
			
			
			if(event.getMessage().length()>=symbol.length() && plugin.getUtility().getWordfilter(event.getMessage().substring(
					symbol.length()))) //Wordfilter
			{
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg06"))));
				return;
			}
			
			TextComponent MSG1 = null;
			if(timeofdays == true) {MSG1 = plugin.getUtility().tc(timeofdaysoutput);}
			else {MSG1 = plugin.getUtility().tc("");}
			
			TextComponent MSG2 = null;
			if(timeofdays == true) {MSG2 = plugin.getUtility().tc(timeofdaysoutput);}
			else {MSG2 = plugin.getUtility().tc("");}
			
			MSG1.setExtra(plugin.getUtility().getTCinLinePN(channel1, playertext, player2text, plugin.getUtility().msgLater(player,symbol.length(),"message", event.getMessage())));
			MSG2.setExtra(plugin.getUtility().getTCinLinePN(channel2, playertext, player2text, plugin.getUtility().msgLater(player,symbol.length(),"message", event.getMessage())));
			
			plugin.getProxy().getConsole().sendMessage(MSG1); //Console
			
			if(event.getMessage().substring(3).length()<0)
			{
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg08"))));
				return;
			}
			
			if(!plugin.getUtility().hasChannelRights(tr, "channel_pm"))
			{
				return;
			}
			if(plugin.getUtility().getIgnored(player,tr))
			{
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg03"))));
				plugin.getUtility().spy(MSG1);
				player.sendMessage(MSG2);
				return;
			}
			tr.sendMessage(MSG1);
			player.sendMessage(MSG2);
			plugin.getUtility().spy(MSG1);
			reply.put(pl, trl);
			reply.put(trl, pl);
			return;
		} else if(channel.equalsIgnoreCase("pm")) //Private Message
		{
			if(!plugin.getUtility().hasChannelRights(player, "channel_pm"))
			{
				return;
			}
			
			String[] targets = event.getMessage().split(" ");
			String target = targets[0].substring(plugin.getYamlHandler().getSymbol("pm").length());
			if(ProxyServer.getInstance().getPlayer(target) == null)
			{
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg02"))));
				return;
			}
			ProxiedPlayer tr = ProxyServer.getInstance().getPlayer(target);
			String trl = tr.getUniqueId().toString();
			
			if(plugin.getAfkRecord()!=null)
			{
				if(plugin.getAfkRecord().isAfk(tr))
				{
					player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".afkrecord.msg01")));
					return;
				}
			}
			
			TextComponent channel1 = plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+".channels.message")));
			channel1.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, 
					plugin.getYamlHandler().getSymbol("pm")+player.getName()+" "));
			channel1.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(
							plugin.getYamlHandler().getL().getString(language+".channelextra.hover.message")
							.replace("%player%", player.getName()))).create()));
			
			TextComponent channel2 = plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+".channels.message")));
			channel2.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, 
					plugin.getYamlHandler().getSymbol("pm")+tr.getName()+" "));
			channel2.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(
							plugin.getYamlHandler().getL().getString(language+".channelextra.hover.message")
							.replace("%player%", tr.getName()))).create()));
	
			TextComponent playertext = plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+".playercolor")+player.getName()));
			playertext.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, 
					plugin.getYamlHandler().getSymbol("pm")+player.getName()+" "));
			playertext.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(
							plugin.getYamlHandler().getL().getString(language+".channelextra.hover.message")
							.replace("%player%", player.getName()))).create()));
			
			TextComponent player2text = plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+".playertoplayer")+tr.getName()));
			player2text.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, 
					plugin.getYamlHandler().getSymbol("pm")+tr.getName()+" "));
			player2text.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(
							plugin.getYamlHandler().getL().getString(language+".channelextra.hover.message")
							.replace("%player%", tr.getName()))).create()));
			
			if(event.getMessage().substring(targets[0].length()+1).length()<0)
			{
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg08"))));
				return;
			}
			if(event.getMessage().length()>=plugin.getYamlHandler().getSymbol("pm").length() 
					&& plugin.getUtility().getWordfilter(event.getMessage().substring(plugin.getYamlHandler().getSymbol("pm").length()))) //Wordfilter
			{
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg06"))));
				return;
			}
			
			TextComponent MSG1 = null;
			if(timeofdays == true) {MSG1 = plugin.getUtility().tc(timeofdaysoutput);}
			else {MSG1 = plugin.getUtility().tc("");}
			
			TextComponent MSG2 = null;
			if(timeofdays == true) {MSG2 = plugin.getUtility().tc(timeofdaysoutput);}
			else {MSG2 = plugin.getUtility().tc("");}
			
			MSG1.setExtra(plugin.getUtility().getTCinLinePN(channel1, playertext, player2text, plugin.getUtility().msgLater(player,targets[0].length(),"message", event.getMessage())));
			MSG2.setExtra(plugin.getUtility().getTCinLinePN(channel2, playertext, player2text, plugin.getUtility().msgLater(player,targets[0].length(),"message", event.getMessage())));
			
			plugin.getProxy().getConsole().sendMessage(MSG1); //Console
			
			if(!plugin.getUtility().hasChannelRights(tr, "channel_pm"))
			{
				return;
			}	
			if(plugin.getUtility().getIgnored(player,tr))
			{
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg03"))));
				plugin.getUtility().spy(MSG1);
				player.sendMessage(MSG2);
				return;
			}
			tr.sendMessage(MSG1);
			player.sendMessage(MSG2);
			plugin.getUtility().spy(MSG1);
			reply.put(pl, trl);
			reply.put(trl, pl);
			return;
		}
	}
}
