package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.Utility;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandModule;

public class ARGIgnoreList extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGIgnoreList(SimpleChatChannels plugin)
	{
		super("ignorelist","scc.cmd.ignorelist",SimpleChatChannels.sccarguments,1,1);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage() + ".CmdScc.";
		if(utility.rightArgs(player,args,1))
		{
			return;
		}
		String list = plugin.getMysqlHandler().getIgnoreList(player, "ignore_name", "player_uuid");
		if(list == null)
		{
			list = "None";
		}
		player.spigot().sendMessage(utility.tctl(plugin.getYamlHandler().getL().getString(language+"Ignore.List")
				.replace("%il%", list)));
		return;
	}
}