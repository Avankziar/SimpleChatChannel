package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.Utility;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.spigot.interfaces.PermanentChannel;

public class ARGPermanentChannelInherit extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelInherit(SimpleChatChannels plugin)
	{
		super("pcinherit","scc.cmd.pc.inherit",SimpleChatChannels.sccarguments,3,3,"pcerben");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage();
		String scc = ".CmdScc.";
		PermanentChannel cc = PermanentChannel.getChannelFromName(args[1]);
		if(plugin.getMysqlHandler().getDataI(args[2], "player_uuid", "player_name") == null)
		{
			///Der Spieler ist nicht online oder existiert nicht!
			player.spigot().sendMessage(utility.tctlYaml(language+scc+"NoPlayerExist"));
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
		player.spigot().sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+scc+"PCInherit.NewCreator")
				.replace("%channel%", cc.getNameColor()+cc.getName())
				.replace("%creator%", args[2])
				.replace("%oldcreator%", oldcreator)));
		if(plugin.getServer().getPlayer(oldcreator) != null)
		{
			Player target = plugin.getServer().getPlayer(oldcreator);
			///Du wurdest vom CustomChannel %channel% gebannt!
			target.spigot().sendMessage(utility.tctl(
					plugin.getYamlHandler().getL().getString(language+scc+"PCInherit.NewCreator")
					.replace("%channel%", cc.getNameColor()+cc.getName())
					.replace("%creator%", args[2])
					.replace("%oldcreator%", oldcreator)));
		}
		if(plugin.getServer().getPlayer(args[2]) != null)
		{
			Player target = plugin.getServer().getPlayer(args[2]);
			///Du wurdest vom CustomChannel %channel% gebannt!
			target.spigot().sendMessage(utility.tctl(
					plugin.getYamlHandler().getL().getString(language+scc+"PCInherit.NewCreator")
					.replace("%channel%", cc.getNameColor()+cc.getName())
					.replace("%creator%", args[2])
					.replace("%oldcreator%", oldcreator)));
		}
		for(Player member : plugin.getServer().getOnlinePlayers())
		{
			if(cc.getMembers().contains(member.getUniqueId().toString()))
			{
				member.spigot().sendMessage(utility.tctl(
						plugin.getYamlHandler().getL().getString(language+scc+"PCInherit.NewCreator")
						.replace("%channel%", cc.getNameColor()+cc.getName())
						.replace("%creator%", args[2])
						.replace("%oldcreator%", oldcreator)));
			}
		}
		return;
	}
}
