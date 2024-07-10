package main.java.me.avankziar.scc.velocity.commands.scc;

import com.velocitypowered.api.command.CommandSource;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.velocity.SCC;
import main.java.me.avankziar.scc.velocity.commands.tree.ArgumentModule;

public class ARGOption extends ArgumentModule
{
	private SCC plugin;
	
	public ARGOption(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSource sender, String[] args)
	{
		sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.OtherCmd")));
	}
}