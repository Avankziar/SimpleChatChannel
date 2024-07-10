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

public class IgnoreObject implements MysqlHandable
{
	private String uuid;
	private String ignoreUUID;
	private String ignoreName;
	
	public IgnoreObject(){}
	
	public IgnoreObject(String uuid, String ignoreUUID, String ignoreName)
	{
		setUUID(uuid);
		setIgnoreUUID(ignoreUUID);
		setIgnoreName(ignoreName);
	}

	public String getUUID()
	{
		return uuid;
	}

	public void setUUID(String uuid)
	{
		this.uuid = uuid;
	}

	public String getIgnoreUUID()
	{
		return ignoreUUID;
	}

	public void setIgnoreUUID(String ignoreUUID)
	{
		this.ignoreUUID = ignoreUUID;
	}

	public String getIgnoreName()
	{
		return ignoreName;
	}

	public void setIgnoreName(String ignoreName)
	{
		this.ignoreName = ignoreName;
	}

	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`player_uuid`, `ignore_uuid`, `ignore_name`) " 
					+ "VALUES(?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getUUID());
	        ps.setString(2, getIgnoreUUID());
	        ps.setString(3, getIgnoreName());
	        
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
					+ "` SET `player_uuid` = ?, `ignore_uuid` = ?, `ignore_name` = ?" 
					+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getUUID());
	        ps.setString(2, getIgnoreUUID());
	        ps.setString(3, getIgnoreName());
	        
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
				al.add(new IgnoreObject(
	        			rs.getString("player_uuid"),
	        			rs.getString("ignore_uuid"),
	        			rs.getString("ignore_name")));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(MysqlBaseHandler.getLogger(), Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<IgnoreObject> convert(ArrayList<Object> arrayList)
	{
		ArrayList<IgnoreObject> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof IgnoreObject)
			{
				l.add((IgnoreObject) o);
			}
		}
		return l;
	}
}