package main.java.me.avankziar.scc.spigot.database.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import main.java.me.avankziar.scc.objects.ChatUser;
import main.java.me.avankziar.scc.objects.ServerLocation;
import main.java.me.avankziar.scc.spigot.SimpleChatChannels;
import main.java.me.avankziar.scc.spigot.database.MysqlHandler;
import main.java.me.avankziar.scc.spigot.database.MysqlHandler.QueryType;

public interface TableI
{	
	default boolean createI(SimpleChatChannels plugin, Object object) 
	{
		if(!(object instanceof ChatUser))
		{
			return false;
		}
		ChatUser cu = (ChatUser) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) {
			try 
			{
				String sql = "INSERT INTO `" + MysqlHandler.Type.CHATUSER.getValue() 
						+ "`(`player_uuid`, `player_name`, `roleplay_name`, `roleplayrenamecooldown`, `mutetime`,"
						+ " `spy`, `channelmessage`, `lasttimejoined`, `joinmessage`, `serverlocation`) " 
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				preparedStatement = conn.prepareStatement(sql);
		        preparedStatement.setString(1, cu.getUUID());
		        preparedStatement.setString(2, cu.getName());
		        preparedStatement.setString(3, cu.getRolePlayName());
		        preparedStatement.setLong(4, cu.getRolePlayRenameCooldown());
		        preparedStatement.setLong(5, cu.getMuteTime());
		        preparedStatement.setBoolean(6, cu.isOptionSpy());
		        preparedStatement.setBoolean(7, cu.isOptionChannelMessage());
		        preparedStatement.setLong(8, cu.getLastTimeJoined());
		        preparedStatement.setBoolean(9, cu.isOptionJoinMessage());
		        preparedStatement.setString(10, cu.serialized());
		        
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
	
	default boolean updateDataI(SimpleChatChannels plugin, Object object, String whereColumn, Object... whereObject) 
	{
		if(!(object instanceof ChatUser))
		{
			return false;
		}
		if(whereObject == null)
		{
			return false;
		}
		ChatUser cu = (ChatUser) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String data = "UPDATE `" + MysqlHandler.Type.CHATUSER.getValue()
						+ "` SET `player_uuid` = ?, `player_name` = ?, `roleplay_name` = ?, `roleplayrenamecooldown` = ?, `mutetime` = ?," 
						+ " `spy` = ?, `channelmessage` = ?, `lasttimejoined` = ?, `joinmessage` = ?, `serverlocation` = ?" 
						+ " WHERE "+whereColumn;
				preparedStatement = conn.prepareStatement(data);
				preparedStatement.setString(1, cu.getUUID());
		        preparedStatement.setString(2, cu.getName());
		        preparedStatement.setString(3, cu.getRolePlayName());
		        preparedStatement.setLong(4, cu.getRolePlayRenameCooldown());
		        preparedStatement.setLong(5, cu.getMuteTime());
		        preparedStatement.setBoolean(6, cu.isOptionSpy());
		        preparedStatement.setBoolean(7, cu.isOptionChannelMessage());
		        preparedStatement.setLong(8, cu.getLastTimeJoined());
		        preparedStatement.setBoolean(9, cu.isOptionJoinMessage());
		        preparedStatement.setString(10, cu.serialized());
		        
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
	
	default Object getDataI(SimpleChatChannels plugin, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.CHATUSER.getValue() 
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
		        	return new ChatUser(
		        			result.getString("player_uuid"),
		        			result.getString("player_name"),
		        			result.getString("roleplay_name"),
		        			result.getLong("roleplayrenamecooldown"),
		        			result.getLong("mutetime"),
		        			result.getBoolean("spy"),
		        			result.getBoolean("channelmessage"),
		        			result.getLong("lasttimejoined"),
		        			result.getBoolean("joinmessage"),
		        			ServerLocation.deserialized(result.getString("serverlocation")));
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
	
	default ArrayList<ChatUser> getListI(SimpleChatChannels plugin, String orderByColumn,
			int start, int end, String whereColumn, Object...whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.CHATUSER.getValue()
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
		        ArrayList<ChatUser> list = new ArrayList<ChatUser>();
		        while (result.next()) 
		        {
		        	ChatUser ep = new ChatUser(
		        			result.getString("player_uuid"),
		        			result.getString("player_name"),
		        			result.getString("roleplay_name"),
		        			result.getLong("roleplayrenamecooldown"),
		        			result.getLong("mutetime"),
		        			result.getBoolean("spy"),
		        			result.getBoolean("channelmessage"),
		        			result.getLong("lasttimejoined"),
		        			result.getBoolean("joinmessage"),
		        			ServerLocation.deserialized(result.getString("serverlocation")));
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
	
	default ArrayList<ChatUser> getTopI(SimpleChatChannels plugin, String orderByColumn, int start, int end)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + MysqlHandler.Type.CHATUSER.getValue() 
						+ "` ORDER BY "+orderByColumn+" DESC LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        
		        result = preparedStatement.executeQuery();
		        MysqlHandler.addRows(QueryType.READ, result.getMetaData().getColumnCount());
		        ArrayList<ChatUser> list = new ArrayList<ChatUser>();
		        while (result.next()) 
		        {
		        	ChatUser ep = new ChatUser(
		        			result.getString("player_uuid"),
		        			result.getString("player_name"),
		        			result.getString("roleplay_name"),
		        			result.getLong("roleplayrenamecooldown"),
		        			result.getLong("mutetime"),
		        			result.getBoolean("spy"),
		        			result.getBoolean("channelmessage"),
		        			result.getLong("lasttimejoined"),
		        			result.getBoolean("joinmessage"),
		        			ServerLocation.deserialized(result.getString("serverlocation")));
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
	
	default ArrayList<ChatUser> getAllListAtI(SimpleChatChannels plugin, String orderByColumn,
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
					sql = "SELECT * FROM `" + MysqlHandler.Type.CHATUSER.getValue()
							+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" DESC";
				} else
				{
					sql = "SELECT * FROM `" + MysqlHandler.Type.CHATUSER.getValue()
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
		        ArrayList<ChatUser> list = new ArrayList<ChatUser>();
		        while (result.next()) 
		        {
		        	ChatUser ep = new ChatUser(
		        			result.getString("player_uuid"),
		        			result.getString("player_name"),
		        			result.getString("roleplay_name"),
		        			result.getLong("roleplayrenamecooldown"),
		        			result.getLong("mutetime"),
		        			result.getBoolean("spy"),
		        			result.getBoolean("channelmessage"),
		        			result.getLong("lasttimejoined"),
		        			result.getBoolean("joinmessage"),
		        			ServerLocation.deserialized(result.getString("serverlocation")));
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