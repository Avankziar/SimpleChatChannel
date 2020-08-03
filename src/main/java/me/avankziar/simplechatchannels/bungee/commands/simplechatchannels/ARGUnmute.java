package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.ChatUser;
import main.java.me.avankziar.simplechatchannels.bungee.database.MysqlHandler;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGUnmute extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGUnmute(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String language = "CmdScc.";
		String target = args[1];
		if(ProxyServer.getInstance().getPlayer(target)== null)
		{
			player.sendMessage(ChatApi.tctl(language+"NoPlayerExist"));
			return;
		}
		ProxiedPlayer t = ProxyServer.getInstance().getPlayer(target);
		ChatUser cu = (ChatUser) plugin.getMysqlHandler().getData(MysqlHandler.Type.CHATUSER,
				"`player_name` = ?", target);
		if(cu == null)
		{
			///Der Spieler ist nicht online oder existiert nicht!
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"NoPlayerExist")));
			return;
		}
		cu.setCanChat(true);
		cu.setMuteTime(0);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.CHATUSER, cu, "`player_uuid` = ?", cu.getUUID());
		if(t != null)
		{
			ChatUser.addChatUser(cu);
			t.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"Mute.Unmute")));
		}
		player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"Mute.HasUnMute")
				.replace("%player%", t.getName())));
		return;
	}
}
