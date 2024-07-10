package main.java.me.avankziar.scc.velocity.commands.scc.pc;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.objects.KeyHandler;
import main.java.me.avankziar.scc.general.objects.PermanentChannel;
import main.java.me.avankziar.scc.velocity.SCC;
import main.java.me.avankziar.scc.velocity.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.velocity.objects.BypassPermission;
import main.java.me.avankziar.scc.velocity.objects.PluginSettings;

public class ARGPermanentChannel_Delete extends ArgumentModule
{
	private SCC plugin;
	
	public ARGPermanentChannel_Delete(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSource sender, String[] args)
	{
		Player player = (Player) sender;
		String channel = args[2];
		PermanentChannel cc = PermanentChannel.getChannelFromName(channel);
		if(cc == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.YouAreNotInAChannel")
					.replace("%channel%", channel)));
			return;
		}
		if(args.length == 3)
		{
			player.sendMessage(ChatApi.tl(ChatApi.click(
					plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Delete.Confirm")
					.replace("%channel%", cc.getNameColor()+cc.getName()),
					"SUGGEST_COMMAND",
					PluginSettings.settings.getCommands(KeyHandler.SCC_PC_DELETE)+cc.getName()+" confirm")));
		} else if(args.length >= 4)
		{
			String confirm = args[3];
			if(confirm.equalsIgnoreCase("confirm") || confirm.equalsIgnoreCase("bestätigen"))
			{
				if(!cc.getCreator().equals(player.getUniqueId().toString()) 
						&& !player.hasPermission(BypassPermission.PERMBYPASSPC))
				{
					player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("NoPermission")));
					return;
				}
				String msg = plugin.getYamlHandler().getLang().getString("CmdScc.PermanentChannel.Delete.Deleted")
						.replace("%channel%", cc.getNameColor()+cc.getName())
						.replace("%player%", player.getUsername());
				for(Player members : plugin.getServer().getAllPlayers())
				{
					if(cc.getMembers().contains(members.getUniqueId().toString()))
					{
						members.sendMessage(ChatApi.tl(msg));
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