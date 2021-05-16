package main.java.me.avankziar.scc.spigot.listener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import main.java.me.avankziar.scc.objects.StaticValues;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;
import main.java.me.avankziar.scc.spigot.handler.ChatHandler;

public class ServerListener  implements PluginMessageListener
{
	private SimpleChatChannels plugin;
	
	public ServerListener(SimpleChatChannels plugin)
	{
		this.plugin = plugin;
	}
	
	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] bytes) 
	{
		if(channel.equals(StaticValues.SCC_TOSPIGOT)) 
		{
			
        	ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
            DataInputStream in = new DataInputStream(stream);
            String task = null;
            try 
            {
            	task = in.readUTF();
            	if(task.equals(StaticValues.SCC_TASK_PINGAPLAYER))
            	{
            		String uuids = in.readUTF();
            		String sound = in.readUTF();
            		UUID uuid = UUID.fromString(uuids);
            		if(uuid == null)
            		{
            			return;
            		}
            		Player mention = plugin.getServer().getPlayer(uuid);
            		if(mention == null)
            		{
            			return;
            		}
                	new ChatHandler(plugin).sendMentionPing(mention, sound);
            	} else if(task.equals(StaticValues.SCC_TASK_ARG))
            	{
            		String uuids = in.readUTF();
            		String cmd = in.readUTF();
            		int argAmount = in.readInt();
            		String runCmd = "/"+cmd+" ";
            		for(int i = 0; i < argAmount; i++)
            		{
            			String s = in.readUTF();
            			runCmd += s+" ";
            		}
            		Player runPlayer = plugin.getServer().getPlayer(UUID.fromString(uuids));
            		if(runPlayer != null)
            		{
            			runPlayer.chat(runCmd);
            		}
            	}
            } catch (IOException e) 
            {
    			e.printStackTrace();
    		}
		}
	}
}
