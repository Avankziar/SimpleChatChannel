package main.java.me.avankziar.scc.bungee.listener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.UUID;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.scc.bungee.database.MysqlHandler;
import main.java.me.avankziar.scc.bungee.database.MysqlHandler.QueryType;
import main.java.me.avankziar.scc.bungee.handler.ChatHandler;
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
        if(task.equals(StaticValues.SCC_TASK_LOCATIONUPDATE))
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
        	System.out.println("3");
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
        }
	}
}