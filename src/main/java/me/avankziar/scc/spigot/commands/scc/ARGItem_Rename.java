package main.java.me.avankziar.scc.spigot.commands.scc;

import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.chat.ItemJson;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.spigot.database.MysqlHandler.Type;

public class ARGItem_Rename extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGItem_Rename(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args) throws IOException
	{
		Player player = (Player) sender;
		String old = args[2];
		String newname = args[3];
		if(newname.equalsIgnoreCase("default"))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.Item.Rename.NotDefault")));
			return;
		}
		ItemJson ij = (ItemJson) plugin.getMysqlHandler().getData(Type.ITEMJSON,
						"`owner` = ? AND `itemname` = ?", player.getUniqueId().toString(), old);
		if(ij == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.Item.Rename.ItemDontExist")));
			return;
		}
		if(plugin.getMysqlHandler().exist(Type.ITEMJSON, "`owner` = ? AND `itemname` = ?",
						player.getUniqueId().toString(), newname))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.Item.Rename.NameAlreadyExist")));
			return;
		}
		final String oldname = ij.getItemName();
		ij.setItemName(newname);
		plugin.getMysqlHandler().updateData(Type.ITEMJSON, ij, "`owner` = ? AND `itemname` = ?",
				player.getUniqueId().toString(), oldname);
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.Item.Rename.Renamed")
				.replace("%oldname%", oldname)
				.replace("%newname%", newname)));
	}
}