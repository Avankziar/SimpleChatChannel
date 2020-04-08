package main.java.me.avankziar.simplechatchannels.bungee.commands.sccargs;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGChannelTrade extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGChannelTrade(SimpleChatChannels plugin)
	{
		super("trade",
				"scc.channels.trade",SimpleChatChannels.sccarguments,1,1,"handel");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String language = plugin.getUtility().getLanguage();
		plugin.getCommandFactory().channeltoggle(player, args, language, "trade", "Trade");
		return;
	}
}
