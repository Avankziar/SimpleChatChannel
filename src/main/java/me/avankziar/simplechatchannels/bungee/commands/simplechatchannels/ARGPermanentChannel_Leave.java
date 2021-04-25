package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.bungee.database.MysqlHandler;
import main.java.me.avankziar.simplechatchannels.bungee.objects.KeyHandler;
import main.java.me.avankziar.simplechatchannels.bungee.objects.PluginSettings;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.PermanentChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannel_Leave extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannel_Leave(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String channel = args[2];
		String confirm = "";
		PermanentChannel cc = PermanentChannel.getChannelFromName(channel);
		if(cc == null)
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.YouAreNotInAChannel")
					.replace("%channel%", channel)));
			return;
		}
		if(cc.getCreator().equals(player.getUniqueId().toString()))
		{
			if(args.length == 3)
			{
				player.sendMessage(ChatApi.clickEvent(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Leave.Confirm"),
						ClickEvent.Action.SUGGEST_COMMAND, PluginSettings.settings.getCommands(KeyHandler.SCC_PC_LEAVE)+channel+" confirm"));
				return;
			} else if(args.length == 4)
			{
				confirm = args[3];
				if(confirm.equalsIgnoreCase("confirm") || confirm.equalsIgnoreCase("bestätigen"))
				{
					String msg = plugin.getYamlHandler().getLang().getString("PermanentChannel")
							.replace("%channel%", cc.getNameColor()+cc.getName());
					for(ProxiedPlayer members : ProxyServer.getInstance().getPlayers())
					{
						if(cc.getMembers().contains(members.getUniqueId().toString()))
						{
							members.sendMessage(ChatApi.tctl(msg));
						}
					}
					plugin.getMysqlHandler().deleteData(MysqlHandler.Type.PERMANENTCHANNEL, "`id` = ?", cc.getId());
					PermanentChannel.removeCustomChannel(cc);
					return;
				} else
				{
					player.sendMessage(ChatApi.clickEvent(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Leave.Confirm"),
							ClickEvent.Action.SUGGEST_COMMAND, PluginSettings.settings.getCommands(KeyHandler.SCC_PC_LEAVE)+channel+" confirm"));
					return;
				}
			}
		}
		cc.removeMembers(player.getUniqueId().toString());
		cc.removeVice(player.getUniqueId().toString());
		plugin.getUtility().updatePermanentChannels(cc);
		player.sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Leave.YouLeft")
				.replace("%channel%", cc.getNameColor()+cc.getName())));
		String msg = plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Leave.PlayerLeft")
				.replace("%player%", player.getName()).replace("%channel%", cc.getNameColor()+cc.getName());
		for(ProxiedPlayer members : ProxyServer.getInstance().getPlayers())
		{
			if(cc.getMembers().contains(members.getUniqueId().toString()))
			{
				members.sendMessage(ChatApi.tctl(msg));
			}
		}
	}
}