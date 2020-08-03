package main.java.me.avankziar.simplechatchannels.bungee.database;

import java.util.ArrayList;

import main.java.me.avankziar.simplechatchannels.bungee.SimpleChatChannels;
import main.java.me.avankziar.simplechatchannels.bungee.database.tables.TableI;
import main.java.me.avankziar.simplechatchannels.bungee.database.tables.TableII;
import main.java.me.avankziar.simplechatchannels.bungee.database.tables.TableIII;

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
	/*private SimpleChatChannels plugin;
	public String tableNameI;
	public String tableNameII;
	public String tableNameIII;
	
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
	
	public boolean hasAccount(ProxiedPlayer player) 
	{
		PreparedStatement preparedUpdateStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
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
	
	public boolean hasAccount(String playername) 
	{
		PreparedStatement preparedUpdateStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `player_uuid` FROM `" + tableNameI + "` WHERE `player_name` = ? LIMIT 1";
		        preparedUpdateStatement = conn.prepareStatement(sql);
		        preparedUpdateStatement.setString(1, playername);
		        
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
		Connection conn = plugin.getMysqlSetup().getConnection();
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
	
	public boolean existChannel(int id) 
	{
		PreparedStatement preparedUpdateStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `id` FROM `" + tableNameIII + "` WHERE `id` = ? LIMIT 1";
		        preparedUpdateStatement = conn.prepareStatement(sql);
		        preparedUpdateStatement.setInt(1, id);
		        
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
	
	public boolean existChannel(String channelname) 
	{
		PreparedStatement preparedUpdateStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `channel_name` FROM `" + tableNameIII + "` WHERE `channel_name` = ? LIMIT 1";
		        preparedUpdateStatement = conn.prepareStatement(sql);
		        preparedUpdateStatement.setString(1, channelname);
		        
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
		Boolean event = player.hasPermission("scc.channels.event");
		Boolean local = player.hasPermission("scc.channels.local");
		Boolean support = player.hasPermission("scc.channels.support");
		Boolean world = player.hasPermission("scc.channels.world");
		Boolean team = player.hasPermission("scc.channels.team");
		Boolean admin = player.hasPermission("scc.channels.admin");
		Boolean group = player.hasPermission("scc.channels.group");
		Boolean pmn = player.hasPermission("scc.channels.pm");
		Boolean temp = player.hasPermission("scc.channels.temp");
		Boolean perma = player.hasPermission("scc.channels.perma");
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) {
			try 
			{
				String sql = "INSERT INTO `" + tableNameI + "`(`player_uuid`, `player_name`,"
						+ " `can_chat`, `mutetime`,"
						+ " `channel_global`, `channel_trade`, `channel_auction`, `channel_support`,"
						+ " `channel_local`, `channel_world`,"
						+ " `channel_team`, `channel_admin`,"
						+ " `channel_group`, `channel_pm`, `channel_temp`, `channel_perma`, "
						+ " `channel_event`, `spy`, `joinmessage`) " 
						+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
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
		        preparedStatement.setBoolean(15, temp);
		        preparedStatement.setBoolean(16, perma);
		        preparedStatement.setBoolean(17, event);
		        preparedStatement.setBoolean(18, f);
		        preparedStatement.setBoolean(19, true);
		        
		        
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
		Connection conn = plugin.getMysqlSetup().getConnection();
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
	
	public boolean createChannel(PermanentChannel pc) 
	{
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) {
			try 
			{
				String sql = "INSERT INTO `" + tableNameIII 
						+ "`(`channel_name`, `creator`, `vice`, `members`, `password`, `banned`,"
						+ " `symbolextra`, `namecolor`, `chatcolor`)" 
						+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
				preparedStatement = conn.prepareStatement(sql);
		        preparedStatement.setString(1, pc.getName());
		        preparedStatement.setString(2, pc.getCreator());
		        preparedStatement.setString(3, String.join(";", pc.getVice()));
		        preparedStatement.setString(4, String.join(";", pc.getMembers()));
		        preparedStatement.setString(5, pc.getPassword());
		        preparedStatement.setString(6, String.join(";", pc.getBanned()));
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
	
	public boolean updateDataI(ProxiedPlayer player, Object object, String setcolumn) 
	{
		PreparedStatement preparedUpdateStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
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
		Connection conn = plugin.getMysqlSetup().getConnection();
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
	
	public boolean updateDataIII(PermanentChannel pc, Object object, String setcolumn) 
	{
		PreparedStatement preparedUpdateStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{
				String data = "UPDATE `" + tableNameIII 
						+ "` " + "SET `" + setcolumn + "` = ?" + " WHERE `id` = ?";
				preparedUpdateStatement = conn.prepareStatement(data);
				preparedUpdateStatement.setObject(1, object);
				preparedUpdateStatement.setInt(2, pc.getId());
				
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
	
	public Object getDataI(Object object, String selectcolumn, String wherecolumn)
	{
		PreparedStatement preparedUpdateStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `" + selectcolumn + "` FROM `" + tableNameI + "` WHERE `" + wherecolumn + "` = ? LIMIT 1";
		        preparedUpdateStatement = conn.prepareStatement(sql);
		        preparedUpdateStatement.setObject(1, object);
		        
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
		Connection conn = plugin.getMysqlSetup().getConnection();
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
	
	public PermanentChannel getDataIII(int id)
	{
		PreparedStatement preparedUpdateStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `*` FROM `" + tableNameIII + "` WHERE `id` = ? LIMIT 1";
		        preparedUpdateStatement = conn.prepareStatement(sql);
		        preparedUpdateStatement.setInt(1, id);
		        
		        result = preparedUpdateStatement.executeQuery();
		        while (result.next()) 
		        {
		        	PermanentChannel pc = new PermanentChannel(
		        			result.getInt("id"),
		        			result.getString("channel_name"), 
		        			result.getString("creator"), 
		        			new ArrayList<String>(Arrays.asList(result.getString("vice").split(";"))), 
		        			new ArrayList<String>(Arrays.asList(result.getString("members").split(";"))), 
		        			result.getString("password"), 
		        			new ArrayList<String>(Arrays.asList(result.getString("banned").split(";"))),
		        			result.getString("symbolextra"),
		        			result.getString("namecolor"),
		        			result.getString("chatcolor"));
		        	return pc;
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
		Connection conn = plugin.getMysqlSetup().getConnection();
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
		Connection conn = plugin.getMysqlSetup().getConnection();
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
		Connection conn = plugin.getMysqlSetup().getConnection();
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
	
	public void deleteDataIII(int id)
	{
		PreparedStatement preparedStatement = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		try 
		{
			String sql = "DELETE FROM `" + tableNameIII + "` WHERE `id` = ?";
			preparedStatement = conn.prepareStatement(sql);
			preparedStatement.setObject(1, id);
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
	
	public int getLastIDIII()
	{
		PreparedStatement preparedUpdateStatement = null;
		ResultSet result = null;
		Connection conn = plugin.getMysqlSetup().getConnection();
		if (conn != null) 
		{
			try 
			{			
				String sql = "SELECT `id` FROM `" + tableNameIII + "` ORDER BY `id` DESC LIMIT 1";
		        preparedUpdateStatement = conn.prepareStatement(sql);
		        
		        result = preparedUpdateStatement.executeQuery();
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
		    		  if (preparedUpdateStatement != null) 
		    		  {
		    			  preparedUpdateStatement.close();
		    		  }
		    	  } catch (Exception e) 
		    	  {
		    		  e.printStackTrace();
		    	  }
		      }
		}
		return 0;
	}
}*/