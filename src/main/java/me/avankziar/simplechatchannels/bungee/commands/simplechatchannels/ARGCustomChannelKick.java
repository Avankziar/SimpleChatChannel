package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.CustomChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGCustomChannelKick extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGCustomChannelKick(SimpleChatChannels plugin)
	{
		super("cckick","scc.cmd.cc.kick",SimpleChatChannels.sccarguments,2,2,"ccrausschmeißen");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage() + ".CmdScc.";
		CustomChannel cc = CustomChannel.getCustomChannel(player);
		if(cc==null)
		{
			///Du bist in keinem CustomChannel!
			player.sendMessage(utility.tctlYaml(language+"CustomChannelGeneral.NotInAChannel"));
			return;
		}
		ProxiedPlayer creator = cc.getCreator();
		if(!creator.getName().equals(player.getName()))
		{
			///Du bist nicht der Ersteller des CustomChannel!
			player.sendMessage(utility.tctlYaml(language+"CustomChannelGeneral.NotTheCreator"));
			return;
		}
		if(plugin.getProxy().getPlayer(args[1])!=null)
		{
			///Der angegebene Spieler ist nicht Mitglied im CustomChannel!
			player.sendMessage(utility.tctlYaml(language+"CustomChannelGeneral.NotChannelMember"));
			return;
		}
		ProxiedPlayer target = plugin.getProxy().getPlayer(args[1]); 
		if(target.getName().equals(creator.getName()))
		{
			///Du als Ersteller kannst dich nicht kicken!
			player.sendMessage(utility.tctlYaml(language+"ChannelKick.CreatorCannotSelfKick"));
			return;
		}
		cc.removeMembers(target);
		///Du wurdest aus dem CustomChannel gekickt!
		target.sendMessage(utility.tctlYaml(language+"ChannelKick.YouWereKicked"));
		///Du hast &f%player% &eaus dem Channel gekickt!
		player.sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+"ChannelKick.YouKicked")
				.replace("%player%", args[1])));
		for(ProxiedPlayer members : cc.getMembers())
		{
			///Der Spieler &f%player% &ewurde aus dem Channel gekickt!
			members.sendMessage(utility.tctl(
					plugin.getYamlHandler().getL().getString(language+"ChannelKick.CreatorKickedSomeone")
					.replace("%player%", args[1])));
		}
		return;
	}
}
