package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.CustomChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
		ProxiedPlayer player = (ProxiedPlayer) sender;
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage();
		String scc = ".CmdScc.";
		CustomChannel cc = CustomChannel.getCustomChannel(player);
		if(cc==null)
		{
			///Du bist in keinem CustomChannel!
			player.sendMessage(utility.tctlYaml(language+scc+"ChannelGeneral.NotInAChannel"));
			return;
		}
		ProxiedPlayer creator = cc.getCreator();
		if(!creator.getName().equals(player.getName()))
		{
			///Du bist nicht der Ersteller des CustomChannel!
			player.sendMessage(utility.tctlYaml(language+scc+"ChannelGeneral.NotTheCreator"));
			return;
		}
		cc.setPassword(args[1]);
		///Du hast das Passwort zu &f%password% &egeändert!
		player.sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+scc+"TCChangepassword.PasswordChange")
				.replace("%password%", args[1])));
		return;
	}
}
