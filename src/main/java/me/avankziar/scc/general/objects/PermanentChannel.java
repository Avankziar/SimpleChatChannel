package main.java.me.avankziar.scc.general.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;

import main.java.me.avankziar.scc.general.database.MysqlBaseHandler;
import main.java.me.avankziar.scc.general.database.MysqlHandable;
import main.java.me.avankziar.scc.general.database.QueryType;

public class PermanentChannel implements MysqlHandable
{
	private int id;
	private String name;
	private String creator;
	private ArrayList<String> vice = new ArrayList<String>();
	private ArrayList<String> members = new ArrayList<String>();
	private static ArrayList<PermanentChannel> permanentChannel = new ArrayList<PermanentChannel>();
	private String password;
	private String symbolExtra;
	private String nameColor;
	private String chatColor;
	private ArrayList<String> banned = new ArrayList<String>();
	
	public PermanentChannel(){}
	
	public PermanentChannel(int id, String name, String creator, ArrayList<String> vice,
			ArrayList<String> members, String password, ArrayList<String> banned,
			String symbolextra, String namecolor, String chatcolor)
	{
		setId(id);
		setName(name);
		setCreator(creator);
		setVice(vice);
		setMembers(members);
		setPassword(password);
		setBanned(banned);
		setSymbolExtra(symbolextra);
		setNameColor(namecolor);
		setChatColor(chatcolor);
	}
	
	public static PermanentChannel getChannelFromPlayer(String playeruuid)
	{
		PermanentChannel c = null;
		for (PermanentChannel pc : permanentChannel)
		{
			for(String p : pc.getMembers())
			{
				if(p.equals(playeruuid))
				{
					c = pc;
					break;
				}
			}
		}
		return c;
	}
	
	public static PermanentChannel getChannelFromName(String name)
	{
		PermanentChannel c = null;
		for (PermanentChannel cc : permanentChannel)
		{
			if(cc.getName().equals(name))
			{
				c = cc;
				break;
			}
		}
		return c;
	}
	
	public static PermanentChannel getChannelFromSymbol(String symbol)
	{
		PermanentChannel c = null;
		for (PermanentChannel cc : permanentChannel)
		{
			if(cc.getSymbolExtra().equals(symbol))
			{
				c = cc;
				break;
			}
		}
		return c;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getCreator()
	{
		return creator;
	}

	public void setCreator(String creator)
	{
		this.creator = creator;
	}

	public ArrayList<String> getVice()
	{
		return vice;
	}

	public void setVice(ArrayList<String> vice)
	{
		this.vice = vice;
	}

	public ArrayList<String> getMembers()
	{
		return members;
	}
	
	public void addVice(String player)
	{
		if(!vice.contains(player))
		{
			vice.add(player);
		}
	}
	
	public void removeVice(String player)
	{
		if(vice.contains(player))
		{
			vice.remove(player);
		}
	}

	public void setMembers(ArrayList<String> members)
	{
		this.members = members;
	}
	
	public void addMembers(String player)
	{
		if(!members.contains(player))
		{
			members.add(player);
		}
	}
	
	public void removeMembers(String player)
	{
		if(members.contains(player))
		{
			members.remove(player);
		}
	}

	public static ArrayList<PermanentChannel> getPermanentChannel()
	{
		return permanentChannel;
	}

	public static void setPermanentChannel(ArrayList<PermanentChannel> permanentChannel)
	{
		PermanentChannel.permanentChannel = permanentChannel;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public String getSymbolExtra()
	{
		return symbolExtra;
	}

	public void setSymbolExtra(String symbolExtra)
	{
		this.symbolExtra = symbolExtra;
	}

	public String getNameColor()
	{
		return nameColor;
	}

	public void setNameColor(String nameColor)
	{
		this.nameColor = nameColor;
	}

	public String getChatColor()
	{
		return chatColor;
	}

	public void setChatColor(String chatColor)
	{
		this.chatColor = chatColor;
	}

	public ArrayList<String> getBanned()
	{
		return banned;
	}

	public void setBanned(ArrayList<String> banned)
	{
		this.banned = banned;
	}
	
	public void addBanned(String player)
	{
		if(!banned.contains(player))
		{
			banned.add(player);
		}
	}
	
	public void removeBanned(String player)
	{
		if(banned.contains(player))
		{
			banned.remove(player);
		}
	}
	
	public static void addCustomChannel(PermanentChannel cc)
	{
		PermanentChannel r = null;
		for(PermanentChannel pc : permanentChannel)
		{
			if(pc.getId() == cc.getId())
			{
				r = pc;
			}
		}
		if(r != null)
		{
			permanentChannel.remove(r);
		}
		permanentChannel.add(cc);
	}
	
	public static void removeCustomChannel(PermanentChannel cc)
	{
		PermanentChannel r = null;
		for(PermanentChannel pc : permanentChannel)
		{
			if(pc.getId() == cc.getId())
			{
				r = pc;
			}
		}
		permanentChannel.remove(r);
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}
	
	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`channel_name`, `creator`, `vice`, `members`,"
					+ " `password`, `banned`, `symbolextra`, `namecolor`,"
					+ " `chatcolor`) " 
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			String vice = null;
			if(getVice() != null)
			{
				vice = String.join(";", getVice());
			}
			String members = null;
			if(getMembers() != null)
			{
				members = String.join(";", getMembers());
			}
			String banned = null;
			if(getBanned() != null)
			{
				banned = String.join(";", getBanned());
			}
			ps = conn.prepareStatement(sql);
			ps.setString(1, getName());
	        ps.setString(2, getCreator());
	        ps.setString(3, vice);
	        ps.setString(4, members);
	        ps.setString(5, getPassword());
	        ps.setString(6, banned);
	        ps.setString(7, getSymbolExtra());
	        ps.setString(8, getNameColor());
	        ps.setString(9, getChatColor());
	        
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
					+ "` SET `channel_name` = ?, `creator` = ?, `vice` = ?, `members` = ?," 
					+ " `password` = ?, `banned` = ?, `symbolextra` = ?, `namecolor` = ?," 
					+ " `chatcolor` = ?" 
					+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			String vice = null;
			if(getVice() != null)
			{
				vice = String.join(";", getVice());
			}
			String members = null;
			if(getMembers() != null)
			{
				members = String.join(";", getMembers());
			}
			String banned = null;
			if(getBanned() != null)
			{
				banned = String.join(";", getBanned());
			}
			ps = conn.prepareStatement(sql);
			ps.setString(1, getName());
	        ps.setString(2, getCreator());
	        ps.setString(3, vice);
	        ps.setString(4, members);
	        ps.setString(5, getPassword());
	        ps.setString(6, banned);
	        ps.setString(7, getSymbolExtra());
	        ps.setString(8, getNameColor());
	        ps.setString(9, getChatColor());
	        
	        int i = 10;
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
				ArrayList<String> vice = new ArrayList<>();
				if(rs.getString("vice") != null)
				{
					vice = new ArrayList<String>(Arrays.asList(rs.getString("vice").split(";")));
				}
				ArrayList<String> members = new ArrayList<>();
				if(rs.getString("members") != null)
				{
					members = new ArrayList<String>(Arrays.asList(rs.getString("members").split(";")));
				}
				ArrayList<String> banned = new ArrayList<>();
				if(rs.getString("banned") != null)
				{
					banned = new ArrayList<String>(Arrays.asList(rs.getString("banned").split(";")));
				}
				al.add(new PermanentChannel(
	        			rs.getInt("id"),
	        			rs.getString("channel_name"),
	        			rs.getString("creator"),
	        			vice,
	        			members,
	        			rs.getString("password"),
	        			banned,
	        			rs.getString("symbolextra"),
	        			rs.getString("namecolor"),
	        			rs.getString("chatcolor")));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(MysqlBaseHandler.getLogger(), Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<PermanentChannel> convert(ArrayList<Object> arrayList)
	{
		ArrayList<PermanentChannel> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof PermanentChannel)
			{
				l.add((PermanentChannel) o);
			}
		}
		return l;
	}
}