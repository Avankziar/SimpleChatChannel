package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String language = plugin.getUtility().getLanguage() + ".CmdScc.";

		String target = args[1];
		if(ProxyServer.getInstance().getPlayer(target)== null)
		{
			player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+"NoPlayerExist")));
			return;
		}
		ProxiedPlayer t = ProxyServer.getInstance().getPlayer(target);
		if(args.length == 2)
		{
			plugin.getMysqlHandler().updateDataI(player, false, "can_chat");
			plugin.getMysqlHandler().updateDataI(player, 0L, "mutetime");
			t.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+"Mute.PermaMute")));
		} else if(args.length == 3)
		{
			int num = 0;
			try
			{
				  num = Integer.parseInt(args[2]);
			} catch (NumberFormatException e) 
			{
				  player.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+"NoNumber")
						  .replace("%arg%", args[2])));
				  return;
			}
			Long time = 60L*1000;
			Long mutetime = System.currentTimeMillis()+num*time;
			plugin.getMysqlHandler().updateDataI(player, false, "can_chat");
			plugin.getMysqlHandler().updateDataI(player, mutetime, "mutetime");
			t.sendMessage(plugin.getUtility().tcl(plugin.getYamlHandler().getL().getString(language+"Mute.TempMute")
					.replace("%time%", args[2])));
		} else if(plugin.getUtility().rightArgs(player,args,3))
		{
			return;
		}
	}
}
