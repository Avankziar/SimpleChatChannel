package main.java.de.avankziar.simplechatchannels.bungee.listener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import main.java.de.avankziar.simplechatchannels.bungee.SimpleChatChannels;
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
	 public void onPluginMessage(PluginMessageEvent ev) 
	 {
		 if (ev.getTag().equals("simplechatchannels:sccbungee")) 
		 {
			 ByteArrayInputStream streamin = new ByteArrayInputStream(ev.getData());
		     DataInputStream in = new DataInputStream(streamin);
		     try 
		     {
		        String[] s = in.readUTF().split("µ");
		        String Category = s[0];
		        String PlayerUUID = s[1];
		        String ToServer = s[2];
		        String Delivery = s[3];
				if(plugin.getProxy().getPlayer(UUID.fromString(PlayerUUID)) == null)
				{
					return;
				}
				if(Category.equals("spy"))
				{
					String µ = "µ";
					String message = Category+µ+PlayerUUID+µ+ToServer+µ+Delivery;
					ByteArrayOutputStream streamout = new ByteArrayOutputStream();
			        DataOutputStream out = new DataOutputStream(streamout);
			        String msg = message;
			        try {
						out.writeUTF(msg);
					} catch (IOException e) {
						e.printStackTrace();
					}
			        for(ProxiedPlayer pp : plugin.getProxy().getPlayers())
			        {
			        	if((boolean) plugin.getMysqlInterface().getDataI(pp, "spy", "player_uuid"))
			        	{
			        		pp.sendMessage(plugin.getUtility().tcl(Delivery));
			        	}
			        }
			        return;
				}
			} catch (IOException e) 
		    {
				e.printStackTrace();
			}
		    return;
		 } else
		 {
			 return;
		 }
	}
}
