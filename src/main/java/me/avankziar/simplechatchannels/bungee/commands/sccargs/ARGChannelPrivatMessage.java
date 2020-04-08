package main.java.me.avankziar.simplechatchannels.bungee.commands.sccargs;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandHandler;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGChannelPrivatMessage extends CommandHandler
{
	private SimpleChatChannels plugin;
	
	public ARGChannelPrivatMessage(SimpleChatChannels plugin)
	{
		super("pm",
				"scc.channels.pm",SimpleChatChannels.sccarguments,1,1,"pn");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String language = plugin.getUtility().getLanguage();
		plugin.getCommandFactory().channeltoggle(player, args, language, "pm", "Private Message");
		return;
	}
}
