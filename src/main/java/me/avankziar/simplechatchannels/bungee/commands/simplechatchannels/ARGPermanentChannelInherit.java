package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.PermanentChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannelInherit extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelInherit(SimpleChatChannels plugin)
	{
		super("pcinherit","scc.cmd.pc.inherit",SimpleChatChannels.sccarguments,3,3,"pcerben",
				"<Channelname>;<Player>".split(";"));
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage();
		String scc = ".CmdScc.";
		PermanentChannel cc = PermanentChannel.getChannelFromName(args[1]);
		if(plugin.getMysqlHandler().getDataI(args[2], "player_uuid", "player_name") == null)
		{
			///Der Spieler ist nicht online oder existiert nicht!
			player.sendMessage(utility.tctlYaml(language+scc+"NoPlayerExist"));
			return;
		}
		String uuid = (String) plugin.getMysqlHandler().getDataI(args[2], "player_uuid", "player_name");
		final String oldcreatoruuid = cc.getCreator();
		final String oldcreator = (String) plugin.getMysqlHandler().getDataI(oldcreatoruuid, "player_name", "player_uuid");
		cc.removeMembers(oldcreatoruuid);
		cc.setCreator(uuid);
		cc.addMembers(uuid);
		plugin.getUtility().updatePermanentChannels(cc);
		///Im &5perma&fnenten &eChannel %channel% &r&ebeerbt der Spieler &a%creator% &eden Spieler &c%oldcreator% &eals neuer Ersteller des Channels.
		player.sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+scc+"PCInherit.NewCreator")
				.replace("%channel%", cc.getNameColor()+cc.getName())
				.replace("%creator%", args[2])
				.replace("%oldcreator%", oldcreator)));
		if(ProxyServer.getInstance().getPlayer(oldcreator) != null)
		{
			ProxiedPlayer target = ProxyServer.getInstance().getPlayer(oldcreator);
			///Du wurdest vom CustomChannel %channel% gebannt!
			target.sendMessage(utility.tctl(
					plugin.getYamlHandler().getL().getString(language+scc+"PCInherit.NewCreator")
					.replace("%channel%", cc.getNameColor()+cc.getName())
					.replace("%creator%", args[2])
					.replace("%oldcreator%", oldcreator)));
		}
		if(ProxyServer.getInstance().getPlayer(args[2]) != null)
		{
			ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[2]);
			///Du wurdest vom CustomChannel %channel% gebannt!
			target.sendMessage(utility.tctl(
					plugin.getYamlHandler().getL().getString(language+scc+"PCInherit.NewCreator")
					.replace("%channel%", cc.getNameColor()+cc.getName())
					.replace("%creator%", args[2])
					.replace("%oldcreator%", oldcreator)));
		}
		for(ProxiedPlayer member : ProxyServer.getInstance().getPlayers())
		{
			if(cc.getMembers().contains(member.getUniqueId().toString()))
			{
				member.sendMessage(utility.tctl(
						plugin.getYamlHandler().getL().getString(language+scc+"PCInherit.NewCreator")
						.replace("%channel%", cc.getNameColor()+cc.getName())
						.replace("%creator%", args[2])
						.replace("%oldcreator%", oldcreator)));
			}
		}
		return;
	}
}
