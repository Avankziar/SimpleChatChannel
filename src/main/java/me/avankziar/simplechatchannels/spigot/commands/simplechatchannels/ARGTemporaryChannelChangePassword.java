package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.Utility;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.spigot.interfaces.TemporaryChannel;

public class ARGTemporaryChannelChangePassword extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGTemporaryChannelChangePassword(SimpleChatChannels plugin)
	{
		super("tcchangepassword","scc.cmd.tc.changepassword",SimpleChatChannels.sccarguments,2,2,"tcpasswortändern");
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
		Player creator = cc.getCreator();
		if(!creator.getName().equals(player.getName()))
		{
			///Du bist nicht der Ersteller des CustomChannel!
			player.spigot().sendMessage(utility.tctlYaml(language+scc+"ChannelGeneral.NotTheCreator"));
			return;
		}
		cc.setPassword(args[1]);
		///Du hast das Passwort zu &f%password% &egeändert!
		player.spigot().sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+scc+"TCChangepassword.PasswordChange")
				.replace("%password%", args[1])));
		return;
	}
}
