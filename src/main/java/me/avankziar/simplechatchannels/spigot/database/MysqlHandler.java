package main.java.me.avankziar.simplechatchannels.spigot.database;

import java.util.ArrayList;

import main.java.me.avankziar.simplechatchannels.spigot.database.tables.TableI;
import main.java.me.avankziar.simplechatchannels.spigot.database.tables.TableII;
import main.java.me.avankziar.simplechatchannels.spigot.database.tables.TableIII;
import main.java.me.avankziar.simplechatchannels.spigot.database.tables.TableIV;
import main.java.me.avankziar.simplechatchannels.spigot.database.tables.TableV;
import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;

public class MysqlHandler implements TableI, TableII, TableIII, TableIV, TableV
{
	public enum Type
	{
		CHATUSER, IGNOREOBJECT, PERMANENTCHANNEL, ITEMJSON, USEDCHANNEL;
	}
	
	private SimpleChatChannels plugin;
	public String tableNameI; //ChatUser
	public String tableNameII; //IgnoreObject
	public String tableNameIII; //Perma
	public String tableNameIV; //ItemJson
	public String tableNameV; //UsedChannel
	
	public MysqlHandler(SimpleChatChannels plugin) 
	{
		this.plugin = plugin;
		loadMysqlHandler();
	}
	
	public boolean loadMysqlHandler()
	{
		tableNameI = plugin.getYamlHandler().getConfig().getString("Mysql.TableNameI");
		if(tableNameI == null)
		{
			return false;
		}
		tableNameII = plugin.getYamlHandler().getConfig().getString("Mysql.TableNameII");
		if(tableNameII == null)
		{
			return false;
		}
		tableNameIII = plugin.getYamlHandler().getConfig().getString("Mysql.TableNameIII");
		if(tableNameIII == null)
		{
			return false;
		}
		tableNameIV = plugin.getYamlHandler().getConfig().getString("Mysql.TableNameIV");
		if(tableNameIV == null)
		{
			return false;
		}
		tableNameV = plugin.getYamlHandler().getConfig().getString("Mysql.TableNameV");
		if(tableNameV == null)
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
		case ITEMJSON:
			return TableIV.super.existIV(plugin, whereColumn, whereObject);
		case USEDCHANNEL:
			return TableV.super.existV(plugin, whereColumn, whereObject);
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
		case ITEMJSON:
			return TableIV.super.createIV(plugin, object);
		case USEDCHANNEL:
			return TableV.super.createV(plugin, object);
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
		case ITEMJSON:
			return TableIV.super.updateDataIV(plugin, object, whereColumn, whereObject);
		case USEDCHANNEL:
			return TableV.super.updateDataV(plugin, object, whereColumn, whereObject);
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
		case ITEMJSON:
			return TableIV.super.getDataIV(plugin, whereColumn, whereObject);
		case USEDCHANNEL:
			return TableV.super.getDataV(plugin, whereColumn, whereObject);
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
		case ITEMJSON:
			return TableIV.super.deleteDataIV(plugin, whereColumn, whereObject);
		case USEDCHANNEL:
			return TableV.super.deleteDataV(plugin, whereColumn, whereObject);
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
		case ITEMJSON:
			return TableIV.super.lastIDIV(plugin);
		case USEDCHANNEL:
			return TableV.super.lastIDV(plugin);
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
		case ITEMJSON:
			return TableIV.super.countWhereIDIV(plugin, whereColumn, whereObject);
		case USEDCHANNEL:
			return TableV.super.countWhereIDV(plugin, whereColumn, whereObject);
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
		case ITEMJSON:
			return TableIV.super.getListIV(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
		case USEDCHANNEL:
			return TableV.super.getListV(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
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
		case ITEMJSON:
			return TableIV.super.getTopIV(plugin, orderByColumn, start, end);
		case USEDCHANNEL:
			return TableV.super.getTopV(plugin, orderByColumn, start, end);
		}
		return null;
	}
	
	public ArrayList<?> getAllListAt(Type type, String orderByColumn, boolean desc, String whereColumn, Object...whereObject)
	{
		switch(type)
		{
		case CHATUSER:
			return TableI.super.getAllListAtI(plugin, orderByColumn, desc, whereColumn, whereObject);
		case IGNOREOBJECT:
			return TableII.super.getAllListAtII(plugin, orderByColumn, desc, whereColumn, whereObject);
		case PERMANENTCHANNEL:
			return TableIII.super.getAllListAtIII(plugin, orderByColumn, desc, whereColumn, whereObject);
		case ITEMJSON:
			return TableIV.super.getAllListAtIV(plugin, orderByColumn, desc, whereColumn, whereObject);
		case USEDCHANNEL:
			return TableV.super.getAllListAtV(plugin, orderByColumn, desc, whereColumn, whereObject);
		}
		return null;
	}
}