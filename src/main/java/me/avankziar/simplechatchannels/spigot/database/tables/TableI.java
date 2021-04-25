package main.java.me.avankziar.simplechatchannels.spigot.database.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import main.java.me.avankziar.simplechatchannels.objects.ChatUser;
import main.java.me.avankziar.simplechatchannels.spigot.SimpleChatChannels;

public interface TableI
{
	default boolean existI(SimpleChatChannels plugin, String whereColumn, Object... object) 
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `id` FROM `" + plugin.getMysqlHandler().tableNameI 
						+ "` WHERE "+whereColumn+" LIMIT 1";
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : object)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        
		        result = preparedStatement.executeQuery();
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
				String sql = "INSERT INTO `" + plugin.getMysqlHandler().tableNameI 
						+ "`(`player_uuid`, `player_name`, `can_chat`, `mutetime`,"
						+ " `channel_global`,`channel_trade`, `channel_auction`, `channel_support`,"
						+ " `channel_local`, `channel_world`, `channel_team`, `channel_admin`,"
						+ " `channel_group`, `channel_pm`, `channel_temp`, `channel_perma`,"
						+ " `channel_event`, `spy`, `joinmessage`) " 
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				preparedStatement = conn.prepareStatement(sql);
		        preparedStatement.setString(1, cu.getUUID());
		        preparedStatement.setString(2, cu.getName());
		        preparedStatement.setBoolean(3, cu.isCanChat());
		        preparedStatement.setLong(4, cu.getMuteTime());
		        preparedStatement.setBoolean(5, cu.isChannelGlobal());
		        preparedStatement.setBoolean(6, cu.isChannelTrade());
		        preparedStatement.setBoolean(7, cu.isChannelAuction());
		        preparedStatement.setBoolean(8, cu.isChannelSupport());
		        preparedStatement.setBoolean(9, cu.isChannelLocal());
		        preparedStatement.setBoolean(10, cu.isChannelWorld());
		        preparedStatement.setBoolean(11, cu.isChannelTeam());
		        preparedStatement.setBoolean(12, cu.isChannelAdmin());
		        preparedStatement.setBoolean(13, cu.isChannelGroup());
		        preparedStatement.setBoolean(14, cu.isChannelPrivateMessage());
		        preparedStatement.setBoolean(15, cu.isChannelTemporary());
		        preparedStatement.setBoolean(16, cu.isChannelPermanent());
		        preparedStatement.setBoolean(17, cu.isChannelEvent());
		        preparedStatement.setBoolean(18, cu.isOptionSpy());
		        preparedStatement.setBoolean(19, cu.isOptionChannelMessage());
		        
		        preparedStatement.executeUpdate();
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
				String data = "UPDATE `" + plugin.getMysqlHandler().tableNameI
						+ "` SET `player_uuid` = ?, `player_name` = ?, `can_chat` = ?, `mutetime` = ?," 
						+ " `channel_global` = ?,`channel_trade` = ?, `channel_auction` = ?, `channel_support` = ?," 
						+ " `channel_local` = ?, `channel_world` = ?, `channel_team` = ?, `channel_admin` = ?," 
						+ " `channel_group` = ?, `channel_pm` = ?, `channel_temp` = ?, `channel_perma` = ?," 
						+ " `channel_event` = ?, `spy` = ?, `joinmessage` = ?" 
						+ " WHERE "+whereColumn;
				preparedStatement = conn.prepareStatement(data);
				preparedStatement.setString(1, cu.getUUID());
		        preparedStatement.setString(2, cu.getName());
		        preparedStatement.setBoolean(3, cu.isCanChat());
		        preparedStatement.setLong(4, cu.getMuteTime());
		        preparedStatement.setBoolean(5, cu.isChannelGlobal());
		        preparedStatement.setBoolean(6, cu.isChannelTrade());
		        preparedStatement.setBoolean(7, cu.isChannelAuction());
		        preparedStatement.setBoolean(8, cu.isChannelSupport());
		        preparedStatement.setBoolean(9, cu.isChannelLocal());
		        preparedStatement.setBoolean(10, cu.isChannelWorld());
		        preparedStatement.setBoolean(11, cu.isChannelTeam());
		        preparedStatement.setBoolean(12, cu.isChannelAdmin());
		        preparedStatement.setBoolean(13, cu.isChannelGroup());
		        preparedStatement.setBoolean(14, cu.isChannelPrivateMessage());
		        preparedStatement.setBoolean(15, cu.isChannelTemporary());
		        preparedStatement.setBoolean(16, cu.isChannelPermanent());
		        preparedStatement.setBoolean(17, cu.isChannelEvent());
		        preparedStatement.setBoolean(18, cu.isOptionSpy());
		        preparedStatement.setBoolean(19, cu.isOptionChannelMessage());
		        
		        int i = 20;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
				
				preparedStatement.executeUpdate();
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
				String sql = "SELECT * FROM `" + plugin.getMysqlHandler().tableNameI 
						+ "` WHERE "+whereColumn+" LIMIT 1";
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        
		        result = preparedStatement.executeQuery();
		        while (result.next()) 
		        {
		        	return new ChatUser(
		        			result.getString("player_uuid"),
		        			result.getString("player_name"),
		        			result.getBoolean("can_chat"),
		        			result.getLong("mutetime"),
		        			result.getBoolean("channel_global"),
		        			result.getBoolean("channel_trade"),
		        			result.getBoolean("channel_auction"),
		        			result.getBoolean("channel_support"),
		        			result.getBoolean("channel_local"),
		        			result.getBoolean("channel_world"),
		        			result.getBoolean("channel_team"),
		        			result.getBoolean("channel_admin"),
		        			result.getBoolean("channel_group"),
		        			result.getBoolean("channel_event"),
		        			result.getBoolean("channel_pm"),
		        			result.getBoolean("channel_temp"),
		        			result.getBoolean("channel_perma"),
		        			result.getBoolean("spy"),
		        			result.getBoolean("joinmessage"));
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
	
	default boolean deleteDataI(SimpleChatChannels plugin, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		try 
		{
			String sql = "DELETE FROM `" + plugin.getMysqlHandler().tableNameI + "` WHERE "+whereColumn;
			preparedStatement = conn.prepareStatement(sql);
			int i = 1;
	        for(Object o : whereObject)
	        {
	        	preparedStatement.setObject(i, o);
	        	i++;
	        }
			preparedStatement.execute();
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
	
	default int lastIDI(SimpleChatChannels plugin)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `id` FROM `" + plugin.getMysqlHandler().tableNameI + "` ORDER BY `id` DESC LIMIT 1";
		        preparedStatement = conn.prepareStatement(sql);
		        
		        result = preparedStatement.executeQuery();
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
	
	default int countWhereIDI(SimpleChatChannels plugin, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `id` FROM `" + plugin.getMysqlHandler().tableNameI
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
		        int count = 0;
		        while(result.next())
		        {
		        	count++;
		        }
		        return count;
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
				String sql = "SELECT * FROM `" + plugin.getMysqlHandler().tableNameI
						+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" DESC LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
		        ArrayList<ChatUser> list = new ArrayList<ChatUser>();
		        while (result.next()) 
		        {
		        	ChatUser ep = new ChatUser(
		        			result.getString("player_uuid"),
		        			result.getString("player_name"),
		        			result.getBoolean("can_chat"),
		        			result.getLong("mutetime"),
		        			result.getBoolean("channel_global"),
		        			result.getBoolean("channel_trade"),
		        			result.getBoolean("channel_auction"),
		        			result.getBoolean("channel_support"),
		        			result.getBoolean("channel_local"),
		        			result.getBoolean("channel_world"),
		        			result.getBoolean("channel_team"),
		        			result.getBoolean("channel_admin"),
		        			result.getBoolean("channel_group"),
		        			result.getBoolean("channel_event"),
		        			result.getBoolean("channel_pm"),
		        			result.getBoolean("channel_temp"),
		        			result.getBoolean("channel_perma"),
		        			result.getBoolean("spy"),
		        			result.getBoolean("joinmessage"));
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
				String sql = "SELECT * FROM `" + plugin.getMysqlHandler().tableNameI 
						+ "` ORDER BY "+orderByColumn+" DESC LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        
		        result = preparedStatement.executeQuery();
		        ArrayList<ChatUser> list = new ArrayList<ChatUser>();
		        while (result.next()) 
		        {
		        	ChatUser ep = new ChatUser(
		        			result.getString("player_uuid"),
		        			result.getString("player_name"),
		        			result.getBoolean("can_chat"),
		        			result.getLong("mutetime"),
		        			result.getBoolean("channel_global"),
		        			result.getBoolean("channel_trade"),
		        			result.getBoolean("channel_auction"),
		        			result.getBoolean("channel_support"),
		        			result.getBoolean("channel_local"),
		        			result.getBoolean("channel_world"),
		        			result.getBoolean("channel_team"),
		        			result.getBoolean("channel_admin"),
		        			result.getBoolean("channel_group"),
		        			result.getBoolean("channel_event"),
		        			result.getBoolean("channel_pm"),
		        			result.getBoolean("channel_temp"),
		        			result.getBoolean("channel_perma"),
		        			result.getBoolean("spy"),
		        			result.getBoolean("joinmessage"));
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