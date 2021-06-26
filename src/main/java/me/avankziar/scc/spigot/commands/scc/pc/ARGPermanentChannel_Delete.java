package main.java.me.avankziar.scc.spigot.commands.scc.pc;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.bungee.objects.PluginSettings;
import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.KeyHandler;
import main.java.me.avankziar.scc.objects.PermanentChannel;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.spigot.database.MysqlHandler;
import main.java.me.avankziar.scc.spigot.objects.BypassPermission;
import net.md_5.bungee.api.chat.ClickEvent;

public class ARGPermanentChannel_Delete extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGPermanentChannel_Delete(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String channel = args[2];
		PermanentChannel cc = PermanentChannel.getChannelFromName(channel);
		if(cc == null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.YouAreNotInAChannel")
					.replace("%channel%", channel)));
			return;
		}
		if(args.length == 3)
		{
			player.spigot().sendMessage(ChatApi.clickEvent(
					plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Delete.Confirm")
					.replace("%channel%", cc.getNameColor()+cc.getName()),
					ClickEvent.Action.SUGGEST_COMMAND,
					PluginSettings.settings.getCommands(KeyHandler.SCC_PC_DELETE)+cc.getName()+" confirm"));
		} else if(args.length >= 4)
		{
			String confirm = args[3];
			if(confirm.equalsIgnoreCase("confirm") || confirm.equalsIgnoreCase("bestätigen"))
			{
				if(!cc.getCreator().equals(player.getUniqueId().toString()) 
						&& !player.hasPermission(BypassPermission.PERMBYPASSPC))
				{
					player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("NoPermission")));
					return;
				}
				String msg = plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Delete.Deleted")
						.replace("%channel%", cc.getNameColor()+cc.getName())
						.replace("%player%", player.getName());
				for(Player members : plugin.getServer().getOnlinePlayers())
				{
					if(cc.getMembers().contains(members.getUniqueId().toString()))
					{
						members.spigot().sendMessage(ChatApi.tctl(msg));
					}
				}
				plugin.getMysqlHandler().deleteData(MysqlHandler.Type.PERMANENTCHANNEL, "`id` = ?", cc.getId());
				PermanentChannel.removeCustomChannel(cc);
				cc = null;
			}
		}
		return;
	}
}