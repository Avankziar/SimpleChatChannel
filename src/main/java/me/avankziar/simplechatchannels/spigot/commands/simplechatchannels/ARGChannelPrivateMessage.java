package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandModule;

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
		Player player = (Player) sender;
		String language = plugin.getUtility().getLanguage();
		plugin.getCommandHelper().channeltoggle(player, args, language, "pm", "Private Message");
		return;
	}
}
