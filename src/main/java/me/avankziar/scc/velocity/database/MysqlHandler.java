package main.java.me.avankziar.scc.velocity.database;

import java.util.LinkedHashMap;

import main.java.me.avankziar.scc.general.database.MysqlBaseHandler;
import main.java.me.avankziar.scc.general.database.QueryType;
import main.java.me.avankziar.scc.velocity.SCC;

public class MysqlHandler extends MysqlBaseHandler
{	
	//FirstKey == ServerName, SecondKey = inserts etc.
	public static LinkedHashMap<String, LinkedHashMap<QueryType, Integer>> serverPerformance = new LinkedHashMap<>();
	
	public MysqlHandler(SCC plugin)
	{
		super(plugin.getLogger(), plugin.getMysqlSetup());
	}
}
