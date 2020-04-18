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
		super("pcban","sc.cmd.pc.ban",SimpleChatChannels.sccarguments,2,2,"pcverbannen");
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
			///Du bist in keinem permanenten Channel!
			player.sendMessage(utility.tctlYaml(language+"ChannelGeneral.NotInAChannelII"));
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
		if(plugin.getProxy().getPlayer(args[1])==null)
		{
			///Der Spieler ist nicht online oder existiert nicht!
			player.sendMessage(utility.tctlYaml(language+"NoPlayerExist"));
			return;
		}
		ProxiedPlayer target = plugin.getProxy().getPlayer(args[1]);
		if(target.getUniqueId().toString().equals(player.getUniqueId().toString()))
		{
			///Du als Ersteller kannst dich nicht selber bannen!
			player.sendMessage(utility.tctlYaml(language+"PCBan.CreatorCannotBan"));
			return;
		}
		if(cc.getBanned().contains(target.getUniqueId().toString()))
		{
			//Der Spieler ist schon auf der Bannliste!
			player.sendMessage(utility.tctlYaml(language+"PCBan.AlreadyBanned"));
			return;
		}
		cc.addBanned(target.getUniqueId().toString());
		cc.removeVice(target.getUniqueId().toString());
		cc.removeMembers(target.getUniqueId().toString());
		plugin.getUtility().updatePermanentChannels(cc);
		///Du hast den Spieler &f%player% &eaus dem CustomChannel gebannt.
		player.sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+"PCBan.YouHasBanned")
				.replace("%player%", args[1])));
		///Du wurdest vom CustomChannel %channel% gebannt!
		target.sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+"PCBan.YourWereBanned")
				.replace("%channel%", cc.getName())));
		for(ProxiedPlayer members : ProxyServer.getInstance().getPlayers())
		{
			if(cc.getMembers().contains(members.getUniqueId().toString()))
			{
				///Der Spieler &f%player% &ewurde aus dem &5perma&fnenten &eChannel verbannt.
				members.sendMessage(utility.tctl(
						plugin.getYamlHandler().getL().getString(language+"PCBan.CreatorHasBanned")
						.replace("%player%", args[1])));
			}
		}
		return;
	}
}