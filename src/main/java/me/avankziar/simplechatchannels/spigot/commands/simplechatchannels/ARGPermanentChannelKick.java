package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.Utility;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.spigot.interfaces.PermanentChannel;
public class ARGPermanentChannelKick extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelKick(SimpleChatChannels plugin)
	{
		super("pckick","scc.cmd.pc.kick",SimpleChatChannels.sccarguments,3,3,"pcrausschmeißen");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage() + ".CmdScc.";
		PermanentChannel cc = PermanentChannel.getChannelFromName(args[1]);
		if(cc==null)
		{
			///Der angegebene Channel &5perma&fnenten %channel% existiert nicht!
			player.spigot().sendMessage(utility.tctl(plugin.getYamlHandler().getL().getString(language+"ChannelGeneral.ChannelNotExistII")
					.replace("%channel%", args[1])));
			return;
		}
		if(!cc.getCreator().equals(player.getUniqueId().toString())
				&& !cc.getVice().contains(player.getUniqueId().toString()))
		{
			///Du bist nicht der Ersteller des CustomChannel!
			player.spigot().sendMessage(utility.tctl(plugin.getYamlHandler().getL().getString(language+"ChannelGeneral.NotChannelViceII")
					.replace("%channel%", cc.getNameColor()+cc.getName())));
			return;
		}
		if(plugin.getMysqlHandler().getDataI(args[2], "player_uuid", "player_name") == null)
		{
			///Der Spieler ist nicht online oder existiert nicht!
			player.spigot().sendMessage(utility.tctlYaml(language+"NoPlayerExist"));
			return;
		}
		String targetuuid = (String) plugin.getMysqlHandler().getDataI(args[2], "player_uuid", "player_name");
		if(cc.getCreator().equals(targetuuid) && cc.getVice().contains(player.getUniqueId().toString()))
		{
			///Du als Stellvertreter kannst den Ersteller nicht kicken!
			player.spigot().sendMessage(utility.tctlYaml(language+"PCKick.ViceCannotKickCreator"));
			return;
		}
		if(!cc.getMembers().contains(targetuuid))
		{
			///Der angegebene Spieler ist nicht Mitglied im CustomChannel!
			player.spigot().sendMessage(utility.tctlYaml(language+"ChannelGeneral.NotChannelMemberII"));
			return;
		}
		if(targetuuid.equals(player.getUniqueId().toString()))
		{
			///Du als Ersteller kannst dich nicht kicken!
			player.spigot().sendMessage(utility.tctlYaml(language+"PCKick.CannotSelfKick"));
			return;
		}
		cc.removeMembers(targetuuid);
		cc.removeVice(targetuuid);
		plugin.getUtility().updatePermanentChannels(cc);
		if(plugin.getServer().getPlayer(args[2]) != null)
		{
			Player target = plugin.getServer().getPlayer(args[2]);
			///Du wurdest aus dem CustomChannel gekickt!
			target.spigot().sendMessage(utility.tctl(plugin.getYamlHandler().getL().getString(language+"PCKick.YouWereKicked")
					.replace("%channel%", cc.getNameColor()+cc.getName())));
		}
		///Du hast &f%player% &eaus dem Channel gekickt!
		player.spigot().sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+"PCKick.YouKicked")
				.replace("%player%", args[2]).replace("%channel%", cc.getNameColor()+cc.getName())));
		for(Player members : plugin.getServer().getOnlinePlayers())
		{
			if(cc.getMembers().contains(members.getUniqueId().toString()))
			{
				///Der Spieler &f%player% &ewurde aus dem Channel gekickt!
				members.spigot().sendMessage(utility.tctl(
						plugin.getYamlHandler().getL().getString(language+"PCKick.KickedSomeone")
						.replace("%player%", args[2]).replace("%channel%", cc.getNameColor()+cc.getName())));
			}
		}
		return;
	}
}