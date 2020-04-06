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

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.interfaces.CustomChannel;
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
		
		String channelwithoutsymbol = plugin.getYamlHandler().getL().getString(language+".channelsymbol.withoutsymbol");
		String channel = plugin.getYamlHandler().getChannel(channelwithoutsymbol, event.getMessage());
		String symbol = plugin.getYamlHandler().getSymbol(channel);
		
		boolean timeofdays = plugin.getYamlHandler().get().getString("addingtimeofdays").equalsIgnoreCase("true");
		String timeofdaysformat = plugin.getUtility().getDate(plugin.getYamlHandler().get().getString("timeofdaysformat"));
		String timeofdaysoutput = plugin.getUtility().tl(plugin.getYamlHandler().get().getString("timeofdaysoutput")
				.replace("%time%", timeofdaysformat));
		
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
					.replace("%time%", time)));
			return;
		}
		
		
		if(channel.equalsIgnoreCase("local")) //----------------------------------------------------------Local Channel
		{			
			if(!plugin.getUtility().hasChannelRights(player, "channel_local"))
			{
				return;
			}
			
			if(event.getMessage().length()>=symbol.length() 
					&& plugin.getUtility().getWordfilter(event.getMessage().substring(
					symbol.length()))) //Wordfilter
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg06")));
				return;
			}
			
			if(event.getMessage().substring(1).length()<0)
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg08")));
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
			
			TextComponent MSG = null;
			if(timeofdays == true) {MSG = plugin.getUtility().tc(timeofdaysoutput);}
			else {MSG = plugin.getUtility().tc("");}
			
			MSG.setExtra(plugin.getUtility().getAllTextComponentForChannels(
					player, event.getMessage(), "local", symbol, symbol.length()));
			
			SimpleChatChannels.log.info(MSG.toLegacyText()); //Console
			
			plugin.getUtility().spy(player, MSG);
			
			for(Player t : Bukkit.getOnlinePlayers())
			{
				World tw = t.getWorld();
				Location tl = t.getLocation();
				if(tw.getName().equals(pyloc.getWorld().getName()))
				{
					if(tl.distance(pyloc) <= blockDistance) 
					{
						if((boolean) plugin.getMysqlInterface().getDataI(t, "channel_local", "player_uuid"))
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
			
			if(event.getMessage().length()>=symbol.length() 
					&& plugin.getUtility().getWordfilter(event.getMessage().substring(symbol.length()))) //Wordfilter
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg06")));
				return;
			}
			
			if(event.getMessage().substring(1).length()<0)
			{
				player.spigot().sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg08")));
				return;
			}
			
			TextComponent MSG = null;
			if(timeofdays == true) {MSG = plugin.getUtility().tc(timeofdaysoutput);}
			else {MSG = plugin.getUtility().tc("");}
			
			MSG.setExtra(plugin.getUtility().getAllTextComponentForChannels(
					player, event.getMessage(), "world", plugin.getYamlHandler().getSymbol("world"),
					plugin.getYamlHandler().getSymbol("world").length()));
			
			SimpleChatChannels.log.info(MSG.toLegacyText()); //Console
			
			plugin.getUtility().spy(player, MSG);
			
			for(Player t : Bukkit.getOnlinePlayers())
			{
				World tw = t.getWorld();
				if(tw.getName().equals(player.getWorld().getName()))
				{
					if((boolean) plugin.getMysqlInterface().getDataI(t, "channel_world", "player_uuid"))
					{
						if(!plugin.getUtility().getIgnored(player,t))
						{
							t.spigot().sendMessage(MSG);
						}
					}
				}
			}
			
			return;
		} else if(channel.equalsIgnoreCase("global") || channel.equalsIgnoreCase("trade") || channel.equalsIgnoreCase("support") 
				|| channel.equalsIgnoreCase("auction") || channel.equalsIgnoreCase("team") || channel.equalsIgnoreCase("admin")) 
			//----------------------------------------------------------Trade Channel
		{
			if(!plugin.getUtility().hasChannelRights(player, "channel_"+channel))
			{
				return;
			}	
			
			if(event.getMessage().length()>=symbol.length()
					&& plugin.getUtility().getWordfilter(event.getMessage().substring(symbol.length()))) //Wordfilter
			{
				player.spigot().sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg06"))));
				return;
			}
			
			TextComponent MSG = null;
			if(timeofdays == true) {MSG = plugin.getUtility().tc(timeofdaysoutput);}
			else {MSG = plugin.getUtility().tc("");}
			
			
			MSG.setExtra(plugin.getUtility().getAllTextComponentForChannels(
					player, event.getMessage(), channel, symbol, symbol.length()));
			
			SimpleChatChannels.log.info(MSG.toLegacyText()); //Console
			
			if(event.getMessage().substring(1).length()<0)
			{
				player.spigot().sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
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
				player.spigot().sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg11"))));
				return;
			}
			
			CustomChannel cc = CustomChannel.getCustomChannel(player);
			
			if(event.getMessage().length()>=symbol.length()
					&& plugin.getUtility().getWordfilter(event.getMessage().substring(symbol.length()))) //Wordfilter
			{
				player.spigot().sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg06"))));
				return;
			}
			
			TextComponent MSG = null;
			if(timeofdays == true) {MSG = plugin.getUtility().tc(timeofdaysoutput);}
			else {MSG = plugin.getUtility().tc("");}
			
			
			MSG.setExtra(plugin.getUtility().getAllTextComponentForChannels(
					player, event.getMessage(), "custom", symbol, symbol.length()));
			
			SimpleChatChannels.log.info(MSG.toLegacyText()); //Console
			
			if(event.getMessage().substring(1).length()<0)
			{
				player.spigot().sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg08"))));
				return;
			}
			plugin.getUtility().spy(player, MSG);
			for(Player all : plugin.getServer().getOnlinePlayers())
			{
				for(Player members : cc.getMembers())
				{
					if(members.getName().equals(all.getName()))
					{
						if((boolean) plugin.getMysqlInterface().getDataI(all, "channel_custom", "player_uuid"))
						{
							if(!plugin.getUtility().getIgnored(player,all))
							{
								all.spigot().sendMessage(MSG);
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
				player.spigot().sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg06"))));
				return;
			}
			
			String preorsuffix = eventmsg[0].substring(symbol.length());
			String pors = preorsuffix;
			
			String ps = plugin.getUtility().getPreOrSuffix(preorsuffix);
			
			if(ps.equals("scc.no_prefix_suffix"))
			{
				player.spigot().sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
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
			
			TextComponent MSG = null;
			if(timeofdays == true) {MSG = plugin.getUtility().tc(timeofdaysoutput);}
			else {MSG = plugin.getUtility().tc("");}
			
			
			MSG.setExtra(plugin.getUtility().getTCinLine(channels, prefix, playertext, suffix, plugin.getUtility().msgLater(player, lenghteventmsg,"group", event.getMessage())));
			
			SimpleChatChannels.log.info(MSG.toLegacyText()); //Console
			
			if(event.getMessage().substring(lenghteventmsg).length()<0)
			{
				player.spigot().sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg08"))));
				return;
			}
			
			player.spigot().sendMessage(MSG);
			
			for(Player all : plugin.getServer().getOnlinePlayers())
			{
				if((boolean) plugin.getMysqlInterface().getDataI(all, "channel_group", "player_uuid"))
				{
					if(!all.equals(player))
					{
						if(!plugin.getUtility().getIgnored(player,all))
						{
							if(all.hasPermission(ps))
							{
								all.spigot().sendMessage(MSG);
							}
						}
					}
				}
			}
			
			plugin.getUtility().spy(player, MSG);
			return;
		} else if(channel.equalsIgnoreCase("pmre")) //Reply Message
		{
			if(!plugin.getUtility().hasChannelRights(player, "channel_pm"))
			{
				return;
			}
			if(!reply.containsKey(pl))
			{
				player.spigot().sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg05"))));
				return;
			}
			String target = reply.get(pl);
			if(plugin.getServer().getPlayer(UUID.fromString(target)) == null)
			{
				player.spigot().sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg02"))));
				return;
			}
			Player tr = plugin.getServer().getPlayer(UUID.fromString(target));
			String trl = tr.getUniqueId().toString();
			
			if(plugin.getAfkRecord()!=null)
			{
				if(plugin.getAfkRecord().isAfk(tr))
				{
					player.spigot().sendMessage(plugin.getUtility().tcl(
							plugin.getYamlHandler().getL().getString(language+".afkrecord.msg01")));
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
							.replace("%player%", player.getName()))).create()));
			
			if(event.getMessage().length()>=symbol.length()
					&& plugin.getUtility().getWordfilter(event.getMessage().substring(
					symbol.length()))) //Wordfilter
			{
				player.spigot().sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg06"))));
				return;
			}
			
			TextComponent MSG1 = null;
			if(timeofdays == true) {MSG1 = plugin.getUtility().tc(timeofdaysoutput);}
			else {MSG1 = plugin.getUtility().tc("");}
			
			TextComponent MSG2 = null;
			if(timeofdays == true) {MSG2 = plugin.getUtility().tc(timeofdaysoutput);}
			else {MSG2 = plugin.getUtility().tc("");}
			
			
			MSG1.setExtra(plugin.getUtility().getTCinLinePN(channel1, playertext, player2text, plugin.getUtility().msgLater(player, symbol.length(),"pmre", event.getMessage())));
			MSG2.setExtra(plugin.getUtility().getTCinLinePN(channel2, playertext, player2text, plugin.getUtility().msgLater(player, symbol.length(),"pmre", event.getMessage())));
			
			SimpleChatChannels.log.info(MSG1.toLegacyText()); //Console
			
			if(event.getMessage().substring(3).length()<0)
			{
				player.spigot().sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg08"))));
				return;
			}
			
			if(!plugin.getUtility().hasChannelRights(tr, "channel_pm"))
			{
				return;
			}
			if(plugin.getUtility().getIgnored(player,tr))
			{
				player.spigot().sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg03"))));
				plugin.getUtility().spy(player, MSG1);
				player.spigot().sendMessage(MSG2);
				return;
			}
			tr.spigot().sendMessage(MSG1);
			player.spigot().sendMessage(MSG2);
			plugin.getUtility().spy(player, MSG1);
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
			String target = targets[0].substring(symbol.length());
			if(plugin.getServer().getPlayer(target) == null)
			{
				player.spigot().sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg02"))));
				return;
			}
			Player tr = plugin.getServer().getPlayer(target);
			String trl = tr.getUniqueId().toString();
			
			if(plugin.getAfkRecord()!=null)
			{
				if(plugin.getAfkRecord().isAfk(tr))
				{
					player.spigot().sendMessage(plugin.getUtility().tcl(
							plugin.getYamlHandler().getL().getString(language+".afkrecord.msg01")));
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
				player.spigot().sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg08"))));
				return;
			}
			if(event.getMessage().length()>=symbol.length()
					&& plugin.getUtility().getWordfilter(event.getMessage().substring(symbol.length()))) //Wordfilter
			{
				player.spigot().sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg06"))));
				return;
			}
			
			TextComponent MSG1 = null;
			if(timeofdays == true) {MSG1 = plugin.getUtility().tc(timeofdaysoutput);}
			else {MSG1 = plugin.getUtility().tc("");}
			
			TextComponent MSG2 = null;
			if(timeofdays == true) {MSG2 = plugin.getUtility().tc(timeofdaysoutput);}
			else {MSG2 = plugin.getUtility().tc("");}
			
			MSG1.setExtra(plugin.getUtility().getTCinLinePN(channel1, playertext, player2text, plugin.getUtility().msgLater(player, targets[0].length(),"pm", event.getMessage())));
			MSG2.setExtra(plugin.getUtility().getTCinLinePN(channel2, playertext, player2text, plugin.getUtility().msgLater(player, targets[0].length(),"pm", event.getMessage())));
			
			SimpleChatChannels.log.info(MSG1.toLegacyText()); //Console
			
			if(!plugin.getUtility().hasChannelRights(tr, "channel_pm"))
			{
				return;
			}	
			if(plugin.getUtility().getIgnored(player,tr))
			{
				player.spigot().sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg03"))));
				plugin.getUtility().spy(player, MSG1);
				player.spigot().sendMessage(MSG2);
				return;
			}
			tr.spigot().sendMessage(MSG1);
			player.spigot().sendMessage(MSG2);
			plugin.getUtility().spy(player, MSG1);
			reply.put(pl, trl);
			reply.put(trl, pl);
			return;
		}
	}
}
