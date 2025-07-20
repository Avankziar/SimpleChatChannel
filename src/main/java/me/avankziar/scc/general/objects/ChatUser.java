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

public class ChatUser extends ServerLocation implements MysqlHandable
{
	private String uuid;
	private String name;
	private String rolePlayName;
	private long rolePlayRenameCooldown;
	private long muteTime;
	private boolean optionSpy;
	private boolean optionChannelMessage;
	private long lastTimeJoined;
	private boolean optionJoinMessage;
	private String mentionSound;
	private String mentionSoundCategory;
	private String userWritingLanguage;
	private ArrayList<String> userReadingLanguages = new ArrayList<>();
	
	public ChatUser() 
	{
		super(new ServerLocation("", "", 0, 0, 0));
	}
	
	public ChatUser(String uuid, String name,
			String rolePlayName, long rolePlayRenameCooldown,
			long muteTime,
			boolean optionSpy, boolean optionChannelMessage,
			long lastTimeJoined,
			boolean optionJoinMessage, ServerLocation serverLocation,
			String mentionSound, String mentionSoundCategory,
			String userWritingLanguage, ArrayList<String> userReadingLanguages)
	{
		super(serverLocation);
		setUUID(uuid);
		setName(name);
		setRolePlayName(rolePlayName);
		setRolePlayRenameCooldown(rolePlayRenameCooldown);
		setMuteTime(muteTime);
		setOptionSpy(optionSpy);
		setOptionChannelMessage(optionChannelMessage);
		setLastTimeJoined(lastTimeJoined);
		setOptionJoinMessage(optionJoinMessage);
		setMentionSound(mentionSound);
		setMentionSoundCategory(mentionSoundCategory);
		setUserWritingLanguage(userWritingLanguage);
		setUserReadingLanguages(userReadingLanguages);
	}

	public String getUUID()
	{
		return uuid;
	}

	public void setUUID(String uuid)
	{
		this.uuid = uuid;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public String getRolePlayName()
	{
		return rolePlayName;
	}

	public void setRolePlayName(String rolePlayName)
	{
		this.rolePlayName = rolePlayName;
	}

	public long getRolePlayRenameCooldown()
	{
		return rolePlayRenameCooldown;
	}

	public void setRolePlayRenameCooldown(long rolePlayRenameCooldown)
	{
		this.rolePlayRenameCooldown = rolePlayRenameCooldown;
	}

	public long getMuteTime()
	{
		return muteTime;
	}

	public void setMuteTime(long muteTime)
	{
		this.muteTime = muteTime;
	}

	public boolean isOptionSpy()
	{
		return optionSpy;
	}

	public void setOptionSpy(boolean optionSpy)
	{
		this.optionSpy = optionSpy;
	}

	public boolean isOptionChannelMessage()
	{
		return optionChannelMessage;
	}

	public void setOptionChannelMessage(boolean optionChannelMessage)
	{
		this.optionChannelMessage = optionChannelMessage;
	}

	public long getLastTimeJoined()
	{
		return lastTimeJoined;
	}

	public void setLastTimeJoined(long lastTimeJoined)
	{
		this.lastTimeJoined = lastTimeJoined;
	}

	public boolean isOptionJoinMessage()
	{
		return optionJoinMessage;
	}

	public void setOptionJoinMessage(boolean optionJoinMessage)
	{
		this.optionJoinMessage = optionJoinMessage;
	}
	
	public String getMentionSound()
	{
		return mentionSound;
	}

	public void setMentionSound(String mentionSound)
	{
		this.mentionSound = mentionSound;
	}

	public String getMentionSoundCategory()
	{
		return mentionSoundCategory;
	}

	public void setMentionSoundCategory(String mentionSoundCategory)
	{
		this.mentionSoundCategory = mentionSoundCategory;
	}

	public String getUserWritingLanguage()
	{
		return userWritingLanguage;
	}

	public void setUserWritingLanguage(String userWritingLanguage)
	{
		this.userWritingLanguage = userWritingLanguage;
	}

	public ArrayList<String> getUserReadingLanguages()
	{
		return userReadingLanguages;
	}

	public void setUserReadingLanguages(ArrayList<String> userReadingLanguages)
	{
		this.userReadingLanguages = userReadingLanguages;
	}

	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`player_uuid`, `player_name`, `roleplay_name`, `roleplayrenamecooldown`, `mutetime`,"
					+ " `spy`, `channelmessage`, `lasttimejoined`, `joinmessage`, `serverlocation`, `mention_sound`, `mention_sound_category`,"
					+ " `user_writing_language`, `user_reading_languages`) " 
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getUUID());
	        ps.setString(2, getName());
	        ps.setString(3, getRolePlayName());
	        ps.setLong(4, getRolePlayRenameCooldown());
	        ps.setLong(5, getMuteTime());
	        ps.setBoolean(6, isOptionSpy());
	        ps.setBoolean(7, isOptionChannelMessage());
	        ps.setLong(8, getLastTimeJoined());
	        ps.setBoolean(9, isOptionJoinMessage());
	        ps.setString(10, serialized());
	        ps.setString(11, getMentionSound());
	        ps.setString(12, getMentionSoundCategory());
	        ps.setString(13, getUserWritingLanguage());
	        ps.setString(14, String.join(";", getUserReadingLanguages()));
	        
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
					+ "` SET `player_uuid` = ?, `player_name` = ?, `roleplay_name` = ?, `roleplayrenamecooldown` = ?, `mutetime` = ?," 
					+ " `spy` = ?, `channelmessage` = ?, `lasttimejoined` = ?, `joinmessage` = ?, `serverlocation` = ?,"
					+ " `mention_sound` = ?, `mention_sound_category` = ?, `user_writing_language` = ?, `user_reading_languages` = ?" 
					+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, getUUID());
	        ps.setString(2, getName());
	        ps.setString(3, getRolePlayName());
	        ps.setLong(4, getRolePlayRenameCooldown());
	        ps.setLong(5, getMuteTime());
	        ps.setBoolean(6, isOptionSpy());
	        ps.setBoolean(7, isOptionChannelMessage());
	        ps.setLong(8, getLastTimeJoined());
	        ps.setBoolean(9, isOptionJoinMessage());
	        ps.setString(10, serialized());
	        ps.setString(11, getMentionSound());
	        ps.setString(12, getMentionSoundCategory());
	        ps.setString(13, getUserWritingLanguage());
	        ps.setString(14, String.join(";", getUserReadingLanguages()));
	        
			int i = 15;
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
				ArrayList<String> url = new ArrayList<>();
				for(String s : rs.getString("user_reading_languages").split(";"))
				{
					url.add(s);
				}
				al.add(new ChatUser(
	        			rs.getString("player_uuid"),
	        			rs.getString("player_name"),
	        			rs.getString("roleplay_name"),
	        			rs.getLong("roleplayrenamecooldown"),
	        			rs.getLong("mutetime"),
	        			rs.getBoolean("spy"),
	        			rs.getBoolean("channelmessage"),
	        			rs.getLong("lasttimejoined"),
	        			rs.getBoolean("joinmessage"),
	        			ServerLocation.deserialized(rs.getString("serverlocation")),
	        			rs.getString("mention_sound"),
	        			rs.getString("mention_sound_category"),
	        			rs.getString("user_writing_language"),
	        			url));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(MysqlBaseHandler.getLogger(), Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<ChatUser> convert(ArrayList<Object> arrayList)
	{
		ArrayList<ChatUser> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof ChatUser)
			{
				l.add((ChatUser) o);
			}
		}
		return l;
	}
}