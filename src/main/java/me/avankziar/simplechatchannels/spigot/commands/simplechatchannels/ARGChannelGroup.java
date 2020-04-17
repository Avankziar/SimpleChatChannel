package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandModule;

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
		Player player = (Player) sender;
		plugin.getCommandHelper().channeltoggle(player, "group", "Group");
		return;
	}
}
