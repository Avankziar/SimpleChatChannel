package main.java.me.avankziar.scc.velocity.handler;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.regex.Pattern;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.ConsoleCommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.handlers.ColorHandler;
import main.java.me.avankziar.scc.general.handlers.TimeHandler;
import main.java.me.avankziar.scc.general.objects.BoundedList;
import main.java.me.avankziar.scc.general.objects.Channel;
import main.java.me.avankziar.scc.general.objects.ChatTitle;
import main.java.me.avankziar.scc.general.objects.ChatUser;
import main.java.me.avankziar.scc.general.objects.ComponentsVelo;
import main.java.me.avankziar.scc.general.objects.ItemJson;
import main.java.me.avankziar.scc.general.objects.KeyHandler;
import main.java.me.avankziar.scc.general.objects.PermanentChannel;
import main.java.me.avankziar.scc.general.objects.ServerLocation;
import main.java.me.avankziar.scc.general.objects.StaticValues;
import main.java.me.avankziar.scc.general.objects.UsedChannel;
import main.java.me.avankziar.scc.velocity.SCC;
import main.java.me.avankziar.scc.velocity.assistance.Utility;
import main.java.me.avankziar.scc.velocity.listener.ChatListener;
import main.java.me.avankziar.scc.velocity.objects.BypassPermission;
import main.java.me.avankziar.scc.velocity.objects.ChatUserHandler;
import main.java.me.avankziar.scc.velocity.objects.PluginSettings;
import main.java.me.avankziar.scc.velocity.objects.chat.TemporaryChannel;
import net.kyori.adventure.text.minimessage.MiniMessage;

public class ChatHandler
{
	private SCC plugin;
	private static String console = "Console";
	private static LinkedHashMap<String, String> privateChatColorPerPlayers = new LinkedHashMap<>();
	private static ArrayList<String> privateChatColor = new ArrayList<>();
	public static LinkedHashMap<String, String> emojiList = new LinkedHashMap<>();
	public static ArrayList<String> breakChat = new ArrayList<>();
	public static Pattern STRIP_COLOR_PATTERN = Pattern.compile("(?i)" + String.valueOf('§') + "[0-9A-FK-ORX]");
	
	public static LinkedHashMap<UUID, BoundedList<String>> chatHistory = new LinkedHashMap<>();
	
	public ChatHandler(SCC plugin)
	{
		this.plugin = plugin;
	}
	
	public static BoundedList<String> getChatHistory(UUID uuid) 
	{
	    return ChatHandler.chatHistory.computeIfAbsent(uuid, k -> new BoundedList<>(50));
	}
	
	public static void initPrivateChatColors()
	{
		List<String> colors = SCC.getPlugin().getYamlHandler().getConfig()
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
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("ChatListener.YouAreMuted")
					.replace("%time%", TimeHandler.getDateTime(mutedTime))));
			return false;
		}
		/*
		 * Wordfilter
		 */
		if(plugin.getUtility().containsBadWords(player, message))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("ChatListener.ContainsBadWords")));
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
		for(ChatTitle ct : SCC.chatTitlesPrefix)
		{
			if(player.hasPermission(ct.getPermission()))
			{
				userPrefix.add(ct);
			}
		}
		for(ChatTitle ct : SCC.chatTitlesSuffix)
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
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("ChatListener.SymbolNotKnow")
						.replace("%symbol%", space[0])));
				return;
			}
			if(!pc.getMembers().contains(player.getUniqueId().toString()))
			{
				///Du bist in keinem Permanenten Channel!
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("ChatListener.NotAPermanentChannel")));
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
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("ChatListener.NotATemporaryChannel")));
				return;
			}
		}
		ComponentsVelo components = getComponent(
				player, null, msg, usedChannel.getChatFormat(), userPrefix, userSuffix, usedChannel, tc, pc, null, null, 
				channelcolor);
		if(breakChat.contains(player.getUniqueId().toString()))
		{
			breakChat.remove(player.getUniqueId().toString());
			return;
		}
		sendMessage(components, player, usedChannel, tc, pc);
	}
	
	public void startPrivateChat(Player player, Player other, String message)
	{
		Channel usedChannel = null;
		for(Channel c : SCC.channels.values())
		{
			if(c.getUniqueIdentifierName().equals("Private"))
			{
				usedChannel = c;
				break;
			}
		}
		if(usedChannel == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdMsg.PrivateChannelsNotActive")));
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
		for(ChatTitle ct : SCC.chatTitlesPrefix)
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
		for(ChatTitle ct : SCC.chatTitlesSuffix)
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
		String basecolor = getUsedChannelColor(usedChannel, player.getUsername(), other.getUsername());
		ComponentsVelo components = getComponent(
				player, other, message, usedChannel.getChatFormat(),
				userPrefix, userSuffix, usedChannel, null, null, otheruserPrefix, otheruserSuffix,
				basecolor);
		if(breakChat.contains(player.getUniqueId().toString()))
		{
			breakChat.remove(player.getUniqueId().toString());
			return;
		}
		sendPrivateMessage(components, player, other, usedChannel);
	}
	
	public void startPrivateConsoleChat(CommandSource console, Player other, String message)
	{
		Channel usedChannel = null;
		for(Channel c : SCC.channels.values())
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
		for(ChatTitle ct : SCC.chatTitlesPrefix)
		{
			if(other.hasPermission(ct.getPermission()))
			{
				otheruserPrefix.add(ct);
			}
		}
		for(ChatTitle ct : SCC.chatTitlesSuffix)
		{
			if(other.hasPermission(ct.getPermission()))
			{
				otheruserSuffix.add(ct);
			}
		}		
		ComponentsVelo components = getComponent(
				console, other, message, usedChannel.getChatFormat(),
				new ArrayList<>(), new ArrayList<>(), usedChannel, null, null, otheruserPrefix, otheruserSuffix,
				usedChannel.getInChatColorMessage());
		if(breakChat.contains("console"))
		{
			breakChat.remove("console");
			return;
		}
		sendPrivateConsoleMessage(components, console, other, usedChannel);
	}
	
	private String getUsedChannelColor(Channel channel, String player1, String player2)
	{
		if(!plugin.getYamlHandler().getConfig().getBoolean("PrivateChannel.UseDynamicColor", true))
		{
			return ChatApi.oldBukkitFormat(channel.getInChatColorMessage());
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
				String co = ChatApi.oldBukkitFormat(c);
				boolean unused = true;
				for(String al : alreadyUsedColors)
				{
					if(co.equals(al))
					{
						unused = false;
					}
				}
				if(unused)
				{
					remainingColors.add(co);
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
	
	public void sendBroadCast(CommandSource players, Channel usedChannel, String message, String server)
	{
		ComponentsVelo components = getComponent(Channel.ChatFormatPlaceholder.MESSAGE.getPlaceholder(),
				message, players, null, new ArrayList<>(), new ArrayList<>(), usedChannel, null, null, null, null,
				usedChannel.getInChatColorMessage());
		if(players instanceof ConsoleCommandSource && breakChat.contains("console"))
		{
			breakChat.remove("console");
			return;
		} else if(players instanceof Player)
		{
			Player player = (Player) players;
			if(breakChat.contains(player.getUniqueId().toString()))
			{
				breakChat.remove(player.getUniqueId().toString());
				return;
			}
		}
		String txc1 = String.join("", components.getComponents());
		String txc2 = String.join("", components.getComponentsWithMentions());
		for(Player all : plugin.getServer().getAllPlayers())
		{
			if(server != null)
			{
				if(!all.getCurrentServer().get().getServerInfo().getName().equals(server))
				{
					continue;
				}
			}
			if(components.isMention(all.getUsername()))
			{
				all.sendMessage(ChatApi.tl(txc2));
				if(all.hasPermission(BypassPermission.USE_SOUND))
				{
					sendMentionPing(all, usedChannel.getMentionSound());
				}
			} else
			{
				all.sendMessage(ChatApi.tl(txc1));
			}
		}
	}
	
	private void rePlayerHandling(Player player, Player other)
	{
		if(SCC.rePlayers.containsKey(player.getUniqueId().toString()))
		{
			ArrayList<String> relist = SCC.rePlayers.get(player.getUniqueId().toString());
			if(!relist.contains(other.getUniqueId().toString()))
			{
				relist.add(other.getUsername());
				SCC.rePlayers.replace(player.getUniqueId().toString(), relist);
			}
		} else
		{
			ArrayList<String> relist = new ArrayList<>();
			relist.add(other.getUsername());
			SCC.rePlayers.put(player.getUniqueId().toString(), relist);
		}
		
		if(SCC.rPlayers.containsKey(player.getUniqueId().toString()))
		{
			SCC.rPlayers.replace(player.getUniqueId().toString(), other.getUniqueId().toString());
		} else
		{
			SCC.rPlayers.put(player.getUniqueId().toString(), other.getUniqueId().toString());
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
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("ChatListener.ChannelIsOff")));
			return false;
		}
		
		/*
		 * Is Player on the server?
		 */
		if(!usedChannel.getIncludedServer().isEmpty())
		{
			if(!usedChannel.getIncludedServer().contains(player.getCurrentServer().get().getServerInfo().getName()))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("ChatListener.YourAreOnTheSpecificServer")));
				return false;
			}
		}
		if(!usedChannel.getExcludedServer().isEmpty())
		{
			if(usedChannel.getExcludedServer().contains(player.getCurrentServer().get().getServerInfo().getName()))
			{
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("ChatListener.YourAreOnTheSpecificServer")));
				return false;
			}
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
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang()
							.getString("ChatListener.PleaseWaitALittleWithSameMessage")
							.replace("%channel%", usedChannel.getInChatName())
							.replace("%time%", TimeHandler.getDateTime(last))));
					return false;
				}
				last = map.get(usedChannel.getUniqueIdentifierName())+usedChannel.getTimeBetweenMessages();
				if(last > now)
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("ChatListener.PleaseWaitALittle")
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
	 * @param chatFormat
	 * @param prefix
	 * @param suffix
	 * @param usedChannel
	 * @return
	 */
	private ComponentsVelo getComponent(CommandSource player, Player other, String message, String chatFormat,
			ArrayList<ChatTitle> prefix, ArrayList<ChatTitle> suffix, Channel usedChannel,
			TemporaryChannel tc, PermanentChannel pc,
			ArrayList<ChatTitle> otherprefix, ArrayList<ChatTitle> othersuffix, String channelColor)
	{
		String s = "";
		ComponentsVelo components = new ComponentsVelo();
		String chatFormats = ChatApi.oldBukkitFormat(chatFormat);
		//String lastColor = "";
		int i = 0;
		while(i < chatFormats.length())
		{
			char c = chatFormats.charAt(i);
			if(c == '%')
			{
				if(!s.isEmpty())
				{
					components.addAllComponents(s);
					s = "";
				}
				int j = i+1;
				i++;
				String placeHolder = String.valueOf(c);
				while(j < chatFormats.length())
				{
					char c2 = chatFormats.charAt(j);
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
	public ComponentsVelo getComponent(String placeHolder, String message, CommandSource players, Player otherplayer,
			ArrayList<ChatTitle> prefixs, ArrayList<ChatTitle> suffixs,
			final Channel usedChannel,
			final TemporaryChannel tch, final PermanentChannel pc,
			ArrayList<ChatTitle> otherprefixs, ArrayList<ChatTitle> othersuffixs, String channelColor)
	{
		boolean isNotConsole = false;
		Player player = null;
		if(players instanceof Player)
		{
			player = (Player) players;
			isNotConsole = true;
		}
		ComponentsVelo components = new ComponentsVelo();
		String tc = null;
		Channel.ChatFormatPlaceholder ph = Channel.ChatFormatPlaceholder.getEnum(placeHolder);
		switch(ph)
		{
		case CHANNEL:
			String text = plugin.getUtility().getChannelHover(usedChannel);
			tc = ChatApi.clickHover(plugin.getUtility().getChannelNameSuggestion(usedChannel, pc, tch),
					"SUGGEST_COMMAND",
					plugin.getUtility().getChannelSuggestion(usedChannel, pc),
					"SHOW_TEXT", 
					text);
			return components.addAllComponents(tc);
		case MESSAGE:
			return components.addAllComponents(getMessageParser(players, message, usedChannel, channelColor));
		case PLAYERNAME:
			if(isNotConsole)
			{
				tc = ChatApi.clickHover(player.getUsername(),
						"SUGGEST_COMMAND", PluginSettings.settings.getCommands(KeyHandler.MSG)+player.getUsername()+" ",
						"SHOW_TEXT", plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
						.replace("%player%", player.getUsername()));
			} else
			{
				tc = console;
			}
			return components.addAllComponents(tc);
		case PLAYERNAME_WITH_CUSTOMCOLOR:
			if(isNotConsole)
			{
				tc = ChatApi.clickHover(usedChannel.getPlayernameCustomColor()+player.getUsername(),
						"SUGGEST_COMMAND", PluginSettings.settings.getCommands(KeyHandler.MSG)+player.getUsername()+" ",
						"SHOW_TEXT", plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
						.replace("%player%", player.getUsername()));
			} else
			{
				tc = ChatApi.oldBukkitFormat(usedChannel.getPlayernameCustomColor()+console);
			}
			return components.addAllComponents(tc);
		case PLAYERNAME_WITH_PREFIXHIGHCOLORCODE:
			if(isNotConsole)
			{
				if(prefixs.size() > 0)
				{
					tc = ChatApi.clickHover(prefixs.get(0).getInChatColorCode()+player.getUsername(),
							"SUGGEST_COMMAND", PluginSettings.settings.getCommands(KeyHandler.MSG)+player.getUsername()+" ",
							"SHOW_TEXT", plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
							.replace("%player%", player.getUsername()));
				} else
				{
					tc = ChatApi.clickHover(player.getUsername(),
							"SUGGEST_COMMAND", PluginSettings.settings.getCommands(KeyHandler.MSG)+player.getUsername()+" ",
							"SHOW_TEXT", plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
							.replace("%player%", player.getUsername()));
				}
			} else
			{
				tc = console;
			}
			return components.addAllComponents(tc);
		case PLAYERNAME_WITH_SUFFIXHIGHCOLORCODE:
			if(isNotConsole)
			{
				if(suffixs.size() > 0)
				{
					tc = ChatApi.clickHover(suffixs.get(0).getInChatColorCode()+player.getUsername(),
							"SUGGEST_COMMAND", PluginSettings.settings.getCommands(KeyHandler.MSG+player.getUsername())+" ",
							"SHOW_TEXT", plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
							.replace("%player%", player.getUsername()));
				} else
				{
					tc = ChatApi.clickHover(player.getUsername(),
							"SUGGEST_COMMAND", PluginSettings.settings.getCommands(KeyHandler.MSG)+player.getUsername()+" ",
							"SHOW_TEXT", plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
							.replace("%player%", player.getUsername()));
				}
			} else
			{
				tc = console;
			}
			return components.addAllComponents(tc);
		case PREFIXALL:
			if(isNotConsole)
			{
				ArrayList<String> bc = new ArrayList<>();
				int i = 0;
				int last = prefixs.size()-1;
				for(ChatTitle cts : prefixs)
				{
					if(i < last)
					{
						bc.add(ChatApi.clickHover(cts.getInChatName()+usedChannel.getSeperatorBetweenSuffix(),
								"SUGGEST_COMMAND", cts.getClick(),
								"SHOW_TEXT", cts.getHover()));
					} else
					{
						bc.add(ChatApi.clickHover(cts.getInChatName(),
								"SUGGEST_COMMAND", cts.getClick(),
								"SHOW_TEXT", cts.getHover()));
					}
					
					i++;
				}
				tc = String.join("", bc);
			} else
			{
				tc = "";
			}
			return components.addAllComponents(tc);
		case PREFIXHIGH:
			if(isNotConsole)
			{
				tc = "";
				if(prefixs.size() > 0)
				{
					ChatTitle ct = prefixs.get(0);
					tc = ChatApi.clickHover(ct.getInChatName(),
							"SUGGEST_COMMAND", ct.getClick(),
							"SHOW_TEXT", ct.getHover());
				}
			} else
			{
				tc = "";
			}
			return components.addAllComponents(tc);
		case PREFIXLOW:
			if(isNotConsole)
			{
				tc = "";
				if(prefixs.size() > 0)
				{
					ChatTitle ct = prefixs.get(prefixs.size()-1);
					tc = ChatApi.clickHover(ct.getInChatName(),
							"SUGGEST_COMMAND", ct.getClick(),
							"SHOW_TEXT", ct.getHover());
				}
			} else
			{
				tc = "";
			}
			return components.addAllComponents(tc);
		case SUFFIXALL:
			if(isNotConsole)
			{
				ArrayList<String> bc = new ArrayList<>();
				int i = 0;
				int last = suffixs.size()-1;
				for(ChatTitle cts : suffixs)
				{
					if(i < last)
					{
						bc.add(ChatApi.clickHover(cts.getInChatName()+usedChannel.getSeperatorBetweenSuffix(), 
								"SUGGEST_COMMAND", cts.getClick(),
								"SHOW_TEXT", cts.getHover()));
					} else
					{
						bc.add(ChatApi.clickHover(cts.getInChatName(),
								"SUGGEST_COMMAND", cts.getClick(),
								"SHOW_TEXT", cts.getHover()));
					}
					
					i++;
				}
				tc = String.join("", bc);
			} else
			{
				tc = "";
			}
			return components.addAllComponents(tc);
		case SUFFIXHIGH:
			if(isNotConsole)
			{
				tc = "";
				if(suffixs.size() > 0)
				{
					ChatTitle ct = suffixs.get(0);
					tc = ChatApi.clickHover(ct.getInChatName(),
							"SUGGEST_COMMAND", ct.getClick(),
							"SHOW_TEXT", ct.getHover());
				}
			} else
			{
				tc = "";
			}
			return components.addAllComponents(tc);
		case SUFFIXLOW:
			if(isNotConsole)
			{
				tc = "";
				if(prefixs.size() > 0)
				{
					ChatTitle ct = suffixs.get(suffixs.size()-1);
					tc = ChatApi.clickHover(ct.getInChatName(),
							"SUGGEST_COMMAND", ct.getClick(),
							"SHOW_TEXT", ct.getHover());
				}
			} else
			{
				tc = "";
			}
			return components.addAllComponents(tc);
		case ROLEPLAYNAME:
			if(isNotConsole)
			{
				ChatUser cu = ChatUserHandler.getChatUser(player.getUniqueId());
				String name = (cu != null) ? cu.getRolePlayName() : player.getUsername();
				tc = ChatApi.clickHover(name,
						"SUGGEST_COMMAND", PluginSettings.settings.getCommands(KeyHandler.MSG)+player.getUsername()+" ",
						"SHOW_TEXT", plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
						.replace("%player%", player.getUsername()));
			} else
			{
				tc = console;
			}
			return components.addAllComponents(tc);
		case OTHER_PLAYERNAME:
			if(otherplayer == null)
			{
				tc = "";
			} else
			{
				tc = ChatApi.clickHover(otherplayer.getUsername(),
						"SUGGEST_COMMAND", PluginSettings.settings.getCommands(KeyHandler.MSG)+otherplayer.getUsername()+" ",
						"SHOW_TEXT", plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
						.replace("%player%", otherplayer.getUsername()));
			}
			return components.addAllComponents(tc);
		case OTHER_PLAYERNAME_WITH_CUSTOMCOLOR:
			if(otherplayer == null)
			{
				tc = "";
			} else
			{
				tc = ChatApi.clickHover(usedChannel.getOtherplayernameCustomColor()+otherplayer.getUsername(),
						"SUGGEST_COMMAND", PluginSettings.settings.getCommands(KeyHandler.MSG)+otherplayer.getUsername()+" ",
						"SHOW_TEXT", plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
						.replace("%player%", otherplayer.getUsername()));
			}
			return components.addAllComponents(tc);
		case OTHER_PLAYERNAME_WITH_PREFIXHIGHCOLORCODE:
			if(otherplayer == null)
			{
				tc = "";
			} else
			{
				if(otherprefixs.size() > 0)
				{
					tc = ChatApi.clickHover(otherprefixs.get(0).getInChatColorCode()+otherplayer.getUsername(),
							"SUGGEST_COMMAND", PluginSettings.settings.getCommands(KeyHandler.MSG)+otherplayer.getUsername()+" ",
							"SHOW_TEXT", plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
							.replace("%player%", otherplayer.getUsername()));
				} else
				{
					tc = ChatApi.clickHover(otherplayer.getUsername(),
							"SUGGEST_COMMAND", PluginSettings.settings.getCommands(KeyHandler.MSG)+otherplayer.getUsername()+" ",
							"SHOW_TEXT", plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
							.replace("%player%", otherplayer.getUsername()));
				}
			}
			return components.addAllComponents(tc);
		case OTHER_PLAYERNAME_WITH_SUFFIXHIGHCOLORCODE:
			if(otherplayer == null)
			{
				tc = "";
			} else
			{
				if(othersuffixs.size() > 0)
				{
					tc = ChatApi.clickHover(othersuffixs.get(0).getInChatColorCode()+otherplayer.getUsername(),
							"SUGGEST_COMMAND", PluginSettings.settings.getCommands(KeyHandler.MSG)+otherplayer.getUsername()+" ",
							"SHOW_TEXT", plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
							.replace("%player%", otherplayer.getUsername()));
				} else
				{
					tc = ChatApi.clickHover(otherplayer.getUsername(),
							"SUGGEST_COMMAND", PluginSettings.settings.getCommands(KeyHandler.MSG)+otherplayer.getUsername()+" ",
							"SHOW_TEXT", plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
							.replace("%player%", otherplayer.getUsername()));
				}
			}
			return components.addAllComponents(tc);
		case OTHER_PREFIXALL:
			if(otherplayer == null)
			{
				tc = "";
			} else
			{
				ArrayList<String> bc = new ArrayList<>();
				for(ChatTitle ctp : prefixs)
				{
					bc.add(ChatApi.clickHover(ctp.getInChatName(),
							"SUGGEST_COMMAND", ctp.getClick(),
							"SHOW_TEXT", ctp.getHover()));
				}
				tc = String.join("", bc);
			}
			return components.addAllComponents(tc);
		case OTHER_PREFIXHIGH:
			if(otherplayer == null)
			{
				tc = "";
			} else
			{
				tc = "";
				if(prefixs.size() > 0)
				{
					ChatTitle ct = prefixs.get(0);
					tc = ChatApi.clickHover(ct.getInChatName(),
							"SUGGEST_COMMAND", ct.getClick(),
							"SHOW_TEXT", ct.getHover());
				}
			}
			return components.addAllComponents(tc);
		case OTHER_PREFIXLOW:
			if(otherplayer == null)
			{
				tc = "";
			} else
			{
				tc = "";
				if(prefixs.size() > 0)
				{
					ChatTitle ct = prefixs.get(prefixs.size()-1);
					tc = ChatApi.clickHover(ct.getInChatName(),
							"SUGGEST_COMMAND", ct.getClick(),
							"SHOW_TEXT", ct.getHover());
				}
			}
			return components.addAllComponents(tc);
		case OTHER_SUFFIXALL:
			if(otherplayer == null)
			{
				tc = "";
			} else
			{
				ArrayList<String> bc = new ArrayList<>();
				for(ChatTitle cts : suffixs)
				{
					bc.add(ChatApi.clickHover(cts.getInChatName(),
							"SUGGEST_COMMAND", cts.getClick(),
							"SHOW_TEXT", cts.getHover()));
				}
				tc = String.join("", bc);
			}
			return components.addAllComponents(tc);
		case OTHER_SUFFIXHIGH:
			if(otherplayer == null)
			{
				tc = "";
			} else
			{
				tc = "";
				if(suffixs.size() > 0)
				{
					ChatTitle ct = suffixs.get(0);
					tc = ChatApi.clickHover(ct.getInChatName(),
							"SUGGEST_COMMAND", ct.getClick(),
							"SHOW_TEXT", ct.getHover());
				}
			}
			return components.addAllComponents(tc);
		case OTHER_SUFFIXLOW:
			if(otherplayer == null)
			{
				tc = "";
			} else
			{
				tc = "";
				if(prefixs.size() > 0)
				{
					ChatTitle ct = suffixs.get(suffixs.size()-1);
					tc = ChatApi.clickHover(ct.getInChatName(),
							"SUGGEST_COMMAND", ct.getClick(),
							"SHOW_TEXT", ct.getHover());
				}
			}
			return components.addAllComponents(tc);
		case OTHER_ROLEPLAYNAME:
			if(otherplayer == null)
			{
				tc = "";
			} else
			{
				ChatUser cu = ChatUserHandler.getChatUser(otherplayer.getUniqueId());
				String name = (cu != null) ? cu.getRolePlayName() : otherplayer.getUsername();
				tc = ChatApi.clickHover(name,
						"SUGGEST_COMMAND", PluginSettings.settings.getCommands(KeyHandler.MSG)+otherplayer.getUsername()+" ",
						"SHOW_TEXT", plugin.getYamlHandler().getLang().getString("ChatListener.PrivateChatHover")
						.replace("%player%", otherplayer.getUsername()));
			}
			return components.addAllComponents(tc);
		case NEWLINE:
			return components.addAllComponents("<newline>");
		case SERVER:
			String server = "";
			String cmd = "";
			String hover = "";
			if(isNotConsole)
			{
				server = player.getCurrentServer().get().getServerInfo().getName();
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
			tc = ChatApi.clickHover(serverReplacer,
					"SUGGEST_COMMAND", cmd,
					"SHOW_TEXT", hover);
			return components.addAllComponents(tc);
		case OTHER_SERVER:
			
			if(otherplayer == null)
			{
				tc = "";
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
			tc = ChatApi.clickHover(serverReplacer,
					"SUGGEST_COMMAND", cmd,
					"SHOW_TEXT", hover);
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
				tc = ChatApi.clickHover(worldReplacer,
						"SUGGEST_COMMAND", cmd,
						"SHOW_TEXT", hover);
				return components.addAllComponents(tc);
			} else
			{
				tc = "";
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
				tc = ChatApi.clickHover(worldReplacer,
						"SUGGEST_COMMAND", cmd,
						"SHOW_TEXT", hover);
				return components.addAllComponents(tc);
			} else
			{
				tc = "";
				return components.addAllComponents(tc);
			}
		case TIME:
			tc = ChatApi.oldBukkitFormat(usedChannel.getTimeColor()+TimeHandler.getTime(System.currentTimeMillis()));
			return components.addAllComponents(tc);
		case TIMES:
			tc = ChatApi.oldBukkitFormat(usedChannel.getTimeColor()+TimeHandler.getTimes(System.currentTimeMillis()));
			return components.addAllComponents(tc);
		case UNDEFINE:
			//ADDME PlaceHolderAPI?
			break;
		}
		tc = "";
		return components.addAllComponents(tc);
	}
	
	public ComponentsVelo getMessageParser(CommandSource players, String msg, Channel usedChannel, String channelColor)
	{
		boolean isNotConsole = false;
		Player player = null;
		if(players instanceof Player)
		{
			player = (Player) players;
			isNotConsole = true;
		}
		
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
		//Strip of all none allow tags
		String message = MiniMessage.miniMessage().stripTags(ChatApi.oldBukkitFormatShort(msg), canColor ? ChatApi.ALLOW_ONLY_COLOR : ChatApi.ALL);
		message = ChatApi.oldBukkitFormatShort(channelColor+message);
		ComponentsVelo components = new ComponentsVelo();
		String[] function = message.split(" "); //TODO split("\s+") um alle Leerzeichen rauszuholen
		int count = -1;
		int newlineCounter = 0;
		String lastColor = channelColor;
		int replacerCount = 0; //Zählt den Item, Command etc. Replacer. Mehr als 5 sind zuviel und würde alle Spieler kicken, die die Nachricht erhalten.
		/* TODO Funktionen die noch hinzugefügt werden können:
		 * rainbow => <rainbow>||||||||||||||||||||||||</rainbow> or <rainbow:!2>||||||||||||||||||||||||</rainbow>!
		 * gradient => <gradient>||||||||||||||||||||||||</gradient> or <gradient:green:blue>||||||||||||||||||||||||</gradient>
		 * transition => <transition:#00ff00:#ff0000:0.0>|||||||||</transition> or <transition:white:black:red:[phase]>Hello world [phase]</transition>
		 */
		for(String f : function)
		{
			++count;
			if(f.startsWith(plugin.getYamlHandler().getConfig().getString("ChatReplacer.Item.Start"))
					&& f.endsWith(plugin.getYamlHandler().getConfig().getString("ChatReplacer.Item.End")))
			{
				
				if(!isNotConsole || (usedChannel.isUseItemReplacer() && player.hasPermission(BypassPermission.USE_ITEM))
					|| player.hasPermission(BypassPermission.USE_ITEM_BYPASS))
				{
					//Substring the start and end from the itemname.
					String itemname = f.substring(plugin.getYamlHandler().getConfig().getString("ChatReplacer.Item.Start").length(),
							f.length()-plugin.getYamlHandler().getConfig().getString("ChatReplacer.Item.End").length());
					String owner = "sv"; //server
					if(isNotConsole)
					{
						owner = player.getUniqueId().toString();
					}
					if(f.contains(plugin.getYamlHandler().getConfig().getString("ChatReplacer.Item.Seperator")))
					{
						//ChatItemReplacer from the Database, with a specified Name.
						itemname = itemname.substring(plugin.getYamlHandler().getConfig().getString("ChatReplacer.Item.Seperator").length());
					} else
					{
						//ChatItemReplacer from the Database, with null name. Aka default.
						itemname = "default";
					}
					ItemJson ij = (ItemJson) plugin.getMysqlHandler().getData(MysqlType.ITEMJSON, "`owner` = ? AND `itemname` = ?",
							owner, itemname);
					String tc = null;
					if(ij == null)
					{
						tc = ChatApi.hover("&f"+itemname,
								plugin.getYamlHandler().getLang().getString("ChatListener.ItemIsMissing"));
					} else
					{
						tc = ChatApi.hover(ij.getItemDisplayName(), ij.getJsonString());
					}
					components.addAllComponents(tc);
					if(count < function.length)
					{
						String tc2 = " ";
						components.addAllComponents(tc2);
					}
					replacerCount++;
					continue;
				}
			} else if(f.startsWith(plugin.getYamlHandler().getConfig().getString("ChatReplacer.Book.Start"))
					&& f.endsWith(plugin.getYamlHandler().getConfig().getString("ChatReplacer.Book.End")))
			{
				if(!isNotConsole || (usedChannel.isUseBookReplacer() && player.hasPermission(BypassPermission.USE_BOOK))
					|| player.hasPermission(BypassPermission.USE_BOOK_BYPASS))
				{
					//Substring the start and end from the itemname.
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
						//ChatItemReplacer from the Database, with a specified Name.
						itemname = itemname.substring(plugin.getYamlHandler().getConfig().getString("ChatReplacer.Book.Seperator").length());
					} else
					{
						//ChatItemReplacer from the Database, with null name. Aka default.
						itemname = "default";
					}
					ItemJson ij = (ItemJson) plugin.getMysqlHandler().getData(MysqlType.ITEMJSON, "`owner` = ? AND `itemname` = ?",
							owner, itemname);
					String tc = null;
					if(ij == null || own == null)
					{
						tc = ChatApi.hover("&f"+itemname,
								"SHOW_TEXT", 
								plugin.getYamlHandler().getLang().getString("ChatListener.ItemIsMissing"));
					} else
					{
						
						tc = ChatApi.clickHover(ij.getItemDisplayName(),
								"RUN_COMMAND",
								PluginSettings.settings.getCommands(KeyHandler.SCC_BOOK)+itemname+" "+own,
								"SHOW_ITEM", 
								ij.getJsonString());
					}
					components.addAllComponents(tc);
					if(count < function.length)
					{
						components.addAllComponents(" ");
					}
					replacerCount++;
					continue;
				}
			} else if(f.startsWith(plugin.getYamlHandler().getConfig()
					.getString("ChatReplacer.Command.RunCommandStart")))
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
					String textstart = plugin.getYamlHandler().getConfig().getString("ChatReplacer.Command.RunCommandStartReplacer");
					String textend = plugin.getYamlHandler().getConfig().getString("ChatReplacer.Command.RunCommandEndReplacer");
					String text = cmd.replace("/", textstart).replace(spacereplacer, " ")+textend;
					String tc = ChatApi.clickHover(text,
							"RUN_COMMAND",
							stripColor(cmd),
							"SHOW_TEXT",
							plugin.getYamlHandler().getLang().getString("ChatListener.CommandRunHover"));
					components.addAllComponents(tc);
					if(count < function.length)
					{
						components.addAllComponents(" ");
					}
					replacerCount++;
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
					String textstart = plugin.getYamlHandler().getConfig().getString("ChatReplacer.Command.SuggestCommandStartReplacer");
					String textend = plugin.getYamlHandler().getConfig().getString("ChatReplacer.Command.SuggestCommandEndReplacer");
					String text = cmd.replace("/", textstart).replace(spacereplacer, " ")+textend;
					String tc = ChatApi.clickHover(text,
							"SUGGEST_COMMAND",
							stripColor(cmd),
							"SHOW_TEXT",
							plugin.getYamlHandler().getLang().getString("ChatListener.CommandSuggestHover"));
					components.addAllComponents(tc);
					if(count < function.length)
					{
						components.addAllComponents(" ");
					}
					replacerCount++;
					continue;
				}
			} else if(f.startsWith("http") || f.startsWith("www.") || f.endsWith(".de") || f.endsWith(".com") || f.endsWith(".net")
					|| f.endsWith(".au") || f.endsWith(".io") || f.endsWith(".be"))
			{
				/*
				 * Website
				 */
				if(!isNotConsole || (usedChannel.isUseWebsiteReplacer() && player.hasPermission(BypassPermission.USE_WEBSITE))
						|| player.hasPermission(BypassPermission.USE_WEBSITE_BYPASS))
				{
					String tc = ChatApi.clickHover(plugin.getYamlHandler().getLang().getString("ChatListener.Website.Replacer"),
							"COPY_TO_CLIPBOARD", 
							stripColor(f),
							"SHOW_TEXT", 
							plugin.getYamlHandler().getLang().getString("ChatListener.Website.Hover")
							+stripColor(f));
					components.addAllComponents(tc);
					if(count < function.length)
					{
						components.addAllComponents(" ");
					}
					replacerCount++;
					continue;
				} else
				{
					String tc = ChatApi.hover(plugin.getYamlHandler().getLang().getString("ChatListener.Website.NotAllowReplacer"),
							"SHOW_TEXT", 
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
					String tc = ChatApi.hover(lastColor+emoji,
							"SHOW_TEXT",
							plugin.getYamlHandler().getLang().getString("ChatListener.Emoji.Hover")
							.replace("%emoji%", f));
					components.addAllComponents(tc);
					if(count < function.length)
					{
						components.addAllComponents(" ");
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
					for(Player mentioned : plugin.getServer().getAllPlayers())
					{
						if(mentioned.getUsername().toLowerCase().startsWith(name.toLowerCase()))
						{
							name = mentioned.getUsername();
							components.addMention(mentioned.getUsername());
							hasFindOne = true;
							break;
						}
					}
					if(!hasFindOne)
					{
						name = f;
					}
					String tc = ChatApi.hover(
							plugin.getYamlHandler().getConfig().getString("ChatReplacer.Mention.Color")+name,
							"SHOW_TEXT",
							plugin.getYamlHandler().getLang().getString("ChatListener.Mention.MentionHover")
							.replace("%target%", name)
							.replace("%player%", player.getUsername()));
					components.addAllComponents(tc);
					if(count < function.length)
					{
						components.addAllComponents(" ");
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
						String tc = plugin.getYamlHandler().getConfig().getString("ChatReplacer.Position.Replace")
								.replace("%server%", sl.getServer())
								.replace("%world%", sl.getWordName())
								.replace("%x%", String.valueOf((int) sl.getX()))
								.replace("%y%", String.valueOf((int) sl.getY()))
								.replace("%z%", String.valueOf((int) sl.getZ()));
						components.addAllComponents(tc);
						if(count < function.length)
						{
							components.addAllComponents(" ");
						}
						continue;
					}
				}
			} else if(f.equals(plugin.getYamlHandler().getConfig().getString("ChatReplacer.NewLine")))
			{
				if(newlineCounter <= 5)
				{
					components.addAllComponents("<newline>");
					newlineCounter++;
					//As only function, to have a Space after and before it.
					continue;
				}
				newlineCounter++;
			} else if(f.contains(plugin.getYamlHandler().getConfig().getString("ChatReplacer.NewLine")))
			{
				if(newlineCounter <= 5)
				{
					components.addAllComponents(f.replace(plugin.getYamlHandler().getConfig().getString("ChatReplacer.NewLine"), "<newline>"));
					newlineCounter++;
					continue;
				}
				newlineCounter++;
			}
			components.addAllComponents(f);
			if(count < function.length)
			{
				components.addAllComponents(" ");
			}
		}
		if(replacerCount > 5)
		{
			players.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("ChatListener.ToManyReplacer")));
			if(players instanceof ConsoleCommandSource)
			{
				breakChat.add("console");
			} else if(players instanceof Player)
			{
				breakChat.add(player.getUniqueId().toString());
			}
			return components;
		}
		return components;
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
			return isPointInCircle(l1.getX(), l1.getZ(), blockRadius, l2.getX(), l2.getZ());
		}
		return false;
	}
	
	private boolean isInRectangle(double centerX, double centerZ, double radius, 
		    double x, double z)
	{
	        return x >= centerX - radius && x <= centerX + radius && 
	            z >= centerZ - radius && z <= centerZ + radius;
	} 
	
	private boolean isPointInCircle(double centerX, double centerZ, double radius, double x, double z)
	{
	    if(isInRectangle(centerX, centerZ, radius, x, z))
	    {
	        double dx = centerX - x;
	        double dz = centerZ - z;
	        dx *= dx;
	        dz *= dz;
	        double distanceSquared = dx + dz;
	        double radiusSquared = radius * radius;
	        return distanceSquared <= radiusSquared;
	    }
	    return false;
	}
	
	private void sendMessage(ComponentsVelo components, Player player, Channel usedChannel, TemporaryChannel tch, PermanentChannel pc)
	{
		String txc1 = String.join("", components.getComponents());
		String txc2 = String.join("", components.getComponentsWithMentions());
		logging(txc2, usedChannel);
		ArrayList<String> sendedPlayer = new ArrayList<>();
		for(Player toMessage : plugin.getServer().getAllPlayers())
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
				if(!toMessage.getCurrentServer().get().getServerInfo().getName().equals(player.getCurrentServer().get().getServerInfo().getName()))
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
			} else if(!usedChannel.getIncludedServer().isEmpty())
			{
				if(!usedChannel.getIncludedServer().contains(player.getCurrentServer().get().getServerInfo().getName()))
				{
					continue;
				}
			} else if(!usedChannel.getExcludedServer().isEmpty())
			{
				if(usedChannel.getExcludedServer().contains(player.getCurrentServer().get().getServerInfo().getName()))
				{
					continue;
				}
			}
			if(components.isMention(toMessage.getUsername()))
			{
				toMessage.sendMessage(ChatApi.tl(txc2));
				if(toMessage.hasPermission(BypassPermission.USE_SOUND))
				{
					sendMentionPing(toMessage, usedChannel.getMentionSound());
				}
			} else
			{
				getChatHistory(toMessage.getUniqueId()).add(txc1);
				toMessage.sendMessage(ChatApi.tl(txc1));
			}
			sendedPlayer.add(toMessage.getUniqueId().toString());
		}
		spy(sendedPlayer, components, txc1, txc2);
	}
	
	private void sendPrivateMessage(ComponentsVelo components, Player player, Player other, Channel usedChannel)
	{
		String txc1 = String.join("", components.getComponents());
		String txc2 = String.join("", components.getComponentsWithMentions());
		logging(txc2, usedChannel);
		
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
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("ChatListener.PlayerHasPrivateChannelOff")
						.replace("%player%", other.getUsername())));
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
				player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("ChatListener.PlayerIgnoreYou")
						.replace("%player%", other.getUsername())));
				return;
			}
			isIgnored = true;
		}
		plugin.getUtility().isAfk(player, other);
		if(components.isMention(player.getUsername()))
		{
			player.sendMessage(ChatApi.tl(txc2));
			getChatHistory(player.getUniqueId()).add(txc2);
		} else
		{
			player.sendMessage(ChatApi.tl(txc1));
			getChatHistory(player.getUniqueId()).add(txc1);
		}
		sendedPlayer.add(player.getUniqueId().toString());
		if(components.isMention(other.getUsername()))
		{
			other.sendMessage(ChatApi.tl(txc2));
			getChatHistory(other.getUniqueId()).add(txc2);
		} else
		{
			other.sendMessage(ChatApi.tl(txc1));
			getChatHistory(other.getUniqueId()).add(txc1);
		}
		if(plugin.getYamlHandler().getConfig().getBoolean("MsgSoundUsage") &&
				other.hasPermission(BypassPermission.USE_SOUND))
		{
			sendMentionPing(other, usedChannel.getMentionSound());
		}
		sendedPlayer.add(other.getUniqueId().toString());
		if(isIgnored)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("ChatListener.PlayerIgnoreYouButYouBypass")
					.replace("%player%", other.getUsername())));
		}
		sendedPlayer.add(other.getUniqueId().toString());
		spy(sendedPlayer, components, txc1, txc2);
	}
	
	private void sendPrivateConsoleMessage(ComponentsVelo components, CommandSource console, Player other, Channel usedChannel)
	{
		String txc1 = String.join("", components.getComponents());
		String txc2 = String.join("", components.getComponentsWithMentions());
		logging(txc2, usedChannel);
		if(components.isMention(other.getUsername()))
		{
			getChatHistory(other.getUniqueId()).add(txc2);
			other.sendMessage(ChatApi.tl(txc2));
		} else
		{
			getChatHistory(other.getUniqueId()).add(txc1);
			other.sendMessage(ChatApi.tl(txc1));
		}
		if(plugin.getYamlHandler().getConfig().getBoolean("MsgSoundUsage") &&
				other.hasPermission(BypassPermission.USE_SOUND))
		{
			sendMentionPing(other, usedChannel.getMentionSound());
		}
		logging(txc1, usedChannel);
	}
	
	private void spy(ArrayList<String> sendedPlayer, final ComponentsVelo components, final String txc1, final String txc2)
	{
		//Spy Part
		for(Player spy : plugin.getServer().getAllPlayers())
		{
			ChatUser cu = ChatUserHandler.getChatUser(spy.getUniqueId());
			if(spy == null
					|| cu == null
					|| sendedPlayer.contains(cu.getUUID())
					|| !cu.isOptionSpy())
			{
				continue;
			}
			getChatHistory(spy.getUniqueId()).add(txc1);
			spy.sendMessage(ChatApi.tl(txc1));
		}
	}
	
	private void logging(final String txc, Channel c)
	{
		if(c.isLogInConsole())
		{
			plugin.getLogger().log(Level.INFO, txc);
		}
	}
	
	public void sendMentionPing(Player player, String soundEnum)
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
        player.getCurrentServer().get().sendPluginMessage(MinecraftChannelIdentifier.from(StaticValues.SCC_TOSPIGOT), streamout.toByteArray());
	}
	
	public static String stripColor(String input) 
	{
	    if (input == null)
	    {
	    	return null;
	    }
	    return STRIP_COLOR_PATTERN.matcher(input).replaceAll("");
	}
}