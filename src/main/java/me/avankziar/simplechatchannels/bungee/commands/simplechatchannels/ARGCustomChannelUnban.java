package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.CustomChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGCustomChannelUnban extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGCustomChannelUnban(SimpleChatChannels plugin)
	{
		super("ccunban","scc.cmd.cc.unban",SimpleChatChannels.sccarguments,2,2,"ccentbannen");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String language = plugin.getUtility().getLanguage() + ".CmdScc.";
		CustomChannel cc = CustomChannel.getCustomChannel(player);
		if(cc==null)
		{
			///Du bist in keinem CustomChannel!
			player.sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+"CustomChannelGeneral.NotInAChannel")));
			return;
		}
		ProxiedPlayer creator = cc.getCreator();
		if(!creator.getName().equals(player.getName()))
		{
			///Du bist nicht der Ersteller des CustomChannel!
			player.sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+"CustomChannelGeneral.NotTheCreator")));
			return;
		}
		if(plugin.getProxy().getPlayer(args[1])!=null)
		{
			///Der angegebene Spieler ist nicht Mitglied im CustomChannel!
			player.sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+"CustomChannelGeneral.NotChannelMember")));
			return;
		}
		ProxiedPlayer target = plugin.getProxy().getPlayer(args[1]); 
		if(!cc.getBanned().contains(target))
		{
			///Der Spieler ist nicht auf der Bannliste!
			player.sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+"CCUnban.PlayerNotBanned")));
			return;
		}
		cc.removeBanned(target);
		///Du hast &f%player% &efür den CustomChannel entbannt!
		player.sendMessage(plugin.getUtility().tcl(
				plugin.getYamlHandler().getL().getString(language+"CCUnban.YouUnbanPlayer")
				.replace("%player%", target.getName())));
		for(ProxiedPlayer members : cc.getMembers())
		{
			///Der Spieler &f%player% &ewurde für den CustomChannel entbannt.
			members.sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+"CCUnban.CreatorUnbanPlayer")
					.replace("%player%", target.getName())));
		}
		return;
	}
}
