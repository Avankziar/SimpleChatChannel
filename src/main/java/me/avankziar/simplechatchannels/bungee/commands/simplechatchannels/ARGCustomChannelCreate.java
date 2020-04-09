package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import java.util.ArrayList;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.CustomChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGCustomChannelCreate extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGCustomChannelCreate(SimpleChatChannels plugin)
	{
		super("cccreate","scc.cmd.cc.create",SimpleChatChannels.sccarguments,2,3,"ccerschaffen");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String language = plugin.getUtility().getLanguage();
		String scc = ".CMDSCC.";
		CustomChannel cc = CustomChannel.getCustomChannel(player);
		if(cc!=null)
		{
			player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+scc+"ChannelCreate.msg01")
					.replace("%channel%", cc.getName()))));
			return;
		}
		String name = null;
		String password = null;
		if(args.length==2)
		{
			name = args[1];
		} else if(args.length==3)
		{
			name = args[1];
			password = args[2];
		}
		ArrayList<ProxiedPlayer> members = new ArrayList<ProxiedPlayer>();
		members.add(player);
		cc = new CustomChannel(name, player, members, password, new ArrayList<ProxiedPlayer>());
		CustomChannel.addCustomChannel(cc);
		if(password==null)
		{
			player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+scc+"ChannelCreate.msg02")
					.replace("%channel%", cc.getName()))));
		} else
		{
			player.sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+scc+"ChannelCreate.msg03")
					.replace("%channel%", cc.getName())
					.replace("%password%", password))));
		}
		return;
	}
}
