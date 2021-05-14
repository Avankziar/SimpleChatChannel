package main.java.me.avankziar.scc.objects;

import java.util.UUID;

public class Mail
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
}