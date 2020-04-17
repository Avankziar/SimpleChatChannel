package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandModule;

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
		Player player = (Player) sender;
		plugin.getCommandHelper().optiontoggle(player, "spy", "spy", "Spy");
		return;
	}
}