package main.java.me.avankziar.simplechatchannels.bungee.assistance;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.database.MysqlHandler;
import main.java.me.avankziar.simplechatchannels.objects.ChatApi;
import main.java.me.avankziar.simplechatchannels.objects.ChatUser;
import main.java.me.avankziar.simplechatchannels.objects.PermanentChannel;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class BackgroundTask 
{
	private SimpleChatChannels plugin;
	private ArrayList<String> players;
	
	public BackgroundTask(SimpleChatChannels plugin)
	{
		this.plugin = plugin;
		players = new ArrayList<String>();
		runTask();
		initPermanentChannels();
		unmuteTask();
	}
	
	public ArrayList<String> getPlayers()
	{
		return players;
	}
	
	private void runTask()
	{
		plugin.getProxy().getScheduler().schedule(plugin, new Runnable() 
		{
			
			@Override
			public void run() 
			{
				for(ProxiedPlayer player : plugin.getProxy().getPlayers())
				{
					if(!players.contains(player.getName()))
					{
						players.add(player.getName());
					}
				}
			}
		}, 15L, TimeUnit.SECONDS);	
	}
	
	public void unmuteTask()
	{
		plugin.getProxy().getScheduler().schedule(plugin, new Runnable() 
		{
			
			@Override
			public void run() 
			{
				for(ProxiedPlayer player : plugin.getProxy().getPlayers())
				{
					ChatUser cu = (ChatUser) plugin.getMysqlHandler().getData(MysqlHandler.Type.CHATUSER, "`player_uuid` = ?",
							player.getUniqueId().toString());
					if(cu.isCanChat() == false)
					{
						long mutetime = cu.getMuteTime();
						if(mutetime > 0L && mutetime < System.currentTimeMillis())
						{
							cu.setCanChat(true);
							cu.setMuteTime(0L);
							plugin.getMysqlHandler().updateData(MysqlHandler.Type.CHATUSER, cu, "`player_uuid` = ?",
									player.getUniqueId().toString());
							player.sendMessage(ChatApi.tctl(plugin.getYamlHandler().getL().getString("CmdScc.Mute.Unmute")));
						}
					}
				}
			}
		}, 1L, TimeUnit.SECONDS);
	}
	
	public void initPermanentChannels()
	{
		int lastid = plugin.getMysqlHandler().lastID(MysqlHandler.Type.PERMANENTCHANNEL);
		if(lastid == 0)
		{
			return;
		}
		for(int i = 1; i <= lastid; i++)
		{
			if(plugin.getMysqlHandler().exist(MysqlHandler.Type.PERMANENTCHANNEL, "`id` = ?", i))
			{
				PermanentChannel pc = (PermanentChannel) plugin.getMysqlHandler().getData(MysqlHandler.Type.PERMANENTCHANNEL,
						"`id` = ?", i);
				PermanentChannel.addCustomChannel(pc);
			}
		}
	}
}
