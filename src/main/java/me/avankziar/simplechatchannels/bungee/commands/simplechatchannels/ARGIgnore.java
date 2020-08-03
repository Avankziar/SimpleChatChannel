package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.IgnoreObject;
import main.java.me.avankziar.simplechatchannels.bungee.database.MysqlHandler;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String language = "CmdScc.";
		String target = args[1];
		if(ProxyServer.getInstance().getPlayer(target) == null)
		{
			///Der Spieler ist nicht online oder existiert nicht!
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"NoPlayerExist")));
			return;
		}
		ProxiedPlayer t = ProxyServer.getInstance().getPlayer(target);
		IgnoreObject io = (IgnoreObject) plugin.getMysqlHandler().getData(MysqlHandler.Type.IGNOREOBJECT,
				"`player_uuid` = ? AND `ignore_uuid` = ?",
				player.getUniqueId().toString(), t.getUniqueId().toString());
		if(io != null)
		{
			plugin.getMysqlHandler().deleteData(MysqlHandler.Type.IGNOREOBJECT,
					"`player_uuid` = ? AND `ignore_uuid` = ?",
					player.getUniqueId().toString(), t.getUniqueId().toString());
			///Du hast den Spieler %player% von deiner Ignoreliste &7genommen!
			player.sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(language+"Ignore.DontIgnore")
					.replace("%player%", target)));
		} else
		{
			plugin.getMysqlHandler().create(MysqlHandler.Type.IGNOREOBJECT,
					new IgnoreObject(
							player.getUniqueId().toString(),
							t.getUniqueId().toString(), target));
			///Der Spieler %player% wird von dir ignoriert!
			player.sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(language+"Ignore.DoIgnore")
					.replace("%player%", target)));
		}
		return;
	}
}
