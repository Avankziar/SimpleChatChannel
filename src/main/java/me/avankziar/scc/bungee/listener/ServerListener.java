package main.java.me.avankziar.scc.bungee.listener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.UUID;
import java.util.logging.Level;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.scc.bungee.database.MysqlHandler;
import main.java.me.avankziar.scc.bungee.database.MysqlHandler.QueryType;
import main.java.me.avankziar.scc.bungee.handler.ChatHandler;
import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.ServerLocation;
import main.java.me.avankziar.scc.objects.StaticValues;
import main.java.me.avankziar.scc.objects.chat.Channel;
import main.java.me.avankziar.scc.objects.chat.ChatTitle;
import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerListener implements Listener
{
	private SimpleChatChannels plugin;
	
	public ServerListener(SimpleChatChannels plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onSccMessage(PluginMessageEvent event) throws IOException
	{
		if (event.isCancelled()) 
		{
            return;
        }
        if (!event.getTag().equalsIgnoreCase(StaticValues.SCC_TOBUNGEE)) 
        {
            return;
        }
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        String task = in.readUTF();
        if(task.equals(StaticValues.SCC_EDITOR)) 
        {
        	String playername = in.readUTF();
        	boolean toggle = in.readBoolean();
        	if(toggle)
        	{
        		if(!plugin.editorplayers.contains(playername))
        		{
        			plugin.editorplayers.add(playername);
        		}
        	} else
        	{
        		if(plugin.editorplayers.contains(playername))
        		{
        			plugin.editorplayers.remove(playername);
        		}
        	}
        } else if(task.equals(StaticValues.SCC_REGISTERCHANNEL))
        {
        	String uniqueChannelName = in.readUTF();
        	String symbol = in.readUTF();
        	String inChatName = in.readUTF();
        	String inChatColorMessage = in.readUTF();
			String permission = in.readUTF();
			String joinPart = in.readUTF(); 
			String chatFormat = in.readUTF();
			boolean useSpecificServer = in.readBoolean();
			boolean useSpecificWorld = in.readBoolean();
			int useBlockRadius = in.readInt(); 
			long minimumTimeBetweenMessages = in.readLong();
			long minimumTimeBetweenSameMessage = in.readLong();
			double percentOfSimiliarityOrLess = in.readDouble();
			String timeColor = in.readUTF();
			String playernameCustomColor = in.readUTF();
			String seperatorBetweenPrefix = in.readUTF();
			String seperatorBetweenSuffix = in.readUTF();
			String mentionSound = in.readUTF();
			boolean useColor = in.readBoolean();
			boolean useItemReplacer = in.readBoolean();
			boolean useBookReplacer = in.readBoolean();
			boolean useRunCommandReplacer = in.readBoolean();
			boolean useSuggestCommandReplacer = in.readBoolean();
			boolean useWebsiteReplacer = in.readBoolean();
			boolean useEmojiReplacer = in.readBoolean();
			boolean useMentionReplacer = in.readBoolean();
			boolean usePositionReplacer = in.readBoolean();
			if(uniqueChannelName == null || symbol == null || inChatName == null || inChatColorMessage == null
					|| permission == null || joinPart == null || chatFormat == null || timeColor == null
					|| playernameCustomColor == null || seperatorBetweenPrefix == null || seperatorBetweenSuffix == null
					|| mentionSound == null 
					|| uniqueChannelName.equalsIgnoreCase("permanent") || uniqueChannelName.equalsIgnoreCase("temporary")
					|| uniqueChannelName.equalsIgnoreCase("private"))
			{
				return;
			}
			LinkedHashMap<String, String> serverReplacerMap = new LinkedHashMap<>();
			LinkedHashMap<String, String> serverCommandMap = new LinkedHashMap<>();
			LinkedHashMap<String, String> serverHoverMap = new LinkedHashMap<>();
			LinkedHashMap<String, String> worldReplacerMap = new LinkedHashMap<>();
			LinkedHashMap<String, String> worldCommandMap = new LinkedHashMap<>();
			LinkedHashMap<String, String> worldHoverMap = new LinkedHashMap<>();
			Channel c = new Channel(
					uniqueChannelName,
					false,
					symbol,
					inChatName,
					inChatColorMessage,
					permission,
					joinPart,
					chatFormat,
					new ArrayList<>(),
					new ArrayList<>(),
					useSpecificServer,
					useSpecificWorld,
					useBlockRadius,
					minimumTimeBetweenMessages,
					minimumTimeBetweenSameMessage,
					percentOfSimiliarityOrLess,
					timeColor,
					playernameCustomColor,
					"&r",
					seperatorBetweenPrefix,
					seperatorBetweenSuffix,
					mentionSound,
					serverReplacerMap, serverCommandMap, serverHoverMap,
					worldReplacerMap, worldCommandMap, worldHoverMap,
					useColor, useItemReplacer, useBookReplacer,
					useRunCommandReplacer, useSuggestCommandReplacer, useWebsiteReplacer,
					useEmojiReplacer, useMentionReplacer, usePositionReplacer);
			SimpleChatChannels.log.log(Level.INFO, "Register "+c.getUniqueIdentifierName()+" Channel!");
        	return;
        } else if(task.equals(StaticValues.SCC_REGISTERCHATTITLE))
        {
        	String uniquechattitle = in.readUTF();
        	boolean isPrefix = in.readBoolean();
        	String inChatName = in.readUTF();
			String inChatColorCode = in.readUTF();
			String suggestCommand = in.readUTF();
			String hover = in.readUTF();
			String permission = in.readUTF();
			int weight = in.readInt();
			if(uniquechattitle == null || inChatName == null || inChatColorCode == null
					|| suggestCommand == null || hover == null || permission == null)
			{
				return;
			}
			for(ChatTitle ct : SimpleChatChannels.chatTitlesPrefix)
			{
				if(ct.getUniqueIdentifierName().equalsIgnoreCase(uniquechattitle))
				{
					return;
				}
			}
			for(ChatTitle ct : SimpleChatChannels.chatTitlesSuffix)
			{
				if(ct.getUniqueIdentifierName().equalsIgnoreCase(uniquechattitle))
				{
					return;
				}
			}
			ChatTitle ct = new ChatTitle(uniquechattitle,
					isPrefix,
					inChatName,
					inChatColorCode,
					suggestCommand,
					hover,
					permission,
					weight);
			if(isPrefix)
			{
				SimpleChatChannels.chatTitlesPrefix.add(ct);
			} else
			{
				SimpleChatChannels.chatTitlesSuffix.add(ct);
			}
			SimpleChatChannels.chatTitlesPrefix.sort(Comparator.comparing(ChatTitle::getWeight));
			Collections.reverse(SimpleChatChannels.chatTitlesPrefix);
			SimpleChatChannels.chatTitlesSuffix.sort(Comparator.comparing(ChatTitle::getWeight));
			Collections.reverse(SimpleChatChannels.chatTitlesSuffix);
			SimpleChatChannels.log.log(Level.INFO, "Register "+uniquechattitle+" ChatTitle!");
        	return;
        } else if(task.equals(StaticValues.SCC_TASK_LOCATIONUPDATE))
        {
        	String uuid = in.readUTF();
        	String server = in.readUTF();
        	String worldName = in.readUTF();
        	double x = in.readDouble();
        	double y = in.readDouble();
        	double z = in.readDouble();
        	ServerLocation sl = new ServerLocation(server, worldName, x, y, z);
        	if(ChatListener.playerLocation.containsKey(uuid))
        	{
        		ChatListener.playerLocation.replace(uuid, sl);
        	} else
        	{
        		ChatListener.playerLocation.put(uuid, sl);
        	}
        	return;
        } else if(task.equals(StaticValues.SCC_TASK_BROADCAST))
        {
        	String uuid = in.readUTF();
        	String message = in.readUTF();
        	String server = in.readUTF();
        	ChatHandler ch = new ChatHandler(plugin);
        	Channel usedChannel = plugin.getChannel(plugin.getYamlHandler().getConfig().getString("BroadCast.UsingChannel"));
    		if(usedChannel == null)
    		{
    			return;
    		}
        	if(uuid.equalsIgnoreCase("Console"))
        	{
        		CommandSender console = plugin.getProxy().getConsole();
        		ch.sendBroadCast(console, usedChannel, message, server.equals("null") ? null : server);
        	} else
        	{
        		ProxiedPlayer player = plugin.getProxy().getPlayer(UUID.fromString(uuid));
        		ch.sendBroadCast(player, usedChannel, message, server.equals("null") ? null : server);
        	}
        } else if(task.equals(StaticValues.SCC_TASK_W))
        {
        	String uuid = in.readUTF();
        	String message = in.readUTF();
        	ChatHandler ch = new ChatHandler(plugin);
    		CommandSender console = plugin.getProxy().getConsole();
    		ProxiedPlayer other = plugin.getProxy().getPlayer(UUID.fromString(uuid));
    		ch.startPrivateConsoleChat(console, other, message);
    		return;
        } else if(task.equals(StaticValues.SCC_TASK_MYSQLPERFORMANCE))
        {
        	String server = in.readUTF();
        	int inserts = in.readInt();
        	int updates = in.readInt();
        	int deletes = in.readInt();
        	int reads = in.readInt();
        	if(MysqlHandler.serverPerformance.containsKey(server))
        	{
        		LinkedHashMap<MysqlHandler.QueryType, Integer> map = MysqlHandler.serverPerformance.get(server);
        		inserts += (map.containsKey(QueryType.INSERT)) ? map.get(QueryType.INSERT) : 0;
        		updates += (map.containsKey(QueryType.UPDATE)) ? map.get(QueryType.UPDATE) : 0;
        		deletes += (map.containsKey(QueryType.DELETE)) ? map.get(QueryType.DELETE) : 0;
        		reads += (map.containsKey(QueryType.READ)) ? map.get(QueryType.READ) : 0;
        		
        		if(map.containsKey(QueryType.INSERT))
    			{
        			map.replace(QueryType.INSERT, inserts);
    			} else
    			{
    				map.put(QueryType.INSERT, inserts);
    			}
        		if(map.containsKey(QueryType.UPDATE))
    			{
        			map.replace(QueryType.UPDATE, updates);
    			} else
    			{
    				map.put(QueryType.UPDATE, updates);
    			}
        		if(map.containsKey(QueryType.DELETE))
    			{
        			map.replace(QueryType.DELETE, deletes);
    			} else
    			{
    				map.put(QueryType.DELETE, deletes);
    			}
        		if(map.containsKey(QueryType.READ))
    			{
        			map.replace(QueryType.READ, reads);
    			} else
    			{
    				map.put(QueryType.READ, reads);
    			}
        		MysqlHandler.serverPerformance.replace(server, map);
        	} else
        	{
        		LinkedHashMap<MysqlHandler.QueryType, Integer> map = new LinkedHashMap<>();
        		map.put(QueryType.INSERT, inserts);
        		map.put(QueryType.UPDATE, updates);
        		map.put(QueryType.DELETE, deletes);
        		map.put(QueryType.READ, reads);
        		MysqlHandler.serverPerformance.put(server, map);
        	}
        } else if(task.equals(StaticValues.M2BS))
        {
        	String uuid = in.readUTF();
        	boolean s = in.readBoolean();
        	String sound = in.readUTF();
        	boolean p = in.readBoolean();
        	String permission = in.readUTF();
        	boolean hasPermission = in.readBoolean();
        	ArrayList<String> msg = getMessages(in);
        	send(uuid, s, sound, p, hasPermission, permission, msg);
        } else if(task.equals(StaticValues.M2BM))
        {
        	ArrayList<String> uuids = getUUIDs(in);
        	boolean s = in.readBoolean();
        	String sound = in.readUTF();
        	boolean p = in.readBoolean();
        	String permission = in.readUTF();
        	boolean hasPermission = in.readBoolean();
        	ArrayList<String> msg = getMessages(in);
        	for(String uuid : uuids)
        	{
        		send(uuid, s, sound, p, hasPermission, permission, msg);
        	}
        } else if(task.equals(StaticValues.M2BA))
        {
        	boolean s = in.readBoolean();
        	String sound = in.readUTF();
        	boolean p = in.readBoolean();
        	String permission = in.readUTF();
        	boolean hasPermission = in.readBoolean();
        	ArrayList<String> msg = getMessages(in);
        	for(ProxiedPlayer all : plugin.getProxy().getPlayers())
        	{
        		String uuid = all.getUniqueId().toString();
        		send(uuid, s, sound, p, hasPermission, permission, msg);
        	}
        } else if(task.equals(StaticValues.BC2BS))
        {
        	String uuid = in.readUTF();
        	boolean s = in.readBoolean();
        	String sound = in.readUTF();
        	boolean p = in.readBoolean();
        	String permission = in.readUTF();
        	boolean hasPermission = in.readBoolean();
        	ArrayList<TextComponent> msg = getBCMessages(in);
        	sendBC(uuid, s, sound,  p, hasPermission, permission, msg);
        } else if(task.equals(StaticValues.BC2BM))
        {
        	ArrayList<String> uuids = getUUIDs(in);
        	boolean s = in.readBoolean();
        	String sound = in.readUTF();
        	boolean p = in.readBoolean();
        	String permission = in.readUTF();
        	boolean hasPermission = in.readBoolean();
        	ArrayList<TextComponent> msg = getBCMessages(in);
        	for(String uuid : uuids)
        	{
        		sendBC(uuid, s, sound, p, hasPermission, permission, msg);
        	}
        } else if(task.equals(StaticValues.BC2BA))
        {
        	boolean s = in.readBoolean();
        	String sound = in.readUTF();
        	boolean p = in.readBoolean();
        	String permission = in.readUTF();
        	boolean hasPermission = in.readBoolean();
        	ArrayList<TextComponent> msg = getBCMessages(in);
        	for(ProxiedPlayer all : plugin.getProxy().getPlayers())
        	{
        		String uuid = all.getUniqueId().toString();
        		sendBC(uuid, s, sound, p, hasPermission, permission, msg);
        	}
        } else if(task.equals(StaticValues.TM2BS))
        {
        	String uuid = in.readUTF();
        	String title = in.readUTF();
        	String subtitle = in.readUTF();
        	int fadeIn = in.readInt();
        	int stay= in.readInt();
        	int fadeOut = in.readInt();
        	boolean s = in.readBoolean();
        	String sound = in.readUTF();
        	boolean p = in.readBoolean();
        	String permission = in.readUTF();
        	boolean hasPermission = in.readBoolean();
        	sendTitle(uuid, title, subtitle, fadeIn, stay, fadeOut, s, sound, p, permission, hasPermission);
        } else if(task.equals(StaticValues.TM2BM))
        {
        	ArrayList<String> uuids = getUUIDs(in);
        	String title = in.readUTF();
        	String subtitle = in.readUTF();
        	int fadeIn = in.readInt();
        	int stay= in.readInt();
        	int fadeOut = in.readInt();
        	boolean s = in.readBoolean();
        	String sound = in.readUTF();
        	boolean p = in.readBoolean();
        	String permission = in.readUTF();
        	boolean hasPermission = in.readBoolean();
        	for(String uuid : uuids)
        	{
        		sendTitle(uuid, title, subtitle, fadeIn, stay, fadeOut, s, sound, p, permission, hasPermission);
        	}
        } else if(task.equals(StaticValues.TM2BA))
        {
        	String title = in.readUTF();
        	String subtitle = in.readUTF();
        	int fadeIn = in.readInt();
        	int stay= in.readInt();
        	int fadeOut = in.readInt();
        	boolean s = in.readBoolean();
        	String sound = in.readUTF();
        	boolean p = in.readBoolean();
        	String permission = in.readUTF();
        	boolean hasPermission = in.readBoolean();
        	for(ProxiedPlayer pp : BungeeCord.getInstance().getPlayers())
        	{
        		sendTitle(pp.getUniqueId().toString(), title, subtitle, fadeIn, stay, fadeOut, s, sound, p, permission, hasPermission);
        	}
        } else if(task.equals(StaticValues.ABM2BS))
        {
        	String uuid = in.readUTF();
        	String actionbarmessage = in.readUTF();
        	boolean s = in.readBoolean();
        	String sound = in.readUTF();
        	boolean p = in.readBoolean();
        	String permission = in.readUTF();
        	boolean hasPermission = in.readBoolean();
        	sendActionBar(uuid, actionbarmessage, s, sound, p, permission, hasPermission);
        } else if(task.equals(StaticValues.ABM2BM))
        {
        	ArrayList<String> uuids = getUUIDs(in);
        	String actionbarmessage = in.readUTF();
        	boolean s = in.readBoolean();
        	String sound = in.readUTF();
        	boolean p = in.readBoolean();
        	String permission = in.readUTF();
        	boolean hasPermission = in.readBoolean();
        	for(String uuid : uuids)
        	{
        		sendActionBar(uuid, actionbarmessage, s, sound, p, permission, hasPermission);
        	}
        } else if(task.equals(StaticValues.ABM2BA))
        {
        	String actionbarmessage = in.readUTF();
        	boolean s = in.readBoolean();
        	String sound = in.readUTF();
        	boolean p = in.readBoolean();
        	String permission = in.readUTF();
        	boolean hasPermission = in.readBoolean();
        	for(ProxiedPlayer pp : BungeeCord.getInstance().getPlayers())
        	{
        		sendActionBar(pp.getUniqueId().toString(), actionbarmessage, s, sound, p, permission, hasPermission);
        	}
        }
	}
	
	private ArrayList<String> getMessages(DataInputStream in) throws IOException
	{
		int lenght = in.readInt();
    	ArrayList<String> list = new ArrayList<>();
    	for(int i = 0; i < lenght; i++)
    	{
    		list.add(in.readUTF());
    	}
    	return list;
	}
	
	private ArrayList<TextComponent> getBCMessages(DataInputStream in) throws IOException
	{
		int lenght = in.readInt();
    	ArrayList<TextComponent> list = new ArrayList<>();
    	for(int i = 0; i < lenght; i++)
    	{
    		list.add(ChatApi.deserialized(in.readUTF()));
    	}
    	return list;
	}
	
	private ArrayList<String> getUUIDs(DataInputStream in) throws IOException
	{
		int size = in.readInt();
		ArrayList<String> uuids = new ArrayList<>();
    	for(int i = 0; i < size; i++)
    	{
    		uuids.add(in.readUTF());
    	}
    	return uuids;
	}
	
	private void send(String uuid, boolean s, String sound, boolean p, boolean hasPermission, String permission, ArrayList<String> msg)
	{
		UUID u = UUID.fromString(uuid);
		if(u == null)
		{
			return;
		}
		ProxiedPlayer player = plugin.getProxy().getPlayer(u);
		if(player == null)
		{
			return;
		}
		if(p)
		{
			if(hasPermission)
			{
				//The player must have the perm, to continue!
				if(player.hasPermission(permission))
				{
					sendsub(player, uuid, s, sound, msg);
				}
			} else
			{
				//The player must not have the perm, to continue!
				if(!player.hasPermission(permission))
				{
					sendsub(player, uuid, s, sound, msg);
				}
			}
		} else
		{
			sendsub(player, uuid, s, sound, msg);
		}
	}
	
	private void sendsub(ProxiedPlayer player, String uuid, boolean s, String sound, ArrayList<String> msg)
	{
		if(s)
		{
			sendsound(player, uuid, sound);
		}
		for(String m : msg)
		{
			player.sendMessage(ChatApi.tctl(m));
		}
	}
	
	private void sendBC(String uuid, boolean s, String sound, boolean p, boolean hasPermission, String permission,
			ArrayList<TextComponent> msg)
	{
		UUID u = UUID.fromString(uuid);
		if(u == null)
		{
			return;
		}
		ProxiedPlayer player = plugin.getProxy().getPlayer(u);
		if(player == null)
		{
			return;
		}
		if(p)
		{
			if(hasPermission)
			{
				//The player must have the perm, to continue!
				if(player.hasPermission(permission))
				{
					sendBCSub(player, uuid, s, sound, msg);
				}
			} else
			{
				if(!player.hasPermission(permission))
				{
					sendBCSub(player, uuid, s, sound, msg);
				}
			}
		} else
		{
			sendBCSub(player, uuid, s, sound, msg);
		}
	}
	
	private void sendBCSub(ProxiedPlayer player, String uuid, boolean s, String sound, ArrayList<TextComponent> msg)
	{
		if(s)
		{
			sendsound(player, uuid, sound);
		}
		for(TextComponent m : msg)
		{
			player.sendMessage(m);
		}
	}
	
	private void sendTitle(String uuid, String title, String subtitle, int fadeIn, int stay, int fadeOut,
			boolean s, String sound, boolean p, String permission, boolean hasPermission)
	{
		UUID u = UUID.fromString(uuid);
		if(u == null)
		{
			return;
		}
		ProxiedPlayer player = plugin.getProxy().getPlayer(u);
		if(player == null)
		{
			return;
		}
		Title t = BungeeCord.getInstance().createTitle();
		t.title(ChatApi.tctl(title));
		t.subTitle(ChatApi.tctl(subtitle));
		t.fadeIn(fadeIn);
		t.stay(stay);
		t.fadeOut(fadeOut);
		if(p)
		{
			if(hasPermission)
			{
				if(player.hasPermission(permission))
				{
					if(s)
					{
						sendsound(player, uuid, sound);
					}
					player.sendTitle(t);
				}
			} else
			{
				if(!player.hasPermission(permission))
				{
					if(s)
					{
						sendsound(player, uuid, sound);
					}
					player.sendTitle(t);
				}
			}
		} else
		{
			player.sendTitle(t);
		}		
	}
	
	private void sendActionBar(String uuid, String actionbar, boolean s, String sound, boolean p, String permission, boolean hasPermission)
	{
		UUID u = UUID.fromString(uuid);
		if(u == null)
		{
			return;
		}
		ProxiedPlayer player = plugin.getProxy().getPlayer(u);
		if(player == null)
		{
			return;
		}
		if(s)
		{
			sendsound(player, uuid, sound);
		}
		if(p)
		{
			if(hasPermission)
			{
				if(player.hasPermission(permission))
				{
					player.sendMessage(ChatMessageType.ACTION_BAR, ChatApi.tctl(actionbar));
				}
			} else
			{
				if(!player.hasPermission(permission))
				{
					player.sendMessage(ChatMessageType.ACTION_BAR, ChatApi.tctl(actionbar));
				}
			}
		} else
		{
			player.sendMessage(ChatMessageType.ACTION_BAR, ChatApi.tctl(actionbar));
		}		
	}
	
	private void sendsound(ProxiedPlayer player, String uuid, String sound)
	{
		ByteArrayOutputStream streamout = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(streamout);
        try {
			out.writeUTF(StaticValues.SENDSOUND);
			out.writeUTF(uuid);
			out.writeUTF(sound);
		} catch (IOException e) {
			e.printStackTrace();
		}
	    player.getServer().sendData(StaticValues.SCC_TOSPIGOT, streamout.toByteArray());
	}
}