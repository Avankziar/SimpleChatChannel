package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.PermanentChannel;
import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentModule;

public class ARGPermanentChannelChangePassword extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelChangePassword(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String scc = "CmdScc.";
		PermanentChannel cc = PermanentChannel.getChannelFromName(args[1]);
		if(cc==null)
		{
			///Du bist in keinem permanenten Channel!
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdScc.PermanentChannel.YouAreNotInAChannel")
					.replace("%channel%", args[1])));
			return;
		}
		if(!cc.getCreator().equals(player.getUniqueId().toString()))
		{
			///Du bist nicht der Ersteller des permanenten Channel!
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdScc.PermanentChannel.YouAreNotTheOwnerOrVice")));
			return;
		}
		cc.setPassword(args[2]);
		plugin.getUtility().updatePermanentChannels(cc);
		///Du hast das Passwort zu &f%password% &egeändert!
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(scc+"PCChangepassword.PasswordChange")
				.replace("%password%", args[2])));
		return;
	}
}