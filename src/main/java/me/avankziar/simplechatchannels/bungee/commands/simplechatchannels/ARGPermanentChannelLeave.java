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

public class ARGPermanentChannelLeave extends CommandModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelLeave(SimpleChatChannels plugin)
	{
		super("pcleave","scc.cmd.pc.leave",SimpleChatChannels.sccarguments,2,3,"pcverlassen",
				new ArrayList<String>(Arrays.asList("<Channelname>".split(";"))));
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
		if(args.length==2)
		{
			if(cc.getCreator().equals(player.getUniqueId().toString()))
			{
				player.sendMessage(plugin.getUtility().clickEvent(language+"PCLeave.Confirm",
						ClickEvent.Action.SUGGEST_COMMAND, "/scc pcleave "+args[1]+" confirm", true));
				return;
			} else
			{
				cc.removeMembers(player.getUniqueId().toString());
				cc.removeVice(player.getUniqueId().toString());
				plugin.getUtility().updatePermanentChannels(cc);
				///Du hast den CustomChannel &f%channel% &everlassen!
				player.sendMessage(utility.tctl(
						plugin.getYamlHandler().getL().getString(language+"PCLeave.YouLeft")
						.replace("%channel%", cc.getNameColor()+cc.getName())));
				for(ProxiedPlayer members : ProxyServer.getInstance().getPlayers())
				{
					if(cc.getMembers().contains(members.getUniqueId().toString()))
					{
						///Der Spieler &f%player% &chat den &5perma&fnenten &cChannel &f%channel% &cverlassen!
						members.sendMessage(utility.tctl(
								plugin.getYamlHandler().getL().getString(language+"PCLeave.PlayerLeft")
								.replace("%player%", player.getName()).replace("%channel%", cc.getNameColor()+cc.getName())));
					}
				}
			}
			return;
		} else if(args.length == 3)
		{
			if(args[2].equalsIgnoreCase("confirm") || args[2].equalsIgnoreCase("bestätigen"))
			{
				if(cc.getCreator().equals(player.getUniqueId().toString()))
				{
					for(ProxiedPlayer members : ProxyServer.getInstance().getPlayers())
					{
						if(cc.getMembers().contains(members.getUniqueId().toString()))
						{
							members.sendMessage(utility.tctl(
									plugin.getYamlHandler().getL().getString(language+"PCLeave.CreatorLeft")
									.replace("%channel%", cc.getNameColor()+cc.getName())));
						}
					}
					plugin.getMysqlHandler().deleteDataIII(cc.getId());
					PermanentChannel.removeCustomChannel(cc);
					return;
				} else
				{
					cc.removeMembers(player.getUniqueId().toString());
					cc.removeVice(player.getUniqueId().toString());
					plugin.getUtility().updatePermanentChannels(cc);
					///Du hast den CustomChannel &f%channel% &everlassen!
					player.sendMessage(utility.tctl(
							plugin.getYamlHandler().getL().getString(language+"PCLeave.YouLeft")
							.replace("%channel%", cc.getNameColor()+cc.getName())));
					for(ProxiedPlayer members : ProxyServer.getInstance().getPlayers())
					{
						if(cc.getMembers().contains(members.getUniqueId().toString()))
						{
							///Der Spieler &f%player% &chat den &5perma&fnenten &cChannel &f%channel% &cverlassen!
							members.sendMessage(utility.tctl(
									plugin.getYamlHandler().getL().getString(language+"PCLeave.PlayerLeft")
									.replace("%player%", player.getName()).replace("%channel%", cc.getNameColor()+cc.getName())));
						}
					}
					return;
				}
			} else
			{
				///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
				player.sendMessage(plugin.getUtility().clickEvent(language+".CmdScc.InputIsWrong",
						ClickEvent.Action.RUN_COMMAND, "/scc", true));
				return;
			}
		}
	}
}