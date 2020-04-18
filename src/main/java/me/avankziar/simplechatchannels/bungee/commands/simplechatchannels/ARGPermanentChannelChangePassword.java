package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.PermanentChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannelChangePassword extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelChangePassword(SimpleChatChannels plugin)
	{
		super("pcchangepassword","scc.cmd.pc.changepassword",SimpleChatChannels.sccarguments,2,2,"pcpasswortändern");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage();
		String scc = ".CmdScc.";
		PermanentChannel cc = PermanentChannel.getChannelFromPlayer(player.getUniqueId().toString());
		if(cc==null)
		{
			///Du bist in keinem permanenten Channel!
			player.sendMessage(utility.tctlYaml(language+scc+"ChannelGeneral.NotInAChannelII"));
			return;
		}
		if(!cc.getCreator().equals(player.getUniqueId().toString()))
		{
			///Du bist nicht der Ersteller des permanenten Channel!
			player.sendMessage(utility.tctlYaml(language+scc+"ChannelGeneral.NotTheCreatorII"));
			return;
		}
		cc.setPassword(args[1]);
		plugin.getUtility().updatePermanentChannels(cc);
		///Du hast das Passwort zu &f%password% &egeändert!
		player.sendMessage(utility.tctl(
				plugin.getYamlHandler().getL().getString(language+scc+"PCChangepassword.PasswordChange")
				.replace("%password%", args[1])));
		return;
	}
}