package main.java.me.avankziar.scc.spigot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.spigot.SimpleChatChannels;
import main.java.me.avankziar.scc.spigot.commands.tree.CommandConstructor;
import main.java.me.avankziar.scc.spigot.handler.ChatHandler;

public class WCommandExecutor implements CommandExecutor
{
	private SimpleChatChannels plugin;
	private static CommandConstructor cc;
	
	public WCommandExecutor(SimpleChatChannels plugin, CommandConstructor cc)
	{
		this.plugin = plugin;
		WCommandExecutor.cc = cc;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args)
	{
		if (sender instanceof Player) 
		{
			SimpleChatChannels.log.info("/%cmd% is only for Consol!".replace("%cmd%", cc.getName()));
			return false;
		}
		if(args.length <= 1)
		{
			return false;
		}
		String otherPlayer = args[0];
		Player other = plugin.getServer().getPlayer(otherPlayer);
		if(other == null)
		{
			return false;
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
		return true;
	}
}