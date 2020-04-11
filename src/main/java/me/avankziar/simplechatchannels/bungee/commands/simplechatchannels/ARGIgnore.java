package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGIgnore extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGIgnore(SimpleChatChannels plugin)
	{
		super("ignore","scc.cmd.ignore",SimpleChatChannels.sccarguments,2,2,"ignorieren");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String language = plugin.getUtility().getLanguage() + ".CmdScc.";
		String target = args[1];
		if(ProxyServer.getInstance().getPlayer(target) == null)
		{
			///Der Spieler ist nicht online oder existiert nicht!
			player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+"NoPlayerExist")));
			return;
		}
		ProxiedPlayer t = ProxyServer.getInstance().getPlayer(target);
		if(plugin.getMysqlHandler().existIgnore(player, t.getUniqueId().toString()))
		{
			plugin.getMysqlHandler().deleteDataII(
					player.getUniqueId().toString(), t.getUniqueId().toString(), "player_uuid", "ignore_uuid");
			///Du hast den Spieler %player% von deiner Ignoreliste &7genommen!
			player.sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+"Ignore.DontIgnore")
					.replace("%player%", target)));
		} else
		{
			plugin.getMysqlHandler().createIgnore(player, t);
			///Der Spieler %player% wird von dir ignoriert!
			player.sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+"Ignore.DoIgnore")
					.replace("%player%", target)));
		}
		return;
	}
}
