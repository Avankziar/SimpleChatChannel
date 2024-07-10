package main.java.me.avankziar.scc.velocity.commands.scc;

import java.util.LinkedHashMap;

import com.velocitypowered.api.command.CommandSource;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.database.QueryType;
import main.java.me.avankziar.scc.general.handlers.TimeHandler;
import main.java.me.avankziar.scc.velocity.SCC;
import main.java.me.avankziar.scc.velocity.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.velocity.database.MysqlHandler;

public class ARGPerformance extends ArgumentModule
{
	private SCC plugin;
	
	public ARGPerformance(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSource sender, String[] args)
	{
		sender.sendMessage(ChatApi.tl(plugin.getYamlHandler().getLang().getString("CmdScc.Performance.Headline")
				.replace("%time%", TimeHandler.getDateTime(System.currentTimeMillis()))));
		String msg = plugin.getYamlHandler().getLang().getString("CmdScc.Performance.Text");
		sender.sendMessage(ChatApi.tl(msg
				.replace("%server%", "BungeeCord")
				.replace("%insert%", String.valueOf(MysqlHandler.inserts))
				.replace("%update%", String.valueOf(MysqlHandler.updates))
				.replace("%delete%", String.valueOf(MysqlHandler.deletes))
				.replace("%read%", String.valueOf(MysqlHandler.reads))));
		int totalInserts = MysqlHandler.inserts;
		int totalUpdates = MysqlHandler.updates;
		int totalDeletes = MysqlHandler.deletes;
		int totalReads = MysqlHandler.reads;
		for(String server : MysqlHandler.serverPerformance.keySet())
		{
			LinkedHashMap<QueryType, Integer> map = MysqlHandler.serverPerformance.get(server);
			int insert = map.get(QueryType.INSERT);
			int update = map.get(QueryType.UPDATE);
			int delete = map.get(QueryType.DELETE);
			int read = map.get(QueryType.READ);
			totalInserts += insert;
			totalUpdates += update;
			totalDeletes += delete;
			totalReads += read;
			sender.sendMessage(ChatApi.tl(msg
				.replace("%server%", server)
				.replace("%insert%", String.valueOf(insert))
				.replace("%update%", String.valueOf(update))
				.replace("%delete%", String.valueOf(delete))
				.replace("%read%", String.valueOf(read))));
		}
		sender.sendMessage(ChatApi.tl(msg
				.replace("%server%", "Total")
				.replace("%insert%", String.valueOf(totalInserts))
				.replace("%update%", String.valueOf(totalUpdates))
				.replace("%delete%", String.valueOf(totalDeletes))
				.replace("%read%", String.valueOf(totalReads))));
		long diff = System.currentTimeMillis()-MysqlHandler.startRecordTime;
		int sec = (int)((double)diff/1000);
		sender.sendMessage(ChatApi.tl(msg
				.replace("%server%", "Total/sec")
				.replace("%insert%", String.valueOf(totalInserts/sec))
				.replace("%update%", String.valueOf(totalUpdates/sec))
				.replace("%delete%", String.valueOf(totalDeletes/sec))
				.replace("%read%", String.valueOf(totalReads/sec))));
	}
}