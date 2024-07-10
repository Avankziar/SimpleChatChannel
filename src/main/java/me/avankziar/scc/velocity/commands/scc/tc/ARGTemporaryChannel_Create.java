package main.java.me.avankziar.scc.velocity.commands.scc.tc;

import java.util.ArrayList;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.velocity.SCC;
import main.java.me.avankziar.scc.velocity.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.velocity.objects.chat.TemporaryChannel;

public class ARGTemporaryChannel_Create extends ArgumentModule
{
	private SCC plugin;
	
	public ARGTemporaryChannel_Create(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSource sender, String[] args)
	{
		Player player = (Player) sender;
		TemporaryChannel cc = TemporaryChannel.getCustomChannel(player);
		if(cc != null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Create.AlreadyInAChannel")
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
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Create.ChannelCreateWithoutPassword")
					.replace("%channel%", cc.getName())));
		} else
		{
			player.sendMessage(ChatApi.tl(
					plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Create.ChannelCreateWithPassword")
					.replace("%channel%", cc.getName())
					.replace("%password%", password)));
		}
	}
}
