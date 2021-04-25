package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.bungee.database.MysqlHandler;
import main.java.me.avankziar.simplechatchannels.bungee.objects.ChatUserHandler;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.ChatUser;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGOption_Join extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGOption_Join(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
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
		if(cu.isOptionJoinMessage())
		{
			cu.setOptionJoinMessage(false);
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Option.Join.Deactive")));
		} else
		{
			cu.setOptionJoinMessage(true);
			
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Option.Join.Active")));
		}
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.CHATUSER, cu,
				"`player_uuid` = ?", cu.getUUID());
	}
}