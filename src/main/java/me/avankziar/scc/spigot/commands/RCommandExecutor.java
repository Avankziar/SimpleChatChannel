package main.java.me.avankziar.scc.spigot.commands;

import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.CommandConstructor;
import main.java.me.avankziar.scc.spigot.SCC;
import main.java.me.avankziar.scc.spigot.handler.ChatHandlerAdventure;

public class RCommandExecutor implements CommandExecutor
{
	private SCC plugin;
	private static CommandConstructor cc;
	
	public RCommandExecutor(SCC plugin, CommandConstructor cc)
	{
		this.plugin = plugin;
		RCommandExecutor.cc = cc;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args)
	{
		if (!(sender instanceof Player)) 
		{
			SCC.logger.info("/%cmd% is only for Player!".replace("%cmd%", cc.getName()));
			return false;
		}
		Player player = (Player) sender;
		if(!player.hasPermission(cc.getPermission()))
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
			return false;
		}
		if(args.length <= 0)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMsg.PleaseEnterAMessage")));
			return false;
		}
		if(!SCC.rPlayers.containsKey(player.getUniqueId().toString()))
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdMsg.YouHaveNoPrivateMessagePartner")));
			return false;
		}
		Player other = plugin.getServer().getPlayer(UUID.fromString(SCC.rPlayers.get(player.getUniqueId().toString())));
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
			i++;
		}
		ChatHandlerAdventure ch = new ChatHandlerAdventure(plugin);
		if(!ch.prePreCheck(player, message))
		{
			return false;
		}
		ch.startPrivateChat(player, other, message);
		return true;
	}
}