package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
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
		super("broadcast","scc.cmd.broadcast",SimpleChatChannels.sccarguments,2,Integer.MAX_VALUE,"ausstrahlung");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String language = plugin.getUtility().getLanguage();
		String scc = ".CMDSCC.";
		String msg = "";
		for (int i = 1; i < args.length; i++) 
        {
			msg += args[i] + " ";
        }
		if(plugin.getUtility().getWordfilter(msg))
		{
			player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+".EventChat.msg06")));
			return;
		}
		TextComponent MSG = plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+scc+"Broadcast.msg20")+" ");
		MSG.setExtra(plugin.getUtility().msgLater(player, 0, "global", msg));
		for(ProxiedPlayer all : ProxyServer.getInstance().getPlayers())
		{
			all.sendMessage(MSG);
		}
		return;
	}
}
