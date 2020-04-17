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
		super("spigotreload","scc.cmd.reload",SimpleChatChannels.sccarguments,1,1,"spigotneuladen");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String language = plugin.getUtility().getLanguage();
		String scc = ".CmdScc.";
		if(plugin.reload())
		{
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