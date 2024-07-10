package main.java.me.avankziar.scc.spigot.commands.scc;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.objects.IgnoreObject;
import main.java.me.avankziar.scc.spigot.SCC;
import main.java.me.avankziar.scc.spigot.assistance.Utility;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;

public class ARGIgnore extends ArgumentModule
{
	private SCC plugin;
	
	public ARGIgnore(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String t = args[1];
		UUID uuid = Utility.convertNameToUUID(t);
		if(uuid == null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		IgnoreObject io = (IgnoreObject) plugin.getMysqlHandler().getData(MysqlType.IGNOREOBJECT,
				"`player_uuid` = ? AND `ignore_uuid` = ?",
				player.getUniqueId().toString(), uuid.toString());
		if(io != null)
		{
			plugin.getMysqlHandler().deleteData(MysqlType.IGNOREOBJECT,
					"`player_uuid` = ? AND `ignore_uuid` = ?",
					player.getUniqueId().toString(), uuid.toString());
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Ignore.Deactive")
					.replace("%player%", t)));
		} else
		{
			plugin.getMysqlHandler().create(MysqlType.IGNOREOBJECT,
					new IgnoreObject(player.getUniqueId().toString(), uuid.toString(), t));
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Ignore.Active")
					.replace("%player%", t)));
		}
	}
}