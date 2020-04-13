package main.java.me.avankziar.simplechatchannels.bungee.listener;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.database.MysqlHandler;
import main.java.me.avankziar.simplechatchannels.bungee.database.YamlHandler;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.CustomChannel;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
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
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage();
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
				player.sendMessage(utility.tctlYaml(language+".Punisher.Jail"));
				return;
			}
		}
		if(event.getMessage().startsWith("/"))
		{
			return;
		}
		YamlHandler yamlHandler = plugin.getYamlHandler();
		if(event.getMessage().length()<=1)
		{
			if(!yamlHandler.get().getBoolean("Denizen_Active", false))
			{
				event.setCancelled(true);
				return;
			} else
			{
				return;
			}
		}
		
		String channelwithoutsymbol = yamlHandler.getL().getString(language+".ChannelSymbol.WithoutSymbol");
		String channel = yamlHandler.getChannel(channelwithoutsymbol, event.getMessage());
		String symbol = yamlHandler.getSymbol(channel);
		
		boolean timeofdays = yamlHandler.get().getBoolean("AddingTimeOfDays", false);
		String timeofdaysformat = utility.getDate(yamlHandler.get().getString("TimeOfDaysFormat"));
		String timeofdaysoutput = utility.tl(yamlHandler.get().getString("TimeOfDaysOutput")
				.replace("%time%", timeofdaysformat));
		
		if(channel.equalsIgnoreCase("Local"))
		{
			if(!yamlHandler.get().getBoolean("Is_SCC_On_Spigot", false))
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
			if(!yamlHandler.get().getBoolean("Is_SCC_On_Spigot", false))
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
		MysqlHandler mysqlHandler = plugin.getMysqlHandler();
		boolean canchat = (boolean) mysqlHandler.getDataI(player, "can_chat", "player_uuid");
		if(!canchat)
		{
			long millitime = (long) mysqlHandler.getDataI(player, "mutetime", "player_uuid");
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
			player.sendMessage(utility.tctl(yamlHandler.getL().getString(language+".EventChat.Muted")
					.replace("%time%", time)));
			return;
		}
		
		if(channel.equalsIgnoreCase("Global") || channel.equalsIgnoreCase("Trade") || channel.equalsIgnoreCase("Support") 
				|| channel.equalsIgnoreCase("Auction") || channel.equalsIgnoreCase("Team") || channel.equalsIgnoreCase("Admin")) 
			//----------------------------------------------------------Trade Channel
		{
			if(!utility.hasChannelRights(player, "channel_"+channel))
			{
				return;
			}	
			
			if(utility.getWordfilter(event.getMessage().substring(symbol.length())))
			{
				///Einer deiner geschriebenen Woerter &cist im Wortfilter enthalten, &cbitte unterlasse sowelche Ausdrücke!
				player.sendMessage(utility.tctlYaml(language+".EventChat.Wordfilter"));
				return;
			}
			
			TextComponent MSG = null;
			if(timeofdays == true) {MSG = utility.tc(timeofdaysoutput);}
			else {MSG = utility.tc("");}
			
			MSG.setExtra(utility.getAllTextComponentForChannels(
					player, event.getMessage(), channel, symbol, symbol.length()));
			
			plugin.getProxy().getConsole().sendMessage(MSG); //Console
			
			if(event.getMessage().substring(1).length()<0)
			{
				///Die Nachricht ist nicht lang genug!
				player.sendMessage(utility.tctlYaml(language+".EventChat.MessageToShort"));
				return;
			}
			
			utility.sendAllMessage(player, "channel_"+channel, MSG);
			return;
		} else if(channel.equals("Custom")) //----------------------------------------------------------Support Channel
		{
			if(!utility.hasChannelRights(player, "channel_custom"))
			{
				return;
			}
			
			if(CustomChannel.getCustomChannel(player)==null)
			{
				///Du bist in keinem CustomChannel!
				player.sendMessage(utility.tctlYaml(language+".CustomChannelGeneral.NotInAChannel"));
				return;
			}
			
			CustomChannel cc = CustomChannel.getCustomChannel(player);
			
			if(event.getMessage().length()>=symbol.length() 
					&& utility.getWordfilter(event.getMessage().substring(symbol.length()))) //Wordfilter
			{
				///Einer deiner geschriebenen Wörter &cist im Wortfilter enthalten, &cbitte unterlasse sowelche Ausdrücke!
				player.sendMessage(utility.tctlYaml(language+".EventChat.Wordfilter"));
				return;
			}
			
			TextComponent MSG = null;
			if(timeofdays == true) {MSG = utility.tc(timeofdaysoutput);}
			else {MSG = utility.tc("");}
			
			MSG.setExtra(utility.getAllTextComponentForChannels(
					player, event.getMessage(), "Custom", symbol, symbol.length()));
			
			plugin.getProxy().getConsole().sendMessage(MSG); //Console
			
			if(event.getMessage().substring(1).length()<0)
			{
				///Die Nachricht ist nicht lang genug!
				player.sendMessage(utility.tctlYaml(language+".EventChat.MessageToShort"));
				return;
			}
			utility.spy(MSG);
			for(ProxiedPlayer all : plugin.getProxy().getPlayers())
			{
				for(ProxiedPlayer members : cc.getMembers())
				{
					if(members.getName().equals(all.getName()))
					{
						if((boolean) mysqlHandler.getDataI(all, "channel_custom", "player_uuid"))
						{
							if(!utility.getIgnored(all,player))
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
			if(!utility.hasChannelRights(player, "channel_group"))
			{
				return;
			}
			
			String[] eventmsg = event.getMessage().split(" ");
			int lenghteventmsg = eventmsg[0].length()+1;
			
			if(event.getMessage().length()>=lenghteventmsg 
					&& utility.getWordfilter(event.getMessage().substring(lenghteventmsg))) //Wordfilter
			{
				///Einer deiner geschriebenen Woerter &cist im Wortfilter enthalten, &cbitte unterlasse sowelche Ausdrücke!
				player.sendMessage(utility.tctlYaml(language+".EventChat.Wordfilter"));
				return;
			}
			
			String preorsuffix = eventmsg[0].substring(symbol.length());
			String pors = preorsuffix;
			
			String ps = utility.getPreOrSuffix(preorsuffix);
			
			if(ps.equals("scc.no_prefix_suffix"))
			{
				///Warnung, die Prefixe oder Suffixe dürfen keine Leerzeichen &cbeinhalten!
				player.sendMessage(utility.tctlYaml(language+".EventChat.NoSpace"));
				return;
			}
			
			TextComponent channels = utility.apichat(yamlHandler.getL().getString(language+".Channels.Group")
					.replace("%group%", preorsuffix), 
					ClickEvent.Action.SUGGEST_COMMAND, symbol+pors+" ", 
					HoverEvent.Action.SHOW_TEXT, yamlHandler.getL().getString(language+".ChannelExtra.Hover.Group"), false);
			
			List<BaseComponent> prefix = utility.getPrefix(player);
			
			TextComponent playertext = utility.apichat(yamlHandler.getL().getString(language+".PlayerColor")+player.getName(), 
					ClickEvent.Action.SUGGEST_COMMAND, yamlHandler.getSymbol("PrivateMessage")+player.getName()+" ", 
					HoverEvent.Action.SHOW_TEXT, yamlHandler.getL().getString(language+".ChannelExtra.Hover.PrivateMessage")
					.replace("%player%", player.getName()), false);
			
			List<BaseComponent> suffix = utility.getSuffix(player);
			
			List<BaseComponent> msg = utility.msgLater(player, lenghteventmsg, channel, event.getMessage());
			
			TextComponent MSG = null;
			if(timeofdays == true) {MSG = utility.tc(timeofdaysoutput);}
			else {MSG = utility.tc("");}
			
			MSG.setExtra(utility.getTCinLine(channels, prefix, playertext, suffix, msg));
			
			plugin.getProxy().getConsole().sendMessage(MSG); //Console
			
			if(event.getMessage().substring(lenghteventmsg).length()<0)
			{
				///Die Nachricht ist nicht lang genug!
				player.sendMessage(utility.tctlYaml(language+".EventChat.MessageToShort"));
				return;
			}
			
			player.sendMessage(MSG);
			
			for(ProxiedPlayer all : plugin.getProxy().getPlayers())
			{
				if((boolean) mysqlHandler.getDataI(all, "channel_group", "player_uuid"))
				{
					if(!all.equals(player))
					{
						if(!utility.getIgnored(all,player))
						{
							if(all.hasPermission(ps))
							{
								all.sendMessage(MSG);
							}
						}
					}
				}
			}
			
			utility.spy(MSG);
			return;
		} else if(channel.equalsIgnoreCase("PrivateMessageRe")) //Reply Message
		{
			if(!utility.hasChannelRights(player, "channel_pm"))
			{
				return;
			}
			if(!reply.containsKey(pl))
			{
				///Du hast mit keinem Spieler dich privat unterhalten!
				player.sendMessage(utility.tctlYaml(language+".EventChat.NoRePlayer"));
				return;
			}
			String target = reply.get(pl);
			if(ProxyServer.getInstance().getPlayer(UUID.fromString(target)) == null)
			{
				///Der Spieler ist nicht online oder existiert nicht!
				player.sendMessage(utility.tctlYaml(language+".CmdScc.NoPlayerExist"));
				return;
			}
			ProxiedPlayer tr = ProxyServer.getInstance().getPlayer(UUID.fromString(target));
			
			if(plugin.getAfkRecord()!=null)
			{
				if(plugin.getAfkRecord().isAfk(tr))
				{
					///Der Spieler ist afk!
					player.sendMessage(utility.tctlYaml(language+".AfkRecord.IsAfk"));
				}
			}
			
			String trl = tr.getUniqueId().toString();
			
			TextComponent channel1 = utility.apichat(language+".Channels.PrivateMessage", 
					ClickEvent.Action.SUGGEST_COMMAND, yamlHandler.getSymbol("PrivateMessage")+player.getName()+" ", 
					HoverEvent.Action.SHOW_TEXT, yamlHandler.getL().getString(language+".ChannelExtra.Hover.PrivateMessage")
					.replace("%player%", player.getName()), true);
			
			TextComponent channel2 = utility.apichat(language+".Channels.PrivateMessage", 
					ClickEvent.Action.SUGGEST_COMMAND, yamlHandler.getSymbol("PrivateMessage")+tr.getName()+" ", 
					HoverEvent.Action.SHOW_TEXT, yamlHandler.getL().getString(language+".ChannelExtra.Hover.PrivateMessage")
					.replace("%player%", tr.getName()), true);
			
			TextComponent playertext = utility.apichat(yamlHandler.getL().getString(language+".PlayerColor")+player.getName(), 
					ClickEvent.Action.SUGGEST_COMMAND, yamlHandler.getSymbol("PrivateMessage")+player.getName()+" ", 
					HoverEvent.Action.SHOW_TEXT, yamlHandler.getL().getString(language+".ChannelExtra.Hover.PrivateMessage")
					.replace("%player%", player.getName()), false);
			
			TextComponent player2text = utility.apichat(yamlHandler.getL().getString(language+".PlayerToPlayer")+tr.getName(), 
					ClickEvent.Action.SUGGEST_COMMAND, yamlHandler.getSymbol("PrivateMessage")+tr.getName()+" ", 
					HoverEvent.Action.SHOW_TEXT, yamlHandler.getL().getString(language+".ChannelExtra.Hover.PrivateMessage")
					.replace("%player%", player.getName()), false);
			
			if(event.getMessage().length()>=symbol.length() && utility.getWordfilter(event.getMessage().substring(
					symbol.length())))
			{
				///Einer deiner geschriebenen Woerter &cist im Wortfilter enthalten, &cbitte unterlasse sowelche Ausdrücke!
				player.sendMessage(utility.tctlYaml(language+".EventChat.Wordfilter"));
				return;
			}
			
			TextComponent MSG1 = null;
			if(timeofdays == true) {MSG1 = utility.tc(timeofdaysoutput);}
			else {MSG1 = utility.tc("");}
			
			TextComponent MSG2 = null;
			if(timeofdays == true) {MSG2 = utility.tc(timeofdaysoutput);}
			else {MSG2 = utility.tc("");}
			
			MSG1.setExtra(utility.getTCinLinePN(channel1, playertext, player2text, 
					utility.msgLater(player,symbol.length(),"PrivateMessage", event.getMessage())));
			MSG2.setExtra(utility.getTCinLinePN(channel2, playertext, player2text, 
					utility.msgLater(player,symbol.length(),"PrivateMessage", event.getMessage())));
			
			plugin.getProxy().getConsole().sendMessage(MSG1); //Console
			
			if(event.getMessage().substring(3).length()<0)
			{
				///Die Nachricht ist nicht lang genug!
				player.sendMessage(utility.tctlYaml(language+".EventChat.MessageToShort"));
				return;
			}
			
			if(!(boolean) mysqlHandler.getDataI(tr, "channel_pm", "player_uuid"))
			{
				///Der Spieler hat private Nachrichten &cdeaktiviert!
				player.sendMessage(utility.tctlYaml(language+".EventChat.PlayerNoPrivateMessage"));
				if(!player.hasPermission(Utility.PERMBYPASSPRIVACY))
				{
					return;
				}
			}	
			
			if(utility.getIgnored(tr, player))
			{
				///Der Spieler ignoriert dich!
				player.sendMessage(utility.tctlYaml(language+".EventChat.PlayerIgnoreYou"));
				utility.spy(MSG1);
				player.sendMessage(MSG2);
				return;
			}
			tr.sendMessage(MSG1);
			player.sendMessage(MSG2);
			utility.spy(MSG1);
			reply.put(pl, trl);
			reply.put(trl, pl);
			return;
		} else if(channel.equalsIgnoreCase("PrivateMessage")) //Private Message
		{
			if(!utility.hasChannelRights(player, "channel_pm"))
			{
				return;
			}
			
			String[] targets = event.getMessage().split(" ");
			String target = targets[0].substring(yamlHandler.getSymbol("PrivateMessage").length());
			if(ProxyServer.getInstance().getPlayer(target) == null)
			{
				///Der Spieler ist nicht online oder existiert nicht!
				player.sendMessage(utility.tctlYaml(language+".CmdScc.NoPlayerExist"));
				return;
			}
			ProxiedPlayer tr = ProxyServer.getInstance().getPlayer(target);
			String trl = tr.getUniqueId().toString();
			
			if(plugin.getAfkRecord()!=null)
			{
				if(plugin.getAfkRecord().isAfk(tr))
				{
					///Der Spieler ist &cafk&e!
					player.sendMessage(utility.tctlYaml(language+".AfkRecord.IsAfk"));
				}
			}
			
			TextComponent channel1 = utility.apichat(language+".Channels.PrivateMessage", 
					ClickEvent.Action.SUGGEST_COMMAND, yamlHandler.getSymbol("PrivateMessage")+player.getName()+" ", 
					HoverEvent.Action.SHOW_TEXT, yamlHandler.getL().getString(language+".ChannelExtra.Hover.PrivateMessage")
					.replace("%player%", player.getName()), true);
			
			TextComponent channel2 = utility.apichat(language+".Channels.PrivateMessage", 
					ClickEvent.Action.SUGGEST_COMMAND, yamlHandler.getSymbol("PrivateMessage")+tr.getName()+" ", 
					HoverEvent.Action.SHOW_TEXT, yamlHandler.getL().getString(language+".ChannelExtra.Hover.PrivateMessage")
					.replace("%player%", tr.getName()), true);
	
			TextComponent playertext = utility.apichat(yamlHandler.getL().getString(language+".PlayerColor")+player.getName(), 
					ClickEvent.Action.SUGGEST_COMMAND, yamlHandler.getSymbol("PrivateMessage")+player.getName()+" ", 
					HoverEvent.Action.SHOW_TEXT, yamlHandler.getL().getString(language+".ChannelExtra.Hover.PrivateMessage")
					.replace("%player%", player.getName()), false);
			
			TextComponent player2text = utility.apichat(yamlHandler.getL().getString(language+".PlayerToPlayer")+tr.getName(), 
					ClickEvent.Action.SUGGEST_COMMAND, yamlHandler.getSymbol("PrivateMessage")+tr.getName()+" ", 
					HoverEvent.Action.SHOW_TEXT, yamlHandler.getL().getString(language+".ChannelExtra.Hover.PrivateMessage")
					.replace("%player%", tr.getName()), false);
			
			if(event.getMessage().substring(targets[0].length()+1).length()<0)
			{
				///Die Nachricht ist nicht lang genug!
				player.sendMessage(utility.tctlYaml(language+".EventChat.MessageToShort"));
				return;
			}
			if(event.getMessage().length()>=yamlHandler.getSymbol("PrivateMessage").length() 
					&& utility.getWordfilter(event.getMessage().substring(
							yamlHandler.getSymbol("PrivateMessage").length()))) //Wordfilter
			{
				///Einer deiner geschriebenen Woerter &cist im Wortfilter enthalten, &cbitte unterlasse sowelche Ausdrücke!
				player.sendMessage(utility.tctlYaml(language+".EventChat.Wordfilter"));
				return;
			}
			
			TextComponent MSG1 = null;
			if(timeofdays == true) {MSG1 = utility.tc(timeofdaysoutput);}
			else {MSG1 = utility.tc("");}
			
			TextComponent MSG2 = null;
			if(timeofdays == true) {MSG2 = utility.tc(timeofdaysoutput);}
			else {MSG2 = utility.tc("");}
			
			MSG1.setExtra(utility.getTCinLinePN(channel1, playertext, player2text, 
					utility.msgLater(player,targets[0].length(),"PrivateMessage", event.getMessage())));
			MSG2.setExtra(utility.getTCinLinePN(channel2, playertext, player2text, 
					utility.msgLater(player,targets[0].length(),"PrivateMessage", event.getMessage())));
			
			plugin.getProxy().getConsole().sendMessage(MSG1); //Console
			
			if(!(boolean) mysqlHandler.getDataI(tr, "channel_pm", "player_uuid"))
			{
				///Der Spieler hat private Nachrichten &cdeaktiviert!
				player.sendMessage(utility.tctlYaml(language+".EventChat.PlayerNoPrivateMessage"));
				if(!player.hasPermission(Utility.PERMBYPASSPRIVACY))
				{
					return;
				}
			}	
			
			if(utility.getIgnored(tr, player))
			{
				///Der Spieler ignoriert dich!
				player.sendMessage(utility.tctlYaml(language+".EventChat.PlayerIgnoreYou"));
				utility.spy(MSG1);
				player.sendMessage(MSG2);
				return;
			}
			tr.sendMessage(MSG1);
			player.sendMessage(MSG2);
			utility.spy(MSG1);
			reply.put(pl, trl);
			reply.put(trl, pl);
			return;
		}
	}
}