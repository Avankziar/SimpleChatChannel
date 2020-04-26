package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import java.util.ArrayList;
import java.util.Arrays;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.PermanentChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannelRename extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelRename(SimpleChatChannels plugin)
	{
		super("pcrename","scc.cmd.pc.rename",SimpleChatChannels.sccarguments,3,3,"pcumbenennen",
				new ArrayList<String>(Arrays.asList("<Channelname>".split(";"))));
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage();
		String scc = ".CmdScc.";
		PermanentChannel cc = PermanentChannel.getChannelFromName(args[1]);
		if(cc==null)
		{
			///Der angegebene Channel &5perma&fnenten %channel% existiert nicht!
			player.sendMessage(utility.tctl(plugin.getYamlHandler().getL().getString(language+scc+"ChannelGeneral.ChannelNotExistII")
					.replace("%channel%", args[1])));
			return;
		}
		if(!cc.getCreator().equals(player.getUniqueId().toString()))
		{
			///Du bist nicht der Ersteller des CustomChannel!
			player.sendMessage(utility.tctlYaml(language+"ChannelGeneral.NotTheCreatorII"));
			return;
		}
		PermanentChannel other = PermanentChannel.getChannelFromName(args[2]);
		if(other != null)
		{
			player.sendMessage(plugin.getUtility().tctl(
					plugin.getYamlHandler().getL().getString(language+"PCRename.NameAlreadyExist")
					.replace("%name%", other.getName())
					.replace("%channel%", other.getNameColor()+other.getName())));
			return;
		}
		final String oldchannel = cc.getName();
		cc.setName(args[2]);
		plugin.getUtility().updatePermanentChannels(cc);
		for(ProxiedPlayer members : ProxyServer.getInstance().getPlayers())
		{
			if(cc.getMembers().contains(members.getUniqueId().toString()))
			{
				///Der &5perma&fnenten &eChannel %oldchannel% &r&ewurde in %channel% &r&eumbenannt.
				members.sendMessage(utility.tctl(
						plugin.getYamlHandler().getL().getString(language+scc+"PCRename.Renaming")
						.replace("%channel%", cc.getNameColor()+cc.getName())
						.replace("%oldchannel%", cc.getNameColor()+oldchannel)));
			}
		}
		return;
	}
}