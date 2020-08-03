package main.java.me.avankziar.simplechatchannels.spigot.commands;

import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.CommandConstructor;

public class MessageCommandExecutor implements CommandExecutor
{
	private SimpleChatChannels plugin;
	private static CommandConstructor cc;
	
	public MessageCommandExecutor(SimpleChatChannels plugin, CommandConstructor cc)
	{
		this.plugin = plugin;
		MessageCommandExecutor.cc = cc;
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
		if(cc == null)
		{
			return false;
		}
		if(cc.getPermission() != null)
		{
			if(!player.hasPermission(cc.getPermission()))
			{
				///Du hast dafür keine Rechte!
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("NoPermission")));
				return false;
			}
		}
		if(args.length < 2)
		{
			return false;
		}
		String toPlayer = args[0];
		if(plugin.getServer().getPlayer(toPlayer) == null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("NoPlayerExist")));
			return false;
		}
		String[] argss = Arrays.copyOfRange(args, 1, args.length);
		String message = "@"+toPlayer+" "+String.join(" ", argss);
		plugin.getServer().getPluginManager().callEvent(new AsyncPlayerChatEvent(true, player, message, null));
		return true;
	}

}
