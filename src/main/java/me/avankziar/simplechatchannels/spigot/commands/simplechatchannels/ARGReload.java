package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentModule;

public class ARGReload extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGReload(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String scc = "CmdScc.";
		if(plugin.reload())
		{
			///Yaml Datein wurden neugeladen.
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(scc+"Reload.Success")));
			return;
		} else
		{
			///Es wurde ein Fehler gefunden! Siehe Konsole!
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(scc+"Reload.Error")));
			return;
		}
	}
}