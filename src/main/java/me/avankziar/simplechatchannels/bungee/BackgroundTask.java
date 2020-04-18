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
