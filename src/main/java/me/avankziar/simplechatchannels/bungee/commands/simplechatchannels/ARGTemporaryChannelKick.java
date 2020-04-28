package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.TemporaryChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGTemporaryChannelKick extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGTemporaryChannelKick(SimpleChatChannels plugin)
	{
		super("tckick","scc.cmd.tc.kick",SimpleChatChannels.sccarguments,2,2,"tcrausschmeißen",
				"<Player>".split(";"));
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage() + ".CmdScc.";
		TemporaryChannel cc = TemporaryChannel.getCustomChannel(player);
		if(cc==null)
		{
			///Du bist in keinem CustomChannel!
			player.sendMessage(utility.tctlYaml(language+"ChannelGeneral.NotInAChannel"));
			return;
		}
		ProxiedPlayer creator = cc.getCreator();
		if(!creator.getName().equals(player.getName()))
		{
			///Du bist nicht der Ersteller des CustomChannel!
			player.sendMessage(utility.tctlYaml(language+"ChannelGeneral.NotTheCreator"));
			return;
		}
		if(plugin.getProxy().getPlayer(args[1])==null)
		{
			///Der Spieler ist nicht online oder existiert nicht!
			player.sendMessage(utility.tctlYaml(language+"NoPlayerExist"));
			return;
		}
		ProxiedPlayer target = plugin.getProxy().getPlayer(args[1]); 
		if(!cc.getMembers().contains(target))
		{
			///Der angegebene Spieler ist nicht Mitglied im CustomChannel!
			player.sendMessage(utility.tctlYaml(language+"ChannelGeneral.NotChannelMember"));
			return;
		}
		if(target.getName().equals(creator.getName()))
		{
			///Du als Ersteller kannst dich nicht kicken!
			player.sendMessage(utility.tctlYaml(language+"TCKick.CreatorCannotSelfKick"));
			return;
		}
		cc.removeMembers(target);
		///Du wurdest aus dem CustomChannel gekickt!
		target.sendMessage(utility.tctl(plugin.getYamlHandler().getL().getString(language+"TCKick.YouWereKicked")
				.replace("%channel%", cc.getName())));
		///Du hast &f%player% &eaus dem Channel gekickt!
		player.sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+"TCKick.YouKicked")
				.replace("%player%", args[1]).replace("%channel%", cc.getName())));
		for(ProxiedPlayer members : cc.getMembers())
		{
			///Der Spieler &f%player% &ewurde aus dem Channel gekickt!
			members.sendMessage(utility.tctl(
					plugin.getYamlHandler().getL().getString(language+"TCKick.CreatorKickedSomeone")
					.replace("%player%", args[1]).replace("%channel%", cc.getName())));
		}
		return;
	}
}
