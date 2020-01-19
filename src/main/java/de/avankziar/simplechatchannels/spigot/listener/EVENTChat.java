package main.java.de.avankziar.simplechatchannels.spigot.listener;

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

import main.java.de.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.de.avankziar.simplechatchannels.spigot.interfaces.CustomChannel;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

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
	public void onChat(AsyncPlayerChatEvent event) throws InterruptedException, ExecutionException
	{
		Player player = (Player) event.getPlayer();
		
		if(plugin.getPunisher()!=null)
		{
			if(plugin.getPunisher().isPlayerJailed(player))
			{
				event.setCancelled(true);
				plugin.getUtility().sendMessage(player, plugin.getYamlHandler().getL().getString(language+".punisher.msg01"));
				return;
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
				if(Bukkit.getScheduler().callSyncMethod(plugin, () -> plugin.getUtility().getTarget(event.getPlayer())).get())
				{
					return;
				} else
				{
					event.setCancelled(true);
					return;
				}
			}
		}
		event.setCancelled(true);	
		String pl = player.getUniqueId().toString();
		boolean canchat = (boolean) plugin.getMysqlInterface().getDataI(player, "can_chat", "player_uuid");
		if(!canchat)
		{
			String time = "";
			if((Long) plugin.getMysqlInterface().getDataI(player, "mutetime", "player_uuid")==0)
			{
				time = "permanent";
			} else
			{
				long a = ((Long) plugin.getMysqlInterface().getDataI(player, "mutetime", "player_uuid")-System.currentTimeMillis())/(1000*60);
				time = String.valueOf(a)+" min";
			}
			player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg07")
					.replaceAll("%time%", time)));
			return;
		}
		
		if(event.getMessage().startsWith(plugin.getYamlHandler().getSymbol("trade"))) //----------------------------------------------------------Trade Channel
		{
			if(plugin.getYamlHandler().get().getString("bungee").equals("true"))
			{
				return;
			}
			if(!plugin.getUtility().hasChannelRights(player, "channel_trade"))
			{
				return;
			}
			
			if(plugin.getUtility().getWordfilter(event.getMessage().substring(plugin.getYamlHandler().getSymbol("trade").length()))) //Wordfilter
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg06")));
				return;
			}
			
			TextComponent MSG = plugin.getUtility().tc("");
			
			MSG.setExtra(plugin.getUtility().getAllTextComponentForChannels(
					player, event.getMessage(), "trade", plugin.getYamlHandler().getSymbol("trade"),
					plugin.getYamlHandler().getSymbol("trade").length()));
			
			SimpleChatChannels.log.info(MSG.toLegacyText()); //Console
			
			if(event.getMessage().substring(1).length()<0)
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg08")));
				return;
			}
			
			plugin.getUtility().sendAllMessage(player, "channel_trade", MSG);
			return;
		} else if(event.getMessage().startsWith(plugin.getYamlHandler().getSymbol("support"))) //----------------------------------------------------------Support Channel
		{
			if(plugin.getYamlHandler().get().getString("bungee").equals("true"))
			{
				return;
			}
			if(!plugin.getUtility().hasChannelRights(player, "channel_support"))
			{
				return;
			}
			
			if(plugin.getUtility().getWordfilter(event.getMessage().substring(plugin.getYamlHandler().getSymbol("support").length()))) //Wordfilter
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg06")));
				return;
			}
			
			TextComponent MSG = plugin.getUtility().tc("");
			
			MSG.setExtra(plugin.getUtility().getAllTextComponentForChannels(
					player, event.getMessage(), "support", plugin.getYamlHandler().getSymbol("support"),
					plugin.getYamlHandler().getSymbol("support").length()));
			
			SimpleChatChannels.log.info(MSG.toLegacyText()); //Console
			
			if(event.getMessage().substring(1).length()<0)
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg08")));
				return;
			}
			
			plugin.getUtility().sendAllMessage(player, "channel_support", MSG);
			return;
		} else if(event.getMessage().startsWith(plugin.getYamlHandler().getSymbol("auction"))) //----------------------------------------------------------Auction Channel
		{
			if(plugin.getYamlHandler().get().getString("bungee").equals("true"))
			{
				return;
			}
			if(!plugin.getUtility().hasChannelRights(player, "channel_auction"))
			{
				return;
			}
			
			if(plugin.getUtility().getWordfilter(event.getMessage().substring(plugin.getYamlHandler().getSymbol("auction").length()))) //Wordfilter
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg06")));
				return;
			}
			
			TextComponent MSG = plugin.getUtility().tc("");
			
			MSG.setExtra(plugin.getUtility().getAllTextComponentForChannels(
					player, event.getMessage(), "auction", plugin.getYamlHandler().getSymbol("auction"), 
					plugin.getYamlHandler().getSymbol("auction").length()));
			
			SimpleChatChannels.log.info(MSG.toLegacyText()); //Console
			
			if(event.getMessage().substring(1).length()<0)
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg08")));
				return;
			}
			
			plugin.getUtility().sendAllMessage(player, "channel_auction", MSG);
			return;
		} else if(event.getMessage().startsWith(plugin.getYamlHandler().getSymbol("team"))) //-------------------------------------Team Channel
		{
			if(plugin.getYamlHandler().get().getString("bungee").equals("true"))
			{
				return;
			}
			if(!plugin.getUtility().hasChannelRights(player, "channel_team"))
			{
				return;
			}
			
			if(plugin.getUtility().getWordfilter(event.getMessage().substring(plugin.getYamlHandler().getSymbol("team").length()))) //Wordfilter
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg06")));
				return;
			}
			
			TextComponent MSG = plugin.getUtility().tc("");
			
			MSG.setExtra(plugin.getUtility().getAllTextComponentForChannels(
					player, event.getMessage(), "team", plugin.getYamlHandler().getSymbol("team"),
					plugin.getYamlHandler().getSymbol("team").length()));
			
			SimpleChatChannels.log.info(MSG.toLegacyText()); //Console
			
			if(event.getMessage().substring(1).length()<0)
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg08")));
				return;
			}
			
			plugin.getUtility().sendAllMessage(player, "channel_team", MSG);
			return;
		} else if(event.getMessage().startsWith(plugin.getYamlHandler().getSymbol("admin"))) //----------------------------------------------------------Admin Channel
		{
			if(plugin.getYamlHandler().get().getString("bungee").equals("true"))
			{
				return;
			}
			if(!plugin.getUtility().hasChannelRights(player, "channel_admin"))
			{
				return;
			}
			
			if(plugin.getUtility().getWordfilter(event.getMessage().substring(plugin.getYamlHandler().getSymbol("admin").length()))) //Wordfilter
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg06")));
				return;
			}
			
			TextComponent MSG = plugin.getUtility().tc("");
			MSG.setExtra(plugin.getUtility().getAllTextComponentForChannels(
					player, event.getMessage(), "admin", plugin.getYamlHandler().getSymbol("admin"),
					plugin.getYamlHandler().getSymbol("admin").length()));
			
			SimpleChatChannels.log.info(MSG.toLegacyText()); //Console
			
			if(event.getMessage().substring(1).length()<0)
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg08")));
				return;
			}
			
			plugin.getUtility().sendAllMessage(player, "channel_admin", MSG);
			return;
		} else if(event.getMessage().startsWith(plugin.getYamlHandler().getSymbol("local"))) //----------------------------------------------------------Local Channel
		{			
			if(!plugin.getUtility().hasChannelRights(player, "channel_local"))
			{
				return;
			}
			
			if(plugin.getUtility().getWordfilter(event.getMessage().substring(plugin.getYamlHandler().getSymbol("local").length()))) //Wordfilter
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg06")));
				return;
			}
			
			if(event.getMessage().substring(1).length()<0)
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg08")));
				return;
			}
			int blockDistance;
			if(plugin.getYamlHandler().get().get("getlocaleradius")==null)
			{
				blockDistance = 50;
			} else
			{
				blockDistance = plugin.getYamlHandler().get().getInt("getlocaleradius");
			}
			Location pyloc = player.getLocation();
			
			TextComponent MSG = plugin.getUtility().tc("");
			MSG.setExtra(plugin.getUtility().getAllTextComponentForChannels(
					player, event.getMessage(), "local", plugin.getYamlHandler().getSymbol("local"),
					plugin.getYamlHandler().getSymbol("local").length()));
			
			SimpleChatChannels.log.info(MSG.toLegacyText()); //Console
			
			plugin.getUtility().spy(MSG);
			
			for(Player t : Bukkit.getOnlinePlayers())
			{
				World tw = t.getWorld();
				Location tl = t.getLocation();
				if(tw.getName().equals(pyloc.getWorld().getName()))
				{
					if(tl.distance(pyloc) <= blockDistance) 
					{
						if((boolean) plugin.getMysqlInterface().getDataI(player, "channel_local", "player_uuid"))
						{
							if(!plugin.getUtility().getIgnored(player,t))
							{
								t.spigot().sendMessage(MSG);
							}
						}
					}
				}
			}
			return;
		} else if(event.getMessage().startsWith(plugin.getYamlHandler().getSymbol("world"))) //----------------------------------------------------------World Channel
		{
			if(!plugin.getUtility().hasChannelRights(player, "channel_world"))
			{
				return;
			}
			
			if(plugin.getUtility().getWordfilter(event.getMessage().substring(plugin.getYamlHandler().getSymbol("world").length()))) //Wordfilter
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg06")));
				return;
			}
			
			if(event.getMessage().substring(1).length()<0)
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg08")));
				return;
			}
			
			TextComponent MSG = plugin.getUtility().tc("");
			MSG.setExtra(plugin.getUtility().getAllTextComponentForChannels(
					player, event.getMessage(), "world", plugin.getYamlHandler().getSymbol("world"),
					plugin.getYamlHandler().getSymbol("world").length()));
			
			SimpleChatChannels.log.info(MSG.toLegacyText()); //Console
			
			plugin.getUtility().spy(MSG);
			
			for(Player t : Bukkit.getOnlinePlayers())
			{
				World tw = t.getWorld();
				if(tw.getName().equals(player.getWorld().getName()))
				{
					if((boolean) plugin.getMysqlInterface().getDataI(player, "channel_world", "player_uuid"))
					{
						if(!plugin.getUtility().getIgnored(player,t))
						{
							t.spigot().sendMessage(MSG);
						}
					}
				}
			}
			
			return;
		} else if(event.getMessage().startsWith(plugin.getYamlHandler().getSymbol("group"))) //----------------------------------------------------------Gruppe Channel
		{
			if(plugin.getYamlHandler().get().getString("bungee").equals("true"))
			{
				return;
			}
			if(!plugin.getUtility().hasChannelRights(player, "channel_group"))
			{
				return;
			}			
			
			String[] eventmsg = event.getMessage().split(" ");
			int lenghteventmsg = eventmsg[0].length()+1;
			
			if(plugin.getUtility().getWordfilter(event.getMessage().substring(lenghteventmsg))) //Wordfilter
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg06")));
				return;
			}
			
			String preorsuffix = eventmsg[0].substring(2);
			String pors = "";
			if(preorsuffix.startsWith("&"))
			{
				pors = preorsuffix.substring(2);
			} else
			{
				pors = preorsuffix;
			}
			
			String ps = plugin.getUtility().getPreOrSuffix(preorsuffix);
			
			if(ps.equals("scc.no_prefix_suffix"))
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg09")));
				return;
			}
			
			TextComponent channel = plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".channels.group")
					.replaceAll("%group%", preorsuffix));
			channel.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, plugin.getYamlHandler().getSymbol("group")+pors+" "));
			channel.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(
							plugin.getYamlHandler().getL().getString(language+".channelextra.hover.group"))).create()));
			
			List<BaseComponent> prefix = plugin.getUtility().getPrefix(player);
			
			TextComponent playertext = plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".playercolor")+player.getName());
			playertext.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, plugin.getYamlHandler().getSymbol("pm")+player.getName()+" "));
			playertext.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(
							plugin.getYamlHandler().getL().getString(language+".channelextra.hover.message")
							.replaceAll("%player%", player.getName()))).create()));
			
			List<BaseComponent> suffix = plugin.getUtility().getSuffix(player);
			
			TextComponent msg = plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".chatsplit.group")
					+plugin.getUtility().MsgLater(lenghteventmsg,"group", event.getMessage()));
			
			TextComponent MSG = plugin.getUtility().tc("");
			
			MSG.setExtra(plugin.getUtility().getTCinLine(channel, prefix, playertext, suffix, msg));
			
			SimpleChatChannels.log.info(MSG.toLegacyText()); //Console
			
			if(event.getMessage().substring(lenghteventmsg).length()<0)
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg08")));
				return;
			}
			
			player.spigot().sendMessage(MSG);
			
			for(Player all : Bukkit.getOnlinePlayers())
			{
				if(all.hasPermission(ps))
				{ 
					if((boolean) plugin.getMysqlInterface().getDataI(player, "channel_group", "player_uuid"))
					{
						if(!plugin.getUtility().getIgnored(player,all))
						{
							all.spigot().sendMessage(MSG);
						}
					}
				}
			}
			plugin.getUtility().spy(MSG);
			return;
		} else if(event.getMessage().startsWith(plugin.getYamlHandler().getSymbol("pm")+"r ")) //----------------------------------------------------------Reply Message
		{
			if(plugin.getYamlHandler().get().getString("bungee").equals("true"))
			{
				return;
			}
			if(!plugin.getUtility().hasChannelRights(player, "channel_pm"))
			{
				return;
			}
			if(!reply.containsKey(pl))
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg05")));
				return;
			}
			String target = reply.get(pl);
			if(Bukkit.getPlayer(UUID.fromString(target)) == null)
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg02")));
				return;
			}
			Player tr = Bukkit.getPlayer(UUID.fromString(target));
			String trl = tr.getUniqueId().toString();
			
			TextComponent channel1 = plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".channels.message"));
			channel1.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, plugin.getYamlHandler().getSymbol("pm")+player.getName()+" "));
			channel1.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(plugin.getYamlHandler().getL().getString(language+".channelextra.hover.message")
							.replaceAll("%player%", player.getName()))).create()));
			
			TextComponent channel2 = plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".channels.message"));
			channel2.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, plugin.getYamlHandler().getSymbol("pm")+tr.getName()+" "));
			channel2.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(
							plugin.getYamlHandler().getL().getString(language+".channelextra.hover.message")
							.replaceAll("%player%", tr.getName()))).create()));
			
			TextComponent playertext = plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".playercolor")+player.getName());
			playertext.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, plugin.getYamlHandler().getSymbol("pm")+player.getName()+" "));
			playertext.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(plugin.getYamlHandler().getL().getString(language+".channelextra.hover.message")
							.replaceAll("%player%", player.getName()))).create()));
			
			TextComponent player2 = plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".playertoplayer")+tr.getName());
			player2.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, plugin.getYamlHandler().getSymbol("pm")+tr.getName()+" "));
			player2.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(plugin.getYamlHandler().getL().getString(language+".channelextra.hover.message")
							.replaceAll("%player%", player.getName()))).create()));
			
			TextComponent msg = plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".chatsplit.message")
					+plugin.getUtility().MsgLater(3,"message", event.getMessage()));
			
			if(plugin.getUtility().getWordfilter(event.getMessage().substring(plugin.getYamlHandler().getSymbol("pm").length()+2))) //Wordfilter
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg06")));
				return;
			}
			
			TextComponent MSG1 = plugin.getUtility().tc("");
			TextComponent MSG2 = plugin.getUtility().tc("");
			
			MSG1.setExtra(plugin.getUtility().getTCinLinePN(channel1, playertext, player2, msg));
			MSG2.setExtra(plugin.getUtility().getTCinLinePN(channel2, playertext, player2, msg));
			
			SimpleChatChannels.log.info(MSG1.toLegacyText()); //Console
			
			if(event.getMessage().substring(3).length()<0)
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg08")));
				return;
			}
			
			if(!plugin.getUtility().hasChannelRights(tr, "channel_pm"))
			{
				return;
			}
			
			if(plugin.getUtility().getIgnored(player,tr))
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg03")));
				plugin.getUtility().spy(MSG1);
				player.spigot().sendMessage(MSG2);
				return;
			}
			tr.spigot().sendMessage(MSG1);
			player.spigot().sendMessage(MSG2);
			plugin.getUtility().spy(MSG1);
			reply.put(pl, trl);
			reply.put(trl, pl);
			return;
		} else if(event.getMessage().startsWith(plugin.getYamlHandler().getSymbol("pm"))) //----------------------------------------------------------Private Message
		{
			if(plugin.getYamlHandler().get().getString("bungee").equals("true"))
			{
				return;
			}
			if(!plugin.getUtility().hasChannelRights(player, "channel_pm"))
			{
				return;
			}
			
			String[] targets = event.getMessage().split(" ");
			String target = targets[0].substring(plugin.getYamlHandler().getSymbol("pm").length());
			if(Bukkit.getPlayer(target) == null)
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg02")));
				return;
			}
			Player tr = Bukkit.getPlayer(target);
			String trl = tr.getUniqueId().toString();
			
			TextComponent channel1 = plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".channels.message"));
			channel1.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, plugin.getYamlHandler().getSymbol("pm")+player.getName()+" "));
			channel1.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(plugin.getYamlHandler().getL().getString(language+".channelextra.hover.message")
							.replaceAll("%player%", player.getName()))).create()));
			
			TextComponent channel2 = plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".channels.message"));
			channel2.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, plugin.getYamlHandler().getSymbol("pm")+tr.getName()+" "));
			channel2.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(plugin.getYamlHandler().getL().getString(language+".channelextra.hover.message")
							.replaceAll("%player%", tr.getName()))).create()));
			
			TextComponent playertext = plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".playercolor")+player.getName());
			playertext.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, plugin.getYamlHandler().getSymbol("pm")+player.getName()+" "));
			playertext.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(plugin.getYamlHandler().getL().getString(language+".channelextra.hover.message")
							.replaceAll("%player%", player.getName()))).create()));
			
			TextComponent player2 = plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".playertoplayer")+tr.getName());
			player2.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, plugin.getYamlHandler().getSymbol("pm")+tr.getName()+" "));
			player2.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(plugin.getYamlHandler().getL().getString(language+".channelextra.hover.message")
							.replaceAll("%player%", tr.getName()))).create()));
			
			TextComponent msg = plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".chatsplit.message")
					+plugin.getUtility().MsgLater(targets[0].length()+1,"message", event.getMessage()));
			
			if(event.getMessage().substring(targets[0].length()+1).length()<0)
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg08")));
				return;
			}
			if(plugin.getUtility().getWordfilter(event.getMessage().substring(plugin.getYamlHandler().getSymbol("pm").length()))) //Wordfilter
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg06")));
				return;
			}
			
			TextComponent MSG1 = plugin.getUtility().tc("");
			TextComponent MSG2 = plugin.getUtility().tc("");
			
			MSG1.setExtra(plugin.getUtility().getTCinLinePN(channel1, playertext, player2, msg));
			MSG2.setExtra(plugin.getUtility().getTCinLinePN(channel2, playertext, player2, msg));
			
			SimpleChatChannels.log.info(MSG1.toLegacyText()); //Console
			
			if(!plugin.getUtility().hasChannelRights(tr, "channel_pm"))
			{
				return;
			}	
			if(plugin.getUtility().getIgnored(player,tr))
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg03")));
				plugin.getUtility().spy(MSG1);
				player.spigot().sendMessage(MSG2);
				return;
			}
			tr.spigot().sendMessage(MSG1);
			player.spigot().sendMessage(MSG2);
			plugin.getUtility().spy(MSG1);
			reply.put(pl, trl);
			reply.put(trl, pl);
			return;
		} else if(event.getMessage().startsWith(plugin.getYamlHandler().getSymbol("custom"))) //----------------------------------------------------------Support Channel
		{
			if(!plugin.getUtility().hasChannelRights(player, "channel_custom"))
			{
				return;
			}
			
			if(CustomChannel.getCustomChannel(player)==null)
			{
				player.spigot().sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg11"))));
				return;
			}
			
			if(plugin.getUtility().getWordfilter(event.getMessage().substring(plugin.getYamlHandler().getSymbol("custom").length()))) //Wordfilter
			{
				player.spigot().sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg06"))));
				return;
			}
			
			TextComponent MSG = plugin.getUtility().tc("");
			
			MSG.setExtra(plugin.getUtility().getAllTextComponentForChannels(
					player, event.getMessage(), "custom", plugin.getYamlHandler().getSymbol("custom"),
					plugin.getYamlHandler().getSymbol("custom").length()));
			
			SimpleChatChannels.log.info(MSG.toLegacyText()); //Console
			
			if(event.getMessage().substring(1).length()<0)
			{
				player.spigot().sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg08"))));
				return;
			}
			plugin.getUtility().spy(MSG);
			plugin.getUtility().sendAllMessage(player, "channel_custom", MSG);
			return;
		} else //----------------------------------------------------------Global Channel
		{
			if(plugin.getYamlHandler().get().getString("bungee").equals("true"))
			{
				return;
			}
			if(!plugin.getUtility().hasChannelRights(player, "channel_global"))
			{
				return;
			}
			
			if(plugin.getUtility().getWordfilter(event.getMessage().substring(plugin.getYamlHandler().getSymbol("global").length()))) //Wordfilter
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg06")));
				return;
			} 
			
			TextComponent MSG = plugin.getUtility().tc("");
			MSG.setExtra(plugin.getUtility().getAllTextComponentForChannels(
					player, event.getMessage(), "global", plugin.getYamlHandler().getSymbol("global"), 
					plugin.getYamlHandler().getSymbol("global").length()));
			
			SimpleChatChannels.log.info(MSG.toLegacyText()); //Console
			
			if(event.getMessage().substring(0).length()<0)
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg08")));
				return;
			}
			
			plugin.getUtility().sendAllMessage(player, "channel_global", MSG);
			return;
		}
	}
	}
}
