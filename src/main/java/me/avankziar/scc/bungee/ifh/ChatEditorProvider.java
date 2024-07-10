package main.java.me.avankziar.scc.bungee.ifh;

import java.util.UUID;

import main.java.me.avankziar.ifh.general.chat.ChatEditor;
import main.java.me.avankziar.scc.bungee.SCC;
import main.java.me.avankziar.scc.general.assistance.ChatApi;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ChatEditorProvider implements ChatEditor
{
	@Override
	public boolean addOnEditor(UUID uuid, boolean message)
	{
		ProxiedPlayer player = SCC.getPlugin().getProxy().getPlayer(uuid);
		if(player == null)
		{
			return false;
		}
		if(!SCC.getPlugin().editorplayers.contains(player.getName()))
		{
			SCC.getPlugin().editorplayers.add(player.getName());
			if(message)
			{
    			player.sendMessage(ChatApi.tctl(SCC.getPlugin().getYamlHandler().getLang().getString("CmdEditor.Active")));
    			return true;
			}
		}
		return false;
	}

	@Override
	public String getProvider()
	{
		return SCC.pluginName;
	}

	@Override
	public boolean removeFromEditor(UUID uuid, boolean message)
	{
		ProxiedPlayer player = SCC.getPlugin().getProxy().getPlayer(uuid);
		if(player == null)
		{
			return false;
		}
		if(SCC.getPlugin().editorplayers.contains(player.getName()))
		{
			SCC.getPlugin().editorplayers.remove(player.getName());
			if(message)
			{
    			player.sendMessage(ChatApi.tctl(SCC.getPlugin().getYamlHandler().getLang().getString("CmdEditor.Deactive")));
    			return true;
			}
		}
		return false;
	}

}
