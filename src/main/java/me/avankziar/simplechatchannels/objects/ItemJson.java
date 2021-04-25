package main.java.me.avankziar.simplechatchannels.objects;

public class ItemJson
{
	private String owner;
	private String itemName;
	private String itemDisplayName;
	private String jsonString;
	private String base64Data;
	
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

}
