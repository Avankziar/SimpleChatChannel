package main.java.me.avankziar.scc.spigot.commands.scc;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.chat.IgnoreObject;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.spigot.database.MysqlHandler;

public class ARGIgnore extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGIgnore(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String t = args[1];
		if(plugin.getServer().getPlayer(t) == null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		Player target = plugin.getServer().getPlayer(t);
		IgnoreObject io = (IgnoreObject) plugin.getMysqlHandler().getData(MysqlHandler.Type.IGNOREOBJECT,
				"`player_uuid` = ? AND `ignore_uuid` = ?",
				player.getUniqueId().toString(), target.getUniqueId().toString());
		if(io != null)
		{
			plugin.getMysqlHandler().deleteData(MysqlHandler.Type.IGNOREOBJECT,
					"`player_uuid` = ? AND `ignore_uuid` = ?",
					player.getUniqueId().toString(), target.getUniqueId().toString());
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Ignore.Deactive")
					.replace("%player%", t)));
		} else
		{
			plugin.getMysqlHandler().create(MysqlHandler.Type.IGNOREOBJECT,
					new IgnoreObject(player.getUniqueId().toString(), target.getUniqueId().toString(), t));
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Ignore.Active")
					.replace("%player%", t)));
		}
	}
}