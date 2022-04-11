package main.java.me.avankziar.scc.bungee.handler;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.scc.bungee.assistance.Utility;
import main.java.me.avankziar.scc.bungee.database.MysqlHandler.Type;
import main.java.me.avankziar.scc.bungee.listener.ChatListener;
import main.java.me.avankziar.scc.bungee.objects.BypassPermission;
import main.java.me.avankziar.scc.bungee.objects.ChatUserHandler;
import main.java.me.avankziar.scc.bungee.objects.PluginSettings;
import main.java.me.avankziar.scc.bungee.objects.chat.TemporaryChannel;
import main.java.me.avankziar.scc.handlers.ColorHandler;
import main.java.me.avankziar.scc.handlers.TimeHandler;
import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.ChatUser;
import main.java.me.avankziar.scc.objects.KeyHandler;
import main.java.me.avankziar.scc.objects.PermanentChannel;
import main.java.me.avankziar.scc.objects.ServerLocation;
import main.java.me.avankziar.scc.objects.StaticValues;
import main.java.me.avankziar.scc.objects.chat.Channel;
import main.java.me.avankziar.scc.objects.chat.ChatTitle;
import main.java.me.avankziar.scc.objects.chat.Components;
import main.java.me.avankziar.scc.objects.chat.ItemJson;
import main.java.me.avankziar.scc.objects.chat.UsedChannel;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
	
	public boolean prePreCheck(ProxiedPlayer player, String message)
	{
		/*
		 * Is player muted?
		 */
		final long mutedTime = plugin.getUtility().getMutedTime(player);
		if(mutedTime > System.currentTimeMillis())
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("ChatListener.YouAreMuted")
					.replace("%time%", TimeHandler.getDateTime(mutedTime))));
			return false;
		}
		/*
		 * Wordfilter
		 */
		if(plugin.getUtility().containsBadWords(message))
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("ChatListener.ContainsBadWords")));
			return false;
		}
		return true;
	}
	
	public void startChat(ProxiedPlayer player, Channel usedChannel, String message)
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
		
		String channelcolor = usedChannel.getInChatColorMessage();
		String msg = message;
		
		if(usedChannel.getUniqueIdentifierName().equalsIgnoreCase("Permanent"))
		{
			/*
			 * Define, whiche pc it is.
			 */
			if(!message.contains(" "))
			{
				return;
			}
			String[] space = message.split(" ");
			if(space.length < 1)
			{
				return;
			}
			if(space[0].length() > 0);
			{
				pc = PermanentChannel.getChannelFromSymbol(space[0]);
			}
			if(pc == null)
			{
				///Der permanente Channel %symbol% existiert nicht.
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("ChatListener.SymbolNotKnow")
						.replace("%symbol%", space[0])));
				return;
			}
			if(!pc.getMembers().contains(player.getUniqueId().toString()))
			{
				///Du bist in keinem Permanenten Channel!
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("ChatListener.NotAPermanentChannel")));
				return;
			}
			msg = message.substring(pc.getSymbolExtra().length()+1);
			channelcolor = pc.getChatColor();
		} else if(usedChannel.getUniqueIdentifierName().equalsIgnoreCase("Temporary"))
		{
			/*
			 * Define which tc it is.
			 */
			tc = TemporaryChannel.getCustomChannel(player);
			
			if(tc == null)
			{
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("ChatListener.NotATemporaryChannel")));
				return;
			}
		}
		Components components = getComponent(
				player, null, msg, usedChannel.getChatFormat(), userPrefix, userSuffix, usedChannel, tc, pc, null, null, 
				channelcolor);
		sendMessage(components, player, usedChannel, tc, pc);
	}
	
	public void startPrivateChat(ProxiedPlayer player, ProxiedPlayer other, String message)
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
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMsg.PrivateChannelsNotActive")));
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
	
	public void startPrivateConsoleChat(CommandSender console, ProxiedPlayer other, String message)
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
		if(!plugin.getYamlHandler().getConfig().getBoolean("PrivateChannel.UseDynamicColor", true))
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
			ArrayList<String> remainingColors = new ArrayList<>();
			for(String playernames : privateChatColorPerPlayers.keySet())
			{
				if(playernames.contains(player1) || playernames.contains(player2))
				{
					alreadyUsedColors.add(privateChatColorPerPlayers.get(playernames));
				}
			}
			for(String c : privateChatColor)
			{
				boolean unused = true;
				for(String al : alreadyUsedColors)
				{
					if(c.equals(al))
					{
						unused = false;
					}
				}
				if(unused)
				{
					remainingColors.add(c);
				}
			}
			Random r = new Random();
			if(remainingColors.isEmpty()
					&& remainingColors.size()-1 < 1)
			{
				/*
				 * If all are in use, use a random color.
				 */
				int i = r.nextInt(privateChatColor.size()-1);
				String color = privateChatColor.get(i);
				privateChatColorPerPlayers.put(player1+player2, color);
				return color;
			} else
			{
				int i = r.nextInt(remainingColors.size()-1);
				String color = remainingColors.get(i);
				privateChatColorPerPlayers.put(player1+player2, color);
				return color;
			}
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
		for(ProxiedPlayer all : plugin.getProxy().getPlayers())
		{
			if(components.isMention(all.getName()))
			{
				all.sendMessage(txc2);
				if(all.hasPermission(BypassPermission.USE_SOUND))
				{
					sendMentionPing(all, usedChannel.getMentionSound());
				}
			} else
			{
				all.sendMessage(txc1);
			}
		}
	}
	
	private void rePlayerHandling(ProxiedPlayer player, ProxiedPlayer other)
	{
		if(SimpleChatChannels.rePlayers.containsKey(player.getUniqueId().toString()))
		{
			ArrayList<String> relist = SimpleChatChannels.rePlayers.get(player.getUniqueId().toString());
			if(!relist.contains(other.getUniqueId().toString()))
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
			SimpleChatChannels.rPlayers.replace(player.getUniqueId().toString(), other.getUniqueId().toString());
		} else
		{
			SimpleChatChannels.rPlayers.put(player.getUniqueId().toString(), other.getUniqueId().toString());
		}
	}
	
	private boolean preChecks(ProxiedPlayer player, Channel usedChannel, String message)
	{
		/*
		 * Is Channel off?
		 */
		if(!Utility.playerUsedChannels.containsKey(player.getUniqueId().toString())
				|| !Utility.playerUsedChannels.get(player.getUniqueId().toString()).containsKey(usedChannel.getUniqueIdentifierName())
				|| !Utility.playerUsedChannels.get(player.getUniqueId().toString()).get(usedChannel.getUniqueIdentifierName()).isUsed())
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("ChatListener.ChannelIsOff")));
			return false;
		}
		
		/*
		 * Spam Protection Wall
		 */
		long now = System.currentTimeMillis();
		if(ChatListener.spamMap.containsKey(player.getUniqueId().toString())
				&& ChatListener.spamMapII.containsKey(player.getUniqueId().toString()))
		{
			LinkedHashMap<String, Long> map = ChatListener.spamMap.get(player.getUniqueId().toString());
			LinkedHashMap<String, String> mapII = ChatListener.spamMapII.get(player.getUniqueId().toString());
			if(map.containsKey(usedChannel.getUniqueIdentifierName())
					&& mapII.containsKey(usedChannel.getUniqueIdentifierName()))
			{
				long last = map.get(usedChannel.getUniqueIdentifierName())+usedChannel.getTimeBetweenSameMessages();
				if(plugin.getUtility().isSimliarText(
						message, mapII.get(usedChannel.getUniqueIdentifierName()), usedChannel.getPercentOfSimilarity())
						&& last > now)
				{
					player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang()
							.getString("ChatListener.PleaseWaitALittleWithSameMessage")
							.replace("%channel%", usedChannel.getInChatName())
							.replace("%time%", TimeHandler.getDateTime(last))));
					return false;
				}
				last = map.get(usedChannel.getUniqueIdentifierName())+usedChannel.getTimeBetweenMessages();
				if(last > now)
				{
					player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("ChatListener.PleaseWaitALittle")
							.replace("%channel%", usedChannel.getInChatName())
							.replace("%time%", TimeHandler.getDateTime(last))));
					return false;
				}
				LinkedHashMap<String, Long> mapIII = ChatListener.spamMap.get(player.getUniqueId().toString());
				mapIII.replace(player.getUniqueId().toString(), now);
				ChatListener.spamMap.put(player.getUniqueId().toString(), mapIII);
				LinkedHashMap<String, String> mapIV = ChatListener.spamMapII.get(player.getUniqueId().toString());
				mapIV.replace(player.getUniqueId().toString(), message);
				ChatListener.spamMapII.put(player.getUniqueId().toString(), mapIV);
			} else
			{
				LinkedHashMap<String, Long> mapIII = new LinkedHashMap<>();
				mapIII.put(usedChannel.getUniqueIdentifierName(), now);
				ChatListener.spamMap.put(player.getUniqueId().toString(), mapIII);
				LinkedHashMap<String, String> mapIV = new LinkedHashMap<>();
				mapIV.put(usedChannel.getUniqueIdentifierName(), message);
				ChatListener.spamMapII.put(player.getUniqueId().toString(), mapIV);
			}
		} else
		{
			LinkedHashMap<String, Long> map = new LinkedHashMap<>();
			map.put(usedChannel.getUniqueIdentifierName(), now+usedChannel.getTimeBetweenMessages());
			ChatListener.spamMap.put(player.getUniqueId().toString(), map);
			LinkedHashMap<String, String> mapII = new LinkedHashMap<>();
			mapII.put(usedChannel.getUniqueIdentifierName(), message);
			ChatListener.spamMapII.put(player.getUniqueId().toString(), mapII);
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
	private Components getComponent(CommandSender player, ProxiedPlayer other, String message, String chatFormt,
			ArrayList<ChatTitle> prefix, ArrayList<ChatTitle> suffix, Channel usedChannel,
			TemporaryChannel tc, PermanentChannel pc,
			ArrayList<ChatTitle> otherprefix, ArrayList<ChatTitle> othersuffix, String channelColor)
	{
		String s = "";
		Components components = new Components();
		String lastColor = "";
		int i = 0;
		while(i < chatFormt.length())
		{
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
							char c3 = chatFormt.charAt(i+2);
							char c4 = chatFormt.charAt(i+3);
							char c5 = chatFormt.charAt(i+4);
							char c6 = chatFormt.charAt(i+5);
							char c7 = chatFormt.charAt(i+6);
							char c8 = chatFormt.charAt(i+7);
							if(!isHexChar(c3, c4, c5, c6, c7, c8))
							{
								i = i+8;
								continue;
							}
							lastColor = "&#"
									+c3+c4+c5+c6+c7+c8;
							s += lastColor;
							i = i+8;
							continue;
						} else
						{
							i += 2;
							continue;
						}
					} else
					{
						lastColor = "&"+String.valueOf(c2);
						s += lastColor;
						i += 2;
						continue;
					}
				} else
				{
					s += String.valueOf(c);
					i++;
					continue;
				}
			} else if(c == '%')
			{
				if(!s.isEmpty())
				{
					components.addAllComponents(ChatApi.tctl(s));
					s = "";
				}
				int j = i+1;
				i++;
				String placeHolder = String.valueOf(c);
				while(j < chatFormt.length())
				{
					char c2 = chatFormt.charAt(j);
					placeHolder += c2;
					if(c2 == '%')
					{
						break;
					}
					j++;
					i++;
				}
				components.addAllComponents(getComponent(placeHolder, message, player,
						other, prefix, suffix, usedChannel, tc, pc, otherprefix, othersuffix, channelColor));
				i++;
				continue;
			} else if(c == ' ')
			{
				s+= " "+lastColor;
				i++;
				continue;
			} else
			{
				s += c;
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
	private Components getComponent(String placeHolder, String message, CommandSender players, ProxiedPlayer otherplayer,
			ArrayList<ChatTitle> prefixs, ArrayList<ChatTitle> suffixs,
			final Channel usedChannel,
			final TemporaryChannel tch, final PermanentChannel pc,
			ArrayList<ChatTitle> otherprefixs, ArrayList<ChatTitle> othersuffixs, String channelColor)
	{
		boolean isNotConsole = false;
		ProxiedPlayer player = null;
		if(players instanceof ProxiedPlayer)
		{
			player = (ProxiedPlayer) players;
			isNotConsole = true;
		}
		Components components = new Components();
		TextComponent tc = null;
		Channel.ChatFormatPlaceholder ph = Channel.ChatFormatPlaceholder.getEnum(placeHolder);
		switch(ph)
		{
		case CHANNEL:
			String text = plugin.getUtility().getChannelHover(usedChannel);
			tc = ChatApi.apiChat(plugin.getUtility().getChannelNameSuggestion(usedChannel, pc, tch),
					ClickEvent.Action.SUGGEST_COMMAND,
					plugin.getUtility().getChannelSuggestion(usedChannel, pc),
					HoverEvent.Action.SHOW_TEXT, 
					text);
			return components.addAllComponents(tc);
		case MESSAGE:
			return components.addAllComponents(getMessageParser(players, message, usedChannel, channelColor));
		case PLAYERNAME:
			if(isNotConsole)
			{
				tc = ChatApi.apiChat(player.getName(),
						ClickEvent.Action.SUGGEST_COMMAND, PluginSettings.settings.getCommands(KeyHandler.MSG)+player.getName()+" ",
						HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
						.replace("%player%", player.getName()));
			} else
			{
				tc = ChatApi.tctl(console);
			}
			return components.addAllComponents(tc);
		case PLAYERNAME_WITH_CUSTOMCOLOR:
			if(isNotConsole)
			{
				tc = ChatApi.apiChat(usedChannel.getPlayernameCustomColor()+player.getName(),
						ClickEvent.Action.SUGGEST_COMMAND, PluginSettings.settings.getCommands(KeyHandler.MSG)+player.getName()+" ",
						HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
						.replace("%player%", player.getName()));
			} else
			{
				tc = ChatApi.tctl(usedChannel.getPlayernameCustomColor()+console);
			}
			return components.addAllComponents(tc);
		case PLAYERNAME_WITH_PREFIXHIGHCOLORCODE:
			if(isNotConsole)
			{
				if(prefixs.size() > 0)
				{
					tc = ChatApi.apiChat(prefixs.get(0).getInChatColorCode()+player.getName(),
							ClickEvent.Action.SUGGEST_COMMAND, PluginSettings.settings.getCommands(KeyHandler.MSG)+player.getName()+" ",
							HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
							.replace("%player%", player.getName()));
				} else
				{
					tc = ChatApi.apiChat(player.getName(),
							ClickEvent.Action.SUGGEST_COMMAND, PluginSettings.settings.getCommands(KeyHandler.MSG)+player.getName()+" ",
							HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
							.replace("%player%", player.getName()));
				}
			} else
			{
				tc = ChatApi.tctl(console);
			}
			return components.addAllComponents(tc);
		case PLAYERNAME_WITH_SUFFIXHIGHCOLORCODE:
			if(isNotConsole)
			{
				if(suffixs.size() > 0)
				{
					tc = ChatApi.apiChat(suffixs.get(0).getInChatColorCode()+player.getName(),
							ClickEvent.Action.SUGGEST_COMMAND, PluginSettings.settings.getCommands(KeyHandler.MSG+player.getName())+" ",
							HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
							.replace("%player%", player.getName()));
				} else
				{
					tc = ChatApi.apiChat(player.getName(),
							ClickEvent.Action.SUGGEST_COMMAND, PluginSettings.settings.getCommands(KeyHandler.MSG)+player.getName()+" ",
							HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
							.replace("%player%", player.getName()));
				}
			} else
			{
				tc = ChatApi.tctl(console);
			}
			return components.addAllComponents(tc);
		case PREFIXALL:
			if(isNotConsole)
			{
				ArrayList<BaseComponent> bc = new ArrayList<>();
				int i = 0;
				int last = prefixs.size()-1;
				for(ChatTitle cts : prefixs)
				{
					if(i < last)
					{
						bc.add(ChatApi.apiChat(cts.getInChatName()+usedChannel.getSeperatorBetweenSuffix(),
								ClickEvent.Action.SUGGEST_COMMAND, cts.getClick(),
								HoverEvent.Action.SHOW_TEXT, cts.getHover()));
					} else
					{
						bc.add(ChatApi.apiChat(cts.getInChatName(),
								ClickEvent.Action.SUGGEST_COMMAND, cts.getClick(),
								HoverEvent.Action.SHOW_TEXT, cts.getHover()));
					}
					
					i++;
				}
				tc = ChatApi.tc("");
				if(bc.size() > 0)
				{
					tc.setExtra(bc);
				}
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
					tc = ChatApi.apiChat(ct.getInChatName(),
							ClickEvent.Action.SUGGEST_COMMAND, ct.getClick(),
							HoverEvent.Action.SHOW_TEXT, ct.getHover());
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
					tc = ChatApi.apiChat(ct.getInChatName(),
							ClickEvent.Action.SUGGEST_COMMAND, ct.getClick(),
							HoverEvent.Action.SHOW_TEXT, ct.getHover());
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
				int i = 0;
				int last = suffixs.size()-1;
				for(ChatTitle cts : suffixs)
				{
					if(i < last)
					{
						bc.add(ChatApi.apiChat(cts.getInChatName()+usedChannel.getSeperatorBetweenSuffix(), 
								ClickEvent.Action.SUGGEST_COMMAND, cts.getClick(),
								HoverEvent.Action.SHOW_TEXT, cts.getHover()));
					} else
					{
						bc.add(ChatApi.apiChat(cts.getInChatName(),
								ClickEvent.Action.SUGGEST_COMMAND, cts.getClick(),
								HoverEvent.Action.SHOW_TEXT, cts.getHover()));
					}
					
					i++;
				}
				tc = ChatApi.tc("");
				if(bc.size() > 0)
				{
					tc.setExtra(bc);
				}
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
					tc = ChatApi.apiChat(ct.getInChatName(),
							ClickEvent.Action.SUGGEST_COMMAND, ct.getClick(),
							HoverEvent.Action.SHOW_TEXT, ct.getHover());
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
					tc = ChatApi.apiChat(ct.getInChatName(),
							ClickEvent.Action.SUGGEST_COMMAND, ct.getClick(),
							HoverEvent.Action.SHOW_TEXT, ct.getHover());
				}
			} else
			{
				tc = ChatApi.tc("");
			}
			return components.addAllComponents(tc);
		case ROLEPLAYNAME:
			if(isNotConsole)
			{
				ChatUser cu = ChatUserHandler.getChatUser(player.getUniqueId());
				String name = (cu != null) ? cu.getRolePlayName() : player.getName();
				tc = ChatApi.apiChat(name,
						ClickEvent.Action.SUGGEST_COMMAND, PluginSettings.settings.getCommands(KeyHandler.MSG)+player.getName()+" ",
						HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
						.replace("%player%", player.getName()));
			} else
			{
				tc = ChatApi.tctl(console);
			}
			return components.addAllComponents(tc);
		case OTHER_PLAYERNAME:
			if(otherplayer == null)
			{
				tc = ChatApi.tc("");
			} else
			{
				tc = ChatApi.apiChat(otherplayer.getName(),
						ClickEvent.Action.SUGGEST_COMMAND, PluginSettings.settings.getCommands(KeyHandler.MSG)+otherplayer.getName()+" ",
						HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
						.replace("%player%", otherplayer.getName()));
			}
			return components.addAllComponents(tc);
		case OTHER_PLAYERNAME_WITH_CUSTOMCOLOR:
			if(otherplayer == null)
			{
				tc = ChatApi.tc("");
			} else
			{
				tc = ChatApi.apiChat(usedChannel.getOtherplayernameCustomColor()+otherplayer.getName(),
						ClickEvent.Action.SUGGEST_COMMAND, PluginSettings.settings.getCommands(KeyHandler.MSG)+otherplayer.getName()+" ",
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
							ClickEvent.Action.SUGGEST_COMMAND, PluginSettings.settings.getCommands(KeyHandler.MSG)+otherplayer.getName()+" ",
							HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
							.replace("%player%", otherplayer.getName()));
				} else
				{
					tc = ChatApi.apiChat(otherplayer.getName(),
							ClickEvent.Action.SUGGEST_COMMAND, PluginSettings.settings.getCommands(KeyHandler.MSG)+otherplayer.getName()+" ",
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
							ClickEvent.Action.SUGGEST_COMMAND, PluginSettings.settings.getCommands(KeyHandler.MSG)+otherplayer.getName()+" ",
							HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
							.replace("%player%", otherplayer.getName()));
				} else
				{
					tc = ChatApi.apiChat(otherplayer.getName(),
							ClickEvent.Action.SUGGEST_COMMAND, PluginSettings.settings.getCommands(KeyHandler.MSG)+otherplayer.getName()+" ",
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
					bc.add(ChatApi.apiChat(ctp.getInChatName(),
							ClickEvent.Action.SUGGEST_COMMAND, ctp.getClick(),
							HoverEvent.Action.SHOW_TEXT, ctp.getHover()));
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
					tc = ChatApi.apiChat(ct.getInChatName(),
							ClickEvent.Action.SUGGEST_COMMAND, ct.getClick(),
							HoverEvent.Action.SHOW_TEXT, ct.getHover());
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
					tc = ChatApi.apiChat(ct.getInChatName(),
							ClickEvent.Action.SUGGEST_COMMAND, ct.getClick(),
							HoverEvent.Action.SHOW_TEXT, ct.getHover());
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
					bc.add(ChatApi.apiChat(cts.getInChatName(),
							ClickEvent.Action.SUGGEST_COMMAND, cts.getClick(),
							HoverEvent.Action.SHOW_TEXT, cts.getHover()));
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
					tc = ChatApi.apiChat(ct.getInChatName(),
							ClickEvent.Action.SUGGEST_COMMAND, ct.getClick(),
							HoverEvent.Action.SHOW_TEXT, ct.getHover());
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
					tc = ChatApi.apiChat(ct.getInChatName(),
							ClickEvent.Action.SUGGEST_COMMAND, ct.getClick(),
							HoverEvent.Action.SHOW_TEXT, ct.getHover());
				}
			}
			return components.addAllComponents(tc);
		case OTHER_ROLEPLAYNAME:
			if(otherplayer == null)
			{
				tc = ChatApi.tc("");
			} else
			{
				ChatUser cu = ChatUserHandler.getChatUser(otherplayer.getUniqueId());
				String name = (cu != null) ? cu.getRolePlayName() : otherplayer.getName();
				tc = ChatApi.apiChat(name,
						ClickEvent.Action.SUGGEST_COMMAND, PluginSettings.settings.getCommands(KeyHandler.MSG)+otherplayer.getName()+" ",
						HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
						.replace("%player%", otherplayer.getName()));
			}
			return components.addAllComponents(tc);
		case NEWLINE:
			return components.addAllComponents(ChatApi.newLine());
		case SERVER:
			String server = "";
			String cmd = "";
			String hover = "";
			if(isNotConsole)
			{
				server = player.getServer().getInfo().getName();
			} else
			{
				server = "proxy";
			}
			String serverReplacer = "";
			if(usedChannel.getServerReplacerMap().containsKey(server))
			{
				serverReplacer = usedChannel.getServerReplacerMap().get(server);
			}
			if(usedChannel.getServerCommandMap().containsKey(server))
			{
				cmd = usedChannel.getServerCommandMap().get(server);
			}
			if(usedChannel.getServerHoverMap().containsKey(server))
			{
				hover = usedChannel.getServerHoverMap().get(server);
			}
			tc = ChatApi.apiChat(serverReplacer,
					ClickEvent.Action.SUGGEST_COMMAND, cmd,
					HoverEvent.Action.SHOW_TEXT, hover);
			return components.addAllComponents(tc);
		case OTHER_SERVER:
			
			if(otherplayer == null)
			{
				tc = ChatApi.tc("");
				return components.addAllComponents(tc);
			} else
			{
				server = "proxy";
			}
			server = "";
			cmd = "";
			hover = "";
			serverReplacer = "";
			if(usedChannel.getServerReplacerMap().containsKey(server))
			{
				serverReplacer = usedChannel.getServerReplacerMap().get(server);
			}
			if(usedChannel.getServerCommandMap().containsKey(server))
			{
				cmd = usedChannel.getServerCommandMap().get(server);
			}
			if(usedChannel.getServerHoverMap().containsKey(server))
			{
				hover = usedChannel.getServerHoverMap().get(server);
			}
			tc = ChatApi.apiChat(serverReplacer,
					ClickEvent.Action.SUGGEST_COMMAND, cmd,
					HoverEvent.Action.SHOW_TEXT, hover);
			return components.addAllComponents(tc);
		case WORLD:
			if(isNotConsole)
			{
				String world = "";
				if(!ChatListener.playerLocation.containsKey(player.getUniqueId().toString()))
				{
					world = "not found";
				}
				world = ChatListener.playerLocation.get(player.getUniqueId().toString()).getWordName();
				String worldReplacer = "";
				cmd = "";
				hover = "";
				if(usedChannel.getWorldReplacerMap().containsKey(world))
				{
					serverReplacer = usedChannel.getWorldReplacerMap().get(world);
				}
				if(usedChannel.getWorldCommandMap().containsKey(world))
				{
					cmd = usedChannel.getWorldCommandMap().get(world);
				}
				if(usedChannel.getWorldHoverMap().containsKey(world))
				{
					hover = usedChannel.getWorldHoverMap().get(world);
				}
				tc = ChatApi.apiChat(worldReplacer,
						ClickEvent.Action.SUGGEST_COMMAND, cmd,
						HoverEvent.Action.SHOW_TEXT, hover);
				return components.addAllComponents(tc);
			} else
			{
				tc = ChatApi.tc("");
				return components.addAllComponents(tc);
			}
		case OTHER_WORLD:
			if(otherplayer != null)
			{
				String world = "";
				if(!ChatListener.playerLocation.containsKey(player.getUniqueId().toString()))
				{
					world = "not found";
				}
				world = ChatListener.playerLocation.get(player.getUniqueId().toString()).getWordName();
				String worldReplacer = "";
				cmd = "";
				hover = "";
				if(usedChannel.getWorldReplacerMap().containsKey(world))
				{
					serverReplacer = usedChannel.getWorldReplacerMap().get(world);
				}
				if(usedChannel.getWorldCommandMap().containsKey(world))
				{
					cmd = usedChannel.getWorldCommandMap().get(world);
				}
				if(usedChannel.getWorldHoverMap().containsKey(world))
				{
					hover = usedChannel.getWorldHoverMap().get(world);
				}
				tc = ChatApi.apiChat(worldReplacer,
						ClickEvent.Action.SUGGEST_COMMAND, cmd,
						HoverEvent.Action.SHOW_TEXT, hover);
				return components.addAllComponents(tc);
			} else
			{
				tc = ChatApi.tc("");
				return components.addAllComponents(tc);
			}
		case TIME:
			tc = ChatApi.tctl(usedChannel.getTimeColor()+TimeHandler.getTime(System.currentTimeMillis()));
			return components.addAllComponents(tc);
		case TIMES:
			tc = ChatApi.tctl(usedChannel.getTimeColor()+TimeHandler.getTimes(System.currentTimeMillis()));
			return components.addAllComponents(tc);
		case UNDEFINE:
			//ADDME PlaceHolderAPI?
			break;
		}
		tc = ChatApi.tc("");
		return components.addAllComponents(tc);
	}
	
	@SuppressWarnings("deprecation")
	public Components getMessageParser(CommandSender players, String message, Channel usedChannel, String channelColor)
	{
		boolean isNotConsole = false;
		ProxiedPlayer player = null;
		if(players instanceof ProxiedPlayer)
		{
			player = (ProxiedPlayer) players;
			isNotConsole = true;
		}
		Components components = new Components();
		String[] function = message.split(" ");
		int count = -1;
		int newlineCounter = 0;
		String lastColor = channelColor;
		for(String f : function)
		{
			++count;
			if(f.startsWith(plugin.getYamlHandler().getConfig().getString("ChatReplacer.Item.Start"))
					&& f.endsWith(plugin.getYamlHandler().getConfig().getString("ChatReplacer.Item.End")))
			{
				if(!isNotConsole || (usedChannel.isUseItemReplacer() && player.hasPermission(BypassPermission.USE_ITEM))
					|| player.hasPermission(BypassPermission.USE_ITEM_BYPASS))
				{
					/*
					 * Substring the start and end from the itemname.
					 */
					String itemname = f.substring(plugin.getYamlHandler().getConfig().getString("ChatReplacer.Item.Start").length(),
							f.length()-plugin.getYamlHandler().getConfig().getString("ChatReplacer.Item.End").length());
					String owner = "sv"; //server
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
					if(count < function.length)
					{
						TextComponent tc2 = ChatApi.tc(" ");
						components.addAllComponents(tc2);
					}
					continue;
				}
			} else if(f.startsWith(plugin.getYamlHandler().getConfig().getString("ChatReplacer.Book.Start"))
					&& f.endsWith(plugin.getYamlHandler().getConfig().getString("ChatReplacer.Book.End")))
			{
				if(!isNotConsole || (usedChannel.isUseBookReplacer() && player.hasPermission(BypassPermission.USE_BOOK))
					|| player.hasPermission(BypassPermission.USE_BOOK_BYPASS))
				{
					/*
					 * Substring the start and end from the itemname.
					 */
					String itemname = f.substring(plugin.getYamlHandler().getConfig().getString("ChatReplacer.Book.Start").length(),
							f.length()-plugin.getYamlHandler().getConfig().getString("ChatReplacer.Book.End").length());
					String owner = "server";
					String own = "server";
					if(isNotConsole)
					{
						owner = player.getUniqueId().toString();
						own = Utility.convertUUIDToName(owner);
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
					if(ij == null || own == null)
					{
						tc = ChatApi.hoverEvent("&r"+itemname,
								HoverEvent.Action.SHOW_TEXT, 
								plugin.getYamlHandler().getLang().getString("ChatListener.ItemIsMissing"));
					} else
					{
						
						tc = ChatApi.tctl(ij.getItemDisplayName());
						tc.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
								PluginSettings.settings.getCommands(KeyHandler.SCC_BOOK)+itemname+" "+own));
						tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, 
										new BaseComponent[]{new TextComponent(ij.getJsonString())}));
					}
					components.addAllComponents(tc);
					if(count < function.length)
					{
						TextComponent tc2 = ChatApi.tc(" ");
						components.addAllComponents(tc2);
					}
					continue;
				}
			} else if(f.startsWith(plugin.getYamlHandler().getConfig()
					.getString("ChatReplacer.Command.RunCommandStart"))) //ADDME:Run und Suggest replacer farblich unterscheiden
			{
				/*
				 * Run Commands
				 */
				if(!isNotConsole || (usedChannel.isUseRunCommandReplacer() && player.hasPermission(BypassPermission.USE_RUNCOMMAND))
						|| player.hasPermission(BypassPermission.USE_RUNCOMMAND_BYPASS))
				{
					String start = plugin.getYamlHandler().getConfig().getString("ChatReplacer.Command.RunCommandStart");
					String spacereplacer = plugin.getYamlHandler().getConfig().getString("ChatReplacer.Command.SpaceReplacer");
					String cmd = f.replace(start, "/").replace(spacereplacer, " ");
					String textstart = plugin.getYamlHandler().getConfig().getString("ChatReplacer.Command.CommandStartReplacer");
					String textend = plugin.getYamlHandler().getConfig().getString("ChatReplacer.Command.CommandEndReplacer");
					String text = cmd.replace("/", textstart).replace(spacereplacer, " ")+textend;
					TextComponent tc = ChatApi.apiChat(text,
							ClickEvent.Action.RUN_COMMAND,
							ChatColor.stripColor(cmd),
							HoverEvent.Action.SHOW_TEXT,
							plugin.getYamlHandler().getConfig().getString("ChatListener.CommandRunHover"));
					components.addAllComponents(tc);
					if(count < function.length)
					{
						TextComponent tc2 = ChatApi.tc(" ");
						components.addAllComponents(tc2);
					}
					continue;
				}				
			} else if(f.startsWith(plugin.getYamlHandler().getConfig().getString("ChatReplacer.Command.SuggestCommandStart")))
			{
				/*
				 * Suggest Command
				 */
				if(!isNotConsole || (usedChannel.isUseSuggestCommandReplacer() && player.hasPermission(BypassPermission.USE_SUGGESTCOMMAND))
						|| player.hasPermission(BypassPermission.USE_SUGGESTCOMMAND_BYPASS))
				{
					String start = plugin.getYamlHandler().getConfig().getString("ChatReplacer.Command.SuggestCommandStart");
					String spacereplacer = plugin.getYamlHandler().getConfig().getString("ChatReplacer.Command.SpaceReplacer");
					String cmd = f.replace(start, "/").replace(spacereplacer, " ");
					String textstart = plugin.getYamlHandler().getConfig().getString("ChatReplacer.Command.CommandStartReplacer");
					String textend = plugin.getYamlHandler().getConfig().getString("ChatReplacer.Command.CommandEndReplacer");
					String text = cmd.replace("/", textstart).replace(spacereplacer, " ")+textend;
					TextComponent tc = ChatApi.apiChat(text,
							ClickEvent.Action.SUGGEST_COMMAND,
							ChatColor.stripColor(cmd),
							HoverEvent.Action.SHOW_TEXT,
							plugin.getYamlHandler().getConfig().getString("ChatListener.CommandSuggestHover"));
					components.addAllComponents(tc);
					if(count < function.length)
					{
						TextComponent tc2 = ChatApi.tc(" ");
						components.addAllComponents(tc2);
					}
					continue;
				}
			} else if(f.startsWith("http") || f.endsWith(".de") || f.endsWith(".com") || f.endsWith(".net")
					|| f.endsWith(".au") || f.endsWith(".io") || f.endsWith(".be"))
			{
				/*
				 * Website
				 */
				if(!isNotConsole || (usedChannel.isUseWebsiteReplacer() && player.hasPermission(BypassPermission.USE_WEBSITE))
						|| player.hasPermission(BypassPermission.USE_WEBSITE_BYPASS))
				{
					TextComponent tc = ChatApi.apiChat(plugin.getYamlHandler().getLang().getString("ChatListener.Website.Replacer"),
							ClickEvent.Action.OPEN_URL, 
							ChatColor.stripColor(f),
							HoverEvent.Action.SHOW_TEXT, 
							plugin.getYamlHandler().getLang().getString("ChatListener.Website.Hover")
							+ChatColor.stripColor(f));
					components.addAllComponents(tc);
					if(count < function.length)
					{
						TextComponent tc2 = ChatApi.tc(" ");
						components.addAllComponents(tc2);
					}
					continue;
				} else
				{
					TextComponent tc = ChatApi.apiChat(plugin.getYamlHandler().getLang().getString("ChatListener.Website.NotAllowReplacer"),
							ClickEvent.Action.OPEN_URL, 
							ChatColor.stripColor(f),
							HoverEvent.Action.SHOW_TEXT, 
							plugin.getYamlHandler().getLang().getString("ChatListener.Website.NotAllowHover"));
					components.addAllComponents(tc);
				}
			} else if(f.startsWith(plugin.getYamlHandler().getConfig()
					.getString("ChatReplacer.Emoji.Start"))
					&& f.endsWith(plugin.getYamlHandler().getConfig()
							.getString("ChatReplacer.Emoji.End")))
			{
				if(!isNotConsole || (usedChannel.isUseEmojiReplacer() && player.hasPermission(BypassPermission.USE_EMOJI))
						|| player.hasPermission(BypassPermission.USE_EMOJI_BYPASS))
				{
					String emoji = f;
					if(emojiList.containsKey(f))
					{
						emoji = emojiList.get(f);
					}
					TextComponent tc = ChatApi.hoverEvent(lastColor+emoji,
							HoverEvent.Action.SHOW_TEXT,
							plugin.getYamlHandler().getLang().getString("ChatListener.Emoji.Hover")
							.replace("%emoji%", f));
					components.addAllComponents(tc);
					if(count < function.length)
					{
						TextComponent tc2 = ChatApi.tc(" ");
						components.addAllComponents(tc2);
					}
					continue;
				}				
			} else if(f.startsWith(plugin.getYamlHandler().getConfig()
					.getString("ChatReplacer.Mention.Start")))
			{
				/*
				 * Player mention with Start and End
				 * Playermention not available for private msg.
				 */
				if(!isNotConsole || (usedChannel.isUseMentionReplacer() && player.hasPermission(BypassPermission.USE_MENTION))
						|| player.hasPermission(BypassPermission.USE_MENTION_BYPASS))
				{
					boolean hasFindOne = false;
					String name = f.substring(
							plugin.getYamlHandler().getConfig().getString("ChatReplacer.Mention.Start").length());
					for(ProxiedPlayer mentioned : plugin.getProxy().getPlayers())
					{
						if(mentioned.getName().toLowerCase().startsWith(name.toLowerCase()))
						{
							name = mentioned.getName();
							components.addMention(mentioned.getName());
							hasFindOne = true;
							break;
						}
					}
					if(!hasFindOne)
					{
						name = f;
					}
					//FIXME am besten hier zwischen dem Erwähnten und dem nicht erwähnten unterscheiden.
					TextComponent tc = ChatApi.hoverEvent(
							plugin.getYamlHandler().getConfig().getString("ChatReplacer.Mention.Color")+name,
							HoverEvent.Action.SHOW_TEXT,
							plugin.getYamlHandler().getLang().getString("ChatListener.Mention.YouAreMentionHover")
							.replace("%player%", player.getName()));
					components.addAllComponents(tc);
					if(count < function.length)
					{
						TextComponent tc2 = ChatApi.tc(" ");
						components.addAllComponents(tc2);
					}
					continue;
				}
			} else if(isNotConsole && f.equalsIgnoreCase(plugin.getYamlHandler().getConfig().getString("ChatReplacer.Position.Replacer")))
			{
				if((usedChannel.isUsePositionReplacer() && player.hasPermission(BypassPermission.USE_POSITION))
						|| player.hasPermission(BypassPermission.USE_POSITION_BYPASS))
				{
					if(ChatListener.playerLocation.containsKey(player.getUniqueId().toString()))
					{
						ServerLocation sl = ChatListener.playerLocation.get(player.getUniqueId().toString());
						TextComponent tc = ChatApi.tctl(plugin.getYamlHandler().getConfig().getString("ChatReplacer.Position.Replace")
								.replace("%server%", sl.getServer())
								.replace("%world%", sl.getWordName())
								.replace("%x%", String.valueOf((int) sl.getX()))
								.replace("%y%", String.valueOf((int) sl.getY()))
								.replace("%z%", String.valueOf((int) sl.getZ())));
						components.addAllComponents(tc);
						if(count < function.length)
						{
							TextComponent tc2 = ChatApi.tc(" ");
							components.addAllComponents(tc2);
						}
						continue;
					}
				}
			} else if(f.equals(plugin.getYamlHandler().getConfig().getString("ChatReplacer.NewLine")))
			{
				if(newlineCounter <= 5)
				{
					components.addAllComponents(ChatApi.newLine());
					//As only function, to NOT have a Space after it.
					continue;
				}
				newlineCounter++;
			}
			/*
			 * Color Handling, if nothing is correct
			 */
			boolean canColor = false;
			if(isNotConsole)
			{
				if((usedChannel.isUseColor() && player.hasPermission(BypassPermission.USE_COLOR))
						|| player.hasPermission(BypassPermission.USE_COLOR_BYPASS))
				{
					canColor = true;
				}
			} else
			{
				canColor = true;
			}
			
			String string = f;
			String s = "";
			int i = 0;
			while(i < string.length())
			{
				if(i == 0 && (string.charAt(i) != '&') || string.charAt(i) != '§')
				{
					s += lastColor;
				}
				char c = string.charAt(i);
				if(c == '&' || c == '§')
				{
					if(string.length() > (i+1))
					{
						char c2 = string.charAt(i+1);
						if(c2 == '#')
						{
							if(string.length() > i+7 && canColor)
							{
								char c3 = string.charAt(i+2);
								char c4 = string.charAt(i+3);
								char c5 = string.charAt(i+4);
								char c6 = string.charAt(i+5);
								char c7 = string.charAt(i+6);
								char c8 = string.charAt(i+7);
								if(!isHexChar(c3, c4, c5, c6, c7, c8))
								{
									i = i+8;
									continue;
								}
								lastColor = "&#"
										+c3+c4+c5+c6+c7+c8;
								s += lastColor;
								i = i+8;
								continue;
							} else if(string.length() > i+7 && !canColor)
							{
								i = i+8;
								continue;
							} else
							{
								i = i+2;
								continue;
							}
						} else if(isColor(c2) && canColor)
						{
							lastColor = "&"+String.valueOf(c2);
							s += lastColor;
							i += 2;
							continue;
						} else
						{
							i += 2;
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
						s += " "+lastColor;
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
			//TextComponent tc = ChatApi.tctl(getColoredString(f, channelColor, canColor));
			TextComponent tc = ChatApi.tctl(s);
			components.addAllComponents(tc);
			if(count < function.length)
			{
				TextComponent tc2 = ChatApi.tc(" ");
				components.addAllComponents(tc2);
			}
		}
		return components;
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
	
	private boolean isHexChar(char...cc)
	{
		for(char c : cc)
		{
			if(c != '0' && c != '1' && c != '2' && c != '3' && c != '4' && c != '5' && c != '6' && c != '7' && c != '8' && c != '9'
					 && c != 'a' && c != 'A' && c != 'b' && c != 'B' && c != 'c' && c != 'C'  && c != 'd'  && c != 'D' 
					 && c != 'e'  && c != 'E'  && c != 'F'  && c != 'F')
			{
				return false;
			}
		}		
		return true;
	}
	
	private boolean isSameWorld(ProxiedPlayer p1, ProxiedPlayer p2)
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
	
	private boolean isInRadius(ProxiedPlayer p1, ProxiedPlayer p2, int blockRadius)
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
	
	private void sendMessage(Components components, ProxiedPlayer player, Channel usedChannel, TemporaryChannel tch, PermanentChannel pc)
	{
		TextComponent txc1 = ChatApi.tc("");
		txc1.setExtra(components.getComponents());
		TextComponent txc2 = ChatApi.tc("");
		txc2.setExtra(components.getComponentsWithMentions());
		plugin.getLogger().log(Level.INFO, txc2.toLegacyText());
		ArrayList<String> sendedPlayer = new ArrayList<>();
		for(ProxiedPlayer toMessage : plugin.getProxy().getPlayers())
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
			if(usedChannel.isSpecificServer())
			{
				if(!toMessage.getServer().getInfo().getName().equals(player.getServer().getInfo().getName()))
				{
					continue;
				}
			} else if(usedChannel.isSpecificWorld())
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
			if(components.isMention(toMessage.getName()))
			{
				toMessage.sendMessage(txc2);
				if(toMessage.hasPermission(BypassPermission.USE_SOUND))
				{
					sendMentionPing(toMessage, usedChannel.getMentionSound());
				}
			} else
			{
				toMessage.sendMessage(txc1);
			}
			sendedPlayer.add(toMessage.getUniqueId().toString());
		}
		spy(sendedPlayer, components, txc1, txc2);
	}
	
	private void sendPrivateMessage(Components components, ProxiedPlayer player, ProxiedPlayer other, Channel usedChannel)
	{
		TextComponent txc1 = ChatApi.tc("");
		txc1.setExtra(components.getComponents());
		TextComponent txc2 = ChatApi.tc("");
		txc2.setExtra(components.getComponentsWithMentions());
		plugin.getLogger().log(Level.INFO, txc2.toLegacyText());
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
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("ChatListener.PlayerHasPrivateChannelOff")
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
				player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("ChatListener.PlayerIgnoreYou")
						.replace("%player%", other.getName())));
				return;
			}
			isIgnored = true;
		}
		if(components.isMention(player.getName()))
		{
			player.sendMessage(txc2);
		} else
		{
			player.sendMessage(txc1);
		}
		sendedPlayer.add(player.getUniqueId().toString());
		if(components.isMention(other.getName()))
		{
			other.sendMessage(txc2);
		} else
		{
			other.sendMessage(txc1);
		}
		if(plugin.getYamlHandler().getConfig().getBoolean("MsgSoundUsage") &&
				other.hasPermission(BypassPermission.USE_SOUND))
		{
			sendMentionPing(other, usedChannel.getMentionSound());
		}
		sendedPlayer.add(other.getUniqueId().toString());
		if(isIgnored)
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("ChatListener.PlayerIgnoreYouButYouBypass")
					.replace("%player%", other.getName())));
		}
		sendedPlayer.add(other.getUniqueId().toString());
		spy(sendedPlayer, components, txc1, txc2);
	}
	
	private void sendPrivateConsoleMessage(Components components, CommandSender console, ProxiedPlayer other, Channel usedChannel)
	{
		TextComponent txc1 = ChatApi.tc("");
		txc1.setExtra(components.getComponents());
		TextComponent txc2 = ChatApi.tc("");
		txc2.setExtra(components.getComponentsWithMentions());
		plugin.getLogger().log(Level.INFO, txc2.toLegacyText());
		if(components.isMention(other.getName()))
		{
			other.sendMessage(txc2);
		} else
		{
			other.sendMessage(txc1);
		}
		if(plugin.getYamlHandler().getConfig().getBoolean("MsgSoundUsage") &&
				other.hasPermission(BypassPermission.USE_SOUND))
		{
			sendMentionPing(other, usedChannel.getMentionSound());
		}
		logging(txc1);
	}
	
	private void spy(ArrayList<String> sendedPlayer, final Components components, final TextComponent txc1, final TextComponent txc2)
	{
		//Spy Part
		for(ProxiedPlayer spy : plugin.getProxy().getPlayers())
		{
			ChatUser cu = ChatUserHandler.getChatUser(spy.getUniqueId());
			if(spy == null
					|| cu == null
					|| sendedPlayer.contains(cu.getUUID())
					|| !cu.isOptionSpy())
			{
				continue;
			}
			spy.sendMessage(txc1);
		}
	}
	
	private void logging(final TextComponent txc)
	{
		plugin.getLogger().log(Level.INFO, txc.toLegacyText());
	}
	
	public void sendMentionPing(ProxiedPlayer player, String soundEnum)
	{
		if(player == null)
		{
			return;
		}
		if(!plugin.getYamlHandler().getConfig().getBoolean("MsgSoundUsage"))
		{
			return;
		}
		ByteArrayOutputStream streamout = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(streamout);
        try {
        	out.writeUTF(StaticValues.SCC_TASK_PINGAPLAYER);
			out.writeUTF(player.getUniqueId().toString());
			if(soundEnum == null)
			{
				out.writeUTF(plugin.getYamlHandler().getConfig().getString("ChatReplacer.Mention.SoundEnum"));
			} else
			{
				out.writeUTF(soundEnum);
			}			
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.getServer().getInfo().sendData(StaticValues.SCC_TOSPIGOT, streamout.toByteArray());
	}
}