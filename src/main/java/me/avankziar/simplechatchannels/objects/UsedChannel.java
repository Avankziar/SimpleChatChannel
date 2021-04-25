package main.java.me.avankziar.simplechatchannels.objects;

public class UsedChannel
{
	/**
	 * The unique identifier name.
	 */
	private String uniqueIdentifierName;
	
	private String playerUUID;
	
	private boolean used;
	
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

}
