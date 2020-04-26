package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.Utility;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.spigot.interfaces.PermanentChannel;

public class ARGPermanentChannelBan extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelBan(SimpleChatChannels plugin)
	{
		super("pcban","scc.cmd.pc.ban",SimpleChatChannels.sccarguments,3,3,"pcverbannen");
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
			///Du bist in keinem permanenten Channel!
			player.spigot().sendMessage(utility.tctl(plugin.getYamlHandler().getL().getString(language+"ChannelGeneral.ChannelNotExistII")
					.replace("%channel%", args[1])));
			return;
		}
		if(!cc.getCreator().equals(player.getUniqueId().toString()) && !cc.getVice().contains(player.getUniqueId().toString()))
		{
			///Du bist weder der Ersteller noch der Stellvertreter im permanenten Channel %channel%!
			player.spigot().sendMessage(utility.tctl(
					plugin.getYamlHandler().getL().getString(language+"ChannelGeneral.NotChannelViceII")
					.replace("%channel%", cc.getName())));
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
			///Du als Stellvertreter kannst den Ersteller nicht ban!
			player.spigot().sendMessage(utility.tctlYaml(language+"PCBan.ViceCannotBanCreator"));
			return;
		}
		if(targetuuid.equals(player.getUniqueId().toString()))
		{
			///Du als Ersteller kannst dich nicht selber bannen!
			player.spigot().sendMessage(utility.tctlYaml(language+"PCBan.YourselfCannotBan"));
			return;
		}
		if(cc.getBanned().contains(targetuuid))
		{
			//Der Spieler ist schon auf der Bannliste!
			player.spigot().sendMessage(utility.tctlYaml(language+"PCBan.AlreadyBanned"));
			return;
		}
		cc.addBanned(targetuuid);
		cc.removeVice(targetuuid);
		cc.removeMembers(targetuuid);
		plugin.getUtility().updatePermanentChannels(cc);
		///Du hast den Spieler &f%player% &eaus dem CustomChannel gebannt.
		player.spigot().sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+"PCBan.YouHasBanned")
				.replace("%player%", args[2])));
		if(plugin.getServer().getPlayer(args[2]) != null)
		{
			Player target = plugin.getServer().getPlayer(args[2]);
			///Du wurdest vom CustomChannel %channel% gebannt!
			target.spigot().sendMessage(utility.tctl(
					plugin.getYamlHandler().getL().getString(language+"PCBan.YourWereBanned")
					.replace("%channel%", cc.getNameColor()+cc.getName())));
		}
		for(Player members : plugin.getServer().getOnlinePlayers())
		{
			if(cc.getMembers().contains(members.getUniqueId().toString()))
			{
				///Der Spieler &f%player% &ewurde aus dem &5perma&fnenten &eChannel verbannt.
				members.spigot().sendMessage(utility.tctl(
						plugin.getYamlHandler().getL().getString(language+"PCBan.CreatorHasBanned")
						.replace("%player%", args[2])));
			}
		}
		return;
	}
}