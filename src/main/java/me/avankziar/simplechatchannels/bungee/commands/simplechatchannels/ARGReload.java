package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String scc = "CmdScc.";
		if(plugin.reload())
		{
			///Yaml Datein wurden neugeladen.
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(scc+"Reload.Success")));
			return;
		} else
		{
			///Es wurde ein Fehler gefunden! Siehe Konsole!
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(scc+"Reload.Error")));
			return;
		}
	}
}
