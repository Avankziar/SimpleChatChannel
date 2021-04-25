package main.java.me.avankziar.simplechatchannels.spigot.commands.simplechatchannels;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.ChatUser;
import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.spigot.assistance.Utility;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.spigot.database.MysqlHandler;
import main.java.me.avankziar.simplechatchannels.spigot.objects.ChatUserHandler;

public class ARGMute extends ArgumentModule
{
	private SimpleChatChannels plugin;
	
	public ARGMute(SimpleChatChannels plugin, ArgumentConstructor argumentConstructor)
	{
		super(plugin, argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		Player player = (Player) sender;
		Utility utility = plugin.getUtility();
		String language = "CmdScc.";

		String target = args[1];
		if(Bukkit.getPlayer(target)== null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("PlayerNotExist")));
			return;
		}
		Player t = Bukkit.getPlayer(target);
		ChatUser cu = ChatUserHandler.getChatUser(t.getUniqueId());
		if(cu == null)
		{
			player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("PlayerNotExist")));
			return;
		}
		if(args.length == 2)
		{
			cu.setCanChat(false);
			cu.setMuteTime(0);
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.IGNOREOBJECT, cu, "`player_uuid` = ?", cu.getUUID());
			ChatUser.addChatUser(cu);
			t.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"Mute.PermaMute")));
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString(language+"Mute.HasMute")
					.replace("%player%", t.getName())
					.replace("%time%", "permanent")));
		} else if(args.length == 3)
		{
			int num = 0;
			try
			{
				  num = Integer.parseInt(args[2]);
			} catch (NumberFormatException e) 
			{
				  player.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"NoNumber")
						  .replace("%arg%", args[2])));
				  return;
			}
			Long time = 60L*1000;
			Long mutetime = System.currentTimeMillis()+num*time;
			cu.setCanChat(false);
			cu.setMuteTime(mutetime);
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.IGNOREOBJECT, cu, "`player_uuid` = ?", cu.getUUID());
			ChatUser.addChatUser(cu);
			t.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"Mute.TempMute")
					.replace("%time%", args[2])));
			player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getL().getString(language+"Mute.HasMute")
					.replace("%player%", t.getName())
					.replace("%time%", num+" min")));
		} else if(utility.rightArgs(player,args,3))
		{
			return;
		}
	}
}