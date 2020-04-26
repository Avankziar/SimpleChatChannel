package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.Utility;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.spigot.interfaces.PermanentChannel;

public class ARGPermanentChannelUnban extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelUnban(SimpleChatChannels plugin)
	{
		super("pcunban","scc.cmd.pc.unban",SimpleChatChannels.sccarguments,3,3,"pcentbannen");
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
			///Der angegebene Channel &5perma&fnenten %channel% existiert nicht!
			player.spigot().sendMessage(utility.tctl(plugin.getYamlHandler().getL().getString(language+"ChannelGeneral.ChannelNotExistII")
					.replace("%channel%", args[1])));
			return;
		}
		if(!cc.getCreator().equals(player.getUniqueId().toString())
				&& !cc.getVice().contains(player.getUniqueId().toString()))
		{
			///Du bist weder der Ersteller noch ein Stellvertreter im &5perma&fnenten &cChannel &f%channel%&c!
			player.spigot().sendMessage(utility.tctl(language+"ChannelGeneral.NotChannelViceII"));
			return;
		}
		if(plugin.getMysqlHandler().getDataI(args[2], "player_uuid", "player_name") == null)
		{
			///Der Spieler ist nicht online oder existiert nicht!
			player.spigot().sendMessage(utility.tctlYaml(language+"NoPlayerExist"));
			return;
		}
		String targetuuid = (String) plugin.getMysqlHandler().getDataI(args[2], "player_uuid", "player_name");
		if(!cc.getBanned().contains(targetuuid))
		{
			///Der Spieler ist nicht auf der Bannliste!
			player.spigot().sendMessage(utility.tctlYaml(language+"PCUnban.PlayerNotBanned"));
			return;
		}
		cc.removeBanned(targetuuid);
		plugin.getUtility().updatePermanentChannels(cc);
		///Du hast &f%player% &efür den CustomChannel entbannt!
		player.spigot().sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+"PCUnban.YouUnbanPlayer")
				.replace("%player%", args[2])));
		if(plugin.getServer().getPlayer(args[2]) != null)
		{
			Player target = plugin.getServer().getPlayer(args[2]);
			///Du wurdest vom CustomChannel %channel% gebannt!
			target.spigot().sendMessage(utility.tctl(
					plugin.getYamlHandler().getL().getString(language+"PCUnban.CreatorUnbanPlayer")
					.replace("%player%", args[2])
					.replace("%channel%", cc.getNameColor()+cc.getName())));
		}
		for(Player members : plugin.getServer().getOnlinePlayers())
		{
			if(cc.getMembers().contains(members.getUniqueId().toString()))
			{
				///Der Spieler &f%player% &ewurde für den CustomChannel entbannt.
				members.spigot().sendMessage(utility.tctl(
						plugin.getYamlHandler().getL().getString(language+"PCUnban.CreatorUnbanPlayer")
						.replace("%player%", args[2])
						.replace("%channel%", cc.getNameColor()+cc.getName())));
			}
		}
		return;
	}
}