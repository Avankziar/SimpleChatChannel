package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.CustomChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGCustomChannelKick extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGCustomChannelKick(SimpleChatChannels plugin)
	{
		super("cckick","scc.cmd.cc.kick",SimpleChatChannels.sccarguments,2,2,"ccrausschmeißen");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String language = plugin.getUtility().getLanguage();
		String scc = ".CMD_SCC.";
		CustomChannel cc = CustomChannel.getCustomChannel(player);
		if(cc==null)
		{
			player.sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"leavechannel.msg01")));
			return;
		}
		ProxiedPlayer creator = cc.getCreator();
		if(!creator.getName().equals(player.getName()))
		{
			player.sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"kickchannel.msg01")));
			return;
		}
		if(plugin.getProxy().getPlayer(args[1])!=null)
		{
			player.sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"kickchannel.msg02")));
			return;
		}
		ProxiedPlayer target = plugin.getProxy().getPlayer(args[1]); 
		if(target.getName().equals(player.getName()))
		{
			player.sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"kickchannel.msg06")));
			return;
		}
		cc.removeMembers(target);
		target.sendMessage(plugin.getUtility().tcl(
				plugin.getYamlHandler().getL().getString(language+scc+"kickchannel.msg03")));
		player.sendMessage(plugin.getUtility().tcl(
				plugin.getYamlHandler().getL().getString(language+scc+"kickchannel.msg04")
				.replace("%player%", args[1])));
		for(ProxiedPlayer members : cc.getMembers())
		{
			members.sendMessage(plugin.getUtility().tcl(
					plugin.getYamlHandler().getL().getString(language+scc+"kickchannel.msg05")
					.replace("%player%", args[1])));
		}
		return;
	}
}
