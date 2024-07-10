package main.java.me.avankziar.scc.bungee.commands.scc.pc;

import main.java.me.avankziar.scc.bungee.SCC;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.bungee.objects.PluginSettings;
import main.java.me.avankziar.scc.general.assistance.ChatApiOld;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.objects.KeyHandler;
import main.java.me.avankziar.scc.general.objects.PermanentChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannel_Leave extends ArgumentModule
{
	private SCC plugin;
	
	public ARGPermanentChannel_Leave(SCC plugin, ArgumentConstructor argumentConstructor)
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
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.YouAreNotInAChannel")
					.replace("%channel%", channel)));
			return;
		}
		if(cc.getCreator().equals(player.getUniqueId().toString()))
		{
			if(args.length == 3)
			{
				player.sendMessage(ChatApiOld.click(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Leave.Confirm"),
						"SUGGEST_COMMAND", PluginSettings.settings.getCommands(KeyHandler.SCC_PC_LEAVE)+channel+" confirm"));
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
							members.sendMessage(ChatApiOld.tctl(msg));
						}
					}
					plugin.getMysqlHandler().deleteData(MysqlType.PERMANENTCHANNEL, "`id` = ?", cc.getId());
					PermanentChannel.removeCustomChannel(cc);
					return;
				} else
				{
					player.sendMessage(ChatApiOld.click(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Leave.Confirm"),
							"SUGGEST_COMMAND", PluginSettings.settings.getCommands(KeyHandler.SCC_PC_LEAVE)+channel+" confirm"));
					return;
				}
			}
		}
		cc.removeMembers(player.getUniqueId().toString());
		cc.removeVice(player.getUniqueId().toString());
		plugin.getUtility().updatePermanentChannels(cc);
		player.sendMessage(ChatApiOld.tctl(
				plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Leave.YouLeft")
				.replace("%channel%", cc.getNameColor()+cc.getName())));
		String msg = plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Leave.PlayerLeft")
				.replace("%player%", player.getName()).replace("%channel%", cc.getNameColor()+cc.getName());
		for(ProxiedPlayer members : ProxyServer.getInstance().getPlayers())
		{
			if(cc.getMembers().contains(members.getUniqueId().toString()))
			{
				members.sendMessage(ChatApiOld.tctl(msg));
			}
		}
	}
}