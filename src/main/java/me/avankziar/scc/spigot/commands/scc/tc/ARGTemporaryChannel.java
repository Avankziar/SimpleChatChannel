package main.java.me.avankziar.scc.spigot.commands.scc.tc;

import org.bukkit.command.CommandSender;

import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;

public class ARGTemporaryChannel extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGTemporaryChannel(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		sender.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.OtherCmd")));
	}
}