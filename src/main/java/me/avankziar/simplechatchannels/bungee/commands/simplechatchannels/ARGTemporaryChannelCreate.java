package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import java.util.ArrayList;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.CustomChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGTemporaryChannelCreate extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGTemporaryChannelCreate(SimpleChatChannels plugin)
	{
		super("tccreate","scc.cmd.tc.create",SimpleChatChannels.sccarguments,2,3,"tcerschaffen");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String language = plugin.getUtility().getLanguage() + ".CmdScc.";
		CustomChannel cc = CustomChannel.getCustomChannel(player);
		if(cc!=null)
		{
			///Du bist schon in dem Channel %channel%! Um einen neuen Channel zu eröffnen, müsst du den vorigen erst schließen.
			player.sendMessage(plugin.getUtility().tctl(
					plugin.getYamlHandler().getL().getString(language+"TCCreate.AlreadyInAChannel")
					.replace("%channel%", cc.getName())));
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
			///Du hast den Custom Channel &f%channel% &eerstellt!
			player.sendMessage(plugin.getUtility().tctl(
					plugin.getYamlHandler().getL().getString(language+"TCCreate.ChannelCreateWithoutPassword")
					.replace("%channel%", cc.getName())));
		} else
		{
			///Du hast den Custom Channel &f%channel% &emit dem Passwort &f%password% &eerstellt!
			player.sendMessage(plugin.getUtility().tctl(
					plugin.getYamlHandler().getL().getString(language+"TCCreate.ChannelCreateWithPassword")
					.replace("%channel%", cc.getName())
					.replace("%password%", password)));
		}
		return;
	}
}
