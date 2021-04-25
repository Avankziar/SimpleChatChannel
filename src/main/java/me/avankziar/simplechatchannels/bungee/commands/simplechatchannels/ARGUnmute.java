package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.bungee.database.MysqlHandler;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.ChatUser;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGUnmute extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGUnmute(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String target = args[1];
		ChatUser cu = (ChatUser) plugin.getMysqlHandler().getData(MysqlHandler.Type.CHATUSER,
				"`player_name` = ?", target);
		if(cu == null)
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		ProxiedPlayer t = ProxyServer.getInstance().getPlayer(target);
		
		cu.setMuteTime(0);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.CHATUSER, cu, "`player_uuid` = ?", cu.getUUID());
		if(t != null)
		{
			t.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Mute.YouHaveBeenUnmute")));
		}
		player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Mute.YouHaveUnmute")
				.replace("%player%", t.getName())));
		if(plugin.getYamlHandler().getConfig().getBoolean("Mute.SendGlobal", false))
		{
			String msg = plugin.getYamlHandler().getLang().getString("CmdScc.Mute.PlayerUnmute")
					.replace("%target%", t.getName())
					.replace("%player%", player.getName());
			for(ProxiedPlayer all : plugin.getProxy().getPlayers())
			{
				if(all.getUniqueId().toString().equals(t.getUniqueId().toString())
						|| all.getUniqueId().toString().equals(player.getUniqueId().toString()))
				{
					continue;
				}
				player.sendMessage(ChatApi.tctl(msg));
			}
		}
	}
}
