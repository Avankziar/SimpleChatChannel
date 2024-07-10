package main.java.me.avankziar.scc.velocity.commands.scc.tc;

import java.util.Optional;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.velocity.SCC;
import main.java.me.avankziar.scc.velocity.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.velocity.objects.chat.TemporaryChannel;

public class ARGTemporaryChannel_Ban extends ArgumentModule
{
	private SCC plugin;
	
	public ARGTemporaryChannel_Ban(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);;
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSource sender, String[] args)
	{
		Player player = (Player) sender;
		String other = args[2];
		
		TemporaryChannel cc = TemporaryChannel.getCustomChannel(player);
		if(cc == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.YouAreNotInAChannel")));
			return;
		}
		Player creator = cc.getCreator();
		if(!creator.getUsername().equals(player.getUsername()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.YouAreNotTheOwner")));
			return;
		}
		Optional<Player> target = plugin.getServer().getPlayer(other);
		if(target.isEmpty())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		if(target.get().getUsername().equals(player.getUsername()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Ban.CreatorCannotSelfBan")));
			return;
		}
		if(cc.getBanned().contains(target.get()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Ban.AlreadyBanned")));
			return;
		}
		cc.addBanned(target.get());
		cc.removeMembers(target.get());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Ban.YouHasBanned").replace("%player%", other)));
		target.get().sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Ban.YourWereBanned").replace("%channel%", cc.getName())));
		String msg = plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Ban.CreatorHasBanned").replace("%player%", other);
		for(Player members : cc.getMembers())
		{
			members.sendMessage(ChatApi.tl(msg));
		}
		return;
	}
}
