package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.Utility;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.spigot.interfaces.PermanentChannel;

public class ARGPermanentChannelChangePassword extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelChangePassword(SimpleChatChannels plugin)
	{
		super("pcchangepassword","scc.cmd.pc.changepassword",SimpleChatChannels.sccarguments,3,3,"pcpasswortändern");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage();
		String scc = ".CmdScc.";
		PermanentChannel cc = PermanentChannel.getChannelFromName(args[1]);
		if(cc==null)
		{
			///Du bist in keinem permanenten Channel!
			player.spigot().sendMessage(utility.tctl(plugin.getYamlHandler().getL().getString(language+scc+"ChannelGeneral.ChannelNotExistII")
					.replace("%channel%", args[1])));
			return;
		}
		if(!cc.getCreator().equals(player.getUniqueId().toString()))
		{
			///Du bist nicht der Ersteller des permanenten Channel!
			player.spigot().sendMessage(utility.tctlYaml(language+scc+"ChannelGeneral.NotTheCreatorII"));
			return;
		}
		cc.setPassword(args[2]);
		plugin.getUtility().updatePermanentChannels(cc);
		///Du hast das Passwort zu &f%password% &egeändert!
		player.spigot().sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+scc+"PCChangepassword.PasswordChange")
				.replace("%password%", args[2])));
		return;
	}
}