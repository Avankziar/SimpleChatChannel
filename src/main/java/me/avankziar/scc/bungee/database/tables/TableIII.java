package main.java.me.avankziar.scc.bungee.database.tables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import main.java.me.avankziar.scc.bungee.SimpleChatChannels;
import main.java.me.avankziar.scc.objects.PermanentChannel;

public interface TableIII
{
	default boolean existIII(SimpleChatChannels plugin, String whereColumn, Object... object) 
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `id` FROM `" + plugin.getMysqlHandler().tableNameIII 
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
	
	default boolean createIII(SimpleChatChannels plugin, Object object) 
	{
		if(!(object instanceof PermanentChannel))
		{
			return false;
		}
		PermanentChannel pc = (PermanentChannel) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) {
			try 
			{
				String sql = "INSERT INTO `" + plugin.getMysqlHandler().tableNameIII 
						+ "`(`channel_name`, `creator`, `vice`, `members`,"
						+ " `password`, `banned`, `symbolextra`, `namecolor`,"
						+ " `chatcolor`) " 
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
				String vice = null;
				if(pc.getVice() != null)
				{
					vice = String.join(";", pc.getVice());
				}
				String members = null;
				if(pc.getMembers() != null)
				{
					members = String.join(";", pc.getMembers());
				}
				String banned = null;
				if(pc.getBanned() != null)
				{
					banned = String.join(";", pc.getBanned());
				}
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setString(1, pc.getName());
		        preparedStatement.setString(2, pc.getCreator());
		        preparedStatement.setString(3, vice);
		        preparedStatement.setString(4, members);
		        preparedStatement.setString(5, pc.getPassword());
		        preparedStatement.setString(6, banned);
		        preparedStatement.setString(7, pc.getSymbolExtra());
		        preparedStatement.setString(8, pc.getNameColor());
		        preparedStatement.setString(9, pc.getChatColor());
		        
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
	
	default boolean updateDataIII(SimpleChatChannels plugin, Object object, String whereColumn, Object... whereObject) 
	{
		if(!(object instanceof PermanentChannel))
		{
			return false;
		}
		if(whereObject == null)
		{
			return false;
		}
		PermanentChannel pc = (PermanentChannel) object;
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String sql = "UPDATE `" + plugin.getMysqlHandler().tableNameIII
						+ "` SET `channel_name` = ?, `creator` = ?, `vice` = ?, `members` = ?," 
						+ " `password` = ?, `banned` = ?, `symbolextra` = ?, `namecolor` = ?," 
						+ " `chatcolor` = ?" 
						+ " WHERE "+whereColumn;
				String vice = null;
				if(pc.getVice() != null)
				{
					vice = String.join(";", pc.getVice());
				}
				String members = null;
				if(pc.getMembers() != null)
				{
					members = String.join(";", pc.getMembers());
				}
				String banned = null;
				if(pc.getBanned() != null)
				{
					banned = String.join(";", pc.getBanned());
				}
				preparedStatement = conn.prepareStatement(sql);
				preparedStatement.setString(1, pc.getName());
		        preparedStatement.setString(2, pc.getCreator());
		        preparedStatement.setString(3, vice);
		        preparedStatement.setString(4, members);
		        preparedStatement.setString(5, pc.getPassword());
		        preparedStatement.setString(6, banned);
		        preparedStatement.setString(7, pc.getSymbolExtra());
		        preparedStatement.setString(8, pc.getNameColor());
		        preparedStatement.setString(9, pc.getChatColor());
		        
		        int i = 10;
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
	
	default Object getDataIII(SimpleChatChannels plugin, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + plugin.getMysqlHandler().tableNameIII 
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
		        	ArrayList<String> vice = new ArrayList<>();
					if(result.getString("vice") != null)
					{
						vice = new ArrayList<String>(Arrays.asList(result.getString("vice").split(";")));
					}
					ArrayList<String> members = new ArrayList<>();
					if(result.getString("members") != null)
					{
						members = new ArrayList<String>(Arrays.asList(result.getString("members").split(";")));
					}
					ArrayList<String> banned = new ArrayList<>();
					if(result.getString("banned") != null)
					{
						banned = new ArrayList<String>(Arrays.asList(result.getString("banned").split(";")));
					}
		        	return new PermanentChannel(
		        			result.getInt("id"),
		        			result.getString("channel_name"),
		        			result.getString("creator"),
		        			vice,
		        			members,
		        			result.getString("password"),
		        			banned,
		        			result.getString("symbolextra"),
		        			result.getString("namecolor"),
		        			result.getString("chatcolor"));
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
	
	default boolean deleteDataIII(SimpleChatChannels plugin, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		try 
		{
			String sql = "DELETE FROM `" + plugin.getMysqlHandler().tableNameIII + "` WHERE "+whereColumn;
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
	
	default int lastIDIII(SimpleChatChannels plugin)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `id` FROM `" + plugin.getMysqlHandler().tableNameIII + "` ORDER BY `id` DESC LIMIT 1";
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
	
	default int countWhereIDIII(SimpleChatChannels plugin, String whereColumn, Object... whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `id` FROM `" + plugin.getMysqlHandler().tableNameIII
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
	
	default ArrayList<PermanentChannel> getListIII(SimpleChatChannels plugin, String orderByColumn,
			int start, int end, String whereColumn, Object...whereObject)
	{
		PreparedStatement preparedStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT * FROM `" + plugin.getMysqlHandler().tableNameIII 
						+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" DESC LIMIT "+start+", "+end;
		        preparedStatement = conn.prepareStatement(sql);
		        int i = 1;
		        for(Object o : whereObject)
		        {
		        	preparedStatement.setObject(i, o);
		        	i++;
		        }
		        result = preparedStatement.executeQuery();
		        ArrayList<PermanentChannel> list = new ArrayList<PermanentChannel>();
		        while (result.next()) 
		        {
		        	ArrayList<String> vice = new ArrayList<>();
					if(result.getString("vice") != null)
					{
						vice = new ArrayList<String>(Arrays.asList(result.getString("vice").split(";")));
					}
					ArrayList<String> members = new ArrayList<>();
					if(result.getString("members") != null)
					{
						members = new ArrayList<String>(Arrays.asList(result.getString("members").split(";")));
					}
					ArrayList<String> banned = new ArrayList<>();
					if(result.getString("banned") != null)
					{
						banned = new ArrayList<String>(Arrays.asList(result.getString("banned").split(";")));
					}
		        	PermanentChannel w = new PermanentChannel(
		        			result.getInt("id"),
		        			result.getString("channel_name"),
		        			result.getString("creator"),
		        			vice,
		        			members,
		        			result.getString("password"),
		        			banned,
		        			result.getString("symbolextra"),
		        			result.getString("namecolor"),
		        			result.getString("chatcolor"));
		        	list.add(w);
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
	
	default ArrayList<PermanentChannel> getTopIII(SimpleChatChannels plugin, String orderByColumn, boolean desc, int start, int end)
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
					sql = "SELECT * FROM `" + plugin.getMysqlHandler().tableNameIII 
							+ "` ORDER BY "+orderByColumn+" DESC LIMIT "+start+", "+end;
				} else
				{
					sql = "SELECT * FROM `" + plugin.getMysqlHandler().tableNameIII 
							+ "` ORDER BY "+orderByColumn+" ASC LIMIT "+start+", "+end;
				}
				
		        preparedStatement = conn.prepareStatement(sql);
		        
		        result = preparedStatement.executeQuery();
		        ArrayList<PermanentChannel> list = new ArrayList<>();
		        while (result.next()) 
		        {
		        	ArrayList<String> vice = new ArrayList<>();
					if(result.getString("vice") != null)
					{
						vice = new ArrayList<String>(Arrays.asList(result.getString("vice").split(";")));
					}
					ArrayList<String> members = new ArrayList<>();
					if(result.getString("members") != null)
					{
						members = new ArrayList<String>(Arrays.asList(result.getString("members").split(";")));
					}
					ArrayList<String> banned = new ArrayList<>();
					if(result.getString("banned") != null)
					{
						banned = new ArrayList<String>(Arrays.asList(result.getString("banned").split(";")));
					}
		        	PermanentChannel w = new PermanentChannel(
		        			result.getInt("id"),
		        			result.getString("channel_name"),
		        			result.getString("creator"),
		        			vice,
		        			members,
		        			result.getString("password"),
		        			banned,
		        			result.getString("symbolextra"),
		        			result.getString("namecolor"),
		        			result.getString("chatcolor"));
		        	list.add(w);
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
	
	default ArrayList<PermanentChannel> getAllListAtIII(SimpleChatChannels plugin, String orderByColumn,
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
					sql = "SELECT * FROM `" + plugin.getMysqlHandler().tableNameIII
							+ "` WHERE "+whereColumn+" ORDER BY "+orderByColumn+" DESC";
				} else
				{
					sql = "SELECT * FROM `" + plugin.getMysqlHandler().tableNameIII
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
		        ArrayList<PermanentChannel> list = new ArrayList<>();
		        while (result.next()) 
		        {
		        	ArrayList<String> vice = new ArrayList<>();
					if(result.getString("vice") != null)
					{
						vice = new ArrayList<String>(Arrays.asList(result.getString("vice").split(";")));
					}
					ArrayList<String> members = new ArrayList<>();
					if(result.getString("members") != null)
					{
						members = new ArrayList<String>(Arrays.asList(result.getString("members").split(";")));
					}
					ArrayList<String> banned = new ArrayList<>();
					if(result.getString("banned") != null)
					{
						banned = new ArrayList<String>(Arrays.asList(result.getString("banned").split(";")));
					}
		        	PermanentChannel w = new PermanentChannel(
		        			result.getInt("id"),
		        			result.getString("channel_name"),
		        			result.getString("creator"),
		        			vice,
		        			members,
		        			result.getString("password"),
		        			banned,
		        			result.getString("symbolextra"),
		        			result.getString("namecolor"),
		        			result.getString("chatcolor"));
		        	list.add(w);
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