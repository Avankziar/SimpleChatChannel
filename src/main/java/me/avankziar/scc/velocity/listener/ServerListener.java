package main.java.me.avankziar.scc.velocity.listener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PluginMessageEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ServerConnection;
import com.velocitypowered.api.proxy.messages.MinecraftChannelIdentifier;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.database.QueryType;
import main.java.me.avankziar.scc.general.objects.Channel;
import main.java.me.avankziar.scc.general.objects.ChatTitle;
import main.java.me.avankziar.scc.general.objects.ServerLocation;
import main.java.me.avankziar.scc.general.objects.StaticValues;
import main.java.me.avankziar.scc.velocity.SCC;
import main.java.me.avankziar.scc.velocity.database.MysqlHandler;
import main.java.me.avankziar.scc.velocity.handler.ChatHandler;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;

public class ServerListener
{
	private SCC plugin;
	
	public ServerListener(SCC plugin)
	{
		this.plugin = plugin;
	}
	
	@Subscribe
    public void onPluginMessageFromBackend(PluginMessageEvent event) 
    {
        if (!(event.getSource() instanceof ServerConnection)) 
        {
            return;
        }
        if (!event.getIdentifier().getId().equalsIgnoreCase(StaticValues.SCC_TOPROXY)) 
        {
            return;
        }
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(event.getData()));
        try
        {
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
    			SCC.getPlugin().getLogger().log(Level.INFO, "Register "+c.getUniqueIdentifierName()+" Channel!");
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
    			for(ChatTitle ct : SCC.chatTitlesPrefix)
    			{
    				if(ct.getUniqueIdentifierName().equalsIgnoreCase(uniquechattitle))
    				{
    					return;
    				}
    			}
    			for(ChatTitle ct : SCC.chatTitlesSuffix)
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
    				SCC.chatTitlesPrefix.add(ct);
    			} else
    			{
    				SCC.chatTitlesSuffix.add(ct);
    			}
    			SCC.chatTitlesPrefix.sort(Comparator.comparing(ChatTitle::getWeight));
    			Collections.reverse(SCC.chatTitlesPrefix);
    			SCC.chatTitlesSuffix.sort(Comparator.comparing(ChatTitle::getWeight));
    			Collections.reverse(SCC.chatTitlesSuffix);
    			SCC.getPlugin().getLogger().log(Level.INFO, "Register "+uniquechattitle+" ChatTitle!");
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
            		CommandSource console = plugin.getServer().getConsoleCommandSource();
            		ch.sendBroadCast(console, usedChannel, message, server.equals("null") ? null : server);
            	} else
            	{
            		Player player = plugin.getServer().getPlayer(UUID.fromString(uuid)).get();
            		ch.sendBroadCast(player, usedChannel, message, server.equals("null") ? null : server);
            	}
            } else if(task.equals(StaticValues.SCC_TASK_W))
            {
            	String uuid = in.readUTF();
            	String message = in.readUTF();
            	ChatHandler ch = new ChatHandler(plugin);
        		CommandSource console = plugin.getServer().getConsoleCommandSource();
        		Player other = plugin.getServer().getPlayer(UUID.fromString(uuid)).get();
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
            		LinkedHashMap<QueryType, Integer> map = MysqlHandler.serverPerformance.get(server);
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
            		LinkedHashMap<QueryType, Integer> map = new LinkedHashMap<>();
            		map.put(QueryType.INSERT, inserts);
            		map.put(QueryType.UPDATE, updates);
            		map.put(QueryType.DELETE, deletes);
            		map.put(QueryType.READ, reads);
            		MysqlHandler.serverPerformance.put(server, map);
            	}
            } else if(task.equals(StaticValues.M2PS))
            {
            	String uuid = in.readUTF();
            	boolean s = in.readBoolean();
            	String sound = in.readUTF();
            	boolean p = in.readBoolean();
            	String permission = in.readUTF();
            	boolean hasPermission = in.readBoolean();
            	ArrayList<String> msg = getMessages(in);
            	send(uuid, s, sound, p, hasPermission, permission, msg);
            } else if(task.equals(StaticValues.M2PM))
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
            } else if(task.equals(StaticValues.M2PA))
            {
            	boolean s = in.readBoolean();
            	String sound = in.readUTF();
            	boolean p = in.readBoolean();
            	String permission = in.readUTF();
            	boolean hasPermission = in.readBoolean();
            	ArrayList<String> msg = getMessages(in);
            	for(Player all : plugin.getServer().getAllPlayers())
            	{
            		String uuid = all.getUniqueId().toString();
            		send(uuid, s, sound, p, hasPermission, permission, msg);
            	}
            }/* else if(task.equals(StaticValues.BC2BS))
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
            	for(Player all : plugin.getProxy().getPlayers())
            	{
            		String uuid = all.getUniqueId().toString();
            		sendBC(uuid, s, sound, p, hasPermission, permission, msg);
            	}
            }*/ else if(task.equals(StaticValues.TM2BS))
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
            	for(Player pp : plugin.getServer().getAllPlayers())
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
            	for(Player pp : plugin.getServer().getAllPlayers())
            	{
            		sendActionBar(pp.getUniqueId().toString(), actionbarmessage, s, sound, p, permission, hasPermission);
            	}
            }
        } catch(Exception e)
        {
        	e.printStackTrace();
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
	
	/*private ArrayList<TextComponent> getBCMessages(DataInputStream in) throws IOException
	{
		int lenght = in.readInt();
    	ArrayList<TextComponent> list = new ArrayList<>();
    	for(int i = 0; i < lenght; i++)
    	{
    		list.add(ChatApiOld.deserialized(in.readUTF()));
    	}
    	return list;
	}*/
	
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
		Optional<Player> player = plugin.getServer().getPlayer(u);
		if(player.isEmpty())
		{
			return;
		}
		if(p)
		{
			if(hasPermission)
			{
				//The player must have the perm, to continue!
				if(player.get().hasPermission(permission))
				{
					sendsub(player.get(), uuid, s, sound, msg);
				}
			} else
			{
				//The player must not have the perm, to continue!
				if(!player.get().hasPermission(permission))
				{
					sendsub(player.get(), uuid, s, sound, msg);
				}
			}
		} else
		{
			sendsub(player.get(), uuid, s, sound, msg);
		}
	}
	
	private void sendsub(Player player, String uuid, boolean s, String sound, ArrayList<String> msg)
	{
		if(s)
		{
			sendsound(player, uuid, sound);
		}
		for(String m : msg)
		{
			player.sendMessage(ChatApi.tl(m));
		}
	}
	
	/*private void sendBC(String uuid, boolean s, String sound, boolean p, boolean hasPermission, String permission,
			ArrayList<TextComponent> msg)
	{
		UUID u = UUID.fromString(uuid);
		if(u == null)
		{
			return;
		}
		Player player = plugin.getProxy().getPlayer(u);
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
	
	private void sendBCSub(Player player, String uuid, boolean s, String sound, ArrayList<TextComponent> msg)
	{
		if(s)
		{
			sendsound(player, uuid, sound);
		}
		for(TextComponent m : msg)
		{
			player.sendMessage(m);
		}
	}*/
	
	private void sendTitle(String uuid, String title, String subtitle, int fadeIn, int stay, int fadeOut,
			boolean s, String sound, boolean p, String permission, boolean hasPermission)
	{
		UUID u = UUID.fromString(uuid);
		if(u == null)
		{
			return;
		}
		Optional<Player> oplayer = plugin.getServer().getPlayer(u);
		if(oplayer.isEmpty())
		{
			return;
		}
		Player player = oplayer.get();
		final Title.Times times = Title.Times.times(Duration.ofMillis(50*fadeIn), Duration.ofMillis(50*stay), Duration.ofMillis(50*fadeOut));
		final Title t = Title.title(Component.text("Hello!"), Component.empty(), times);
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
					player.showTitle(t); 
				}
			} else
			{
				if(!player.hasPermission(permission))
				{
					if(s)
					{
						sendsound(player, uuid, sound);
					}
					player.showTitle(t); 
				}
			}
		} else
		{
			player.showTitle(t); 
		}
	}
	
	private void sendActionBar(String uuid, String actionbar, boolean s, String sound, boolean p, String permission, boolean hasPermission)
	{
		UUID u = UUID.fromString(uuid);
		if(u == null)
		{
			return;
		}
		Optional<Player> player = plugin.getServer().getPlayer(u);
		if(player.isEmpty())
		{
			return;
		}
		if(s)
		{
			sendsound(player.get(), uuid, sound);
		}
		//FIXME SendActionbar
		/*if(p)
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
		}*/	
	}
	
	private void sendsound(Player player, String uuid, String sound)
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
	    player.getCurrentServer().ifPresent(y -> y.sendPluginMessage(MinecraftChannelIdentifier.from(StaticValues.SCC_TOSPIGOT), streamout.toByteArray()));
	}
}