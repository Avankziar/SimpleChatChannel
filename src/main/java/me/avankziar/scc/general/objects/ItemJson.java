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

public class ItemJson implements MysqlHandable
{
	private String owner;
	private String itemName;
	private String itemDisplayName;
	private String jsonString;
	private String base64Data;
	
	public ItemJson(){}
	
	public ItemJson(String owner, String itemName, String itemDisplayName, String jsonString,
			String base64Data)
	{
		setOwner(owner);
		setItemName(itemName);
		setItemDisplayName(itemDisplayName);
		setJsonString(jsonString);
		setBase64Data(base64Data);
	}

	public String getOwner()
	{
		return owner;
	}

	public void setOwner(String owner)
	{
		this.owner = owner;
	}

	public String getItemName()
	{
		return itemName;
	}

	public void setItemName(String itemName)
	{
		this.itemName = itemName;
	}

	public String getItemDisplayName()
	{
		return itemDisplayName;
	}

	public void setItemDisplayName(String itemDisplayName)
	{
		this.itemDisplayName = itemDisplayName;
	}

	public String getJsonString()
	{
		return jsonString;
	}

	public void setJsonString(String jsonString)
	{
		this.jsonString = jsonString;
	}

	public String getBase64Data()
	{
		return base64Data;
	}

	public void setBase64Data(String base64Data)
	{
		this.base64Data = base64Data;
	}
	
	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`owner`, `itemname`, `itemdisplayname`, `jsonstring`, `base64`) " 
					+ "VALUES(?, ?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getOwner());
	        ps.setString(2, getItemName());
	        ps.setString(3, getItemDisplayName());
	        ps.setString(4, getJsonString());
	        ps.setString(5, getBase64Data());
	        
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
					+ "` SET `owner` = ?, `itemname` = ?, `itemdisplayname` = ?, `jsonstring` = ?, `base64` = ?"
					+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getOwner());
	        ps.setString(2, getItemName());
	        ps.setString(3, getItemDisplayName());
	        ps.setString(4, getJsonString());
	        ps.setString(5, getBase64Data());
	        
	        int i = 6;
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
				al.add(new ItemJson(
	        			rs.getString("owner"),
	        			rs.getString("itemname"),
	        			rs.getString("itemdisplayname"),
	        			rs.getString("jsonstring"),
	        			rs.getString("base64")));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(MysqlBaseHandler.getLogger(), Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<ItemJson> convert(ArrayList<Object> arrayList)
	{
		ArrayList<ItemJson> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof ItemJson)
			{
				l.add((ItemJson) o);
			}
		}
		return l;
	}
}
