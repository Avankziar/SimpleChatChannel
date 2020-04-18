package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import java.util.ArrayList;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.spigot.interfaces.TemporaryChannel;


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
		Player player = (Player) sender;
		String language = plugin.getUtility().getLanguage() + ".CmdScc.";
		TemporaryChannel cc = TemporaryChannel.getCustomChannel(player);
		if(cc!=null)
		{
			///Du bist schon in dem Channel %channel%! Um einen neuen Channel zu eröffnen, müsst du den vorigen erst schließen.
			player.spigot().sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+"TCCreate.AlreadyInAChannel")
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
		ArrayList<Player> members = new ArrayList<Player>();
		members.add(player);
		cc = new TemporaryChannel(name, player, members, password, new ArrayList<Player>());
		TemporaryChannel.addCustomChannel(cc);
		if(password==null)
		{
			///Du hast den Custom Channel &f%channel% &eerstellt!
			player.spigot().sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+"TCCreate.ChannelCreateWithoutPassword")
					.replace("%channel%", cc.getName()))));
		} else
		{
			///Du hast den Custom Channel &f%channel% &emit dem Passwort &f%password% &eerstellt!
			player.spigot().sendMessage(plugin.getUtility().tc(plugin.getUtility().tl(
					plugin.getYamlHandler().getL().getString(language+"TCCreate.ChannelCreateWithPassword")
					.replace("%channel%", cc.getName())
					.replace("%password%", password))));
		}
		return;
	}
}