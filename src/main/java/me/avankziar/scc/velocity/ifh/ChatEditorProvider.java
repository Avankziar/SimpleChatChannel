package main.java.me.avankziar.scc.velocity.ifh;

import java.util.Optional;
import java.util.UUID;

import com.velocitypowered.api.proxy.Player;

import main.java.me.avankziar.ifh.general.chat.ChatEditor;
import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.velocity.SCC;

public class ChatEditorProvider implements ChatEditor
{
	@Override
	public boolean addOnEditor(UUID uuid, boolean message)
	{
		Optional<Player> player = SCC.getPlugin().getServer().getPlayer(uuid);
		if(player.isEmpty())
		{
			return false;
		}
		if(!SCC.getPlugin().editorplayers.contains(player.get().getUsername()))
		{
			SCC.getPlugin().editorplayers.add(player.get().getUsername());
			if(message)
			{
    			player.get().sendMessage(ChatApi.tl(SCC.getPlugin().getYamlHandler().getLang().getString("CmdEditor.Active")));
    			return true;
			}
		}
		return false;
	}

	@Override
	public String getProvider()
	{
		return SCC.getPlugin().pluginName;
	}

	@Override
	public boolean removeFromEditor(UUID uuid, boolean message)
	{
		Optional<Player> player = SCC.getPlugin().getServer().getPlayer(uuid);
		if(player.isEmpty())
		{
			return false;
		}
		if(SCC.getPlugin().editorplayers.contains(player.get().getUsername()))
		{
			SCC.getPlugin().editorplayers.remove(player.get().getUsername());
			if(message)
			{
    			player.get().sendMessage(ChatApi.tl(SCC.getPlugin().getYamlHandler().getLang().getString("CmdEditor.Deactive")));
    			return true;
			}
		}
		return false;
	}

}
