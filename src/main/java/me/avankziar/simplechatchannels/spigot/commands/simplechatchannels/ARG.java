package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandModule;

public class ARG extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARG(SimpleChatChannels plugin)
	{
		super("arg","perm",SimpleChatChannels.sccarguments,1,1,"aliases");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String language = plugin.getUtility().getLanguage();
		String scc = ".CmdScc.";
	}
}
