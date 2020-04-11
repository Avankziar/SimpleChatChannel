package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.spigot.interfaces.CustomChannel;

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
		Player player = (Player) sender;
		String language = plugin.getUtility().getLanguage() + ".CmdScc.";
		CustomChannel cc = CustomChannel.getCustomChannel(player);
		if(cc==null)
		{
			///Du bist in keinem CustomChannel!
			player.spigot().sendMessage(plugin.getUtility().tctlYaml(language+"CustomChannelGeneral.NotInAChannel"));
			return;
		}
		Player creator = cc.getCreator();
		if(!creator.getName().equals(player.getName()))
		{
			///Du bist nicht der Ersteller des CustomChannel!
			player.spigot().sendMessage(plugin.getUtility().tctlYaml(language+"CustomChannelGeneral.NotTheCreator"));
			return;
		}
		if(Bukkit.getPlayer(args[1])!=null)
		{
			///Der angegebene Spieler ist nicht Mitglied im CustomChannel!
			player.spigot().sendMessage(plugin.getUtility().tctlYaml(language+"CustomChannelGeneral.NotChannelMember"));
			return;
		}
		Player target = Bukkit.getPlayer(args[1]);
		if(target.getName().equals(player.getName()))
		{
			///Du als Ersteller kannst dich nicht selber bannen!
			player.spigot().sendMessage(plugin.getUtility().tctlYaml(language+"CCBan.CreatorCannotSelfBan"));
			return;
		}
		if(cc.getBanned().contains(target))
		{
			//Der Spieler ist schon auf der Bannliste!
			player.spigot().sendMessage(plugin.getUtility().tctlYaml(language+"CCBan.AlreadyBanned"));
			return;
		}
		cc.addBanned(target);
		///Du hast den Spieler &f%player% &eaus dem CustomChannel gebannt.
		player.spigot().sendMessage(plugin.getUtility().tctl(
				plugin.getYamlHandler().getL().getString(language+"CCBan.YouHasBanned")
				.replace("%player%", args[1])));
		///Du wurdest vom CustomChannel %channel% gebannt!
		target.spigot().sendMessage(plugin.getUtility().tctl(
				plugin.getYamlHandler().getL().getString(language+"CCBan.YourWereBanned")
				.replace("%channel%", cc.getName())));
		for(Player members : cc.getMembers())
		{
			///Der Spieler &f%player% &ewurde aus dem CustomChannel verbannt.
			members.spigot().sendMessage(plugin.getUtility().tctl(
					plugin.getYamlHandler().getL().getString(language+"CCBan.CreatorHasBanned")
					.replace("%player%", args[1])));
		}
		return;
	}
}
