package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.CustomChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGCustomChannelChangePassword extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGCustomChannelChangePassword(SimpleChatChannels plugin)
	{
		super("ccchangepassword","scc.cmd.cc.changepassword",SimpleChatChannels.sccarguments,2,2,"ccpasswortändern");
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
		cc.setPassword(args[1]);
		player.sendMessage(plugin.getUtility().tcl(
				plugin.getYamlHandler().getL().getString(language+scc+"changepassword.msg01")
				.replace("%password%", args[1])));
		return;
	}
}
