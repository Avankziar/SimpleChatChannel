package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.Utility;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandModule;

public class ARGUnmute extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGUnmute(SimpleChatChannels plugin)
	{
		super("unmute","scc.cmd.unmute",SimpleChatChannels.sccarguments,2,2,"entstummen");
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
			player.spigot().sendMessage(utility.tctl(language+"NoPlayerExist"));
			return;
		}
		Player t = Bukkit.getPlayer(target);
		plugin.getMysqlHandler().updateDataI(player, true, "can_chat");
		plugin.getMysqlHandler().updateDataI(player, 0L, "mutetime");
		t.spigot().sendMessage(utility.tctlYaml(language+"Mute.Unmute"));
		return;
	}
}