package main.java.me.avankziar.scc.bungee.listener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.scc.bungee.handler.ChatHandler;
import main.java.me.avankziar.scc.objects.ServerLocation;
import main.java.me.avankziar.scc.objects.StaticValues;
import main.java.me.avankziar.scc.objects.chat.Channel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
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
        if (!(event.getSender() instanceof Server))
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
        	String uuid = in.readUTF();
        	String message = in.readUTF();
        	ChatHandler ch = new ChatHandler(plugin);
        	Channel usedChannel = SimpleChatChannels.channels.get(plugin.getYamlHandler().getConfig().getString("BroadCast.UsingChannel"));
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
        }
	}
}