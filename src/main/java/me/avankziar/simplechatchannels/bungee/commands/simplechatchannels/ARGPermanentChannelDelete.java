package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.assistance.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.bungee.database.MysqlHandler;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.PermanentChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannelDelete extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannelDelete(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String scc = "CmdScc.";
		PermanentChannel cc = PermanentChannel.getChannelFromName(args[1]);
		if(cc==null)
		{
			///Der angegebene Channel &5perma&fnenten %channel% existiert nicht!
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(scc+"ChannelGeneral.ChannelNotExistII")
					.replace("%channel%", args[1])));
			return;
		}
		if(args.length == 2)
		{
			///Der &5perma&fnenten &eChannel %oldchannel% &r&ewurde in %channel% &r&eumbenannt.
			player.sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getL().getString(scc+"PCDelete.Confirm")
					.replace("%channel%", cc.getNameColor()+cc.getName()),
					ClickEvent.Action.SUGGEST_COMMAND, "/scc pcdelete "+cc.getName()+" confirm"));
			return;
		} else if(args.length == 3)
		{
			if(args[2].equalsIgnoreCase("confirm") || args[2].equalsIgnoreCase("bestätigen"))
			{
				if(!cc.getCreator().equals(player.getUniqueId().toString()) 
						&& !player.hasPermission(Utility.PERMBYPASSPCDELETE))
				{
					///Du hast dafür keine Rechte!
					player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(scc+"NoPermission")));
					return;
				}
				for(ProxiedPlayer members : ProxyServer.getInstance().getPlayers())
				{
					if(cc.getMembers().contains(members.getUniqueId().toString()))
					{
						members.sendMessage(ChatApi.tctl(
								plugin.getYamlHandler().getL().getString(scc+"PCDelete.Deleted")
								.replace("%channel%", cc.getNameColor()+cc.getName())
								.replace("%player%", player.getName())));
					}
				}
				plugin.getMysqlHandler().deleteData(MysqlHandler.Type.PERMANENTCHANNEL, "`id` = ?", cc.getId());
				PermanentChannel.removeCustomChannel(cc);
				cc = null;
			} else
			{
				///Deine Eingabe ist fehlerhaft, klicke hier auf den Text um &cweitere Infos zu bekommen!
				player.sendMessage(ChatApi.clickEvent(plugin.getYamlHandler().getL().getString("CmdScc.InputIsWrong"),
						ClickEvent.Action.RUN_COMMAND, "/scc"));
				return;
			}
		}
		return;
	}
}