package main.java.me.avankziar.scc.spigot.ifh;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.general.objects.StaticValues;
import main.java.me.avankziar.scc.spigot.SCC;
import me.avankziar.ifh.spigot.tobungee.chatlike.MessageToBungee;
import me.avankziar.ifh.spigot.tovelocity.chatlike.MessageToVelocity;

public class MessageToProxyProvider implements MessageToBungee, MessageToVelocity
{
	private Sound sound = Sound.MUSIC_CREDITS;
	private String permission = "naavioqwinfwnwiq.hohbwfqwhaqhvj.anfuqwdf9ßpqwjvha"; //random perm :D
	private boolean hasPermission = true;
	
	@Override
	public void sendMessage(UUID uuid, String... message)
	{
		sendMessage(uuid, sound, message);
	}
	
	@Override
	public void sendMessage(UUID uuid, Sound sound, String... message)
	{
		boolean s = false;
		if(sound != null && sound != this.sound) 
		{
			s = true;
		}
		boolean p = false;
		/* Here not needed
		if(permission != null && !permission.isEmpty())
		{
			p = true;
		}*/
		Player player = SCC.getPlugin().getServer().getPlayer(uuid);
		if(player != null && player.isOnline())
		{
			sendWhenOnlineOnLocalServer(player, s, sound, message);
			return;
		}
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.M2PS);
			out.writeUTF(uuid.toString());
			addOutputStream(out, s, sound, p, permission, hasPermission, message);
		} catch (IOException e) {
			e.printStackTrace();
		}
        sendPluginMessage(stream);
	}

	@Override
	public void sendMessage(ArrayList<UUID> uuids, String... message)
	{
		sendMessage(uuids, permission, hasPermission, sound, message);
	}

	@Override
	public void sendMessage(ArrayList<UUID> uuids, Sound sound, String... message)
	{
		sendMessage(uuids, permission, hasPermission, sound, message);
	}

	@Override
	public void sendMessage(ArrayList<UUID> uuids, String permission, boolean hasPermission, String... message)
	{
		sendMessage(uuids, permission, hasPermission, sound, message);
	}

	@Override
	public void sendMessage(ArrayList<UUID> uuids, String permission, boolean hasPermission, Sound sound, String... message)
	{
		if(uuids.isEmpty())
		{
			return;
		}
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
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.M2PM);
			writeUUIDs(out, uuids);
			addOutputStream(out, s, sound, p, permission, hasPermission, message);
		} catch (IOException e) {
			e.printStackTrace();
		}
        sendPluginMessage(stream);
	}
	
	@Override
	public void sendMessage(String... message)
	{
		sendMessage(permission, hasPermission, sound, message);
	}
	
	@Override
	public void sendMessage(Sound sound, String... message)
	{
		sendMessage(permission, hasPermission, sound, message);
	}
	
	@Override
	public void sendMessage(String permission, boolean hasPermission, String... message)
	{
		sendMessage(permission, hasPermission, sound, message);
	}
	
	@Override
	public void sendMessage(String permission, boolean hasPermission, Sound sound, String... message)
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
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.M2PA);
			addOutputStream(out, s, sound, p, permission, hasPermission, message);
		} catch (IOException e) {
			e.printStackTrace();
		}
        sendPluginMessage(stream);
	}
	
	private void sendWhenOnlineOnLocalServer(Player player, boolean s, Sound sound, String... message)
	{
		if(s)
		{
    		player.playSound(player.getLocation(), sound, 3.0F, 0.5F);
		}
		for(String msg : message)
		{
			player.sendMessage(msg);
		}
	}	
	
	private void addOutputStream(DataOutputStream out,
			boolean s, Sound sound,
			boolean p, String permission, boolean hasPermission,
			String...message) throws IOException
	{
		out.writeBoolean(s);
		out.writeUTF(sound.toString());
		out.writeBoolean(p);
		out.writeUTF(permission);
		out.writeBoolean(hasPermission);
		writeMessage(out, message);
	}
	
	private void writeMessage(DataOutputStream out, String... message) throws IOException
	{
		out.writeInt(message.length);
		for(String s : message)
		{
			out.writeUTF(s);
		}
	}
	
	private void writeUUIDs(DataOutputStream out, ArrayList<UUID> uuids) throws IOException
	{
		out.writeInt(uuids.size());
		for(UUID u : uuids)
		{
			out.writeUTF(u.toString());
		}
	}
	
	private void sendPluginMessage(ByteArrayOutputStream stream)
	{
		for(Player player : Bukkit.getOnlinePlayers())
		{
			if(player != null)
			{
				player.sendPluginMessage(
		        		SCC.getPlugin(), StaticValues.SCC_TOPROXY, stream.toByteArray());
				break;
			}
		}
	}
}