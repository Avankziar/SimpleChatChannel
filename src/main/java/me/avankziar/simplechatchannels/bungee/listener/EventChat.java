package main.java.me.avankziar.simplechatchannels.bungee.listener;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.assistance.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.database.MysqlHandler;
import main.java.me.avankziar.simplechatchannels.bungee.database.YamlHandler;
import main.java.me.avankziar.simplechatchannels.bungee.objects.ChatUserHandler;
import main.java.me.avankziar.simplechatchannels.bungee.objects.TemporaryChannel;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.ChatUser;
import main.java.me.avankziar.simplechatchannels.objects.PermanentChannel;
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
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("Punisher.Jail")));
				return;
			}
		}
		
		if(event.getMessage().startsWith("/"))
		{
			return;
		}
		YamlHandler yamlHandler = plugin.getYamlHandler();
		if(event.getMessage().length()==1)
		{
			if(yamlHandler.get().getBoolean("Denizen_Active", false))
			{
				return;
			} else
			{
				event.setCancelled(true);
				return;
			}
		}
		
		String channelwithoutsymbol = yamlHandler.getL().getString("ChannelSymbol.WithoutSymbol");
		String channel = yamlHandler.getChannel(channelwithoutsymbol, event.getMessage());
		String symbol = yamlHandler.getSymbol(channel);
		
		boolean timeofdays = yamlHandler.get().getBoolean("AddingTimeOfDays", false);
		String timeofdaysformat = utility.getDate(yamlHandler.get().getString("TimeOfDaysFormat"));
		String timeofdaysoutput = ChatApi.tl(yamlHandler.get().getString("TimeOfDaysOutput")
				.replace("%time%", timeofdaysformat));
		
		if(channel.equalsIgnoreCase("Local"))
		{
			if(!yamlHandler.get().getBoolean("Is_Scc_On_Spigot", false))
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
			if(!yamlHandler.get().getBoolean("Is_Scc_On_Spigot", false))
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
		ChatUser cu = ChatUserHandler.getChatUser(player.getUniqueId());
		
		boolean canchat = cu.isCanChat();
		if(!canchat)
		{
			long millitime = cu.getMuteTime();
			String time = "";
			if(millitime==0)
			{
				time = "permanent";
				///Du bist für %time% gemutet!
				player.sendMessage(ChatApi.tctl(yamlHandler.getL().getString("EventChat.Muted")
						.replace("%time%", time)));
				return;
			} else
			{
				if(millitime > System.currentTimeMillis())
				{
					millitime = (millitime-System.currentTimeMillis())/(1000*60);
					time = String.valueOf(millitime)+" min";
					///Du bist für %time% gemutet!
					player.sendMessage(ChatApi.tctl(yamlHandler.getL().getString("EventChat.Muted")
							.replace("%time%", time)));
					return;
				}
				cu.setCanChat(true);
				cu.setMuteTime(0);
				plugin.getMysqlHandler().updateData(MysqlHandler.Type.CHATUSER, cu,
						"`player_uuid` = ?", player.getUniqueId().toString());
				ChatUser.addChatUser(cu);
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdScc.Mute.Unmute")));
			}
		}
		
		if(channel.equalsIgnoreCase("Global") || channel.equalsIgnoreCase("Trade") || channel.equalsIgnoreCase("Support") 
				|| channel.equalsIgnoreCase("Auction") || channel.equalsIgnoreCase("Team") || channel.equalsIgnoreCase("Admin")
				|| channel.equalsIgnoreCase("Event")) 
		{
			if(!utility.hasChannelRights(player, "channel_"+channel))
			{
				return;
			}	
			
			if(utility.getWordfilter(event.getMessage().substring(symbol.length())))
			{
				///Einer deiner geschriebenen Woerter &cist im Wortfilter enthalten, &cbitte unterlasse sowelche Ausdrücke!
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.Wordfilter")));
				return;
			}
			
			TextComponent MSG = ChatApi.tc("");
			
			MSG.setExtra(utility.getAllTextComponentForChannels(
					player, event.getMessage(), channel, symbol, symbol.length(), timeofdays, timeofdaysoutput));
			
			plugin.getProxy().getConsole().sendMessage(MSG); //Console
			
			if(event.getMessage().substring(symbol.length()).length()<0)
			{
				///Die Nachricht ist nicht lang genug!
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.MessageToShort")));
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
			
			TemporaryChannel cc = TemporaryChannel.getCustomChannel(player);
			
			if(cc == null)
			{
				///Du bist in keinem CustomChannel!
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdScc.ChannelGeneral.NotInAChannel")));
				return;
			}
			
			if(event.getMessage().length()>=symbol.length() 
					&& utility.getWordfilter(event.getMessage().substring(symbol.length()))) //Wordfilter
			{
				///Einer deiner geschriebenen Wörter &cist im Wortfilter enthalten, &cbitte unterlasse sowelche Ausdrücke!
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.Wordfilter")));
				return;
			}
			
			TextComponent channels = ChatApi.apiChat(yamlHandler.getL().getString("Channels.Perma")
					.replace("%channel%", cc.getName()), 
					ClickEvent.Action.SUGGEST_COMMAND, symbol+" ", 
					HoverEvent.Action.SHOW_TEXT, yamlHandler.getL().getString("ChannelExtra.Hover.Perma"));
			
			List<BaseComponent> prefix = utility.getPrefix(player);
			
			TextComponent playertext = ChatApi.apiChat(yamlHandler.getL().getString("PlayerColor")+player.getName(), 
					ClickEvent.Action.SUGGEST_COMMAND, yamlHandler.getSymbol("PrivateMessage")+player.getName()+" ", 
					HoverEvent.Action.SHOW_TEXT, yamlHandler.getL().getString("ChannelExtra.Hover.PrivateMessage")
					.replace("%player%", player.getName()));
			
			List<BaseComponent> suffix = utility.getSuffix(player);
			
			List<BaseComponent> msg = utility.msgLater(player, symbol.length(), channel, event.getMessage());
			
			TextComponent MSG = null;
			if(timeofdays == true) {MSG = ChatApi.tc(timeofdaysoutput);}
			else {MSG = ChatApi.tc("");}
			
			MSG.setExtra(utility.getTCinLine(channels, prefix, playertext, suffix, msg, timeofdays, ChatApi.tc(timeofdaysoutput)));
			
			plugin.getProxy().getConsole().sendMessage(MSG); //Console
			
			if(event.getMessage().substring(symbol.length()).length()<0)
			{
				///Die Nachricht ist nicht lang genug!
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.MessageToShort")));
				return;
			}
			utility.spy(MSG);
			
			plugin.getUtility().saveAfkTimes(player);
			
			for(ProxiedPlayer members : cc.getMembers())
			{
				ChatUser cume = ChatUserHandler.getChatUser(members.getUniqueId());
				if(cume != null)
				{
					if(cume.isChannelTemporary())
					{
						if(!utility.getIgnored(members,player, false))
						{
							members.sendMessage(MSG);
						}
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
			
			if(space.length < 2)
			{
				//If ".." or ".test" is used without another text
				return;
			}
			
			PermanentChannel cc = null;
			
			//If not empty String is used
			String s = space[0].substring(symbol.length());
			if(s.length() > 0);
			{
				cc = PermanentChannel.getChannelFromSymbol(space[0].substring(symbol.length()));
			}
			
			if(cc == null)
			{
				///Der permanente Channel %symbol% existiert nicht.
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdScc.PCSymbol.ChannelUnknown")));
				return;
			}
			
			if(!cc.getMembers().contains(player.getUniqueId().toString()))
			{
				///Du bist in keinem Permanenten Channel!
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdScc.ChannelGeneral.NotInAChannelII")));
				return;
			}
			
			symbol = symbol+cc.getSymbolExtra();
			
			if(event.getMessage().length()>=symbol.length() 
					&& utility.getWordfilter(event.getMessage().substring(symbol.length()))) //Wordfilter
			{
				///Einer deiner geschriebenen Wörter &cist im Wortfilter enthalten, &cbitte unterlasse sowelche Ausdrücke!
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.Wordfilter")));
				return;
			}
			
			TextComponent channels = ChatApi.apiChat(yamlHandler.getL().getString("Channels.Perma")
					.replace("%channel%", cc.getNameColor()+cc.getName()), 
					ClickEvent.Action.SUGGEST_COMMAND, symbol+" ", 
					HoverEvent.Action.SHOW_TEXT, yamlHandler.getL().getString("ChannelExtra.Hover.Perma"));
			
			List<BaseComponent> prefix = utility.getPrefix(player);
			
			TextComponent playertext = ChatApi.apiChat(yamlHandler.getL().getString("PlayerColor")+player.getName(), 
					ClickEvent.Action.SUGGEST_COMMAND, yamlHandler.getSymbol("PrivateMessage")+player.getName()+" ", 
					HoverEvent.Action.SHOW_TEXT, yamlHandler.getL().getString("ChannelExtra.Hover.PrivateMessage")
					.replace("%player%", player.getName()));
			
			List<BaseComponent> suffix = utility.getSuffix(player);
			
			List<BaseComponent> msg = utility.msgPerma(player, symbol.length()+1, channel, event.getMessage(), cc.getChatColor());
			
			TextComponent MSG = null;
			if(timeofdays == true) {MSG = ChatApi.tc(timeofdaysoutput);}
			else {MSG = ChatApi.tc("");}
			
			MSG.setExtra(utility.getTCinLine(channels, prefix, playertext, suffix, msg, timeofdays, ChatApi.tc(timeofdaysoutput)));
			
			plugin.getProxy().getConsole().sendMessage(MSG); //Console
			
			if(event.getMessage().substring(symbol.length()).length()<0)
			{
				///Die Nachricht ist nicht lang genug!
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.MessageToShort")));
				return;
			}
			utility.spy(MSG);
			
			plugin.getUtility().saveAfkTimes(player);
			
			for(ProxiedPlayer all : plugin.getProxy().getPlayers())
			{
				if(cc.getMembers().contains(all.getUniqueId().toString()))
				{
					ChatUser allcu = ChatUserHandler.getChatUser(player.getUniqueId());
					if(allcu != null)
					{
						if(allcu.isChannelPermanent())
						{
							if(!utility.getIgnored(all,player, false))
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
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.Wordfilter")));
				return;
			}
			
			String preorsuffix = eventmsg[0].substring(symbol.length());
			String pors = preorsuffix;
			
			String ps = utility.getPreOrSuffix(preorsuffix);
			
			if(ps.equals("scc.no_prefix_suffix"))
			{
				///Warnung, die Prefixe oder Suffixe dürfen keine Leerzeichen &cbeinhalten!
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.NoSpace")));
				return;
			}
			
			TextComponent channels = ChatApi.apiChat(yamlHandler.getL().getString("Channels.Group")
					.replace("%group%", preorsuffix), 
					ClickEvent.Action.SUGGEST_COMMAND, symbol+pors+" ", 
					HoverEvent.Action.SHOW_TEXT, yamlHandler.getL().getString("ChannelExtra.Hover.Group"));
			
			List<BaseComponent> prefix = utility.getPrefix(player);
			
			TextComponent playertext = ChatApi.apiChat(yamlHandler.getL().getString("PlayerColor")+player.getName(), 
					ClickEvent.Action.SUGGEST_COMMAND, yamlHandler.getSymbol("PrivateMessage")+player.getName()+" ", 
					HoverEvent.Action.SHOW_TEXT, yamlHandler.getL().getString("ChannelExtra.Hover.PrivateMessage")
					.replace("%player%", player.getName()));
			
			List<BaseComponent> suffix = utility.getSuffix(player);
			
			List<BaseComponent> msg = utility.msgLater(player, lenghteventmsg, channel, event.getMessage());
			
			TextComponent MSG = null;
			if(timeofdays == true) {MSG = ChatApi.tc(timeofdaysoutput);}
			else {MSG = ChatApi.tc("");}
			
			MSG.setExtra(utility.getTCinLine(channels, prefix, playertext, suffix, msg, timeofdays, ChatApi.tc(timeofdaysoutput)));
			
			plugin.getProxy().getConsole().sendMessage(MSG); //Console
			
			if(event.getMessage().substring(lenghteventmsg).length()<0)
			{
				///Die Nachricht ist nicht lang genug!
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.MessageToShort")));
				return;
			}
			
			player.sendMessage(MSG);
			
			plugin.getUtility().saveAfkTimes(player);
			
			for(ProxiedPlayer all : plugin.getProxy().getPlayers())
			{
				if(!all.getUniqueId().toString().equals(player.getUniqueId().toString()))
				{
					ChatUser allcu = ChatUserHandler.getChatUser(all.getUniqueId());
					if(allcu != null)
					{
						if(allcu.isChannelGroup())
						{
							if(!utility.getIgnored(all,player, false))
							{
								if(all.hasPermission(ps))
								{
									all.sendMessage(MSG);
								}
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
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.NoRePlayer")));
				return;
			}
			String target = reply.get(pl);
			if(ProxyServer.getInstance().getPlayer(UUID.fromString(target)) == null)
			{
				///Der Spieler ist nicht online oder existiert nicht!
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("NoPlayerExist")));
				return;
			}
			ProxiedPlayer tr = ProxyServer.getInstance().getPlayer(UUID.fromString(target));
			
			utility.isAfk(player, tr);
			
			String trl = tr.getUniqueId().toString();
			
			TextComponent channel1 = ChatApi.apiChat(yamlHandler.getL().getString("Channels.PrivateMessage"), 
					ClickEvent.Action.SUGGEST_COMMAND, yamlHandler.getSymbol("PrivateMessage")+player.getName()+" ", 
					HoverEvent.Action.SHOW_TEXT, yamlHandler.getL().getString("ChannelExtra.Hover.PrivateMessage")
					.replace("%player%", player.getName()));
			
			TextComponent channel2 = ChatApi.apiChat(yamlHandler.getL().getString("Channels.PrivateMessage"), 
					ClickEvent.Action.SUGGEST_COMMAND, yamlHandler.getSymbol("PrivateMessage")+tr.getName()+" ", 
					HoverEvent.Action.SHOW_TEXT, yamlHandler.getL().getString("ChannelExtra.Hover.PrivateMessage")
					.replace("%player%", tr.getName()));
			
			TextComponent playertext = ChatApi.apiChat(yamlHandler.getL().getString("PlayerColor")+player.getName(), 
					ClickEvent.Action.SUGGEST_COMMAND, yamlHandler.getSymbol("PrivateMessage")+player.getName()+" ", 
					HoverEvent.Action.SHOW_TEXT, yamlHandler.getL().getString("ChannelExtra.Hover.PrivateMessage")
					.replace("%player%", player.getName()));
			
			TextComponent player2text = ChatApi.apiChat(yamlHandler.getL().getString("PlayerToPlayer")+tr.getName(), 
					ClickEvent.Action.SUGGEST_COMMAND, yamlHandler.getSymbol("PrivateMessage")+tr.getName()+" ", 
					HoverEvent.Action.SHOW_TEXT, yamlHandler.getL().getString("ChannelExtra.Hover.PrivateMessage")
					.replace("%player%", player.getName()));
			
			if(event.getMessage().length()>=symbol.length() && utility.getWordfilter(event.getMessage().substring(
					symbol.length())))
			{
				///Einer deiner geschriebenen Woerter &cist im Wortfilter enthalten, &cbitte unterlasse sowelche Ausdrücke!
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.Wordfilter")));
				return;
			}
			
			TextComponent MSG1 = null;
			if(timeofdays == true) {MSG1 = ChatApi.tc(timeofdaysoutput);}
			else {MSG1 = ChatApi.tc("");}
			
			TextComponent MSG2 = null;
			if(timeofdays == true) {MSG2 = ChatApi.tc(timeofdaysoutput);}
			else {MSG2 = ChatApi.tc("");}
			
			MSG1.setExtra(utility.getTCinLinePN(channel1, playertext, player2text, 
					utility.msgLater(player,symbol.length(),"PrivateMessage", event.getMessage())
					, timeofdays, ChatApi.tc(timeofdaysoutput)));
			MSG2.setExtra(utility.getTCinLinePN(channel2, playertext, player2text, 
					utility.msgLater(player,symbol.length(),"PrivateMessage", event.getMessage())
					, timeofdays, ChatApi.tc(timeofdaysoutput)));
			
			plugin.getProxy().getConsole().sendMessage(MSG1); //Console
			
			if(event.getMessage().substring(symbol.length()).length()<0)
			{
				///Die Nachricht ist nicht lang genug!
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.MessageToShort")));
				return;
			}
			
			ChatUser cut = ChatUserHandler.getChatUser(tr.getUniqueId());
			if(cut != null)
			{
				if(!cut.isChannelPrivateMessage())
				{
					///Der Spieler hat private Nachrichten &cdeaktiviert!
					player.sendMessage(
							ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.PlayerNoPrivateMessage")));
					if(!player.hasPermission(Utility.PERMBYPASSPRIVACY))
					{
						player.sendMessage(MSG2);
						utility.spy(MSG1);
						reply.put(pl, trl);
						return;
					}
				}
			}
			
			plugin.getUtility().saveAfkTimes(player);
			
			if(utility.getIgnored(tr, player, true))
			{
				///Der Spieler ignoriert dich!
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.PlayerIgnoreYou")));
				utility.spy(MSG1);
				player.sendMessage(MSG2);
				reply.put(pl, trl);
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
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("NoPlayerExist")));
				return;
			}
			ProxiedPlayer tr = ProxyServer.getInstance().getPlayer(target);
			String trl = tr.getUniqueId().toString();
			
			utility.isAfk(player, tr);
			
			TextComponent channel1 = ChatApi.apiChat(
					yamlHandler.getL().getString("Channels.PrivateMessage"), 
					ClickEvent.Action.SUGGEST_COMMAND, yamlHandler.getSymbol("PrivateMessage")+player.getName()+" ", 
					HoverEvent.Action.SHOW_TEXT, yamlHandler.getL().getString("ChannelExtra.Hover.PrivateMessage")
					.replace("%player%", player.getName()));
			
			TextComponent channel2 = ChatApi.apiChat(
					yamlHandler.getL().getString("Channels.PrivateMessage"), 
					ClickEvent.Action.SUGGEST_COMMAND, yamlHandler.getSymbol("PrivateMessage")+tr.getName()+" ", 
					HoverEvent.Action.SHOW_TEXT, yamlHandler.getL().getString("ChannelExtra.Hover.PrivateMessage")
					.replace("%player%", tr.getName()));
	
			TextComponent playertext = ChatApi.apiChat(yamlHandler.getL().getString("PlayerColor")+player.getName(), 
					ClickEvent.Action.SUGGEST_COMMAND, yamlHandler.getSymbol("PrivateMessage")+player.getName()+" ", 
					HoverEvent.Action.SHOW_TEXT, yamlHandler.getL().getString("ChannelExtra.Hover.PrivateMessage")
					.replace("%player%", player.getName()));
			
			TextComponent player2text = ChatApi.apiChat(yamlHandler.getL().getString("PlayerToPlayer")+tr.getName(), 
					ClickEvent.Action.SUGGEST_COMMAND, yamlHandler.getSymbol("PrivateMessage")+tr.getName()+" ", 
					HoverEvent.Action.SHOW_TEXT, yamlHandler.getL().getString("ChannelExtra.Hover.PrivateMessage")
					.replace("%player%", tr.getName()));
			
			if(event.getMessage().substring(targets[0].length()).length()<0)
			{
				///Die Nachricht ist nicht lang genug!
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.MessageToShort")));
				return;
			}
			if(event.getMessage().length()>=symbol.length() 
					&& utility.getWordfilter(event.getMessage().substring(symbol.length()))) //Wordfilter
			{
				///Einer deiner geschriebenen Woerter &cist im Wortfilter enthalten, &cbitte unterlasse sowelche Ausdrücke!
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.Wordfilter")));
				return;
			}
			
			TextComponent MSG1 = null;
			if(timeofdays == true) {MSG1 = ChatApi.tc(timeofdaysoutput);}
			else {MSG1 = ChatApi.tc("");}
			
			TextComponent MSG2 = null;
			if(timeofdays == true) {MSG2 = ChatApi.tc(timeofdaysoutput);}
			else {MSG2 = ChatApi.tc("");}
			
			MSG1.setExtra(utility.getTCinLinePN(channel1, playertext, player2text, 
					utility.msgLater(player,targets[0].length(),"PrivateMessage", event.getMessage())
					, timeofdays, ChatApi.tc(timeofdaysoutput)));
			MSG2.setExtra(utility.getTCinLinePN(channel2, playertext, player2text, 
					utility.msgLater(player,targets[0].length(),"PrivateMessage", event.getMessage())
					, timeofdays, ChatApi.tc(timeofdaysoutput)));
			
			plugin.getProxy().getConsole().sendMessage(MSG1); //Console
			
			ChatUser cut = ChatUserHandler.getChatUser(tr.getUniqueId());
			if(cut != null)
			{
				if(!cut.isChannelPrivateMessage())
				{
					///Der Spieler hat private Nachrichten &cdeaktiviert!
					player.sendMessage(
							ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.PlayerNoPrivateMessage")));
					if(!player.hasPermission(Utility.PERMBYPASSPRIVACY))
					{
						player.sendMessage(MSG2);
						utility.spy(MSG1);
						reply.put(pl, trl);
						return;
					}
				}
			}	
			
			plugin.getUtility().saveAfkTimes(player);
			
			if(utility.getIgnored(tr, player, true))
			{
				///Der Spieler ignoriert dich!
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.PlayerIgnoreYou")));
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
