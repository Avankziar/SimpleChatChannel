package main.java.me.avankziar.scc.velocity.commands.scc;

import java.util.Optional;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.objects.ChatUser;
import main.java.me.avankziar.scc.velocity.SCC;
import main.java.me.avankziar.scc.velocity.commands.tree.ArgumentModule;

public class ARGUnmute extends ArgumentModule
{
	private SCC plugin;
	
	public ARGUnmute(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSource sender, String[] args)
	{
		Player player = (Player) sender;
		String target = args[1];
		ChatUser cu = (ChatUser) plugin.getMysqlHandler().getData(MysqlType.CHATUSER,
				"`player_name` = ?", target);
		if(cu == null)
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		Optional<Player> t = plugin.getServer().getPlayer(target);
		if(t.isEmpty())
		{
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("PlayerNotExist")));
			return;
		}
		cu.setMuteTime(0);
		plugin.getMysqlHandler().updateData(MysqlType.CHATUSER, cu, "`player_uuid` = ?", cu.getUUID());
		if(t != null)
		{
			t.get().sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.Mute.YouHaveBeenUnmute")));
		}
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.Mute.YouHaveUnmute")
				.replace("%player%", t.get().getUsername())));
		if(plugin.getYamlHandler().getConfig().getBoolean("Mute.SendGlobal", false))
		{
			String msg = plugin.getYamlHandler().getLang().getString("CmdScc.Mute.PlayerUnmute")
					.replace("%target%", t.get().getUsername())
					.replace("%player%", player.getUsername());
			for(Player all : plugin.getServer().getAllPlayers())
			{
				if(all.getUniqueId().toString().equals(t.get().getUniqueId().toString())
						|| all.getUniqueId().toString().equals(player.getUniqueId().toString()))
				{
					continue;
				}
				all.sendMessage(ChatApi.tl(msg));
			}
		}
	}
}
