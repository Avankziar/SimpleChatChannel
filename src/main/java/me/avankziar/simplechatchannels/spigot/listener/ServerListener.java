package main.java.me.avankziar.simplechatchannels.spigot.listener;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;

import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;

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
		String language = plugin.getUtility().getLanguage();
		if(channel.equals("simplechatchannels:sccbungee")) 
		{
        	ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
            DataInputStream in = new DataInputStream(stream);
            try {
            	String[] s = in.readUTF().split("µ");
            	final String Category = s[0];
            	if(Category.equalsIgnoreCase("bungeeswitch"))
            	{
            		boolean boo = plugin.getYamlHandler().get().getBoolean("bungee", false);
            		if(boo)
            		{
            			plugin.getYamlHandler().get().set("Bungee", false);
            			plugin.getYamlHandler().saveConfig();
        				player.spigot().sendMessage(plugin.getUtility().tctl(
        						plugin.getYamlHandler().getL().getString(language+".CmdScc.ServerListener.BungeeOff")));
        				return;
            		} else
            		{
            			plugin.getYamlHandler().get().set("Bungee", true);
            			plugin.getYamlHandler().saveConfig();
        				player.spigot().sendMessage(plugin.getUtility().tctl(
        						plugin.getYamlHandler().getL().getString(language+".CmdScc.Serverlistener.BungeeOn")));
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
