package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import java.util.ArrayList;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.bungee.objects.TemporaryChannel;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGTemporaryChannelCreate extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGTemporaryChannelCreate(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String language = "CmdScc.";
		TemporaryChannel cc = TemporaryChannel.getCustomChannel(player);
		if(cc!=null)
		{
			///Du bist schon in dem Channel %channel%! Um einen neuen Channel zu eröffnen, müsst du den vorigen erst schließen.
			player.sendMessage(ChatApi.tctl(
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
		cc = new TemporaryChannel(name, player, members, password, new ArrayList<ProxiedPlayer>());
		TemporaryChannel.addCustomChannel(cc);
		if(password==null)
		{
			///Du hast den Custom Channel &f%channel% &eerstellt!
			player.sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(language+"TCCreate.ChannelCreateWithoutPassword")
					.replace("%channel%", cc.getName())));
		} else
		{
			///Du hast den Custom Channel &f%channel% &emit dem Passwort &f%password% &eerstellt!
			player.sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(language+"TCCreate.ChannelCreateWithPassword")
					.replace("%channel%", cc.getName())
					.replace("%password%", password)));
		}
		return;
	}
}
