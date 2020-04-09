package main.java.me.avankziar.simplechatchannels.bungee.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class MysqlHandler 
{
	private SimpleChatChannels plugin;
	public String tableNameI;
	public String tableNameII;
	
	public MysqlHandler(SimpleChatChannels plugin)
	{
		this.plugin = plugin;
		tableNameI = plugin.getYamlHandler().get().getString("mysql.tableNameI");
		tableNameII = plugin.getYamlHandler().get().getString("mysql.tableNameII");
	}
	
	public boolean hasAccount(ProxiedPlayer player) 
	{
		PreparedStatement preparedUpdateStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getDatabaseHandler().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `player_uuid` FROM `" + tableNameI + "` WHERE `player_uuid` = ? LIMIT 1";
		        preparedUpdateStatement = conn.prepareStatement(sql);
		        preparedUpdateStatement.setString(1, player.getUniqueId().toString());
		        
		        result = preparedUpdateStatement.executeQuery();
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
		    		  if (preparedUpdateStatement != null) 
		    		  {
		    			  preparedUpdateStatement.close();
		    		  }
		    	  } catch (Exception e) {
		    		  e.printStackTrace();
		    	  }
		      }
		}
		return false;
	}
	
	public boolean existIgnore(ProxiedPlayer player, String ignoreuuid) 
	{
		PreparedStatement preparedUpdateStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getDatabaseHandler().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `player_uuid` FROM `" + tableNameII 
						+ "` WHERE `player_uuid` = ? AND `ignore_uuid` = ? LIMIT 1";
		        preparedUpdateStatement = conn.prepareStatement(sql);
		        preparedUpdateStatement.setString(1, player.getUniqueId().toString());
		        preparedUpdateStatement.setString(2, ignoreuuid);
		        
		        result = preparedUpdateStatement.executeQuery();
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
		    		  if (preparedUpdateStatement != null) 
		    		  {
		    			  preparedUpdateStatement.close();
		    		  }
		    	  } catch (Exception e) {
		    		  e.printStackTrace();
		    	  }
		      }
		}
		return false;
	}
	
	public boolean createAccount(ProxiedPlayer player) 
	{
		boolean f = false;
		Boolean global = player.hasPermission("scc.channels.global");
		Boolean trade = player.hasPermission("scc.channels.trade");
		Boolean auction = player.hasPermission("scc.channels.auction");
		Boolean local = player.hasPermission("scc.channels.local");
		Boolean support = player.hasPermission("scc.channels.support");
		Boolean world = player.hasPermission("scc.channels.world");
		Boolean team = player.hasPermission("scc.channels.team");
		Boolean admin = player.hasPermission("scc.channels.admin");
		Boolean group = player.hasPermission("scc.channels.group");
		Boolean pmn = player.hasPermission("scc.channels.pm");
		Boolean custom = player.hasPermission("scc.channels.custom");
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getDatabaseHandler().getConnection();
		if (conn != null) {
			try 
			{
				String sql = "INSERT INTO `" + tableNameI + "`(`player_uuid`, `player_name`,"
						+ " `can_chat`, `mutetime`,"
						+ " `channel_global`, `channel_trade`, `channel_auction`, `channel_support`,"
						+ " `channel_local`, `channel_world`,"
						+ " `channel_team`, `channel_admin`,"
						+ " `channel_group`, `channel_pm`, `channel_custom`,"
						+ " `spy`, `joinmessage`) " 
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
				preparedStatement = conn.prepareStatement(sql);
		        preparedStatement.setString(1, player.getUniqueId().toString());
		        preparedStatement.setString(2, player.getName());
		        preparedStatement.setBoolean(3, true);
		        preparedStatement.setLong(4, 0L);
		        preparedStatement.setBoolean(5, global);
		        preparedStatement.setBoolean(6, trade);
		        preparedStatement.setBoolean(7, auction);
		        preparedStatement.setBoolean(8, support);
		        preparedStatement.setBoolean(9, local);
		        preparedStatement.setBoolean(10, world);
		        preparedStatement.setBoolean(11, team);
		        preparedStatement.setBoolean(12, admin);
		        preparedStatement.setBoolean(13, group);
		        preparedStatement.setBoolean(14, pmn);
		        preparedStatement.setBoolean(15, custom);
		        preparedStatement.setBoolean(16, f);
		        preparedStatement.setBoolean(17, true);
		        
		        
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
	
	public boolean createIgnore(ProxiedPlayer player, ProxiedPlayer ignore) 
	{
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getDatabaseHandler().getConnection();
		if (conn != null) {
			try 
			{
				String sql = "INSERT INTO `" + tableNameII + "`(`player_uuid`, `ignore_uuid`, `ignore_name`) " 
						+ "VALUES(?, ?, ?)";
				preparedStatement = conn.prepareStatement(sql);
		        preparedStatement.setString(1, player.getUniqueId().toString());
		        preparedStatement.setString(2, ignore.getUniqueId().toString());
		        preparedStatement.setString(3, ignore.getName());
		        
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
	
	public boolean updateDataI(ProxiedPlayer player, Object object, String setcolumn) 
	{
		if (!hasAccount(player)) 
		{
			createAccount(player);
		}
		PreparedStatement preparedUpdateStatement = null;
		Connection conn = plugin.getDatabaseHandler().getConnection();
		if (conn != null) 
		{
			try 
			{
				String data = "UPDATE `" + tableNameI 
						+ "` " + "SET `" + setcolumn + "` = ?" + " WHERE `player_uuid` = ?";
				preparedUpdateStatement = conn.prepareStatement(data);
				preparedUpdateStatement.setObject(1, object);
				preparedUpdateStatement.setString(2, player.getUniqueId().toString());
				
				preparedUpdateStatement.executeUpdate();
				return true;
			} catch (SQLException e) {
				SimpleChatChannels.log.warning("Error: " + e.getMessage());
				e.printStackTrace();
			} finally {
				try {
					if (preparedUpdateStatement != null) {
						preparedUpdateStatement.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
        return false;
	}
	
	public boolean updateDataII(ProxiedPlayer player, Object object, String setcolumn, String wherecolumn) 
	{
		PreparedStatement preparedUpdateStatement = null;
		Connection conn = plugin.getDatabaseHandler().getConnection();
		if (conn != null) 
		{
			try 
			{
				String data = "UPDATE `" + tableNameII 
						+ "` " + "SET `" + setcolumn + "` = ?" + " WHERE `" + wherecolumn + "` = ?";
				preparedUpdateStatement = conn.prepareStatement(data);
				preparedUpdateStatement.setObject(1, object);
				preparedUpdateStatement.setString(2, player.getUniqueId().toString());
				
				preparedUpdateStatement.executeUpdate();
				return true;
			} catch (SQLException e) {
				SimpleChatChannels.log.warning("Error: " + e.getMessage());
				e.printStackTrace();
			} finally {
				try {
					if (preparedUpdateStatement != null) {
						preparedUpdateStatement.close();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
        return false;
	}
	
	public Object getDataI(ProxiedPlayer player, String selectcolumn, String wherecolumn)
	{
		if (!hasAccount(player)) 
		{
			createAccount(player);
		}
		PreparedStatement preparedUpdateStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getDatabaseHandler().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `" + selectcolumn + "` FROM `" + tableNameI + "` WHERE `" + wherecolumn + "` = ? LIMIT 1";
		        preparedUpdateStatement = conn.prepareStatement(sql);
		        preparedUpdateStatement.setString(1, player.getUniqueId().toString());
		        
		        result = preparedUpdateStatement.executeQuery();
		        while (result.next()) 
		        {
		        	return result.getObject(selectcolumn);
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
		    		  if (preparedUpdateStatement != null) 
		    		  {
		    			  preparedUpdateStatement.close();
		    		  }
		    	  } catch (Exception e) {
		    		  e.printStackTrace();
		    	  }
		      }
		}
		return null;
	}
	
	public Object getDataII(ProxiedPlayer player, String selectcolumn, String wherecolumn)
	{
		if (!hasAccount(player)) 
		{
			createAccount(player);
		}
		PreparedStatement preparedUpdateStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getDatabaseHandler().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `" + selectcolumn + "` FROM `" + tableNameII + "` WHERE `" + wherecolumn + "` = ? LIMIT 1";
		        preparedUpdateStatement = conn.prepareStatement(sql);
		        preparedUpdateStatement.setString(1, player.getUniqueId().toString());
		        
		        result = preparedUpdateStatement.executeQuery();
		        while (result.next()) 
		        {
		        	return result.getObject(selectcolumn);
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
		    		  if (preparedUpdateStatement != null) 
		    		  {
		    			  preparedUpdateStatement.close();
		    		  }
		    	  } catch (Exception e) {
		    		  e.printStackTrace();
		    	  }
		      }
		}
		return null;
	}
	
	public String getIgnoreList(ProxiedPlayer player, String selectcolumn, String wherecolumn)
	{
		PreparedStatement preparedUpdateStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getDatabaseHandler().getConnection();
		String list = "";
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `" + selectcolumn + "` FROM `" + tableNameII + "` WHERE `" + wherecolumn + "` = ?";
		        preparedUpdateStatement = conn.prepareStatement(sql);
		        preparedUpdateStatement.setString(1, player.getUniqueId().toString());
		        
		        result = preparedUpdateStatement.executeQuery();
		        while (result.next()) 
		        {
		        	list += result.getString(selectcolumn)+", ";
		        }
		        if(list.length()<=2)
		        {
		        	return null;
		        }
		        return list.substring(0,list.length()-2);
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
		    		  if (preparedUpdateStatement != null) 
		    		  {
		    			  preparedUpdateStatement.close();
		    		  }
		    	  } catch (Exception e) {
		    		  e.printStackTrace();
		    	  }
		      }
		}
		return null;
	}
	
	public void deleteDataI(Object object, String wherecolumn)
	{
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getDatabaseHandler().getConnection();
		try 
		{
			String sql = "DELETE FROM `" + tableNameI + "` WHERE `" + wherecolumn + "` = ?";
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setObject(1, object);
			preparedStatement.execute();
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
	}
	
	public void deleteDataII(Object objectI, Object objectII, String wherecolumnI, String wherecolumnII)
	{
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getDatabaseHandler().getConnection();
		try 
		{
			String sql = "DELETE FROM `" + tableNameII + "` WHERE `" + wherecolumnI + "` = ? AND `" + wherecolumnII + "` = ?";
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setObject(1, objectI);
			preparedStatement.setObject(2, objectII);
			preparedStatement.execute();
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
	}
}
