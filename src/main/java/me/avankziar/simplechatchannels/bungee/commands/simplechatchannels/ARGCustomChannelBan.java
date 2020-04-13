package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.CustomChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGCustomChannelBan extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGCustomChannelBan(SimpleChatChannels plugin)
	{
		super("ccban","scc.cmd.cc.ban",SimpleChatChannels.sccarguments,2,2,"ccverbannen");
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
		if(target.getName().equals(player.getName()))
		{
			///Du als Ersteller kannst dich nicht selber bannen!
			player.sendMessage(utility.tctlYaml(language+"CCBan.CreatorCannotSelfBan"));
			return;
		}
		if(cc.getBanned().contains(target))
		{
			//Der Spieler ist schon auf der Bannliste!
			player.sendMessage(utility.tctlYaml(language+"CCBan.AlreadyBanned"));
			return;
		}
		cc.addBanned(target);
		///Du hast den Spieler &f%player% &eaus dem CustomChannel gebannt.
		player.sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+"CCBan.YouHasBanned")
				.replace("%player%", args[1])));
		///Du wurdest vom CustomChannel %channel% gebannt!
		target.sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+"CCBan.YourWereBanned")
				.replace("%channel%", cc.getName())));
		for(ProxiedPlayer members : cc.getMembers())
		{
			///Der Spieler &f%player% &ewurde aus dem CustomChannel verbannt.
			members.sendMessage(utility.tctl(
					plugin.getYamlHandler().getL().getString(language+"CCBan.CreatorHasBanned")
					.replace("%player%", args[1])));
		}
		return;
	}
}
