package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGBroadcast extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGBroadcast(SimpleChatChannels plugin)
	{
		super("broadcast","scc.cmd.broadcast",SimpleChatChannels.sccarguments,1,999999999,"ausstrahlung");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage();
		String msg = "";
		for (int i = 1; i < args.length; i++) 
        {
			msg += args[i] + " ";
        }
		if(utility.getWordfilter(msg))
		{
			///Einer deiner geschriebenen Woerter &cist im Wortfilter enthalten, &cbitte unterlasse sowelche Ausdrücke!
			player.sendMessage(utility.tctlYaml(language+".EventChat.Wordfilter"));
			return;
		}
		TextComponent MSG = utility.tc("");
		MSG.setExtra(utility.broadcast(player, 0, "Global", msg, utility.tctlYaml(language+".CmdScc.Broadcast.Intro")));
		for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers())
		{
			all.sendMessage(MSG);
		}
		return;
	}
}
