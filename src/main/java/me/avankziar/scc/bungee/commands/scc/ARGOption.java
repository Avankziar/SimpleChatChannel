package main.java.me.avankziar.scc.bungee.commands.scc;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.objects.ChatApi;
import net.md_5.bungee.api.CommandSender;

public class ARGOption extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGOption(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		sender.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.OtherCmd")));
	}
}