package main.java.me.avankziar.scc.velocity.commands.scc.tc;

import java.util.Optional;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.velocity.SCC;
import main.java.me.avankziar.scc.velocity.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.velocity.objects.chat.TemporaryChannel;

public class ARGTemporaryChannel_Unban extends ArgumentModule
{
	private SCC plugin;
	
	public ARGTemporaryChannel_Unban(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSource sender, String[] args)
	{
		Player player = (Player) sender;
		TemporaryChannel cc = TemporaryChannel.getCustomChannel(player);
		if(cc == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.YouAreNotInAChannel")));
			return;
		}
		Player creator = cc.getCreator();
		if(!creator.getUsername().equals(player.getUsername()))
		{
			player.sendMessage(ChatApi.tl("CmdScc.TemporaryChannel.YouAreNotTheOwner"));
			return;
		}
		Optional<Player> target = plugin.getServer().getPlayer(args[2]); 
		if(target.isEmpty())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		if(!cc.getBanned().contains(target.get()))
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Ban.PlayerNotBanned")));
			return;
		}
		cc.removeBanned(target.get());
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Ban.YouUnbanPlayer")
				.replace("%player%", target.get().getUsername())));
		String msg = plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Ban.CreatorUnbanPlayer")
				.replace("%player%", target.get().getUsername());
		for(Player members : cc.getMembers())
		{
			members.sendMessage(ChatApi.tl(msg));
		}
	}
}
