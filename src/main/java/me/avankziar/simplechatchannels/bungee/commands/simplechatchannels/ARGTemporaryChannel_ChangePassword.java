package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.bungee.objects.chat.TemporaryChannel;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGTemporaryChannel_ChangePassword extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGTemporaryChannel_ChangePassword(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
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
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.YouAreNotTheOwner")));
			return;
		}
		cc.setPassword(args[2]);
		player.sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.ChangePassword.PasswordChange")
				.replace("%password%", args[2])));
		return;
	}
}
