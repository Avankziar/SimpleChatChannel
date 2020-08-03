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

import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.ChatUser;
import main.java.me.avankziar.simplechatchannels.objects.PermanentChannel;
import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.assistance.Utility;
import main.java.me.avankziar.simplechatchannels.spigot.database.MysqlHandler;
import main.java.me.avankziar.simplechatchannels.spigot.database.YamlHandler;
import main.java.me.avankziar.simplechatchannels.spigot.objects.TemporaryChannel;
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
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("Punisher.Jail")));
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
				if(Bukkit.getScheduler().callSyncMethod(plugin, () -> utility.getTarget(event.getPlayer())).get())
				{
					return;
				}
			}
		}
		
		String channelwithoutsymbol = yamlHandler.getL().getString("ChannelSymbol.WithoutSymbol");
		String channel = yamlHandler.getChannel(channelwithoutsymbol, event.getMessage());
		String symbol = yamlHandler.getSymbol(channel);
		
		boolean timeofdays = yamlHandler.get().getBoolean("AddingTimeOfDays", false);
		String timeofdaysformat = utility.getDate(yamlHandler.get().getString("TimeOfDaysFormat"));
		String timeofdaysoutput = ChatApi.tl(yamlHandler.get().getString("TimeOfDaysOutput")
				.replace("%time%", timeofdaysformat));
		
		event.setCancelled(true);	
		String pl = player.getUniqueId().toString();
		ChatUser cu = ChatUser.getChatUser(player.getUniqueId());
		boolean bungee = plugin.getYamlHandler().get().getBoolean("Bungee", false);
		if(bungee)
		{
			cu = (ChatUser) plugin.getMysqlHandler().getData(MysqlHandler.Type.CHATUSER, 
					"`player_uuid` = ?", player.getUniqueId().toString());
		}
		if(!cu.isCanChat())
		{
			long millitime = cu.getMuteTime();
			String time = "";
			if(millitime==0)
			{
				time = "permanent";
				///Du bist für %time% gemutet!
				player.spigot().sendMessage(ChatApi.tctl(yamlHandler.getL().getString("EventChat.Muted")
						.replace("%time%", time)));
				return;
			} else
			{
				if(millitime > System.currentTimeMillis())
				{
					millitime = (millitime-System.currentTimeMillis())/(1000*60);
					time = String.valueOf(millitime)+" min";
					///Du bist für %time% gemutet!
					player.spigot().sendMessage(ChatApi.tctl(yamlHandler.getL().getString("EventChat.Muted")
							.replace("%time%", time)));
					return;
				}
				cu.setCanChat(true);
				cu.setMuteTime(0);
				plugin.getMysqlHandler().updateData(MysqlHandler.Type.CHATUSER, cu,
						"`player_uuid` = ?", player.getUniqueId().toString());
				ChatUser.addChatUser(cu);
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdScc.Mute.Unmute")));
			}
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
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.Wordfilter")));
				return;
			}
			
			if(event.getMessage().substring(symbol.length()).length()<0)
			{
				///Die Nachricht ist nicht lang genug!
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.MessageToShort")));
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
			
			TextComponent MSG = ChatApi.tc("");
			
			MSG.setExtra(utility.getAllTextComponentForChannels(
					player, event.getMessage(), "Local", symbol, symbol.length(), timeofdays, timeofdaysoutput));
			
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
						ChatUser cut = ChatUser.getChatUser(t.getUniqueId());
						if(cut != null)
						{
							if(bungee)
							{
								cut = (ChatUser) plugin.getMysqlHandler().getData(MysqlHandler.Type.CHATUSER, 
										"`player_uuid` = ?", t.getUniqueId().toString());
							}
							if(cut.isChannelLocal())
							{
								if(!utility.getIgnored(t,player, false))
								{
									t.spigot().sendMessage(MSG);
								}
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
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.Wordfilter")));
				return;
			}
			
			if(event.getMessage().substring(1).length()<0)
			{
				///Die Nachricht ist nicht lang genug!
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.MessageToShort")));
				return;
			}
			
			TextComponent MSG = null;
			if(timeofdays == true) {MSG = ChatApi.tc(timeofdaysoutput);}
			else {MSG = ChatApi.tc("");}
			
			MSG.setExtra(utility.getAllTextComponentForChannels(
					player, event.getMessage(), "World", yamlHandler.getSymbol("World"),
					yamlHandler.getSymbol("World").length(), timeofdays, timeofdaysoutput));
			
			SimpleChatChannels.log.info(MSG.toLegacyText()); //Console
			
			utility.spy(MSG);
			
			utility.saveAfkTimes(player);
			
			for(Player t : Bukkit.getOnlinePlayers())
			{
				World tw = t.getWorld();
				if(tw.getName().equals(player.getWorld().getName()))
				{
					ChatUser cut = ChatUser.getChatUser(t.getUniqueId());
					if(cut != null)
					{
						if(bungee)
						{
							cut = (ChatUser) plugin.getMysqlHandler().getData(MysqlHandler.Type.CHATUSER, 
									"`player_uuid` = ?", t.getUniqueId().toString());
						}
						if(cut.isChannelWorld())
						{
							if(!utility.getIgnored(t, player, false))
							{
								t.spigot().sendMessage(MSG);
							}
						}
					}
				}
			}
			
			return;
		} else if(channel.equalsIgnoreCase("Global") || channel.equalsIgnoreCase("Trade") || channel.equalsIgnoreCase("Support") 
				|| channel.equalsIgnoreCase("Auction") || channel.equalsIgnoreCase("Team") || channel.equalsIgnoreCase("Admin")
				|| channel.equalsIgnoreCase("Event")) 
			//----------------------------------------------------------Trade Channel
		{
			if(!utility.hasChannelRights(player, "channel_"+channel))
			{
				return;
			}	
			
			if(utility.getWordfilter(event.getMessage().substring(symbol.length())))
			{
				///Einer deiner geschriebenen Woerter &cist im Wortfilter enthalten, &cbitte unterlasse sowelche Ausdrücke!
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.Wordfilter")));
				return;
			}
			
			TextComponent MSG = null;
			if(timeofdays == true) {MSG = ChatApi.tc(timeofdaysoutput);}
			else {MSG = ChatApi.tc("");}
			
			MSG.setExtra(utility.getAllTextComponentForChannels(
					player, event.getMessage(), channel, symbol, symbol.length(), timeofdays, timeofdaysoutput));
			
			SimpleChatChannels.log.info(MSG.toLegacyText()); //Console
			
			if(event.getMessage().substring(symbol.length()).length()<0)
			{
				///Die Nachricht ist nicht lang genug!
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.MessageToShort")));
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
				player.spigot().sendMessage(
						ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdScc.ChannelGeneral.NotInAChannel")));
				return;
			}
			
			TemporaryChannel cc = TemporaryChannel.getCustomChannel(player);
			
			if(event.getMessage().length()>=symbol.length() 
					&& utility.getWordfilter(event.getMessage().substring(symbol.length()))) //Wordfilter
			{
				///Einer deiner geschriebenen Wörter &cist im Wortfilter enthalten, &cbitte unterlasse sowelche Ausdrücke!
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.Wordfilter")));
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
			
			SimpleChatChannels.log.info(MSG.toLegacyText()); //Console
			
			if(event.getMessage().substring(symbol.length()).length()<0)
			{
				///Die Nachricht ist nicht lang genug!
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.MessageToShort")));
				return;
			}
			utility.spy(MSG);
			
			plugin.getUtility().saveAfkTimes(player);
			
			for(Player members : cc.getMembers())
			{
				ChatUser cume = ChatUser.getChatUser(members.getUniqueId());
				if(cume != null)
				{
					if(bungee)
					{
						cume = (ChatUser) plugin.getMysqlHandler().getData(MysqlHandler.Type.CHATUSER, 
								"`player_uuid` = ?", members.getUniqueId().toString());
					}
					if(cume.isChannelTemporary())
					{
						if(!utility.getIgnored(members,player, false))
						{
							members.spigot().sendMessage(MSG);
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
			PermanentChannel cc = PermanentChannel.getChannelFromSymbol(space[0].substring(symbol.length()));
			if(cc==null)
			{
				///Der permanente Channel %symbol% existiert nicht.
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("PCSymbol.ChannelUnknow")));
				return;
			}
			
			if(!cc.getMembers().contains(player.getUniqueId().toString()))
			{
				///Du bist in keinem Permanenten Channel!
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("ChannelGeneral.NotInAChannelII")));
				return;
			}
			
			symbol = symbol+cc.getSymbolExtra();
			
			if(event.getMessage().length()>=symbol.length() 
					&& utility.getWordfilter(event.getMessage().substring(symbol.length()))) //Wordfilter
			{
				///Einer deiner geschriebenen Wörter &cist im Wortfilter enthalten, &cbitte unterlasse sowelche Ausdrücke!
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.Wordfilter")));
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
			
			SimpleChatChannels.log.info(MSG.toLegacyText()); //Console
			
			if(event.getMessage().substring(symbol.length()).length()<0)
			{
				///Die Nachricht ist nicht lang genug!
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.MessageToShort")));
				return;
			}
			utility.spy(MSG);
			
			plugin.getUtility().saveAfkTimes(player);
			
			for(Player all : plugin.getServer().getOnlinePlayers())
			{
				if(cc.getMembers().contains(all.getUniqueId().toString()))
				{
					ChatUser allcu = ChatUser.getChatUser(player.getUniqueId());
					if(allcu != null)
					{
						if(bungee)
						{
							allcu = (ChatUser) plugin.getMysqlHandler().getData(MysqlHandler.Type.CHATUSER, 
									"`player_uuid` = ?", all.getUniqueId().toString());
						}
						if(allcu.isChannelPermanent())
						{
							if(!utility.getIgnored(all,player, false))
							{
								all.spigot().sendMessage(MSG);
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
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.Wordfilter")));
				return;
			}
			
			String preorsuffix = eventmsg[0].substring(symbol.length());
			String pors = preorsuffix;
			
			String ps = utility.getPreOrSuffix(preorsuffix);
			
			if(ps.equals("scc.no_prefix_suffix"))
			{
				///Warnung, die Prefixe oder Suffixe dürfen keine Leerzeichen &cbeinhalten!
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.NoSpace")));
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
			
			SimpleChatChannels.log.info(MSG.toLegacyText()); //Console
			
			if(event.getMessage().substring(lenghteventmsg).length()<0)
			{
				///Die Nachricht ist nicht lang genug!
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.MessageToShort")));
				return;
			}
			
			player.spigot().sendMessage(MSG);
			
			plugin.getUtility().saveAfkTimes(player);
			
			for(Player all : plugin.getServer().getOnlinePlayers())
			{
				if(!all.getUniqueId().toString().equals(player.getUniqueId().toString()))
				{
					ChatUser allcu = ChatUser.getChatUser(all.getUniqueId());
					if(allcu != null)
					{
						if(bungee)
						{
							allcu = (ChatUser) plugin.getMysqlHandler().getData(MysqlHandler.Type.CHATUSER, 
									"`player_uuid` = ?", all.getUniqueId().toString());
						}
						if(allcu.isChannelGroup())
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
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.NoRePlayer")));
				return;
			}
			String target = reply.get(pl);
			if(plugin.getServer().getPlayer(UUID.fromString(target)) == null)
			{
				///Der Spieler ist nicht online oder existiert nicht!
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdScc.NoPlayerExist")));
				return;
			}
			Player tr = plugin.getServer().getPlayer(UUID.fromString(target));
			
			utility.isAfk(player, tr);
			
			String trl = tr.getUniqueId().toString();
			
			TextComponent channel1 = ChatApi.apiChat(plugin.getYamlHandler().getL().getString("Channels.PrivateMessage"), 
					ClickEvent.Action.SUGGEST_COMMAND, yamlHandler.getSymbol("PrivateMessage")+player.getName()+" ", 
					HoverEvent.Action.SHOW_TEXT, yamlHandler.getL().getString("ChannelExtra.Hover.PrivateMessage")
					.replace("%player%", player.getName()));
			
			TextComponent channel2 = ChatApi.apiChat(plugin.getYamlHandler().getL().getString("Channels.PrivateMessage"), 
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
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.Wordfilter")));
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
			
			SimpleChatChannels.log.info(MSG1.toLegacyText()); //Console
			
			if(event.getMessage().substring(symbol.length()).length()<0)
			{
				///Die Nachricht ist nicht lang genug!
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.MessageToShort")));
				return;
			}
			
			ChatUser cut = ChatUser.getChatUser(tr.getUniqueId());
			if(cut != null)
			{
				if(!cut.isChannelPrivateMessage())
				{
					///Der Spieler hat private Nachrichten &cdeaktiviert!
					player.spigot().sendMessage(
							ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.PlayerNoPrivateMessage")));
					if(!player.hasPermission(Utility.PERMBYPASSPRIVACY))
					{
						player.spigot().sendMessage(MSG2);
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
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.PlayerIgnoreYou")));
				utility.spy(MSG1);
				player.spigot().sendMessage(MSG2);
				reply.put(pl, trl);
				return;
			}
			
			tr.spigot().sendMessage(MSG1);
			player.spigot().sendMessage(MSG2);
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
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdScc.NoPlayerExist")));
				return;
			}
			Player tr = plugin.getServer().getPlayer(target);
			String trl = tr.getUniqueId().toString();
			
			utility.isAfk(player, tr);
			
			TextComponent channel1 = ChatApi.apiChat(plugin.getYamlHandler().getL().getString("Channels.PrivateMessage"), 
					ClickEvent.Action.SUGGEST_COMMAND, yamlHandler.getSymbol("PrivateMessage")+player.getName()+" ", 
					HoverEvent.Action.SHOW_TEXT, yamlHandler.getL().getString("ChannelExtra.Hover.PrivateMessage")
					.replace("%player%", player.getName()));
			
			TextComponent channel2 = ChatApi.apiChat(plugin.getYamlHandler().getL().getString("Channels.PrivateMessage"), 
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
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.MessageToShort")));
				return;
			}
			if(event.getMessage().length()>=symbol.length() 
					&& utility.getWordfilter(event.getMessage().substring(symbol.length()))) //Wordfilter
			{
				///Einer deiner geschriebenen Woerter &cist im Wortfilter enthalten, &cbitte unterlasse sowelche Ausdrücke!
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.Wordfilter")));
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
			
			SimpleChatChannels.log.info(MSG1.toLegacyText()); //Console
			
			ChatUser cut = ChatUser.getChatUser(tr.getUniqueId());
			if(cut != null)
			{
				if(!cut.isChannelPrivateMessage())
				{
					///Der Spieler hat private Nachrichten &cdeaktiviert!
					player.spigot().sendMessage(
							ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.PlayerNoPrivateMessage")));
					if(!player.hasPermission(Utility.PERMBYPASSPRIVACY))
					{
						player.spigot().sendMessage(MSG2);
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
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.PlayerIgnoreYou")));
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
