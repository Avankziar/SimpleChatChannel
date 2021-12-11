package main.java.me.avankziar.scc.spigot.ifh;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import main.java.me.avankziar.ifh.spigot.tobungee.displaychatlike.TitleMessageToBungee;
import main.java.me.avankziar.scc.objects.StaticValues;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;

public class TitleMessageToBungeeAPI implements TitleMessageToBungee
{
	private int fadeIn = 1;
	private int stay = 20;
	private int fadeOut = 1;
	private Sound sound = Sound.MUSIC_CREDITS;
	private String permission = "naavioqwinfwnwiq.hohbwfqwhaqhvj.anfuqwdf9ßpqwjvha"; //random perm :D
	private boolean hasPermission = true;

	@Override
	public void sendTitleMessage(UUID uuid, String title, String subtitle)
	{
		sendTitleMessage(uuid, title, subtitle, fadeIn, stay, fadeOut, sound, permission, hasPermission);
	}
	
	@Override
	public void sendTitleMessage(UUID uuid, String title, String subtitle, Sound sound)
	{
		sendTitleMessage(uuid, title, subtitle, fadeIn, stay, fadeOut, sound, permission, hasPermission);
	}
	
	@Override
	public void sendTitleMessage(UUID uuid, String title, String subtitle, String permissio, boolean hasPermission)
	{
		sendTitleMessage(uuid, title, subtitle, fadeIn, stay, fadeOut, sound, permission, hasPermission);
	}

	@Override
	public void sendTitleMessage(UUID uuid, String title, String subtitle, Sound sound, String permissio, boolean hasPermission)
	{
		sendTitleMessage(uuid, title, subtitle, fadeIn, stay, fadeOut, sound, permission, hasPermission);
	}
	
	@Override
	public void sendTitleMessage(UUID uuid, String title, String subtitle, int fadeIn, int stay, int fadeOut)
	{
		sendTitleMessage(uuid, title, subtitle, fadeIn, stay, fadeOut, sound, permission, hasPermission);
	}
	
	@Override
	public void sendTitleMessage(UUID uuid, String title, String subtitle, int fadeIn, int stay, int fadeOut, Sound sound)
	{
		sendTitleMessage(uuid, title, subtitle, fadeIn, stay, fadeOut, sound, permission, hasPermission);
	}

	@Override
	public void sendTitleMessage(UUID uuid, String title, String subtitle, int fadeIn, int stay, int fadeOut,
			String permission, boolean hasPermission)
	{
		sendTitleMessage(uuid, title, subtitle, fadeIn, stay, fadeOut, sound, permission, hasPermission);
	}
	
	@Override
	public void sendTitleMessage(UUID uuid, String title, String subtitle, int fadeIn, int stay, int fadeOut, Sound sound,
			String permission, boolean hasPermission)
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
			sendWhenOnlineOnLocalServer(player, title, subtitle, fadeIn, stay, fadeOut, s, sound,
					p, permission, hasPermission);
			return;
		}
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.TM2BS);
			out.writeUTF(uuid.toString());
			addOutputStream(out, title, subtitle, fadeIn, stay, fadeOut, s, sound, p, permission, hasPermission);
		} catch (IOException e) {
			e.printStackTrace();
		}
        sendPluginMessage(stream);
	}

	@Override
	public void sendTitleMessage(ArrayList<UUID> uuid, String title, String subtitle)
	{
		sendTitleMessage(uuid, title, subtitle, fadeIn, stay, fadeOut, sound, permission, hasPermission);
	}
	
	@Override
	public void sendTitleMessage(ArrayList<UUID> uuid, String title, String subtitle, Sound sound)
	{
		sendTitleMessage(uuid, title, subtitle, fadeIn, stay, fadeOut, sound, permission, hasPermission);
	}
	
	@Override
	public void sendTitleMessage(ArrayList<UUID> uuid, String title, String subtitle, String permission, boolean hasPermission)
	{
		sendTitleMessage(uuid, title, subtitle, fadeIn, stay, fadeOut, sound, permission, hasPermission);
	}
	
	@Override
	public void sendTitleMessage(ArrayList<UUID> uuid, String title, String subtitle, Sound sound, String permission, boolean hasPermission)
	{
		sendTitleMessage(uuid, title, subtitle, fadeIn, stay, fadeOut, sound, permission, hasPermission);
	}
	
	@Override
	public void sendTitleMessage(ArrayList<UUID> uuid, String title, String subtitle, int fadeIn, int stay, int fadeOut)
	{
		sendTitleMessage(uuid, title, subtitle, fadeIn, stay, fadeOut, sound, permission, hasPermission);
	}
	
	@Override
	public void sendTitleMessage(ArrayList<UUID> uuid, String title, String subtitle, int fadeIn, int stay, int fadeOut,
			Sound sound)
	{
		sendTitleMessage(uuid, title, subtitle, fadeIn, stay, fadeOut, sound, permission, hasPermission);
	}
	
	@Override
	public void sendTitleMessage(ArrayList<UUID> uuid, String title, String subtitle, int fadeIn, int stay, int fadeOut,
			String permission, boolean hasPermission)
	{
		sendTitleMessage(uuid, title, subtitle, fadeIn, stay, fadeOut, sound, permission, hasPermission);
	}

	@Override
	public void sendTitleMessage(ArrayList<UUID> uuid, String title, String subtitle, int fadeIn, int stay, int fadeOut,
			Sound sound, String permission, boolean hasPermission)
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
		if(uuid.isEmpty())
		{
			return;
		}
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.TM2BM);
			out.writeInt(uuid.size());
			for(UUID u : uuid)
			{
				out.writeUTF(u.toString());
			}
			addOutputStream(out, title, subtitle, fadeIn, stay, fadeOut, s, sound, p, permission, hasPermission);
		} catch (IOException e) {
			e.printStackTrace();
		}
        sendPluginMessage(stream);
	}
	
	@Override
	public void sendTitleMessage(String title, String subtitle)
	{
		sendTitleMessage(title, subtitle, fadeIn, stay, fadeOut, sound, permission, hasPermission);
	}

	@Override
	public void sendTitleMessage(String title, String subtitle, Sound sound)
	{
		sendTitleMessage(title, subtitle, fadeIn, stay, fadeOut, sound, permission, hasPermission);
	}

	@Override
	public void sendTitleMessage(String title, String subtitle, String permission, boolean hasPermission)
	{
		sendTitleMessage(title, subtitle, fadeIn, stay, fadeOut, sound, permission, hasPermission);
	}

	@Override
	public void sendTitleMessage(String title, String subtitle, Sound sound, String permission, boolean hasPermission)
	{
		sendTitleMessage(title, subtitle, fadeIn, stay, fadeOut, sound, permission, hasPermission);
	}
	
	@Override
	public void sendTitleMessage(String title, String subtitle, int fadeIn, int stay, int fadeOut)
	{
		sendTitleMessage(title, subtitle, fadeIn, stay, fadeOut, sound, permission, hasPermission);
	}

	@Override
	public void sendTitleMessage(String title, String subtitle, int fadeIn, int stay, int fadeOut, Sound sound)
	{
		sendTitleMessage(title, subtitle, fadeIn, stay, fadeOut, sound, permission, hasPermission);
	}

	@Override
	public void sendTitleMessage(String title, String subtitle, int fadeIn, int stay, int fadeOut, String permission, boolean hasPermission)
	{
		sendTitleMessage(title, subtitle, fadeIn, stay, fadeOut, sound, permission, hasPermission);
	}

	@Override
	public void sendTitleMessage(String title, String subtitle, int fadeIn, int stay, int fadeOut, Sound sound,
			String permission, boolean hasPermission)
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
			out.writeUTF(StaticValues.TM2BA);
			addOutputStream(out, title, subtitle, fadeIn, stay, fadeOut, s, sound, p, permission, hasPermission);
		} catch (IOException e) {
			e.printStackTrace();
		}
        sendPluginMessage(stream);
	}
	
	private void sendWhenOnlineOnLocalServer(Player player,
			String title, String subtitle, int fadeIn, int stay, int fadeOut,
			boolean s, Sound sound,
			boolean p, String perm, boolean hasPerm)
	{
		if(s)
		{
    		player.playSound(player.getLocation(), sound, 3.0F, 0.5F);
		}
		if(p)
		{
			if(hasPerm)
			{
				if(player.hasPermission(perm))
				{
					player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
				}
			} else
			{
				if(!player.hasPermission(perm))
				{
					player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
				}
			}
		} else
		{
			player.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
		}
	}	
	
	private void addOutputStream(DataOutputStream out,
			String title, String subtitle, Integer fadeIn, Integer stay, Integer fadeOut,
			boolean s, Sound sound,
			boolean p, String permission, boolean hasPermission
			) throws IOException
	{
		out.writeUTF(title);
		out.writeUTF(subtitle);
		out.writeInt(fadeIn);
		out.writeInt(stay);
		out.writeInt(fadeOut);
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