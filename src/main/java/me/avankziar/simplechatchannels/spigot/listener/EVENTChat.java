package main.java.me.avankziar.simplechatchannels.spigot.listener;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import main.java.me.avankziar.simplechatchannels.spigot.Utility;
import main.java.me.avankziar.simplechatchannels.spigot.database.MysqlHandler;
import main.java.me.avankziar.simplechatchannels.spigot.database.YamlHandler;
import main.java.me.avankziar.simplechatchannels.spigot.interfaces.PermanentChannel;
import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.interfaces.TemporaryChannel;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class EVENTChat implements Listener
{
	private SimpleChatChannels plugin;
	HashMap<String,String> reply = new HashMap<String, String>();
	
	public EVENTChat(SimpleChatChannels plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler (priority = EventPriority.HIGHEST)
	public void onChat(AsyncPlayerChatEvent event) throws InterruptedException, ExecutionException
	{
		Player player = (Player) event.getPlayer();
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage();
		if(event.isCancelled())
		{
			return;
		}
		
		if(SimpleChatChannels.editorplayers.contains(player.getName()))
		{
			return;
		}
		
		if(plugin.getPunisher()!=null)
		{
			if(plugin.getPunisher().isPlayerJailed(player))
			{
				event.setCancelled(true);
				///Du bist im Gefängnis! Du kannst nichts schreiben und auch keine Befehle nutzten!
				player.spigot().sendMessage(utility.tctlYaml(language+".Punisher.Jail"));
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
			if(yamlHandler.get().getBoolean("Denizen_Active", false))
			{
				event.setCancelled(true);
				return;
			} else
			{
				if(Bukkit.getScheduler().callSyncMethod(plugin, () -> utility.getTarget(event.getPlayer())).get())
				{
					return;
				} else
				{
					event.setCancelled(true);
					return;
				}
			}
		}
		
		String channelwithoutsymbol = yamlHandler.getL().getString(language+".ChannelSymbol.WithoutSymbol");
		String channel = yamlHandler.getChannel(channelwithoutsymbol, event.getMessage());
		String symbol = yamlHandler.getSymbol(channel);
		
		boolean timeofdays = yamlHandler.get().getBoolean("AddingTimeOfDays", false);
		String timeofdaysformat = utility.getDate(yamlHandler.get().getString("TimeOfDaysFormat"));
		String timeofdaysoutput = utility.tl(yamlHandler.get().getString("TimeOfDaysOutput")
				.replace("%time%", timeofdaysformat));
		
		event.setCancelled(true);	
		String pl = player.getUniqueId().toString();
		MysqlHandler mysqlHandler = plugin.getMysqlHandler();
		boolean canchat = (boolean) mysqlHandler.getDataI(player.getUniqueId().toString(), "can_chat", "player_uuid");
		
		if(!canchat)
		{
			String time = "";
			if((Long) mysqlHandler.getDataI(player.getUniqueId().toString(), "mutetime", "player_uuid")==0)
			{
				time = "permanent";
			} else
			{
				long a = ((Long) mysqlHandler.getDataI(player.getUniqueId().toString(), "mutetime", "player_uuid")
						-System.currentTimeMillis())/(1000*60);
				time = String.valueOf(a)+" min";
			}
			///Du bist für &c%time% gemutet!
			player.spigot().sendMessage(utility.tctl(yamlHandler.getL().getString(language+".EventChat.Muted")
					.replace("%time%", time)));
			return;
		}
		
		
		if(channel.equalsIgnoreCase("Local")) //----------------------------------------------------------Local Channel
		{			
			if(!utility.hasChannelRights(player, "channel_local"))
			{
				return;
			}
			
			if(event.getMessage().length()>=symbol.length() 
					&& utility.getWordfilter(event.getMessage().substring(
					symbol.length()))) //Wordfilter
			{
				///Einer deiner geschriebenen Wörter &cist im Wortfilter enthalten, &cbitte unterlasse sowelche Ausdrücke!
				player.spigot().sendMessage(utility.tctlYaml(language+".EventChat.Wordfilter"));
				return;
			}
			
			if(event.getMessage().substring(1).length()<0)
			{
				///Die Nachricht ist nicht lang genug!
				player.spigot().sendMessage(utility.tctlYaml(language+".EventChat.MessageToShort"));
				return;
			}
			int blockDistance;
			if(yamlHandler.get().get("GetLocalRadius")==null)
			{
				blockDistance = 50;
			} else
			{
				blockDistance = yamlHandler.get().getInt("GetLocalRadius");
			}
			Location pyloc = player.getLocation();
			
			TextComponent MSG = null;
			if(timeofdays == true) {MSG = utility.tc(timeofdaysoutput);}
			else {MSG = utility.tc("");}
			
			MSG.setExtra(utility.getAllTextComponentForChannels(
					player, event.getMessage(), "Local", symbol, symbol.length()));
			
			SimpleChatChannels.log.info(MSG.toLegacyText()); //Console
			
			utility.spy(MSG);
			
			utility.saveAfkTimes(player);
			
			for(Player t : Bukkit.getOnlinePlayers())
			{
				World tw = t.getWorld();
				Location tl = t.getLocation();
				if(tw.getName().equals(pyloc.getWorld().getName()))
				{
					if(tl.distance(pyloc) <= blockDistance) 
					{
						if((boolean) mysqlHandler.getDataI(t.getUniqueId().toString(), "channel_local", "player_uuid"))
						{
							if(!utility.getIgnored(t,player, false))
							{
								t.spigot().sendMessage(MSG);
							}
						}
					}
				}
			}
			return;
		} else if(channel.equalsIgnoreCase("World")) //----------------------------------------------------------World Channel
		{
			if(!utility.hasChannelRights(player, "channel_world"))
			{
				return;
			}
			
			if(event.getMessage().length()>=symbol.length() 
					&& utility.getWordfilter(event.getMessage().substring(symbol.length()))) //Wordfilter
			{
				///Einer deiner geschriebenen Woerter &cist im Wortfilter enthalten, &cbitte unterlasse sowelche Ausdrücke!
				player.spigot().sendMessage(utility.tctlYaml(language+".EventChat.Wordfilter"));
				return;
			}
			
			if(event.getMessage().substring(1).length()<0)
			{
				///Die Nachricht ist nicht lang genug!
				player.spigot().sendMessage(utility.tctlYaml(language+".EventChat.MessageToShort"));
				return;
			}
			
			TextComponent MSG = null;
			if(timeofdays == true) {MSG = utility.tc(timeofdaysoutput);}
			else {MSG = utility.tc("");}
			
			MSG.setExtra(utility.getAllTextComponentForChannels(
					player, event.getMessage(), "World", yamlHandler.getSymbol("World"),
					yamlHandler.getSymbol("World").length()));
			
			SimpleChatChannels.log.info(MSG.toLegacyText()); //Console
			
			utility.spy(MSG);
			
			utility.saveAfkTimes(player);
			
			for(Player t : Bukkit.getOnlinePlayers())
			{
				World tw = t.getWorld();
				if(tw.getName().equals(player.getWorld().getName()))
				{
					if((boolean) mysqlHandler.getDataI(t.getUniqueId().toString(), "channel_world", "player_uuid"))
					{
						if(!utility.getIgnored(t,player, false))
						{
							t.spigot().sendMessage(MSG);
						}
					}
				}
			}
			
			return;
		} else if(channel.equalsIgnoreCase("Global") || channel.equalsIgnoreCase("Trade") || channel.equalsIgnoreCase("Support") 
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
				player.spigot().sendMessage(utility.tctlYaml(language+".EventChat.Wordfilter"));
				return;
			}
			
			TextComponent MSG = null;
			if(timeofdays == true) {MSG = utility.tc(timeofdaysoutput);}
			else {MSG = utility.tc("");}
			
			MSG.setExtra(utility.getAllTextComponentForChannels(
					player, event.getMessage(), channel, symbol, symbol.length()));
			
			SimpleChatChannels.log.info(MSG.toLegacyText()); //Console
			
			if(event.getMessage().substring(symbol.length()).length()<0)
			{
				///Die Nachricht ist nicht lang genug!
				player.spigot().sendMessage(utility.tctlYaml(language+".EventChat.MessageToShort"));
				return;
			}
			
			plugin.getUtility().saveAfkTimes(player);
			
			utility.sendAllMessage(player, "channel_"+channel, MSG, false);
			return;
		} else if(channel.equals("Temp")) //----------------------------------------------------------Temporärer Channel
		{
			if(!utility.hasChannelRights(player, "channel_temp"))
			{
				return;
			}
			
			if(TemporaryChannel.getCustomChannel(player)==null)
			{
				///Du bist in keinem CustomChannel!
				player.spigot().sendMessage(utility.tctlYaml(language+".CmdScc.ChannelGeneral.NotInAChannel"));
				return;
			}
			
			TemporaryChannel cc = TemporaryChannel.getCustomChannel(player);
			
			if(event.getMessage().length()>=symbol.length() 
					&& utility.getWordfilter(event.getMessage().substring(symbol.length()))) //Wordfilter
			{
				///Einer deiner geschriebenen Wörter &cist im Wortfilter enthalten, &cbitte unterlasse sowelche Ausdrücke!
				player.spigot().sendMessage(utility.tctlYaml(language+".EventChat.Wordfilter"));
				return;
			}
			
			TextComponent channels = utility.apichat(yamlHandler.getL().getString(language+".Channels.Perma")
					.replace("%channel%", cc.getName()), 
					ClickEvent.Action.SUGGEST_COMMAND, symbol+" ", 
					HoverEvent.Action.SHOW_TEXT, yamlHandler.getL().getString(language+".ChannelExtra.Hover.Perma"), false);
			
			List<BaseComponent> prefix = utility.getPrefix(player);
			
			TextComponent playertext = utility.apichat(yamlHandler.getL().getString(language+".PlayerColor")+player.getName(), 
					ClickEvent.Action.SUGGEST_COMMAND, yamlHandler.getSymbol("PrivateMessage")+player.getName()+" ", 
					HoverEvent.Action.SHOW_TEXT, yamlHandler.getL().getString(language+".ChannelExtra.Hover.PrivateMessage")
					.replace("%player%", player.getName()), false);
			
			List<BaseComponent> suffix = utility.getSuffix(player);
			
			List<BaseComponent> msg = utility.msgLater(player, symbol.length(), channel, event.getMessage());
			
			TextComponent MSG = null;
			if(timeofdays == true) {MSG = utility.tc(timeofdaysoutput);}
			else {MSG = utility.tc("");}
			
			MSG.setExtra(utility.getTCinLine(channels, prefix, playertext, suffix, msg));
			
			SimpleChatChannels.log.info(MSG.toLegacyText()); //Console
			
			if(event.getMessage().substring(symbol.length()).length()<0)
			{
				///Die Nachricht ist nicht lang genug!
				player.spigot().sendMessage(utility.tctlYaml(language+".EventChat.MessageToShort"));
				return;
			}
			utility.spy(MSG);
			
			plugin.getUtility().saveAfkTimes(player);
			
			for(Player members : cc.getMembers())
			{
				if((boolean) mysqlHandler.getDataI(members.getUniqueId().toString(), "channel_temp", "player_uuid"))
				{
					if(!utility.getIgnored(members,player, false))
					{
						members.spigot().sendMessage(MSG);
					}
				}
			}
			return;
		} else if(channel.equals("Perma")) //----------------------------------------------------------Permanenter Channel
		{
			if(!utility.hasChannelRights(player, "channel_perma"))
			{
				return;
			}
			
			String[] space = event.getMessage().split(" ");
			PermanentChannel cc = PermanentChannel.getChannelFromSymbol(space[0].substring(symbol.length()));
			if(cc==null)
			{
				///Der permanente Channel %symbol% existiert nicht.
				player.spigot().sendMessage(utility.tctlYaml(language+".PCSymbol.ChannelUnknow"));
				return;
			}
			symbol = symbol+cc.getSymbolExtra();
			
			if(event.getMessage().length()>=symbol.length() 
					&& utility.getWordfilter(event.getMessage().substring(symbol.length()))) //Wordfilter
			{
				///Einer deiner geschriebenen Wörter &cist im Wortfilter enthalten, &cbitte unterlasse sowelche Ausdrücke!
				player.spigot().sendMessage(utility.tctlYaml(language+".EventChat.Wordfilter"));
				return;
			}
			
			TextComponent channels = utility.apichat(yamlHandler.getL().getString(language+".Channels.Perma")
					.replace("%channel%", cc.getNameColor()+cc.getName()), 
					ClickEvent.Action.SUGGEST_COMMAND, symbol+" ", 
					HoverEvent.Action.SHOW_TEXT, yamlHandler.getL().getString(language+".ChannelExtra.Hover.Perma"), false);
			
			List<BaseComponent> prefix = utility.getPrefix(player);
			
			TextComponent playertext = utility.apichat(yamlHandler.getL().getString(language+".PlayerColor")+player.getName(), 
					ClickEvent.Action.SUGGEST_COMMAND, yamlHandler.getSymbol("PrivateMessage")+player.getName()+" ", 
					HoverEvent.Action.SHOW_TEXT, yamlHandler.getL().getString(language+".ChannelExtra.Hover.PrivateMessage")
					.replace("%player%", player.getName()), false);
			
			List<BaseComponent> suffix = utility.getSuffix(player);
			
			List<BaseComponent> msg = utility.msgPerma(player, symbol.length()+1, channel, event.getMessage(), cc.getChatColor());
			
			TextComponent MSG = null;
			if(timeofdays == true) {MSG = utility.tc(timeofdaysoutput);}
			else {MSG = utility.tc("");}
			
			MSG.setExtra(utility.getTCinLine(channels, prefix, playertext, suffix, msg));
			
			SimpleChatChannels.log.info(MSG.toLegacyText()); //Console
			
			if(event.getMessage().substring(symbol.length()).length()<0)
			{
				///Die Nachricht ist nicht lang genug!
				player.spigot().sendMessage(utility.tctlYaml(language+".EventChat.MessageToShort"));
				return;
			}
			utility.spy(MSG);
			
			plugin.getUtility().saveAfkTimes(player);
			
			for(Player all : plugin.getServer().getOnlinePlayers())
			{
				if(cc.getMembers().contains(all.getUniqueId().toString()))
				{
					if((boolean) mysqlHandler.getDataI(all.getUniqueId().toString(), "channel_perma", "player_uuid"))
					{
						if(!utility.getIgnored(all,player, false))
						{
							all.spigot().sendMessage(MSG);
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
				player.spigot().sendMessage(utility.tctlYaml(language+".EventChat.Wordfilter"));
				return;
			}
			
			String preorsuffix = eventmsg[0].substring(symbol.length());
			String pors = preorsuffix;
			
			String ps = utility.getPreOrSuffix(preorsuffix);
			
			if(ps.equals("scc.no_prefix_suffix"))
			{
				///Warnung, die Prefixe oder Suffixe dürfen keine Leerzeichen &cbeinhalten!
				player.spigot().sendMessage(utility.tctlYaml(language+".EventChat.NoSpace"));
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
			
			SimpleChatChannels.log.info(MSG.toLegacyText()); //Console
			
			if(event.getMessage().substring(lenghteventmsg).length()<0)
			{
				///Die Nachricht ist nicht lang genug!
				player.spigot().sendMessage(utility.tctlYaml(language+".EventChat.MessageToShort"));
				return;
			}
			
			player.spigot().sendMessage(MSG);
			
			plugin.getUtility().saveAfkTimes(player);
			
			for(Player all : plugin.getServer().getOnlinePlayers())
			{
				if((boolean) mysqlHandler.getDataI(all.getUniqueId().toString(), "channel_group", "player_uuid"))
				{
					if(!all.equals(player))
					{
						if(!utility.getIgnored(all,player, false))
						{
							if(all.hasPermission(ps))
							{
								all.spigot().sendMessage(MSG);
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
				player.spigot().sendMessage(utility.tctlYaml(language+".EventChat.NoRePlayer"));
				return;
			}
			String target = reply.get(pl);
			if(plugin.getServer().getPlayer(UUID.fromString(target)) == null)
			{
				///Der Spieler ist nicht online oder existiert nicht!
				player.spigot().sendMessage(utility.tctlYaml(language+".CmdScc.NoPlayerExist"));
				return;
			}
			Player tr = plugin.getServer().getPlayer(UUID.fromString(target));
			
			utility.isAfk(player, tr);
			
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
				player.spigot().sendMessage(utility.tctlYaml(language+".EventChat.Wordfilter"));
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
			
			SimpleChatChannels.log.info(MSG1.toLegacyText()); //Console
			
			if(event.getMessage().substring(symbol.length()).length()<0)
			{
				///Die Nachricht ist nicht lang genug!
				player.spigot().sendMessage(utility.tctlYaml(language+".EventChat.MessageToShort"));
				return;
			}
			
			if(!(boolean) mysqlHandler.getDataI(tr.getUniqueId().toString(), "channel_pm", "player_uuid"))
			{
				///Der Spieler hat private Nachrichten &cdeaktiviert!
				player.spigot().sendMessage(utility.tctlYaml(language+".EventChat.PlayerNoPrivateMessage"));
				if(!player.hasPermission(Utility.PERMBYPASSPRIVACY))
				{
					player.spigot().sendMessage(MSG2);
					utility.spy(MSG1);
					reply.put(pl, trl);
					return;
				}
			}
			
			plugin.getUtility().saveAfkTimes(player);
			
			if(utility.getIgnored(tr, player, true))
			{
				///Der Spieler ignoriert dich!
				player.spigot().sendMessage(utility.tctlYaml(language+".EventChat.PlayerIgnoreYou"));
				utility.spy(MSG1);
				player.spigot().sendMessage(MSG2);
				reply.put(pl, trl);
				return;
			}
			
			tr.spigot().sendMessage(MSG1);
			
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
			if(plugin.getServer().getPlayer(target) == null)
			{
				///Der Spieler ist nicht online oder existiert nicht!
				player.spigot().sendMessage(utility.tctlYaml(language+".CmdScc.NoPlayerExist"));
				return;
			}
			Player tr = plugin.getServer().getPlayer(target);
			String trl = tr.getUniqueId().toString();
			
			utility.isAfk(player, tr);
			
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
			
			if(event.getMessage().substring(targets[0].length()+symbol.length()).length()<0)
			{
				///Die Nachricht ist nicht lang genug!
				player.spigot().sendMessage(utility.tctlYaml(language+".EventChat.MessageToShort"));
				return;
			}
			if(event.getMessage().length()>=symbol.length() 
					&& utility.getWordfilter(event.getMessage().substring(symbol.length()))) //Wordfilter
			{
				///Einer deiner geschriebenen Woerter &cist im Wortfilter enthalten, &cbitte unterlasse sowelche Ausdrücke!
				player.spigot().sendMessage(utility.tctlYaml(language+".EventChat.Wordfilter"));
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
			
			SimpleChatChannels.log.info(MSG1.toLegacyText()); //Console
			
			if(!(boolean) mysqlHandler.getDataI(tr.getUniqueId().toString(), "channel_pm", "player_uuid"))
			{
				///Der Spieler hat private Nachrichten &cdeaktiviert!
				player.spigot().sendMessage(utility.tctlYaml(language+".EventChat.PlayerNoPrivateMessage"));
				if(!player.hasPermission(Utility.PERMBYPASSPRIVACY))
				{
					player.spigot().sendMessage(MSG2);
					utility.spy(MSG1);
					reply.put(pl, trl);
					return;
				}
			}	
			
			plugin.getUtility().saveAfkTimes(player);
			
			if(utility.getIgnored(tr, player, true))
			{
				///Der Spieler ignoriert dich!
				player.spigot().sendMessage(utility.tctlYaml(language+".EventChat.PlayerIgnoreYou"));
				utility.spy(MSG1);
				player.spigot().sendMessage(MSG2);
				return;
			}
			
			tr.spigot().sendMessage(MSG1);
			player.spigot().sendMessage(MSG2);
			utility.spy(MSG1);
			reply.put(pl, trl);
			reply.put(trl, pl);
			return;
		}
	}
}
