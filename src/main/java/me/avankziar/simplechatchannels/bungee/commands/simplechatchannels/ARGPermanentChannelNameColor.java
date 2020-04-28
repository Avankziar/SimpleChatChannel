package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.PermanentChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannelNameColor extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelNameColor(SimpleChatChannels plugin)
	{
		super("pcnamecolor","scc.cmd.pc.namecolor",SimpleChatChannels.sccarguments,3,3,"pcnamenfarbe",
				"<Channelname>;<ColorCode>".split(";"));
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage() + ".CmdScc.";
		PermanentChannel cc = PermanentChannel.getChannelFromName(args[1]);
		if(cc==null)
		{
			///Der angegebene Channel &5perma&fnenten %channel% existiert nicht!
			player.sendMessage(utility.tctl(plugin.getYamlHandler().getL().getString(language+"ChannelGeneral.ChannelNotExistII")
					.replace("%channel%", args[1])));
			return;
		}
		if(!cc.getCreator().equals(player.getUniqueId().toString()))
		{
			///Du bist nicht der Ersteller des CustomChannel!
			player.sendMessage(utility.tctlYaml(language+"ChannelGeneral.NotTheCreatorII"));
			return;
		}
		String namecolor = args[2];
		cc.setNameColor(namecolor);
		plugin.getUtility().updatePermanentChannels(cc);
		for(ProxiedPlayer members : ProxyServer.getInstance().getPlayers())
		{
			if(cc.getMembers().contains(members.getUniqueId().toString()))
			{
				members.sendMessage(plugin.getUtility().tctl(
						plugin.getYamlHandler().getL().getString(language+"PCNameColor.NewColor")
						.replace("%color%", namecolor)
						.replace("%channel%", cc.getNameColor()+cc.getName())));
			}
		}
		return;
	}
}