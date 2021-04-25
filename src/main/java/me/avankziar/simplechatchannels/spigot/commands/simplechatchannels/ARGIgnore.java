package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.IgnoreObject;
import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.spigot.database.MysqlHandler;

public class ARGIgnore extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGIgnore(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String language = "CmdScc.";
		String target = args[1];
		if(Bukkit.getPlayer(target) == null)
		{
			///Der Spieler ist nicht online oder existiert nicht!
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("PlayerNotExist")));
			return;
		}
		Player t = Bukkit.getPlayer(target);
		IgnoreObject io = (IgnoreObject) plugin.getMysqlHandler().getData(MysqlHandler.Type.IGNOREOBJECT,
				"`player_uuid` = ? AND `ignore_uuid` = ?",
				player.getUniqueId().toString(), t.getUniqueId().toString());
		if(io != null)
		{
			plugin.getMysqlHandler().deleteData(MysqlHandler.Type.IGNOREOBJECT,
					"`player_uuid` = ? AND `ignore_uuid` = ?",
					player.getUniqueId().toString(), t.getUniqueId().toString());
			///Du hast den Spieler %player% von deiner Ignoreliste &7genommen!
			player.spigot().sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(language+"Ignore.DontIgnore")
					.replace("%player%", target)));
		} else
		{
			plugin.getMysqlHandler().create(MysqlHandler.Type.IGNOREOBJECT,
					new IgnoreObject(
							player.getUniqueId().toString(),
							t.getUniqueId().toString(), target));
			///Der Spieler %player% wird von dir ignoriert!
			player.spigot().sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(language+"Ignore.DoIgnore")
					.replace("%player%", target)));
		}
		return;
	}
}