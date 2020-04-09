package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import java.util.List;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGWordfilter extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGWordfilter(SimpleChatChannels plugin)
	{
		super("wordfilter","scc.cmd.wordfilter",SimpleChatChannels.sccarguments,2,2,"wortfilter");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String language = plugin.getUtility().getLanguage();
		String scc = ".CMD_SCC.";
		if(!player.hasPermission("scc.cmd.wordfilter"))
		{
			player.sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"msg02")));
			return;
		}
		if(plugin.getUtility().rightArgs(player,args,3))
		{
			return;
		}
		List<String> wordfilter= plugin.getYamlHandler().get().getStringList("wordfilter");
		String word = args[2];
		if(wordfilter.contains(word))
		{
			wordfilter.remove(word);
			plugin.getYamlHandler().get().set("wordfilter", wordfilter);
			plugin.getYamlHandler().saveConfig();
			player.sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"wordfilter.msg04")
					.replace("%word%", word)));
		} else
		{
			wordfilter.add(word);
			plugin.getYamlHandler().get().set("wordfilter", wordfilter);
			plugin.getYamlHandler().saveConfig();
			player.sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"wordfilter.msg02")
					.replace("%word%", word)));
		}
		return;
	}
}
