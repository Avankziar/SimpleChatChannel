package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandModule;

public class ARGChannelLocal extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGChannelLocal(SimpleChatChannels plugin)
	{
		super("local",
				"scc.channels.local",SimpleChatChannels.sccarguments,1,1,"lokal");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		plugin.getCommandHelper().channeltoggle(player, "local", "Local");
		return;
	}
}
