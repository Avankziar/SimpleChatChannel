package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.Utility;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandModule;
import net.md_5.bungee.api.chat.TextComponent;

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
		Player player = (Player) sender;
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage();
		String msg = "";
		for (int i = 1; i < args.length; i++) 
        {
			msg += args[i] + " ";
        }
		if(plugin.getUtility().getWordfilter(msg))
		{
			///Einer deiner geschriebenen Woerter &cist im Wortfilter enthalten, &cbitte unterlasse sowelche Ausdrücke!
			player.spigot().sendMessage(plugin.getUtility().tctlYaml(language+".EventChat.Wordfilter"));
			return;
		}
		TextComponent MSG = utility.tc("");
		MSG.setExtra(utility.broadcast(player, 0, "Global", msg, utility.tctlYaml(language+".CmdScc.Broadcast.Intro")));
		for(Player all : Bukkit.getOnlinePlayers())
		{
			all.spigot().sendMessage(MSG);
		}
		return;
	}
}