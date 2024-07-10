package main.java.me.avankziar.scc.velocity.commands.scc;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.velocity.SCC;
import main.java.me.avankziar.scc.velocity.commands.tree.ArgumentModule;

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
	public void run(CommandSource sender, String[] args)
	{
		Player player = (Player) sender;
		plugin.getUtility().sendToSpigot(player, args.length, cmdName, args);
	}
}