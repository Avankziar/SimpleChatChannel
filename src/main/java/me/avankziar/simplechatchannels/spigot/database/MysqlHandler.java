package main.java.me.avankziar.simplechatchannels.spigot.database;

import java.util.ArrayList;

import main.java.me.avankziar.simplechatchannels.spigot.database.tables.TableI;
import main.java.me.avankziar.simplechatchannels.spigot.database.tables.TableII;
import main.java.me.avankziar.simplechatchannels.spigot.database.tables.TableIII;
import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;

public class MysqlHandler implements TableI, TableII, TableIII
{
	public enum Type
	{
		CHATUSER, IGNOREOBJECT, PERMANENTCHANNEL;
	}
	
	private SimpleChatChannels plugin;
	public String tableNameI; //ChatUser
	public String tableNameII; //IgnoreObject
	public String tableNameIII; //Perma
	
	public MysqlHandler(SimpleChatChannels plugin) 
	{
		this.plugin = plugin;
		loadMysqlHandler();
	}
	
	public boolean loadMysqlHandler()
	{
		tableNameI = plugin.getYamlHandler().get().getString("Mysql.TableNameI");
		if(tableNameI == null)
		{
			return false;
		}
		tableNameII = plugin.getYamlHandler().get().getString("Mysql.TableNameII");
		if(tableNameII == null)
		{
			return false;
		}
		tableNameIII = plugin.getYamlHandler().get().getString("Mysql.TableNameIII");
		if(tableNameIII == null)
		{
			return false;
		}
		return true;
	}
	
	public boolean exist(Type type, String whereColumn, Object... whereObject)
	{
		switch(type)
		{
		case CHATUSER:
			return TableI.super.existI(plugin, whereColumn, whereObject);
		case IGNOREOBJECT:
			return TableII.super.existII(plugin, whereColumn, whereObject);
		case PERMANENTCHANNEL:
			return TableIII.super.existIII(plugin, whereColumn, whereObject);
		}
		return false;
	}
	
	public boolean create(Type type, Object object)
	{
		switch(type)
		{
		case CHATUSER:
			return TableI.super.createI(plugin, object);
		case IGNOREOBJECT:
			return TableII.super.createII(plugin, object);
		case PERMANENTCHANNEL:
			return TableIII.super.createIII(plugin, object);
		}
		return false;
	}
	
	public boolean updateData(Type type, Object object, String whereColumn, Object... whereObject)
	{
		switch(type)
		{
		case CHATUSER:
			return TableI.super.updateDataI(plugin, object, whereColumn, whereObject);
		case IGNOREOBJECT:
			return TableII.super.updateDataII(plugin, object, whereColumn, whereObject);
		case PERMANENTCHANNEL:
			return TableIII.super.updateDataIII(plugin, object, whereColumn, whereObject);
		}
		return false;
	}
	
	public Object getData(Type type, String whereColumn, Object... whereObject)
	{
		switch(type)
		{
		case CHATUSER:
			return TableI.super.getDataI(plugin, whereColumn, whereObject);
		case IGNOREOBJECT:
			return TableII.super.getDataII(plugin, whereColumn, whereObject);
		case PERMANENTCHANNEL:
			return TableIII.super.getDataIII(plugin, whereColumn, whereObject);
		}
		return null;
	}
	
	public boolean deleteData(Type type, String whereColumn, Object... whereObject)
	{
		switch(type)
		{
		case CHATUSER:
			return TableI.super.deleteDataI(plugin, whereColumn, whereObject);
		case IGNOREOBJECT:
			return TableII.super.deleteDataII(plugin, whereColumn, whereObject);
		case PERMANENTCHANNEL:
			return TableIII.super.deleteDataIII(plugin, whereColumn, whereObject);
		}
		return false;
	}
	
	public int lastID(Type type)
	{
		switch(type)
		{
		case CHATUSER:
			return TableI.super.lastIDI(plugin);
		case IGNOREOBJECT:
			return TableII.super.lastIDII(plugin);
		case PERMANENTCHANNEL:
			return TableIII.super.lastIDIII(plugin);
		}
		return 0;
	}
	
	public int countWhereID(Type type, String whereColumn, Object... whereObject)
	{
		switch(type)
		{
		case CHATUSER:
			return TableI.super.countWhereIDI(plugin, whereColumn, whereObject);
		case IGNOREOBJECT:
			return TableII.super.countWhereIDII(plugin, whereColumn, whereObject);
		case PERMANENTCHANNEL:
			return TableIII.super.countWhereIDIII(plugin, whereColumn, whereObject);
		}
		return 0;
	}
	
	public ArrayList<?> getList(Type type, String orderByColumn, boolean desc, int start, int quantity, String whereColumn, Object...whereObject)
	{
		switch(type)
		{
		case CHATUSER:
			return TableI.super.getListI(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case IGNOREOBJECT:
			return TableII.super.getListII(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case PERMANENTCHANNEL:
			return TableIII.super.getListIII(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		}
		return null;
	}
	
	public ArrayList<?> getTop(Type type, String orderByColumn, boolean desc, int start, int end)
	{
		switch(type)
		{
		case CHATUSER:
			return TableI.super.getTopI(plugin, orderByColumn, start, end);
		case IGNOREOBJECT:
			return TableII.super.getTopII(plugin, orderByColumn, start, end);
		case PERMANENTCHANNEL:
			return TableIII.super.getTopIII(plugin, orderByColumn, desc, start, end);
		}
		return null;
	}
}
