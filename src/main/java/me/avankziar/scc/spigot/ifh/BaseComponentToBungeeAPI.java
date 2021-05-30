package main.java.me.avankziar.scc.spigot.ifh;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Sound;

import main.java.me.avankziar.interfacehub.spigot.interfaces.BaseComponentToBungee;
import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.StaticValues;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;
import net.md_5.bungee.api.chat.BaseComponent;

public class BaseComponentToBungeeAPI implements BaseComponentToBungee
{
	@Override
	public void sendMessage(UUID uuid, ArrayList<ArrayList<BaseComponent>> message)
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.BCTBS);
			out.writeUTF(uuid.toString());
			writeMessage(out, message);
		} catch (IOException e) {
			e.printStackTrace();
		}
        SimpleChatChannels.getPlugin().getServer().sendPluginMessage(
        		SimpleChatChannels.getPlugin(), StaticValues.SCC_TOBUNGEE, stream.toByteArray());
	}

	@Override
	public void sendMessage(UUID uuid, Sound sound, ArrayList<ArrayList<BaseComponent>> message)
	{
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.BCTBSS);
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
	public void sendMessage(ArrayList<UUID> uuids, ArrayList<ArrayList<BaseComponent>> message)
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
	public void sendMessage(ArrayList<UUID> uuids, Sound sound, ArrayList<ArrayList<BaseComponent>> message)
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
	public void sendMessage(ArrayList<UUID> uuids, String permission, boolean hasPermission, ArrayList<ArrayList<BaseComponent>> message)
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
	public void sendMessage(ArrayList<UUID> uuids, String permission, boolean hasPermission, Sound sound, ArrayList<ArrayList<BaseComponent>> message)
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
	public void sendMessage(ArrayList<ArrayList<BaseComponent>> message)
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
	public void sendMessage(Sound sound, ArrayList<ArrayList<BaseComponent>> message)
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
	public void sendMessage(String permission, boolean hasPermission, ArrayList<ArrayList<BaseComponent>> message)
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
	public void sendMessage(String permission, boolean hasPermission, Sound sound, ArrayList<ArrayList<BaseComponent>> message)
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
}
