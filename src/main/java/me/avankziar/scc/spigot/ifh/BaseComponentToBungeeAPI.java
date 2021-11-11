package main.java.me.avankziar.scc.spigot.ifh;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import main.java.me.avankziar.interfacehub.spigot.interfaces.BaseComponentToBungee;
import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.StaticValues;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;
import net.md_5.bungee.api.chat.BaseComponent;

public class BaseComponentToBungeeAPI implements BaseComponentToBungee
{
	private Sound sound = Sound.MUSIC_CREDITS;
	private String permission = "naavioqwinfwnwiq.hohbwfqwhaqhvj.anfuqwdf9ßpqwjvha"; //random perm :D
	private boolean hasPermission = true;
	
	@Override
	public void sendMessage(UUID uuid, ArrayList<ArrayList<BaseComponent>> message)
	{
		sendMessage(uuid, sound, message);
	}

	@Override
	public void sendMessage(UUID uuid, Sound sound, ArrayList<ArrayList<BaseComponent>> message)
	{
        boolean s = false;
		if(sound != null && sound != this.sound) 
		{
			s = true;
		}
		boolean p = false;
		/* Here not needed
		if(permission != null && !permission.isEmpty() && permission != this.permission)
		{
			p = true;
		}*/
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.BC2BS);
			out.writeUTF(uuid.toString());
			addOutputStream(out, s, sound, p, permission, hasPermission, message);
		} catch (IOException e) {
			e.printStackTrace();
		}
        sendPluginMessage(stream);
	}

	@Override
	public void sendMessage(ArrayList<UUID> uuids, ArrayList<ArrayList<BaseComponent>> message)
	{
		sendMessage(uuids, permission, hasPermission, sound, message);
	}

	@Override
	public void sendMessage(ArrayList<UUID> uuids, Sound sound, ArrayList<ArrayList<BaseComponent>> message)
	{
		sendMessage(uuids, permission, hasPermission, sound, message);
	}

	@Override
	public void sendMessage(ArrayList<UUID> uuids, String permission, boolean hasPermission, ArrayList<ArrayList<BaseComponent>> message)
	{
		sendMessage(uuids, permission, hasPermission, sound, message);
	}

	@Override
	public void sendMessage(ArrayList<UUID> uuids, String permission, boolean hasPermission, Sound sound, ArrayList<ArrayList<BaseComponent>> message)
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
			out.writeUTF(StaticValues.BC2BM);
			writeUUIDs(out, uuids);
			addOutputStream(out, s, sound, p, permission, hasPermission, message);
		} catch (IOException e) {
			e.printStackTrace();
		}
        sendPluginMessage(stream);
	}
	
	@Override
	public void sendMessage(ArrayList<ArrayList<BaseComponent>> message)
	{
		sendMessage(permission, hasPermission, sound, message);
	}
	
	@Override
	public void sendMessage(Sound sound, ArrayList<ArrayList<BaseComponent>> message)
	{
		sendMessage(permission, hasPermission, sound, message);
	}
	
	@Override
	public void sendMessage(String permission, boolean hasPermission, ArrayList<ArrayList<BaseComponent>> message)
	{
		sendMessage(permission, hasPermission, sound, message);
	}
	
	@Override
	public void sendMessage(String permission, boolean hasPermission, Sound sound, ArrayList<ArrayList<BaseComponent>> message)
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
			out.writeUTF(StaticValues.BC2BA);
			addOutputStream(out, s, sound, p, permission, hasPermission, message);
		} catch (IOException e) {
			e.printStackTrace();
		}
        sendPluginMessage(stream);
	}
	
	private void addOutputStream(DataOutputStream out,
			boolean s, Sound sound,
			boolean p, String permission, boolean hasPermission,
			ArrayList<ArrayList<BaseComponent>> message) throws IOException
	{
		out.writeBoolean(s);
		out.writeUTF(sound.toString());
		out.writeBoolean(p);
		out.writeUTF(permission);
		out.writeBoolean(hasPermission);
		writeMessage(out, message);
	}
	
	private void writeMessage(DataOutputStream out, ArrayList<ArrayList<BaseComponent>> message) throws IOException 
	{
		out.writeInt(message.size());
		for(ArrayList<BaseComponent> list : message)
		{
			String s = ChatApi.serialized(list);
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
		        		SimpleChatChannels.getPlugin(), StaticValues.SCC_TOBUNGEE, stream.toByteArray());
				break;
			}
		}
	}
}
