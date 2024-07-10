package main.java.me.avankziar.scc.velocity.commands.scc.tc;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.velocity.SCC;
import main.java.me.avankziar.scc.velocity.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.velocity.objects.chat.TemporaryChannel;

public class ARGTemporaryChannel_ChangePassword extends ArgumentModule
{
	private SCC plugin;
	
	public ARGTemporaryChannel_ChangePassword(SCC plugin, ArgumentConstructor argumentConstructor)
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
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.YouAreNotTheOwner")));
			return;
		}
		cc.setPassword(args[2]);
		player.sendMessage(ChatApi.tl(
				plugin.getYamlHandler().getLang().getString("CmdScc.TemporaryChannel.ChangePassword.PasswordChange")
				.replace("%password%", args[2])));
		return;
	}
}
