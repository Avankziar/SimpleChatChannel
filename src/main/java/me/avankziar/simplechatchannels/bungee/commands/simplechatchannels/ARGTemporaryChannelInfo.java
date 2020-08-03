package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.bungee.objects.TemporaryChannel;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String scc = "CmdScc.";
		TemporaryChannel cc = TemporaryChannel.getCustomChannel(player);
		if(cc==null)
		{
			///Du bist in keinem CustomChannel!
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(scc+"ChannelGeneral.NotInAChannel")));
			return;
		}
		///&e=====&5[&fCustomChannel &6%channel%&5]&e=====
		player.sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(scc+"TCInfo.Headline")
				.replace("%channel%", cc.getName())));
		///Channel Ersteller: &f%creator%
		player.sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(scc+"TCInfo.Creator")
				.replace("%creator%", cc.getCreator().getName())));
		///Channel Mitglieder: &f%members%
		player.sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(scc+"TCInfo.Members")
				.replace("%members%", cc.getMembers().toString())));
		if(cc.getPassword()!=null)
		{
			///Channel Passwort: &f%password%
			player.sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(scc+"TCInfo.Password")
					.replace("%password%", cc.getPassword())));
		}
		///Channel Gebannte Spieler: &f%banned%
		player.sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getL().getString(scc+"TCInfo.Banned")
				.replace("%banned%", cc.getBanned().toString())));
		return;
	}
}
