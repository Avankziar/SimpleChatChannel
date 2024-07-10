package main.java.me.avankziar.scc.velocity.commands.scc;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.objects.ChatUser;
import main.java.me.avankziar.scc.velocity.SCC;
import main.java.me.avankziar.scc.velocity.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.velocity.objects.ChatUserHandler;

public class ARGOption_Channel extends ArgumentModule
{
	private SCC plugin;
	
	public ARGOption_Channel(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSource sender, String[] args)
	{
		Player player = (Player) sender;
		ChatUser cu = ChatUserHandler.getChatUser(player.getUniqueId());
		if(cu == null)
		{
			return;
		}
		if(cu.isOptionChannelMessage())
		{
			cu.setOptionChannelMessage(false);
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.Option.Channel.Deactive")));
		} else
		{
			cu.setOptionChannelMessage(true);
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.Option.Channel.Active")));
		}
		plugin.getMysqlHandler().updateData(MysqlType.CHATUSER, cu,
				"`player_uuid` = ?", cu.getUUID());
	}
}
