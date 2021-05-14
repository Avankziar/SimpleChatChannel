package main.java.me.avankziar.scc.spigot.database.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import main.java.me.avankziar.scc.objects.Mail;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;
import main.java.me.avankziar.scc.spigot.database.MysqlHandler;
import main.java.me.avankziar.scc.spigot.database.MysqlHandler.QueryType;

public interface TableVI
{	
	default boolean createVI(SimpleChatChannels plugin, Object object) 
	{
		if(!(object instanceof Mail))
		{
			return false;
		}
		Mail cu = (Mail) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) {
			try 
			{
				String sql = "INSERT INTO `" + plugin.getMysqlHandler().tableNameVI 
						+ "`(`sender_uuid`, `sender_name`, `reciver_uuid`, `reciver_name`,"
						+ " `carboncopy_uuid`, `carboncopy_name`, `sendeddate`,"
						+ " `readeddate`, `subject`, `rawmessage`) " 
						+ "VALUES"
						+ "(?, ?, ?, ?, ?"
						+ "?, ?, ?, ?, ?)";
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setString(1, cu.getSenderUUID());
		        preparedStatement.setString(2, cu.getSender());
		        preparedStatement.setString(3, cu.getReciverUUID().toString());
		        preparedStatement.setString(4, cu.getReciver());
		        preparedStatement.setString(5, cu.getCarbonCopyUUIDs());
		        preparedStatement.setString(6, cu.getCarbonCopyNames());
		        preparedStatement.setLong(7, cu.getSendedDate());
		        preparedStatement.setLong(8, cu.getReadedDate());
		        preparedStatement.setString(9, cu.getSubject());
		        preparedStatement.setString(10, cu.getRawText());
		        
		        int i = preparedStatement.executeUpdate();
		        MysqlHandler.addRows(QueryType.INSERT, i);
		        return true;
		    } catch (SQLException e) 
			{
				  SimpleChatChannels.log.warning("Error: " + e.getMessage());
				  e.printStackTrace();
		    } finally 
			{
		    	  try 
		    	  {
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
		return false;
	}
	
	default boolean updateDataVI(SimpleChatChannels plugin, Object object, String whereColumn, Object... whereObject) 
	{
		if(!(object instanceof Mail))
		{
			return false;
		}
		if(whereObject == null)
		{
			return false;
		}
		Mail cu = (Mail) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String data = "UPDATE `" + plugin.getMysqlHandler().tableNameVI
						+ "` SET `sender_uuid` = ?, `sender_name` = ?, `reciver_uuid` = ?, `reciver_name` = ?,"
						+ " `carboncopy_uuid` = ?, `carboncopy_name` = ?,"
						+ " `sendeddate` = ?, `readeddate` = ?, `subject`= ?, `rawmessage` = ?"
						+ " WHERE "+whereColumn;
				preparedStatement = conn.prepareStatement(data);
				preparedStatement.setString(1, cu.getSenderUUID());
		        preparedStatement.setString(2, cu.getSender());
		        preparedStatement.setString(3, cu.getReciverUUID().toString());
		        preparedStatement.setString(4, cu.getReciver());
		        preparedStatement.setString(5, cu.getCarbonCopyUUIDs());
		        preparedStatement.setString(6, cu.getCarbonCopyNames());
		        preparedStatement.setLong(7, cu.getSendedDate());
		        preparedStatement.setLong(8, cu.getReadedDate());
		        preparedStatement.setString(9, cu.getSubject());
		        preparedStatement.setString(10, cu.getRawText());
		        
		        int i = 11;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
				
				int u = preparedStatement.executeUpdate();
				MysqlHandler.addRows(QueryType.UPDATE, u);
				return true;
			} catch (SQLException e) {
				SimpleChatChannels.log.warning("Error: " + e.getMessage());
				e.printStackTrace();
			} finally {
				try {
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
	
	default Object getDataVI(SimpleChatChannels plugin, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + plugin.getMysqlHandler().tableNameVI 
						+ "` WHERE "+whereColumn+" LIMIT 1";
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
		        	return new Mail(
		        			result.getInt("id"),
		        			result.getString("sender_uuid"),
		        			result.getString("sender_name"),
		        			UUID.fromString(result.getString("reciver_uuid")),
		        			result.getString("reciver_name"),
		        			result.getString("carboncopy_uuid"),
		        			result.getString("carboncopy_name"),
		        			result.getLong("sendeddate"),
		        			result.getLong("readeddate"),
		        			result.getString("subject"),
		        			result.getString("rawmessage"));
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
		return null;
	}
	
	default ArrayList<Mail> getListVI(SimpleChatChannels plugin, String orderByColumn,
			int start, int end, String whereColumn, Object...whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + plugin.getMysqlHandler().tableNameVI
						+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" DESC LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
		        MysqlHandler.addRows(QueryType.READ, result.getMetaData().getColumnCount());
		        ArrayList<Mail> list = new ArrayList<Mail>();
		        while (result.next()) 
		        {
		        	Mail ep = new Mail(
		        			result.getInt("id"),
		        			result.getString("sender_uuid"),
		        			result.getString("sender_name"),
		        			UUID.fromString(result.getString("reciver_uuid")),
		        			result.getString("reciver_name"),
		        			result.getString("carboncopy_uuid"),
		        			result.getString("carboncopy_name"),
		        			result.getLong("sendeddate"),
		        			result.getLong("readeddate"),
		        			result.getString("subject"),
		        			result.getString("rawmessage"));
		        	list.add(ep);
		        }
		        return list;
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
		return null;
	}
	
	default ArrayList<Mail> getTopVI(SimpleChatChannels plugin, String orderByColumn, int start, int end)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + plugin.getMysqlHandler().tableNameVI 
						+ "` ORDER BY "+orderByColumn+" DESC LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        
		        result = preparedStatement.executeQuery();
		        MysqlHandler.addRows(QueryType.READ, result.getMetaData().getColumnCount());
		        ArrayList<Mail> list = new ArrayList<Mail>();
		        while (result.next()) 
		        {
		        	Mail ep = new Mail(
		        			result.getInt("id"),
		        			result.getString("sender_uuid"),
		        			result.getString("sender_name"),
		        			UUID.fromString(result.getString("reciver_uuid")),
		        			result.getString("reciver_name"),
		        			result.getString("carboncopy_uuid"),
		        			result.getString("carboncopy_name"),
		        			result.getLong("sendeddate"),
		        			result.getLong("readeddate"),
		        			result.getString("subject"),
		        			result.getString("rawmessage"));
		        	list.add(ep);
		        }
		        return list;
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
		return null;
	}
	
	default ArrayList<Mail> getAllListAtVI(SimpleChatChannels plugin, String orderByColumn,
			boolean desc, String whereColumn, Object...whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "";
				if(desc)
				{
					sql = "SELECT * FROM `" + plugin.getMysqlHandler().tableNameVI
							+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" DESC";
				} else
				{
					sql = "SELECT * FROM `" + plugin.getMysqlHandler().tableNameVI
							+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" ASC";
				}
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
		        MysqlHandler.addRows(QueryType.READ, result.getMetaData().getColumnCount());
		        ArrayList<Mail> list = new ArrayList<Mail>();
		        while (result.next()) 
		        {
		        	Mail ep = new Mail(
		        			result.getInt("id"),
		        			result.getString("sender_uuid"),
		        			result.getString("sender_name"),
		        			UUID.fromString(result.getString("reciver_uuid")),
		        			result.getString("reciver_name"),
		        			result.getString("carboncopy_uuid"),
		        			result.getString("carboncopy_name"),
		        			result.getLong("sendeddate"),
		        			result.getLong("readeddate"),
		        			result.getString("subject"),
		        			result.getString("rawmessage"));
		        	list.add(ep);
		        }
		        return list;
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
		return null;
	}
}
