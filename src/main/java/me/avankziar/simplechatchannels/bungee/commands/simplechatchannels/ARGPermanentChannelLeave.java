package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import java.util.ArrayList;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.CommandModule;
import main.java.me.avankziar.simplechatchannels.bungee.interfaces.PermanentChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannelLeave extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelLeave(SimpleChatChannels plugin)
	{
		super("pcleave","scc.cmd.pc.leave",SimpleChatChannels.sccarguments,1,2,"pcverlassen");
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		Utility utility = plugin.getUtility();
		String language = utility.getLanguage() + ".CmdScc.";
		PermanentChannel cc = PermanentChannel.getChannelFromPlayer(player.getUniqueId().toString());
		if(cc==null)
		{
			///Du bist in keinem CustomChannel!
			player.sendMessage(utility.tctlYaml(language+"ChannelGeneral.NotInAChannelII"));
			return;
		}
		final String name = cc.getName();
		if(args.length==1)
		{
			if(cc.getCreator().equals(player.getUniqueId().toString()))
			{
				player.sendMessage(plugin.getUtility().clickEvent(language+"PCLeave.Confirm",
						ClickEvent.Action.SUGGEST_COMMAND, "/scc pcleave confirm", true));
				return;
			} else
			{
				cc.removeMembers(player.getUniqueId().toString());
				cc.removeVice(player.getUniqueId().toString());
				plugin.getUtility().updatePermanentChannels(cc);
				///Du hast den CustomChannel &f%channel% &everlassen!
				player.sendMessage(utility.tctl(
						plugin.getYamlHandler().getL().getString(language+"PCLeave.YouLeft")
						.replace("%channel%", name)));
				for(ProxiedPlayer members : ProxyServer.getInstance().getPlayers())
				{
					if(cc.getMembers().contains(members.getUniqueId().toString()))
					{
						///Der Spieler &f%player% &chat den &5perma&fnenten &cChannel &f%channel% &cverlassen!
						members.sendMessage(utility.tctl(
								plugin.getYamlHandler().getL().getString(language+"PCLeave.PlayerLeft")
								.replace("%player%", player.getName()).replace("%channel%", cc.getName())));
					}
				}
			}
			return;
		} else if(args.length == 2)
		{
			if(args[1].equalsIgnoreCase("confirm") || args[1].equalsIgnoreCase("bestätigen"))
			{
				if(cc.getCreator().equals(player.getName()))
				{
					final ArrayList<String> oldmembers = cc.getMembers();
					plugin.getMysqlHandler().deleteDataIII(cc.getName());
					PermanentChannel.removeCustomChannel(cc);
					cc = null;
					for(ProxiedPlayer members : ProxyServer.getInstance().getPlayers())
					{
						if(oldmembers.contains(members.getUniqueId().toString()))
						{
							player.sendMessage(utility.tctl(
									plugin.getYamlHandler().getL().getString(language+"PCLeave.YouLeft")
									.replace("%channel%", name)));
						}
					}
				} else
				{
					cc.removeMembers(player.getUniqueId().toString());
					cc.removeVice(player.getUniqueId().toString());
					plugin.getUtility().updatePermanentChannels(cc);
					///Du hast den CustomChannel &f%channel% &everlassen!
					player.sendMessage(utility.tctl(
							plugin.getYamlHandler().getL().getString(language+"PCLeave.YouLeft")
							.replace("%channel%", name)));
					for(ProxiedPlayer members : ProxyServer.getInstance().getPlayers())
					{
						if(cc.getMembers().contains(members.getUniqueId().toString()))
						{
							///Der Spieler &f%player% &chat den &5perma&fnenten &cChannel &f%channel% &cverlassen!
							members.sendMessage(utility.tctl(
									plugin.getYamlHandler().getL().getString(language+"PCLeave.PlayerLeft")
									.replace("%player%", player.getName()).replace("%channel%", cc.getName())));
						}
					}
				}
			} else
			{
				///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
				player.sendMessage(plugin.getUtility().clickEvent(language+".CmdScc.InputIsWrong",
						ClickEvent.Action.RUN_COMMAND, "/scc", true));
			}
			return;
		}
	}
}