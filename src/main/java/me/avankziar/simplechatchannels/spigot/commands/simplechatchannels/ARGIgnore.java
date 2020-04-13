package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.Utility;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandModule;
import net.md_5.bungee.api.ProxyServer;

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
		Player player = (Player) sender;
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage() + ".CmdScc.";
		String target = args[1];
		if(ProxyServer.getInstance().getPlayer(target) == null)
		{
			///Der Spieler ist nicht online oder existiert nicht!
			player.spigot().sendMessage(utility.tctlYaml(language+"NoPlayerExist"));
			return;
		}
		Player t = Bukkit.getPlayer(target);
		if(plugin.getMysqlHandler().existIgnore(player, t.getUniqueId().toString()))
		{
			plugin.getMysqlHandler().deleteDataII(
					player.getUniqueId().toString(), t.getUniqueId().toString(), "player_uuid", "ignore_uuid");
			///Du hast den Spieler %player% von deiner Ignoreliste &7genommen!
			player.spigot().sendMessage(utility.tctl(
					plugin.getYamlHandler().getL().getString(language+"Ignore.DontIgnore")
					.replace("%player%", target)));
		} else
		{
			plugin.getMysqlHandler().createIgnore(player, t);
			///Der Spieler %player% wird von dir ignoriert!
			player.spigot().sendMessage(utility.tctl(
					plugin.getYamlHandler().getL().getString(language+"Ignore.DoIgnore")
					.replace("%player%", target)));
		}
		return;
	}
}