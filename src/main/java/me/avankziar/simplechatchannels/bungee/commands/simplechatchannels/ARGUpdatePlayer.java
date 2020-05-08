package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.bungee.database.MysqlHandler;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGUpdatePlayer extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGUpdatePlayer(SimpleChatChannels plugin)
	{
		super("updateplayer",
				"scc.cmd.updateplayer",SimpleChatChannels.sccarguments,2,2,"aktualisierungplayer");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String playername = args[1];
		String language = plugin.getYamlHandler().getLanguages();
		if(plugin.getMysqlHandler().hasAccount(playername))
		{
			MysqlHandler mi = plugin.getMysqlHandler();
			mi.updateDataI(player, player.hasPermission("scc.channels.global"), "channel_global");
			mi.updateDataI(player, player.hasPermission("scc.channels.local"), "channel_local");
			mi.updateDataI(player, player.hasPermission("scc.channels.auction"), "channel_auction");
			mi.updateDataI(player, player.hasPermission("scc.channels.trade"), "channel_trade");
			mi.updateDataI(player, player.hasPermission("scc.channels.support"), "channel_support");
			mi.updateDataI(player, player.hasPermission("scc.channels.team"), "channel_team");
			mi.updateDataI(player, player.hasPermission("scc.channels.admin"), "channel_admin");
			mi.updateDataI(player, player.hasPermission("scc.channels.world"), "channel_world");
			mi.updateDataI(player, player.hasPermission("scc.channels.group"), "channel_group");
			mi.updateDataI(player, player.hasPermission("scc.channels.pm"), "channel_pm");
			mi.updateDataI(player, player.hasPermission("scc.channels.temp"), "channel_temp");
			mi.updateDataI(player, player.hasPermission("scc.channels.perma"), "channel_perma");
			mi.updateDataI(player, player.hasPermission("scc.option.spy"), "spy");
			player.sendMessage(plugin.getUtility().tctl(plugin.getYamlHandler().getL().getString(language+".CmdScc.IsUpdated")
					.replace("%player%", playername)));
			if(plugin.getProxy().getPlayer(playername) != null)
			{
				ProxiedPlayer target = plugin.getProxy().getPlayer(playername);
				target.sendMessage(plugin.getUtility().tctl(plugin.getYamlHandler().getL().getString(language+".CmdScc.YouWasUpdated")
						.replace("%player%", playername)));
			}
			return;
		}
		player.sendMessage(plugin.getUtility().tctl(
				plugin.getYamlHandler().getL().getString(
						plugin.getUtility().getLanguage()+".CmdScc.UpdatePlayer.Error")));
		return;
	}
}