package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGBungee extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGBungee(SimpleChatChannels plugin)
	{
		super("bungee","scc.cmd.bungee",SimpleChatChannels.sccarguments,1,1,null);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		Utility utility = plugin.getUtility();
		if(utility.rightArgs(player,args,1))
		{
			return;
		}
		utility.sendMessage(player.getServer(),"simplechatchannels:sccbungee", "bungeeswitch");
		return;
	}
}
