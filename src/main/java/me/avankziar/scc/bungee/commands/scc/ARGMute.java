package main.java.me.avankziar.scc.bungee.commands.scc;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.bungee.database.MysqlHandler;
import main.java.me.avankziar.scc.bungee.objects.ChatUserHandler;
import main.java.me.avankziar.scc.handlers.MatchApi;
import main.java.me.avankziar.scc.handlers.TimeHandler;
import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.ChatUser;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ARGMute extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGMute(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		ProxiedPlayer player = (ProxiedPlayer) sender;
		String target = args[1];
		ProxiedPlayer t = ProxyServer.getInstance().getPlayer(target);
		if(t == null)
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		ChatUser cu = ChatUserHandler.getChatUser(t.getUniqueId());
		if(cu == null)
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		long time = 0;
		if(args.length == 2)
		{
			time = Long.MAX_VALUE;
			cu.setMuteTime(time);
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.CHATUSER, cu, "`player_uuid` = ?", cu.getUUID());
		} else if(args.length >= 3)
		{
			time = System.currentTimeMillis();
			int i = 2;
			while(i < args.length)
			{
				if(!args[i].contains(":"))
				{
					i++;
					continue;
				}
				String[] arg = args[i].split(":");
				String option = arg[0];
				int num = 0;
				if(!MatchApi.isInteger(arg[1]))
				{
					i++;
					continue;
				}
				num = Integer.parseInt(arg[1]);
				switch(option)
				{
				case "y":
					time += num*1000L*60*60*24*365;
					break;
				case "M":
					time += num*1000L*60*60*24*30;
					break;
				case "d":
					time += num*1000L*60*60*24;
					break;
				case "H":
					time += num*1000L*60*60;
					break;
				case "m":
					time += num*1000L*60;
					break;
				case "s":
					time += num*1000L;
					break;
				default:
					break;
				}
				i++;
			}
			cu.setMuteTime(time);
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.CHATUSER, cu, "`player_uuid` = ?", cu.getUUID());
		}
		if(t != null)
		{
			t.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Mute.YouHaveBeenMuted")
					.replace("%time%", TimeHandler.getDateTime(time))));
		}
		
		player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Mute.YouhaveMuteThePlayer")
				.replace("%player%", t.getName())
				.replace("%time%", TimeHandler.getDateTime(time))));
		if(plugin.getYamlHandler().getConfig().getBoolean("Mute.SendGlobal", false))
		{
			String msg = plugin.getYamlHandler().getLang().getString("CmdScc.Mute.PlayerMute")
					.replace("%target%", t.getName())
					.replace("%player%", player.getName())
					.replace("%time%", TimeHandler.getDateTime(time));
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
