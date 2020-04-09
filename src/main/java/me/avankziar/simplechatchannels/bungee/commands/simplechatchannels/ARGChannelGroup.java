package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGChannelGroup extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGChannelGroup(SimpleChatChannels plugin)
	{
		super("group",
				"scc.channels.group",SimpleChatChannels.sccarguments,1,1,"gruppe");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String language = plugin.getUtility().getLanguage();
		plugin.getCommandHelper().channeltoggle(player, args, language, "group", "Group");
		return;
	}
}
