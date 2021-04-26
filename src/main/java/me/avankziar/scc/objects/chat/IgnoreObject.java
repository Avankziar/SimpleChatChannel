package main.java.me.avankziar.scc.objects.chat;

public class IgnoreObject
{
	private String uuid;
	private String ignoreUUID;
	private String ignoreName;
	
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

}
