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

public class EventChat implements Listener 
{
	private SimpleChatChannels plugin;
	HashMap<String,String> reply = new HashMap<String, String>();
	
	public EventChat(SimpleChatChannels plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onChat(ChatEvent event)
	{
		ProxiedPlayer player = (ProxiedPlayer) event.getSender();
		String language = plugin.getUtility().getLanguage();
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
				plugin.getUtility().sendMessage(player, plugin.getYamlHandler().getL().getString(language+".Punisher.Jail"));
				return;
			}
		}
		if(event.getMessage().startsWith("/"))
		{
			return;
		}
		if(event.getMessage().length()<=1)
		{
			if(!plugin.getYamlHandler().get().getBoolean("Denizen_Active", false))
			{
				event.setCancelled(true);
				return;
			} else
			{
				return;
			}
		}
		
		String channelwithoutsymbol = plugin.getYamlHandler().getL().getString(language+".ChannelSymbol.WithoutSymbol");
		String channel = plugin.getYamlHandler().getChannel(channelwithoutsymbol, event.getMessage());
		String symbol = plugin.getYamlHandler().getSymbol(channel);
		
		boolean timeofdays = plugin.getYamlHandler().get().getBoolean("AddingTimeOfDays", false);
		String timeofdaysformat = plugin.getUtility().getDate(plugin.getYamlHandler().get().getString("TimeOfDaysFormat"));
		String timeofdaysoutput = plugin.getUtility().tl(plugin.getYamlHandler().get().getString("TimeOfDaysOutput")
				.replace("%time%", timeofdaysformat));
		
		if(channel.equalsIgnoreCase("Local"))
		{
			if(!plugin.getYamlHandler().get().getBoolean("Is_SCC_On_Spigot", false))
			{
				event.setCancelled(true);
				return;
			} else
			{
				return;
			}
		}
		if(channel.equalsIgnoreCase("World"))
		{
			if(!plugin.getYamlHandler().get().getBoolean("Is_SCC_On_Spigot", false))
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
		boolean canchat = (boolean) plugin.getMysqlHandler().getDataI(player, "can_chat", "player_uuid");
		if(!canchat)
		{
			long millitime = (long) plugin.getMysqlHandler().getDataI(player, "mutetime", "player_uuid");
			String time = "";
			if(millitime==0)
			{
				time = "permanent";
			} else
			{
				millitime = (millitime-System.currentTimeMillis())/(1000*60);
				time = String.valueOf(millitime)+" min";
			}
			///Du bist für %time% gemutet!
			plugin.getUtility().sendMessage(player, plugin.getYamlHandler().getL().getString(language+".EventChat.Muted")
					.replace("%time%", time));
			return;
		}
		
		if(channel.equalsIgnoreCase("Global") || channel.equalsIgnoreCase("Trade") || channel.equalsIgnoreCase("Support") 
				|| channel.equalsIgnoreCase("Auction") || channel.equalsIgnoreCase("Team") || channel.equalsIgnoreCase("Admin")) 
			//----------------------------------------------------------Trade Channel
		{
			if(!plugin.getUtility().hasChannelRights(player, "channel_"+channel))
			{
				return;
			}	
			
			if(plugin.getUtility().getWordfilter(event.getMessage().substring(symbol.length()))) //Wordfilter
			{
				///Einer deiner geschriebenen Woerter &cist im Wortfilter enthalten, &cbitte unterlasse sowelche Ausdrücke!
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EventChat.Wordfilter"))));
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
				///Die Nachricht ist nicht lang genug!
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EventChat.MessageToShort"))));
				return;
			}
			
			plugin.getUtility().sendAllMessage(player, "channel_"+channel, MSG);
			return;
		} else if(channel.equals("Custom")) //----------------------------------------------------------Support Channel
		{
			if(!plugin.getUtility().hasChannelRights(player, "channel_custom"))
			{
				return;
			}
			
			if(CustomChannel.getCustomChannel(player)==null)
			{
				///Du bist in keinem CustomChannel!
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".CustomChannelGeneral.NotInAChannel"))));
				return;
			}
			
			CustomChannel cc = CustomChannel.getCustomChannel(player);
			
			if(event.getMessage().length()>=symbol.length() 
					&& plugin.getUtility().getWordfilter(event.getMessage().substring(symbol.length()))) //Wordfilter
			{
				///Einer deiner geschriebenen Wörter &cist im Wortfilter enthalten, &cbitte unterlasse sowelche Ausdrücke!
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EventChat.Wordfilter"))));
				return;
			}
			
			TextComponent MSG = null;
			if(timeofdays == true) {MSG = plugin.getUtility().tc(timeofdaysoutput);}
			else {MSG = plugin.getUtility().tc("");}
			
			MSG.setExtra(plugin.getUtility().getAllTextComponentForChannels(
					player, event.getMessage(), "Custom", symbol, symbol.length()));
			
			plugin.getProxy().getConsole().sendMessage(MSG); //Console
			
			if(event.getMessage().substring(1).length()<0)
			{
				///Die Nachricht ist nicht lang genug!
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EventChat.MessageToShort"))));
				return;
			}
			plugin.getUtility().spy(MSG);
			for(ProxiedPlayer all : plugin.getProxy().getPlayers())
			{
				for(ProxiedPlayer members : cc.getMembers())
				{
					if(members.getName().equals(all.getName()))
					{
						if((boolean) plugin.getMysqlHandler().getDataI(all, "channel_custom", "player_uuid"))
						{
							if(!plugin.getUtility().getIgnored(all,player))
							{
								all.sendMessage(MSG);
							}
						}
					}
				}
			}
			return;
		} else if(channel.equalsIgnoreCase("Group")) //----------------------------------------------------------Gruppe Channel
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
				///Einer deiner geschriebenen Woerter &cist im Wortfilter enthalten, &cbitte unterlasse sowelche Ausdrücke!
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EventChat.Wordfilter"))));
				return;
			}
			
			String preorsuffix = eventmsg[0].substring(symbol.length());
			String pors = preorsuffix;
			
			String ps = plugin.getUtility().getPreOrSuffix(preorsuffix);
			
			if(ps.equals("scc.no_prefix_suffix"))
			{
				///Warnung, die Prefixe oder Suffixe dürfen keine Leerzeichen &cbeinhalten!
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EventChat.NoSpace"))));
				return;
			}
			
			TextComponent channels = plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+".Channels.Group")
					.replace("%group%", preorsuffix)));
			channels.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, 
					symbol+pors+" "));
			channels.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(
							plugin.getYamlHandler().getL().getString(language+".ChannelExtra.Hover.Group"))).create()));
			
			List<BaseComponent> prefix = plugin.getUtility().getPrefix(player);
			
			TextComponent playertext = plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+".PlayerColor")+player.getName()));
			playertext.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, 
					plugin.getYamlHandler().getSymbol("pm")+player.getName()+" "));
			playertext.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(
							plugin.getYamlHandler().getL().getString(language+".ChannelExtra.Hover.PrivateMessage")
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
				///Die Nachricht ist nicht lang genug!
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EventChat.MessageToShort"))));
				return;
			}
			
			player.sendMessage(MSG);
			
			for(ProxiedPlayer all : plugin.getProxy().getPlayers())
			{
				if((boolean) plugin.getMysqlHandler().getDataI(all, "channel_group", "player_uuid"))
				{
					if(!all.equals(player))
					{
						if(!plugin.getUtility().getIgnored(all,player))
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
		} else if(channel.equalsIgnoreCase("PrivateMessageRe")) //Reply Message
		{
			if(!plugin.getUtility().hasChannelRights(player, "channel_pm"))
			{
				return;
			}
			if(!reply.containsKey(pl))
			{
				///Du hast mit keinem Spieler dich privat unterhalten!
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EventChat.NoRePlayer"))));
				return;
			}
			String target = reply.get(pl);
			if(ProxyServer.getInstance().getPlayer(UUID.fromString(target)) == null)
			{
				///Der Spieler ist nicht online oder existiert nicht!
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".CmdScc.NoPlayerExist"))));
				return;
			}
			ProxiedPlayer tr = ProxyServer.getInstance().getPlayer(UUID.fromString(target));
			
			if(plugin.getAfkRecord()!=null)
			{
				if(plugin.getAfkRecord().isAfk(tr))
				{
					///Der Spieler ist afk!
					player.sendMessage(plugin.getUtility().tcl(
							plugin.getYamlHandler().getL().getString(language+".AfkRecord.IsAfk")));
					return;
				}
			}
			
			String trl = tr.getUniqueId().toString();
			
			TextComponent channel1 = plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+".Channels.PrivateMessage")));
			channel1.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, 
					plugin.getYamlHandler().getSymbol("PrivateMessage")+player.getName()+" "));
			channel1.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(
							plugin.getYamlHandler().getL().getString(language+".ChannelExtra.Hover.PrivateMessage")
							.replace("%player%", player.getName()))).create()));
			
			TextComponent channel2 = plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+".Channels.PrivateMessage")));
			channel2.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, 
					plugin.getYamlHandler().getSymbol("PrivateMessage")+tr.getName()+" "));
			channel2.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(
							plugin.getYamlHandler().getL().getString(language+".ChannelExtra.Hover.PrivateMessage")
							.replace("%player%", tr.getName()))).create()));
			
			TextComponent playertext = plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+".PlayerColor")+player.getName()));
			playertext.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, 
					plugin.getYamlHandler().getSymbol("PrivateMessage")+player.getName()+" "));
			playertext.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(
							plugin.getYamlHandler().getL().getString(language+".ChannelExtra.Hover.PrivateMessage")
							.replace("%player%", player.getName()))).create()));
			
			TextComponent player2text = plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+".PlayerToPlayer")+tr.getName()));
			player2text.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, 
					plugin.getYamlHandler().getSymbol("PrivateMessage")+tr.getName()+" "));
			player2text.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(
							plugin.getYamlHandler().getL().getString(language+".ChannelExtra.Hover.PrivateMessage")
							.replace("%player%", player.getName()))).create()));
			
			
			
			if(event.getMessage().length()>=symbol.length() && plugin.getUtility().getWordfilter(event.getMessage().substring(
					symbol.length())))
			{
				///Einer deiner geschriebenen Woerter &cist im Wortfilter enthalten, &cbitte unterlasse sowelche Ausdrücke!
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EventChat.Wordfilter"))));
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
				///Die Nachricht ist nicht lang genug!
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EventChat.MessageToShort"))));
				return;
			}
			
			if(!plugin.getUtility().hasChannelRights(tr, "channel_pm"))
			{
				return;
			}
			if(plugin.getUtility().getIgnored(tr, player))
			{
				///Der Spieler ignoriert dich!
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EventChat.PlayerIgnoreYou"))));
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
		} else if(channel.equalsIgnoreCase("PrivateMessage")) //Private Message
		{
			if(!plugin.getUtility().hasChannelRights(player, "channel_pm"))
			{
				return;
			}
			
			String[] targets = event.getMessage().split(" ");
			String target = targets[0].substring(plugin.getYamlHandler().getSymbol("PrivateMessage").length());
			if(ProxyServer.getInstance().getPlayer(target) == null)
			{
				///Der Spieler ist nicht online oder existiert nicht!
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".CmdScc.NoPlayerExist"))));
				return;
			}
			ProxiedPlayer tr = ProxyServer.getInstance().getPlayer(target);
			String trl = tr.getUniqueId().toString();
			
			if(plugin.getAfkRecord()!=null)
			{
				if(plugin.getAfkRecord().isAfk(tr))
				{
					///Der Spieler ist &cafk&e!
					player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".AfkRecord.IsAfk")));
					return;
				}
			}
			
			TextComponent channel1 = plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+".Channels.PrivateMessage")));
			channel1.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, 
					plugin.getYamlHandler().getSymbol("PrivateMessage")+player.getName()+" "));
			channel1.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(
							plugin.getYamlHandler().getL().getString(language+".ChannelExtra.Hover.PrivateMessage")
							.replace("%player%", player.getName()))).create()));
			
			TextComponent channel2 = plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+".Channels.PrivateMessage")));
			channel2.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, 
					plugin.getYamlHandler().getSymbol("PrivateMessage")+tr.getName()+" "));
			channel2.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(
							plugin.getYamlHandler().getL().getString(language+".ChannelExtra.Hover.PrivateMessage")
							.replace("%player%", tr.getName()))).create()));
	
			TextComponent playertext = plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+".PlayerColor")+player.getName()));
			playertext.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, 
					plugin.getYamlHandler().getSymbol("PrivateMessage")+player.getName()+" "));
			playertext.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(
							plugin.getYamlHandler().getL().getString(language+".ChannelExtra.Hover.PrivateMessage")
							.replace("%player%", player.getName()))).create()));
			
			TextComponent player2text = plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+".PlayerToPlayer")+tr.getName()));
			player2text.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, 
					plugin.getYamlHandler().getSymbol("PrivateMessage")+tr.getName()+" "));
			player2text.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(
							plugin.getYamlHandler().getL().getString(language+".ChannelExtra.Hover.PrivateMessage")
							.replace("%player%", tr.getName()))).create()));
			
			if(event.getMessage().substring(targets[0].length()+1).length()<0)
			{
				///Die Nachricht ist nicht lang genug!
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EventChat.MessageToShort"))));
				return;
			}
			if(event.getMessage().length()>=plugin.getYamlHandler().getSymbol("PrivateMessage").length() 
					&& plugin.getUtility().getWordfilter(event.getMessage().substring(
							plugin.getYamlHandler().getSymbol("PrivateMessage").length()))) //Wordfilter
			{
				///Einer deiner geschriebenen Woerter &cist im Wortfilter enthalten, &cbitte unterlasse sowelche Ausdrücke!
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EventChat.Wordfilter"))));
				return;
			}
			
			TextComponent MSG1 = null;
			if(timeofdays == true) {MSG1 = plugin.getUtility().tc(timeofdaysoutput);}
			else {MSG1 = plugin.getUtility().tc("");}
			
			TextComponent MSG2 = null;
			if(timeofdays == true) {MSG2 = plugin.getUtility().tc(timeofdaysoutput);}
			else {MSG2 = plugin.getUtility().tc("");}
			
			MSG1.setExtra(plugin.getUtility().getTCinLinePN(channel1, playertext, player2text, 
					plugin.getUtility().msgLater(player,targets[0].length(),"PrivateMessage", event.getMessage())));
			MSG2.setExtra(plugin.getUtility().getTCinLinePN(channel2, playertext, player2text, 
					plugin.getUtility().msgLater(player,targets[0].length(),"PrivateMessage", event.getMessage())));
			
			plugin.getProxy().getConsole().sendMessage(MSG1); //Console
			
			if(!plugin.getUtility().hasChannelRights(tr, "channel_pm"))
			{
				return;
			}	
			if(plugin.getUtility().getIgnored(tr, player))
			{
				///Der Spieler ignoriert dich!
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EventChat.PlayerIgnoreYou"))));
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
