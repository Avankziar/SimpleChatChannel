package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.bungee.objects.chat.TemporaryChannel;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGTemporaryChannel_Unban extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGTemporaryChannel_Unban(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		TemporaryChannel cc = TemporaryChannel.getCustomChannel(player);
		if(cc == null)
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.YouAreNotInAChannel")));
			return;
		}
		ProxiedPlayer creator = cc.getCreator();
		if(!creator.getName().equals(player.getName()))
		{
			player.sendMessage(ChatApi.tctl("CmdScc.TemporaryChannel.YouAreNotTheOwner"));
			return;
		}
		ProxiedPlayer target = plugin.getProxy().getPlayer(args[2]); 
		if(target == null)
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		if(!cc.getBanned().contains(target))
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Ban.PlayerNotBanned")));
			return;
		}
		cc.removeBanned(target);
		player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Ban.YouUnbanPlayer").replace("%player%", target.getName())));
		for(ProxiedPlayer members : cc.getMembers())
		{
			members.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.Ban.CreatorUnbanPlayer").replace("%player%", target.getName())));
		}
		return;
	}
}
