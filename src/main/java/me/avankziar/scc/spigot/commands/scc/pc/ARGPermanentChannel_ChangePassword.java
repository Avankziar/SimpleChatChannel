package main.java.me.avankziar.scc.spigot.commands.scc.pc;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.PermanentChannel;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;

public class ARGPermanentChannel_ChangePassword extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannel_ChangePassword(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String channel = args[2];
		PermanentChannel cc = PermanentChannel.getChannelFromName(channel);
		if(cc==null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.YouAreNotInAChannel")
					.replace("%channel%", channel)));
			return;
		}
		if(!cc.getCreator().equals(player.getUniqueId().toString()))
		{
			player.spigot().sendMessage(ChatApi.tctl("CmdScc.PermanentChannel.YouAreNotTheOwnerOrVice"));
			return;
		}
		String pw = args[3];
		cc.setPassword(pw);
		plugin.getUtility().updatePermanentChannels(cc);
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.ChangePassword.Success")
				.replace("%password%", pw)));
	}
}