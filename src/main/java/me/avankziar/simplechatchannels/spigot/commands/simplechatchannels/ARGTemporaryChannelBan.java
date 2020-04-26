package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.Utility;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.spigot.interfaces.TemporaryChannel;

public class ARGTemporaryChannelBan extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGTemporaryChannelBan(SimpleChatChannels plugin)
	{
		super("tcban","sc.cmd.tc.ban",SimpleChatChannels.sccarguments,2,2,"tcverbannen");
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
			player.spigot().sendMessage(utility.tctlYaml(language+"ChannelGeneral.NotTheCreator"));
			return;
		}
		if(plugin.getServer().getPlayer(args[1])==null)
		{
			///Der angegebene Spieler ist nicht Mitglied im CustomChannel!
			player.spigot().sendMessage(utility.tctlYaml(language+"ChannelGeneral.NoPlayerExist"));
			return;
		}
		Player target = plugin.getServer().getPlayer(args[1]);
		if(target.getName().equals(player.getName()))
		{
			///Du als Ersteller kannst dich nicht selber bannen!
			player.spigot().sendMessage(utility.tctlYaml(language+"TCBan.CreatorCannotSelfBan"));
			return;
		}
		if(cc.getBanned().contains(target))
		{
			//Der Spieler ist schon auf der Bannliste!
			player.spigot().sendMessage(utility.tctlYaml(language+"TCBan.AlreadyBanned"));
			return;
		}
		cc.addBanned(target);
		cc.removeMembers(target);
		///Du hast den Spieler &f%player% &eaus dem CustomChannel gebannt.
		player.spigot().sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+"TCBan.YouHasBanned")
				.replace("%player%", args[1])));
		///Du wurdest vom CustomChannel %channel% gebannt!
		target.spigot().sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+"TCBan.YourWereBanned")
				.replace("%channel%", cc.getName())));
		for(Player members : cc.getMembers())
		{
			///Der Spieler &f%player% &ewurde aus dem CustomChannel verbannt.
			members.spigot().sendMessage(utility.tctl(
					plugin.getYamlHandler().getL().getString(language+"TCBan.CreatorHasBanned")
					.replace("%player%", args[1])));
		}
		return;
	}
}
