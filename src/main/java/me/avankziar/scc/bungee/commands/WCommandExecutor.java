package main.java.me.avankziar.scc.bungee.commands;

import main.java.me.avankziar.scc.bungee.SCC;
import main.java.me.avankziar.scc.bungee.handler.ChatHandler;
import main.java.me.avankziar.scc.general.commands.tree.CommandConstructor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class WCommandExecutor extends Command
{
	private SCC plugin;
	private static CommandConstructor cc;
	
	public WCommandExecutor(SCC plugin, CommandConstructor cc)
	{
		super(cc.getName(), null);
		this.plugin = plugin;
		WCommandExecutor.cc = cc;
	}

	@Override
	public void execute(CommandSender sender, String[] args)
	{
		if (sender instanceof ProxiedPlayer) 
		{
			SCC.logger.info("/%cmd% is only for Consol!".replace("%cmd%", cc.getName()));
			return;
		}
		if(args.length <= 1)
		{
			return;
		}
		String otherPlayer = args[0];
		ProxiedPlayer other = plugin.getProxy().getPlayer(otherPlayer);
		if(other == null)
		{
			return;
		}
		String message = "";
		int i = 1;
		while(i < args.length)
		{
			message += args[i];
			if(i < (args.length-1))
			{
				message += " ";
			}
		}
		ChatHandler ch = new ChatHandler(plugin);
		ch.startPrivateConsoleChat(sender, other, message);
	}
}