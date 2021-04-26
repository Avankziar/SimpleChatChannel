package main.java.me.avankziar.scc.spigot.commands.scc.pc;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.KeyHandler;
import main.java.me.avankziar.scc.objects.PermanentChannel;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.spigot.database.MysqlHandler;
import main.java.me.avankziar.scc.spigot.objects.PluginSettings;
import net.md_5.bungee.api.chat.ClickEvent;
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
		Player player = (Player) sender;
		String channel = args[2];
		String confirm = "";
		PermanentChannel cc = PermanentChannel.getChannelFromName(channel);
		if(cc == null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.YouAreNotInAChannel")
					.replace("%channel%", channel)));
			return;
		}
		if(cc.getCreator().equals(player.getUniqueId().toString()))
		{
			if(args.length == 3)
			{
				player.spigot().sendMessage(ChatApi.clickEvent(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Leave.Confirm"),
						ClickEvent.Action.SUGGEST_COMMAND, PluginSettings.settings.getCommands(KeyHandler.SCC_PC_LEAVE)+channel+" confirm"));
				return;
			} else if(args.length == 4)
			{
				confirm = args[3];
				if(confirm.equalsIgnoreCase("confirm") || confirm.equalsIgnoreCase("bestätigen"))
				{
					String msg = plugin.getYamlHandler().getLang().getString("PermanentChannel")
							.replace("%channel%", cc.getNameColor()+cc.getName());
					for(Player members : plugin.getServer().getOnlinePlayers())
					{
						if(cc.getMembers().contains(members.getUniqueId().toString()))
						{
							members.spigot().sendMessage(ChatApi.tctl(msg));
						}
					}
					plugin.getMysqlHandler().deleteData(MysqlHandler.Type.PERMANENTCHANNEL, "`id` = ?", cc.getId());
					PermanentChannel.removeCustomChannel(cc);
					return;
				} else
				{
					player.spigot().sendMessage(ChatApi.clickEvent(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Leave.Confirm"),
							ClickEvent.Action.SUGGEST_COMMAND, PluginSettings.settings.getCommands(KeyHandler.SCC_PC_LEAVE)+channel+" confirm"));
					return;
				}
			}
		}
		cc.removeMembers(player.getUniqueId().toString());
		cc.removeVice(player.getUniqueId().toString());
		plugin.getUtility().updatePermanentChannels(cc);
		player.spigot().sendMessage(ChatApi.tctl(
				plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Leave.YouLeft")
				.replace("%channel%", cc.getNameColor()+cc.getName())));
		String msg = plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Leave.PlayerLeft")
				.replace("%player%", player.getName()).replace("%channel%", cc.getNameColor()+cc.getName());
		for(Player members : plugin.getServer().getOnlinePlayers())
		{
			if(cc.getMembers().contains(members.getUniqueId().toString()))
			{
				members.spigot().sendMessage(ChatApi.tctl(msg));
			}
		}
	}
}