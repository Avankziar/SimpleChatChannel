package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGChannelPerma extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGChannelPerma(SimpleChatChannels plugin)
	{
		super("perma",
				"scc.channels.perma",SimpleChatChannels.sccarguments,1,1,null);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		plugin.getCommandHelper().channeltoggle(player, "perma", "Perma");
		return;
	}
}