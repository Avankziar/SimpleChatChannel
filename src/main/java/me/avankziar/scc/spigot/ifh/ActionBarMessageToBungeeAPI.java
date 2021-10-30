package main.java.me.avankziar.scc.spigot.ifh;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import main.java.me.avankziar.interfacehub.general.assitance.ChatApi;
import main.java.me.avankziar.interfacehub.spigot.interfaces.ActionBarMessageToBungee;
import main.java.me.avankziar.scc.objects.StaticValues;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;
import net.md_5.bungee.api.ChatMessageType;

public class ActionBarMessageToBungeeAPI implements ActionBarMessageToBungee
{
	private Sound sound = Sound.MUSIC_CREDITS;
	private String permission = "naavioqwinfwnwiq.hohbwfqwhaqhvj.anfuqwdf9ßpqwjvha"; //random perm :D
	private boolean hasPermission = true;

	@Override
	public void sendActionBarMessage(UUID uuid, String actionbarmessage)
	{
		sendActionBarMessage(uuid, actionbarmessage, sound, permission, hasPermission);
	}
	
	@Override
	public void sendActionBarMessage(UUID uuid, String actionbarmessage, Sound sound)
	{
		sendActionBarMessage(uuid, actionbarmessage, sound, permission, hasPermission);
	}
	
	@Override
	public void sendActionBarMessage(UUID uuid, String actionbarmessage, String permission, boolean hasPermission)
	{
		sendActionBarMessage(uuid, actionbarmessage, sound, permission, hasPermission);
	}
	
	@Override
	public void sendActionBarMessage(UUID uuid, String actionbarmessage, Sound sound, String permission, boolean hasPermission)
	{
		boolean s = false;
		if(sound != null && sound != this.sound) 
		{
			s = true;
		}
		boolean p = false;
		if(permission != null && !permission.isEmpty() && permission != this.permission)
		{
			p = true;
		}
		Player player = SimpleChatChannels.getPlugin().getServer().getPlayer(uuid);
		if(player != null && player.isOnline())
		{
			sendWhenOnlineOnLocalServer(player, actionbarmessage, s, sound,
					p, permission, hasPermission);
			return;
		}
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.ABM2BS);
			out.writeUTF(uuid.toString());
			addOutputStream(out, actionbarmessage, s, sound, p, permission, hasPermission);
		} catch (IOException e) {
			e.printStackTrace();
		}
        sendPluginMessage(stream);
	}

	@Override
	public void sendActionBarMessage(ArrayList<UUID> uuid, String actionbarmessage)
	{
		sendActionBarMessage(uuid, actionbarmessage, sound, permission, hasPermission);
	}
	
	@Override
	public void sendActionBarMessage(ArrayList<UUID> uuid, String actionbarmessage, Sound sound)
	{
		sendActionBarMessage(uuid, actionbarmessage, sound, permission, hasPermission);
	}
	
	@Override
	public void sendActionBarMessage(ArrayList<UUID> uuid, String actionbarmessage, String permission, boolean hasPermission)
	{
		sendActionBarMessage(uuid, actionbarmessage, sound, permission, hasPermission);
	}
	
	@Override
	public void sendActionBarMessage(ArrayList<UUID> uuid, String actionbarmessage, Sound sound, String permission, boolean hasPermission)
	{
		boolean s = false;
		if(sound != null && sound != this.sound) 
		{
			s = true;
		}
		boolean p = false;
		if(permission != null && !permission.isEmpty())
		{
			p = true;
		}
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.ABM2BM);
			out.writeInt(uuid.size());
			for(UUID u : uuid)
			{
				out.writeUTF(u.toString());
			}
			addOutputStream(out, actionbarmessage, s, sound, p, permission, hasPermission);
		} catch (IOException e) {
			e.printStackTrace();
		}
        sendPluginMessage(stream);
	}
	
	@Override
	public void sendActionBarMessage(String actionbarmessage)
	{
		sendActionBarMessage(actionbarmessage, sound, permission, hasPermission);
	}

	@Override
	public void sendActionBarMessage(String actionbarmessage, Sound sound)
	{
		sendActionBarMessage(actionbarmessage, sound, permission, hasPermission);
	}

	@Override
	public void sendActionBarMessage(String actionbarmessage, String permission, boolean hasPermission)
	{
		sendActionBarMessage(actionbarmessage, sound, permission, hasPermission);
	}

	@Override
	public void sendActionBarMessage(String actionbarmessage, Sound sound, String permission, boolean hasPermission)
	{
		boolean s = false;
		if(sound != null && sound != this.sound) 
		{
			s = true;
		}
		boolean p = false;
		if(permission != null && !permission.isEmpty())
		{
			p = true;
		}
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.ABM2BA);
			addOutputStream(out, actionbarmessage, s, sound, p, permission, hasPermission);
		} catch (IOException e) {
			e.printStackTrace();
		}
        sendPluginMessage(stream);
	}
	
	private void sendWhenOnlineOnLocalServer(Player player,
			String actionbarmessage,
			boolean s, Sound sound,
			boolean p, String perm, boolean hasPerm)
	{
		if(s)
		{
    		player.spigot().sendMessage(ChatMessageType.ACTION_BAR, ChatApi.tctl(actionbarmessage));
		}
		if(p)
		{
			if(hasPerm)
			{
				if(player.hasPermission(perm))
				{
					player.spigot().sendMessage(ChatMessageType.ACTION_BAR, ChatApi.tctl(actionbarmessage));
				}
			} else
			{
				if(!player.hasPermission(perm))
				{
					player.spigot().sendMessage(ChatMessageType.ACTION_BAR, ChatApi.tctl(actionbarmessage));
				}
			}
		} else
		{
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, ChatApi.tctl(actionbarmessage));
		}
	}	
	
	private void addOutputStream(DataOutputStream out,
			String actionbarmessage,
			boolean s, Sound sound,
			boolean p, String permission, boolean hasPermission
			) throws IOException
	{
		out.writeUTF(actionbarmessage);
		out.writeBoolean(s);
		out.writeUTF(sound.toString());
		out.writeBoolean(p);
		out.writeUTF(permission);
		out.writeBoolean(hasPermission);
	}
	
	private void sendPluginMessage(ByteArrayOutputStream stream)
	{
		for(Player player : Bukkit.getOnlinePlayers())
		{
			if(player != null)
			{
				player.sendPluginMessage(
		        		SimpleChatChannels.getPlugin(), StaticValues.SCC_TOBUNGEE, stream.toByteArray());
				break;
			}
		}
	}
}