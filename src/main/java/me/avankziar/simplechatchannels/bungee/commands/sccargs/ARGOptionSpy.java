package main.java.me.avankziar.simplechatchannels.bungee.commands.sccargs;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGOptionSpy extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGOptionSpy(SimpleChatChannels plugin)
	{
		super("spy",
				"scc.option.spy", SimpleChatChannels.sccarguments,1,1,"spitzeln");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String language = plugin.getUtility().getLanguage();
		plugin.getCommandFactory().optiontoggle(player, args, language, "spy", "spy", "Spy");
		return;
	}
}
