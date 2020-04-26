package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.Utility;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.spigot.interfaces.TemporaryChannel;

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
		Player player = (Player) sender;
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage();
		String scc = ".CmdScc.";
		TemporaryChannel cc = TemporaryChannel.getCustomChannel(player);
		if(cc==null)
		{
			///Du bist in keinem CustomChannel!
			player.spigot().sendMessage(utility.tctlYaml(language+scc+"ChannelGeneral.NotInAChannel"));
			return;
		}
		///&e=====&5[&fCustomChannel &6%channel%&5]&e=====
		player.spigot().sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+scc+"TCInfo.Headline")
				.replace("%channel%", cc.getName())));
		///Channel Ersteller: &f%creator%
		player.spigot().sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+scc+"TCInfo.Creator")
				.replace("%creator%", cc.getCreator().getName())));
		///Channel Mitglieder: &f%members%
		player.spigot().sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+scc+"TCInfo.Members")
				.replace("%members%", cc.getMembers().toString())));
		if(cc.getPassword()!=null)
		{
			///Channel Passwort: &f%password%
			player.spigot().sendMessage(utility.tctl(
					plugin.getYamlHandler().getL().getString(language+scc+"TCInfo.Password")
					.replace("%password%", cc.getPassword())));
		}
		///Channel Gebannte Spieler: &f%banned%
		player.spigot().sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+scc+"TCInfo.Banned")
				.replace("%banned%", cc.getBanned().toString())));
		return;
	}
}
