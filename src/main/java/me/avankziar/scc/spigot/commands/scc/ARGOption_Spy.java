package main.java.me.avankziar.scc.spigot.commands.scc;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.objects.ChatUser;
import main.java.me.avankziar.scc.spigot.SCC;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.spigot.objects.ChatUserHandler;

public class ARGOption_Spy extends ArgumentModule
{
	private SCC plugin;
	
	public ARGOption_Spy(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		ChatUser cu = ChatUserHandler.getChatUser(player.getUniqueId());
		if(cu == null)
		{
			return;
		}
		if(cu.isOptionSpy())
		{
			cu.setOptionSpy(false);			
			player.spigot().sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getLang().getString("CmdScc.Option.Spy.Deactive")));
		} else
		{
			cu.setOptionSpy(true);
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Option.Spy.Active")));
		}
		plugin.getMysqlHandler().updateData(MysqlType.CHATUSER, cu,"`player_uuid` = ?", player.getUniqueId().toString());
	}
}
