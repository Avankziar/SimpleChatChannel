package main.java.me.avankziar.scc.spigot.commands;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;
import main.java.me.avankziar.scc.spigot.commands.tree.CommandConstructor;
import main.java.me.avankziar.scc.spigot.handler.ChatHandler;

public class RCommandExecutor implements CommandExecutor
{
	private SimpleChatChannels plugin;
	private static CommandConstructor cc;
	
	public RCommandExecutor(SimpleChatChannels plugin, CommandConstructor cc)
	{
		this.plugin = plugin;
		RCommandExecutor.cc = cc;
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
		if(!SimpleChatChannels.rPlayers.containsKey(player.getUniqueId().toString()))
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMsg.YouHaveNoPrivateMessagePartner")));
			return false;
		}
		Player other = plugin.getServer().getPlayer(UUID.fromString(SimpleChatChannels.rPlayers.get(player.getUniqueId().toString())));
		if(other == null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotOnline")));
			return false;
		}
		String message = "";
		int i = 0;
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