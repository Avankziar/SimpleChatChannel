package main.java.me.avankziar.scc.bungee.commands.scc;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentModule;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGItem_Replacers extends ArgumentModule
{
	private SimpleChatChannels plugin;
	private String cmdName;
	
	public ARGItem_Replacers(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor, String commandName)
	{
		super(argumentConstructor);
		this.plugin = plugin;
		cmdName = commandName;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		plugin.getUtility().sendToSpigot(player, args.length, cmdName, args);
	}
}