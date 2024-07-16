package main.java.me.avankziar.scc.spigot.ifh;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.objects.StaticValues;
import main.java.me.avankziar.scc.spigot.SCC;
import me.avankziar.ifh.general.chat.ChatEditor;

public class ChatEditorProvider implements ChatEditor
{
	@Override
	public boolean addOnEditor(UUID uuid, boolean message)
	{
		Player player = SCC.getPlugin().getServer().getPlayer(uuid);
		if(player == null)
		{
			return false;
		}
		if(!SCC.getPlugin().editorplayers.contains(player.getName()))
		{
			SCC.getPlugin().editorplayers.add(player.getName());
			send(player, message);
			if(message)
			{
				player.spigot().sendMessage(ChatApi.tctl(SCC.getPlugin().getYamlHandler().getLang().getString("CmdEditor.Active")));
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
		Player player = SCC.getPlugin().getServer().getPlayer(uuid);
		if(player == null)
		{
			return false;
		}
		if(SCC.getPlugin().editorplayers.contains(player.getName()))
		{
			SCC.getPlugin().editorplayers.remove(player.getName());
			send(player, message);
			if(message)
			{
				player.spigot().sendMessage(ChatApi.tctl(SCC.getPlugin().getYamlHandler().getLang().getString("CmdEditor.Deactive")));
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
        player.sendPluginMessage(SCC.getPlugin(), StaticValues.SCC_TOPROXY, stream.toByteArray());
    }
}
