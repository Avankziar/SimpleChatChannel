package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.Utility;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandModule;

public class ARGMute extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGMute(SimpleChatChannels plugin)
	{
		super("mute","scc.cmd.mute",SimpleChatChannels.sccarguments,1,3,"verstummen");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage() + ".CmdScc.";

		String target = args[1];
		if(Bukkit.getPlayer(target)== null)
		{
			player.spigot().sendMessage(utility.tctl(plugin.getYamlHandler().getL().getString(language+"NoPlayerExist")));
			return;
		}
		Player t = Bukkit.getPlayer(target);
		if(args.length == 2)
		{
			plugin.getMysqlHandler().updateDataI(player, false, "can_chat");
			plugin.getMysqlHandler().updateDataI(player, 0L, "mutetime");
			t.spigot().sendMessage(utility.tctlYaml(language+"Mute.PermaMute"));
		} else if(args.length == 3)
		{
			int num = 0;
			try
			{
				  num = Integer.parseInt(args[2]);
			} catch (NumberFormatException e) 
			{
				  player.spigot().sendMessage(utility.tctl(plugin.getYamlHandler().getL().getString(language+"NoNumber")
						  .replace("%arg%", args[2])));
				  return;
			}
			Long time = 60L*1000;
			Long mutetime = System.currentTimeMillis()+num*time;
			plugin.getMysqlHandler().updateDataI(player, false, "can_chat");
			plugin.getMysqlHandler().updateDataI(player, mutetime, "mutetime");
			t.spigot().sendMessage(utility.tctl(plugin.getYamlHandler().getL().getString(language+"Mute.TempMute")
					.replace("%time%", args[2])));
		} else if(utility.rightArgs(player,args,3))
		{
			return;
		}
	}
}