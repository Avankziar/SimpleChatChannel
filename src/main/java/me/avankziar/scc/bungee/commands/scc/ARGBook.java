package main.java.me.avankziar.scc.bungee.commands.scc;

import main.java.me.avankziar.scc.bungee.SCC;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGBook extends ArgumentModule
{
	private SCC plugin;
	private String cmdName;
	
	public ARGBook(SCC plugin, ArgumentConstructor argumentConstructor, String commandName)
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