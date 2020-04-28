package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.TemporaryChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGTemporaryChannelUnban extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGTemporaryChannelUnban(SimpleChatChannels plugin)
	{
		super("tcunban","scc.cmd.tc.unban",SimpleChatChannels.sccarguments,2,2,"tcentbannen",
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
			player.sendMessage(utility.tctl(language+"ChannelGeneral.NotTheCreator"));
			return;
		}
		if(plugin.getProxy().getPlayer(args[1])==null)
		{
			///Der angegebene Spieler ist nicht Mitglied im CustomChannel!
			player.sendMessage(utility.tctlYaml(language+"NoPlayerExist"));
			return;
		}
		ProxiedPlayer target = plugin.getProxy().getPlayer(args[1]); 
		if(!cc.getBanned().contains(target))
		{
			///Der Spieler ist nicht auf der Bannliste!
			player.sendMessage(utility.tctlYaml(language+"TCUnban.PlayerNotBanned"));
			return;
		}
		cc.removeBanned(target);
		///Du hast &f%player% &efür den CustomChannel entbannt!
		player.sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+"TCUnban.YouUnbanPlayer")
				.replace("%player%", target.getName())));
		for(ProxiedPlayer members : cc.getMembers())
		{
			///Der Spieler &f%player% &ewurde für den CustomChannel entbannt.
			members.sendMessage(utility.tctl(
					plugin.getYamlHandler().getL().getString(language+"TCUnban.CreatorUnbanPlayer")
					.replace("%player%", target.getName())));
		}
		return;
	}
}
