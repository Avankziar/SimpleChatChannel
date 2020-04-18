package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.PermanentChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannelUnban extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelUnban(SimpleChatChannels plugin)
	{
		super("pcunban","scc.cmd.pc.unban",SimpleChatChannels.sccarguments,2,2,"pcentbannen");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage() + ".CmdScc.";
		PermanentChannel cc = PermanentChannel.getChannelFromPlayer(player.getUniqueId().toString());
		if(cc==null)
		{
			///Du bist in keinem CustomChannel!
			player.sendMessage(utility.tctlYaml(language+"ChannelGeneral.NotInAChannelII"));
			return;
		}
		if(!cc.getCreator().equals(player.getUniqueId().toString())
				&& !cc.getVice().contains(player.getUniqueId().toString()))
		{
			///Du bist weder der Ersteller noch ein Stellvertreter im &5perma&fnenten &cChannel &f%channel%&c!
			player.sendMessage(utility.tctl(language+"ChannelGeneral.NotChannelViceII"));
			return;
		}
		if(plugin.getProxy().getPlayer(args[1])==null)
		{
			///Der angegebene Spieler ist nicht Mitglied im CustomChannel!
			player.sendMessage(utility.tctlYaml(language+"NoPlayerExist"));
			return;
		}
		ProxiedPlayer target = plugin.getProxy().getPlayer(args[1]); 
		if(!cc.getBanned().contains(target.getUniqueId().toString()))
		{
			///Der Spieler ist nicht auf der Bannliste!
			player.sendMessage(utility.tctlYaml(language+"PCUnban.PlayerNotBanned"));
			return;
		}
		cc.removeBanned(target.getUniqueId().toString());
		plugin.getUtility().updatePermanentChannels(cc);
		///Du hast &f%player% &efür den CustomChannel entbannt!
		player.sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+"PCUnban.YouUnbanPlayer")
				.replace("%player%", target.getName())));
		for(ProxiedPlayer members : ProxyServer.getInstance().getPlayers())
		{
			if(cc.getMembers().contains(members.getUniqueId().toString()))
			{
				///Der Spieler &f%player% &ewurde für den CustomChannel entbannt.
				members.sendMessage(utility.tctl(
						plugin.getYamlHandler().getL().getString(language+"PCUnban.CreatorUnbanPlayer")
						.replace("%player%", target.getName())));
			}
		}
		return;
	}
}