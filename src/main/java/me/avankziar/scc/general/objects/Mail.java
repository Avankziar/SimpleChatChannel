package main.java.me.avankziar.scc.general.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;

import main.java.me.avankziar.scc.general.database.MysqlBaseHandler;
import main.java.me.avankziar.scc.general.database.MysqlHandable;
import main.java.me.avankziar.scc.general.database.QueryType;

public class Mail implements MysqlHandable
{
	private int id;
	private String senderUUID;
	private String sender;
	private UUID reciverUUID;
	private String reciver;
	private String carbonCopyUUIDs; //split with @
	private String carbonCopyNames; //split with @
	private long sendedDate;
	private long readedDate;
	private String subject; //Betreff
	private String rawText;
	
	public Mail(){}
	
	public Mail(int id, String senderUUID, String sender, UUID reciverUUID, String reciver,
			String carbonCopyUUIDs, String carbonCopyNames,
			long sendedDate, long readedDate,
			String subject, String rawText)
	{
		setId(id);
		setSenderUUID(senderUUID);
		setSender(sender);
		setReciver(reciver);
		setReciverUUID(reciverUUID);
		setCarbonCopyUUIDs(carbonCopyUUIDs);
		setCarbonCopyNames(carbonCopyNames);
		setSendedDate(sendedDate);
		setReadedDate(readedDate);
		setSubject(subject);
		setRawText(rawText);
	}

	public String getSenderUUID()
	{
		return senderUUID;
	}

	public void setSenderUUID(String senderUUID)
	{
		this.senderUUID = senderUUID;
	}

	public String getSender()
	{
		return sender;
	}

	public void setSender(String sender)
	{
		this.sender = sender;
	}

	public UUID getReciverUUID()
	{
		return reciverUUID;
	}

	public void setReciverUUID(UUID reciverUUID)
	{
		this.reciverUUID = reciverUUID;
	}

	public String getReciver()
	{
		return reciver;
	}

	public void setReciver(String reciver)
	{
		this.reciver = reciver;
	}

	public String getCarbonCopyUUIDs()
	{
		return carbonCopyUUIDs;
	}

	public void setCarbonCopyUUIDs(String carbonCopyUUIDs)
	{
		this.carbonCopyUUIDs = carbonCopyUUIDs;
	}

	public String getCarbonCopyNames()
	{
		return carbonCopyNames;
	}

	public void setCarbonCopyNames(String carbonCopyNames)
	{
		this.carbonCopyNames = carbonCopyNames;
	}

	public long getSendedDate()
	{
		return sendedDate;
	}

	public void setSendedDate(long sendedDate)
	{
		this.sendedDate = sendedDate;
	}

	public long getReadedDate()
	{
		return readedDate;
	}

	public void setReadedDate(long readedDate)
	{
		this.readedDate = readedDate;
	}

	public String getRawText()
	{
		return rawText;
	}

	public void setRawText(String rawText)
	{
		this.rawText = rawText;
	}

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
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
					+ "`(`sender_uuid`, `sender_name`, `reciver_uuid`, `reciver_name`,"
					+ " `carboncopy_uuid`, `carboncopy_name`, `sendeddate`,"
					+ " `readeddate`, `subject`, `rawmessage`) " 
					+ "VALUES"
					+ "(?, ?, ?, ?, ?,"
					+ "?, ?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getSenderUUID());
	        ps.setString(2, getSender());
	        ps.setString(3, getReciverUUID().toString());
	        ps.setString(4, getReciver());
	        ps.setString(5, getCarbonCopyUUIDs());
	        ps.setString(6, getCarbonCopyNames());
	        ps.setLong(7, getSendedDate());
	        ps.setLong(8, getReadedDate());
	        ps.setString(9, getSubject());
	        ps.setString(10, getRawText());
	        
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
					+ "` SET `sender_uuid` = ?, `sender_name` = ?, `reciver_uuid` = ?, `reciver_name` = ?,"
					+ " `carboncopy_uuid` = ?, `carboncopy_name` = ?,"
					+ " `sendeddate` = ?, `readeddate` = ?, `subject`= ?, `rawmessage` = ?"
					+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getSenderUUID());
	        ps.setString(2, getSender());
	        ps.setString(3, getReciverUUID().toString());
	        ps.setString(4, getReciver());
	        ps.setString(5, getCarbonCopyUUIDs());
	        ps.setString(6, getCarbonCopyNames());
	        ps.setLong(7, getSendedDate());
	        ps.setLong(8, getReadedDate());
	        ps.setString(9, getSubject());
	        ps.setString(10, getRawText());
	        
	        int i = 11;
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
				al.add(new Mail(
	        			rs.getInt("id"),
	        			rs.getString("sender_uuid"),
	        			rs.getString("sender_name"),
	        			UUID.fromString(rs.getString("reciver_uuid")),
	        			rs.getString("reciver_name"),
	        			rs.getString("carboncopy_uuid"),
	        			rs.getString("carboncopy_name"),
	        			rs.getLong("sendeddate"),
	        			rs.getLong("readeddate"),
	        			rs.getString("subject"),
	        			rs.getString("rawmessage")));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(MysqlBaseHandler.getLogger(), Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<Mail> convert(ArrayList<Object> arrayList)
	{
		ArrayList<Mail> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof Mail)
			{
				l.add((Mail) o);
			}
		}
		return l;
	}
}