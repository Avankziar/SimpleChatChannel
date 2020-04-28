package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.PermanentChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannelBan extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelBan(SimpleChatChannels plugin)
	{
		super("pcban","scc.cmd.pc.ban",SimpleChatChannels.sccarguments,3,3,"pcverbannen",
				"<Channelname>;<Player>".split(";"));
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage() + ".CmdScc.";
		PermanentChannel cc = PermanentChannel.getChannelFromName(args[1]);
		if(cc==null)
		{
			///Du bist in keinem permanenten Channel!
			player.sendMessage(utility.tctl(plugin.getYamlHandler().getL().getString(language+"ChannelGeneral.ChannelNotExistII")
					.replace("%channel%", args[1])));
			return;
		}
		if(!cc.getCreator().equals(player.getUniqueId().toString()) && !cc.getVice().contains(player.getUniqueId().toString()))
		{
			///Du bist weder der Ersteller noch der Stellvertreter im permanenten Channel %channel%!
			player.sendMessage(utility.tctl(
					plugin.getYamlHandler().getL().getString(language+"ChannelGeneral.NotChannelViceII")
					.replace("%channel%", cc.getName())));
			return;
		}
		if(plugin.getMysqlHandler().getDataI(args[2], "player_uuid", "player_name") == null)
		{
			///Der Spieler ist nicht online oder existiert nicht!
			player.sendMessage(utility.tctlYaml(language+"NoPlayerExist"));
			return;
		}
		String targetuuid = (String) plugin.getMysqlHandler().getDataI(args[2], "player_uuid", "player_name");
		if(cc.getCreator().equals(targetuuid) && cc.getVice().contains(player.getUniqueId().toString()))
		{
			///Du als Stellvertreter kannst den Ersteller nicht ban!
			player.sendMessage(utility.tctlYaml(language+"PCBan.ViceCannotBanCreator"));
			return;
		}
		if(targetuuid.equals(player.getUniqueId().toString()))
		{
			///Du als Ersteller kannst dich nicht selber bannen!
			player.sendMessage(utility.tctlYaml(language+"PCBan.YourselfCannotBan"));
			return;
		}
		if(cc.getBanned().contains(targetuuid))
		{
			//Der Spieler ist schon auf der Bannliste!
			player.sendMessage(utility.tctlYaml(language+"PCBan.AlreadyBanned"));
			return;
		}
		cc.addBanned(targetuuid);
		cc.removeVice(targetuuid);
		cc.removeMembers(targetuuid);
		plugin.getUtility().updatePermanentChannels(cc);
		///Du hast den Spieler &f%player% &eaus dem CustomChannel gebannt.
		player.sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+"PCBan.YouHasBanned")
				.replace("%player%", args[2])));
		if(ProxyServer.getInstance().getPlayer(args[2]) != null)
		{
			ProxiedPlayer target = ProxyServer.getInstance().getPlayer(args[2]);
			///Du wurdest vom CustomChannel %channel% gebannt!
			target.sendMessage(utility.tctl(
					plugin.getYamlHandler().getL().getString(language+"PCBan.YourWereBanned")
					.replace("%channel%", cc.getNameColor()+cc.getName())));
		}
		for(ProxiedPlayer members : ProxyServer.getInstance().getPlayers())
		{
			if(cc.getMembers().contains(members.getUniqueId().toString()))
			{
				///Der Spieler &f%player% &ewurde aus dem &5perma&fnenten &eChannel verbannt.
				members.sendMessage(utility.tctl(
						plugin.getYamlHandler().getL().getString(language+"PCBan.CreatorHasBanned")
						.replace("%player%", args[2])));
			}
		}
		return;
	}
}