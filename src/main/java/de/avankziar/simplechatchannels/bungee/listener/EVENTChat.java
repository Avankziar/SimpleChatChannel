package main.java.de.avankziar.simplechatchannels.bungee.listener;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import main.java.de.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.de.avankziar.simplechatchannels.bungee.interfaces.CustomChannel;
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
		if(event.getMessage().startsWith(plugin.getYamlHandler().getSymbol("local")))
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
		if(event.getMessage().startsWith(plugin.getYamlHandler().getSymbol("world")))
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
					.replaceAll("%time%", time));
			return;
		}
		
		if(event.getMessage().startsWith(plugin.getYamlHandler().getSymbol("trade"))) //----------------------------------------------------------Trade Channel
		{
			if(!plugin.getUtility().hasChannelRights(player, "channel_trade"))
			{
				return;
			}	
			
			if(plugin.getUtility().getWordfilter(event.getMessage().substring(plugin.getYamlHandler().getSymbol("trade").length()))) //Wordfilter
			{
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg06"))));
				return;
			}
			
			TextComponent MSG = plugin.getUtility().tc("");
			
			MSG.setExtra(plugin.getUtility().getAllTextComponentForChannels(
					player, event.getMessage(), "trade", plugin.getYamlHandler().getSymbol("trade"),
					plugin.getYamlHandler().getSymbol("trade").length()));
			
			plugin.getProxy().getConsole().sendMessage(MSG); //Console
			
			if(event.getMessage().substring(1).length()<0)
			{
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg08"))));
				return;
			}
			
			plugin.getUtility().sendAllMessage(player, "channel_trade", MSG);
			return;
		} else if(event.getMessage().startsWith(plugin.getYamlHandler().getSymbol("support"))) //----------------------------------------------------------Support Channel
		{
			if(!plugin.getUtility().hasChannelRights(player, "channel_support"))
			{
				return;
			}
			
			if(plugin.getUtility().getWordfilter(event.getMessage().substring(plugin.getYamlHandler().getSymbol("support").length()))) //Wordfilter
			{
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg06"))));
				return;
			}
			
			TextComponent MSG = plugin.getUtility().tc("");
			
			MSG.setExtra(plugin.getUtility().getAllTextComponentForChannels(
					player, event.getMessage(), "support", plugin.getYamlHandler().getSymbol("support"),
					plugin.getYamlHandler().getSymbol("support").length()));
			
			plugin.getProxy().getConsole().sendMessage(MSG); //Console
			
			if(event.getMessage().substring(1).length()<0)
			{
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg08"))));
				return;
			}
			
			plugin.getUtility().sendAllMessage(player, "channel_support", MSG);
			return;
		} else if(event.getMessage().startsWith(plugin.getYamlHandler().getSymbol("auction"))) //----------------------------------------------------------Auction Channel
		{
			if(!plugin.getUtility().hasChannelRights(player, "channel_auction"))
			{
				return;
			}
			
			if(plugin.getUtility().getWordfilter(event.getMessage().substring(plugin.getYamlHandler().getSymbol("auction").length()))) //Wordfilter
			{
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg06"))));
				return;
			}
			
			TextComponent MSG = plugin.getUtility().tc("");
			
			MSG.setExtra(plugin.getUtility().getAllTextComponentForChannels(
					player, event.getMessage(), "auction", plugin.getYamlHandler().getSymbol("auction"),
					plugin.getYamlHandler().getSymbol("auction").length()));
			
			plugin.getProxy().getConsole().sendMessage(MSG); //Console
			
			if(event.getMessage().substring(1).length()<0)
			{
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg08"))));
				return;
			}
			
			plugin.getUtility().sendAllMessage(player, "channel_auction", MSG);
			return;
		} else if(event.getMessage().startsWith(plugin.getYamlHandler().getSymbol("team"))) //----------------------------------------------------------Team Channel
		{
			if(!plugin.getUtility().hasChannelRights(player, "channel_team"))
			{
				return;
			}
			
			if(plugin.getUtility().getWordfilter(event.getMessage().substring(plugin.getYamlHandler().getSymbol("team").length()))) //Wordfilter
			{
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg06"))));
				return;
			}
			
			TextComponent MSG = plugin.getUtility().tc("");
			
			MSG.setExtra(plugin.getUtility().getAllTextComponentForChannels(
					player, event.getMessage(), "team", plugin.getYamlHandler().getSymbol("team"),
					plugin.getYamlHandler().getSymbol("team").length()));
			
			plugin.getProxy().getConsole().sendMessage(MSG); //Console
			
			if(event.getMessage().substring(1).length()<0)
			{
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg08"))));
				return;
			}
			
			plugin.getUtility().sendAllMessage(player, "channel_team", MSG);
			return;
		} else if(event.getMessage().startsWith(plugin.getYamlHandler().getSymbol("admin"))) //----------------------------------------------------------Admin Channel
		{
			if(!plugin.getUtility().hasChannelRights(player, "channel_admin"))
			{
				return;
			}
			
			if(plugin.getUtility().getWordfilter(event.getMessage().substring(plugin.getYamlHandler().getSymbol("admin").length()))) //Wordfilter
			{
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg06"))));
				return;
			}
			
			TextComponent MSG = plugin.getUtility().tc("");
			MSG.setExtra(plugin.getUtility().getAllTextComponentForChannels(
					player, event.getMessage(), "admin", plugin.getYamlHandler().getSymbol("admin"),
					plugin.getYamlHandler().getSymbol("admin").length()));
			
			plugin.getProxy().getConsole().sendMessage(MSG); //Console
			
			if(event.getMessage().substring(1).length()<0)
			{
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg08"))));
				return;
			}
			
			plugin.getUtility().sendAllMessage(player, "channel_admin", MSG);
			return;
		} else if(event.getMessage().startsWith(plugin.getYamlHandler().getSymbol("group"))) //----------------------------------------------------------Gruppe Channel
		{
			if(!plugin.getUtility().hasChannelRights(player, "channel_group"))
			{
				return;
			}
			
			String[] eventmsg = event.getMessage().split(" ");
			int lenghteventmsg = eventmsg[0].length()+1;
			
			if(plugin.getUtility().getWordfilter(event.getMessage().substring(lenghteventmsg))) //Wordfilter
			{
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg06"))));
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
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg09"))));
				return;
			}
			
			TextComponent channel = plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+".channels.group")
					.replaceAll("%group%", preorsuffix)));
			channel.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, plugin.getYamlHandler().getSymbol("group")+pors+" "));
			channel.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(
							plugin.getYamlHandler().getL().getString(language+".channelextra.hover.group"))).create()));
			
			List<BaseComponent> prefix = plugin.getUtility().getPrefix(player);
			
			TextComponent playertext = plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+".playercolor")+player.getName()));
			playertext.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, plugin.getYamlHandler().getSymbol("pm")+player.getName()+" "));
			playertext.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(
							plugin.getYamlHandler().getL().getString(language+".channelextra.hover.message")
							.replaceAll("%player%", player.getName()))).create()));
			
			List<BaseComponent> suffix = plugin.getUtility().getSuffix(player);
			
			TextComponent msg = plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+".chatsplit.group")
					+plugin.getUtility().MsgLater(lenghteventmsg,"group", event.getMessage())));
			
			TextComponent MSG = plugin.getUtility().tc("");
			
			MSG.setExtra(plugin.getUtility().getTCinLine(channel, prefix, playertext, suffix, msg));
			
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
				if((boolean) plugin.getMysqlInterface().getDataI(player, "channel_group", "player_uuid"))
				{
					if(!all.equals(player))
					{
						if(!plugin.getUtility().getIgnored(player,all))
						{
							all.sendMessage(MSG);
						}
					}
				}
			}
			
			plugin.getUtility().spy(MSG);
			return;
		} else if(event.getMessage().startsWith(plugin.getYamlHandler().getSymbol("pm")+"r ")) //Reply Message
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
			String trl = tr.getUniqueId().toString();
			
			TextComponent channel1 = plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+".channels.message")));
			channel1.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, plugin.getYamlHandler().getSymbol("pm")+player.getName()+" "));
			channel1.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(
							plugin.getYamlHandler().getL().getString(language+".channelextra.hover.message")
							.replaceAll("%player%", player.getName()))).create()));
			
			TextComponent channel2 = plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString("SCC."+language+".channels.message")));
			channel2.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, plugin.getYamlHandler().getSymbol("pm")+tr.getName()+" "));
			channel2.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(plugin.getYamlHandler().getL().getString(language+".channelextra.hover.message")
							.replaceAll("%player%", tr.getName()))).create()));
			
			TextComponent playertext = plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString("SCC."+language+".playercolor")+player.getName()));
			playertext.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, plugin.getYamlHandler().getSymbol("pm")+player.getName()+" "));
			playertext.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(
							plugin.getYamlHandler().getL().getString(language+".channelextra.hover.message")
							.replaceAll("%player%", player.getName()))).create()));
			
			TextComponent player2text = plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString("SCC."+language+".playertoplayer")+tr.getName()));
			player2text.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, plugin.getYamlHandler().getSymbol("pm")+tr.getName()+" "));
			player2text.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(
							plugin.getYamlHandler().getL().getString(language+".channelextra.hover.message")
							.replaceAll("%player%", player.getName()))).create()));
			
			TextComponent msg = plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+".chatsplit.message")
					+plugin.getUtility().MsgLater(3,"message", event.getMessage())));
			
			if(plugin.getUtility().getWordfilter(event.getMessage().substring(plugin.getYamlHandler().getSymbol("pm").length()+2))) //Wordfilter
			{
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg06"))));
				return;
			}
			
			TextComponent MSG1 = plugin.getUtility().tc("");
			TextComponent MSG2 = plugin.getUtility().tc("");
			
			MSG1.setExtra(plugin.getUtility().getTCinLinePN(channel1, playertext, player2text, msg));
			MSG2.setExtra(plugin.getUtility().getTCinLinePN(channel2, playertext, player2text, msg));
			
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
		} else if(event.getMessage().startsWith(plugin.getYamlHandler().getSymbol("pm"))) //Private Message
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
			
			TextComponent channel1 = plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+".channels.message")));
			channel1.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, plugin.getYamlHandler().getSymbol("pm")+player.getName()+" "));
			channel1.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(
							plugin.getYamlHandler().getL().getString(language+".channelextra.hover.message")
							.replaceAll("%player%", player.getName()))).create()));
			
			TextComponent channel2 = plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+".channels.message")));
			channel2.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, plugin.getYamlHandler().getSymbol("pm")+tr.getName()+" "));
			channel2.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(
							plugin.getYamlHandler().getL().getString(language+".channelextra.hover.message")
							.replaceAll("%player%", tr.getName()))).create()));
			
			TextComponent playertext = plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+".playercolor")+player.getName()));
			playertext.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, plugin.getYamlHandler().getSymbol("pm")+player.getName()+" "));
			playertext.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(
							plugin.getYamlHandler().getL().getString(language+".channelextra.hover.message")
							.replaceAll("%player%", player.getName()))).create()));
			
			TextComponent player2text = plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+".playertoplayer")+tr.getName()));
			player2text.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, plugin.getYamlHandler().getSymbol("pm")+tr.getName()+" "));
			player2text.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
					, new ComponentBuilder(plugin.getUtility().tl(
							plugin.getYamlHandler().getL().getString(language+".channelextra.hover.message")
							.replaceAll("%player%", tr.getName()))).create()));
			
			TextComponent msg = plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+".chatsplit.message")
					+plugin.getUtility().MsgLater(targets[0].length()+1,"message", event.getMessage())));
			
			if(event.getMessage().substring(targets[0].length()+1).length()<0)
			{
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg08"))));
				return;
			}
			if(plugin.getUtility().getWordfilter(event.getMessage().substring(plugin.getYamlHandler().getSymbol("pm").length()))) //Wordfilter
			{
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg06"))));
				return;
			}
			
			TextComponent MSG1 = plugin.getUtility().tc("");
			TextComponent MSG2 = plugin.getUtility().tc("");
			
			MSG1.setExtra(plugin.getUtility().getTCinLinePN(channel1, playertext, player2text, msg));
			MSG2.setExtra(plugin.getUtility().getTCinLinePN(channel2, playertext, player2text, msg));
			
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
		} else if(event.getMessage().startsWith(plugin.getYamlHandler().getSymbol("custom"))) //----------------------------------------------------------Support Channel
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
			
			if(plugin.getUtility().getWordfilter(event.getMessage().substring(plugin.getYamlHandler().getSymbol("custom").length()))) //Wordfilter
			{
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg06"))));
				return;
			}
			
			TextComponent MSG = plugin.getUtility().tc("");
			
			MSG.setExtra(plugin.getUtility().getAllTextComponentForChannels(
					player, event.getMessage(), "custom", plugin.getYamlHandler().getSymbol("custom"),
					plugin.getYamlHandler().getSymbol("custom").length()));
			
			plugin.getProxy().getConsole().sendMessage(MSG); //Console
			
			if(event.getMessage().substring(1).length()<0)
			{
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg08"))));
				return;
			}
			plugin.getUtility().spy(MSG);
			plugin.getUtility().sendAllMessage(player, "channel_custom", MSG);
			return;
		} else //Global Channel
		{
			if(!plugin.getUtility().hasChannelRights(player, "channel_global"))
			{
				return;
			}
			
			if(plugin.getUtility().getWordfilter(event.getMessage().substring(plugin.getYamlHandler().getSymbol("global").length()))) //Wordfilter
			{
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg06"))));
				return;
			} 
			
			TextComponent MSG = plugin.getUtility().tc("");
			MSG.setExtra(plugin.getUtility().getAllTextComponentForChannels(
					player, event.getMessage(), "global", plugin.getYamlHandler().getSymbol("global"),
					plugin.getYamlHandler().getSymbol("global").length()));
			
			plugin.getProxy().getConsole().sendMessage(MSG); //Console
			
			if(event.getMessage().substring(0).length()<0)
			{
				player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg08"))));
				return;
			}
			
			plugin.getUtility().sendAllMessage(player, "channel_global", MSG);
			return;
		}
	}
}
