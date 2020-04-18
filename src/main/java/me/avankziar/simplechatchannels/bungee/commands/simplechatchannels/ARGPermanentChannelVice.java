package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.PermanentChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannelVice extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelVice(SimpleChatChannels plugin)
	{
		super("pcvice","scc.cmd.pc.vice",SimpleChatChannels.sccarguments,2,2,"pcstellvertreter");
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
			///Du bist nicht der Ersteller des CustomChannel!
			player.sendMessage(utility.tctlYaml(language+"ChannelGeneral.NotChannelViceII"));
			return;
		}
		if(plugin.getProxy().getPlayer(args[1])==null)
		{
			///Der Spieler ist nicht online oder existiert nicht!
			player.sendMessage(utility.tctlYaml(language+"NoPlayerExist"));
			return;
		}
		ProxiedPlayer target = plugin.getProxy().getPlayer(args[1]); 
		if(!cc.getMembers().contains(target.getUniqueId().toString()))
		{
			///Der angegebene Spieler ist nicht Mitglied im CustomChannel!
			player.sendMessage(utility.tctlYaml(language+"ChannelGeneral.NotChannelMemberII"));
			return;
		}
		if(cc.getVice().contains(target.getUniqueId().toString()))
		{
			cc.getVice().remove(target.getUniqueId().toString());
			plugin.getUtility().updatePermanentChannels(cc);
			for(ProxiedPlayer members : ProxyServer.getInstance().getPlayers())
			{
				if(cc.getMembers().contains(members.getUniqueId().toString()))
				{
					///Der Spieler &f%player% &ewurde zum Mitglied im &5perma&fnenten &eChannel &f%channel% &cdegradiert&e!
					members.sendMessage(utility.tctl(
							plugin.getYamlHandler().getL().getString(language+"PCVice.Degraded")
							.replace("%player%", args[1]).replace("%channel%", cc.getName())));
				}
			}
		} else
		{
			cc.getVice().add(target.getUniqueId().toString());
			plugin.getUtility().updatePermanentChannels(cc);
			for(ProxiedPlayer members : ProxyServer.getInstance().getPlayers())
			{
				if(cc.getMembers().contains(members.getUniqueId().toString()))
				{
					///Der Spieler &f%player% &ewurde zum Stellvertreter im &5perma&fnenten &eChannel &f%channel% &abefördert&e!
					members.sendMessage(utility.tctl(
							plugin.getYamlHandler().getL().getString(language+"PCVice.Promoted")
							.replace("%player%", args[1]).replace("%channel%", cc.getName())));
				}
			}
		}
		return;
	}
}