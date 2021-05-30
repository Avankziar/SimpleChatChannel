package main.java.me.avankziar.scc.spigot.ifh;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Sound;

import main.java.me.avankziar.interfacehub.spigot.interfaces.MessageToBungee;
import main.java.me.avankziar.scc.objects.StaticValues;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;

public class MessageToBungeeAPI implements MessageToBungee
{	
	@Override
	public void sendMessage(UUID uuid, String... message)
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.MTBS);
			out.writeUTF(uuid.toString());
			writeMessage(out, message);
		} catch (IOException e) {
			e.printStackTrace();
		}
        SimpleChatChannels.getPlugin().getServer().sendPluginMessage(
        		SimpleChatChannels.getPlugin(), StaticValues.SCC_TOBUNGEE, stream.toByteArray());
	}
	
	@Override
	public void sendMessage(UUID uuid, Sound sound, String... message)
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.MTBSS);
			out.writeUTF(uuid.toString());
			out.writeUTF(sound.toString());
			writeMessage(out, message);
		} catch (IOException e) {
			e.printStackTrace();
		}
        SimpleChatChannels.getPlugin().getServer().sendPluginMessage(
        		SimpleChatChannels.getPlugin(), StaticValues.SCC_TOBUNGEE, stream.toByteArray());
	}

	@Override
	public void sendMessage(ArrayList<UUID> uuids, String... message)
	{
		if(uuids.isEmpty())
		{
			return;
		}
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.MTBM);
			writeUUIDs(out, uuids);
			writeMessage(out, message);
		} catch (IOException e) {
			e.printStackTrace();
		}
        SimpleChatChannels.getPlugin().getServer().sendPluginMessage(
        		SimpleChatChannels.getPlugin(), StaticValues.SCC_TOBUNGEE, stream.toByteArray());
	}

	@Override
	public void sendMessage(ArrayList<UUID> uuids, Sound sound, String... message)
	{
		if(uuids.isEmpty())
		{
			return;
		}
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.MTBMS);
			out.writeUTF(sound.toString());
			writeUUIDs(out, uuids);
			writeMessage(out, message);
		} catch (IOException e) {
			e.printStackTrace();
		}
        SimpleChatChannels.getPlugin().getServer().sendPluginMessage(
        		SimpleChatChannels.getPlugin(), StaticValues.SCC_TOBUNGEE, stream.toByteArray());
	}

	@Override
	public void sendMessage(ArrayList<UUID> uuids, String permission, boolean hasPermission, String... message)
	{
		if(uuids.isEmpty())
		{
			return;
		}
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.MTBMP);
			out.writeUTF(permission);
			out.writeBoolean(hasPermission);
			writeUUIDs(out, uuids);
			writeMessage(out, message);
		} catch (IOException e) {
			e.printStackTrace();
		}
        SimpleChatChannels.getPlugin().getServer().sendPluginMessage(
        		SimpleChatChannels.getPlugin(), StaticValues.SCC_TOBUNGEE, stream.toByteArray());
	}

	@Override
	public void sendMessage(ArrayList<UUID> uuids, String permission, boolean hasPermission, Sound sound, String... message)
	{
		if(uuids.isEmpty())
		{
			return;
		}
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.MTBMSP);
			out.writeUTF(sound.toString());
			out.writeUTF(permission);
			out.writeBoolean(hasPermission);
			writeUUIDs(out, uuids);
			writeMessage(out, message);
		} catch (IOException e) {
			e.printStackTrace();
		}
        SimpleChatChannels.getPlugin().getServer().sendPluginMessage(
        		SimpleChatChannels.getPlugin(), StaticValues.SCC_TOBUNGEE, stream.toByteArray());
	}
	
	@Override
	public void sendMessage(String... message)
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.MTBA);
			writeMessage(out, message);
		} catch (IOException e) {
			e.printStackTrace();
		}
        SimpleChatChannels.getPlugin().getServer().sendPluginMessage(
        		SimpleChatChannels.getPlugin(), StaticValues.SCC_TOBUNGEE, stream.toByteArray());
	}
	
	@Override
	public void sendMessage(Sound sound, String... message)
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.MTBAS);
			out.writeUTF(sound.toString());
			writeMessage(out, message);
		} catch (IOException e) {
			e.printStackTrace();
		}
        SimpleChatChannels.getPlugin().getServer().sendPluginMessage(
        		SimpleChatChannels.getPlugin(), StaticValues.SCC_TOBUNGEE, stream.toByteArray());
	}
	
	@Override
	public void sendMessage(String permission, boolean hasPermission, String... message)
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.MTBASP);
			out.writeUTF(permission);
			out.writeBoolean(hasPermission);
			writeMessage(out, message);
		} catch (IOException e) {
			e.printStackTrace();
		}
        SimpleChatChannels.getPlugin().getServer().sendPluginMessage(
        		SimpleChatChannels.getPlugin(), StaticValues.SCC_TOBUNGEE, stream.toByteArray());
	}
	
	@Override
	public void sendMessage(String permission, boolean hasPermission, Sound sound, String... message)
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.MTBASP);
			out.writeUTF(permission);
			out.writeBoolean(hasPermission);
			out.writeUTF(sound.toString());
			writeMessage(out, message);
		} catch (IOException e) {
			e.printStackTrace();
		}
        SimpleChatChannels.getPlugin().getServer().sendPluginMessage(
        		SimpleChatChannels.getPlugin(), StaticValues.SCC_TOBUNGEE, stream.toByteArray());
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
}