package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.PermanentChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannelKick extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelKick(SimpleChatChannels plugin)
	{
		super("pckick","scc.cmd.pc.kick",SimpleChatChannels.sccarguments,2,2,"pcrausschmeißen");
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
		if(target.getUniqueId().toString().equals(player.getUniqueId().toString()))
		{
			///Du als Ersteller kannst dich nicht kicken!
			player.sendMessage(utility.tctlYaml(language+"PCKick.CannotSelfKick"));
			return;
		}
		cc.removeMembers(target.getUniqueId().toString());
		cc.removeVice(target.getUniqueId().toString());
		plugin.getUtility().updatePermanentChannels(cc);
		///Du wurdest aus dem CustomChannel gekickt!
		target.sendMessage(utility.tctl(plugin.getYamlHandler().getL().getString(language+"PCKick.YouWereKicked")
				.replace("%channel%", cc.getName())));
		///Du hast &f%player% &eaus dem Channel gekickt!
		player.sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+"PCKick.YouKicked")
				.replace("%player%", args[1]).replace("%channel%", cc.getName())));
		for(ProxiedPlayer members : ProxyServer.getInstance().getPlayers())
		{
			if(cc.getMembers().contains(members.getUniqueId().toString()))
			{
				///Der Spieler &f%player% &ewurde aus dem Channel gekickt!
				members.sendMessage(utility.tctl(
						plugin.getYamlHandler().getL().getString(language+"PCKick.KickedSomeone")
						.replace("%player%", args[1]).replace("%channel%", cc.getName())));
			}
		}
		return;
	}
}