package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.Utility;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandModule;

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
		Player player = (Player) sender;
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage() + ".CmdScc.";
		List<String> wordfilter= plugin.getYamlHandler().get().getStringList("wordfilter");
		String word = args[2];
		if(wordfilter.contains(word))
		{
			wordfilter.remove(word);
			plugin.getYamlHandler().get().set("wordfilter", wordfilter);
			plugin.getYamlHandler().saveConfig();
			///Das Word %word% wurde aus dem Wortfilter entfernt!
			player.spigot().sendMessage(utility.tctl(
					plugin.getYamlHandler().getL().getString(language+"Wordfilter.Removed")
					.replace("%word%", word)));
		} else
		{
			wordfilter.add(word);
			plugin.getYamlHandler().get().set("wordfilter", wordfilter);
			plugin.getYamlHandler().saveConfig();
			///Das Word %word% wurde dem Wortfilter hinzugefügt!
			player.spigot().sendMessage(utility.tctl(
					plugin.getYamlHandler().getL().getString(language+"Wordfilter.Added")
					.replace("%word%", word)));
		}
		return;
	}
}
