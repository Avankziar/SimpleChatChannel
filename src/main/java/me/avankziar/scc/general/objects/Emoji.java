package main.java.me.avankziar.scc.general.objects;

public class Emoji
{
	private String identifierName;
	
	private String emoji;
	
	public Emoji(String identifierName, String emoji)
	{
		setIdentifierName(identifierName);
		setEmoji(emoji);
	}

	public String getEmoji()
	{
		return emoji;
	}

	public void setEmoji(String emoji)
	{
		this.emoji = emoji;
	}

	public String getIdentifierName()
	{
		return identifierName;
	}

	public void setIdentifierName(String identifiername)
	{
		this.identifierName = identifiername;
	}

}
