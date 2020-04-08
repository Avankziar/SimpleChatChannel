package main.java.me.avankziar.simplechatchannels.spigot;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BlockIterator;

import main.java.me.avankziar.simplechatchannels.spigot.database.MysqlInterface;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class Utility 
{
	private SimpleChatChannels plugin;
	private String language;
	public static LinkedHashMap<String, String> item = new LinkedHashMap<>(); //Playeruuid, ItemJason
	public static LinkedHashMap<String, String> itemname = new LinkedHashMap<>(); //Playeruuid, Itemname
	
	public Utility(SimpleChatChannels plugin) 
	{
		this.plugin = plugin;
		language = plugin.getYamlHandler().get().getString("language");
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
	
	public String tl(String path)
	{
		return ChatColor.translateAlternateColorCodes('&', path);
	}
	
	public TextComponent tc(String s)
	{
		return new TextComponent(s);
	}
	
	public TextComponent tcl(String s)
	{
		return new TextComponent(ChatColor.translateAlternateColorCodes('&', s));
	}
	
	public TextComponent suggestCmdText(String lpath, String cmd)
	{
		TextComponent msg = tc(tl(
				plugin.getYamlHandler().getL().getString(lpath)));
		msg.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, cmd));
		return msg;
	}
	
	public TextComponent runCmdText(String lpath, String cmd)
	{
		TextComponent msg = tc(tl(
				plugin.getYamlHandler().getL().getString(language+lpath)));
		msg.setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND, cmd));
		return msg;
	}
	
	public void sendMessage(Player player, String path)
	{
		player.spigot().sendMessage(tc(tl(path)));
	}
	
	public String getDate(String format)
	{
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		String dt = sdf.format(now);
		return dt;
	}
	
	public List<BaseComponent> getAllTextComponentForChannels(Player p, String eventmsg,
			String channelname, String channelsymbol, int substring)
	{
		TextComponent channel = tc(tl(plugin.getYamlHandler().getL().getString(language+".channels."+channelname)));
		channel.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, channelsymbol));
		channel.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
				, new ComponentBuilder(tl(plugin.getYamlHandler().getL().getString(
						language+".channelextra.hover."+channelname))).create()));
		
		List<BaseComponent> prefix = getPrefix(p);
		
		TextComponent player = tc(tl(getFirstPrefixColor(p)+p.getName()));
		player.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, 
				plugin.getYamlHandler().getSymbol("pm")+p.getName()+" "));
		player.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
				, new ComponentBuilder(tl(plugin.getYamlHandler().getL().getString(language+".channelextra.hover.message")
						.replace("%player%", p.getName()))).create()));
		
		List<BaseComponent> suffix = getSuffix(p);
		
		List<BaseComponent> msg = msgLater(p,substring,channelname, eventmsg);
		
		return getTCinLine(channel, prefix, player, suffix, msg);
	}
	
	public List<BaseComponent> msgLater(Player player, int ss, String channel, String msg)
	{
		String channelsplit = plugin.getYamlHandler().getL().getString(language+".chatsplit."+channel);
		String rawmsg = msg.substring(ss);
		List<BaseComponent> list = new ArrayList<>();
		list.add(tc(tl(channelsplit)));
		String[] fullmsg = rawmsg.split(" ");
		String cc = plugin.getYamlHandler().getL().getString(language+".channelcolor."+channel);
		String safeColor = null;
		for(String splitmsg : fullmsg)
		{
			if(player.hasPermission("scc.channels.bypass.color"))
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
	
	private BaseComponent addFunctions(Player player, String splitmsg, String colorFreeWord, TextComponent word, String cc)
	{
		if(splitmsg.contains("http"))
		{
			if(player.hasPermission("scc.channels.bypass.website"))
			{
				word = tc(tl(removeColor(colorFreeWord)));
				word.setClickEvent( new ClickEvent(ClickEvent.Action.OPEN_URL,
						colorFreeWord));
				word.setColor(getFristUseColor(plugin.getYamlHandler().getL().getString(language+".replacercolor.website")));
			}
		} else if(splitmsg.contains(plugin.getYamlHandler().getL().getString(language+".replacerblock.item")))
		{
			if(player.hasPermission("scc.channels.bypass.item"))
			{
				if(Utility.itemname.containsKey(player.getUniqueId().toString())
						&& Utility.item.containsKey(player.getUniqueId().toString()))
				{
					word = tc(tl(removeColor(Utility.itemname.get(player.getUniqueId().toString()))+" "));
					word.setColor(getFristUseColor(plugin.getYamlHandler().getL().getString(language+".replacercolor.item")));
					word.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, 
							new BaseComponent[]{new TextComponent(Utility.item.get(player.getUniqueId().toString()))}));
				}
			}
		} else if(splitmsg.contains(plugin.getYamlHandler().getL().getString(language+".replacerblock.command")))
		{
			if(player.hasPermission("scc.channels.bypass.command"))
			{
				word = tc(tl(colorFreeWord
						.replace(plugin.getYamlHandler().getL().getString(language+".replacerblock.commandargseperator"), " ")
						.replace(plugin.getYamlHandler().getL().getString(language+".replacerblock.command"), 
								plugin.getYamlHandler().getL().getString(language+".replacerblock.commandchatoutput"))
						+" "));
				word.setColor(getFristUseColor(plugin.getYamlHandler().getL().getString(language+".replacercolor.command")));
				word.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, colorFreeWord
						.replace(plugin.getYamlHandler().getL().getString(language+".replacerblock.commandargseperator"), " ")
						.replace(plugin.getYamlHandler().getL().getString(language+".replacerblock.command"), "/")));
				word.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT, 
						new ComponentBuilder(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".replacerblock.commandhover"))).create()));
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

	public void spy(Player player, TextComponent msg)
	{
		if(plugin.getYamlHandler().get().getString("bungee").equalsIgnoreCase("true"))
		{
			sendBungeeSpyMessage(player, "", msg.toLegacyText());
		} else
		{
			for(Player all : plugin.getServer().getOnlinePlayers())
			{
				if((boolean) plugin.getMysqlInterface().getDataI(all, "spy", "player_uuid"))
				{
					all.spigot().sendMessage(msg);
				}
			}
		}
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
	
	public void sendBungeeSpyMessage(Player p, String server, String msg)  //FIN
	{
		String µ = "µ";
		String Category = "spy";
        String PlayerUUID = p.getUniqueId().toString();
        String ToServer = server;
		String message = Category+µ+PlayerUUID+µ+ToServer+µ+msg;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
        p.sendPluginMessage(plugin, "simplechatchannels:sccbungee", stream.toByteArray());
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
	
	public String getPreOrSuffix(String preorsuffix)
	{
		int a = 1;
		int b = Integer.parseInt(plugin.getYamlHandler().getL().getString(language+".prefixsuffixamount"));
		while(a<=b)
		{
			if(plugin.getYamlHandler().getL().getString(language+".prefix."+String.valueOf(a))!=null)
			{
				if(removeColor(plugin.getYamlHandler().getL().getString(language+".prefix."+String.valueOf(a))).equals(removeColor(preorsuffix)))
				{
					String perm = "scc.prefix."+String.valueOf(a);
					return perm;
				}
			}
			if(plugin.getYamlHandler().getL().getString(language+".suffix."+String.valueOf(a))!=null)
			{
				if(removeColor(plugin.getYamlHandler().getL().getString(language+".suffix."+String.valueOf(a))).equals(removeColor(preorsuffix)))
				{
					String perm = "scc.suffix."+String.valueOf(a);
					return perm;
				}
			}
			a++;
		}
		return "scc.no_prefix_suffix";
	}
	
	public boolean getIgnored(Player player, Player target)
	{
		if(plugin.getMysqlInterface().existIgnore(player, target.getUniqueId().toString()))
		{
			if(target.hasPermission("scc.cmd.ignorebypass"))
			{
				target.spigot().sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
						plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg03"))));
				return false;
			}
			return true;
		}
		return false;
	}
	
	public boolean getWordfilter(String msg)
	{
		List<String> wordfilter = plugin.getYamlHandler().get().getStringList("wordfilter");
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
	
	public String getFirstPrefixColor(Player p)
	{
		Boolean useprefixforplayercolor = plugin.getYamlHandler().get().getString("useprefixforplayercolor").equals("true");
		if(useprefixforplayercolor!=null && useprefixforplayercolor==false)
		{
			return plugin.getYamlHandler().getL().getString(language+".playercolor");
		}
		String prefix = null;
		int a = 1;
		int b = Integer.parseInt(plugin.getYamlHandler().getL().getString(language+".prefixsuffixamount"));
		while(a<=b)
		{
			if(p.hasPermission("scc.prefix."+String.valueOf(a))) 
			{
				String preorsuffix = plugin.getYamlHandler().getL().getString(language+".prefix."+String.valueOf(a));
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
			prefix = plugin.getYamlHandler().getL().getString(language+".playercolor");
		}
		return prefix;
	}
	
	public List<BaseComponent> getPrefix(Player p)
	{
		List<BaseComponent> list = new ArrayList<>();
		int a = 1;
		int b = Integer.parseInt(plugin.getYamlHandler().getL().getString(language+".prefixsuffixamount"));
		while(a<=b)
		{
			if(p.hasPermission("scc.prefix."+String.valueOf(a))) 
			{
				if(plugin.getYamlHandler().getL().getString(language+".prefix."+String.valueOf(a))!=null)
				{
					String preorsuffix = plugin.getYamlHandler().getL().getString(language+".prefix."+String.valueOf(a));
					String pors = removeColor(preorsuffix);
					TextComponent prefix = tc(tl(plugin.getYamlHandler().getL().getString(language+".prefix."+String.valueOf(a))+" "));
					prefix.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
							plugin.getYamlHandler().getSymbol("group")+pors +" "));
					prefix.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
							, new ComponentBuilder(tl(plugin.getYamlHandler().getL().getString(
									language+".channelextra.hover.group"))).create()));
					list.add(prefix);
				}
			}
			a++;
		}		
		if(list.isEmpty())
		{
			list.add(tc(""));
		}
		return list;
	}
	
	public List<BaseComponent> getSuffix(Player p)
	{
		List<BaseComponent> list = new ArrayList<>();
		int a = 1;
		int b = Integer.parseInt(plugin.getYamlHandler().getL().getString(language+".prefixsuffixamount"));
		while(a<=b)
		{
			if(p.hasPermission("scc.suffix."+String.valueOf(a))) 
			{
				if(plugin.getYamlHandler().getL().getString(language+".suffix."+String.valueOf(a))!=null)
				{
					String preorsuffix = plugin.getYamlHandler().getL().getString(language+".suffix."+String.valueOf(a));
					String pors = removeColor(preorsuffix);
					TextComponent suffix = tc(tl(" "+plugin.getYamlHandler().getL().getString(language+".suffix."+String.valueOf(a))+" "));
					suffix.setClickEvent( new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND,
							plugin.getYamlHandler().getSymbol("group")+pors+" "));
					suffix.setHoverEvent( new HoverEvent(HoverEvent.Action.SHOW_TEXT
							, new ComponentBuilder(tl(plugin.getYamlHandler().getL().getString(
									language+".channelextra.hover.group"))).create()));
					list.add(suffix);
				}
			}
			a++;
		}		
		if(list.isEmpty())
		{
			list.add(tc(""));
		}
		return list;
	}
	
	public boolean hasChannelRights(Player player, String mysql_channel)
	{
		if(!(boolean) plugin.getMysqlInterface().getDataI(player, mysql_channel, "player_uuid"))
		{
			sendMessage(player,plugin.getYamlHandler().getL().getString(language+".EVENT_Chat.msg01"));
			return false;
		} else
		{
			return true;
		}
	}
	
	public void sendAllMessage(Player player, String mysql_channel, TextComponent MSG)
	{
		for(Player all : plugin.getServer().getOnlinePlayers())
		{
			if((boolean) plugin.getMysqlInterface().getDataI(all, mysql_channel, "player_uuid"))
			{
				if(!getIgnored(all,player))
				{
					all.spigot().sendMessage(MSG);
				}
			}
		}
	}
	
	public boolean rightArgs(Player p, String[] args, int i)
    {
    	if(args.length!=i)
    	{
    		TextComponent msg = tc(tl(plugin.getYamlHandler().getL().getString(language+".CMD_SCC.msg01")));
			msg.setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/scc"));
			p.spigot().sendMessage(msg);
			return true;
    	} else
    	{
    		return false;
    	}
    }
	
	public String getActiveChannels(Player player)
	{
		String language = plugin.getYamlHandler().get().getString("language");
		MysqlInterface mi = plugin.getMysqlInterface();
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
		
		String comma = plugin.getYamlHandler().getL().getString(language+".join.comma");
		
		String ac = "";
		if(!canchat) {ac = plugin.getYamlHandler().getL().getString(language+".EVENT_JoinLeave.msg04"); return ac;}
		ac += plugin.getYamlHandler().getL().getString(language+".join.info");
		if(global) {ac += plugin.getYamlHandler().getL().getString(language+".join.global")+comma;}
		if(local) {ac += plugin.getYamlHandler().getL().getString(language+".join.local")+comma;}
		if(auction) {ac += plugin.getYamlHandler().getL().getString(language+".join.auction")+comma;}
		if(trade) {ac += plugin.getYamlHandler().getL().getString(language+".join.trade")+comma;}
		if(support) {ac += plugin.getYamlHandler().getL().getString(language+".join.support")+comma;}
		if(world) {ac += plugin.getYamlHandler().getL().getString(language+".join.world")+comma;}
		if(team) {ac += plugin.getYamlHandler().getL().getString(language+".join.team")+comma;}
		if(admin) {ac += plugin.getYamlHandler().getL().getString(language+".join.admin")+comma;}
		if(group) {ac += plugin.getYamlHandler().getL().getString(language+".join.group")+comma;}
		if(privatemsg) {ac += plugin.getYamlHandler().getL().getString(language+".join.pm")+comma;}
		if(custom) {ac += plugin.getYamlHandler().getL().getString(language+".join.custom")+comma;}
		if(spy) {ac += plugin.getYamlHandler().getL().getString(language+".join.spy")+comma;}
		return ac.substring(0, ac.length()-2);
	}
	
	public void controlChannelSaves(Player player)
	{
		if(!plugin.getMysqlInterface().hasAccount(player))
		{
			plugin.getMysqlInterface().createAccount(player);
			return;
		} else
		{
			return;
		}
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
}
