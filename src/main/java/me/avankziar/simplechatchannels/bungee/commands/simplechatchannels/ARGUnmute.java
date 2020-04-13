package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
		ProxiedPlayer player = (ProxiedPlayer) sender;
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage() + ".CmdScc.";
		String target = args[1];
		if(ProxyServer.getInstance().getPlayer(target)== null)
		{
			player.sendMessage(utility.tctl(language+"NoPlayerExist"));
			return;
		}
		ProxiedPlayer t = ProxyServer.getInstance().getPlayer(target);
		plugin.getMysqlHandler().updateDataI(player, true, "can_chat");
		plugin.getMysqlHandler().updateDataI(player, 0L, "mutetime");
		t.sendMessage(utility.tctlYaml(language+"Mute.Unmute"));
		return;
	}
}
