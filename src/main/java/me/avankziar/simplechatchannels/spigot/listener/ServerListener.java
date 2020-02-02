package main.java.de.avankziar.simplechatchannels.spigot.listener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import main.java.de.avankziar.simplechatchannels.spigot.SimpleChatChannels;

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
		String language = plugin.getYamlHandler().get().getString("language");
		if(channel.equals("simplechatchannels:sccbungee")) 
		{
        	ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
            DataInputStream in = new DataInputStream(stream);
            try {
            	String[] s = in.readUTF().split("µ");
            	final String Category = s[0];
            	if(Category.equalsIgnoreCase("bungeeswitch"))
            	{
            		String boo = plugin.getYamlHandler().get().getString("bungee");
            		if(boo.equals("true"))
            		{
            			plugin.getYamlHandler().get().set("bungee", "false");
            			plugin.getYamlHandler().saveConfig();
        				player.spigot().sendMessage(plugin.getUtility().tcl(
        						plugin.getYamlHandler().getL().getString(language+".CMD_SCC.serverlistener.msg01")));
        				return;
            		} else if(boo.equals("false"))
            		{
            			plugin.getYamlHandler().get().set("bungee", "true");
            			plugin.getYamlHandler().saveConfig();
        				player.spigot().sendMessage(plugin.getUtility().tcl(
        						plugin.getYamlHandler().getL().getString(language+".CMD_SCC.serverlistener.msg02")));
        				return;
            		}
            	} else if(Category.equalsIgnoreCase("editor"))
            	{
            		String playername= s[1];
            		String i = s[2];
            		if(i.equals("remove"))
            		{
            			if(SimpleChatChannels.editorplayers.contains(playername))
            			{
            				SimpleChatChannels.editorplayers.remove(playername);
            			}
            			return;
            		} else if(i.equals("add"))
            		{
            			SimpleChatChannels.log.info("Bungeemsg: "+playername+" "+SimpleChatChannels.editorplayers.contains(playername));
            			if(!SimpleChatChannels.editorplayers.contains(playername))
            			{
            				SimpleChatChannels.editorplayers.add(playername);
            			}
            			return;
            		}
            		return;
            	}
            } catch (IOException e) {
    			e.printStackTrace();
    		}
		}
	}
}
