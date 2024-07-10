package main.java.me.avankziar.scc.spigot.commands.scc;

import java.io.IOException;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.objects.ItemJson;
import main.java.me.avankziar.scc.spigot.SCC;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;

public class ARGItem_Rename extends ArgumentModule
{
	private SCC plugin;
	
	public ARGItem_Rename(SCC plugin, ArgumentConstructor argumentConstructor)
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
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Item.Rename.NotDefault")));
			return;
		}
		ItemJson ij = (ItemJson) plugin.getMysqlHandler().getData(MysqlType.ITEMJSON,
						"`owner` = ? AND `itemname` = ?", player.getUniqueId().toString(), old);
		if(ij == null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Item.Rename.ItemDontExist")));
			return;
		}
		if(plugin.getMysqlHandler().exist(MysqlType.ITEMJSON, "`owner` = ? AND `itemname` = ?",
						player.getUniqueId().toString(), newname))
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Item.Rename.NameAlreadyExist")));
			return;
		}
		final String oldname = ij.getItemName();
		ij.setItemName(newname);
		plugin.getMysqlHandler().updateData(MysqlType.ITEMJSON, ij, "`owner` = ? AND `itemname` = ?",
				player.getUniqueId().toString(), oldname);
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Item.Rename.Renamed")
				.replace("%oldname%", oldname)
				.replace("%newname%", newname)));
	}
}