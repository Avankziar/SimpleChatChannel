package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentModule;

public class ARGWordfilter extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGWordfilter(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String language = "CmdScc.";
		List<String> wordfilter= plugin.getYamlHandler().getWord().getStringList("wordfilter");
		String word = args[2];
		if(wordfilter.contains(word))
		{
			wordfilter.remove(word);
			plugin.getYamlHandler().getWord().set("wordfilter", wordfilter);
			plugin.getYamlHandler().saveWordfilter();
			///Das Word %word% wurde aus dem Wortfilter entfernt!
			player.spigot().sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(language+"Wordfilter.Removed")
					.replace("%word%", word)));
		} else
		{
			wordfilter.add(word);
			plugin.getYamlHandler().getWord().set("wordfilter", wordfilter);
			plugin.getYamlHandler().saveWordfilter();
			///Das Word %word% wurde dem Wortfilter hinzugefügt!
			player.spigot().sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(language+"Wordfilter.Added")
					.replace("%word%", word)));
		}
		return;
	}
}
