package main.java.me.avankziar.simplechatchannels.bungee.commands.sccargs;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandHandler;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGChannelGlobal extends CommandHandler
{
	private SimpleChatChannels plugin;
	
	public ARGChannelGlobal(SimpleChatChannels plugin)
	{
		super("global",
				"scc.channels.global",SimpleChatChannels.sccarguments,1,1);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String language = plugin.getUtility().getLanguage();
		plugin.getCommandFactory().channeltoggle(player, args, language, "global", "Global");
		return;
	}

}
