package main.java.me.avankziar.scc.general.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

import main.java.me.avankziar.scc.general.database.MysqlBaseHandler;
import main.java.me.avankziar.scc.general.database.MysqlHandable;
import main.java.me.avankziar.scc.general.database.QueryType;

public class UsedChannel implements MysqlHandable
{
	/**
	 * The unique identifier name.
	 */
	private String uniqueIdentifierName;
	
	private String playerUUID;
	
	private boolean used;
	
	public UsedChannel(){}
	
	public UsedChannel(String uniqueIdentifierName, String playerUUID, boolean used)
	{
		setUniqueIdentifierName(uniqueIdentifierName);
		setPlayerUUID(playerUUID);
		setUsed(used);
	}

	public String getUniqueIdentifierName()
	{
		return uniqueIdentifierName;
	}

	public void setUniqueIdentifierName(String uniqueIdentifierName)
	{
		this.uniqueIdentifierName = uniqueIdentifierName;
	}

	public String getPlayerUUID()
	{
		return playerUUID;
	}

	public void setPlayerUUID(String playerUUID)
	{
		this.playerUUID = playerUUID;
	}

	public boolean isUsed()
	{
		return used;
	}

	public void setUsed(boolean used)
	{
		this.used = used;
	}
	
	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`uniqueidentifiername`, `player_uuid`, `used`) " 
					+ "VALUES(?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getUniqueIdentifierName());
	        ps.setString(2, getPlayerUUID());
	        ps.setBoolean(3, isUsed());
	        
	        int i = ps.executeUpdate();
	        MysqlBaseHandler.addRows(QueryType.INSERT, i);
	        return true;
		} catch (SQLException e)
		{
			this.log(MysqlBaseHandler.getLogger(), Level.WARNING, "SQLException! Could not create a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return false;
	}

	@Override
	public boolean update(Connection conn, String tablename, String whereColumn, Object... whereObject)
	{
		try
		{
			String sql = "UPDATE `" + tablename
					+ "` SET `uniqueidentifiername` = ?, `player_uuid` = ?, `used` = ?"
					+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getUniqueIdentifierName());
	        ps.setString(2, getPlayerUUID());
	        ps.setBoolean(3, isUsed());
	        
	        int i = 4;
			for(Object o : whereObject)
			{
				ps.setObject(i, o);
				i++;
			}			
			int u = ps.executeUpdate();
			MysqlBaseHandler.addRows(QueryType.UPDATE, u);
			return true;
		} catch (SQLException e)
		{
			this.log(MysqlBaseHandler.getLogger(), Level.WARNING, "SQLException! Could not update a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return false;
	}

	@Override
	public ArrayList<Object> get(Connection conn, String tablename, String orderby, String limit, String whereColumn, Object... whereObject)
	{
		try
		{
			String sql = "SELECT * FROM `" + tablename
				+ "` WHERE "+whereColumn+" ORDER BY "+orderby+limit;
			PreparedStatement ps = conn.prepareStatement(sql);
			int i = 1;
			for(Object o : whereObject)
			{
				ps.setObject(i, o);
				i++;
			}
			
			ResultSet rs = ps.executeQuery();
			MysqlBaseHandler.addRows(QueryType.READ, rs.getMetaData().getColumnCount());
			ArrayList<Object> al = new ArrayList<>();
			while (rs.next()) 
			{
				al.add(new UsedChannel(
	        			rs.getString("uniqueidentifiername"),
	        			rs.getString("player_uuid"),
	        			rs.getBoolean("used")));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(MysqlBaseHandler.getLogger(), Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<UsedChannel> convert(ArrayList<Object> arrayList)
	{
		ArrayList<UsedChannel> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof UsedChannel)
			{
				l.add((UsedChannel) o);
			}
		}
		return l;
	}
}