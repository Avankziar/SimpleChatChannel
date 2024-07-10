package main.java.me.avankziar.scc.spigot.commands.scc;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.objects.ChatUser;
import main.java.me.avankziar.scc.spigot.SCC;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;

public class ARGUnmute extends ArgumentModule
{
	private SCC plugin;
	
	public ARGUnmute(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String target = args[1];
		ChatUser cu = (ChatUser) plugin.getMysqlHandler().getData(MysqlType.CHATUSER,
				"`player_name` = ?", target);
		if(cu == null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		Player t = plugin.getServer().getPlayer(target);
		if(t == null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		cu.setMuteTime(0);
		plugin.getMysqlHandler().updateData(MysqlType.CHATUSER, cu, "`player_uuid` = ?", cu.getUUID());
		if(t != null)
		{
			t.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Mute.YouHaveBeenUnmute")));
		}
		player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Mute.YouHaveUnmute")
				.replace("%player%", t.getName())));
		if(plugin.getYamlHandler().getConfig().getBoolean("Mute.SendGlobal", false))
		{
			String msg = plugin.getYamlHandler().getLang().getString("CmdScc.Mute.PlayerUnmute")
					.replace("%target%", t.getName())
					.replace("%player%", player.getName());
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
