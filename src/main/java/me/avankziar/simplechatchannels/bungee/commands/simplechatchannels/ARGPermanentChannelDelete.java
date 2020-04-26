package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import java.util.ArrayList;
import java.util.Arrays;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.PermanentChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannelDelete extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelDelete(SimpleChatChannels plugin)
	{
		super("pcdelete","scc.cmd.pc.rename",SimpleChatChannels.sccarguments,2,3,"pclöschen",
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
		if(args.length == 2)
		{
			///Der &5perma&fnenten &eChannel %oldchannel% &r&ewurde in %channel% &r&eumbenannt.
			player.sendMessage(plugin.getUtility().clickEvent(
					plugin.getYamlHandler().getL().getString(language+scc+"PCDelete.Confirm")
					.replace("%channel%", cc.getNameColor()+cc.getName()),
					ClickEvent.Action.SUGGEST_COMMAND, "/scc pcdelete "+cc.getName()+" confirm", false));
			return;
		} else if(args.length == 3)
		{
			if(args[2].equalsIgnoreCase("confirm") || args[2].equalsIgnoreCase("bestätigen"))
			{
				if(!cc.getCreator().equals(player.getUniqueId().toString()) 
						&& !player.hasPermission(Utility.PERMBYPASSPCDELETE))
				{
					///Du hast dafür keine Rechte!
					player.sendMessage(plugin.getUtility().tctlYaml(language+scc+"NoPermission"));
					return;
				}
				for(ProxiedPlayer members : ProxyServer.getInstance().getPlayers())
				{
					if(cc.getMembers().contains(members.getUniqueId().toString()))
					{
						members.sendMessage(utility.tctl(
								plugin.getYamlHandler().getL().getString(language+scc+"PCDelete.Deleted")
								.replace("%channel%", cc.getNameColor()+cc.getName())
								.replace("%player%", player.getName())));
					}
				}
				plugin.getMysqlHandler().deleteDataIII(cc.getId());
				PermanentChannel.removeCustomChannel(cc);
				cc = null;
			} else
			{
				///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
				player.sendMessage(plugin.getUtility().clickEvent(language+".CmdScc.InputIsWrong",
						ClickEvent.Action.RUN_COMMAND, "/scc", true));
				return;
			}
		}
		return;
	}
}