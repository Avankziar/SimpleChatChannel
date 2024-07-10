package main.java.me.avankziar.scc.spigot.commands.scc;

import org.bukkit.command.CommandSender;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.spigot.SCC;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;

public class ARGOption extends ArgumentModule
{
	private SCC plugin;
	
	public ARGOption(SCC plugin, ArgumentConstructor argumentConstructor)
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