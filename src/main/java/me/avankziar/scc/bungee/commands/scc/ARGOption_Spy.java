package main.java.me.avankziar.scc.bungee.commands.scc;

import main.java.me.avankziar.scc.bungee.SCC;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.bungee.objects.ChatUserHandler;
import main.java.me.avankziar.scc.general.assistance.ChatApiOld;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.objects.ChatUser;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
		ProxiedPlayer player = (ProxiedPlayer) sender;
		ChatUser cu = ChatUserHandler.getChatUser(player.getUniqueId());
		if(cu == null)
		{
			return;
		}
		if(cu.isOptionSpy())
		{
			cu.setOptionSpy(false);			
			player.sendMessage(ChatApiOld.tctl(
					plugin.getYamlHandler().getLang().getString("CmdScc.Option.Spy.Deactive")));
		} else
		{
			cu.setOptionSpy(true);
			player.sendMessage(ChatApiOld.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Option.Spy.Active")));
		}
		plugin.getMysqlHandler().updateData(MysqlType.CHATUSER, cu,"`player_uuid` = ?", player.getUniqueId().toString());
	}
}
