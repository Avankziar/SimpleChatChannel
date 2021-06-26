package main.java.me.avankziar.scc.bungee.ifh;

import java.util.UUID;

import main.java.me.avankziar.interfacehub.general.chat.ChatEditor;
import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.scc.objects.ChatApi;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ChatEditorProvider implements ChatEditor
{
	@Override
	public boolean addOnEditor(UUID uuid, boolean message)
	{
		ProxiedPlayer player = SimpleChatChannels.getPlugin().getProxy().getPlayer(uuid);
		if(player == null)
		{
			return false;
		}
		if(!SimpleChatChannels.getPlugin().editorplayers.contains(player.getName()))
		{
			if(message)
			{
				SimpleChatChannels.getPlugin().editorplayers.add(player.getName());
    			player.sendMessage(ChatApi.tctl(SimpleChatChannels.getPlugin().getYamlHandler().getLang().getString("CmdEditor.Active")));
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
		ProxiedPlayer player = SimpleChatChannels.getPlugin().getProxy().getPlayer(uuid);
		if(player == null)
		{
			return false;
		}
		if(SimpleChatChannels.getPlugin().editorplayers.contains(player.getName()))
		{
			if(message)
			{
				SimpleChatChannels.getPlugin().editorplayers.remove(player.getName());
    			player.sendMessage(ChatApi.tctl(SimpleChatChannels.getPlugin().getYamlHandler().getLang().getString("CmdEditor.Deactive")));
    			return true;
			}
		}
		return false;
	}

}
