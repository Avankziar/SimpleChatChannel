package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.ChatUser;
import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.spigot.database.MysqlHandler;

public class ARGUnmute extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGUnmute(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		String language = "CmdScc.";
		String target = args[1];
		OfflinePlayer t = Bukkit.getOfflinePlayer(target);
		ChatUser cu = (ChatUser) plugin.getMysqlHandler().getData(MysqlHandler.Type.CHATUSER,
				"`player_name` = ?", target);
		if(cu == null)
		{
			///Der Spieler ist nicht online oder existiert nicht!
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("PlayerNotExist")));
			return;
		}
		cu.setCanChat(true);
		cu.setMuteTime(0);
		plugin.getMysqlHandler().updateData(MysqlHandler.Type.CHATUSER, cu, "`player_uuid` = ?", cu.getUUID());
		if(t.getPlayer() != null)
		{
			ChatUser.addChatUser(cu);
			t.getPlayer().spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"Mute.Unmute")));
		}
		player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString(language+"Mute.HasUnMute")
				.replace("%player%", t.getName())));
		return;
	}
}