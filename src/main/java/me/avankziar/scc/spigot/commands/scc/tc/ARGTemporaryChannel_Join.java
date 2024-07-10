package main.java.me.avankziar.scc.spigot.commands.scc.tc;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.spigot.SCC;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.spigot.objects.TemporaryChannel;

public class ARGTemporaryChannel_Join extends ArgumentModule
{
	private SCC plugin;
	
	public ARGTemporaryChannel_Join(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
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
		TemporaryChannel cc = TemporaryChannel.getCustomChannel(name);
		TemporaryChannel oldcc = TemporaryChannel.getCustomChannel(player);
		if(oldcc != null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Join.AlreadyInAChannel")));
			return;
		}
		if(cc == null)
		{
			player.spigot().sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Join.UnknownChannel")
					.replace("%name%", name)));
			return;
		}
		if(cc.getBanned().contains(player))
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Join.Banned")));
			return;
		}
		if(password == null)
		{
			if(cc.getPassword() != null)
			{
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Join.ChannelHasPassword")));
				return;
			}
		} else
		{
			if(!cc.getPassword().equals(password))
			{
				player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Join.PasswordIncorrect")));
				return;
			}
		}
		cc.addMembers(player);
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Join.ChannelJoined")
				.replace("%channel%", cc.getName())));
		String msg = plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Join.PlayerIsJoined")
				.replace("%player%", player.getName());
		for(Player members : cc.getMembers())
		{
			members.spigot().sendMessage(ChatApi.tctl(msg));
		}
	}
}
