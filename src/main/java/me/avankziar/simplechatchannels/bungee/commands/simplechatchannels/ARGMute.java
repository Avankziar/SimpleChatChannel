package main.java.me.avankziar.simplechatchannels.bungee.commands.simplechatchannels;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.assistance.Utility;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.simplechatchannels.bungee.commands.tree.ArgumentModule;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.ChatUser;
import main.java.me.avankziar.simplechatchannels.bungee.database.MysqlHandler;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

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
		ProxiedPlayer player = (ProxiedPlayer) sender;
		Utility utility = plugin.getUtility();
		String language = "CmdScc.";

		String target = args[1];
		if(ProxyServer.getInstance().getPlayer(target)== null)
		{
			player.sendMessage(ChatApi.tctl(
					plugin.getYamlHandler().getL().getString(language+"NoPlayerExist")));
			return;
		}
		ProxiedPlayer t = ProxyServer.getInstance().getPlayer(target);
		ChatUser cu = ChatUser.getChatUser(t.getUniqueId());
		if(cu == null)
		{
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"NoPlayerExist")));
			return;
		}
		if(args.length == 2)
		{
			cu.setCanChat(false);
			cu.setMuteTime(0);
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.IGNOREOBJECT, cu, "`player_uuid` = ?", cu.getUUID());
			ChatUser.addChatUser(cu);
			t.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"Mute.PermaMute")));
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"Mute.HasMute")
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
				  player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"NoNumber")
						  .replace("%arg%", args[2])));
				  return;
			}
			Long time = 60L*1000;
			Long mutetime = System.currentTimeMillis()+num*time;
			cu.setCanChat(false);
			cu.setMuteTime(mutetime);
			plugin.getMysqlHandler().updateData(MysqlHandler.Type.IGNOREOBJECT, cu, "`player_uuid` = ?", cu.getUUID());
			ChatUser.addChatUser(cu);
			t.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"Mute.TempMute")
					.replace("%time%", args[2])));
			player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString(language+"Mute.HasMute")
					.replace("%player%", t.getName())
					.replace("%time%", num+" min")));
		} else if(utility.rightArgs(player,args,3))
		{
			return;
		}
	}
}
