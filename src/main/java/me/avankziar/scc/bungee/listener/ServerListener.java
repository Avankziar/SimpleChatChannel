package main.java.me.avankziar.scc.bungee.listener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.UUID;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.scc.bungee.database.MysqlHandler;
import main.java.me.avankziar.scc.bungee.database.MysqlHandler.QueryType;
import main.java.me.avankziar.scc.bungee.handler.ChatHandler;
import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.ServerLocation;
import main.java.me.avankziar.scc.objects.StaticValues;
import main.java.me.avankziar.scc.objects.chat.Channel;
import net.md_5.bungee.api.CommandSender;
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
        	if(plugin.editorplayers.contains(playername))
    		{
    			plugin.editorplayers.remove(playername);
    		} else
    		{
    			plugin.editorplayers.add(playername);
    		}
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
        	ChatHandler ch = new ChatHandler(plugin);
        	Channel usedChannel = plugin.getChannel(plugin.getYamlHandler().getConfig().getString("BroadCast.UsingChannel"));
    		if(usedChannel == null)
    		{
    			return;
    		}
        	if(uuid.equalsIgnoreCase("Console"))
        	{
        		CommandSender console = plugin.getProxy().getConsole();
        		ch.sendBroadCast(console, usedChannel, message);
        	} else
        	{
        		ProxiedPlayer player = plugin.getProxy().getPlayer(UUID.fromString(uuid));
        		ch.sendBroadCast(player, usedChannel, message);
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
        } else if(task.equals(StaticValues.MTBS))
        {
        	String uuid = in.readUTF();
        	ArrayList<String> msg = getMessages(in);
        	send(uuid, false, null, false, false, null, msg);
        } else if(task.equals(StaticValues.MTBSS))
        {
        	String uuid = in.readUTF();
        	String sound = in.readUTF();
        	ArrayList<String> msg = getMessages(in);
        	send(uuid, true, sound, false, false, null, msg);
        } else if(task.equals(StaticValues.MTBM))
        {
        	ArrayList<String> uuids = getUUIDs(in);
        	ArrayList<String> msg = getMessages(in);
        	for(String uuid : uuids)
        	{
        		send(uuid, false, null, false, false, null, msg);
        	}
        } else if(task.equals(StaticValues.MTBMS))
        {
        	String sound = in.readUTF();
        	ArrayList<String> uuids = getUUIDs(in);
        	ArrayList<String> msg = getMessages(in);
        	for(String uuid : uuids)
        	{
        		send(uuid, true, sound, false, false, null, msg);
        	}
        } else if(task.equals(StaticValues.MTBMP))
        {
        	boolean hasPermission = in.readBoolean();
        	String permission = in.readUTF();
        	ArrayList<String> uuids = getUUIDs(in);
        	ArrayList<String> msg = getMessages(in);
        	for(String uuid : uuids)
        	{
        		send(uuid, false, null, true, hasPermission, permission, msg);
        	}
        } else if(task.equals(StaticValues.MTBMSP))
        {
        	String sound = in.readUTF();
        	boolean hasPermission = in.readBoolean();
        	String permission = in.readUTF();
        	ArrayList<String> uuids = getUUIDs(in);
        	ArrayList<String> msg = getMessages(in);
        	for(String uuid : uuids)
        	{
        		send(uuid, true, sound, true, hasPermission, permission, msg);
        	}
        } else if(task.equals(StaticValues.MTBA))
        {
        	ArrayList<String> msg = getMessages(in);
        	for(ProxiedPlayer all : plugin.getProxy().getPlayers())
        	{
        		String uuid = all.getUniqueId().toString();
        		send(uuid, false, null, false, false, null, msg);
        	}
        } else if(task.equals(StaticValues.MTBAS))
        {
        	String sound = in.readUTF();
        	ArrayList<String> msg = getMessages(in);
        	for(ProxiedPlayer all : plugin.getProxy().getPlayers())
        	{
        		String uuid = all.getUniqueId().toString();
        		send(uuid, true, sound, false, false, null, msg);
        	}
        } else if(task.equals(StaticValues.MTBAP))
        {
        	boolean hasPermission = in.readBoolean();
        	String permission = in.readUTF();
        	ArrayList<String> msg = getMessages(in);
        	for(ProxiedPlayer all : plugin.getProxy().getPlayers())
        	{
        		String uuid = all.getUniqueId().toString();
        		send(uuid, false, null, true, hasPermission, permission, msg);
        	}
        } else if(task.equals(StaticValues.MTBASP))
        {
        	String sound = in.readUTF();
        	boolean hasPermission = in.readBoolean();
        	String permission = in.readUTF();
        	ArrayList<String> msg = getMessages(in);
        	for(ProxiedPlayer all : plugin.getProxy().getPlayers())
        	{
        		String uuid = all.getUniqueId().toString();
        		send(uuid, true, sound, true, hasPermission, permission, msg);
        	}
        } else if(task.equals(StaticValues.BCTBS))
        {
        	String uuid = in.readUTF();
        	ArrayList<String> msg = getMessages(in);
        	send(uuid, false, null, false, false, null, msg);
        } else if(task.equals(StaticValues.BCTBSS))
        {
        	String uuid = in.readUTF();
        	String sound = in.readUTF();
        	ArrayList<String> msg = getMessages(in);
        	send(uuid, true, sound, false, false, null, msg);
        } else if(task.equals(StaticValues.BCTBM))
        {
        	ArrayList<String> uuids = getUUIDs(in);
        	ArrayList<String> msg = getMessages(in);
        	for(String uuid : uuids)
        	{
        		send(uuid, false, null, false, false, null, msg);
        	}
        } else if(task.equals(StaticValues.BCTBMS))
        {
        	String sound = in.readUTF();
        	ArrayList<String> uuids = getUUIDs(in);
        	ArrayList<String> msg = getMessages(in);
        	for(String uuid : uuids)
        	{
        		send(uuid, true, sound, false, false, null, msg);
        	}
        } else if(task.equals(StaticValues.BCTBMP))
        {
        	boolean hasPermission = in.readBoolean();
        	String permission = in.readUTF();
        	ArrayList<String> uuids = getUUIDs(in);
        	ArrayList<String> msg = getMessages(in);
        	for(String uuid : uuids)
        	{
        		send(uuid, false, null, true, hasPermission, permission, msg);
        	}
        } else if(task.equals(StaticValues.BCTBMSP))
        {
        	String sound = in.readUTF();
        	boolean hasPermission = in.readBoolean();
        	String permission = in.readUTF();
        	ArrayList<String> uuids = getUUIDs(in);
        	ArrayList<String> msg = getMessages(in);
        	for(String uuid : uuids)
        	{
        		send(uuid, true, sound, true, hasPermission, permission, msg);
        	}
        } else if(task.equals(StaticValues.BCTBA))
        {
        	ArrayList<String> msg = getMessages(in);
        	for(ProxiedPlayer all : plugin.getProxy().getPlayers())
        	{
        		String uuid = all.getUniqueId().toString();
        		send(uuid, false, null, false, false, null, msg);
        	}
        } else if(task.equals(StaticValues.BCTBAS))
        {
        	String sound = in.readUTF();
        	ArrayList<String> msg = getMessages(in);
        	for(ProxiedPlayer all : plugin.getProxy().getPlayers())
        	{
        		String uuid = all.getUniqueId().toString();
        		send(uuid, true, sound, false, false, null, msg);
        	}
        } else if(task.equals(StaticValues.BCTBAP))
        {
        	boolean hasPermission = in.readBoolean();
        	String permission = in.readUTF();
        	ArrayList<String> msg = getMessages(in);
        	for(ProxiedPlayer all : plugin.getProxy().getPlayers())
        	{
        		String uuid = all.getUniqueId().toString();
        		send(uuid, false, null, true, hasPermission, permission, msg);
        	}
        } else if(task.equals(StaticValues.BCTBASP))
        {
        	String sound = in.readUTF();
        	boolean hasPermission = in.readBoolean();
        	String permission = in.readUTF();
        	ArrayList<String> msg = getMessages(in);
        	for(ProxiedPlayer all : plugin.getProxy().getPlayers())
        	{
        		String uuid = all.getUniqueId().toString();
        		send(uuid, true, sound, true, hasPermission, permission, msg);
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
	
	private void send(String uuid, boolean sound, String sounds, boolean perm, boolean hasPermission, String permission, ArrayList<String> msg)
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
		if(perm)
		{
			if(hasPermission)
			{
				//The player must have the perm, to continue!
				if(!player.hasPermission(permission))
				{
					return;
				}
			} else
			{
				//The player must not have the perm, to continue!
				if(player.hasPermission(permission))
				{
					return;
				}
			}
		}
		if(sound)
		{
			sendsound(player, uuid, sounds);
		}
		for(String s : msg)
		{
			player.sendMessage(ChatApi.tctl(s));
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