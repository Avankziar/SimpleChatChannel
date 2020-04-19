package main.java.me.avankziar.simplechatchannels.bungee;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import main.java.me.avankziar.simplechatchannels.bungee.interfaces.PermanentChannel;
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
					if((boolean) plugin.getMysqlHandler().getDataI(player.getUniqueId().toString(), "can_chat", "player_uuid") == false)
					{
						long mutetime = Long.parseLong(
								(String) plugin.getMysqlHandler().getDataI(player.getUniqueId().toString(), "mutetime", "player_uuid"));
						if(mutetime > 0L && mutetime < System.currentTimeMillis())
						{
							plugin.getMysqlHandler().updateDataI(player, true, "can_chat");
							plugin.getMysqlHandler().updateDataI(player, 0L, "mutetime");
							player.sendMessage(plugin.getUtility().tctlYaml(plugin.getUtility().getLanguage()+".CmdScc.Mute.Unmute"));
						}
					}
				}
			}
		}, 15L, TimeUnit.SECONDS);
	}
	
	public void initPermanentChannels()
	{
		int lastid = plugin.getMysqlHandler().getLastIDIII();
		if(lastid == 0)
		{
			return;
		}
		for(int i = 1; i <= lastid; i++)
		{
			if(plugin.getMysqlHandler().existChannel(i))
			{
				PermanentChannel pc = plugin.getMysqlHandler().getDataIII(i);
				PermanentChannel.addCustomChannel(pc);
			}
		}
	}
}
