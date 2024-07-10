package main.java.me.avankziar.scc.spigot.commands.scc;

import org.bukkit.command.CommandSender;

import main.java.me.avankziar.scc.general.assistance.ChatApi;
import main.java.me.avankziar.scc.general.commands.tree.ArgumentConstructor;
import main.java.me.avankziar.scc.general.handlers.TimeHandler;
import main.java.me.avankziar.scc.spigot.SCC;
import main.java.me.avankziar.scc.spigot.commands.tree.ArgumentModule;
import main.java.me.avankziar.scc.spigot.database.MysqlHandler;

public class ARGPerformance extends ArgumentModule
{
	private SCC plugin;
	
	public ARGPerformance(SCC plugin, ArgumentConstructor argumentConstructor)
	{
		super(argumentConstructor);
		this.plugin = plugin;
	}

	@Override
	public void run(CommandSender sender, String[] args)
	{
		sender.spigot().sendMessage(ChatApi.tctl(plugin.getYamlHandler().getLang().getString("CmdScc.Performance.Headline")
				.replace("%time%", TimeHandler.getDateTime(System.currentTimeMillis()))));
		String msg = plugin.getYamlHandler().getLang().getString("CmdScc.Performance.Text");
		int totalInserts = MysqlHandler.inserts;
		int totalUpdates = MysqlHandler.updates;
		int totalDeletes = MysqlHandler.deletes;
		int totalReads = MysqlHandler.reads;
		sender.spigot().sendMessage(ChatApi.tctl(msg
				.replace("%server%", "Total")
				.replace("%insert%", String.valueOf(totalInserts))
				.replace("%update%", String.valueOf(totalUpdates))
				.replace("%delete%", String.valueOf(totalDeletes))
				.replace("%read%", String.valueOf(totalReads))));
		long diff = System.currentTimeMillis()-MysqlHandler.startRecordTime;
		int sec = (int)((double)diff/1000);
		sender.spigot().sendMessage(ChatApi.tctl(msg
				.replace("%server%", "Total/sec")
				.replace("%insert%", String.valueOf(totalInserts/sec))
				.replace("%update%", String.valueOf(totalUpdates/sec))
				.replace("%delete%", String.valueOf(totalDeletes/sec))
				.replace("%read%", String.valueOf(totalReads/sec))));
	}
}