package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGChannelAuction extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGChannelAuction(SimpleChatChannels plugin)
	{
		super("auction",
				"scc.channels.auction",SimpleChatChannels.sccarguments,1,1,"auktion");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String language = plugin.getUtility().getLanguage();
		plugin.getCommandHelper().channeltoggle(player, args, language, "auction", "Auction");
		return;
	}
}
