package main.java.me.avankziar.scc.objects;

public class ChatUser extends ServerLocation
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
	
	public ChatUser(String uuid, String name,
			String rolePlayName, long rolePlayRenameCooldown,
			long muteTime,
			boolean optionSpy, boolean optionChannelMessage,
			long lastTimeJoined,
			boolean optionJoinMessage, ServerLocation serverLocation)
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
}
