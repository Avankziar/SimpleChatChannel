package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGIgnoreList extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGIgnoreList(SimpleChatChannels plugin)
	{
		super("ignorelist","scc.cmd.ignorelist",SimpleChatChannels.sccarguments,1,1,null,null);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
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
		player.sendMessage(utility.tctl(plugin.getYamlHandler().getL().getString(language+"Ignore.List")
				.replace("%il%", list)));
		return;
	}
}
