package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.spigot.objects.TemporaryChannel;

public class ARGTemporaryChannelInfo extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGTemporaryChannelInfo(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String scc = "CmdScc.";
		TemporaryChannel cc = TemporaryChannel.getCustomChannel(player);
		if(cc==null)
		{
			///Du bist in keinem CustomChannel!
			player.spigot().sendMessage(
					ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdScc.TemporaryChannel.YouAreNotInAChannel")));
			return;
		}
		///&e=====&5[&fCustomChannel &6%channel%&5]&e=====
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(scc+"TCInfo.Headline")
				.replace("%channel%", cc.getName())));
		///Channel Ersteller: &f%creator%
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(scc+"TCInfo.Creator")
				.replace("%creator%", cc.getCreator().getName())));
		///Channel Mitglieder: &f%members%
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(scc+"TCInfo.Members")
				.replace("%members%", cc.getMembers().toString())));
		if(cc.getPassword()!=null)
		{
			///Channel Passwort: &f%password%
			player.spigot().sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(scc+"TCInfo.Password")
					.replace("%password%", cc.getPassword())));
		}
		///Channel Gebannte Spieler: &f%banned%
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(scc+"TCInfo.Banned")
				.replace("%banned%", cc.getBanned().toString())));
		return;
	}
}
