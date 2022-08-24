package main.java.me.avankziar.scc.spigot.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import main.java.me.avankziar.scc.spigot.SimpleChatChannels;
import main.java.me.avankziar.scc.spigot.database.tables.TableI;
import main.java.me.avankziar.scc.spigot.database.tables.TableII;
import main.java.me.avankziar.scc.spigot.database.tables.TableIII;
import main.java.me.avankziar.scc.spigot.database.tables.TableIV;
import main.java.me.avankziar.scc.spigot.database.tables.TableV;
import main.java.me.avankziar.scc.spigot.database.tables.TableVI;

public class MysqlHandler implements TableI, TableII, TableIII, TableIV, TableV, TableVI
{
	public enum Type
	{
		CHATUSER("sccPlayerData"), 
		IGNOREOBJECT("sccIgnorelist"),
		PERMANENTCHANNEL("sccPermanentChannels"), 
		ITEMJSON("sccItemJson"), 
		USEDCHANNEL("sccPlayerUsedChannels"), 
		MAIL("sccMails");
		
		private Type(String value)
		{
			this.value = value;
		}
		
		private final String value;

		public String getValue()
		{
			return value;
		}
	}
	
	public enum QueryType
	{
		INSERT, UPDATE, DELETE, READ;
	}
	
	/*
	 * Alle Mysql Reihen, welche durch den Betrieb aufkommen.
	 */
	public static long startRecordTime = System.currentTimeMillis();
	public static int inserts = 0;
	public static int updates = 0;
	public static int deletes = 0;
	public static int reads = 0;
	
	public static void addRows(QueryType type, int amount)
	{
		switch(type)
		{
		case DELETE:
			deletes += amount;
			break;
		case INSERT:
			inserts += amount;
		case READ:
			reads += amount;
			break;
		case UPDATE:
			updates += amount;
			break;
		}
	}
	
	public static void resetsRows()
	{
		inserts = 0;
		updates = 0;
		reads = 0;
		deletes = 0;
	}
	
	private SimpleChatChannels plugin;
	public String tableNameI; //ChatUser
	public String tableNameII; //IgnoreObject
	public String tableNameIII; //Perma
	public String tableNameIV; //ItemJson
	public String tableNameV; //UsedChannel	
	public String tableNameVI; //Mail	
	
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
		tableNameVI = plugin.getYamlHandler().getConfig().getString("Mysql.TableNameVI");
		if(tableNameVI == null)
		{
			return false;
		}
		return true;
	}
	
	public boolean exist(Type type, String whereColumn, Object... object) 
	{
		String table = getTable(type);
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `id` FROM `" + table
						+ "` WHERE "+whereColumn+" LIMIT 1";
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : object)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        
		        result = preparedStatement.executeQuery();
		        MysqlHandler.addRows(QueryType.READ, result.getMetaData().getColumnCount());
		        while (result.next()) 
		        {
		        	return true;
		        }
		    } catch (SQLException e) 
			{
				  SimpleChatChannels.log.warning("Error: " + e.getMessage());
				  e.printStackTrace();
		    } finally 
			{
		    	  try 
		    	  {
		    		  if (result != null) 
		    		  {
		    			  result.close();
		    		  }
		    		  if (preparedStatement != null) 
		    		  {
		    			  preparedStatement.close();
		    		  }
		    	  } catch (Exception e) {
		    		  e.printStackTrace();
		    	  }
		      }
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
		case MAIL:
			return TableVI.super.createVI(plugin, object);
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
		case MAIL:
			return TableVI.super.updateDataVI(plugin, object, whereColumn, whereObject);
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
		case MAIL:
			return TableVI.super.getDataVI(plugin, whereColumn, whereObject);
		}
		return null;
	}
	
	private String getTable(Type type)
	{
		String table = "";
		switch(type)
		{
		case CHATUSER:
			table = tableNameI;
			break;
		case IGNOREOBJECT:
			table = tableNameII;
			break;
		case PERMANENTCHANNEL:
			table = tableNameIII;
			break;
		case ITEMJSON:
			table = tableNameIV;
			break;
		case USEDCHANNEL:
			table = tableNameV;
			break;
		case MAIL:
			table = tableNameVI;
			break;
		}
		return table;
	}
	
	public boolean deleteData(Type type, String whereColumn, Object... whereObject)
	{
		String table = getTable(type);
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		try 
		{
			String sql = "DELETE FROM `" + table + "` WHERE "+whereColumn;
			preparedStatement = conn.prepareStatement(sql);
			int i = 1;
	        for(Object o : whereObject)
	        {
	        	preparedStatement.setObject(i, o);
	        	i++;
	        }
			int d = preparedStatement.executeUpdate();
			MysqlHandler.addRows(QueryType.DELETE, d);
			return true;
		} catch (Exception e) 
		{
			e.printStackTrace();
		} finally 
		{
			try {
				if (preparedStatement != null) 
				{
					preparedStatement.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public int lastID(Type type)
	{
		String table = getTable(type);
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `id` FROM `" + table + "` ORDER BY `id` DESC LIMIT 1";
		        preparedStatement = conn.prepareStatement(sql);
		        
		        result = preparedStatement.executeQuery();
		        MysqlHandler.addRows(QueryType.READ, result.getMetaData().getColumnCount());
		        while(result.next())
		        {
		        	return result.getInt("id");
		        }
		    } catch (SQLException e) 
			{
		    	e.printStackTrace();
		    	return 0;
		    } finally 
			{
		    	  try 
		    	  {
		    		  if (result != null) 
		    		  {
		    			  result.close();
		    		  }
		    		  if (preparedStatement != null) 
		    		  {
		    			  preparedStatement.close();
		    		  }
		    	  } catch (Exception e) 
		    	  {
		    		  e.printStackTrace();
		    	  }
		      }
		}
		return 0;
	}
	
	public int countWhereID(Type type, String whereColumn, Object... whereObject)
	{
		String table = getTable(type);
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `id` FROM `" + table
						+ "` WHERE "+whereColumn
						+ " ORDER BY `id` DESC";
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
		        MysqlHandler.addRows(QueryType.READ, result.getMetaData().getColumnCount());
		        return result.getMetaData().getColumnCount();
		    } catch (SQLException e) 
			{
		    	e.printStackTrace();
		    	return 0;
		    } finally 
			{
		    	  try 
		    	  {
		    		  if (result != null) 
		    		  {
		    			  result.close();
		    		  }
		    		  if (preparedStatement != null) 
		    		  {
		    			  preparedStatement.close();
		    		  }
		    	  } catch (Exception e) 
		    	  {
		    		  e.printStackTrace();
		    	  }
		      }
		}
		return 0;
	}
	
	public int getCount(Type type, String orderByColumn, String whereColumn, Object... whereObject)
	{
		String table = getTable(type);
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String sql = " SELECT count(*) FROM `"+table
						+"` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" DESC";
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        
		        result = preparedStatement.executeQuery();
		        MysqlHandler.addRows(QueryType.READ, result.getMetaData().getColumnCount());
		        while (result.next()) 
		        {
		        	return result.getInt(1);
		        }
		    } catch (SQLException e) 
			{
				  SimpleChatChannels.log.warning("Error: " + e.getMessage());
				  e.printStackTrace();
		    } finally 
			{
		    	  try 
		    	  {
		    		  if (result != null) 
		    		  {
		    			  result.close();
		    		  }
		    		  if (preparedStatement != null) 
		    		  {
		    			  preparedStatement.close();
		    		  }
		    	  } catch (Exception e) {
		    		  e.printStackTrace();
		    	  }
		      }
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
		case MAIL:
			return TableVI.super.getListVI(plugin, orderByColumn, start, quantity, whereColumn, whereObject);
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
		case MAIL:
			return TableVI.super.getTopVI(plugin, orderByColumn, start, end);
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
		case MAIL:
			return TableVI.super.getAllListAtVI(plugin, orderByColumn, desc, whereColumn, whereObject);
		}
		return null;
	}
}