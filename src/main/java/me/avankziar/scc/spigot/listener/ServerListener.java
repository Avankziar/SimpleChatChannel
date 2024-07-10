package main.java.me.avankziar.scc.spigot.listener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import main.java.me.avankziar.scc.general.objects.StaticValues;
import main.java.me.avankziar.scc.spigot.SCC;
import main.java.me.avankziar.scc.spigot.handler.ChatHandlerAdventure;

public class ServerListener implements PluginMessageListener
{
	private SCC plugin;
	
	public ServerListener(SCC plugin)
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
                	new ChatHandlerAdventure(plugin).sendMentionPing(mention, sound);
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
            	} else if(task.equals(StaticValues.SENDSOUND))
            	{
            		String uuids = in.readUTF();
            		String s = in.readUTF();
            		Sound sound;
            		try
            		{
            			sound = Sound.valueOf(s);
            		} catch(Exception e)
            		{
            			sound = Sound.ENTITY_WANDERING_TRADER_REAPPEARED;
            		}
            		UUID uuid = UUID.fromString(uuids);
            		if(uuid == null)
            		{
            			return;
            		}
            		Player target = plugin.getServer().getPlayer(uuid);
            		if(target == null)
            		{
            			return;
            		}
            		target.playSound(target.getLocation(), sound, 3.0F, 0.5F);
            	}
            } catch (IOException e) 
            {
    			e.printStackTrace();
    		}
		}
	}
}
