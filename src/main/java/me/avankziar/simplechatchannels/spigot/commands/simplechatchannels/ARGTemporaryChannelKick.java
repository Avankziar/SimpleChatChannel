package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.Utility;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.spigot.interfaces.CustomChannel;

public class ARGTemporaryChannelKick extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGTemporaryChannelKick(SimpleChatChannels plugin)
	{
		super("tckick","scc.cmd.tc.kick",SimpleChatChannels.sccarguments,2,2,"tcrausschmeißen");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage() + ".CmdScc.";
		CustomChannel cc = CustomChannel.getCustomChannel(player);
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
			player.spigot().sendMessage(utility.tctlYaml(language+"ChannelGeneral.NotTheCreator"));
			return;
		}
		if(Bukkit.getPlayer(args[1])==null)
		{
			///Der angegebene Spieler ist nicht Mitglied im CustomChannel!
			player.spigot().sendMessage(utility.tctlYaml(language+"ChannelGeneral.NoPlayerExist"));
			return;
		}
		Player target = Bukkit.getPlayer(args[1]); 
		if(!cc.getMembers().contains(target))
		{
			///Der angegebene Spieler ist nicht Mitglied im CustomChannel!
			player.spigot().sendMessage(utility.tctlYaml(language+"ChannelGeneral.NotChannelMember"));
			return;
		}
		if(target.getName().equals(creator.getName()))
		{
			///Du als Ersteller kannst dich nicht kicken!
			player.spigot().sendMessage(utility.tctlYaml(language+"TCKick.CreatorCannotSelfKick"));
			return;
		}
		cc.removeMembers(target);
		///Du wurdest aus dem CustomChannel gekickt!
		target.spigot().sendMessage(utility.tctlYaml(plugin.getYamlHandler().getL().getString(language+"TCKick.YouWereKicked")
				.replace("%channel%", cc.getName())));
		///Du hast &f%player% &eaus dem Channel gekickt!
		player.spigot().sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+"TCKick.YouKicked")
				.replace("%player%", args[1]).replace("%channel%", cc.getName())));
		for(Player members : cc.getMembers())
		{
			///Der Spieler &f%player% &ewurde aus dem Channel gekickt!
			members.spigot().sendMessage(utility.tctl(
					plugin.getYamlHandler().getL().getString(language+"TCKick.CreatorKickedSomeone")
					.replace("%player%", args[1]).replace("%channel%", cc.getName())));
		}
		return;
	}
}
