package main.java.me.avankziar.simplechatchannels.bungee.listener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.assistance.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.objects.ChatUserHandler;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.ChatUser;
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
		        String category = s[0];
		        String playerUUID = s[1];
				if(plugin.getProxy().getPlayer(UUID.fromString(playerUUID)) == null)
				{
					return;
				}
				if(category.equals("spy"))
				{
					String ToServer = s[2];
			        String Delivery = s[3];
					String µ = "µ";
					String message = category+µ+playerUUID+µ+ToServer+µ+Delivery;
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
			        	ChatUser cu = ChatUserHandler.getChatUser(pp.getUniqueId().toString());
			        	if(cu != null)
			        	{
			        		if(cu.isOptionSpy())
				        	{
				        		pp.sendMessage(ChatApi.tctl(Delivery));
				        	}
			        	}
			        }
			        return;
				}
				if(category.equals("item"))
				{
					String itemname = s[2];
					String item = s[3];
					if(Utility.item.containsKey(playerUUID))
					{
						Utility.item.replace(playerUUID, item);
					} else
					{
						Utility.item.put(playerUUID, item);
					}
					if(Utility.itemname.containsKey(playerUUID))
					{
						Utility.itemname.replace(playerUUID, itemname);
					} else
					{
						Utility.itemname.put(playerUUID, itemname);
					}
					return;
				}
				if(category.equals("itemclear"))
				{
					if(Utility.item.containsKey(playerUUID))
					{
						Utility.item.remove(playerUUID);
					}
					if(Utility.itemname.containsKey(playerUUID))
					{
						Utility.itemname.remove(playerUUID);
					}
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