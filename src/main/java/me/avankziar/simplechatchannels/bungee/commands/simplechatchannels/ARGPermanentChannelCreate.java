package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import java.util.ArrayList;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.PermanentChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannelCreate extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelCreate(SimpleChatChannels plugin)
	{
		super("pccreate","scc.cmd.pc.create",SimpleChatChannels.sccarguments,2,3,"pcerschaffen");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String language = plugin.getUtility().getLanguage() + ".CmdScc.";
		PermanentChannel cc = PermanentChannel.getChannelFromPlayer(player.getUniqueId().toString());
		if(cc!=null)
		{
			///Du bist schon in dem permanenten Channel %channel%! 
			///Um einen neuen permanenten Channel zu eröffnen, müsst du den vorigen erst schließen.
			player.sendMessage(plugin.getUtility().tctl(
					plugin.getYamlHandler().getL().getString(language+"PCCreate.AlreadyInAChannel")
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
		ArrayList<String> members = new ArrayList<String>();
		members.add(player.getUniqueId().toString());
		cc = new PermanentChannel(0, name, player.getUniqueId().toString(), new ArrayList<String>(), 
				members, password, new ArrayList<String>());
		plugin.getMysqlHandler().createChannel(cc);
		cc.setId(plugin.getMysqlHandler().getLastIDIII());
		PermanentChannel.addCustomChannel(cc);
		if(password==null)
		{
			///Du hast den permanenten Channel %channel% erstellt!
			player.sendMessage(plugin.getUtility().tctl(
					plugin.getYamlHandler().getL().getString(language+"PCCreate.ChannelCreateWithoutPassword")
					.replace("%channel%", cc.getName())));
		} else
		{
			///Du hast den permanenten Channel %channel% mit dem Passwort %password% erstellt!
			player.sendMessage(plugin.getUtility().tctl(
					plugin.getYamlHandler().getL().getString(language+"PCCreate.ChannelCreateWithPassword")
					.replace("%channel%", cc.getName())
					.replace("%password%", password)));
		}
		return;
	}
}