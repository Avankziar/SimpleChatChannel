package main.java.me.avankziar.scc.bungee.commands.scc.tc;

import main.java.me.avankziar.scc.bungee.SCC;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.bungee.objects.chat.TemporaryChannel;
import main.java.me.avankziar.scc.general.assistance.ChatApiOld;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGTemporaryChannel_Kick extends ArgumentModule
{
	private SCC plugin;
	
	public ARGTemporaryChannel_Kick(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String other = args[2];
		TemporaryChannel cc = TemporaryChannel.getCustomChannel(player);
		if(cc == null)
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.YouAreNotInAChannel")));
			return;
		}
		ProxiedPlayer creator = cc.getCreator();
		if(!creator.getName().equals(player.getName()))
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.YouAreNotTheOwner")));
			return;
		}
		ProxiedPlayer target = plugin.getProxy().getPlayer(other);
		if(target == null)
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		if(!cc.getMembers().contains(target))
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.NotAChannelMember")));
			return;
		}
		if(target.getName().equals(creator.getName()))
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Kick.CreatorCannotSelfKick")));
			return;
		}
		cc.removeMembers(target);
		target.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Kick.YouWereKicked")
				.replace("%channel%", cc.getName())));
		player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Kick.YouKicked")
				.replace("%player%", other).replace("%channel%", cc.getName())));
		String msg = plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Kick.CreatorKickedSomeone")
				.replace("%player%", other).replace("%channel%", cc.getName());
		for(ProxiedPlayer members : cc.getMembers())
		{
			members.sendMessage(ChatApiOld.tctl(msg));
		}
	}
}
