package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.Utility;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.spigot.interfaces.TemporaryChannel;

public class ARGTemporaryChannelUnban extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGTemporaryChannelUnban(SimpleChatChannels plugin)
	{
		super("tcunban","scc.cmd.tc.unban",SimpleChatChannels.sccarguments,2,2,"tcentbannen");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage() + ".CmdScc.";
		TemporaryChannel cc = TemporaryChannel.getCustomChannel(player);
		if(cc==null)
		{
			///Du bist in keinem CustomChannel!
			player.spigot().sendMessage(utility.tctlYaml(language+"ChannelGeneral.NotInAChannel"));
			return;
		}
		Player creator = cc.getCreator();
		if(!creator.getName().equals(player.getName()))
		{
			///Du bist nicht der Ersteller des CustomChannel!
			player.spigot().sendMessage(utility.tctl(language+"ChannelGeneral.NotTheCreator"));
			return;
		}
		if(plugin.getServer().getPlayer(args[1])==null)
		{
			///Der angegebene Spieler ist nicht Mitglied im CustomChannel!
			player.spigot().sendMessage(utility.tctlYaml(language+"NoPlayerExist"));
			return;
		}
		Player target = plugin.getServer().getPlayer(args[1]); 
		if(!cc.getBanned().contains(target))
		{
			///Der Spieler ist nicht auf der Bannliste!
			player.spigot().sendMessage(utility.tctlYaml(language+"TCUnban.PlayerNotBanned"));
			return;
		}
		cc.removeBanned(target);
		///Du hast &f%player% &efür den CustomChannel entbannt!
		player.spigot().sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+"TCUnban.YouUnbanPlayer")
				.replace("%player%", target.getName())));
		for(Player members : cc.getMembers())
		{
			///Der Spieler &f%player% &ewurde für den CustomChannel entbannt.
			members.spigot().sendMessage(utility.tctl(
					plugin.getYamlHandler().getL().getString(language+"TCUnban.CreatorUnbanPlayer")
					.replace("%player%", target.getName())));
		}
		return;
	}
}