package main.java.me.avankziar.scc.bungee.commands.scc.tc;

import main.java.me.avankziar.scc.bungee.SCC;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.bungee.objects.chat.TemporaryChannel;
import main.java.me.avankziar.scc.general.assistance.ChatApiOld;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGTemporaryChannel_Ban extends ArgumentModule
{
	private SCC plugin;
	
	public ARGTemporaryChannel_Ban(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);;
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
		if(target.getName().equals(player.getName()))
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Ban.CreatorCannotSelfBan")));
			return;
		}
		if(cc.getBanned().contains(target))
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Ban.AlreadyBanned")));
			return;
		}
		cc.addBanned(target);
		cc.removeMembers(target);
		player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Ban.YouHasBanned").replace("%player%", other)));
		target.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Ban.YourWereBanned").replace("%channel%", cc.getName())));
		String msg = plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Ban.CreatorHasBanned").replace("%player%", other);
		for(ProxiedPlayer members : cc.getMembers())
		{
			members.sendMessage(ChatApiOld.tctl(msg));
		}
		return;
	}
}
