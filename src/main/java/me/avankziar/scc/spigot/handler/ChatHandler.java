package main.java.me.avankziar.scc.spigot.handler;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.handlers.ColorHandler;
import main.java.me.avankziar.scc.handlers.TimeHandler;
import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.ChatUser;
import main.java.me.avankziar.scc.objects.KeyHandler;
import main.java.me.avankziar.scc.objects.PermanentChannel;
import main.java.me.avankziar.scc.objects.ServerLocation;
import main.java.me.avankziar.scc.objects.chat.Channel;
import main.java.me.avankziar.scc.objects.chat.ChatTitle;
import main.java.me.avankziar.scc.objects.chat.Components;
import main.java.me.avankziar.scc.objects.chat.ItemJson;
import main.java.me.avankziar.scc.objects.chat.UsedChannel;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;
import main.java.me.avankziar.scc.spigot.assistance.Utility;
import main.java.me.avankziar.scc.spigot.database.MysqlHandler.Type;
import main.java.me.avankziar.scc.spigot.listener.ChatListener;
import main.java.me.avankziar.scc.spigot.objects.BypassPermission;
import main.java.me.avankziar.scc.spigot.objects.ChatUserHandler;
import main.java.me.avankziar.scc.spigot.objects.PluginSettings;
import main.java.me.avankziar.scc.spigot.objects.TemporaryChannel;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class ChatHandler
{
	private SimpleChatChannels plugin;
	private static String console = "Console";
	private static LinkedHashMap<String, String> privateChatColorPerPlayers = new LinkedHashMap<>();
	private static ArrayList<String> privateChatColor = new ArrayList<>();
	public static LinkedHashMap<String, String> emojiList = new LinkedHashMap<>();
	
	public ChatHandler(SimpleChatChannels plugin)
	{
		this.plugin = plugin;
	}
	
	public static void initPrivateChatColors()
	{
		List<String> colors = SimpleChatChannels.getPlugin().getYamlHandler().getConfig()
				.getStringList("PrivateChannel.DynamicColorPerPlayerChat");
		for(String c : colors)
		{
			String hex = ColorHandler.getHex(c);
			privateChatColor.add(hex);
		}
	}
	
	public boolean prePreCheck(Player player, String message)
	{
		/*
		 * Is player muted?
		 */
		final long mutedTime = plugin.getUtility().getMutedTime(player);
		if(mutedTime > System.currentTimeMillis())
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("ChatListener.YouAreMuted")
					.replace("%time%", TimeHandler.getDateTime(mutedTime))));
			return false;
		}
		/*
		 * Wordfilter
		 */
		if(plugin.getUtility().containsBadWords(message))
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("ChatListener.ContainsBadWords")));
			return false;
		}
		return true;
	}
	
	public void startChat(Player player, Channel usedChannel, String message)
	{
		/*
		 * PreChecks
		 */
		if(!preChecks(player, usedChannel, message))
		{
			return;
		}
		/*
		 * Define the users Pre- and suffixs.
		 */
		ArrayList<ChatTitle> userPrefix = new ArrayList<>();
		ArrayList<ChatTitle> userSuffix = new ArrayList<>();
		for(ChatTitle ct : SimpleChatChannels.chatTitlesPrefix)
		{
			if(player.hasPermission(ct.getPermission()))
			{
				userPrefix.add(ct);
			}
		}
		for(ChatTitle ct : SimpleChatChannels.chatTitlesSuffix)
		{
			if(player.hasPermission(ct.getPermission()))
			{
				userSuffix.add(ct);
			}
		}
		/*
		 * Init tc and pc
		 */
		TemporaryChannel tc = null;
		PermanentChannel pc = null;
		
		if(usedChannel.getUniqueIdentifierName().equalsIgnoreCase("Permanent"))
		{
			/*
			 * Define, whiche pc it is.
			 */
			String[] space = message.split(" ");
			if(space.length < 2)
			{
				return;
			}
			String s = space[0].substring(usedChannel.getSymbol().length());
			if(s.length() > 0);
			{
				pc = PermanentChannel.getChannelFromSymbol(s);
			}
			if(pc == null)
			{
				///Der permanente Channel %symbol% existiert nicht.
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("ChatListener.SymbolNotKnow")
						.replace("%symbol%", space[0])));
				return;
			}
			if(!pc.getMembers().contains(player.getUniqueId().toString()))
			{
				///Du bist in keinem Permanenten Channel!
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("ChatListener.NotAPermanentChannel")));
				return;
			}
		} else if(usedChannel.getUniqueIdentifierName().equalsIgnoreCase("Temporary"))
		{
			/*
			 * Define which tc it is.
			 */
			tc = TemporaryChannel.getCustomChannel(player);
			
			if(tc == null)
			{
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("ChatListener.NotATemporaryChannel")));
				return;
			}
		}
		Components components = getComponent(
				player, null, message, usedChannel.getChatFormat(), userPrefix, userSuffix, usedChannel, tc, pc, null, null, 
				usedChannel.getInChatColorMessage());
		sendMessage(components, player, usedChannel, tc, pc);
	}
	
	public void startPrivateChat(Player player, Player other, String message)
	{
		Channel usedChannel = null;
		for(Channel c : SimpleChatChannels.channels.values())
		{
			if(c.getUniqueIdentifierName().equals("Private"))
			{
				usedChannel = c;
				break;
			}
		}
		if(usedChannel == null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMsg.PrivateChannelsNotActive")));
			return;
		}
		/*
		 * RePlayer set
		 */
		rePlayerHandling(player, other);
		rePlayerHandling(other, player);
		/*
		 * PreChecks
		 */
		if(!preChecks(player, usedChannel, message))
		{
			return;
		}
		/*
		 * Define the users Pre- and suffixs.
		 */
		ArrayList<ChatTitle> userPrefix = new ArrayList<>();
		ArrayList<ChatTitle> userSuffix = new ArrayList<>();
		ArrayList<ChatTitle> otheruserPrefix = new ArrayList<>();
		ArrayList<ChatTitle> otheruserSuffix = new ArrayList<>();
		for(ChatTitle ct : SimpleChatChannels.chatTitlesPrefix)
		{
			if(player.hasPermission(ct.getPermission()))
			{
				userPrefix.add(ct);
			}
			if(other.hasPermission(ct.getPermission()))
			{
				otheruserPrefix.add(ct);
			}
		}
		for(ChatTitle ct : SimpleChatChannels.chatTitlesSuffix)
		{
			if(player.hasPermission(ct.getPermission()))
			{
				userSuffix.add(ct);
			}
			if(other.hasPermission(ct.getPermission()))
			{
				otheruserSuffix.add(ct);
			}
		}
		String basecolor = getUsedChannelColor(usedChannel, player.getName(), other.getName());
		Components components = getComponent(
				player, other, message, usedChannel.getChatFormat(),
				userPrefix, userSuffix, usedChannel, null, null, otheruserPrefix, otheruserSuffix,
				basecolor);
		sendPrivateMessage(components, player, other, usedChannel);
	}
	
	public void startPrivateConsoleChat(CommandSender console, Player other, String message)
	{
		Channel usedChannel = null;
		for(Channel c : SimpleChatChannels.channels.values())
		{
			if(c.getUniqueIdentifierName().equals("Private"))
			{
				usedChannel = c;
				break;
			}
		}
		/*
		 * Define the users Pre- and suffixs.
		 */
		ArrayList<ChatTitle> otheruserPrefix = new ArrayList<>();
		ArrayList<ChatTitle> otheruserSuffix = new ArrayList<>();
		for(ChatTitle ct : SimpleChatChannels.chatTitlesPrefix)
		{
			if(other.hasPermission(ct.getPermission()))
			{
				otheruserPrefix.add(ct);
			}
		}
		for(ChatTitle ct : SimpleChatChannels.chatTitlesSuffix)
		{
			if(other.hasPermission(ct.getPermission()))
			{
				otheruserSuffix.add(ct);
			}
		}		
		Components components = getComponent(
				console, other, message, usedChannel.getChatFormat(),
				new ArrayList<>(), new ArrayList<>(), usedChannel, null, null, otheruserPrefix, otheruserSuffix,
				usedChannel.getInChatColorMessage());
		sendPrivateConsoleMessage(components, console, other, usedChannel);
	}
	
	private String getUsedChannelColor(Channel channel, String player1, String player2)
	{
		if(plugin.getYamlHandler().getConfig().getBoolean("PrivateChannel.UseDynamicColor", true))
		{
			return channel.getInChatColorMessage();
		}
		if(privateChatColorPerPlayers.containsKey(player1+player2))
		{
			return privateChatColorPerPlayers.get(player1+player2);
		} else if(privateChatColorPerPlayers.containsKey(player2+player1))
		{
			return privateChatColorPerPlayers.get(player2+player1);
		} else
		{
			ArrayList<String> alreadyUsedColors = new ArrayList<>();
			for(String playernames : privateChatColorPerPlayers.keySet())
			{
				if(playernames.contains(player1) || playernames.contains(player2))
				{
					alreadyUsedColors.add(privateChatColorPerPlayers.get(playernames));
				}
			}
			for(int i = 0; i < privateChatColor.size(); i++)
			{
				if(i >= alreadyUsedColors.size() || !privateChatColor.get(i).equalsIgnoreCase(alreadyUsedColors.get(i)))
				{
					return privateChatColor.get(i);
				}
			}
			Random r = new Random();
			int i = r.nextInt(privateChatColor.size()-1);
			return privateChatColor.get(i);
		}
	}
	
	public void sendBroadCast(CommandSender players, Channel usedChannel, String message)
	{
		Components components = getComponent(Channel.ChatFormatPlaceholder.MESSAGE.getPlaceholder(),
				message, players, null, new ArrayList<>(), new ArrayList<>(), usedChannel, null, null, null, null,
				usedChannel.getInChatColorMessage());
		TextComponent txc1 = ChatApi.tc("");
		txc1.setExtra(components.getComponents());
		TextComponent txc2 = ChatApi.tc("");
		txc2.setExtra(components.getComponentsWithMentions());
		
		
		for(Player all : plugin.getServer().getOnlinePlayers())
		{
			if(components.isMention(all.getName()))
			{
				all.spigot().sendMessage(txc2);
				if(all.hasPermission(BypassPermission.USE_SOUND))
				{
					sendMentionPing(all);
				}
			} else
			{
				all.spigot().sendMessage(txc1);
			}
		}
	}
	
	private void rePlayerHandling(Player player, Player other)
	{
		if(SimpleChatChannels.rePlayers.containsKey(player.getUniqueId().toString()))
		{
			ArrayList<String> relist = SimpleChatChannels.rePlayers.get(player.getUniqueId().toString());
			if(!relist.contains(other.getName()))
			{
				relist.add(other.getName());
				SimpleChatChannels.rePlayers.replace(player.getUniqueId().toString(), relist);
			}
		} else
		{
			ArrayList<String> relist = new ArrayList<>();
			relist.add(other.getName());
			SimpleChatChannels.rePlayers.put(player.getUniqueId().toString(), relist);
		}
		if(SimpleChatChannels.rPlayers.containsKey(player.getUniqueId().toString()))
		{
			SimpleChatChannels.rPlayers.replace(player.getUniqueId().toString(), other.getName());
		} else
		{
			SimpleChatChannels.rPlayers.put(player.getUniqueId().toString(), other.getName());
		}
	}
	
	private boolean preChecks(Player player, Channel usedChannel, String message)
	{
		/*
		 * Is Channel off?
		 */
		if(!Utility.playerUsedChannels.containsKey(player.getUniqueId().toString())
				|| !Utility.playerUsedChannels.get(player.getUniqueId().toString()).containsKey(usedChannel.getUniqueIdentifierName())
				|| !Utility.playerUsedChannels.get(player.getUniqueId().toString()).get(usedChannel.getUniqueIdentifierName()).isUsed())
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("ChatListener.ChannelIsOff")));
			return false;
		}
		/*
		 * Trim the orginal message, and if the message is empty, so return;
		 */
		if(message.length() <= usedChannel.getSymbol().length())
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("ChatListener.StringTrim")));
			return false;
		}
		
		/*
		 * Spam Protection Wall
		 */
		long now = System.currentTimeMillis();
		long last = 0;
		if(ChatListener.spamMap.containsKey(player.getUniqueId().toString()) 
				&& ChatListener.spamMap.get(player.getUniqueId().toString()).containsKey(usedChannel.getUniqueIdentifierName()))
		{
			if(ChatListener.spamMap.get(player.getUniqueId().toString()).get(usedChannel.getUniqueIdentifierName()) > now)
			{ 
				final long diff = ChatListener.spamMap.get(player.getUniqueId().toString()).get(usedChannel.getUniqueIdentifierName()) - now;
				last = diff +now;
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("ChatListener.PleaseWaitALittle")
						.replace("%channel%", usedChannel.getInChatName())
						.replace("%time%", TimeHandler.getDateTime(last))));
				ChatListener.spamMap.get(player.getUniqueId().toString())
					.replace(player.getUniqueId().toString(), now+usedChannel.getTimeBetweenMessages());
				return false;
			}
			LinkedHashMap<String, Long> map = ChatListener.spamMap.get(player.getUniqueId().toString());
			map.replace(player.getUniqueId().toString(), now+usedChannel.getTimeBetweenMessages());
		} else
		{
			if(ChatListener.spamMap.containsKey(player.getUniqueId().toString()))
			{
				LinkedHashMap<String, Long> map = ChatListener.spamMap.get(player.getUniqueId().toString());
				map.put(usedChannel.getUniqueIdentifierName(), now+usedChannel.getTimeBetweenMessages());
				ChatListener.spamMap.replace(player.getUniqueId().toString(), map);
			} else
			{
				LinkedHashMap<String, Long> map = new LinkedHashMap<>();
				map.put(usedChannel.getUniqueIdentifierName(), now+usedChannel.getTimeBetweenMessages());
				ChatListener.spamMap.put(player.getUniqueId().toString(), map);
			}
			
		}
		/*
		 * Spam Protection Wall 2
		 */
		if(ChatListener.spamMapII.containsKey(player.getUniqueId().toString())
				&& ChatListener.spamMapII.get(player.getUniqueId().toString()).containsKey(usedChannel.getUniqueIdentifierName()))
		{
			if(plugin.getUtility().isSimliarText(message, 
					ChatListener.spamMapII.get(player.getUniqueId().toString())
					.get(usedChannel.getUniqueIdentifierName()), usedChannel.getPercentOfSimilarity())
					
					&& last > (now-usedChannel.getTimeBetweenSameMessages()))
			{
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("ChatListener.PleaseWaitALittleWithSameMessage")
						.replace("%channel%", usedChannel.getInChatName())
						.replace("%time%", TimeHandler.getDateTime(last))));
				return false;
			}
			LinkedHashMap<String, String> map = ChatListener.spamMapII.get(player.getUniqueId().toString());
			map.replace(player.getUniqueId().toString(), message);
		} else
		{
			if(ChatListener.spamMapII.containsKey(player.getUniqueId().toString()))
			{
				LinkedHashMap<String, String> map = ChatListener.spamMapII.get(player.getUniqueId().toString());
				map.put(usedChannel.getUniqueIdentifierName(), message);
				ChatListener.spamMapII.replace(player.getUniqueId().toString(), map);
			} else
			{
				LinkedHashMap<String, String> map = new LinkedHashMap<>();
				map.put(usedChannel.getUniqueIdentifierName(), message);
				ChatListener.spamMapII.put(player.getUniqueId().toString(), map);
			}
		}
		return true;
	}
	
	/**
	 * Return a ArrayList of BaseComponents. This Methode parse every char through and replace the placeholders.
	 * @param player
	 * @param chatFormt
	 * @param prefix
	 * @param suffix
	 * @param usedChannel
	 * @return
	 */
	private Components getComponent(CommandSender player, Player other, String message, String chatFormt,
			ArrayList<ChatTitle> prefix, ArrayList<ChatTitle> suffix, Channel usedChannel,
			TemporaryChannel tc, PermanentChannel pc,
			ArrayList<ChatTitle> otherprefix, ArrayList<ChatTitle> othersuffix, String channelColor)
	{
		Components components = new Components();
		int i = 0;
		while(i < chatFormt.length())
		{
			String s = "";
			char c = chatFormt.charAt(i);
			if(c == '&')
			{
				if(chatFormt.length() > (i+1))
				{
					char c2 = chatFormt.charAt(i+1);
					if(c2 == '#')
					{
						if(chatFormt.length() > i+7)
						{
							s += c+c2
									+chatFormt.charAt(i+2)+chatFormt.charAt(i+3)
									+chatFormt.charAt(i+4)+chatFormt.charAt(i+5)
									+chatFormt.charAt(i+6)+chatFormt.charAt(i+7);
							i = i+8;
							components.addComponent(ChatApi.tctl(s));
							components.addComponentWithMentions(ChatApi.tctl(s));
							continue;
						} else
						{
							s += c+c2;
							i = i+2;
							components.addComponent(ChatApi.tctl(s)).addComponentWithMentions(ChatApi.tctl(s));
							continue;
						}
					} else
					{
						s += c+c2;
						i++;
						components.addComponent(ChatApi.tctl(s)).addComponentWithMentions(ChatApi.tctl(s));
						continue;
					}
				} else
				{
					s += c;
					i++;
					components.addComponent(ChatApi.tctl(s)).addComponentWithMentions(ChatApi.tctl(s));
					continue;
				}
			} else if(c == '%')
			{
				int j = i+1;
				String placeHolder = ""+c;
				while(j < chatFormt.length())
				{
					char c2 = chatFormt.charAt(j);
					placeHolder += c2;
					if(c2 == '%')
					{
						break;
					}
					j++;
				}
				components.addAllComponents(getComponent(placeHolder, message, player,
						other, prefix, suffix, usedChannel, tc, pc, otherprefix, othersuffix, channelColor));
				i++;
				continue;
			} else
			{
				components.addComponent(ChatApi.tctl(s)).addComponentWithMentions(ChatApi.tctl(s));
				i++;
				continue;
			}
		}
		return components;
	}
	
	/**
	 * Get the BaseComponent of every placeholder.
	 * @param placeHolder
	 * @param player
	 * @param prefix
	 * @param suffix
	 * @param usedChannel
	 * @return
	 */
	private Components getComponent(String placeHolder, String message, CommandSender players, Player otherplayer,
			ArrayList<ChatTitle> prefixs, ArrayList<ChatTitle> suffixs,
			Channel usedChannel,
			TemporaryChannel tch, PermanentChannel pc,
			ArrayList<ChatTitle> otherprefixs, ArrayList<ChatTitle> othersuffixs, String channelColor)
	{
		boolean isNotConsole = false;
		Player player = null;
		if(player instanceof Player)
		{
			player = (Player) players;
			isNotConsole = true;
		}
		Components components = new Components();
		TextComponent tc = null;
		Channel.ChatFormatPlaceholder ph = Channel.ChatFormatPlaceholder.getEnum(placeHolder);
		switch(ph)
		{
		case CHANNEL:
			tc = ChatApi.apiChat(usedChannel.getInChatName(),
					ClickEvent.Action.SUGGEST_COMMAND,
					plugin.getUtility().getChannelSuggestion(usedChannel, pc),
					HoverEvent.Action.SHOW_TEXT, 
					plugin.getUtility().getChannelHover(usedChannel));
			return components.addAllComponents(tc);
		case MESSAGE:
			return components.addAllComponents(getMessageParser(players, message, usedChannel, channelColor));
		case PLAYERNAME:
			if(isNotConsole)
			{
				tc = ChatApi.apiChat(player.getName(),
						ClickEvent.Action.SUGGEST_COMMAND, PluginSettings.settings.getCommands(KeyHandler.MSG)+otherplayer.getName(),
						HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
						.replace("%player%", player.getName()));
			} else
			{
				tc = ChatApi.apiChat(console,
						ClickEvent.Action.SUGGEST_COMMAND, PluginSettings.settings.getCommands(KeyHandler.MSG)+otherplayer.getName(),
						HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
						.replace("%player%", "Console"));
			}
			return components.addAllComponents(tc);
		case PLAYERNAME_WITH_PREFIXHIGHCOLORCODE:
			if(isNotConsole)
			{
				if(prefixs.size() > 0)
				{
					tc = ChatApi.apiChat(prefixs.get(0).getInChatColorCode()+player.getName(),
							ClickEvent.Action.SUGGEST_COMMAND, PluginSettings.settings.getCommands(KeyHandler.MSG)+otherplayer.getName(),
							HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
							.replace("%player%", player.getName()));
				} else
				{
					tc = ChatApi.apiChat(player.getName(),
							ClickEvent.Action.SUGGEST_COMMAND, PluginSettings.settings.getCommands(KeyHandler.MSG)+otherplayer.getName(),
							HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
							.replace("%player%", player.getName()));
				}
			} else
			{
				tc = ChatApi.apiChat(console,
						ClickEvent.Action.SUGGEST_COMMAND, PluginSettings.settings.getCommands(KeyHandler.MSG)+otherplayer.getName(),
						HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
						.replace("%player%", console));
			}
			return components.addAllComponents(tc);
		case PLAYERNAME_WITH_SUFFIXHIGHCOLORCODE:
			if(isNotConsole)
			{
				if(suffixs.size() > 0)
				{
					tc = ChatApi.apiChat(suffixs.get(0).getInChatColorCode()+player.getName(),
							ClickEvent.Action.SUGGEST_COMMAND, PluginSettings.settings.getCommands(KeyHandler.MSG+otherplayer.getName()),
							HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
							.replace("%player%", player.getName()));
				} else
				{
					tc = ChatApi.apiChat(player.getName(),
							ClickEvent.Action.SUGGEST_COMMAND, PluginSettings.settings.getCommands(KeyHandler.MSG)+otherplayer.getName(),
							HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
							.replace("%player%", player.getName()));
				}
			} else
			{
				tc = ChatApi.apiChat(console,
						ClickEvent.Action.SUGGEST_COMMAND, PluginSettings.settings.getCommands(KeyHandler.MSG)+otherplayer.getName(),
						HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
						.replace("%player%", console));
			}
			return components.addAllComponents(tc);
		case PREFIXALL:
			if(isNotConsole)
			{
				ArrayList<BaseComponent> bc = new ArrayList<>();
				for(ChatTitle ctp : prefixs)
				{
					bc.add(ChatApi.hoverEvent(ctp.getInChatName(), HoverEvent.Action.SHOW_TEXT, ctp.getHover()));
				}
				tc = ChatApi.tc("");
				tc.setExtra(bc);
			} else
			{
				tc = ChatApi.tc("");
			}
			return components.addAllComponents(tc);
		case PREFIXHIGH:
			if(isNotConsole)
			{
				tc = ChatApi.tc("");
				if(prefixs.size() > 0)
				{
					ChatTitle ct = prefixs.get(0);
					tc = ChatApi.hoverEvent(ct.getInChatName(), HoverEvent.Action.SHOW_TEXT, ct.getHover());
				}
			} else
			{
				tc = ChatApi.tc("");
			}
			return components.addAllComponents(tc);
		case PREFIXLOW:
			if(isNotConsole)
			{
				tc = ChatApi.tc("");
				if(prefixs.size() > 0)
				{
					ChatTitle ct = prefixs.get(prefixs.size()-1);
					tc = ChatApi.hoverEvent(ct.getInChatName(), HoverEvent.Action.SHOW_TEXT, ct.getHover());
				}
			} else
			{
				tc = ChatApi.tc("");
			}
			return components.addAllComponents(tc);
		case SUFFIXALL:
			if(isNotConsole)
			{
				ArrayList<BaseComponent> bc = new ArrayList<>();
				for(ChatTitle cts : suffixs)
				{
					bc.add(ChatApi.hoverEvent(cts.getInChatName(), HoverEvent.Action.SHOW_TEXT, cts.getHover()));
				}
				tc = ChatApi.tc("");
				tc.setExtra(bc);
			} else
			{
				tc = ChatApi.tc("");
			}
			return components.addAllComponents(tc);
		case SUFFIXHIGH:
			if(isNotConsole)
			{
				tc = ChatApi.tc("");
				if(suffixs.size() > 0)
				{
					ChatTitle ct = suffixs.get(0);
					tc = ChatApi.hoverEvent(ct.getInChatName(), HoverEvent.Action.SHOW_TEXT, ct.getHover());
				}
			} else
			{
				tc = ChatApi.tc("");
			}
			return components.addAllComponents(tc);
		case SUFFIXLOW:
			if(isNotConsole)
			{
				tc = ChatApi.tc("");
				if(prefixs.size() > 0)
				{
					ChatTitle ct = suffixs.get(suffixs.size()-1);
					tc = ChatApi.hoverEvent(ct.getInChatName(), HoverEvent.Action.SHOW_TEXT, ct.getHover());
				}
			} else
			{
				tc = ChatApi.tc("");
			}
			return components.addAllComponents(tc);
		case OTHER_PLAYERNAME:
			if(otherplayer == null)
			{
				tc = ChatApi.tc("");
			} else
			{
				tc = ChatApi.apiChat(otherplayer.getName(),
						ClickEvent.Action.SUGGEST_COMMAND, PluginSettings.settings.getCommands(KeyHandler.MSG)+otherplayer.getName(),
						HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
						.replace("%player%", otherplayer.getName()));
			}
			return components.addAllComponents(tc);
		case OTHER_PLAYERNAME_WITH_PREFIXHIGHCOLORCODE:
			if(otherplayer == null)
			{
				tc = ChatApi.tc("");
			} else
			{
				if(otherprefixs.size() > 0)
				{
					tc = ChatApi.apiChat(otherprefixs.get(0).getInChatColorCode()+otherplayer.getName(),
							ClickEvent.Action.SUGGEST_COMMAND, PluginSettings.settings.getCommands(KeyHandler.MSG)+otherplayer.getName(),
							HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
							.replace("%player%", otherplayer.getName()));
				} else
				{
					tc = ChatApi.apiChat(otherplayer.getName(),
							ClickEvent.Action.SUGGEST_COMMAND, PluginSettings.settings.getCommands(KeyHandler.MSG)+otherplayer.getName(),
							HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
							.replace("%player%", otherplayer.getName()));
				}
			}
			return components.addAllComponents(tc);
		case OTHER_PLAYERNAME_WITH_SUFFIXHIGHCOLORCODE:
			if(otherplayer == null)
			{
				tc = ChatApi.tc("");
			} else
			{
				if(othersuffixs.size() > 0)
				{
					tc = ChatApi.apiChat(othersuffixs.get(0).getInChatColorCode()+otherplayer.getName(),
							ClickEvent.Action.SUGGEST_COMMAND, PluginSettings.settings.getCommands(KeyHandler.MSG)+otherplayer.getName(),
							HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
							.replace("%player%", otherplayer.getName()));
				} else
				{
					tc = ChatApi.apiChat(otherplayer.getName(),
							ClickEvent.Action.SUGGEST_COMMAND, PluginSettings.settings.getCommands(KeyHandler.MSG)+otherplayer.getName(),
							HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
							.replace("%player%", otherplayer.getName()));
				}
			}
			return components.addAllComponents(tc);
		case OTHER_PREFIXALL:
			if(otherplayer == null)
			{
				tc = ChatApi.tc("");
			} else
			{
				ArrayList<BaseComponent> bc = new ArrayList<>();
				for(ChatTitle ctp : prefixs)
				{
					bc.add(ChatApi.hoverEvent(ctp.getInChatName(), HoverEvent.Action.SHOW_TEXT, ctp.getHover()));
				}
				tc = ChatApi.tc("");
				tc.setExtra(bc);
			}
			return components.addAllComponents(tc);
		case OTHER_PREFIXHIGH:
			if(otherplayer == null)
			{
				tc = ChatApi.tc("");
			} else
			{
				tc = ChatApi.tc("");
				if(prefixs.size() > 0)
				{
					ChatTitle ct = prefixs.get(0);
					tc = ChatApi.hoverEvent(ct.getInChatName(), HoverEvent.Action.SHOW_TEXT, ct.getHover());
				}
			}
			return components.addAllComponents(tc);
		case OTHER_PREFIXLOW:
			if(otherplayer == null)
			{
				tc = ChatApi.tc("");
			} else
			{
				tc = ChatApi.tc("");
				if(prefixs.size() > 0)
				{
					ChatTitle ct = prefixs.get(prefixs.size()-1);
					tc = ChatApi.hoverEvent(ct.getInChatName(), HoverEvent.Action.SHOW_TEXT, ct.getHover());
				}
			}
			return components.addAllComponents(tc);
		case OTHER_SUFFIXALL:
			if(otherplayer == null)
			{
				tc = ChatApi.tc("");
			} else
			{
				ArrayList<BaseComponent> bc = new ArrayList<>();
				for(ChatTitle cts : suffixs)
				{
					bc.add(ChatApi.hoverEvent(cts.getInChatName(), HoverEvent.Action.SHOW_TEXT, cts.getHover()));
				}
				tc = ChatApi.tc("");
				tc.setExtra(bc);
			}
			return components.addAllComponents(tc);
		case OTHER_SUFFIXHIGH:
			if(otherplayer == null)
			{
				tc = ChatApi.tc("");
			} else
			{
				tc = ChatApi.tc("");
				if(suffixs.size() > 0)
				{
					ChatTitle ct = suffixs.get(0);
					tc = ChatApi.hoverEvent(ct.getInChatName(), HoverEvent.Action.SHOW_TEXT, ct.getHover());
				}
			}
			return components.addAllComponents(tc);
		case OTHER_SUFFIXLOW:
			if(otherplayer == null)
			{
				tc = ChatApi.tc("");
			} else
			{
				tc = ChatApi.tc("");
				if(prefixs.size() > 0)
				{
					ChatTitle ct = suffixs.get(suffixs.size()-1);
					tc = ChatApi.hoverEvent(ct.getInChatName(), HoverEvent.Action.SHOW_TEXT, ct.getHover());
				}
			}
			return components.addAllComponents(tc);
		case TIME:
			tc = ChatApi.tctl(TimeHandler.getTime(System.currentTimeMillis()));
			return components.addAllComponents(tc);
		case TIMES:
			tc = ChatApi.tctl(TimeHandler.getTimes(System.currentTimeMillis()));
			return components.addAllComponents(tc);
		case UNDEFINE:
			//ADDME PlaceHolderAPI?
			break;
		}
		tc = ChatApi.tc("");
		return components.addAllComponents(tc);
	}
	
	/*
	 * FIXME Checke ob funktionen in Farbe gehen, wahrscheinlich aber nicht.
	 */
	@SuppressWarnings("deprecation")
	public Components getMessageParser(CommandSender players, String message, Channel usedChannel, String channelColor)
	{
		boolean isNotConsole = false;
		Player player = null;
		if(player instanceof Player)
		{
			player = (Player) players;
			isNotConsole = true;
		}
		Components components = new Components();
		String[] function = message.split(" ");
		int count = -1;
		for(String f : function)
		{
			++count;
			if(f.startsWith(plugin.getYamlHandler().getConfig().getString("ChatReplacer.Item.Start"))
					&& f.endsWith(plugin.getYamlHandler().getConfig().getString("ChatReplacer.Item.End")))
			{
				if((usedChannel.isUseItemReplacer() && player.hasPermission(BypassPermission.USE_ITEM))
					|| player.hasPermission(BypassPermission.USE_ITEM_BYPASS))
				{
					/*
					 * Substring the start and end from the itemname.
					 */
					String itemname = f.substring(plugin.getYamlHandler().getConfig().getString("ChatReplacer.Item.Start").length(),
							f.length()-plugin.getYamlHandler().getConfig().getString("ChatReplacer.Item.End").length());
					String owner = "server";
					if(isNotConsole)
					{
						owner = player.getUniqueId().toString();
					}
					if(f.contains(plugin.getYamlHandler().getConfig().getString("ChatReplacer.Item.Seperator")))
					{
						/*
						 * ChatItemReplacer from the Database, with a specified Name.
						 */
						itemname = itemname.substring(plugin.getYamlHandler().getConfig().getString("ChatReplacer.Item.Seperator").length());
					} else
					{
						/*
						 * ChatItemReplacer from the Database, with null name. Aka default.
						 */
						itemname = "default";
					}
					ItemJson ij = (ItemJson) plugin.getMysqlHandler().getData(Type.ITEMJSON, "`owner` = ? AND `itemname` = ?",
							owner, itemname);
					TextComponent tc = null;
					if(ij == null)
					{
						tc = ChatApi.hoverEvent("&r"+itemname,
								HoverEvent.Action.SHOW_TEXT, 
								plugin.getYamlHandler().getLang().getString("ChatListener.ItemIsMissing"));
					} else
					{
						tc = ChatApi.tctl(ij.getItemDisplayName());
						tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, 
										new BaseComponent[]{new TextComponent(ij.getJsonString())}));
					}
					components.addAllComponents(tc);
					continue;
				}
			} else if(f.startsWith(plugin.getYamlHandler().getConfig().getString("ChatReplacer.Book.Start"))
					&& f.endsWith(plugin.getYamlHandler().getConfig().getString("ChatReplacer.Book.End")))
			{
				if((usedChannel.isUseBookReplacer() && player.hasPermission(BypassPermission.USE_ITEM))
					|| player.hasPermission(BypassPermission.USE_BOOK_BYPASS))
				{
					/*
					 * Substring the start and end from the itemname.
					 */
					String itemname = f.substring(plugin.getYamlHandler().getConfig().getString("ChatReplacer.Book.Start").length(),
							f.length()-plugin.getYamlHandler().getConfig().getString("ChatReplacer.Book.End").length());
					String owner = "server";
					if(isNotConsole)
					{
						owner = player.getUniqueId().toString();
					}
					if(f.contains(plugin.getYamlHandler().getConfig().getString("ChatReplacer.Book.Seperator")))
					{
						/*
						 * ChatItemReplacer from the Database, with a specified Name.
						 */
						itemname = itemname.substring(plugin.getYamlHandler().getConfig().getString("ChatReplacer.Book.Seperator").length());
					} else
					{
						/*
						 * ChatItemReplacer from the Database, with null name. Aka default.
						 */
						itemname = "default";
					}
					ItemJson ij = (ItemJson) plugin.getMysqlHandler().getData(Type.ITEMJSON, "`owner` = ? AND `itemname` = ?",
							owner, itemname);
					TextComponent tc = null;
					if(ij == null)
					{
						tc = ChatApi.hoverEvent("&r"+itemname,
								HoverEvent.Action.SHOW_TEXT, 
								plugin.getYamlHandler().getLang().getString("ChatListener.ItemIsMissing"));
					} else
					{
						tc = ChatApi.tctl(ij.getItemDisplayName());
						tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
								PluginSettings.settings.getCommands(KeyHandler.SCC_BOOK)+itemname+" "+owner));
						tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, 
										new BaseComponent[]{new TextComponent(ij.getJsonString())}));
					}
					components.addAllComponents(tc);
					continue;
				}
			} else if(f.startsWith(plugin.getYamlHandler().getConfig()
					.getString("ChatReplacer.Command.RunCommandStart")))
			{
				/*
				 * Run Commands
				 */
				if((usedChannel.isUseRunCommandReplacer() && player.hasPermission(BypassPermission.USE_RUNCOMMAND))
						|| player.hasPermission(BypassPermission.USE_RUNCOMMAND_BYPASS))
				{
					String start = plugin.getYamlHandler().getConfig().getString("ChatReplacer.Command.RunCommandStart");
					String spacereplacer = plugin.getYamlHandler().getConfig().getString("ChatReplacer.Command.SpaceReplacer");
					String cmd = f.replace(start, "/").replace(spacereplacer, " ");
					String textstart = plugin.getYamlHandler().getConfig().getString("ChatReplacer.Command.CommandStartReplacer");
					String textend = plugin.getYamlHandler().getConfig().getString("ChatReplacer.Command.CommandEndReplacer");
					String text = f.replace(start, textstart).replace(spacereplacer, " ")+textend;
					TextComponent tc = ChatApi.apiChat(text,
							ClickEvent.Action.RUN_COMMAND,
							ChatColor.stripColor(cmd),
							HoverEvent.Action.SHOW_TEXT,
							plugin.getYamlHandler().getConfig().getString("ChatListener.CommandRunHover"));
					components.addAllComponents(tc);
					continue;
				}				
			} else if(f.startsWith(plugin.getYamlHandler().getConfig().getString("ChatReplacer.Command.SuggestCommandStart")))
			{
				/*
				 * Suggest Command
				 */
				if((usedChannel.isUseSuggestCommandReplacer() && player.hasPermission(BypassPermission.USE_SUGGESTCOMMAND))
						|| player.hasPermission(BypassPermission.USE_SUGGESTCOMMAND_BYPASS))
				{
					String start = plugin.getYamlHandler().getConfig().getString("ChatReplacer.Command.SuggestCommandStart");
					String spacereplacer = plugin.getYamlHandler().getConfig().getString("ChatReplacer.Command.SpaceReplacer");
					String cmd = f.replace(start, "/").replace(spacereplacer, " ");
					String textstart = plugin.getYamlHandler().getConfig().getString("ChatReplacer.Command.CommandStartReplacer");
					String textend = plugin.getYamlHandler().getConfig().getString("ChatReplacer.Command.CommandEndReplacer");
					String text = f.replace(start, textstart).replace(spacereplacer, " ")+textend;
					TextComponent tc = ChatApi.apiChat(text,
							ClickEvent.Action.SUGGEST_COMMAND,
							ChatColor.stripColor(cmd),
							HoverEvent.Action.SHOW_TEXT,
							plugin.getYamlHandler().getConfig().getString("ChatListener.CommandSuggestHover"));
					components.addAllComponents(tc);
					continue;
				}
			} else if(f.startsWith("http") || f.endsWith(".de") || f.endsWith(".com") || f.endsWith(".net")
					|| f.endsWith(".au") || f.endsWith(".io") || f.endsWith(".be"))
			{
				/*
				 * Website
				 */
				if((usedChannel.isUseWebsiteReplacer() && player.hasPermission(BypassPermission.USE_WEBSITE))
						|| player.hasPermission(BypassPermission.USE_WEBSITE_BYPASS))
				{
					TextComponent tc = ChatApi.apiChat(plugin.getYamlHandler().getLang().getString("ChatListener.Website.Replacer"),
							ClickEvent.Action.OPEN_URL, 
							ChatColor.stripColor(f),
							HoverEvent.Action.SHOW_TEXT, 
							plugin.getYamlHandler().getLang().getString("ChatListener.Website.Hover")
							+ChatColor.stripColor(f));
					components.addAllComponents(tc);
				} else
				{
					TextComponent tc = ChatApi.apiChat(plugin.getYamlHandler().getLang().getString("ChatListener.Website.NotAllowReplacer"),
							ClickEvent.Action.OPEN_URL, 
							ChatColor.stripColor(f),
							HoverEvent.Action.SHOW_TEXT, 
							plugin.getYamlHandler().getLang().getString("ChatListener.Website.NotAllowHover"));
					components.addAllComponents(tc);
				}
				continue;
			} else if(f.startsWith(plugin.getYamlHandler().getConfig()
					.getString("ChatReplacer.Emoji.Start"))
					&& f.endsWith(plugin.getYamlHandler().getConfig()
							.getString("ChatReplacer.Emoji.End")))
			{
				if((usedChannel.isUseEmojiReplacer() && player.hasPermission(BypassPermission.USE_EMOJI))
						|| player.hasPermission(BypassPermission.USE_EMOJI_BYPASS))
				{
					String emoji = f;
					if(emojiList.containsKey(f))
					{
						emoji = emojiList.get(f);
					}
					TextComponent tc = ChatApi.tctl(emoji);
					components.addAllComponents(tc);
					continue;
				}				
			} else if(f.startsWith(plugin.getYamlHandler().getConfig()
					.getString("ChatReplacer.Mention.Start")))
			{
				/*
				 * Player mention with Start and End
				 * Playermention not available for private msg.
				 */
				if((usedChannel.isUseMentionReplacer() && player.hasPermission(BypassPermission.USE_MENTION))
						|| player.hasPermission(BypassPermission.USE_MENTION_BYPASS))
				{
					boolean hasFindOne = false;
					String name = f.substring(
							plugin.getYamlHandler().getConfig().getString("ChatReplacer.Mention.Start").length());
					for(Player mentioned : plugin.getServer().getOnlinePlayers())
					{
						if(mentioned.getName().toLowerCase().startsWith(name.toLowerCase()))
						{
							name = mentioned.getName();
							hasFindOne = true;
							break;
						}
					}
					if(!hasFindOne)
					{
						name = f;
					}
					TextComponent tc = ChatApi.hoverEvent(
							plugin.getYamlHandler().getConfig().getString("ChatReplacer.Mention.Color")+name,
							HoverEvent.Action.SHOW_TEXT,
							plugin.getYamlHandler().getLang().getString("ChatListener.Mention.YouAreMentionHover")
							.replace("%player%", player.getName()));
					components.addAllComponents(tc).addMention(name);
					continue;
				}
			} else if(isNotConsole && f.equalsIgnoreCase(plugin.getYamlHandler().getConfig().getString("ChatReplacer.PositionReplacer")))
			{
				if((usedChannel.isUsePositionReplacer() && player.hasPermission(BypassPermission.USE_POSITION))
						|| player.hasPermission(BypassPermission.USE_POSITION_BYPASS))
				{
					if(ChatListener.playerLocation.containsKey(player.getUniqueId().toString()))
					{
						ServerLocation sl = ChatListener.playerLocation.get(player.getUniqueId().toString());
						TextComponent tc = ChatApi.tctl(plugin.getYamlHandler().getConfig().getString("ChatReplacer.Position.Replace")
								.replace("%server%", sl.getServer())
								.replace("%world%", sl.getWordName()
								.replace("%x%", String.valueOf((int) sl.getX()))
								.replace("%y%", String.valueOf((int) sl.getY()))
								.replace("%z%", String.valueOf((int) sl.getZ()))));
						components.addAllComponents(tc);
						continue;
					}
				}
			}
			/*
			 * Color Handling, if nothing is correct
			 */
			boolean canColor = false;
			if((usedChannel.isUseColor() && player.hasPermission(BypassPermission.USE_COLOR))
					|| player.hasPermission(BypassPermission.USE_COLOR_BYPASS))
			{
				canColor = true;
			}
			TextComponent tc = ChatApi.tctl(getColoredString(f, channelColor, canColor));
			components.addAllComponents(tc);
			if(count < function.length)
			{
				TextComponent tc2 = ChatApi.tc(" ");
				components.addAllComponents(tc2);
			}
		}
		return components;
	}
	
	//FIXME Geht das überhaupt so auf?
	private String getColoredString(String string, String channelColor, boolean canColor)
	{
		String s = "";
		String lastColor = "";
		int i = 0;
		while(i < string.length())
		{
			char c = string.charAt(i);
			if(c == '&')
			{
				if(string.length() > (i+1))
				{
					char c2 = string.charAt(i+1);
					if(c2 == '#')
					{
						if(string.length() > i+7 && canColor)
						{
							lastColor += c+c2
									+string.charAt(i+2)+string.charAt(i+3)
									+string.charAt(i+4)+string.charAt(i+5)
									+string.charAt(i+6)+string.charAt(i+7);
							s += lastColor;
							i = i+8;
							continue;
						} else
						{
							s += c;
							i = i+2;
							continue;
						}
					} else if(isColor(c2) && canColor)
					{
						lastColor += c+c2;
						s += lastColor;
						i++;
						continue;
					}
				} else
				{
					s += c;
					i++;
					continue;
				}
			} else if(c == ' ')
			{
				if(canColor)
				{
					s+= " "+lastColor;
					i++;
					continue;
				}
				s += " "+channelColor;
				i++;
				continue;
			} else
			{
				s += c;
				i++;
				continue;
			}
		}
		return s;
	}
	
	private boolean isColor(char c)
	{
		if(c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9' 
				 || c == 'a' || c == 'A' || c == 'b' || c == 'B' || c == 'c' || c == 'C' || c == 'd' || c == 'D' || c == 'e' || c == 'E'
				 || c == 'f' || c == 'F' || c == 'k' || c == 'K' || c == 'm' || c == 'M' || c == 'n' || c == 'N' || c == 'o' || c == 'O'
				 || c == 'r' || c == 'R')
		{
			return true;
		}
		return false;
	}
	
	private boolean isSameWorld(Player p1, Player p2)
	{
		if(!ChatListener.playerLocation.containsKey(p1.getUniqueId().toString())
				|| !ChatListener.playerLocation.containsKey(p2.getUniqueId().toString()))
		{
			return false;
		}
		ServerLocation l1 = ChatListener.playerLocation.get(p1.getUniqueId().toString());
		ServerLocation l2 = ChatListener.playerLocation.get(p2.getUniqueId().toString());
		if(l1.getServer().equals(l2.getServer())
				&& l1.getWordName().equals(l2.getWordName()))
		{
			return true;
		}
		return false;
	}
	
	private double absoluteValue(double d)
	{
		if(d < 0)
		{
			return -1*d;
		}
		return d;
	}
	
	private boolean isInRadius(Player p1, Player p2, int blockRadius)
	{
		if(!ChatListener.playerLocation.containsKey(p1.getUniqueId().toString())
				|| !ChatListener.playerLocation.containsKey(p2.getUniqueId().toString()))
		{
			return false;
		}
		ServerLocation l1 = ChatListener.playerLocation.get(p1.getUniqueId().toString());
		ServerLocation l2 = ChatListener.playerLocation.get(p2.getUniqueId().toString());
		if(l1.getServer().equals(l2.getServer())
				&& l1.getWordName().equals(l2.getWordName()))
		{
			double x = Math.max(absoluteValue(l1.getX()), absoluteValue(l2.getX()))-Math.min(absoluteValue(l1.getX()), absoluteValue(l2.getX()));
			double y = Math.max(absoluteValue(l1.getY()), absoluteValue(l2.getY()))-Math.min(absoluteValue(l1.getY()), absoluteValue(l2.getY()));
			double z = Math.max(absoluteValue(l1.getZ()), absoluteValue(l2.getZ()))-Math.min(absoluteValue(l1.getZ()), absoluteValue(l2.getZ()));
			if(x <= blockRadius && y <= blockRadius && z <= blockRadius)
			{
				return true;
			}
		}
		return false;
	}
	
	private void sendMessage(Components components, Player player, Channel usedChannel, TemporaryChannel tch, PermanentChannel pc)
	{
		TextComponent txc1 = ChatApi.tc("");
		txc1.setExtra(components.getComponents());
		TextComponent txc2 = ChatApi.tc("");
		txc2.setExtra(components.getComponentsWithMentions());
		ArrayList<String> sendedPlayer = new ArrayList<>();
		for(Player toMessage : plugin.getServer().getOnlinePlayers())
		{
			/*
			 * Ignore the toMessage-Player the message sender?
			 */
			if(!toMessage.getUniqueId().toString().equals(player.getUniqueId().toString())
					&& plugin.getUtility().getIgnored(toMessage, player, false))
			{
				continue;
			}
			if(tch != null)
			{
				/*
				 * If tc exist, is the player a member
				 */
				if(!tch.getMembers().contains(toMessage))
				{
					continue;
				}
			} else if(pc != null)
			{
				/*
				 * if pc exist, is the player a member
				 */
				if(!pc.getMembers().contains(toMessage.getUniqueId().toString()))
				{
					continue;
				}
			}
			/*
			 * Use the player the channel?
			 */
			LinkedHashMap<String, UsedChannel> uclist = Utility.playerUsedChannels.get(toMessage.getUniqueId().toString());
			if(uclist == null
					|| uclist.get(usedChannel.getUniqueIdentifierName()) == null
					|| !uclist.get(usedChannel.getUniqueIdentifierName()).isUsed())
			{
				continue;
			}
			if(usedChannel.isSpecificWorld())
			{
				if(!isSameWorld(player, toMessage))
				{
					continue;
				}
			} else if(usedChannel.getBlockRadius() > 0)
			{
				if(!isInRadius(player, toMessage, usedChannel.getBlockRadius()))
				{
					continue;
				}
			}
			if(components.isMention(player.getName()))
			{
				toMessage.spigot().sendMessage(txc2);
				if(toMessage.hasPermission(BypassPermission.USE_SOUND))
				{
					sendMentionPing(toMessage);
				}
			} else
			{
				toMessage.spigot().sendMessage(txc1);
			}
			sendedPlayer.add(toMessage.getUniqueId().toString());
		}
		spy(sendedPlayer, components, txc1, txc2);
	}
	
	private void sendPrivateMessage(Components components, Player player, Player other, Channel usedChannel)
	{
		TextComponent txc1 = ChatApi.tc("");
		txc1.setExtra(components.getComponents());
		TextComponent txc2 = ChatApi.tc("");
		txc2.setExtra(components.getComponentsWithMentions());
		ArrayList<String> sendedPlayer = new ArrayList<>();
		/*
		 * Use the player the channel?
		 */
		LinkedHashMap<String, UsedChannel> uclist = Utility.playerUsedChannels.get(other.getUniqueId().toString());
		if(uclist == null
				|| uclist.get(usedChannel.getUniqueIdentifierName()) == null
				|| !uclist.get(usedChannel.getUniqueIdentifierName()).isUsed())
		{
			if(!player.hasPermission(BypassPermission.PERMBYPASSOFFLINECHANNEL))
			{
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("ChatListener.PlayerHasPrivateChannelOff")
						.replace("%player%", other.getName())));
				return;
			}
		}
		/*
		 * Ignore the toMessage-Player the message sender?
		 */
		boolean isIgnored = false;
		if(plugin.getUtility().getIgnored(other, player, false))
		{
			if(!player.hasPermission(BypassPermission.PERMBYPASSIGNORE))
			{
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("ChatListener.PlayerIgnoreYou")
						.replace("%player%", other.getName())));
				return;
			}
			isIgnored = true;
		}
		if(components.isMention(player.getName()))
		{
			player.spigot().sendMessage(txc2);
			
		} else
		{
			player.spigot().sendMessage(txc1);
		}
		if(player.hasPermission(BypassPermission.USE_SOUND))
		{
			sendMentionPing(player);
		}
		sendedPlayer.add(player.getUniqueId().toString());
		if(components.isMention(other.getName()))
		{
			other.spigot().sendMessage(txc2);
		} else
		{
			other.spigot().sendMessage(txc1);
		}
		if(other.hasPermission(BypassPermission.USE_SOUND))
		{
			sendMentionPing(other);
		}
		sendedPlayer.add(other.getUniqueId().toString());
		if(isIgnored)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("ChatListener.PlayerIgnoreYouButYouBypass")
					.replace("%player%", other.getName())));
		}
		sendedPlayer.add(other.getUniqueId().toString());
		spy(sendedPlayer, components, txc1, txc2);
	}
	
	private void sendPrivateConsoleMessage(Components components, CommandSender console, Player other, Channel usedChannel)
	{
		TextComponent txc1 = ChatApi.tc("");
		txc1.setExtra(components.getComponents());
		TextComponent txc2 = ChatApi.tc("");
		txc2.setExtra(components.getComponentsWithMentions());
		if(components.isMention(other.getName()))
		{
			other.spigot().sendMessage(txc2);
		} else
		{
			other.spigot().sendMessage(txc1);
		}
		if(other.hasPermission(BypassPermission.USE_SOUND))
		{
			sendMentionPing(other);
		}
	}
	
	private void spy(ArrayList<String> sendedPlayer, Components components, TextComponent txc1, TextComponent txc2)
	{
		//Spy Part
		for(Player spy : plugin.getServer().getOnlinePlayers())
		{
			ChatUser cu = ChatUserHandler.getChatUser(spy.getUniqueId());
			if(spy == null
					|| cu == null
					|| !cu.isOptionSpy()
					|| sendedPlayer.contains(cu.getUUID()))
			{
				continue;
			}
			if(components.isMention(spy.getName()))
			{
				spy.spigot().sendMessage(txc2);
			} else
			{
				spy.spigot().sendMessage(txc1);
			}
		}
	}
	
	public void sendMentionPing(Player player)
	{
		sendMentionPing(player, plugin.getYamlHandler().getConfig().getString("ChatReplacer.Mention.SoundEnum"));
	}
	
	public void sendMentionPing(Player player, String sound)
	{
		player.playSound(player.getLocation(),
				Sound.valueOf(sound), 
				0.5F,
				0.5F);
	}
}