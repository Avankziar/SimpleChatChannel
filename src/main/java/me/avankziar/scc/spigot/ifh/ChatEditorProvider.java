package main.java.me.avankziar.scc.spigot.ifh;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.entity.Player;

import main.java.me.avankziar.ifh.general.chat.ChatEditor;
import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.StaticValues;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;

public class ChatEditorProvider implements ChatEditor
{
	@Override
	public boolean addOnEditor(UUID uuid, boolean message)
	{
		Player player = SimpleChatChannels.getPlugin().getServer().getPlayer(uuid);
		if(player == null)
		{
			return false;
		}
		if(!SimpleChatChannels.getPlugin().editorplayers.contains(player.getName()))
		{
			SimpleChatChannels.getPlugin().editorplayers.add(player.getName());
			send(player, message);
			if(message)
			{
    			player.sendMessage(ChatApi.tl(SimpleChatChannels.getPlugin().getYamlHandler().getLang().getString("CmdEditor.Active")));
    			return true;
			}
		}
		return false;
	}

	@Override
	public String getProvider()
	{
		return SimpleChatChannels.pluginName;
	}

	@Override
	public boolean removeFromEditor(UUID uuid, boolean message)
	{
		Player player = SimpleChatChannels.getPlugin().getServer().getPlayer(uuid);
		if(player == null)
		{
			return false;
		}
		if(SimpleChatChannels.getPlugin().editorplayers.contains(player.getName()))
		{
			SimpleChatChannels.getPlugin().editorplayers.remove(player.getName());
			send(player, message);
			if(message)
			{
    			player.sendMessage(ChatApi.tl(SimpleChatChannels.getPlugin().getYamlHandler().getLang().getString("CmdEditor.Deactive")));
    			return true;
			}
		}
		return false;
	}
	
	private void send(Player player, boolean toggle)
    {
    	ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
			out.writeUTF(StaticValues.SCC_EDITOR);
			out.writeUTF(player.getName());
			out.writeBoolean(toggle);
		} catch (IOException e) {
			e.printStackTrace();
		}
        player.sendPluginMessage(SimpleChatChannels.getPlugin(), StaticValues.SCC_TOBUNGEE, stream.toByteArray());
    }
}
