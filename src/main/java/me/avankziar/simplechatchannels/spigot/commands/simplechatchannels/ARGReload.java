package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandModule;

public class ARGReload extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGReload(SimpleChatChannels plugin)
	{
		super("reload","scc.cmd.reload",SimpleChatChannels.sccarguments,1,1,"neuladen");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String language = plugin.getUtility().getLanguage();
		String scc = ".CmdScc.";
		plugin.getYamlHandler().mkdir();
		if(plugin.getYamlHandler().loadYamls())
		{
			plugin.getYamlHandler().reload();
			///Yaml Datein wurden neugeladen.
			player.spigot().sendMessage(plugin.getUtility().tctlYaml(language+scc+"Reload.Success"));
			return;
		} else
		{
			///Es wurde ein Fehler gefunden! Siehe Konsole!
			player.spigot().sendMessage(plugin.getUtility().tctlYaml(language+scc+"Reload.Error"));
			return;
		}
	}
}