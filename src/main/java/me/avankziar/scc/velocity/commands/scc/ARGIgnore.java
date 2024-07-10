package main.java.me.avankziar.scc.velocity.commands.scc;

import java.util.UUID;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.objects.IgnoreObject;
import main.java.me.avankziar.scc.velocity.SCC;
import main.java.me.avankziar.scc.velocity.assistance.Utility;
import main.java.me.avankziar.scc.velocity.commands.tree.ArgumentModule;

public class ARGIgnore extends ArgumentModule
{
	private SCC plugin;
	
	public ARGIgnore(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSource sender, String[] args)
	{
		Player player = (Player) sender;
		String t = args[1];
		UUID uuid = Utility.convertNameToUUID(t);
		if(uuid == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
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
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.Ignore.Deactive")
					.replace("%player%", t)));
		} else
		{
			plugin.getMysqlHandler().create(MysqlType.IGNOREOBJECT,
					new IgnoreObject(player.getUniqueId().toString(), uuid.toString(), t));
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.Ignore.Active")
					.replace("%player%", t)));
		}
	}
}