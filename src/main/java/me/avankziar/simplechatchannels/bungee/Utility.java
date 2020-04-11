package main.java.me.avankziar.simplechatchannels.bungee;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import main.java.me.avankziar.simplechatchannels.bungee.database.MysqlHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

public class Utility 
{
	private SimpleChatChannels plugin;
	public static LinkedHashMap<String, String> item = new LinkedHashMap<>(); //Playeruuid, ItemJason
	public static LinkedHashMap<String, String> itemname = new LinkedHashMap<>(); //Playeruuid, Itemname
	
	private String prefix;
	private String language;
	
	final public static String 
	PERMBYPASSCOLOR = "scc.channels.bypass.color",
	PERMBYPASSCOMMAND = "scc.channels.bypass.command",
	PERMBYPASSIGNORE = "scc.channels.bypass.ignore",
	PERMBYPASSITEM = "scc.channels.bypass.item",
	PERMBYPASSPRIVACY = "scc.channel.bypass.privacy",
	PERMBYPASSWEBSITE = "scc.channels.bypass.website";
	
	public Utility(SimpleChatChannels plugin)
	{
		this.plugin = plugin;
		setPrefix(plugin.getYamlHandler().get().getString("Prefix"));
		setLanguage(plugin.getYamlHandler().get().getString("Language"));
	}
	
	public String getPrefix()
	{
		return prefix;
	}

	public void setPrefix(String prefix)
	{
		this.prefix = prefix;
	}

	public String getLanguage()
	{
		return language;
	}

	public void setLanguage(String language)
	{
		this.language = language;
	}
	
	//tl = translate
	public String tl(String path)
	{
		return ChatColor.translateAlternateColorCodes('&', path);
	}
	
	public TextComponent tc(String s)
	{
		return new TextComponent(s);
	}
	
	public TextComponent tctl(String s)
	{
		return new TextComponent(ChatColor.translateAlternateColorCodes('&', s));
	}
	
	public TextComponent tctlYaml(String path)
	{
		return new TextComponent(ChatColor.translateAlternateColorCodes('&', plugin.getYamlHandler().getL().getString(path)));
	}
	
	public TextComponent TextWithExtra(String s, List<BaseComponent> list)
	{
		TextComponent tc = tctl(s);
		tc.setExtra(list);
		return tc;
	}
	
	public TextComponent clickEvent(String text, @Nonnull ClickEvent.Action caction, String cmd, boolean yaml)
	{
		TextComponent msg = null;
		if(yaml)
		{
			msg = tctl(plugin.getYamlHandler().getL().getString(text));
		} else
		{
			msg = tctl(text);
		}
		msg.setClickEvent( new ClickEvent(caction, cmd));
		return msg;
	}
	
	public TextComponent hoverEvent(String text, @Nonnull HoverEvent.Action haction, String hover, boolean yaml)
	{
		TextComponent msg = null;
		if(yaml)
		{
			msg = tctl(plugin.getYamlHandler().getL().getString(text));
		} else
		{
			msg = tctl(text);
		}
		msg.setHoverEvent( new HoverEvent(haction, new ComponentBuilder(tl(hover)).create()));
		return msg;
	}
	
	public TextComponent apichat(String text, ClickEvent.Action caction, String cmd,
			HoverEvent.Action haction, String hover, boolean yaml)
	{
		TextComponent msg = null;
		if(yaml)
		{
			msg = tctl(plugin.getYamlHandler().getL().getString(text));
		} else
		{
			msg = tctl(text);
		}
		if(caction != null)
		{
			msg.setClickEvent( new ClickEvent(caction, cmd));
		}
		if(haction != null)
		{
			msg.setHoverEvent( new HoverEvent(haction, new ComponentBuilder(tl(hover)).create()));
		}
		return msg;
	}
	
	/*
	 * itemStringFromReflection see {@link RefectionUtil}
	 */
	public TextComponent apiChatItem(@Nonnull String text, @Nullable ClickEvent.Action caction, @Nullable String cmd,
			@Nonnull String itemStringFromReflection)
	{
		TextComponent msg = tc(tl(text));
		if(caction != null && cmd != null)
		{
			msg.setClickEvent( new ClickEvent(caction, cmd));
		}
		msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, 
				new BaseComponent[]{new TextComponent(itemStringFromReflection)}));
		return msg;
	}
	
	public void sendMessage(ProxiedPlayer player, String path)
	{
		player.sendMessage(tc(tl(path)));
	}
	
	public  String getDate(String format)
	{
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String dt = sdf.format(now);
		return dt;
	}
	
	public List<BaseComponent> getAllTextComponentForChannels(ProxiedPlayer p, String eventmsg,
			String channelname, String channelsymbol, int substring)
	{
		TextComponent channel = apichat(language+".Channels."+channelname,
				ClickEvent.Action.SUGGEST_COMMAND, channelsymbol, 
				HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getL().getString(
						language+".ChannelExtra.Hover."+channelname), true);
		
		List<BaseComponent> prefix = getPrefix(p);
		
		TextComponent player = apichat(getFirstPrefixColor(p)+p.getName(), 
				ClickEvent.Action.SUGGEST_COMMAND, plugin.getYamlHandler().getSymbol("PrivateMessage")+p.getName()+" ", 
				HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getL().getString(language+".ChannelExtra.Hover.PrivateMessage")
				.replace("%player%", p.getName()), false);
		
		List<BaseComponent> suffix = getSuffix(p);
		
		List<BaseComponent> msg = msgLater(p,substring,channelname, eventmsg);
		
		return getTCinLine(channel, prefix, player, suffix, msg);
	}
	
	public List<BaseComponent> msgLater(ProxiedPlayer player, int ss, String channel, String msg)
	{
		String channelsplit = plugin.getYamlHandler().getL().getString(language+".ChatSplit."+channel);
		String rawmsg = msg.substring(ss);
		List<BaseComponent> list = new ArrayList<>();
		list.add(tc(tl(channelsplit)));
		String[] fullmsg = rawmsg.split(" ");
		String cc = plugin.getYamlHandler().getL().getString(language+".ChannelColor."+channel);
		String safeColor = null;
		for(String splitmsg : fullmsg)
		{
			if(player.hasPermission(PERMBYPASSCOLOR))
			{
				if(hasColor(splitmsg))
				{
					safeColor = getSafeColor(splitmsg);
					TextComponent word = tc(tl(splitmsg+" "));
					list.add(word);
				} else
				{
					if(safeColor==null)
					{
						safeColor = cc;
					}
					TextComponent word = tc(tl(splitmsg+" "));
					list.add(word);
				}
			} else
			{
				String colorFreeWord = removeColor(splitmsg);
				TextComponent word = tc(tl(colorFreeWord+" "));
				list.add(addFunctions(player, splitmsg, colorFreeWord, word, cc));
			}
			
		}
		return list;
	}
	
	private BaseComponent addFunctions(ProxiedPlayer player, String splitmsg, String colorFreeWord, TextComponent word, String cc)
	{
		if(splitmsg.contains("http"))
		{
			if(player.hasPermission(PERMBYPASSWEBSITE))
			{
				word = tctl(removeColor(colorFreeWord));
				word.setClickEvent( new ClickEvent(ClickEvent.Action.OPEN_URL,
						colorFreeWord));
				word.setColor(getFristUseColor(plugin.getYamlHandler().getL().getString(language+".ReplacerColor.Website")));
			}
		} else if(splitmsg.contains(plugin.getYamlHandler().getL().getString(language+".ReplacerBlock.Item")))
		{
			if(player.hasPermission(PERMBYPASSITEM))
			{
				if(Utility.itemname.containsKey(player.getUniqueId().toString())
						&& Utility.item.containsKey(player.getUniqueId().toString()))
				{
					word = tctl(removeColor(Utility.itemname.get(player.getUniqueId().toString()))+" ");
					word.setColor(getFristUseColor(plugin.getYamlHandler().getL().getString(language+".ReplacerColor.Item")));
					word.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, 
							new BaseComponent[]{new TextComponent(Utility.item.get(player.getUniqueId().toString()))}));
				}
			}
		} else if(splitmsg.contains(plugin.getYamlHandler().getL().getString(language+".ReplacerBlock.Command")))
		{
			if(player.hasPermission(PERMBYPASSCOMMAND))
			{
				word = tctl(colorFreeWord
						.replace(plugin.getYamlHandler().getL().getString(language+".ReplacerBlock.CommandArgSeperator"), " ")
						.replace(plugin.getYamlHandler().getL().getString(language+".ReplacerBlock.Command"), 
								plugin.getYamlHandler().getL().getString(language+".ReplacerBlock.CommandChatOutput"))
						+" ");
				word.setColor(getFristUseColor(plugin.getYamlHandler().getL().getString(language+".ReplacerColor.Command")));
				word.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
						colorFreeWord.replace(plugin.getYamlHandler().getL().getString(language+".ReplacerBlock.CommandArgSeperator"), " ")
						.replace(plugin.getYamlHandler().getL().getString(language+".ReplacerBlock.Command"), "/")));
				word.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
						new ComponentBuilder(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".ReplacerBlock.CommandHover"))).create()));
			}
		} else
		{
			word.setColor(getFristUseColor(cc));
		}
		return word;
	}
	
	private boolean hasColor(String msg)
	{
		if(msg.contains("&0")||msg.contains("&1")||msg.contains("&2")||msg.contains("&3")||msg.contains("&4")
				||msg.contains("&5")||msg.contains("&6")||msg.contains("&7")||msg.contains("&8")||msg.contains("&9")
				||msg.contains("&a")||msg.contains("&A")||msg.contains("&b")||msg.contains("&B")
				||msg.contains("&c")||msg.contains("&C")||msg.contains("&d")||msg.contains("&D")
				||msg.contains("&d")||msg.contains("&D")||msg.contains("&e")||msg.contains("&E")
				||msg.contains("&f")||msg.contains("&f")||msg.contains("&k")||msg.contains("&K")
				||msg.contains("&m")||msg.contains("&M")||msg.contains("&n")||msg.contains("&N")
				||msg.contains("&l")||msg.contains("&L")||msg.contains("&o")||msg.contains("&O")
				||msg.contains("&r")||msg.contains("&R"))
		{
			return true;
		}
		return false;
	}
	
	private String getSafeColor(String s)
	{
		String m = "";
		for(int i = 0; i < s.length(); i++)
		{
		   char c = s.charAt(i);
		   if(c == '&')
		   {
			   if(i+1<s.length())
			   {
				   char d = s.charAt(i+1);
				   if(d == '0' || d == '1' || d == '2' || d == '3' || d == '4' || d == '5' || d == '6'
						   || d == '7' || d == '8' || d == '9' || d == 'a' || d == 'A' || d == 'b' || d == 'B'
						   || d == 'c' || d == 'C' || d == 'd' || d == 'D' || d == 'e' || d == 'E'
						   || d == 'f' || d == 'F' || d == 'k' || d == 'K' || d == 'm' || d == 'M'
						   || d == 'n' || d == 'N' || d == 'l' || d == 'L' || d == 'o' || d == 'O'
						   || d == 'r' || d == 'R')
				   {
					   m = "&"+Character.toString(d);
					   break;
				   }
			   }
		   }
		}
		return m;
	}
	
	private String removeColor(String msg)
	{
		String a = msg.replace("&0", "").replace("&1", "").replace("&2", "").replace("&3", "").replace("&4", "").replace("&5", "")
				.replace("&6", "").replace("&7", "").replace("&8", "").replace("&9", "")
				.replace("&a", "").replace("&b", "").replace("&c", "").replace("&d", "").replace("&e", "").replace("&f", "")
				.replace("&k", "").replace("&l", "").replace("&m", "").replace("&n", "").replace("&o", "").replace("&r", "")
				.replace("&A", "").replace("&B", "").replace("&C", "").replace("&D", "").replace("&E", "").replace("&F", "")
				.replace("&K", "").replace("&L", "").replace("&M", "").replace("&N", "").replace("&O", "").replace("&R", "")
				.replace("§0", "").replace("§1", "").replace("&2", "").replace("§3", "").replace("§4", "").replace("§5", "")
				.replace("§6", "").replace("§7", "").replace("§8", "").replace("§9", "")
				.replace("§a", "").replace("§b", "").replace("§c", "").replace("§d", "").replace("§e", "").replace("§f", "")
				.replace("§k", "").replace("§l", "").replace("§m", "").replace("§n", "").replace("§o", "").replace("§r", "")
				.replace("§A", "").replace("§B", "").replace("§C", "").replace("§D", "").replace("§E", "").replace("§F", "")
				.replace("§K", "").replace("§L", "").replace("§M", "").replace("§N", "").replace("§O", "").replace("§R", "");
		return a;
	}
	
	private ChatColor getFristUseColor(String msg)
	{
		if(msg.startsWith("&0")) {return ChatColor.BLACK;}
		else if(msg.startsWith("&1")) {return ChatColor.DARK_BLUE;}
		else if(msg.startsWith("&2")) {return ChatColor.DARK_GREEN;}
		else if(msg.startsWith("&3")) {return ChatColor.DARK_AQUA;}
		else if(msg.startsWith("&4")) {return ChatColor.DARK_RED;}
		else if(msg.startsWith("&5")) {return ChatColor.DARK_PURPLE;}
		else if(msg.startsWith("&6")) {return ChatColor.GOLD;}
		else if(msg.startsWith("&7")) {return ChatColor.GRAY;}
		else if(msg.startsWith("&8")) {return ChatColor.DARK_GRAY;}
		else if(msg.startsWith("&9")) {return ChatColor.BLUE;}
		else if(msg.startsWith("&a")||msg.startsWith("&A")) {return ChatColor.GREEN;}
		else if(msg.startsWith("&b")||msg.startsWith("&B")) {return ChatColor.AQUA;}
		else if(msg.startsWith("&c")||msg.startsWith("&C")) {return ChatColor.RED;}
		else if(msg.startsWith("&d")||msg.startsWith("&D")) {return ChatColor.LIGHT_PURPLE;}
		else if(msg.startsWith("&e")||msg.startsWith("&E")) {return ChatColor.YELLOW;}
		else if(msg.startsWith("&f")||msg.startsWith("&F")) {return ChatColor.WHITE;}
		else if(msg.startsWith("&k")||msg.startsWith("&K")) {return ChatColor.MAGIC;}
		else if(msg.startsWith("&l")||msg.startsWith("&L")) {return ChatColor.BOLD;}
		else if(msg.startsWith("&m")||msg.startsWith("&M")) {return ChatColor.STRIKETHROUGH;}
		else if(msg.startsWith("&n")||msg.startsWith("&N")) {return ChatColor.UNDERLINE;}
		else if(msg.startsWith("&o")||msg.startsWith("&O")) {return ChatColor.ITALIC;}
		else if(msg.startsWith("&r")||msg.startsWith("&R")) {return ChatColor.RESET;}
		return ChatColor.RESET;
	}

	public void spy(TextComponent msg)
	{
		for(ProxiedPlayer player : ProxyServer.getInstance().getPlayers())
		{
			if((boolean) plugin.getMysqlHandler().getDataI(player, "spy", "player_uuid"))
			{
				player.sendMessage(msg);
			}
		}
	}
	
	public String getPreOrSuffix(String preorsuffix)
	{
		int a = 1;
		int b = Integer.parseInt(plugin.getYamlHandler().getL().getString(language+".PrefixSuffixAmount"));
		while(a<=b)
		{
			if(removeColor(
					plugin.getYamlHandler().getL().getString(language+".Prefix."+String.valueOf(a))).equals(removeColor(preorsuffix)))
			{
				String perm = "scc.prefix."+String.valueOf(a);
				return perm;
			}
			if(removeColor(
					plugin.getYamlHandler().getL().getString(language+".Suffix."+String.valueOf(a))).equals(removeColor(preorsuffix)))
			{
				String perm = "scc.suffix."+String.valueOf(a);
				return perm;
			}
			a++;
		}
		return "scc.no_prefix_suffix";
	}
	
	public boolean getIgnored(ProxiedPlayer player, ProxiedPlayer target)
	{
		if(plugin.getMysqlHandler().existIgnore(player, target.getUniqueId().toString()))
		{
			if(target.hasPermission(PERMBYPASSIGNORE))
			{
				target.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EventChat.PlayerIgnoreYou"))));
				return false;
			}
			return true;
		}
		return false;
	}
	
	public boolean getWordfilter(String msg)
	{
		List<String> wordfilter = plugin.getYamlHandler().get().getStringList("WordFilter");
		for(String wf : wordfilter)
		{
			if(msg.contains(wf))
			{
				return true;
			}
		}
		return false;
	}
	
	public List<BaseComponent> getTCinLine(TextComponent channel, List<BaseComponent> prefix, TextComponent player, 
			List<BaseComponent> suffix, List<BaseComponent> msg)
	{
		List<BaseComponent> list = new ArrayList<>();
		list.add(channel);
		for(BaseComponent bcp : prefix)
		{
			list.add(bcp);
		}
		list.add(player);
		for(BaseComponent bcs : suffix)
		{
			list.add(bcs);
		}
		for(BaseComponent bmsg : msg)
		{
			list.add(bmsg);
		}
		return list;
	}
	
	public List<BaseComponent> getTCinLinePN(TextComponent channel, TextComponent player, TextComponent player2, List<BaseComponent> msg)
	{
		List<BaseComponent> list = new ArrayList<>();
		list.add(channel);
		list.add(player);
		list.add(player2);
		for(BaseComponent bmsg : msg)
		{
			list.add(bmsg);
		}
		return list;
	}
	
	public String getFirstPrefixColor(ProxiedPlayer p)
	{
		Boolean useprefixforplayercolor = plugin.getYamlHandler().get().getBoolean("UsePrefixForPlayerColor", true);
		if(useprefixforplayercolor!=null && useprefixforplayercolor==false)
		{
			return plugin.getYamlHandler().getL().getString(language+".PlayerColor");
		}
		String prefix = null;
		int a = 1;
		int b = Integer.parseInt(plugin.getYamlHandler().getL().getString(language+".PrefixSuffixAmount"));
		while(a<=b)
		{
			if(p.hasPermission("scc.prefix."+String.valueOf(a))) 
			{
				String preorsuffix = plugin.getYamlHandler().getL().getString(language+".Prefix."+String.valueOf(a));
				if(preorsuffix.startsWith("&"))
				{
					prefix = preorsuffix.substring(0, 2);
					break;
				}
			}
			a++;
		}		
		if(prefix == null)
		{
			prefix = plugin.getYamlHandler().getL().getString(language+".PlayerColor");
		}
		return prefix;
	}
	
	public List<BaseComponent> getPrefix(ProxiedPlayer p)
	{
		List<BaseComponent> list = new ArrayList<>();
		int a = 1;
		int b = Integer.parseInt(plugin.getYamlHandler().getL().getString(language+".PrefixSuffixAmount"));
		while(a<=b)
		{
			if(p.hasPermission("scc.prefix."+String.valueOf(a))) 
			{
				String preorsuffix = plugin.getYamlHandler().getL().getString(language+".Prefix."+String.valueOf(a));
				String pors = removeColor(preorsuffix);
				TextComponent prefix = apichat(plugin.getYamlHandler().getL().getString(language+".Prefix."+String.valueOf(a))+" ", 
						ClickEvent.Action.SUGGEST_COMMAND, plugin.getYamlHandler().getSymbol("Group")+pors+" ", 
						HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getL().getString(
								language+".ChannelExtra.Hover.Group"), false);
				list.add(prefix);
			}
			a++;
		}		
		if(list.isEmpty())
		{
			list.add(tc(""));
		}
		return list;
	}
	
	public List<BaseComponent> getSuffix(ProxiedPlayer p)
	{
		List<BaseComponent> list = new ArrayList<>();
		int a = 1;
		int b = Integer.parseInt(plugin.getYamlHandler().getL().getString(language+".PrefixSuffixAmount"));
		while(a<=b)
		{
			if(p.hasPermission("scc.suffix."+String.valueOf(a))) 
			{
				String preorsuffix = plugin.getYamlHandler().getL().getString(language+".Suffix."+String.valueOf(a));
				String pors = removeColor(preorsuffix);
				TextComponent suffix = apichat(" "+plugin.getYamlHandler().getL().getString(language+".Suffix."+String.valueOf(a))+" ", 
						ClickEvent.Action.SUGGEST_COMMAND, plugin.getYamlHandler().getSymbol("Group")+pors+" ", 
						HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getL().getString(
								language+".ChannelExtra.Hover.Group"), false);
				list.add(suffix);
			}
			a++;
		}		
		if(list.isEmpty())
		{
			list.add(tc(""));
		}
		return list;
	}
	
	public boolean hasChannelRights(ProxiedPlayer player, String mysql_channel)
	{
		if(!(boolean) plugin.getMysqlHandler().getDataI(player, mysql_channel, "player_uuid"))
		{
			///Du hast diesen Channel ausgeschaltet. Bitte schalte ihn &7mit &c/scc <channel> &7wieder an.
			player.sendMessage(tctlYaml(language+".EventChat.ChannelIsOff"));
			return false;
		} else
		{
			return true;
		}
	}
	
	public void sendAllMessage(ProxiedPlayer player, String mysql_channel, TextComponent MSG)
	{
		for(ProxiedPlayer all : plugin.getProxy().getPlayers())
		{
			if((boolean) plugin.getMysqlHandler().getDataI(all, mysql_channel, "player_uuid"))
			{
				if(!getIgnored(all,player))
				{
					all.sendMessage(MSG);
				}
			}
		}
	}
	
	public boolean rightArgs(ProxiedPlayer p, String[] args, int i)
    {
    	if(args.length!=i)
    	{
    		///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			p.sendMessage(clickEvent(language+".CmdScc.InputIsWrong",
					ClickEvent.Action.RUN_COMMAND, "/scc", true));
			return true;
    	} else
    	{
    		return false;
    	}
    }
	
	public void sendMessage(Server server, String Channel, String message) 
	{
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        String msg = message;
        try {
			out.writeUTF(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
        server.sendData(Channel, stream.toByteArray());
    }
	
	public String getActiveChannels(ProxiedPlayer player)
	{
		String language = getLanguage();
		MysqlHandler mi = plugin.getMysqlHandler();
		boolean canchat = (boolean)mi.getDataI(player, "can_chat", "player_uuid");
		boolean global = (boolean)mi.getDataI(player, "channel_global", "player_uuid");
		boolean local = (boolean)mi.getDataI(player, "channel_local", "player_uuid");
		boolean auction = (boolean)mi.getDataI(player, "channel_auction", "player_uuid");
		boolean trade = (boolean)mi.getDataI(player, "channel_trade", "player_uuid");
		boolean support = (boolean)mi.getDataI(player, "channel_support", "player_uuid");
		boolean team = (boolean)mi.getDataI(player, "channel_team", "player_uuid");
		boolean admin = (boolean)mi.getDataI(player, "channel_admin", "player_uuid");
		boolean world = (boolean)mi.getDataI(player, "channel_world", "player_uuid");
		boolean group = (boolean)mi.getDataI(player, "channel_group", "player_uuid");
		boolean privatemsg = (boolean)mi.getDataI(player, "channel_pm", "player_uuid");
		boolean custom = (boolean)mi.getDataI(player, "channel_custom", "player_uuid");
		boolean spy = (boolean)mi.getDataI(player, "spy", "player_uuid");
		
		String comma = plugin.getYamlHandler().getL().getString(language+".Join.Comma");
		
		String ac = "";
		///Du hast zurzeit kein Recht im Chat zu schreiben!
		if(!canchat) {ac = plugin.getYamlHandler().getL().getString(language+".EventJoinLeave.msg04"); return ac;}
		ac += plugin.getYamlHandler().getL().getString(language+".Join.Info");
		if(global) {ac += plugin.getYamlHandler().getL().getString(language+".Join.Global")+comma;}
		if(local) {ac += plugin.getYamlHandler().getL().getString(language+".Join.Local")+comma;}
		if(auction) {ac += plugin.getYamlHandler().getL().getString(language+".Join.Auction")+comma;}
		if(trade) {ac += plugin.getYamlHandler().getL().getString(language+".Join.Trade")+comma;}
		if(support) {ac += plugin.getYamlHandler().getL().getString(language+".Join.Support")+comma;}
		if(world) {ac += plugin.getYamlHandler().getL().getString(language+".Join.World")+comma;}
		if(team) {ac += plugin.getYamlHandler().getL().getString(language+".Join.Team")+comma;}
		if(admin) {ac += plugin.getYamlHandler().getL().getString(language+".Join.Admin")+comma;}
		if(group) {ac += plugin.getYamlHandler().getL().getString(language+".Join.Group")+comma;}
		if(privatemsg) {ac += plugin.getYamlHandler().getL().getString(language+".Join.PrivateMessage")+comma;}
		if(custom) {ac += plugin.getYamlHandler().getL().getString(language+".Join.Custom")+comma;}
		if(spy) {ac += plugin.getYamlHandler().getL().getString(language+".Join.Spy")+comma;}
		return ac.substring(0, ac.length()-2);
	}
	
	public void controlChannelSaves(ProxiedPlayer player)
	{
		if(!plugin.getMysqlHandler().hasAccount(player))
		{
			plugin.getMysqlHandler().createAccount(player);
			return;
		} else
		{
			return;
		}
	}
	
	public void sendSpigotMessage(String tagkey, String message)
	{
		ByteArrayOutputStream streamout = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(streamout);
        String msg = message;
        try {
			out.writeUTF(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
        for(ServerInfo si : plugin.getProxy().getServers().values())
        {
        	si.sendData(tagkey, streamout.toByteArray());
        }
	}
}
