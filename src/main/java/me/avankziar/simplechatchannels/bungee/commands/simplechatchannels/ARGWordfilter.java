package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import java.util.List;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String language = "CmdScc.";
		List<String> wordfilter= plugin.getYamlHandler().get().getStringList("wordfilter");
		String word = args[2];
		if(wordfilter.contains(word))
		{
			wordfilter.remove(word);
			plugin.getYamlHandler().getWord().set("wordfilter", wordfilter);
			plugin.getYamlHandler().saveWordfilter();
			///Das Word %word% wurde aus dem Wortfilter entfernt!
			player.sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(language+"Wordfilter.Removed")
					.replace("%word%", word)));
		} else
		{
			wordfilter.add(word);
			plugin.getYamlHandler().getWord().set("wordfilter", wordfilter);
			plugin.getYamlHandler().saveWordfilter();
			///Das Word %word% wurde dem Wortfilter hinzugefügt!
			player.sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(language+"Wordfilter.Added")
					.replace("%word%", word)));
		}
		return;
	}
}
