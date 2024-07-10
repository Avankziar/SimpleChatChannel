package main.java.me.avankziar.scc.bungee.commands.scc;

import java.util.UUID;

import main.java.me.avankziar.scc.bungee.SCC;
import main.java.me.avankziar.scc.bungee.assistance.Utility;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.general.assistance.ChatApiOld;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.objects.IgnoreObject;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String t = args[1];
		UUID uuid = Utility.convertNameToUUID(t);
		if(uuid == null)
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
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
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Ignore.Deactive")
					.replace("%player%", t)));
		} else
		{
			plugin.getMysqlHandler().create(MysqlType.IGNOREOBJECT,
					new IgnoreObject(player.getUniqueId().toString(), uuid.toString(), t));
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Ignore.Active")
					.replace("%player%", t)));
		}
	}
}