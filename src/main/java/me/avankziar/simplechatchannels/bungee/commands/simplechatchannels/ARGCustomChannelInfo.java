package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.CustomChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGCustomChannelInfo extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGCustomChannelInfo(SimpleChatChannels plugin)
	{
		super("ccinfo","scc.cmd.cc.info",SimpleChatChannels.sccarguments,1,1);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String language = plugin.getUtility().getLanguage();
		String scc = ".CMDSCC.";
		CustomChannel cc = CustomChannel.getCustomChannel(player);
		if(cc==null)
		{
			player.sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"ChannelLeave.msg01")));
			return;
		}
		player.sendMessage(plugin.getUtility().tcl(
				plugin.getYamlHandler().getL().getString(language+scc+"ChannelInfo.msg01")
				.replace("%channel%", cc.getName())));
		player.sendMessage(plugin.getUtility().tcl(
				plugin.getYamlHandler().getL().getString(language+scc+"ChannelInfo.msg02")
				.replace("%creator%", cc.getCreator().getName())));
		player.sendMessage(plugin.getUtility().tcl(
				plugin.getYamlHandler().getL().getString(language+scc+"ChannelInfo.msg03")
				.replace("%members%", cc.getMembers().toString())));
		if(cc.getPassword()!=null)
		{
			player.sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"ChannelInfo.msg04")
					.replace("%password%", cc.getPassword())));
		}
		
		player.sendMessage(plugin.getUtility().tcl(
				plugin.getYamlHandler().getL().getString(language+scc+"ChannelInfo.msg05")
				.replace("%banned%", cc.getBanned().toString())));
		return;
	}
}
