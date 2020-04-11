package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandModule;

public class ARGBungee extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGBungee(SimpleChatChannels plugin)
	{
		super("bunge","scc.cmd.bungee",SimpleChatChannels.sccarguments,1,1);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String language = plugin.getUtility().getLanguage();
		String scc = ".CmdScc.";
		if(plugin.getYamlHandler().get().getBoolean("bungee", false))
		{
			return;
		}
		if(plugin.getYamlHandler().get().getBoolean("bungee", false))
		{
			plugin.getYamlHandler().get().set("bungee", false);
			plugin.getYamlHandler().saveConfig();
			///BungeeCord für SCC wurde deaktiviert!
			player.spigot().sendMessage(plugin.getUtility().tctlYaml(language+scc+"Serverlistener.BungeeOff"));
			return;
		} else if(plugin.getYamlHandler().get().getBoolean("bungee", true))
		{
			plugin.getYamlHandler().get().set("bungee", true);
			plugin.getYamlHandler().saveConfig();
			///BungeeCord für SCC wurde &7aktiviert!
			player.spigot().sendMessage(plugin.getUtility().tctlYaml(language+scc+"Serverlistener.BungeeOn"));
			return;
		}
	}
}