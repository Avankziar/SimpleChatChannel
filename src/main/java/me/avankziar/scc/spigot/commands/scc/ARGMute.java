package main.java.me.avankziar.scc.spigot.commands.scc;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.handlers.MatchApi;
import main.java.me.avankziar.scc.general.handlers.TimeHandler;
import main.java.me.avankziar.scc.general.objects.ChatUser;
import main.java.me.avankziar.scc.spigot.SCC;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.spigot.objects.ChatUserHandler;

public class ARGMute extends ArgumentModule
{
	private SCC plugin;
	
	public ARGMute(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String target = args[1];
		Player t = plugin.getServer().getPlayer(target);
		if(t == null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		ChatUser cu = ChatUserHandler.getChatUser(t.getUniqueId());
		if(cu == null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		long time = 0;
		if(args.length == 2)
		{
			time = Long.MAX_VALUE;
			cu.setMuteTime(time);
			plugin.getMysqlHandler().updateData(MysqlType.CHATUSER, cu, "`player_uuid` = ?", cu.getUUID());
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
			plugin.getMysqlHandler().updateData(MysqlType.CHATUSER, cu, "`player_uuid` = ?", cu.getUUID());
		}
		if(t != null)
		{
			t.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Mute.YouHaveBeenMuted")
					.replace("%time%", TimeHandler.getDateTime(time))));
		}
		
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Mute.YouhaveMuteThePlayer")
				.replace("%player%", t.getName())
				.replace("%time%", TimeHandler.getDateTime(time))));
		if(plugin.getYamlHandler().getConfig().getBoolean("Mute.SendGlobal", false))
		{
			String msg = plugin.getYamlHandler().getLang().getString("CmdScc.Mute.PlayerMute")
					.replace("%target%", t.getName())
					.replace("%player%", player.getName())
					.replace("%time%", TimeHandler.getDateTime(time));
			for(Player all : plugin.getServer().getOnlinePlayers())
			{
				if(all.getUniqueId().toString().equals(t.getUniqueId().toString())
						|| all.getUniqueId().toString().equals(player.getUniqueId().toString()))
				{
					continue;
				}
				player.spigot().sendMessage(ChatApi.tctl(msg));
			}
		}
	}
}
