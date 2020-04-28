package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import java.util.List;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGWordfilter extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGWordfilter(SimpleChatChannels plugin)
	{
		super("wordfilter","scc.cmd.wordfilter",SimpleChatChannels.sccarguments,2,2,"wortfilter",
				"<Word>".split(";"));
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
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
			player.sendMessage(utility.tctl(
					plugin.getYamlHandler().getL().getString(language+"Wordfilter.Removed")
					.replace("%word%", word)));
		} else
		{
			wordfilter.add(word);
			plugin.getYamlHandler().get().set("wordfilter", wordfilter);
			plugin.getYamlHandler().saveConfig();
			///Das Word %word% wurde dem Wortfilter hinzugefügt!
			player.sendMessage(utility.tctl(
					plugin.getYamlHandler().getL().getString(language+"Wordfilter.Added")
					.replace("%word%", word)));
		}
		return;
	}
}
