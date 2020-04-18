package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.TemporaryChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGTemporaryChannelInfo extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGTemporaryChannelInfo(SimpleChatChannels plugin)
	{
		super("tcinfo","scc.cmd.tc.info",SimpleChatChannels.sccarguments,1,1);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage();
		String scc = ".CmdScc.";
		TemporaryChannel cc = TemporaryChannel.getCustomChannel(player);
		if(cc==null)
		{
			///Du bist in keinem CustomChannel!
			player.sendMessage(utility.tctlYaml(language+scc+"ChannelGeneral.NotInAChannel"));
			return;
		}
		///&e=====&5[&fCustomChannel &6%channel%&5]&e=====
		player.sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+scc+"TCInfo.Headline")
				.replace("%channel%", cc.getName())));
		///Channel Ersteller: &f%creator%
		player.sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+scc+"TCInfo.Creator")
				.replace("%creator%", cc.getCreator().getName())));
		///Channel Mitglieder: &f%members%
		player.sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+scc+"TCInfo.Members")
				.replace("%members%", cc.getMembers().toString())));
		if(cc.getPassword()!=null)
		{
			///Channel Passwort: &f%password%
			player.sendMessage(utility.tctl(
					plugin.getYamlHandler().getL().getString(language+scc+"TCInfo.Password")
					.replace("%password%", cc.getPassword())));
		}
		///Channel Gebannte Spieler: &f%banned%
		player.sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+scc+"TCInfo.Banned")
				.replace("%banned%", cc.getBanned().toString())));
		return;
	}
}
