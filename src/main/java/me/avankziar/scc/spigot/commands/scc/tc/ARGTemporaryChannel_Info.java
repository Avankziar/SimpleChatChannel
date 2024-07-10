package main.java.me.avankziar.scc.spigot.commands.scc.tc;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.spigot.SCC;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.spigot.objects.TemporaryChannel;

public class ARGTemporaryChannel_Info extends ArgumentModule
{
	private SCC plugin;
	
	public ARGTemporaryChannel_Info(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		TemporaryChannel cc = TemporaryChannel.getCustomChannel(player);
		if(cc == null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.YouAreNotInAChannel")));
			return;
		}
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Info.Headline")
				.replace("%channel%", cc.getName())));
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Info.Creator")
				.replace("%creator%", cc.getCreator().getName())));
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Info.Members")
				.replace("%members%", cc.getMembers().toString())));
		if(cc.getPassword() != null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Info.Password")
					.replace("%password%", cc.getPassword())));
		}
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Info.Banned")
				.replace("%banned%", cc.getBanned().toString())));
	}
}
