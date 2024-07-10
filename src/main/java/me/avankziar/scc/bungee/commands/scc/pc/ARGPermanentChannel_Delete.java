package main.java.me.avankziar.scc.bungee.commands.scc.pc;

import main.java.me.avankziar.scc.bungee.SCC;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.bungee.objects.BypassPermission;
import main.java.me.avankziar.scc.bungee.objects.PluginSettings;
import main.java.me.avankziar.scc.general.assistance.ChatApiOld;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.objects.KeyHandler;
import main.java.me.avankziar.scc.general.objects.PermanentChannel;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGPermanentChannel_Delete extends ArgumentModule
{
	private SCC plugin;
	
	public ARGPermanentChannel_Delete(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String channel = args[2];
		PermanentChannel cc = PermanentChannel.getChannelFromName(channel);
		if(cc == null)
		{
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.YouAreNotInAChannel")
					.replace("%channel%", channel)));
			return;
		}
		if(args.length == 3)
		{
			player.sendMessage(ChatApiOld.click(
					plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Delete.Confirm")
					.replace("%channel%", cc.getNameColor()+cc.getName()),
					"SUGGEST_COMMAND",
					PluginSettings.settings.getCommands(KeyHandler.SCC_PC_DELETE)+cc.getName()+" confirm"));
		} else if(args.length >= 4)
		{
			String confirm = args[3];
			if(confirm.equalsIgnoreCase("confirm") || confirm.equalsIgnoreCase("bestätigen"))
			{
				if(!cc.getCreator().equals(player.getUniqueId().toString()) 
						&& !player.hasPermission(BypassPermission.PERMBYPASSPC))
				{
					player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
					return;
				}
				String msg = plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Delete.Deleted")
						.replace("%channel%", cc.getNameColor()+cc.getName())
						.replace("%player%", player.getName());
				for(ProxiedPlayer members : ProxyServer.getInstance().getPlayers())
				{
					if(cc.getMembers().contains(members.getUniqueId().toString()))
					{
						members.sendMessage(ChatApiOld.tctl(msg));
					}
				}
				plugin.getMysqlHandler().deleteData(MysqlType.PERMANENTCHANNEL, "`id` = ?", cc.getId());
				PermanentChannel.removeCustomChannel(cc);
				cc = null;
			}
		}
		return;
	}
}