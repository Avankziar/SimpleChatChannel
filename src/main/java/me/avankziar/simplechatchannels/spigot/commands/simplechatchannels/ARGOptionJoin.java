package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandModule;

public class ARGOptionJoin extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGOptionJoin(SimpleChatChannels plugin)
	{
		super("join",
				"scc.option.join", SimpleChatChannels.sccarguments,1,1,"joinmessage","eintrittsnachricht");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		plugin.getCommandHelper().optiontoggle(player, "join", "joinmessage", "Join Message");
		return;
	}
}