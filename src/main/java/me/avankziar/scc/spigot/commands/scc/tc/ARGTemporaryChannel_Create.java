package main.java.me.avankziar.scc.spigot.commands.scc.tc;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.spigot.objects.TemporaryChannel;

public class ARGTemporaryChannel_Create extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGTemporaryChannel_Create(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		TemporaryChannel cc = TemporaryChannel.getCustomChannel(player);
		if(cc != null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Create.AlreadyInAChannel")
					.replace("%channel%", cc.getName())));
			return;
		}
		String name = null;
		String password = null;
		if(args.length >= 3)
		{
			name = args[2];
		}
		if(args.length == 4)
		{
			password = args[3];
		}
		ArrayList<Player> members = new ArrayList<Player>();
		members.add(player);
		cc = new TemporaryChannel(name, player, members, password, new ArrayList<Player>());
		TemporaryChannel.addCustomChannel(cc);
		if(password == null)
		{
			player.spigot().sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Create.ChannelCreateWithoutPassword")
					.replace("%channel%", cc.getName())));
		} else
		{
			player.spigot().sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Create.ChannelCreateWithPassword")
					.replace("%channel%", cc.getName())
					.replace("%password%", password)));
		}
	}
}
