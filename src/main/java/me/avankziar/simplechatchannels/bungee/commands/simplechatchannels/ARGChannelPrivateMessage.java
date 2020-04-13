package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGChannelPrivateMessage extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGChannelPrivateMessage(SimpleChatChannels plugin)
	{
		super("pm",
				"scc.channels.pm",SimpleChatChannels.sccarguments,1,1,"pn");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage();
		plugin.getCommandHelper().channeltoggle(player, args, language, "pm", "Private Message");
		return;
	}
}
