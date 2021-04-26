package main.java.me.avankziar.scc.spigot.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;
import main.java.me.avankziar.scc.spigot.commands.tree.CommandConstructor;
import main.java.me.avankziar.scc.spigot.handler.ChatHandler;

public class ReCommandExecutor implements CommandExecutor
{
	private SimpleChatChannels plugin;
	private static CommandConstructor cc;
	
	public ReCommandExecutor(SimpleChatChannels plugin, CommandConstructor cc)
	{
		this.plugin = plugin;
		ReCommandExecutor.cc = cc;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args)
	{
		if (!(sender instanceof Player)) 
		{
			SimpleChatChannels.log.info("/%cmd% is only for Player!".replace("%cmd%", cc.getName()));
			return false;
		}
		Player player = (Player) sender;
		if(!player.hasPermission(cc.getPermission()))
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
			return false;
		}
		if(args.length <= 1)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMsg.PleaseEnterAMessage")));
			return false;
		}
		String otherPlayer = args[0];
		Player other = plugin.getServer().getPlayer(otherPlayer);
		if(other == null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotOnline")));
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
		if(ch.prePreCheck(player, message))
		{
			return false;
		}
		ch.startPrivateChat(player, other, message);
		return true;
	}
}