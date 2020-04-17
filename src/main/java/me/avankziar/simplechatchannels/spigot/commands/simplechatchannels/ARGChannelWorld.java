package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandModule;

public class ARGChannelWorld extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGChannelWorld(SimpleChatChannels plugin)
	{
		super("world",
				"scc.channels.world",SimpleChatChannels.sccarguments,1,1,"welt");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		plugin.getCommandHelper().channeltoggle(player, "world", "World");
		return;
	}
}
