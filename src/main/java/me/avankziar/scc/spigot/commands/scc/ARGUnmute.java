package main.java.me.avankziar.scc.spigot.commands.scc;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.scc.objects.ChatApi;
import main.java.me.avankziar.scc.objects.ChatUser;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.spigot.database.MysqlHandler;

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
		Player player = (Player) sender;
		String target = args[1];
		ChatUser cu = (ChatUser) plugin.getMysqlHandler().getData(MysqlHandler.Type.CHATUSER,
				"`player_name` = ?", target);
		if(cu == null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		Player t = plugin.getServer().getPlayer(target);
		
		cu.setMuteTime(0);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.CHATUSER, cu, "`player_uuid` = ?", cu.getUUID());
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
