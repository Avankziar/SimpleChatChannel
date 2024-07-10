package main.java.me.avankziar.scc.bungee.commands.scc.tc;

import main.java.me.avankziar.scc.bungee.SCC;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.bungee.objects.chat.TemporaryChannel;
import main.java.me.avankziar.scc.general.assistance.ChatApiOld;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
		ProxiedPlayer player = (ProxiedPlayer) sender;
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
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Join.AlreadyInAChannel")));
			return;
		}
		if(cc == null)
		{
			player.sendMessage(ChatApiOld.tctl(
					plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Join.UnknownChannel")
					.replace("%name%", name)));
			return;
		}
		if(cc.getBanned().contains(player))
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Join.Banned")));
			return;
		}
		if(password == null)
		{
			if(cc.getPassword() != null)
			{
				player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Join.ChannelHasPassword")));
				return;
			}
		} else
		{
			if(!cc.getPassword().equals(password))
			{
				player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Join.PasswordIncorrect")));
				return;
			}
		}
		cc.addMembers(player);
		player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Join.ChannelJoined")
				.replace("%channel%", cc.getName())));
		String msg = plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Join.PlayerIsJoined")
				.replace("%player%", player.getName());
		for(ProxiedPlayer members : cc.getMembers())
		{
			members.sendMessage(ChatApiOld.tctl(msg));
		}
	}
}
