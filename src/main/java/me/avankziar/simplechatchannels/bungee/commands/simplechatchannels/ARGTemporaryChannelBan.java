package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import java.util.ArrayList;
import java.util.Arrays;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.TemporaryChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGTemporaryChannelBan extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGTemporaryChannelBan(SimpleChatChannels plugin)
	{
		super("tcban","sc.cmd.tc.ban",SimpleChatChannels.sccarguments,2,2,"tcverbannen",
				new ArrayList<String>(Arrays.asList("<Player>".split(";"))));
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
			///Der angegebene Spieler ist nicht Mitglied im CustomChannel!
			player.sendMessage(utility.tctlYaml(language+"ChannelGeneral.NoPlayerExist"));
			return;
		}
		ProxiedPlayer target = plugin.getProxy().getPlayer(args[1]);
		if(target.getName().equals(player.getName()))
		{
			///Du als Ersteller kannst dich nicht selber bannen!
			player.sendMessage(utility.tctlYaml(language+"TCBan.CreatorCannotSelfBan"));
			return;
		}
		if(cc.getBanned().contains(target))
		{
			//Der Spieler ist schon auf der Bannliste!
			player.sendMessage(utility.tctlYaml(language+"TCBan.AlreadyBanned"));
			return;
		}
		cc.addBanned(target);
		cc.removeMembers(target);
		///Du hast den Spieler &f%player% &eaus dem CustomChannel gebannt.
		player.sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+"TCBan.YouHasBanned")
				.replace("%player%", args[1])));
		///Du wurdest vom CustomChannel %channel% gebannt!
		target.sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+"TCBan.YourWereBanned")
				.replace("%channel%", cc.getName())));
		for(ProxiedPlayer members : cc.getMembers())
		{
			///Der Spieler &f%player% &ewurde aus dem CustomChannel verbannt.
			members.sendMessage(utility.tctl(
					plugin.getYamlHandler().getL().getString(language+"TCBan.CreatorHasBanned")
					.replace("%player%", args[1])));
		}
		return;
	}
}
