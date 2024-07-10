package main.java.me.avankziar.scc.spigot.commands;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.general.commands.tree.CommandConstructor;
import main.java.me.avankziar.scc.general.objects.StaticValues;
import main.java.me.avankziar.scc.spigot.SCC;
import main.java.me.avankziar.scc.spigot.assistance.Utility;
import main.java.me.avankziar.scc.spigot.handler.ChatHandlerAdventure;
import main.java.me.avankziar.scc.spigot.objects.PluginSettings;

public class WCommandExecutor implements CommandExecutor
{
	private SCC plugin;
	private static CommandConstructor cc;
	
	public WCommandExecutor(SCC plugin, CommandConstructor cc)
	{
		this.plugin = plugin;
		WCommandExecutor.cc = cc;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String lable, String[] args)
	{
		if (sender instanceof Player) 
		{
			SCC.logger.info("/%cmd% is only for Consol!".replace("%cmd%", cc.getName()));
			return false;
		}
		if(args.length <= 1)
		{
			return false;
		}
		String otherPlayer = args[0];
		UUID uuid = Utility.convertNameToUUID(otherPlayer);
		if(uuid == null)
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
			i++;
		}
		if(PluginSettings.settings.isBungee())
		{
			for(Player all : plugin.getServer().getOnlinePlayers())
			{
				if(all != null)
				{
					ByteArrayOutputStream stream = new ByteArrayOutputStream();
			        DataOutputStream out = new DataOutputStream(stream);
			        try {
						out.writeUTF(StaticValues.SCC_TASK_W);
						out.writeUTF(uuid.toString());
						out.writeUTF(message);
					} catch (IOException e) {
						e.printStackTrace();
					}
			        all.sendPluginMessage(SCC.getPlugin(), StaticValues.SCC_TOPROXY, stream.toByteArray());
					break;
				}
			}
			return true;
		} else
		{
			Player other = plugin.getServer().getPlayer(otherPlayer);
			if(other == null)
			{
				return false;
			}
			ChatHandlerAdventure ch = new ChatHandlerAdventure(plugin);
			ch.startPrivateConsoleChat(sender, other, message);
			return true;
		}		
	}
}