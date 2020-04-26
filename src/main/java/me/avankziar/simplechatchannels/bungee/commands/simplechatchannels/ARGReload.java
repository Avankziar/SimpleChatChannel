package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGReload extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGReload(SimpleChatChannels plugin)
	{
		super("bungeereload","scc.cmd.reload",SimpleChatChannels.sccarguments,1,1,"bungeeneuladen",null);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String language = plugin.getUtility().getLanguage();
		String scc = ".CmdScc.";
		if(plugin.reload())
		{
			///Yaml Datein wurden neugeladen.
			player.sendMessage(plugin.getUtility().tctlYaml(language+scc+"Reload.Success"));
			return;
		} else
		{
			///Es wurde ein Fehler gefunden! Siehe Konsole!
			player.sendMessage(plugin.getUtility().tctlYaml(language+scc+"Reload.Error"));
			return;
		}
	}
}
