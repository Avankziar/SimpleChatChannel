package main.java.me.avankziar.simplechatchannels.bungee.commands;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class CommandExecutorSimpleChatChannel extends Command 
{
	private SimpleChatChannels plugin;
	
	public CommandExecutorSimpleChatChannel(SimpleChatChannels plugin)
	{
		super("scc",null,"simplechatchannel");
		this.plugin = plugin;
	}

	@Override
	public void execute(CommandSender sender, String[] args)
	{
		String language = plugin.getUtility().getLanguage();
		ProxiedPlayer player = (ProxiedPlayer) sender;
		player.sendMessage(plugin.getUtility().tc(this.getName()));
		return;
		// Checks if the label is one of yours.
		/*if (this.getName().equalsIgnoreCase("scc") 
				|| this.getName().equalsIgnoreCase("simplechatchannel")) 
		{
			if (!(player instanceof ProxiedPlayer)) 
			{
				SimpleChatChannels.log.info("/scc is only for Player!");
				return;
			}
			if (args.length == 0) 
			{
				plugin.getCommandFactory().scc(player, language); //Info Command
				return;
			}
			if (SimpleChatChannels.sccarguments.containsKey(args[0])) 
			{
				CommandHandler mod = SimpleChatChannels.sccarguments.get(args[0]);
				if (player.hasPermission(mod.permission)) 
				{
					mod.run(sender, args);
				} else 
				{
					//Du hast dafür keine Rechte!
					player.sendMessage(plugin.getUtility().tcl(
							plugin.getYamlHandler().getL().getString(language+".CMD_SCC.msg02")));
					return;
				}
			} else 
			{
				//Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
				player.sendMessage(plugin.getUtility().runCmdText(
						plugin.getYamlHandler().getL().getString(language+".CMD_SCC.msg01"), "/scc"));
				return;
			}
		}
		return;*/
	}
}

