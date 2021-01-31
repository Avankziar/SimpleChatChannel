package main.java.me.avankziar.simplechatchannels.spigot.assistance;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;

import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.ChatUser;
import main.java.me.avankziar.simplechatchannels.objects.IgnoreObject;
import main.java.me.avankziar.simplechatchannels.objects.PermanentChannel;
import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.database.MysqlHandler;
import main.java.me.avankziar.simplechatchannels.spigot.objects.ChatUserHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Utility 
{
	private static SimpleChatChannels plugin;
	public static LinkedHashMap<String, String> item = new LinkedHashMap<>(); //Playeruuid, ItemJason
	public static LinkedHashMap<String, String> itemname = new LinkedHashMap<>(); //Playeruuid, Itemname
	
	private String prefix;
	
	public static String 
	PERMBYPASSCOLOR = "",
	PERMBYPASSCOMMAND = "",
	PERMBYPASSIGNORE = "",
	PERMBYPASSITEM = "",
	PERMBYPASSITEMUPLOAD = "",
	PERMBYPASSPRIVACY = "",
	PERMBYPASSWEBSITE = "",
	
	PERMBYPASSPCDELETE = "",
	
	PERMPREFIX = "",
	PERMSUFFIX = "",
	
	PERMGLOBAL = "",
	PERMTRADE = "",
	PERMAUCTION = "",
	PERMLOCAL = "",
	PERMWORLD = "",
	PERMEVENT = "",
	PERMSUPPORT = "",
	PERMTEAM = "",
	PERMADMIN = "",
	PERMPM = "",
	PERMGROUP = "",
	PERMPERMA = "",
	PERMTEMP = "";
	
	public Utility(SimpleChatChannels plugin)
	{
		Utility.plugin = plugin;
		loadUtility();
		setPermissions();
	}
	
	public boolean loadUtility()
	{
		setPrefix(plugin.getYamlHandler().get().getString("Prefix", "&7[&eS&6c&cc&7] &r"));
		return true;
	}
	
	public void setPermissions()
	{
		PERMBYPASSCOLOR = plugin.getYamlHandler().getCom().getString("Bypass.Color", "scc.channels.bypass.color");
		PERMBYPASSCOMMAND = plugin.getYamlHandler().getCom().getString("Bypass.Command", "scc.channels.bypass.command");
		PERMBYPASSIGNORE = plugin.getYamlHandler().getCom().getString("Bypass.Ignore", "scc.channels.bypass.ignore");
		PERMBYPASSITEM = plugin.getYamlHandler().getCom().getString("Bypass.Item", "scc.channels.bypass.item");
		PERMBYPASSITEMUPLOAD = plugin.getYamlHandler().getCom().getString("Bypass.ItemUpload", "scc.channels.bypass.itemupload");
		PERMBYPASSPRIVACY = plugin.getYamlHandler().getCom().getString("Bypass.Privacy", "scc.channels.bypass.privacy");
		PERMBYPASSWEBSITE = plugin.getYamlHandler().getCom().getString("Bypass.Website", "scc.channels.bypass.website");
		PERMBYPASSPCDELETE = plugin.getYamlHandler().getCom().getString("Bypass.PermanentChannel", "scc.channels.bypass.permanentchannel");
		
		PERMPREFIX = plugin.getYamlHandler().getCom().getString("Custom.Prefix", "scc.prefix.");
		PERMSUFFIX = plugin.getYamlHandler().getCom().getString("Custom.Suffix", "scc.suffix.");
		
		PERMGLOBAL = plugin.getYamlHandler().getCom().getString("scc_global.Permission", "scc.channels.global");
		PERMTRADE = plugin.getYamlHandler().getCom().getString("scc_trade.Permission", "scc.channels.trade");
		PERMAUCTION = plugin.getYamlHandler().getCom().getString("scc_auction.Permission", "scc.channels.auction");
		PERMLOCAL = plugin.getYamlHandler().getCom().getString("scc_local.Permission", "scc.channels.local");
		PERMWORLD = plugin.getYamlHandler().getCom().getString("scc_world.Permission", "scc.channels.world");
		PERMEVENT = plugin.getYamlHandler().getCom().getString("scc_event.Permission", "scc.channels.event");
		PERMSUPPORT = plugin.getYamlHandler().getCom().getString("scc_support.Permission", "scc.channels.support");
		PERMTEAM = plugin.getYamlHandler().getCom().getString("scc_team.Permission", "scc.channels.team");
		PERMADMIN = plugin.getYamlHandler().getCom().getString("scc_admin.Permission", "scc.channels.admin");
		PERMPM = plugin.getYamlHandler().getCom().getString("scc_pm.Permission", "scc.channels.pm");
		PERMGROUP = plugin.getYamlHandler().getCom().getString("scc_group.Permission", "scc.channels.group");
		PERMPERMA = plugin.getYamlHandler().getCom().getString("scc_perma.Permission", "scc.channels.perma");
		PERMTEMP = plugin.getYamlHandler().getCom().getString("scc_temp.Permission", "scc.channels.temp");
		
	}
	
	public String getPrefix()
	{
		return prefix;
	}

	public void setPrefix(String prefix)
	{
		this.prefix = prefix;
	}
	
	/**
	 * <ul><b><u>getDurationInSeconds</u></b>
	 * <pre><p><i>String tl</i></pre>
	 * <p><b>Deutsch:</b> Gibt einen String zurück, wo &x Zeichenpaare in den ChatColor umgewandelt wird. 
	 * tl steht für das translate als Abkürzung.
	 * @return String
	 * @author Avankziar
	 */
	
	public TextComponent TextWithExtra(String s, List<BaseComponent> list)
	{
		TextComponent tc = ChatApi.tctl(s);
		tc.setExtra(list);
		return tc;
	}
	
	/*
	 * itemStringFromReflection see {@link RefectionUtil}
	 */
	@SuppressWarnings("deprecation")
	public TextComponent apiChatItem(@Nonnull String text, @Nullable ClickEvent.Action caction, @Nullable String cmd,
			@Nonnull String itemStringFromReflection)
	{
		TextComponent msg = ChatApi.tctl(text);
		if(caction != null && cmd != null)
		{
			msg.setClickEvent( new ClickEvent(caction, cmd));
		}
		msg.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, 
				new BaseComponent[]{new TextComponent(itemStringFromReflection)}));
		return msg;
	}
	
	public  String getDate(String format)
	{
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String dt = sdf.format(now);
		return dt;
	}
	
	public List<BaseComponent> getAllTextComponentForChannels(Player p, String eventmsg,
			String channelname, String channelsymbol, int substring, boolean time, String timevalue)
	{
		TextComponent channel = ChatApi.apiChat(plugin.getYamlHandler().getL().getString("Channels."+channelname),
				ClickEvent.Action.SUGGEST_COMMAND, channelsymbol, 
				HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getL().getString(
						"ChannelExtra.Hover."+channelname));
		
		List<BaseComponent> prefix = getPrefix(p);
		
		TextComponent player = ChatApi.apiChat(getFirstPrefixColor(p)+p.getName(), 
				ClickEvent.Action.SUGGEST_COMMAND, plugin.getYamlHandler().getSymbol("PrivateMessage")+p.getName()+" ", 
				HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getL().getString("ChannelExtra.Hover.PrivateMessage")
				.replace("%player%", p.getName()));
		
		List<BaseComponent> suffix = getSuffix(p);
		
		List<BaseComponent> msg = msgLater(p,substring,channelname, eventmsg);
		
		return getTCinLine(channel, prefix, player, suffix, msg, time, ChatApi.tc(timevalue));
	}
	
	public List<BaseComponent> msgLater(Player player, int ss, String channel, String msg)
	{
		String channelsplit = plugin.getYamlHandler().getL().getString("ChatSplit."+channel);
		String rawmsg = msg.substring(ss);
		List<BaseComponent> list = new ArrayList<>();
		list.add(ChatApi.tctl(channelsplit));
		String[] fullmsg = rawmsg.split(" ");
		String cc = plugin.getYamlHandler().getL().getString("ChannelColor."+channel);
		String safeColor = null;
		for(String splitmsg : fullmsg)
		{
			String colorFreeWord = removeColor(splitmsg);
			if(player.hasPermission(PERMBYPASSCOLOR))
			{
				if(hasColor(splitmsg))
				{
					if(safeColor==null)
					{
						safeColor = cc;
					}
					TextComponent word = ChatApi.tctl(safeColor+splitmsg+" ");
					safeColor = getSafeColor(splitmsg);
					list.add(addFunctions(player, splitmsg, colorFreeWord, word, cc, channel));
				} else
				{
					if(safeColor==null)
					{
						safeColor = cc;
					}
					TextComponent word = ChatApi.tctl(safeColor+splitmsg+" ");
					list.add(addFunctions(player, splitmsg, colorFreeWord, word, cc, channel));
				}
			} else
			{
				TextComponent word = ChatApi.tctl(colorFreeWord+" ");
				list.add(addFunctions(player, splitmsg, colorFreeWord, word, cc, channel));
			}
			
		}
		return list;
	}
	
	public List<BaseComponent> msgPerma(Player player, int ss, String channel, String msg, String chatcolor)
	{
		String channelsplit = plugin.getYamlHandler().getL().getString("ChatSplit."+channel);
		String rawmsg = msg.substring(ss);
		List<BaseComponent> list = new ArrayList<>();
		list.add(ChatApi.tctl(channelsplit));
		String[] fullmsg = rawmsg.split(" ");
		String cc = chatcolor;
		String safeColor = null;
		for(String splitmsg : fullmsg)
		{
			String colorFreeWord = removeColor(splitmsg);
			if(player.hasPermission(PERMBYPASSCOLOR))
			{
				if(hasColor(splitmsg))
				{
					if(safeColor==null)
					{
						safeColor = cc;
					}
					TextComponent word = ChatApi.tctl(safeColor+splitmsg+" ");
					safeColor = getSafeColor(splitmsg);
					list.add(addFunctions(player, splitmsg, colorFreeWord, word, safeColor, channel));
				} else
				{
					if(safeColor==null)
					{
						safeColor = cc;
					}
					TextComponent word = ChatApi.tctl(safeColor+splitmsg+" ");
					list.add(addFunctions(player, splitmsg, colorFreeWord, word, safeColor, channel));
				}
			} else
			{
				TextComponent word = ChatApi.tctl(colorFreeWord+" ");
				list.add(addFunctions(player, splitmsg, colorFreeWord, word, cc, channel));
			}
			
		}
		return list;
	}
	
	public List<BaseComponent> broadcast(Player player, int ss, String channel, String msg, TextComponent intro)
	{
		String rawmsg = msg.substring(ss);
		List<BaseComponent> list = new ArrayList<>();
		list.add(intro);
		String[] fullmsg = rawmsg.split(" ");
		String cc = plugin.getYamlHandler().getL().getString("ChannelColor."+channel);
		String safeColor = null;
		for(String splitmsg : fullmsg)
		{
			String colorFreeWord = removeColor(splitmsg);
			if(player.hasPermission(PERMBYPASSCOLOR))
			{
				if(hasColor(splitmsg))
				{
					if(safeColor==null)
					{
						safeColor = cc;
					}
					TextComponent word = ChatApi.tctl(safeColor+splitmsg+" ");
					safeColor = getSafeColor(splitmsg);
					list.add(addFunctions(player, splitmsg, colorFreeWord, word, cc, channel));
				} else
				{
					if(safeColor==null)
					{
						safeColor = cc;
					}
					TextComponent word = ChatApi.tctl(safeColor+splitmsg+" ");
					list.add(addFunctions(player, splitmsg, colorFreeWord, word, cc, channel));
				}
			} else
			{
				TextComponent word = ChatApi.tctl(colorFreeWord+" ");
				list.add(addFunctions(player, splitmsg, colorFreeWord, word, cc, channel));
			}
			
		}
		return list;
	}
	
	
	@SuppressWarnings("deprecation")
	private BaseComponent addFunctions(Player player, String splitmsg, String colorFreeWord, TextComponent word, String cc,
			String channel)
	{
		if(splitmsg.contains("http"))
		{
			if(player.hasPermission(PERMBYPASSWEBSITE))
			{
				word = ChatApi.tctl(removeColor(colorFreeWord));
				word.setClickEvent( new ClickEvent(ClickEvent.Action.OPEN_URL,
						colorFreeWord));
				word.setColor(getFristUseColor(plugin.getYamlHandler().getL().getString("ReplacerColor.Website")));
			}
		} else if(splitmsg.contains(plugin.getYamlHandler().getL().getString("ReplacerBlock.Item")))
		{
			if(player.hasPermission(PERMBYPASSITEM+"."+channel.toLowerCase()))
			{
				if(Utility.itemname.containsKey(player.getUniqueId().toString())
						&& Utility.item.containsKey(player.getUniqueId().toString()))
				{
					word = ChatApi.tctl(Utility.itemname.get(player.getUniqueId().toString())+" ");
					word.setColor(getFristUseColor(plugin.getYamlHandler().getL().getString("ReplacerColor.Item")));
					word.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, 
							new BaseComponent[]{new TextComponent(Utility.item.get(player.getUniqueId().toString()))}));
				}
			}
		} else if(splitmsg.contains(plugin.getYamlHandler().getL().getString("ReplacerBlock.Command")))
		{
			if(player.hasPermission(PERMBYPASSCOMMAND))
			{
				word = ChatApi.tctl(colorFreeWord
						.replace(plugin.getYamlHandler().getL().getString("ReplacerBlock.CommandArgSeperator"), " ")
						.replace(plugin.getYamlHandler().getL().getString("ReplacerBlock.Command"), 
								plugin.getYamlHandler().getL().getString("ReplacerBlock.CommandChatOutput"))
						+" ");
				word.setColor(getFristUseColor(plugin.getYamlHandler().getL().getString("ReplacerColor.Command")));
				word.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
						colorFreeWord.replace(plugin.getYamlHandler().getL().getString("ReplacerBlock.CommandArgSeperator"), " ")
						.replace(plugin.getYamlHandler().getL().getString("ReplacerBlock.Command"), "/")));
				word.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
						new ComponentBuilder(ChatApi.tl(
						plugin.getYamlHandler().getL().getString("ReplacerBlock.CommandHover"))).create()));
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
				   }
			   }
		   }
		}
		return m;
	}
	
	public String removeColor(String msg)
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
	
	public String getPreOrSuffix(String preorsuffix)
	{
		int a = 1;
		int b = Integer.parseInt(plugin.getYamlHandler().getL().getString("PrefixSuffixAmount"));
		while(a<=b)
		{
			if(removeColor(
					plugin.getYamlHandler().getL().getString("Prefix."+String.valueOf(a))).equals(removeColor(preorsuffix)))
			{
				String perm = PERMPREFIX+String.valueOf(a);
				return perm;
			}
			if(removeColor(
					plugin.getYamlHandler().getL().getString("Suffix."+String.valueOf(a))).equals(removeColor(preorsuffix)))
			{
				String perm = PERMSUFFIX+String.valueOf(a);
				return perm;
			}
			a++;
		}
		return "scc.no_prefix_suffix";
	}
	
	public List<BaseComponent> getPrefix(Player p)
	{
		List<BaseComponent> list = new ArrayList<>();
		int a = 1;
		int b = Integer.parseInt(plugin.getYamlHandler().getL().getString("PrefixSuffixAmount"));
		while(a<=b)
		{
			if(p.hasPermission(PERMPREFIX+String.valueOf(a))) 
			{
				String preorsuffix = plugin.getYamlHandler().getL().getString("Prefix."+String.valueOf(a));
				if(preorsuffix != null)
				{
					String pors = removeColor(preorsuffix);
					TextComponent prefix = ChatApi.apiChat(plugin.getYamlHandler().getL().getString("Prefix."+String.valueOf(a))+" ", 
							ClickEvent.Action.SUGGEST_COMMAND, plugin.getYamlHandler().getSymbol("Group")+pors+" ", 
							HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getL().getString(
									"ChannelExtra.Hover.Group"));
					list.add(prefix);
				}
			}
			a++;
		}		
		if(list.isEmpty())
		{
			list.add(ChatApi.tc(""));
		}
		return list;
	}
	
	public List<BaseComponent> getSuffix(Player p)
	{
		List<BaseComponent> list = new ArrayList<>();
		int a = 1;
		int b = Integer.parseInt(plugin.getYamlHandler().getL().getString("PrefixSuffixAmount"));
		ArrayList<String> perms = new ArrayList<>();
		while(a<=b)
		{
			if(p.hasPermission(PERMSUFFIX+String.valueOf(a))) 
			{
				perms.add(PERMSUFFIX+String.valueOf(a)); //TODO
				String preorsuffix = plugin.getYamlHandler().getL().getString("Suffix."+String.valueOf(a));
				if(preorsuffix != null)
				{
					String pors = removeColor(preorsuffix);
					TextComponent suffix = ChatApi.apiChat(" "+plugin.getYamlHandler().getL().getString("Suffix."+String.valueOf(a))+" ", 
							ClickEvent.Action.SUGGEST_COMMAND, plugin.getYamlHandler().getSymbol("Group")+pors+" ", 
							HoverEvent.Action.SHOW_TEXT, plugin.getYamlHandler().getL().getString(
									"ChannelExtra.Hover.Group"));
					list.add(suffix);
				}
			}
			a++;
		}
		if(perms.isEmpty())
		{
			list.add(ChatApi.tc(""));
			return list;
		}
		return list;
	}
	
	public List<BaseComponent> getTCinLine(TextComponent channel, List<BaseComponent> prefix, TextComponent player, 
			List<BaseComponent> suffix, List<BaseComponent> msg, boolean time, TextComponent timevalue)
	{
		List<BaseComponent> list = new ArrayList<>();
		if(time)
		{
			list.add(timevalue);
		}
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
	
	public List<BaseComponent> getTCinLinePN(TextComponent channel, TextComponent player, TextComponent player2,
			List<BaseComponent> msg, boolean time, TextComponent timevalue)
	{
		List<BaseComponent> list = new ArrayList<>();
		if(time)
		{
			list.add(timevalue);
		}
		list.add(channel);
		list.add(player);
		list.add(player2);
		for(BaseComponent bmsg : msg)
		{
			list.add(bmsg);
		}
		return list;
	}
	
	public int countCharacters(String s, String character)
	{
        int i1 = s.length();
        String s2 = s.replace(character,""); 
        int i2 = i1-s2.length();
        return i2;
    }
	
	public void spy(TextComponent msg)
	{
		for(Player player : plugin.getServer().getOnlinePlayers())
		{
			ChatUser allcu = ChatUserHandler.getChatUser(player.getUniqueId());
			if(allcu != null)
			{
				if(allcu.isOptionSpy())
				{
					player.spigot().sendMessage(msg);
				}
			}
		}
	}
	
	public boolean getIgnored(Player target, Player player, boolean privatechat)
	{
		IgnoreObject io = (IgnoreObject) plugin.getMysqlHandler().getData(MysqlHandler.Type.IGNOREOBJECT,
				"`player_uuid` = ? AND `ignore_uuid` = ?",
				target.getUniqueId().toString(), player.getUniqueId().toString());
		if(io != null)
		{
			if(privatechat)
			{
				if(player.hasPermission(PERMBYPASSIGNORE))
				{
					player.spigot().sendMessage(ChatApi.tctl(
							plugin.getYamlHandler().getL().getString("EventChat.PlayerIgnoreYou")));
					return false;
				}
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
			if(containsIgnoreCase(msg, wf))
			{
				return true;
			}
		}
		return false;
	}
	
	public String getFirstPrefixColor(Player p)
	{
		Boolean useprefixforplayercolor = plugin.getYamlHandler().get().getBoolean("UsePrefixForPlayerColor", true);
		if(useprefixforplayercolor!=null && useprefixforplayercolor==false)
		{
			return plugin.getYamlHandler().getL().getString("PlayerColor");
		}
		String prefix = null;
		int a = 1;
		int b = Integer.parseInt(plugin.getYamlHandler().getL().getString("PrefixSuffixAmount"));
		while(a<=b)
		{
			if(p.hasPermission("scc.prefix."+String.valueOf(a))) 
			{
				String preorsuffix = plugin.getYamlHandler().getL().getString("Prefix."+String.valueOf(a));
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
			prefix = plugin.getYamlHandler().getL().getString("PlayerColor");
		}
		return prefix;
	}
	
	public boolean hasChannelRights(Player player, String mysql_channel)
	{
		ChatUser allcu = ChatUserHandler.getChatUser(player.getUniqueId());
		if(plugin.getYamlHandler().get().getBoolean("Bungee", false))
		{
			allcu = (ChatUser) plugin.getMysqlHandler().getData(MysqlHandler.Type.CHATUSER, 
					"`player_uuid` = ?", player.getUniqueId().toString());
		}
		if(allcu != null)
		{
			if(!ChatUser.getBoolean(allcu, mysql_channel))
			{
				///Du hast diesen Channel ausgeschaltet. Bitte schalte ihn &7mit &c/scc <channel> &7wieder an.
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("EventChat.ChannelIsOff")));
				return false;
			}
		}
		return true;
	}
	
	public void sendAllMessage(Player player, String mysql_channel, TextComponent MSG, boolean privatechat)
	{
		for(Player all : plugin.getServer().getOnlinePlayers())
		{
			ChatUser allcu = ChatUserHandler.getChatUser(all.getUniqueId());
			if(allcu != null)
			{
				if(plugin.getYamlHandler().get().getBoolean("Bungee", false))
				{
					allcu = (ChatUser) plugin.getMysqlHandler().getData(MysqlHandler.Type.CHATUSER, 
							"`player_uuid` = ?", all.getUniqueId().toString());
				}
				if(ChatUser.getBoolean(allcu, mysql_channel))
				{
					if(!getIgnored(all,player, privatechat))
					{
						all.spigot().sendMessage(MSG);
					}
				}
			}
		}
	}
	
	public boolean rightArgs(Player p, String[] args, int i)
    {
    	if(args.length!=i)
    	{
    		///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
			p.spigot().sendMessage(ChatApi.clickEvent(plugin.getYamlHandler().getL().getString("CmdScc.InputIsWrong"),
					ClickEvent.Action.RUN_COMMAND, "/scc"));
			return true;
    	} else
    	{
    		return false;
    	}
    }
	
	public String getActiveChannels(ChatUser cu)
	{		
		String comma = plugin.getYamlHandler().getL().getString("Join.Comma");
		
		String ac = "";
		///Du hast zurzeit kein Recht im Chat zu schreiben!
		if(!cu.isCanChat()) {ac = plugin.getYamlHandler().getL().getString("EventJoinLeave.NoRight"); return ac;}
		ac += plugin.getYamlHandler().getL().getString("Join.Info");
		if(cu.isChannelGlobal()) {ac += plugin.getYamlHandler().getL().getString("Join.Global")+comma;}
		if(cu.isChannelLocal()) {ac += plugin.getYamlHandler().getL().getString("Join.Local")+comma;}
		if(cu.isChannelAuction()) {ac += plugin.getYamlHandler().getL().getString("Join.Auction")+comma;}
		if(cu.isChannelEvent()) {ac += plugin.getYamlHandler().getL().getString("Join.Event")+comma;}
		if(cu.isChannelTrade()) {ac += plugin.getYamlHandler().getL().getString("Join.Trade")+comma;}
		if(cu.isChannelSupport()) {ac += plugin.getYamlHandler().getL().getString("Join.Support")+comma;}
		if(cu.isChannelWorld()) {ac += plugin.getYamlHandler().getL().getString("Join.World")+comma;}
		if(cu.isChannelTeam()) {ac += plugin.getYamlHandler().getL().getString("Join.Team")+comma;}
		if(cu.isChannelAdmin()) {ac += plugin.getYamlHandler().getL().getString("Join.Admin")+comma;}
		if(cu.isChannelGroup()) {ac += plugin.getYamlHandler().getL().getString("Join.Group")+comma;}
		if(cu.isChannelPrivateMessage()) {ac += plugin.getYamlHandler().getL().getString("Join.PrivateMessage")+comma;}
		if(cu.isChannelTemporary()) {ac += plugin.getYamlHandler().getL().getString("Join.Temp")+comma;}
		if(cu.isChannelPermanent()) {ac += plugin.getYamlHandler().getL().getString("Join.Perma")+comma;}
		if(cu.isOptionSpy()) {ac += plugin.getYamlHandler().getL().getString("Join.Spy")+comma;}
		return ac.substring(0, ac.length()-2);
	}
	
	public ChatUser controlChannelSaves(Player player)
	{
		ChatUser cu = new ChatUser(
				player.getUniqueId().toString(),
				player.getName(),
				true, 
				0,
				player.hasPermission(PERMGLOBAL),
				player.hasPermission(PERMTRADE),
				player.hasPermission(PERMAUCTION),
				player.hasPermission(PERMSUPPORT),
				player.hasPermission(PERMLOCAL),
				player.hasPermission(PERMWORLD),
				player.hasPermission(PERMTEAM),
				player.hasPermission(PERMADMIN),
				player.hasPermission(PERMGROUP),
				player.hasPermission(PERMEVENT),
				player.hasPermission(PERMPM),
				player.hasPermission(PERMTEMP),
				player.hasPermission(PERMPERMA),
				false, true);
		if(!plugin.getMysqlHandler().exist(MysqlHandler.Type.CHATUSER,
				"`player_uuid` = ?", player.getUniqueId().toString()))
		{
			plugin.getMysqlHandler().create(MysqlHandler.Type.CHATUSER, cu);
		} else
		{
			cu = (ChatUser) plugin.getMysqlHandler().getData(MysqlHandler.Type.CHATUSER, 
					"`player_uuid` = ?", player.getUniqueId().toString());
		}
		ChatUser.addChatUser(cu);
		return cu;
	}
	
	public boolean existMethod(Class<?> externclass, String method)
	{
	    try 
	    {
	    	Method[] mtds = externclass.getMethods();
	    	for(Method methods : mtds)
	    	{
	    		if(methods.getName().equalsIgnoreCase(method))
	    		{
	    	    	//SimpleChatChannels.log.info("Method "+method+" in Class "+externclass.getName()+" loaded!");
	    	    	return true;
	    		}
	    	}
	    	return false;
	    } catch (Exception e) 
	    {
	    	return false;
	    }
	}
	
	public void isAfk(Player player, Player targed)
	{
		if(plugin.getAfkRecord() != null)
		{
			if(existMethod(plugin.getAfkRecord().getClass(), "isAfk"))
			{
				if(plugin.getAfkRecord().isAfk(targed))
				{
					///Der Spieler ist afk!
					player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("AfkRecord.IsAfk")));	
				}
			}
		}
	}
	
	public void saveAfkTimes(Player player)
	{
		if(plugin.getAfkRecord() != null)
		{
			if(existMethod(plugin.getAfkRecord().getClass(), "softSave"))
			{
				plugin.getAfkRecord().softSave(player);
			}
		}
	}
	
	public void updatePermanentChannels(PermanentChannel pc)
	{
		if(plugin.getMysqlHandler().exist(MysqlHandler.Type.PERMANENTCHANNEL, "`id` = ?", pc.getId()))
		{
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.PERMANENTCHANNEL, pc, "`id` = ?", pc.getId());
		}
		PermanentChannel.addCustomChannel(pc);
	}
	
	public static boolean containsIgnoreCase(String message, String searchStr)     
	{
	    if(message == null || searchStr == null) return false;

	    final int length = searchStr.length();
	    if (length == 0)
	        return true;

	    for (int i = message.length() - length; i >= 0; i--) 
	    {
	        if (message.regionMatches(true, i, searchStr, 0, length))
	        {
	        	return true;
	        }
	    }
	    return false;
	}
	
	public void sendBungeeSpyMessage(Player player, String server, String msg)  //FIN
	{
		String µ = "µ";
		String Category = "spy";
        String PlayerUUID = player.getUniqueId().toString();
        String ToServer = server;
		String message = Category+µ+PlayerUUID+µ+ToServer+µ+msg;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(plugin, "simplechatchannels:sccbungee", stream.toByteArray());
    }
	
	public void sendBungeeMessage(Player player, String server, String msg)  //FIN
	{
		String µ = "µ";
		String Category = "spy";
        String PlayerUUID = player.getUniqueId().toString();
        String ToServer = server;
		String message = Category+µ+PlayerUUID+µ+ToServer+µ+msg;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
        plugin.getServer().sendPluginMessage(plugin, "simplechatchannels:sccbungee", stream.toByteArray());
    }
	
	public void sendBungeeItemMessage(Player p, String itemname, String msg)  //FIN
	{
		String µ = "µ";
		String Category = "item";
        String PlayerUUID = p.getUniqueId().toString();
		String message = Category+µ+PlayerUUID+µ+itemname+µ+msg;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
        p.sendPluginMessage(plugin, "simplechatchannels:sccbungee", stream.toByteArray());
    }
	
	public void sendBungeeItemClearMessage(Player p, String itemname, String msg)  //FIN
	{
		String µ = "µ";
		String Category = "itemclear";
        String PlayerUUID = p.getUniqueId().toString();
		String message = Category+µ+PlayerUUID+µ+itemname+µ+msg;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
        p.sendPluginMessage(plugin, "simplechatchannels:sccbungee", stream.toByteArray());
    }
	
	@SuppressWarnings("deprecation")
	public String convertItemStackToJson(ItemStack itemStack) //FIN
	{
		/*
		 * so baut man das manuell
		 * ItemStack is = createHoverItem(p, bookpath+".hover.item."+ar[2], bok);
		 * emptyword.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM,new BaseComponent[]{new TextComponent(convertItemStackToJson((is)))}));
		 */
	    // ItemStack methods to get a net.minecraft.server.ItemStack object for serialization
	    Class<?> craftItemStackClazz = ReflectionUtil.getOBCClass("inventory.CraftItemStack");
	    Method asNMSCopyMethod = ReflectionUtil.getMethod(craftItemStackClazz, "asNMSCopy", ItemStack.class);

	    // NMS Method to serialize a net.minecraft.server.ItemStack to a valid Json string
	    Class<?> nmsItemStackClazz = ReflectionUtil.getNMSClass("ItemStack");
	    Class<?> nbtTagCompoundClazz = ReflectionUtil.getNMSClass("NBTTagCompound");
	    Method saveNmsItemStackMethod = ReflectionUtil.getMethod(nmsItemStackClazz, "save", nbtTagCompoundClazz);

	    Object nmsNbtTagCompoundObj; // This will just be an empty NBTTagCompound instance to invoke the saveNms method
	    Object nmsItemStackObj; // This is the net.minecraft.server.ItemStack object received from the asNMSCopy method
	    Object itemAsJsonObject; // This is the net.minecraft.server.ItemStack after being put through saveNmsItem method

	    try {
	        nmsNbtTagCompoundObj = nbtTagCompoundClazz.newInstance();
	        nmsItemStackObj = asNMSCopyMethod.invoke(null, itemStack);
	        itemAsJsonObject = saveNmsItemStackMethod.invoke(nmsItemStackObj, nmsNbtTagCompoundObj);
	    } catch (Throwable t) {
	        t.printStackTrace();
	        return null;
	    }

	    // Return a string representation of the serialized object
	    return itemAsJsonObject.toString();
	}
	
	public boolean getTarget(Player player) 
    {
    	int range = 20;
    	List<Entity> nearbyE = player.getNearbyEntities(range, range, range);
    	ArrayList<LivingEntity> livingE = new ArrayList<LivingEntity>();
     
    	for (Entity e : nearbyE) {
    		if (e instanceof LivingEntity) 
    		{
    			livingE.add((LivingEntity) e);
    		}
    	}
     
    	LivingEntity target = null;
    	BlockIterator bItr = new BlockIterator(player, range);
    	Block block;
    	Location loc;
    	int bx, by, bz;
    	double ex, ey, ez;
    	// loop through player's line of sight
    	while (bItr.hasNext()) 
    	{
    		block = bItr.next();
    		bx = block.getX();
    		by = block.getY();
    		bz = block.getZ();
    		// check for entities near this block in the line of sight
    		for (LivingEntity e : livingE) 
    		{
    			loc = e.getLocation();
    			ex = loc.getX();
    			ey = loc.getY();
    			ez = loc.getZ();
    			if ((bx - .75 <= ex && ex <= bx + 1.75)
    					&& (bz - .75 <= ez && ez <= bz + 1.75)
    					&& (by - 1 <= ey && ey <= by + 2.5)) {
    				// entity is close enough, set target and stop
    				target = e;
    				break;
    			}
    		}
    	}
    	if(target==null)
    	{
    		return false;
    	}
    	return true;
    }
	
	public static String convertUUIDToName(String uuid)
	{
		String name = null;
		if(plugin.getMysqlHandler().exist(MysqlHandler.Type.CHATUSER, "player_uuid = ?", uuid))
		{
			name = ((ChatUser) plugin.getMysqlHandler().getData(MysqlHandler.Type.CHATUSER,
					"player_uuid = ?", uuid)).getName();
			return name;
		}
		return null;
	}
	
	public static UUID convertNameToUUID(String playername)
	{
		UUID uuid = null;
		if(plugin.getMysqlHandler().exist(MysqlHandler.Type.CHATUSER, "player_name = ?", playername))
		{
			uuid = UUID.fromString(((ChatUser) plugin.getMysqlHandler().getData(MysqlHandler.Type.CHATUSER,
					"player_name = ?", playername)).getUUID());
			return uuid;
		}
		return null;
	}
}
