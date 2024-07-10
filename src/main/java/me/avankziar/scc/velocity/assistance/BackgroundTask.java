package main.java.me.avankziar.scc.velocity.assistance;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;

import com.velocitypowered.api.proxy.Player;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.database.MysqlType;
import main.java.me.avankziar.scc.general.handlers.ConvertHandler;
import main.java.me.avankziar.scc.general.objects.ChatUser;
import main.java.me.avankziar.scc.general.objects.PermanentChannel;
import main.java.me.avankziar.scc.velocity.SCC;
import main.java.me.avankziar.scc.velocity.objects.ChatUserHandler;

public class BackgroundTask 
{
	private SCC plugin;
	private ArrayList<String> players;
	
	public BackgroundTask(SCC plugin)
	{
		this.plugin = plugin;
		players = new ArrayList<String>();
		runTask();
		initPermanentChannels();
		unmuteTask();
		if(plugin.getYamlHandler().getConfig().getBoolean("CleanUp.RunAutomaticByRestart", true))
		{
			runTaskCleanUp();
			runTaskCleanUpMails();
		}
	}
	
	public ArrayList<String> getPlayers()
	{
		return players;
	}
	
	private void runTaskCleanUpMails()
	{
		final int days = plugin.getYamlHandler().getConfig().getInt("CleanUp.DeleteReadedMailWhichIsOlderThanDays", 120);
		final long d = (long)days*1000L*60*60*24;
		final long lasttime = System.currentTimeMillis()-d;
		plugin.getMysqlHandler().deleteData(MysqlType.MAIL, "`readeddate` != ? AND `readeddate` < ?", 0, lasttime);
	}
	
	private void runTaskCleanUp()
	{
		final int days = plugin.getYamlHandler().getConfig().getInt("CleanUp.DeletePlayerWhichJoinIsOlderThanDays", 120);
		final long d = (long)days*1000L*60*60*24;
		final long lasttime = System.currentTimeMillis()-d;
		final ArrayList<ChatUser> users = ConvertHandler.convertListI(plugin.getMysqlHandler()
				.getFullList(MysqlType.CHATUSER, "`id` ASC", "?", 1));
		AtomicInteger count = new AtomicInteger(0);
		AtomicInteger deleted = new AtomicInteger(0);
		plugin.getServer().getScheduler().buildTask(plugin, (task) ->
		{
			if(count.get() >= users.size())
			{
				task.cancel();
				plugin.getLogger().log(Level.INFO, "Deleted "+deleted+" ChatUsers in the CleanUp Task!");
				return;
			}
			ChatUser user = users.get(count.get());
			if(lasttime >= user.getLastTimeJoined())
			{
				final String uuid = user.getUUID();
				plugin.getMysqlHandler().deleteData(MysqlType.USEDCHANNEL, "`player_uuid` = ?", uuid);
				plugin.getMysqlHandler().deleteData(MysqlType.ITEMJSON, "`owner` = ?", uuid);
				plugin.getMysqlHandler().deleteData(MysqlType.IGNOREOBJECT, "`player_uuid` = ? OR `ignore_uuid` = ?", uuid, uuid);
				plugin.getMysqlHandler().deleteData(MysqlType.CHATUSER, "`player_uuid` = ?", uuid);
				deleted.addAndGet(1);
			}
			count.addAndGet(1);
		}).delay(1L, TimeUnit.MILLISECONDS).repeat(15L, TimeUnit.MILLISECONDS).schedule();
	}
	
	private void runTask()
	{
		plugin.getServer().getScheduler().buildTask(plugin, (task) ->
		{
			for(Player player : plugin.getServer().getAllPlayers())
			{
				if(!players.contains(player.getUsername()))
				{
					players.add(player.getUsername());
				}
			}
		}).delay(1L, TimeUnit.MILLISECONDS).repeat(15L, TimeUnit.MILLISECONDS).schedule();
	}
	
	public void unmuteTask()
	{
		plugin.getServer().getScheduler().buildTask(plugin, (task) ->
		{
			for(Player player : plugin.getServer().getAllPlayers())
			{
				ChatUser cu = (ChatUser) plugin.getMysqlHandler().getData(MysqlType.CHATUSER, "`player_uuid` = ?",
						player.getUniqueId().toString());
				if(cu == null)
				{
					continue;
				}
				if(cu.getMuteTime() != 0)
				{
					long mutetime = cu.getMuteTime();
					if(mutetime < System.currentTimeMillis())
					{
						cu.setMuteTime(0L);
						plugin.getMysqlHandler().updateData(MysqlType.CHATUSER, cu, "`player_uuid` = ?",
								player.getUniqueId().toString());
						ChatUser chu = ChatUserHandler.getChatUser(player.getUniqueId());
						if(chu != null)
						{
							chu.setMuteTime(0L);
						}
						player.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.Mute.YouHaveBeenUnmute")));
					}
				}
			}
		}).delay(1L, TimeUnit.MILLISECONDS).repeat(15L, TimeUnit.MILLISECONDS).schedule();
	}
	
	public void initPermanentChannels()
	{
		int lastid = plugin.getMysqlHandler().lastID(MysqlType.PERMANENTCHANNEL);
		if(lastid == 0)
		{
			return;
		}
		for(int i = 1; i <= lastid; i++)
		{
			if(plugin.getMysqlHandler().exist(MysqlType.PERMANENTCHANNEL, "`id` = ?", i))
			{
				PermanentChannel pc = (PermanentChannel) plugin.getMysqlHandler().getData(MysqlType.PERMANENTCHANNEL,
						"`id` = ?", i);
				PermanentChannel.addCustomChannel(pc);
			}
		}
	}
}
